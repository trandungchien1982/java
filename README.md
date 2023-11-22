
# Java
Các ví dụ liên quan đến Java từ cơ bản đến nâng cao<br/>
Mỗi nhánh trong Repo sẽ là 1 ví dụ/ giải pháp/ project mẫu trong Java

# Môi trường phát triển
- JDK 8/11/17

# Build Tools sử dụng
- Maven + Gradle
- Intelij IDEA

# Folder liên quan trên Windows
```
D:\Projects\java
```
==============================================================

# Ví dụ [01.HelloWorld]
==============================================================

## Tạo 1 App mẫu gửi lời chào đến thế giới:<br/>
(Build Tool dùng Gradle với các depedencies cần thiết)

## Kết quả thực thi
```shell
./gradlew bootRun
--------------------------------------------------------------------------
14:54:26.979 [main] INFO  - Starting ProtocolHandler ["http-nio-8100"]
14:54:27.018 [main] INFO  - Tomcat started on port(s): 8100 (http) with context path ''
14:54:27.031 [main] INFO  - Started HelloWorldApplication in 3.138 seconds (JVM running for 3.825)
14:54:27.033 [main] INFO  - Hello World!
14:54:27.033 [main] INFO  - Gửi lời chào đến thế giới nào :) ... 

```
