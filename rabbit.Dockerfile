FROM rabbitmq:3.13.1-management-alpine
RUN rabbitmq-plugins enable rabbitmq_stomp
EXPOSE 5672 15672 61613 15674
