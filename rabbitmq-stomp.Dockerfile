FROM rabbitmq:4-management-alpine
RUN rabbitmq-plugins enable rabbitmq_stomp --offline
EXPOSE 5672 15672 61613