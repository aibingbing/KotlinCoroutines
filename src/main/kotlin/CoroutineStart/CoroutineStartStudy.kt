package CoroutineStart

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import kotlin.concurrent.thread

class CoroutineStartStudy {
    val myThread = thread {
        println("myThread running in thread: ${Thread.currentThread().name}")
    }
}

private suspend fun defaultMode() {
    log(1)
    val job = GlobalScope.launch {
        log(2)
    }
    log(3)
    job.join()
    log(4)
}

private suspend fun lazyMode() {
    log(1)
    val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        log(2)
    }
    log(3)
    job.start()
    log(4)
}

private suspend fun lazyMode1() {
    log(1)
    val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        log(2)
    }
    log(3)
//    job.start()
    log(4)
}

private suspend fun atomicMode() {
    log(1)
    val job = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
        log(2)
    }
    job.cancel()
    log(3)
}

private suspend fun atomicMode1() {
    log(1)
    val job = GlobalScope.launch(start = CoroutineStart.DEFAULT) {
        log(2)
    }
    job.cancel()
    log(3)
}

private suspend fun unDispatched() {
    log(1)
    val job = GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
        log(2)
        delay(100)
        log(3)
    }
    log(4)
    job.join()
    log(5)
}

fun log(msg: Any?) {
    println("${LocalTime.now()}  [${Thread.currentThread().name}]: $msg")
}

/**
 * DEFAULT	立即执行协程体
 * ATOMIC	立即执行协程体，但在开始运行之前无法取消
 * UNDISPATCHED	立即在当前线程执行协程体，直到第一个 suspend 调用
 * LAZY	只有在需要的情况下运行
 */
suspend fun main(args: Array<String>) {
//    CoroutineStartStudy()
//    defaultMode()
//    lazyMode()
//    lazyMode1()
    atomicMode()
//    atomicMode1()
//    unDispatched()
}