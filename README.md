# Notification Service - Backend

Microserviço de notificação baseado em eventos com RabbitMQ para integração com outras aplicações.

## 📁 Estrutura de Diretórios

```
notification-service/
├── 1-DTO-Request/
│   ├── PublishMessageRequest.java
│   └── MessageMetadataRequest.java
├── 2-DTO-Response/
│   ├── PublishMessageResponse.java
│   ├── ErrorResponse.java
│   ├── HealthCheckResponse.java
│   └── MessageReceivedResponse.java
├── 3-Controller/
│   └── NotificationController.java
├── 4-Service/
│   └── NotificationService.java
├── 5-Config/
│   ├── RabbitMQConfig.java
│   └── CustomRabbitTemplateConfig.java
├── 6-Event/
│   └── MessageEventListener.java
├── 7-Consumer/
│   ├── AbstractNotificationConsumer.java
│   └── NotificationConsumer.java
├── 8-Exception/
│   ├── NotificationException.java
│   └── InvalidQueueException.java
├── 9-Application/
│   └── NotificationServiceApplication.java
├── 10-Resources/
│   ├── application.yml
│   └── application-docker.yml
├── 11-Maven/
│   └── pom.xml
└── 12-Docker/
    └── docker-compose.yml
```

## 🚀 Como Usar

### 1. Clonar e Estruturar o Projeto

```bash
# Criar estrutura de diretórios do Spring Boot
mkdir -p notification-service/src/main/java/com/cesaraugusto/notification/{config,controller,service,dto/{request,response},event,consumer,exception}
mkdir -p notification-service/src/main/resources
mkdir -p notification-service/src/test

# Copiar os arquivos para seus respectivos pacotes
cp 1-DTO-Request/* notification-service/src/main/java/com/cesaraugusto/notification/dto/request/
cp 2-DTO-Response/* notification-service/src/main/java/com/cesaraugusto/notification/dto/response/
cp 3-Controller/* notification-service/src/main/java/com/cesaraugusto/notification/controller/
cp 4-Service/* notification-service/src/main/java/com/cesaraugusto/notification/service/
cp 5-Config/* notification-service/src/main/java/com/cesaraugusto/notification/config/
cp 6-Event/* notification-service/src/main/java/com/cesaraugusto/notification/event/
cp 7-Consumer/* notification-service/src/main/java/com/cesaraugusto/notification/consumer/
cp 8-Exception/* notification-service/src/main/java/com/cesaraugusto/notification/exception/
cp 9-Application/* notification-service/src/main/java/com/cesaraugusto/notification/
cp 10-Resources/* notification-service/src/main/resources/
cp 11-Maven/pom.xml notification-service/
cp 12-Docker/docker-compose.yml notification-service/
```

### 2. Iniciar o RabbitMQ

```bash
cd notification-service
docker-compose up -d
```

### 3. Compilar e Rodar a Aplicação

```bash
mvn clean install
mvn spring-boot:run
```

Ou com Docker:

```bash
mvn spring-boot:build-image
docker run -p 8080:8080 --network notification-service_default notification-service:1.0.0
```

## 🔌 Endpoints Disponíveis

### 1. Publicar Mensagem
**POST** `/api/v1/notifications/publish`

**Request Body:**
```json
{
  "exchange": "notification-exchange",
  "routingKey": "notification.email",
  "message": "Sua mensagem aqui",
  "contentType": "application/json"
}
```

**Ou direto em uma fila:**
```json
{
  "queue": "notification-queue",
  "message": "Sua mensagem aqui"
}
```

**Response (Success):**
```json
{
  "messageId": "550e8400-e29b-41d4-a716-446655440000",
  "status": "SUCCESS",
  "message": "Mensagem publicada com sucesso",
  "exchange": "notification-exchange",
  "routingKey": "notification.email",
  "timestamp": "2026-02-25T10:30:45"
}
```

### 2. Health Check
**GET** `/api/v1/notifications/health`

**Response:**
```json
{
  "status": "UP",
  "message": "Serviço operacional"
}
```

### 3. Informações do Serviço
**GET** `/api/v1/notifications/info`

