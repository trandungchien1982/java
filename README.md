
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

# Ví dụ [03.ExceptionLambdaPipeline]
==============================================================
## Bài toán đặt ra
- Giả sử 1 có 1 pipeline xử lý lambda cho nhiều phép biến đổi
```shell
    A -> B -> C -> Final Result
```
- Nếu có Exception xảy ra tại 1 điểm, chẳng hạn như B thì việc capture Exception diễn ra như thế nào ? 
Nó có làm break workflow tại B hay vẫn xử lý đến Final Result 🙂

**Kết quả trong TH xử lý Synchronus, đơn luồng:**<br/>
- Ta cần xử lý List: `Value 01`, `Value 02`, `Value 03`, `Value 04`, `Value 05`, `Value 06`, `Value 07`
- Exception xảy ra ở `Value 03` thuộc `MainThread`
- Pipeline xử lý cũng thuộc cùng 1 Thread (`MainThread`) và worklow xử lý bị ngắt ngay tại `Value 03`
```shell
> Task :MainApp.main()
10:39:38.185 [main] INFO  - Thực hiện UseCase có Exception được raise trong Lambda và check kết quả!
10:39:38.195 [main] INFO  -  --- Current thread: 1
10:39:38.195 [main] INFO  - Process in Single Thread ...
10:39:38.199 [main] INFO  - -------------------------------------------------
10:39:38.203 [main] INFO  - Call filters with value: Value 01 --- Current thread: 1
10:39:38.204 [main] INFO  - Call map() with add Suffix01, value: Value 01 --- Current thread: 1
10:39:38.205 [main] INFO  - Call map() with add MIDDLE, value: Value 01 - Suffix01 --- Current thread: 1
10:39:38.205 [main] INFO  - Call map() with Suffix02, value: Value 01 - Suffix01 - Middle --- Current thread: 1
10:39:38.206 [main] INFO  - -------------------------------------------------
10:39:38.206 [main] INFO  - Call filters with value: Value 02 --- Current thread: 1
10:39:38.206 [main] INFO  - Call map() with add Suffix01, value: Value 02 --- Current thread: 1
10:39:38.206 [main] INFO  - Call map() with add MIDDLE, value: Value 02 - Suffix01 --- Current thread: 1
10:39:38.206 [main] INFO  - Call map() with Suffix02, value: Value 02 - Suffix01 - Middle --- Current thread: 1
10:39:38.207 [main] INFO  - -------------------------------------------------
10:39:38.207 [main] INFO  - Call filters with value: Value 03 --- Current thread: 1
10:39:38.207 [main] INFO  - Call map() with add Suffix01, value: Value 03 --- Current thread: 1
10:39:38.211 [main] ERROR -  >> Exception occur in SINGLE Thread 
java.lang.NullPointerException: Custom Exception with [Value 03]
	at exception_in_lambda.MainApp.lambda$singleThread$1(MainApp.java:52)
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
	at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:179)
	at java.base/java.util.AbstractList$RandomAccessSpliterator.forEachRemaining(AbstractList.java:720)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
	at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
	at java.base/java.util.stream.ReduceOps$5.evaluateSequential(ReduceOps.java:258)
	at java.base/java.util.stream.ReduceOps$5.evaluateSequential(ReduceOps.java:248)
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.base/java.util.stream.ReferencePipeline.count(ReferencePipeline.java:709)
	at exception_in_lambda.MainApp.singleThread(MainApp.java:63)
	at exception_in_lambda.MainApp.main(MainApp.java:18)
```

