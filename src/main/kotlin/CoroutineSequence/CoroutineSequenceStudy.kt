package CoroutineSequence

import CoroutineStart.log


fun sequence1() {
    val fibonacci = sequence<Long> {
        yield(1L)
        var cur = 1L
        var next = 1L
        while (true) {
            yield(next)
            val temp = cur + next
            next = temp
        }
    }
    fibonacci.take(10).forEach(::log)
}

fun sequence2() {
    val seq = sequence<Int> {
        log("yield 1,2,3")
        yieldAll(listOf(1, 2, 3))
        log("yield 4,5,6")
        yieldAll(listOf(4, 5, 6))
        log("yield 7,8,9")
        yieldAll(listOf(7, 8, 9))
    }
    seq.take(5).forEach(::log)
}

/**
 * Sequence实现参考： @SequenceBuilder
 */

/**
 * 序列生成器很好的利用了协程的状态机特性，将序列生成的过程从形式上整合到了一起，
 * 让程序更加紧凑，表现力更强。本节讨论的序列，某种意义上更像是生产 - 消费者模型中的生产者，
 * 而迭代序列的一方则像是消费者，其实在 kotlinx.coroutines 库中提供了更为强大的能力来实现生产 - 消费者模式
 */

suspend fun main() {
//    sequence1()
    sequence2()
}