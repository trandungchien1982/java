
# Java
Các ví dụ liên quan đến Java từ cơ bản đến nâng cao<br/>
Mỗi nhánh trong Repo sẽ là 1 ví dụ/ giải pháp/ project mẫu trong Java

# Môi trường phát triển
- JDK 21

# Build Tools sử dụng
- Gradle
- Intelij IDEA

# Folder liên quan trên Windows
```
D:\Projects\java
```
==============================================================

# Ví dụ [04.ExecutorService]
==============================================================
## Bài toán đặt ra
- Cho 1 ConcurrenceList voi 500 items khac nhau. 
- Tao ra 1 ConcurrenceHashMap<String,String> ket qua nhu sau : 
  - key = item trong ConcurrenceList 
  - value = LinkedList<String> = 
    - "Unit value with $key at index 1 at time $now, 
    - Unit value with $key at index 2 at time $now, ... 
- Gia su moi lan xu ly 1 key/item ton 500 ms 
- Moi lan them 1 unit vao trong values ton 10 ms, moi value chua 15 units.
- Hay impl bang cach su dung ExecutorService



**Kết quả trong TH xử lý Synchronus, đơn luồng:**<br/>
- Ta cần xử lý List: `Value 01`, `Value 02`, `Value 03`, `Value 04`, `Value 05`, `Value 06`, `Value 07`
- Exception xảy ra ở `Value 03` thuộc `MainThread`
- Pipeline xử lý cũng thuộc cùng 1 Thread (`MainThread`) và worklow xử lý bị ngắt ngay tại `Value 03`
```shell
