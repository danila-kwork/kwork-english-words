import os

import requests
import json
from bs4 import BeautifulSoup

url = 'https://online-london.com/blog/lexis/1000-slov-angliyskogo-yazyka/'
filePath = 'words.json'

response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

words = []

tr_items = soup.findAll('tr')

for tr in tr_items:
    td_items = tr.find_all_next('td')
    word_en = td_items[1].text.replace(' ', '').replace('\n', '').replace('\r', '').replace('\t', '')
    word_ru = td_items[2].text.replace(' ', '').replace('\n', '').replace('\r', '').replace('\t', '')
    print(f'{word_en} - {word_ru}')
    words.append({'word_en': word_en, 'word_ru': word_ru})


if os.path.exists(filePath):
    os.remove(filePath)

with open(filePath, 'x') as fp:
    json.dump(words, fp)
