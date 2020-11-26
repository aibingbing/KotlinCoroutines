package CoroutineFlow

import CoroutineStart.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.util.concurrent.Executors


suspend fun flow1() {
    val ints = sequence<Int> {
        (1..3).forEach {
            yield(it)
        }
    }
    for (item in ints) {
        log(item)
    }
}

/**
 * 通过 flowOn 设置的调度器只对它之前的操作有影响，因此这里意味着 intFlow 的构造逻辑会在 IO 调度器上执行。
 */
suspend fun flow2() {
    val intFlow = flow {
        (1..3).forEach {
            emit(it)
            delay(1000)
        }
    }
    GlobalScope.launch {
        intFlow.flowOn(Dispatchers.IO)
            .collect {
                log(it)
            }
    }.join()
}

/**
 * Flow 的调度器 API 中看似只有 flowOn 与 subscribeOn 对应，其实不然， collect 所在协程的调度器则与
 * observeOn 指定的调度器对应。在 RxJava 的学习和使用过程中， subscribeOn 和 observeOn 经常容易被混淆；
 * 而在 Flow 当中 collect 所在的协程自然就是观察者，它想运行在什么调度器上它自己指定即可，非常容易区分
 */
suspend fun flow3() {
    val intFlow = flow {
        (1..3).forEach {
            log("send: $it")
            emit(it)
            delay(1000)
        }
    }
    val myDispatcher = Executors.newSingleThreadExecutor { r ->
        Thread(r, "MyThread")
    }.asCoroutineDispatcher()
    GlobalScope.launch(myDispatcher) {
        intFlow.flowOn(Dispatchers.IO)
            .collect {
                log("receive: $it")
            }
    }.join()
    myDispatcher.close()
}

suspend fun flow4() {
    val intFlow = flow {
        (1..3).forEach {
            log("send: $it")
            emit(it)
            delay(1000)
        }
    }
    val myDispatcher = Executors.newSingleThreadExecutor { r ->
        Thread(r, "MyThread")
    }.asCoroutineDispatcher()
    GlobalScope.launch(myDispatcher) {
        // collect 2次，消费2次
        intFlow.collect { log("collect1 : receive: $it") }
        intFlow.collect { log("collect2 : receive: $it") }
    }.join()
    myDispatcher.close()
}

suspend fun flow5() {
    GlobalScope.launch {
        flow<Int> {
            emit(1)
            throw ArithmeticException("Test")
            emit(2)
        }.catch { t: Throwable ->
            log("caught error: $t")
        }.collect {
            log("collect1 : receive: $it")
        }
    }.join()
}

suspend fun flow6() {
    GlobalScope.launch {
        flow<Int> {
            emit(1)
            throw ArithmeticException("Test")
            emit(2)
        }.catch { t: Throwable ->
            log("caught error: $t")
        }.onCompletion {
            log("onComplete")
        }.collect {
            log("collect1 : receive: $it")
        }
    }.join()
}

/**
 * 命令式的异常处理（不推荐）
 */
suspend fun flow7() {
    GlobalScope.launch {
        flow<Int> {
            try {
                emit(1)
                throw ArithmeticException("Test")
                emit(2)
            } catch (e: Exception) {
                log("caught error: $e")
            } finally {
                log("onComplete")
            }
        }.collect {
            log("collect1 : receive: $it")
        }
    }.join()
}

/**
 * 异常中恢复
 */
suspend fun flow8() {
    GlobalScope.launch {
        flow<Int> {
            emit(1)
            throw ArithmeticException("Test")
            emit(2)
        }.catch { t: Throwable ->
            log("caught error: $t")
            emit(2)
        }.onCompletion {
            log("onComplete")
        }.collect {
            log("collect1 : receive: $it")
        }
    }.join()
}

/**
 * 集合类型转换操作，包括 toList、toSet 等。
 * 聚合操作，包括将 Flow 规约到单值的 reduce、fold 等操作，以及获得单个元素的操作包括 single、singleOrNull、first 等。
 */
