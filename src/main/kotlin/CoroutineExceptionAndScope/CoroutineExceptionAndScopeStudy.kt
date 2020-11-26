package CoroutineExceptionAndScope

import CoroutineStart.log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


private suspend fun exception1() {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        log("Throws an exception with message: ${throwable.message}")
    }
    log(1)
    GlobalScope.launch(exceptionHandler) {
        throw ArithmeticException("Test")
    }.join()
    log(2)
}

/**
 *  META-INF/services/kotlinx.coroutines.CoroutineExceptionHandler，
 *  文件名实际上就是 CoroutineExceptionHandler 的全类名，
 *  文件内容就写我们的实现类的全类名：CoroutineException.GlobalCoroutineExceptionHandler
 *  这样协程中没有被捕获的异常就会最终交给它处理。
 */
class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        println("Coroutine exception: $exception")
    }
}

/***
 * 异常的传播
 * coroutineScope: 是继承外部 Job 的上下文创建作用域，在其内部的取消操作是双向传播的，
 *                 子协程未捕获的异常也会向上传递给父协程。它更适合一系列对等的协程并发的完成一项工作，
 *                 任何一个子协程异常退出，那么整体都将退出，简单来说就是”一损俱损“。
 *                 这也是协程内部再启动子协程的默认作用域。
 * supervisorScope:同样继承外部作用域的上下文，但其内部的取消操作是单向传播的，父协程向子协程传播，
 *                 反过来则不然，这意味着子协程出了异常并不会影响父协程以及其他兄弟协程。
 *                 它更适合一些独立不相干的任务，任何一个任务出问题，并不会影响其他任务的工作，
 *                 简单来说就是”自作自受“，例如 UI，我点击一个按钮出了异常，其实并不会影响手机状态栏的刷新。
 *                 需要注意的是，supervisorScope 内部启动的子协程内部再启动子协程，
 *                 如无明确指出，则遵守默认作用域规则，也即 supervisorScope 只作用域其直接子协程。
 */
suspend fun scope1() {
    log(1)
    try {
        coroutineScope {
            log(2)
            launch {
                log(3)
                launch {
                    log(4)
                    delay(100)
                    throw java.lang.ArithmeticException("Test")
                }
                log(5)
            }
            log(6)
            val job = launch {
                log(7)
                delay(1000)
            }
            try {
                log(8)
                job.join()
                log(9)
            } catch (e: Exception) {
                log("10. $e")
            }
        }
        log(11)
    } catch (e: Exception) {
        log("12. $e")
    }
    log(13)
}

suspend fun scope2() {
    log(1)
    try {
        supervisorScope {
            log(2)
            launch {
                log(3)
                launch {
                    log(4)
                    delay(100)
                    throw java.lang.ArithmeticException("Test")
                }
                log(5)
            }
            log(6)
            val job = launch {
                log(7)
                delay(1000)
            }
            try {
                log(8)
                job.join()
                log(9)
            } catch (e: Exception) {
                log("10. $e")
            }
        }
        log(11)
    } catch (e: Exception) {
        log("12. $e")
    }
    log(13)
}

suspend fun scope3() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        log("${coroutineContext[CoroutineName]} $throwable")
    }
    log(1)
    try {
        supervisorScope {
            log(2)
            launch(exceptionHandler + CoroutineName("②")) {
                log(3)
                launch(exceptionHandler + CoroutineName("③")) {
                    log(4)
                    delay(100)
                    throw java.lang.ArithmeticException("Test")
                }
                log(5)
            }
            log(6)
            val job = launch {
                log(7)
                delay(1000)
            }
            try {
                log(8)
                job.join()
                log(9)
            } catch (e: Exception) {
                log("10. $e")
            }
        }
        log(11)
    } catch (e: Exception) {
        log("12. $e")
    }
    log(13)
}

suspend fun async1() {
    val deferred = GlobalScope.async<Int> {
        throw java.lang.ArithmeticException()
    }
    try {
        val value = deferred.await()
        log("1. $value")
    } catch (e: java.lang.Exception) {
        log("2. $e")
    }
}

suspend fun join1() {
    val deferred = GlobalScope.async<Int> {
        throw java.lang.ArithmeticException()
    }
    try {
        deferred.join()
        log("1")
    } catch (e: java.lang.Exception) {
        log("2. $e")
    }
}

/**
 * 协程异常的处理总结
 *
 * 协程内部异常处理流程：launch 会在内部出现未捕获的异常时尝试触发对父协程的取消，能否取消要看作用域的定义，
 *                    如果取消成功，那么异常传递给父协程，否则传递给启动时上下文中配置的
 *                    CoroutineExceptionHandler 中，如果没有配置，会查找全局（JVM上）的
 *                    CoroutineExceptionHandler 进行处理，如果仍然没有，那么就将异常交给当前线程的
 *                    UncaughtExceptionHandler 处理；而 async 则在未捕获的异常出现时同样会尝试取消父协程，
 *                    但不管是否能够取消成功都不会后其他后续的异常处理，直到用户主动调用 await 时将异常抛出。
 * 异常在作用域内的传播：当协程出现异常时，会根据当前作用域触发异常传递，GlobalScope 会创建一个独立的作用域，
 *                    所谓“自成一派”，而 在 coroutineScope 当中协程异常会触发父协程的取消，
 *                    进而将整个协程作用域取消掉，如果对 coroutineScope 整体进行捕获，也可以捕获到该异常，
 *                    所谓“一损俱损”；如果是 supervisorScope，那么子协程的异常不会向上传递，所谓“自作自受”。
 * join和await 的不同：join 只关心协程是否执行完，await 则关心运行的结果，
 *                    因此 join 在协程出现异常时也不会抛出该异常，而 await 则会；考虑到作用域的问题，
 *                    如果协程抛异常，可能会导致父协程的取消，因此调用 join 时尽管不会对协程本身的异常进行抛出，
 *                    但如果 join 调用所在的协程被取消，那么它会抛出取消异常，这一点需要留意。
 */

suspend fun main() {
//    exception1()
    scope1()
//    scope2()
//    scope3()
//    async1()
//    join1()
}