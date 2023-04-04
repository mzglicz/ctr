import requests
import random

# pip3 install requests
r = requests.get("http://localhost:8080/admin/links")
items = r.json()

print(items)
for x in items:
    k = random.randrange(5)
    for i in range(0,k):
        id = x["id"]
        url = x["url"]
        print(f"Visiting {id} time: {i+1}")
        r = requests.get(url)
        if r.status_code is not 200:
            print(f"Something went wrong for {id} {url} got {r.status_code}")