**Response:**
```json
{
  "service": "Notification Service",
  "version": "1.0.0",
  "description": "Microserviço de notificação baseado em eventos RabbitMQ"
}
```

## 🐰 Acessar RabbitMQ Management

```
URL: http://localhost:15672
Username: guest
Password: guest
```

## 📤 Testar com cURL

### Publicar em Exchange
```bash
curl -X POST http://localhost:8080/api/v1/notifications/publish \
  -H "Content-Type: application/json" \
  -d '{
    "exchange": "notification-exchange",
    "routingKey": "notification.email",
    "message": "Email de boas-vindas enviado"
  }'
```

### Publicar em Queue
```bash
curl -X POST http://localhost:8080/api/v1/notifications/publish \
  -H "Content-Type: application/json" \
  -d '{
    "queue": "notification-queue",
    "message": "Notificação simples"
  }'
```

### Health Check
```bash
curl http://localhost:8080/api/v1/notifications/health
```

## 📚 Usar o Consumer em Outro Microserviço

### Passo 1: Copiar a Classe Abstrata
Copie apenas `AbstractNotificationConsumer.java` para seu microserviço:

```
src/main/java/com/yourcompany/notification/consumer/AbstractNotificationConsumer.java
```

### Passo 2: Implementar um Consumer Customizado

```java
package com.yourcompany.order.consumer;

import com.cesaraugusto.notification.consumer.AbstractNotificationConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderNotificationConsumer extends AbstractNotificationConsumer {

    @RabbitListener(queues = "notification-queue")
    public void handleOrderNotification(String message) {
        consumeMessage(message, "notification-queue");
    }

    @Override
    public void handleMessage(String message) throws Exception {
        log.info("Processando notificação de pedido: {}", message);
        
        // Sua lógica de negócio aqui
        // Exemplo: enviar email, atualizar banco de dados, etc
    }

    @Override
    protected void handleError(String message, Exception e) {
        log.error("Erro ao processar notificação: {}", e.getMessage());
        // Sua lógica de tratamento de erro aqui
    }
}
```

### Passo 3: Adicionar Dependência AMQP

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### Passo 4: Configurar RabbitMQ no application.yml

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
```

## 🔄 Fluxo de Mensagens

1. **Frontend Angular** → Formulário com Exchange/Queue e Mensagem
2. **HTTP POST** → NotificationController
3. **NotificationService** → Publica em RabbitMQ
4. **RabbitMQ** → Roteia/Armazena
5. **MessageEventListener** → Consume na origem
6. **Outros Microserviços** → Consomem via AbstractNotificationConsumer

## ⚙️ Exchanges e Filas Pré-configuradas

| Nome | Tipo | Pattern |
|------|------|---------|
| notification-exchange | Topic | notification.* |
| event-exchange | Direct | event |
| notification-queue | - | Vinculada ao notification-exchange |
| event-queue | - | Vinculada ao event-exchange |
| notification-dlq | - | Dead Letter Queue |

## 🔐 Logs

Os logs estão configurados em `DEBUG` para desenvolvimento e `INFO` para produção.

```
2026-02-25 10:30:45 - Mensagem publicada no exchange: notification-exchange com routing key: notification.email
2026-02-25 10:30:46 - Mensagem recebida na fila notification-queue: Email de boas-vindas
```

## 📝 Notas Importantes

- **CORS**: Habilitado para `*` no controller (restringir em produção)
- **RabbitMQ**: Certificar que está rodando antes de iniciar a aplicação
- **Java**: Versão 17+
- **Spring Boot**: Versão 3.2.0+

## 🚨 Troubleshooting

### Erro: "Unable to connect to RabbitMQ"
- Verificar se RabbitMQ está rodando: `docker-compose up -d`
- Verificar firewall na porta 5672

### Erro: "Queue not found"
- Executar a aplicação uma vez para criar as filas automaticamente
- Acessar http://localhost:15672 para verificar

### Consumer não recebe mensagens
- Verificar se o `@RabbitListener` tem o nome correto da fila
- Verificar se o Consumer está marcado com `@Component`

## 📄 Licença

© 2026 César Augusto Vieira Bezerra. Todos os direitos reservados.
