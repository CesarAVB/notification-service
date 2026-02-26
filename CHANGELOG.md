## [1.5.1](https://github.com/CesarAVB/notification-service/compare/v1.5.0...v1.5.1) (2026-02-26)

### Bug Fixes

* **notification:** Remove unnecessary line breaks in NotificationService ([165fb27](https://github.com/CesarAVB/notification-service/commit/165fb27d72374b42a3eb5641f11c2859d139687f))

## [1.5.0](https://github.com/CesarAVB/notification-service/compare/v1.4.0...v1.5.0) (2026-02-25)

### Features

* **changelog:** Adiciona versão 1.4.0 com novas funcionalidades de ([523aa84](https://github.com/CesarAVB/notification-service/commit/523aa8433d06a735fb8d7e949ba4a08310b38c79))
* **config:** Adiciona configuração de datasource e JPA/Hibernate para ([17cc3a3](https://github.com/CesarAVB/notification-service/commit/17cc3a38c7cc127496f913b9213c3a8fdcedba36))
* **dependencies:** Adiciona Spring Boot Starter para JPA no pom.xml ([d4b3afc](https://github.com/CesarAVB/notification-service/commit/d4b3afc2b4d6b708d9f82a0b4fec2093b91fbb27))
* **notification:** Adiciona repositório para gerenciamento de ([c377c28](https://github.com/CesarAVB/notification-service/commit/c377c280b6c235b4d85bebcd480c305040061295))
* **notification:** Atualiza método para retornar histórico paginado de ([ea69a0c](https://github.com/CesarAVB/notification-service/commit/ea69a0c71d78fb96bd938998aa2fe9b99a04cb76))
* **notification:** Cria entidade NotificationMessage para gerenciamento ([02cae6d](https://github.com/CesarAVB/notification-service/commit/02cae6d5581bc45561fc8553876fb6d3445f3e21))
* **notification:** Implementa persistência e recuperação paginada de ([9c67dd3](https://github.com/CesarAVB/notification-service/commit/9c67dd39ef2d96a5ecb66925fc00a98efbdb68e8))

## [1.4.0](https://github.com/CesarAVB/notification-service/compare/v1.3.0...v1.4.0) (2026-02-25)

### Features

* **notification:** Implementa gerenciamento dinâmico de filas e ([66cbe1f](https://github.com/CesarAVB/notification-service/commit/66cbe1feb7af9b1610d9b957cfdd7f255df58a8e))
* **rabbitmq:** Adiciona configuração do RabbitAdmin para gerenciamento ([51dad09](https://github.com/CesarAVB/notification-service/commit/51dad09158399591a7092691a4d754ae4793470a))

## [1.3.0](https://github.com/CesarAVB/notification-service/compare/v1.2.0...v1.3.0) (2026-02-25)

### Features

* **docker:** Adiciona Dockerfile para construção e execução da ([1ba4c1d](https://github.com/CesarAVB/notification-service/commit/1ba4c1df4eadfb3a44cfb85c0a77b98e514d028b))

## [1.2.0](https://github.com/CesarAVB/notification-service/compare/v1.1.0...v1.2.0) (2026-02-25)

### Features

* **exception:** Adiciona GlobalExceptionHandler para tratamento ([b4c0f85](https://github.com/CesarAVB/notification-service/commit/b4c0f85df3b17ecc80250f6e30222edc186e5e1e))
* **notification:** Adiciona a classe ExceptionResponse para padronizar ([a35630f](https://github.com/CesarAVB/notification-service/commit/a35630fb8608ec76da6fea150a438354b5c766a2))
* **notification:** Adiciona histórico recente de notificações ao ([8a7e0e3](https://github.com/CesarAVB/notification-service/commit/8a7e0e306c4b7ea074c8e382c7d6397d722d35c7))

## [1.1.0](https://github.com/CesarAVB/notification-service/compare/v1.0.0...v1.1.0) (2026-02-25)

### Features

* **configuration:** Adiciona arquivos de configuração para RabbitMQ, ([99c2c55](https://github.com/CesarAVB/notification-service/commit/99c2c55ca53618c86b469f7d987f35cf4cd75a76))
* **configuration:** Adiciona configuração CORS para permitir ([75cac57](https://github.com/CesarAVB/notification-service/commit/75cac572659cdd80507c912d6cfab3eb75d46084))

## 1.0.0 (2026-02-25)

### Features

* **notification:** add DTOs for message publishing and health check ([717643d](https://github.com/CesarAVB/notification-service/commit/717643d01d9674eb7c509e2b1c10740db98e9803))
* **notification:** Adiciona AbstractNotificationConsumer e ([b25d604](https://github.com/CesarAVB/notification-service/commit/b25d60405d062905302493baca24802b5a9b90db))
* **notification:** Adiciona configuração personalizada do ([9656cbb](https://github.com/CesarAVB/notification-service/commit/9656cbb9b764df42f6be4e555b7f12785973ad0c))
* **notification:** Adiciona exceções customizadas para notificações e ([d4a03ee](https://github.com/CesarAVB/notification-service/commit/d4a03eea49aa36263601b7b4098e22ddc40a2d09))
* **notification:** Adiciona MessageEventListener para escuta de filas ([27e641e](https://github.com/CesarAVB/notification-service/commit/27e641e6616bc060d066cef15bcbaf8e60422275))
* **notification:** Adiciona NotificationController para gerenciamento ([e418546](https://github.com/CesarAVB/notification-service/commit/e4185462f61ec0eef48ef6acf71837e36bbfdb9e))
* **notification:** implementa NotificationService para publicação de ([3bd6c26](https://github.com/CesarAVB/notification-service/commit/3bd6c2687087675e7a5f0737337261f88084f1c8))
