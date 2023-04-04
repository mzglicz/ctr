Simple CTR application. 

Allows you to create links in the database
Once user clicks on the link they will be redirected to the specified target
And the interation is going to be logged (should be sent to kafka)
And stored in different collection

Structure
├── docker
    - files for docker compose - init-mongo.js does not currently work hence one needs to create
    indexes manually (docker exec + mongosh)
├── src
    - java code + tests, test I'd prefer test in groovy - did not get to it.
├── scripts
    - helper scripts, to get some data in 

├── gradle
    - gradle wrapper
└── README.md

## Getting started

### Tests
`./gradlew test`

### Running locally from Intellij

Run `docker-compose -f docker/docker-compose.yml up`
Set your profile to local (assuming nothing changes in the docker-compose file - credentials)
aka `-Dspring.profiles.active=local`

### Running inside of docker with prometheus
* Build docker container. This can be done with:
`./scripts/build-docker.sh` - this basically `docker build -f scripts/Dockerfile . -t maciek/ctr` just to make sure name of the image is consitent
* Run `docker-compose -f docker/docker-compose-full.yml up`

### Generating some data
Run `./scripts/create_links.sh <count>` - this will create <count> links in the db
Run `./scripts/traverse_in_parallel.sh` - (this requires python3 to be installed and `requests` to be installed `pip3 install requests`)

### Functionality
To see exposed endpoints see:
http://localhost:8080/swagger-ui/index.html

Basically they are divided into 3 categories:
* `/admin/links` - these are used to create, update etc links.
  In production setting should be secured and not exposed to the outside world
* `/links` - endpoint used for redirecting to desired target and capturing the interaction
* `/stats` - endpoints to get some status from the db (like top 10 most popular clicks).  Similarly this functionality should not be exposed to the public

### Metrics 
Metrics are exposed via `actuator`. To see what is collected (assuming running dockerised version with prometheus)
Follow this [url](http://localhost:9090/graph?g0.expr=%7Bjob%3D%22service%22%7D&g0.tab=1&g0.stacked=0&g0.show_exemplars=0&g0.range_input=1h)

To access grafana [url](http://localhost:3000) - credentials are in `docker/docker-compose-full.yml`
Currently one needs to add the prometheus datasource.
Go to [datasource](http://localhost:3000/datasources/new) and select prometheus and input `http://prometheus:9090`
Then one can use grafana.