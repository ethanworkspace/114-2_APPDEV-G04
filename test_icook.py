import urllib.request
import re

try:
    req = urllib.request.Request('https://icook.tw/search/%E9%AE%AD%E9%AD%9A/', headers={'User-Agent': 'Mozilla/5.0'})
    html = urllib.request.urlopen(req).read().decode('utf-8')
    cards = re.findall(r'class=\"browse-recipe-card\".*?href=\"(.*?)\".*?class=\"browse-recipe-name\".*?>(.*?)<', html, re.S)
    print("Found:", len(cards))
    for i in range(min(5, len(cards))):
        print(cards[i][0].strip(), cards[i][1].strip())
except Exception as e:
    print(e)
