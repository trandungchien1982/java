
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

# Ví dụ [02.MemoryWithStack+Heap]
==============================================================
## Tham khảo
- https://gpcoder.com/2160-quan-ly-bo-nho-trong-java-voi-heap-space-vs-stack/
- https://levunguyen.com/laptrinhjava/2020/01/16/bo-nho-heap-va-stack-trong-lap-trinh-java/
- https://hocjava.com/stack-vs-heap-trong-java/

## Tạo 1 App mẫu và tìm hiểu cách phân bổ vùng nhớ Stack/Heap dành cho các biến/objects:<br/>
**Các biến nằm trong Stack: (dung lượng thấp/tốc độ truy xuất nhanh)**
- Biến primitives: int/float/double/...
- Địa chỉ tham chiếu đến biến Object
- Scope của Stack nằm trong function(), các biến sẽ được release khi thoát ra khỏi hàm
- Tràn vùng nhớ sẽ có lỗi : StackOverFlowError

**Các biến nằm trong Heap: (dung lượng cao/tốc độ truy xuất chậm)**
- Các biến khởi tạo bằng new 
- Data được allocate cho objects
- Khi biến không còn được reference thì vùng nhớ Heap dành cho nó sẽ được giải phóng nhờ vào Garbage Collector
- Tràn vùng nhớ sẽ có lỗi: OutOfMemoryError

**Theo dõi vùng nhớ dành cho Stack/Heap ( không chắc có làm được không )**

## Kết quả thực thi
```shell
./gradlew bootRun
--------------------------------------------------------------------------
> Task :Memory.main()
22:29:43.329 [main] INFO  - Tìm hiểu về Stack+Heap Memory!
22:29:43.333 [main] INFO  - java.lang.Object@13805618
```
