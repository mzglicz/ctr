import requests
import random

# pip3 install requests
r = requests.get("http://localhost:8080/admin/links?limit=100")
response = r.json()
print(response)
items = response["items"]
print(items)
for x in items:
    k = random.randrange(5)
    for i in range(0,k):
        id = x["id"]
        url = x["url"]
        print(f"Visiting {id} time: {i+1}")
        r = requests.get(url)
        if r.status_code != 200:
            print(f"Something went wrong for {id} {url} got {r.status_code}")

