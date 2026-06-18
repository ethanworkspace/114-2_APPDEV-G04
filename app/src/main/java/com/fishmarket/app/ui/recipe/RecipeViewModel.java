package com.fishmarket.app.ui.recipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.fishmarket.app.data.model.Recipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeViewModel extends ViewModel {

    private final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LiveData<List<Recipe>> getRecipes() { return recipes; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void searchRecipes(String query) {
        if (query == null || query.trim().isEmpty()) return;
        loading.setValue(true);

        executor.execute(() -> {
            try {
                String encodedQuery = URLEncoder.encode(query.trim(), "UTF-8");
                String url = "https://icook.tw/search/" + encodedQuery + "/";
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .get();

                Element scriptLdJson = doc.selectFirst("script[type=application/ld+json]");
                List<Recipe> results = new ArrayList<>();

                if (scriptLdJson != null) {
                    String jsonStr = scriptLdJson.html();
                    JsonObject root = JsonParser.parseString(jsonStr).getAsJsonObject();
                    if (root.has("@graph")) {
                        JsonArray graph = root.getAsJsonArray("@graph");
                        for (JsonElement elem : graph) {
                            JsonObject obj = elem.getAsJsonObject();
                            if (obj.has("@type") && "ItemList".equals(obj.get("@type").getAsString())) {
                                JsonArray items = obj.getAsJsonArray("itemListElement");
                                for (JsonElement itemElem : items) {
                                    JsonObject item = itemElem.getAsJsonObject();
                                    String name = item.has("name") ? item.get("name").getAsString() : "";
                                    String description = item.has("description") ? item.get("description").getAsString() : "";
                                    String imageUrl = item.has("image") ? item.get("image").getAsString() : "";
                                    String recipeUrl = item.has("url") ? item.get("url").getAsString() : "";
                                    results.add(new Recipe(name, description, imageUrl, recipeUrl));
                                }
                                break;
                            }
                        }
                    }
                }
                recipes.postValue(results);
            } catch (Exception e) {
                error.postValue("取得食譜失敗: " + e.getMessage());
            } finally {
                loading.postValue(false);
            }
        });
    }
}
