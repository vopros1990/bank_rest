# Система управления банковскими картами
Основные функции:
- Регистрация и аутентификация пользователей в системе
- Создание банковских карт и управление ими
- Переводы между своими счетами
- Заявки на блокировку карт от пользователей, их учет и обработка администраторами

## Запуск на локальной машине
### Настройка БД
Настройте PostgreSQL в файле docker/docker-compose.yml
```yaml
    ports:
      - "5433:5432" # Порт компьютера : порт postgreSQL
    environment:
      - POSTGRES_USER=admin # Имя пользователя БД
      - POSTGRES_PASSWORD=admin # Пароль пользователя БД
      - POSTGRES_DB=bank_db # Имя базы данных
    volumes:
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql # Файл с инициализацией схемы
```
Настройте подключение к БД в файле application.yml
```yaml
spring:
  datasource:
    username: admin # Имя пользователя БД
    password: admin # Пароль пользователя БД
    url: jdbc:postgresql://localhost:5433/bank_db?sslmode=disable # Ссылка для подключения к БД
    # ...
    hikari:
      schema: bank_schema # Имя схемы
```
### Запуск Docker контейнера с БД
Выполните следующие команды в терминале:
```
cd docker # Перейдите в папку docker проекта
docker compose up
```
Для удаления контейнера воспользуйтесь командой
```
docker compose down
```
## Запуск и сборка приложения
### Требования

Для запуска приложения локально необходимы:
- Java 21
- Maven 3.9+
- PostgreSQL 14+
- Свободный порт 8080

### Сборка проекта
Выполните следующие команды в корне проекта:
```
mvn clean package
```

В результате будет собран JAR-файл:
<code>target/bankcards-0.0.1-SNAPSHOT.jar</code>

## Переменные окружения

Приложение использует переменные окружения для обеспечения безопасности данных.
В файле <code>application.yml</code> содержатся данные по-умолчанию. Рекомендуется запускать приложение, указав свои данные.

### JWT_SECRET
Base64-ключ для подписи JWT-токенов. Закодируйте секрет (не менее 64 символа) в Base64 строку (UTF-8). Для кодирования можно воспользоваться сайтом https://www.base64encode.org/
### CARD_HASH_SECRET
Base64-ключ для HMAC-хеширования номеров карт.
### ADMIN_PASSWORD
Пароль администратора, создаваемого при старте. Значение по-умолчанию можно задать в application.yml.

### Пример запуска с переменными окружения
```
export JWT_SECRET=jwt-secret
export CARD_HASH_SECRET=base64-secret
export ADMIN_PASSWORD=admin123

mvn spring-boot:run
```
### Запуск через java -jar
```
java -jar target/bankcards-0.0.1-SNAPSHOT.jar
```