suspend fun flow9() {
    GlobalScope.launch {
        val set = flow<Int> {
            (1..10).forEach {
                emit(it)
                delay(100)
            }
            (10 downTo 1).forEach {
                emit(it)
                delay(100)
            }
        }.toSet()
        log("set: $set")
    }.join()
}

/**
 * onEach
 */
suspend fun flow10() {
    GlobalScope.launch {
        flow<Int> {
            (1..10).forEach {
                emit(it)
                delay(100)
            }
        }.onEach {
            log("onEach: $it")
        }.collect {

        }
    }.join()
}

suspend fun flow11() = flow<Int> {
    (1..10).forEach {
        emit(it)
        delay(100)
    }
}.onEach {
    log("onEach: $it")
}

suspend fun flow12() {
    flow11().launchIn(GlobalScope)
}

/**
 * 想要取消 Flow 只需要取消它所在的协程即可
 */
suspend fun flow13() {
    val job = GlobalScope.launch {
        val intFlow = flow<Int> {
            (1..3).forEach {
                delay(1000)
                emit(it)
            }
        }
        intFlow.collect {
            log(it)
        }
    }
    delay(2500)
    job.cancelAndJoin()
}

suspend fun flow14() {
    GlobalScope.launch {
        flow<Int> {
            emit(1)
            // Bad practise, emit不是线程安全的
            withContext(Dispatchers.IO) {
                emit(2)
            }
        }.collect {
            log("receive: $it")
        }
    }.join()
}

suspend fun flow15() {
    GlobalScope.launch {
        channelFlow<Int> {
            send(1)
            // Bad practise, emit不是线程安全的
            withContext(Dispatchers.IO) {
                send(2)
            }
        }.collect {
            log("receive: $it")
        }
    }.join()
}

/**
 * 背压问题在生产者的生产速率高于消费者的处理速率的情况下出现。为了保证数据不丢失，我们也会考虑添加缓存来缓解问题
 */
/**
 * 解决背压问题 - conflate 合并
 */
suspend fun flow16() {
    GlobalScope.launch {
        flow {
            List(100) {
                emit(it)
            }
        }.conflate()
            .collect {
                log("collecting $it")
                delay(100)
                log("collected $it")
            }
    }.join()
}

/**
 * 解决背压问题 - collectLatest, mapLatest, flatMapLatest
 */
suspend fun flow17() {
    GlobalScope.launch {
        flow {
            List(100) {
                emit(it)
            }
        }.collectLatest {
            log("collecting $it")
            delay(100)
            log("collected $it")
        }
    }.join()
}

/**
 * Map
 */
suspend fun flow18() {
    GlobalScope.launch {
        flow {
            List(5) {
                emit(it)
            }
        }.map {
            it * 2
        }.collect {
            delay(100)
            log("collected $it")
        }
    }.join()
}

/**
 * flattenConcat
 */
suspend fun flow19() {
    GlobalScope.launch {
        flow {
            List(5) {
                emit(it)
            }
        }.map {
            flow { List(it) { emit(it * 2) } }
        }.flattenConcat()
            .collect {
                delay(100)
                log("collected $it")
            }
    }.join()
}

/**
 * flow多路复用 consumeAsFlow
 */
suspend fun flow20() {
    val channels = List(10) {
        Channel<Int>()
    }
    val producer = GlobalScope.launch {
        for ((i, channel) in channels.withIndex()) {
            log("channel$i: send $i")
            channel.send(i)
            channel.close()
        }
    }
    val consumer = GlobalScope.launch {
        val result = channels.map {
            it.consumeAsFlow()
        }.merge()
            .first()
        log("result:$result")
    }
    producer.join()
    consumer.join()
}

suspend fun main() {
//    flow1()
//    flow2()
//    flow3()
//    flow4()
//    flow5()
//    flow6()
//    flow7()
//    flow8()
//    flow9()
//    flow10()
//    flow12()
//    flow13()
//    flow14()
//    flow15()
//    flow16()
//    flow17()
//    flow18()
//    flow19()
    flow20()
}