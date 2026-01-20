package com.kandaovr.meeting.kotlin_test

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.junit.Test
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/**
 * Kotlin 线程与协程测试大全
 *
 * 这个类展示了从传统的 Java 线程到现代 Kotlin 协程的各种用法。
 */
class ThreadTest {

    // =========================================================================================
    // 1. 传统线程 (Traditional Threads)
    // =========================================================================================

    /**
     * 演示了最基础的线程创建和启动方式。
     * - `Thread.sleep()`: 会阻塞当前线程，在协程中应避免使用，改用 `delay()`。
     */
    @Test
    fun traditionalThreadTest() {
        println("Main thread started: ${Thread.currentThread().name}")

        val myThread = Thread {
            println("Worker thread started: ${Thread.currentThread().name}")
            Thread.sleep(1000) // 模拟耗时操作
            println("Worker thread finished.")
        }
        myThread.start()

        // 等待子线程完成，以便在测试结束前看到其输出
        myThread.join()
        println("Main thread finished.")
    }

    /**
     * 演示了 Kotlin 提供的语法糖 `thread { ... }`，它让线程的创建更简洁。
     */
    @Test
    fun kotlinThreadHelperTest() {
        println("Main thread started: ${Thread.currentThread().name}")

        val job = thread(start = true, name = "Kotlin-Helper-Thread") {
            println("Worker thread started: ${Thread.currentThread().name}")
            Thread.sleep(1000)
            println("Worker thread finished.")
        }

        job.join() // 等待线程结束
        println("Main thread finished.")
    }

    /**
     * 演示了如何使用线程池 (ExecutorService) 来复用线程，避免频繁创建和销毁线程带来的开销。
     * 这是比手动创建线程更推荐的做法。
     */
    @Test
    fun threadPoolExecutorTest() {
        println("Main thread started: ${Thread.currentThread().name}")
        // 创建一个固定大小为2的线程池
        val executor = Executors.newFixedThreadPool(2)

        repeat(5) { index ->
            executor.submit {
                println("Task $index started on thread: ${Thread.currentThread().name}")
                Thread.sleep(500)
                println("Task $index finished.")
            }
        }

        executor.shutdown() // 不再接受新任务
        // 在测试环境中，需要等待所有任务完成
        while (!executor.isTerminated) {
            Thread.sleep(100)
        }
        println("All tasks finished.")
    }

    // =========================================================================================
    // 2. Kotlin 协程 (Coroutines)
    // =========================================================================================

    /**
     * **`runBlocking`**:
     * - 它是一个协程构建器，会**阻塞**当前线程，直到其内部的所有协程都执行完毕。
     * - 主要用途是作为桥梁，连接普通阻塞代码和挂起式的协程代码。
     * - 非常适合在 `main` 函数或 JUnit 测试方法中使用。
     */
    @Test
    fun runBlockingTest() = runBlocking {
        println("runBlocking starts on: ${Thread.currentThread().name}")

        launch { // 在 runBlocking 的上下文中启动一个新协程
            println("  -> launch starts on: ${Thread.currentThread().name}")
            delay(500) // 非阻塞式延迟
            println("  -> launch finished.")
        }

        println("runBlocking continues...")
        delay(1000) // 等待上面的 launch 完成
        println("runBlocking finished.")
    }

    /**
     * **`launch` vs `async`**:
     * - `launch`: “即发即忘”。它启动一个协程，但**不返回任何结果**。返回一个 `Job` 对象，可用于控制协程的生命周期（如取消）。
     * - `async`: 用于执行并发任务并**期望返回结果**。它返回一个 `Deferred` 对象（也是一个 Job），可以通过调用 `.await()` 来获取结果。
     */
    @Test
    fun launchVsAsyncTest() = runBlocking {
        println("--- Testing launch (fire and forget) ---")
        val job = launch {
            delay(500)
            println("Launch coroutine finished.")
        }
        println("Launched a job...")

        println("\n--- Testing async (expecting a result) ---")
        val deferredResult: Deferred<String> = async {
            delay(1000)
            "This is the result"
        }
        println("Launched an async task...")

        println("Waiting for results...")
        val result = deferredResult.await() // 阻塞当前协程直到结果返回
        println("Async result: '$result'")
        job.join() // 等待 launch 的 job 完成
    }

