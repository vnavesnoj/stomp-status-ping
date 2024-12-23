# Stomp Status Ping Service
The service is designed to track user statuses (`ONLINE` / `OFFLINE`). 
The service is horizontally scalable.

### How to use?
* Users communicate with the service via a websocket
client based on the STOMP protocol.
* Default connection endpoint: `/stomp`
* Schema with basic commands: [schema.txt](schema.txt)

### Requirements
* Redis.
* Message broker with a STOMP protocol support.
* Authentication service.

### Local-build
* `docker-compose-localbuild.yaml` - run the application with a test page and with:
  - test local authentication service;
  - local Redis;
  - local RabbitMq.
* `/test/stomp-js-client` - default testing page endpoint
* [src/main/resources/tokens.properties](src/main/resources/tokens.properties) -
test tokens with their owners.