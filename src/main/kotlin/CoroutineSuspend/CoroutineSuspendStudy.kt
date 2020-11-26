package CoroutineSuspend

import CoroutineStart.log
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.concurrent.thread
import kotlin.coroutines.resume


suspend fun suspend1() = suspendCancellableCoroutine<Int> { cancellableContinuation ->
    log(1)
    thread {
        Thread.sleep(1000)
        log(2)
        cancellableContinuation.resume(1024)
    }
    log(3)
}


/**
 *  1. Job.join() 这个方法会首先检查调用者 Job 的状态是否已经完成，如果是，就直接返回并继续执行后面的代码而不再挂起，
 *     否则就会走到这个 joinSuspend 的分支当中
 *  2. 简单来说就是，对于 suspend 函数，不是一定要挂起的，可以在需要的时候挂起，也就是要等待的协程还没有执行完的
 *     时候，等待协程执行完再继续执行；而如果在开始 join 或者 await 或者其他 suspend 函数，如果目标协程已经完成，
 *     那么就没必要等了，直接拿着结果走人即可。那么这个神奇的逻辑就在于 cancellable.getResult() 究竟返回什么了
 */

/**
 * 协程的挂起函数本质上就是一个回调，回调类型就是 Continuation
 * 协程体的执行就是一个状态机，每一次遇到挂起函数，都是一次状态转移
 */

/**
 * suspend fun main()的本质参考：@RunSuspend.kt
 */

suspend fun main() {
    val v = suspend1()
    log("$v")
}