    /**
     * **`withContext`**:
     * - 用于在不阻塞的情况下**切换协程的上下文**（即运行的线程）。
     * - `Dispatchers.IO`: 用于执行磁盘或网络I/O等阻塞式、耗时的操作。
     * - `Dispatchers.Default`: 用于执行计算密集型任务（如排序、解析JSON）。
     * - `Dispatchers.Main`: (仅在Android等UI环境可用) 用于更新UI。
     */
    @Test
    fun withContextDispatcherSwitchTest() = runBlocking {
        println("Started in: ${Thread.currentThread().name}")

        val result = withContext(Dispatchers.IO) {
            println("  -> Switched to IO dispatcher: ${Thread.currentThread().name}")
            Thread.sleep(500) // 模拟阻塞的IO操作
            "Result from IO"
        }

        println("Back in original context: ${Thread.currentThread().name}")
        println("Result: $result")
    }

    @Test
    fun test() {
        println("startTest:${Thread.currentThread().name}")

        // 1. 创建通道，用于传递结果（非阻塞）
        val resultChannel = Channel<String>()
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val job = coroutineScope.launch {
            println("run:${Thread.currentThread().name}")
            val result = doSomething()
            resultChannel.send(result)
            println("run end:${Thread.currentThread().name}")
            resultChannel.close() // 关闭通道
        }

        // 3. 非阻塞接收通道结果（启动新协程，不阻塞原线程）
        coroutineScope.launch {
            val result = resultChannel.receive() // 非阻塞接收结果（无数据时挂起，不阻塞线程）
            println("原test线程通过通道获取到result: $result，当前线程: ${Thread.currentThread().name}")
        }

        // 验证：当前测试线程未被阻塞，会立即执行该行
        println("startTest end: ${Thread.currentThread().name}")

        // 测试环境下，避免进程提前退出（实际项目中无需此代码）
        Thread.sleep(2000)
    }

    // 挂起函数：非阻塞延迟，执行核心逻辑
    suspend fun doSomething(): String {
        println("doSomething 开始执行: ${Thread.currentThread().name}")
        delay(1000) // 非阻塞延迟（区别于Thread.sleep()的阻塞延迟）
        val result = "done something"
        println("doSomething 执行结束: ${Thread.currentThread().name}")
        return result
    }


