package CoroutineChannel

import CoroutineStart.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*


suspend fun channel1() {
    val channel = Channel<Int>()
    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            channel.send(i++)
            delay(1000)
        }
    }

    val consumer = GlobalScope.launch {
        while (true) {
            val element = channel.receive()
            log(element)
        }
    }
    producer.join()
    consumer.join()
}

suspend fun channel2() {
    val channel = Channel<Int>()
    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            i++
            log("before send $i")
            channel.send(i)
            log("after send $i")
            delay(1000)
        }
    }

    val consumer = GlobalScope.launch {
        while (true) {
            delay(2000)
            val element = channel.receive()
            log(element)
        }
    }
    producer.join()
    consumer.join()
}

suspend fun channel3() {
    val channel = Channel<Int>()

    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            channel.send(i++)
            delay(1000)
        }
    }

    val consumer = GlobalScope.launch {
        val iterator = channel.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            log(element)
            delay(2000)
        }
    }

    producer.join()
    consumer.join()
}

suspend fun channel4() {
    val channel = Channel<Int>()

    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            channel.send(i++)
            delay(1000)
        }
    }

    val consumer = GlobalScope.launch {
        for (element in channel) {
            log(element)
            delay(2000)
        }
    }

    producer.join()
    consumer.join()
}

/**
 * ReceiveChannel 和 SendChannel 都是 Channel 的父接口，前者定义了 receive，
 * 后者定义了 send，Channel 也因此既可以 receive 又可以 send。
 */
suspend fun channel5() {
    var i = 0
    val receiveChannel: ReceiveChannel<Int> = GlobalScope.produce {
        while (true) {
            delay(1000)
            i++
            log("send: $i")
            send(i)
        }
    }
    for (element in receiveChannel) {
        log("receive: $element")
        delay(1000)
    }
}

suspend fun channel6() {
    val sendChannel: SendChannel<Int> = GlobalScope.actor {
        while (true) {
            val element = receive()
            log("receive: $element")
        }
    }
    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            i++
            log("send: $i")
            sendChannel.send(i)
            delay(1000)
        }
    }
    producer.join()
}


/**
 * Channel其实内部的资源就是个缓冲区，这个东西本质上就是个线性表，就是一块儿内存，
 * 所以如果我们开了一个 Channel 而不去关闭它，其实也不会造成什么资源泄露，
 * 发端如果自己已经发完，它就可以不理会这个 Channel 了
 */
/**
 * But，这时候在接收端就比较尴尬了，它不知道会不会有数据发过来，如果 Channel 是微信，
 * 那么接收端打开微信的窗口可能一直看到的是『对方正在输入』，
 * 然后它就一直这样了，孤独终老。所以这里的关闭更多像是一种约定
 */
/**
 * 那么 Channel 的关闭究竟应该有谁来处理呢？正常的通信，如果是单向的，就好比领导讲话，讲完都会说『我讲完了』，
 * 你不能在领导还没讲完的时候就说『我听完了』，所以单向通信的情况比较推荐由发端处理关闭；而对于双向通信的情况，
 * 就要考虑协商了，双向通信从技术上两端是对等的，但业务场景下通常来说不是，建议由主导的一方处理关闭。
 */
suspend fun channel7() {
    val channel = Channel<Int>(3)

    val producer = GlobalScope.launch {
        List(5) {
            log("send: $it")
            channel.send(it)
        }
        channel.close()
        log("close channel. ClosedForSend = ${channel.isClosedForSend} CloseForReceive:${channel.isClosedForReceive}")
    }

    val consumer = GlobalScope.launch {
        for (element in channel) {
            log("receive: $element")
            delay(1000)
        }
        log("After Consuming. ClosedForSend = ${channel.isClosedForSend} CloseForReceive: ${channel.isClosedForReceive}")
    }

    producer.join()
    consumer.join()
}

suspend fun channel8() {
    val broadcastChannel = BroadcastChannel<Int>(5)

    val producer = GlobalScope.launch {
        List(5) {
            log("send: $it")
            broadcastChannel.send(it)
        }
    }

    List(3) { index ->
        GlobalScope.launch {
            val receiveChannel = broadcastChannel.openSubscription()
            for (element in receiveChannel) {
                log("[$index] receive : $element")
                delay(1000)
            }
        }
    }.forEach { it.join() }
    producer.join()
}

suspend fun channel9() {
    val channel = Channel<Int>()
    val broadcastChannel = channel.broadcast(5)

    val producer = GlobalScope.launch {
        List(5) {
            log("send: $it")
            broadcastChannel.send(it)
        }
    }

    List(3) { index ->
        GlobalScope.launch {
            val receiveChannel = broadcastChannel.openSubscription()
            for (element in receiveChannel) {
                log("[$index] receive : $element")
                delay(1000)
            }
        }
    }.forEach { it.join() }
    producer.join()
}


suspend fun channel10() {
    val channel = GlobalScope.produce<Int>(Dispatchers.Unconfined) {
        log("A")
        send(1)
        log("B")
        send(2)
        log("Done")
    }
    for (item in channel) {
        log("Got $item")
    }

    val sequence = sequence<Int> {
        log("A")
        yield(1)
        log("B")
        yield(2)
        log("Done")
    }
    for (item in sequence) {
        log("Got $item")
    }
}

suspend fun channel11() {
    val channel = GlobalScope.produce<Int>(Dispatchers.Unconfined) {
        log("A")
        send(1)
        withContext(Dispatchers.IO) {
            log("B")
            send(2)
        }
        log("Done")
    }
    for (item in channel) {
        log("Got $item")
    }
}

suspend fun main() {
//    channel1()
//    channel2()
//    channel3()
//    channel4()
//    channel5()
//    channel6()
//    channel7()
//    channel8()
//    channel9()
//    channel10()
    channel11()
}