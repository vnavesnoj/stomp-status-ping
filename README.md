# Stomp Status Ping Service
Сервис предназачен для слежения за статусами (`ONLINE` / `OFFLINE`) 
пользователей.

### Как пользоваться?
* Общение пользователей с сервисом приосходит через SockJs 
клиент на базе протокола STOMP.
* Эндпоинт подключения: `/stomp`
* Подключение к сервису происходит подобным образом:
```
var socket = new SockJS('http://service-domen/stomp');
stompClient = Stomp.over(socket);
var headers = {token: credential-token};
stompClient.connect(headers, onConnected, onError);
```
* После успешного подключения и аутентификации приходит 
сообщение об успешном подключении типа `CONNECTED`.
* Чтобы подписаться на изменения статуса определенного 
пользователя, нужно отправить запрос с `destination: topic/status`
и с `id: никнейм-пользователя`. Например:
```
var headers = {id: 'target-user'}
stompClient.subscribe('/topic/status', function (message) {
    console.log('Received message: ' + message.body);
}, headers);
```
* После успешной подписки приходит сообщение `MESSAGE` с
payload типа json о текущем статусе пользователя:
```
{
    "username":"targer-user",
    "status":"OFFLINE",
    "timestamp":1733951322660
}
```
* Такой же тип сообщения будет приходит в дальнейшем при любом
изменении статуса пользователя, на которого подписались.
* Пользователь считается `ONLINE`, когда установленно хотя бы одно 
аутентифицированное WebSocket соединение.
* Пользователь считается `OFFLINE`, когда никаких WebSocket 
соединений с ним не установленно.
* Так же поддерживаются ряд других типов сообщений, представленых
в спецификации STOMP, такие как `UNSUBSCRIBE`, `HEARTBEAT`, `DISCONNECT`
и т.д.

### Установка образа

* С помощью докера командами:
`docker build -t vnavesnoj/stomp-status-ping:latest .`
* С помощью Buildpacks: `./gradlew bootBuildImage`

### Properties
* Все настраиваемые свойства приложения представлены в [src/main/resources/application.yaml](src/main/resources/application.yaml) параметрах.
* Например, `${REDIS_HOST:localhost}` означает, что свойство для параметра `spring.data.redis.host` будет браться
от переменной окружения `REDIS_HOST`. Если переменная будет отсутствовать,
то будет браться дефолтное свойство `localhost`.
* Переменная окружения типа `spring.data.redis.host` тоже
устанавливает соответствующий параметр в [src/main/resources/application.yaml](src/main/resources/application.yaml)

### Требования
* Подключение к Redis. Параметры подключения находятся в [src/main/resources/application.yaml](src/main/resources/application.yaml)
`spring.data.redis`
* Доступ к серверу аутентификации по токену. Параметры доступа находятся в [src/main/resources/application.yaml](src/main/resources/application.yaml) 
`app.webclient.auth`