    /**
     * **协程的取消 (Cancellation)**
     * - 协程的取消是“协作式”的，意味着协程代码需要主动检查其是否已被取消。
     * - `kotlinx.coroutines` 中的所有挂起函数（如 `delay`, `yield`, `withContext`）都是可取消的，它们会检查协程状态并抛出 `CancellationException`。
     */
    @Test
    fun cancellationTest() = runBlocking {
        val job = launch(Dispatchers.Default) {
            try {
                repeat(1000) { i ->
                    // 如果不加 isActive 检查，且循环内部没有可取消的挂起函数，
                    // 那么这个协程将无法被取消。
                    if (!isActive) return@launch

                    println("Job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                // `finally` 块通常用于执行清理操作
                // 如果需要在这里执行挂起函数，需使用 withContext(NonCancellable)
                withContext(NonCancellable) {
                    println("Job: I'm running in finally!")
                    delay(100)
                    println("Job: And I've just delayed for 100ms in NonCancellable.")
                }
            }
        }

        delay(1300L) // 等待一段时间
        println("Main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消任务并等待其完成
        println("Main: Now I can quit.")
    }

    // =========================================================================================
    // 3. 结构化并发 - 高级用法与常见陷阱 (Advanced Structured Concurrency)
    // =========================================================================================

    /**
     * **高级用法 1: `coroutineScope` vs `supervisorScope`**
     * - `coroutineScope`: **一损俱损**. 子协程的失败会向上传播，导致父协程和所有其他子协程被取消。
     *   适用于所有子任务都必须成功才能完成整体操作的场景。
     *
     * - `supervisorScope`: **独立监督**. 子协程的失败不会影响父协程或其他兄弟协程。
     *   适用于子任务相互独立，一个失败不应影响其他的场景 (例如：更新UI的多个独立部分)。
     *
     *   **陷阱**: `supervisorScope` 只阻止异常向上传播给父级，但异常本身依然会发生。对于 `launch` 启动的协程，
     *   如果异常未被处理 (如通过 `CoroutineExceptionHandler`)，它最终会到达默认的异常处理器，可能导致应用崩溃。
     */
    @Test
    fun scopeDifferenceTest() = runBlocking {
        println("--- Testing coroutineScope (一损俱损) ---")
        try {
            coroutineScope {
                launch {
                    delay(500)
                    println("coroutineScope: Child 1 finished successfully.") // 这行不会被打印
                }
                launch {
                    delay(200)
                    println("coroutineScope: Child 2 is failing...")
                    throw RuntimeException("Child 2 failed")
                }
            }
        } catch (e: Exception) {
            println("coroutineScope caught exception: ${e.message}")
        }
        println("coroutineScope finished.\n")


        println("--- Testing supervisorScope (独立监督) ---")
        // 为了捕获未处理的异常，我们需要一个 CoroutineExceptionHandler
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught exception in handler: $exception")
        }
        supervisorScope {
            launch(handler) { // 使用 handler 来捕获这个 launch 的异常
                delay(200)
                println("supervisorScope: Child 2 is failing...")
                throw RuntimeException("Child 2 failed inside supervisor")
            }
            launch {
                delay(500)
                // 这个协程不会被 Child 2 的失败所影响
                println("supervisorScope: Child 1 finished successfully.")
            }
        }
        println("supervisorScope finished.")
    }

    /**
     * **高级用法 2: 异常处理 (`CoroutineExceptionHandler`)**
     *
     * **常见的坑**:
     * 1. **`async`的异常被“吞掉”**: `async` 构建的协程在内部抛出异常时，并不会立即导致程序崩溃。异常会被“存储”在
     *    返回的 `Deferred` 对象中，直到你调用 `.await()` 时才会抛出。**必须**在调用 `.await()` 的地方使用 `try-catch`
     *    来处理异常，否则异常会继续向上传播。
     *
     * 2. **`CoroutineExceptionHandler` 对 `async` 无效**: `CoroutineExceptionHandler` 只对 `launch`
     *    启动的、未被捕获的异常有效。它无法捕获 `async` 的异常，因为 `async` 的异常被认为是“预期”的，需要由调用者通过 `.await()` 来处理。
     *
     * 3. **`supervisorScope` 无法阻止 `async` 异常传播**: 如果在 `supervisorScope` 中使用 `async` 并且
     *    在 Scope 外部调用 `await()` 却没有 `try-catch`，异常仍然会传播并可能导致崩溃。
     */
    @Test
    fun exceptionHandlingTest() = runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Handler caught: $exception")
        }

        val scope = CoroutineScope(Job() + Dispatchers.Default + handler)

        println("--- 1. Exception in launch (caught by handler) ---")
        val job = scope.launch {
            println("Launch is starting...")
            throw RuntimeException("Failure in launch")
        }
        job.join() // 等待 job 完成
        println()


        println("--- 2. Exception in async (must be caught at .await()) ---")
        val deferred = scope.async {
            println("Async is starting...")
            throw RuntimeException("Failure in async")
        }

        try {
            deferred.await()
        } catch (e: Exception) {
            println("Caught exception from await: ${e.message}")
        }

        // 清理 scope
        scope.cancel()
    }


}