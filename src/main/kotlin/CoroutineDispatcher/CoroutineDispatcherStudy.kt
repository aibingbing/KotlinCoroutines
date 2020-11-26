package CoroutineDispatcher

import CoroutineStart.log
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext


class MyContinuationInterceptor : ContinuationInterceptor {
    override val key = ContinuationInterceptor
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        MyContinuation(continuation)
}

class MyContinuation<T>(private val continuation: Continuation<T>) : Continuation<T> {
    override val context: CoroutineContext = continuation.context
    override fun resumeWith(result: Result<T>) {
        log("<MyContinuation> $result")
        continuation.resumeWith(result)
    }
}

suspend fun dispatcher1() {
    GlobalScope.launch(MyContinuationInterceptor()) {
        log(1)
        val job = async {
            log(2)
            delay(1000)
            log(3)
            "Hello"
        }
        log(4)
        val result = job.await()
        log("5 $result")
    }.join()
    log(6)
}

suspend fun dispatcher2() {
    GlobalScope.launch(MyContinuationInterceptor()) {
        log(1)
        val job = async {
            log(2)
//            delay(1000)
            log(3)
            "Hello"
        }
        log(4)
        val result = job.await()
        log("5 $result")
    }.join()
    log(6)
}


suspend fun dispatcher3() {
    val myDispatcher = Executors.newSingleThreadExecutor { r ->
        Thread(r, "MyThread")
    }.asCoroutineDispatcher()
    GlobalScope.launch(myDispatcher) {
        log(1)
    }.join()
    log(2)
    // close executor
    myDispatcher.close()
}

suspend fun dispatcher4() {
    Executors.newFixedThreadPool(10)
        .asCoroutineDispatcher()
        .use { dispatcher ->
            GlobalScope.launch(dispatcher) {
                log(1)
                val job = async {
                    log(2)
                    delay(1000)
                    log(3)
                    "HelloWorld"
                }
                log(4)
                val result = job.await()
                log("5. $result")
            }.join()
            log(6)
        }
}

suspend fun dispatcher5() {
    GlobalScope.launch(Dispatchers.IO) {
        log(1)
        delay(1000)
        log(2)
    }.join()
    log(3)
}

suspend fun main() {
//    dispatcher1()
//    dispatcher2()
//    dispatcher3()
//    dispatcher4()
    dispatcher5()
}