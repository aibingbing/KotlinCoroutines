package CoroutineSelect

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.selects.select
import kotlin.random.Random


fun CoroutineScope.getUserFromApi(login: String) = async(Dispatchers.IO) {
//    gitHubServiceApi.getUserSuspend(login)
}

fun CoroutineScope.getUserFromLocal(login: String) = async(Dispatchers.IO) {
//    File("", login).takeIf { it.exists() }?.readText()?.let { Gson.formJson(it, User::class.java) }
}

suspend fun select1() {
//   GlobalScope.launch {
//       val login = ""
//       val localDeferred = getUserFromLocal(login)
//       val remoteDeferred = getUserFromApi(login)
//
//       val userResponse = select<Response<User?>> {
//           localDeferred.onAwait { Response(it, true)}
//           remoteDeferred.onAwait { Response(it, false)}
//       }
//
//       userResponse.value?.let {
//           log(it)
//       }
//       userResponse.isLocal.takeIf { it }?.let {
//           val userFromApi = remoteDeferred.await()
//           cacheUser(login, userFromApi)
//           log(userFromApi)
//       }
//   }.join()
}

suspend fun select2() {
    GlobalScope.launch {
        val channels = List(10) {
            Channel<Int>()
        }
        channels.forEach {
            it.send(Random(100).nextInt())
        }
        select<Int?> {
            channels.forEach { channel ->
                channel.onReceive { it }
//            channel.onReceiveOrNull() {it}
            }
        }
    }.join()
}

/**
 * Kotlin 协程 Select 多路复用
 */
/**
 * 我们怎么知道哪些事件可以被 select 呢？其实所有能够被 select 的事件都是 SelectClauseN 类型，包括：
 * 因此如果大家想要确认挂起函数是否支持 select，只需要查看其是否存在对应的 SelectClauseN 即可
 */
/**
 * 在协程当中，Select 的语义与 Java NIO 或者 Unix 的 IO 多路复用类似，
 * 它的存在使得我们可以轻松实现 1 拖 N，实现哪个先来就处理哪个。
 * 尽管 Select 和 Channel 比起标准库的协程 API 已经更接近业务开发了，
 * 不过个人认为它们仍属于相对底层的 API 封装，在实践当中多数情况下也可以使用 Flow API 来解决
 */
suspend fun main() {
//    select1()
    select2()
}