
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

# Ví dụ [04.ExecutorCompletionService+CompletableFuture]
==============================================================
## Bài toán đặt ra
- Cho 1 ConcurrenceList voi 1000 items khac nhau. 
- Tao ra 1 ConcurrenceHashMap<String,String> ket qua nhu sau : 
  - key = item trong ConcurrenceList 
  - value = LinkedList<String> = 
    - "Unit value with $key at index 1 at time $now, 
    - Unit value with $key at index 2 at time $now, ... 
- Gia su moi lan xu ly 1 key/item ton 500 ms 
- Moi lan them 1 unit vao trong values ton 10 ms, moi value chua 15 units.
- Hay impl bang cach su dung ExecutorCompletionService hoac CompletableFuture

## ExecutorCompletionServiceDemo
- Submit 1000 jobs
- Lan luot lay complete job gan nhat day vao resultMap<String,String>
- Moi buoc lay item deu co try..catch() {} de xu ly loi
- Tat ca cac jobs deu se duoc xu ly bat ke co nem Exception ra hay khong.

## CompletableFutureDemo
- Tao ra 1000 jobs
- Goi lenh supplyAsync() -> Xu ly job chinh
- Goi lenh thenAccept() -> Xu ly sau khi xong moi job (onComplete())
- Handle exception xay ra -> {}
- Goi lenh CompletableFuture.allOf() de cho den khi tat ca jobs duoc xu ly xong het
- Tiep theo la tong hop ket qua trong resultMap<String,String>
- Tat ca cac jobs deu se duoc xu ly at ke co nem Exception ra hay khong.

## CompletableFutureBreakDemo
- Xu ly tuong tu nhu CompletableFutureDemo, tuy nhien khi co 1 Job nem ra ngoai le
  thi se update flag atomic: cancelled = true, tiep theo do la cancel tat ca cac jobs chua duoc chay.
- Doi voi cac job da chay thi se check cancelled = false de break job.
- Nhu vay se tiet kiem resource khi co 1 job bi loi ...
