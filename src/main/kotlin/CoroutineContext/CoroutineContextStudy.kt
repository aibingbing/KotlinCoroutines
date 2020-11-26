package CoroutineContext

import CoroutineStart.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

suspend inline fun Job.Key.currentJob() = coroutineContext[Job]

suspend fun coroutineJob() {
    GlobalScope.launch {
        log(Job.currentJob())
    }
    log(Job.currentJob())
}

suspend fun coroutineName() {
    GlobalScope.launch(CoroutineName("HelloWorld")) {
        log(Job.currentJob())
    }
    log(Job.currentJob())
}

suspend fun main() {
//    coroutineJob()
    coroutineName()
}