**Kết quả trong TH xử lý Asynchronus/ Parallel, đa luồng**
- Ta cần xử lý List: `Item 01`, `Item 02`, `Item 03`, `Item 04`, `Item 05`, `Item 06`, `Item 07`
- Exception xảy ra tại `Item 03` thuộc 1 Thread khác `ForkJoinPool.commonPool-worker-3`
- Chỉ có logic trong nội bộ của Item 03 bị break, tất cả các xử lý khác vẫn tiếp tục
- Sau cùng vẫn có Exception raise ra cho `MainThread`
```shell
10:52:39.565 [main] INFO  - Process in Multiple Thread ...
10:52:39.576 [main] INFO  - -------------------------------------------------
10:52:39.577 [ForkJoinPool.commonPool-worker-3] INFO  - -------------------------------------------------
10:52:39.577 [ForkJoinPool.commonPool-worker-1] INFO  - -------------------------------------------------
10:52:39.577 [ForkJoinPool.commonPool-worker-2] INFO  - -------------------------------------------------
10:52:39.578 [ForkJoinPool.commonPool-worker-3] INFO  - Process value: Item 01 [Filters] --- Current thread: 15
10:52:39.578 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 02 [Filters] --- Current thread: 13
10:52:39.578 [ForkJoinPool.commonPool-worker-2] INFO  - Process value: Item 07 [Filters] --- Current thread: 14
10:52:39.579 [ForkJoinPool.commonPool-worker-3] INFO  - Process value: Item 01 [Mapping Suffix01] --- Current thread: 15
10:52:39.579 [ForkJoinPool.commonPool-worker-2] INFO  - Process value: Item 07 [Mapping Suffix01] --- Current thread: 14
10:52:39.579 [ForkJoinPool.commonPool-worker-3] INFO  - Process value: Item 01 [Mapping Middle] --- Current thread: 15
10:52:39.579 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 02 [Mapping Suffix01] --- Current thread: 13
10:52:39.579 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 02 [Mapping Middle] --- Current thread: 13
10:52:39.579 [ForkJoinPool.commonPool-worker-3] INFO  - Process value: Item 01 [Mapping Suffix02] --- Current thread: 15
10:52:39.580 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 02 [Mapping Suffix02] --- Current thread: 13
10:52:39.580 [ForkJoinPool.commonPool-worker-3] INFO  - -------------------------------------------------
10:52:39.580 [ForkJoinPool.commonPool-worker-1] INFO  - -------------------------------------------------
10:52:39.580 [ForkJoinPool.commonPool-worker-3] INFO  - Process value: Item 03 [Filters] --- Current thread: 15
10:52:39.580 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 04 [Filters] --- Current thread: 13
10:52:39.580 [ForkJoinPool.commonPool-worker-3] INFO  - Process value: Item 03 [Mapping Suffix01] --- Current thread: 15
10:52:39.580 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 04 [Mapping Suffix01] --- Current thread: 13
10:52:39.580 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 04 [Mapping Middle] --- Current thread: 13
10:52:39.580 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 04 [Mapping Suffix02] --- Current thread: 13
10:52:39.580 [ForkJoinPool.commonPool-worker-1] INFO  - -------------------------------------------------
10:52:39.580 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 06 [Filters] --- Current thread: 13
10:52:39.580 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 06 [Mapping Suffix01] --- Current thread: 13
10:52:39.581 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 06 [Mapping Middle] --- Current thread: 13
10:52:39.581 [ForkJoinPool.commonPool-worker-1] INFO  - Process value: Item 06 [Mapping Suffix02] --- Current thread: 13
10:52:39.581 [ForkJoinPool.commonPool-worker-2] INFO  - Process value: Item 07 [Mapping Middle] --- Current thread: 14
10:52:39.581 [ForkJoinPool.commonPool-worker-2] INFO  - Process value: Item 07 [Mapping Suffix02] --- Current thread: 14
10:52:39.584 [main] INFO  - Process value: Item 05 [Filters] --- Current thread: 1
10:52:39.584 [main] INFO  - Process value: Item 05 [Mapping Suffix01] --- Current thread: 1
10:52:39.584 [main] INFO  - Process value: Item 05 [Mapping Middle] --- Current thread: 1
10:52:39.584 [main] INFO  - Process value: Item 05 [Mapping Suffix02] --- Current thread: 1
10:52:39.585 [main] ERROR -  >> Exception occur in MULTIPLE Thread 
java.lang.NullPointerException: null
	at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:480)
	at java.base/java.util.concurrent.ForkJoinTask.getThrowableException(ForkJoinTask.java:564)
	at java.base/java.util.concurrent.ForkJoinTask.reportException(ForkJoinTask.java:591)
	at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:689)
	at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateParallel(ReduceOps.java:927)
	at java.base/java.util.stream.ReduceOps$5.evaluateParallel(ReduceOps.java:267)
	at java.base/java.util.stream.ReduceOps$5.evaluateParallel(ReduceOps.java:248)
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
	at java.base/java.util.stream.ReferencePipeline.count(ReferencePipeline.java:709)
	at exception_in_lambda.MainApp.multiThread(MainApp.java:94)
	at exception_in_lambda.MainApp.main(MainApp.java:25)
Caused by: java.lang.NullPointerException: Another Exception with [Item 03]
	at exception_in_lambda.MainApp.lambda$multiThread$5(MainApp.java:83)
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
	at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:179)
	at java.base/java.util.AbstractList$RandomAccessSpliterator.forEachRemaining(AbstractList.java:720)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
	at java.base/java.util.stream.ReduceOps$ReduceTask.doLeaf(ReduceOps.java:960)
Caused by: java.lang.NullPointerException: Another Exception with [Item 03]

	at java.base/java.util.stream.ReduceOps$ReduceTask.doLeaf(ReduceOps.java:934)
	at java.base/java.util.stream.AbstractTask.compute(AbstractTask.java:327)
	at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:754)
	at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
10:52:39.585 [main] INFO  - 



10:52:39.585 [main] INFO  - The final script in MainApp ... 
10:52:41.590 [main] INFO  - The final script in MainApp 01 ... 

```
