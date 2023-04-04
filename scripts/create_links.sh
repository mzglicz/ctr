#!/bin/sh

COUNT=$1
for (( i=1; i<=$COUNT; i++))
do
  BODY="{\"target\":\"https://www.nba.com?q=${i}\" }"
  curl -vv -X POST http://localhost:8080/admin/links -H 'Content-type: application/json' -d "$BODY"
done