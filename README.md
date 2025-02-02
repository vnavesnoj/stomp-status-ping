# Stomp Status Ping Service
The service is designed to track user statuses (`ONLINE` / `OFFLINE`). 
The service is horizontally scalable.

### How to use?
* Users communicate with the service via a websocket
client based on the STOMP protocol.
* Default connection endpoint: `/stomp`
* [schema.txt](schema.txt) - schema with basic commands.
* [src/main/resources/application.yaml](src/main/resources/application.yaml) - application properties to configure 
(example: from environment variables).

### Requirements
* Redis.
* Message broker with a STOMP protocol support.
* Authentication service.

### Local-build
* `docker compose -f ./docker-compose-localbuild.yaml up` - 
run the application with a test page and with:
  - test local authentication service;
  - local Redis;
  - local RabbitMq.
* `/test/stomp-js-client` - default testing page endpoint.
* `/test/stomp-js-client-with-cookie` - default testing page endpoint 
with cookie authentication. `?cookie=cookieName=cookieValue` 
for setting dynamic cookie from the param.
* [src/main/resources/tokens.properties](src/main/resources/tokens.properties) -
test tokens with their owners.