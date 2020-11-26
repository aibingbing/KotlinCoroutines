package CoroutineAndroid

import CoroutineStart.log
import kotlinx.coroutines.*

/**
 * 原来就是 SupervisorJob 整合了 Dispatchers.Main 而已，它的异常传播是自上而下的，
 * 这一点与 supervisorScope 的行为一致，此外，作用域内的调度是基于 Android 主线程的调度器的，
 * 因此作用域内除非明确声明调度器，协程体都调度在主线程执行
 */
fun android1() {
//    val mainScope = MainScope()
//    launchButton.setOnClickListener {
//        mainScope.launch {
//            log(1)
//            textView.text = async(Dispatchers.IO) {
//                log(2)
//                delay(1000)
//                log(3)
//                "Hello1111"
//            }
//        }.await()
//        log(4)
//    }
//    cancelButton.setOnClickListener {
//        mainScope.cancel()
//        log("MainScope is cancelled.")
//    }
}

/**
 * 谨慎使用 GlobalScope: GlobalScope 不会继承外部作用域，因此大家使用时一定要注意，
 *                      如果在使用了绑定生命周期的 MainScope 之后，内部再使用 GlobalScope 启动协程，
 *                      意味着 MainScope 就不会起到应有的作用
 *
 */
suspend fun main() {
    android1()
}