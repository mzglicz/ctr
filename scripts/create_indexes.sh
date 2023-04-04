# should work from dockercompose and the dockerinitdb folder but for some reason does not:
docker exec -it docker-mongo-1 /bin/bash 'mongosh -u $MONGO_INITDB_ROOT_USERNAME -p $MONGO_INITDB_ROOT_PASSWORD --file /docker-entrypoint-initdb.d/init-mongo.js'
