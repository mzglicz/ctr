use ctr
// Don't know why this is not working
print("Switched to ctr")

show dbs

print("Going to create colletions")
db.createCollection("links")
db.createCollection("clicks")
show collections

db.links.createIndex({
    "relativeUrl": 1
}, {
    unique: true
}
)
db.clicks.createIndex({ "elementId":1 }, { unique: true})
print("Created collections")

print(db.links.getIndexes())
print(db.clicks.getIndexes())