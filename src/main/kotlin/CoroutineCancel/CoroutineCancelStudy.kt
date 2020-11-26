package CoroutineCancel

import CoroutineStart.log
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.random.Random


fun cancel1() = runBlocking {
    val job1 = launch {
        log(1)
        // delay 与线程的 sleep 不同，它不会阻塞线程，你可以认为它实际上就是触发了一个延时任务，
        // 告诉协程调度系统 1000ms 之后再来执行后面的这段代码
        delay(1000)
        log(2)
    }
    delay(100)
    log(3)
    job1.cancel()
    log(4)
}

fun cancel2() = runBlocking {
    val job1 = launch {
        log(1)
        // delay 与线程的 sleep 不同，它不会阻塞线程，你可以认为它实际上就是触发了一个延时任务，
        // 告诉协程调度系统 1000ms 之后再来执行后面的这段代码
        try {
            delay(1000)
        } catch (e: Exception) {
            log("cancelled. $e")
        }
        log(2)
    }
    delay(100)
    log(3)
    job1.cancel()
    log(4)
}

suspend fun getRandomInt() = suspendCancellableCoroutine<Int> { continuation ->
    continuation.invokeOnCancellation {
        log("invokeOnCancellation: cancel the request.")
    }
    //模拟请求耗时及成功失败
    Thread.sleep(300)
    val randomInt = Random(100).nextInt()
    if (randomInt < 50) {
        continuation.resume(randomInt)
    } else {
        continuation.resumeWithException(IllegalArgumentException("random int > 50"))
    }
}

suspend fun cancel3() {
    val job1 = GlobalScope.launch {
        log(1)
        val v = getRandomInt()
        log(v)
        log(2)
    }
    delay(10)
    log(3)
    job1.cancel()
    log(4)
}

suspend fun getRandomIntDefered(): Deferred<Int> {
    // 模拟retrofit - adapter
    val deferred = CompletableDeferred<Int>()
    deferred.invokeOnCompletion {
        if (deferred.isCancelled) {
            log("Deferred is canceled")
        }
    }
    //模拟请求耗时及成功失败
    val randomInt = Random(100).nextInt()
    GlobalScope.launch {
        delay(500)
        if (randomInt < 50) {
            deferred.complete(randomInt)
        } else {
            deferred.completeExceptionally(IllegalArgumentException("random int > 50"))
        }
    }
    return deferred
}

suspend fun cancel4() {
    log(1)
    val deferred = getRandomIntDefered()
    log(2)
    withContext(Dispatchers.IO) {
        deferred.cancel()
    }
    try {
        val v = deferred.await()
        log("result. $v")
    } catch (e: Exception) {
        log("error.$e")
    }
}

suspend fun cancel5() {
    // 复现一个retrofit-adapter的一个bug
    val job = GlobalScope.launch {
        log(1)
        val deferred = getRandomIntDefered()
        log(2)
        try {
            val v = deferred.await()
            log("result. $v")
        } catch (e: Exception) {
            log("error.$e")
        }
        log(3)
    }
    delay(10)
    job.cancelAndJoin()
}

suspend fun main() {
//    cancel1()
//    cancel2()
//    cancel3()
//    cancel4()
    cancel5()
}