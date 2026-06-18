package com.fishmarket.app.data.model;

import java.util.Arrays;
import java.util.List;

public class MarketLocation {

    private final String marketNo;
    private final String marketName;
    private final double latitude;
    private final double longitude;
    private final String address;

    public MarketLocation(String marketNo, String marketName, double latitude, double longitude, String address) {
        this.marketNo = marketNo;
        this.marketName = marketName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public String getMarketNo() { return marketNo; }
    public String getMarketName() { return marketName; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getAddress() { return address; }

    public static List<MarketLocation> getAllMarkets() {
        return Arrays.asList(
                new MarketLocation("F109", "台北", 25.0268, 121.5063, "台北市萬華區萬大路533號"),
                new MarketLocation("F241", "三重", 25.0598, 121.4872, "新北市三重區大同北路98號"),
                new MarketLocation("F200", "基隆", 25.1339, 121.7415, "基隆市中正區正濱路116號"),
                new MarketLocation("F261", "頭城", 24.8592, 121.8233, "宜蘭縣頭城鎮港口路101號"),
                new MarketLocation("F330", "桃園", 25.0000, 121.3000, "桃園市桃園區三民路三段381號"),
                new MarketLocation("F300", "新竹", 24.8073, 120.9731, "新竹市北區南寮街69號"),
                new MarketLocation("F360", "苗栗", 24.5736, 120.8168, "苗栗縣竹南鎮龍山路一段480號"),
                new MarketLocation("F400", "台中", 24.2641, 120.5171, "台中市梧棲區大智路二段600號"),
                new MarketLocation("F500", "彰化", 24.0518, 120.5337, "彰化縣彰化市中央路178號"),
                new MarketLocation("F513", "埔心", 23.9530, 120.5440, "彰化縣埔心鄉員鹿路二段451號"),
                new MarketLocation("F630", "斗南", 23.6795, 120.4792, "雲林縣斗南鎮延平路四段298號"),
                new MarketLocation("F600", "嘉義", 23.4590, 120.3100, "嘉義市西區北興街1號"),
                new MarketLocation("F730", "新營", 23.3103, 120.3268, "台南市新營區民治路36號"),
                new MarketLocation("F708", "台南", 22.9972, 120.2126, "台南市南區中華南路二段72號"),
                new MarketLocation("F722", "佳里", 23.1666, 120.1834, "台南市佳里區安西路410號"),
                new MarketLocation("F709", "興達港", 22.8637, 120.2140, "高雄市茄萣區大發路88號"),
                new MarketLocation("F800", "高雄", 22.6129, 120.2856, "高雄市前鎮區漁港南三路1號"),
                new MarketLocation("F820", "岡山", 22.7955, 120.2947, "高雄市岡山區岡山路60號"),
                new MarketLocation("F916", "東港", 22.4627, 120.4509, "屏東縣東港鎮新生三路191號"),
                new MarketLocation("F936", "新港", 23.5567, 121.5000, "花蓮縣新城鄉新港路"),
                new MarketLocation("F950", "花蓮", 23.9706, 121.6080, "花蓮縣花蓮市海濱路25號")
        );
    }
}
