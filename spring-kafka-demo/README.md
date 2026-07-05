# Spring Boot 3.x Kafka Demo

Project mẫu dùng Spring Boot 3.x + Gradle + Apache Kafka native image.

## Yêu cầu

- Ubuntu
- Docker + Docker Compose
- Java 21

## Chạy Kafka local

```bash
docker compose up -d
```

Kafka broker chạy tại:

```text
localhost:9092
```

Kafka UI:

```text
http://localhost:8080
```

## Chạy app

```bash
./gradlew bootRun
```

Khi app start, nó sẽ:

1. Tạo topic `test-topic` nếu chưa có.
2. Topic có 3 partitions.
3. Producer gửi 100 messages.
4. Consumer đọc messages và log ra console.

## Kiểm tra topic trong Kafka container

```bash
docker exec -it kafka-native /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --describe \
  --topic test-topic
```

## Xóa topic để test lại từ đầu

```bash
docker exec -it kafka-native /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --delete \
  --topic test-topic
```
