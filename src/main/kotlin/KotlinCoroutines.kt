import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select
import java.io.IOException
import java.lang.ArithmeticException
import java.lang.AssertionError
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/**
 * Copyright: ETU (Beijing) Education Technology Co., Ltd. All rights reserved.
 * Author: aibingbing <br>
 * Date: 2018/12/5 <br>
 * Desc: <br>
 * Edit History: <br>
 */
fun main(args: Array<String>) {
    GlobalScope.launch {
        delay(1000L)
        println("${Thread.currentThread().name}: World")
    }
    println("${Thread.currentThread().name}: Hello")
    Thread.sleep(2000L)
    println("${Thread.currentThread().name}: Finish!")

}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    launch {
//        delay(1000L)
//        println("${Thread.currentThread().name} World!")
//    }
//    println("${Thread.currentThread().name} Hello,")
//    delay(2000L)
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val job = launch(Dispatchers.IO) {
//        delay(1000L)
//        println("${Thread.currentThread().name} World!")
//    }
//    println("${Thread.currentThread().name} Hello,")
//    job.join()
//    println("${Thread.currentThread().name} Finish!")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val job = launch(Dispatchers.IO) {
//        delay(1000L)
//        println("${Thread.currentThread().name} World!")
//    }
//    println("${Thread.currentThread().name} Hello,")
//    job.cancel()
//    println("${Thread.currentThread().name} Finish!")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val jobs = List(100_000) {
//        launch {
//            delay(1000L)
//            println("${Thread.currentThread().name} . ")
//        }
//    }
//    jobs.forEach {
//        it.join()
//    }
//    println("${Thread.currentThread().name} Finish!")
//}

////-----------------------------------------------------------------
//fun main() {
//    val job = GlobalScope.async {
//        delay(1000L)
//        println("${Thread.currentThread().name} : World")
//    }
//    println("${Thread.currentThread().name} Hello!")
//
//    runBlocking {
//        delay(2000L)
//    }
//    println("${Thread.currentThread().name} Finish!")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    GlobalScope.launch {
//        delay(1000L)
//        println("${Thread.currentThread().name} : World")
//    }
//    println("${Thread.currentThread().name} Hello!")
//
//    delay(2000L)
//    println("${Thread.currentThread().name} Finish!")
//}


////-----------------------------------------------------------------
//fun main() = runBlocking {
//    launch {
//        delay(1000L)
//        println("${Thread.currentThread().name} : launch")
//    }
//    coroutineScope {
//        launch {
//            delay(2000L)
//            println("${Thread.currentThread().name} coroutineScope-launch!")
//        }
//        delay(1000L)
//        println("${Thread.currentThread().name} coroutineScope")
//    }
//
//    println("${Thread.currentThread().name} Finish!")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    launch {
//        doWorld()
//    }
//    println("${Thread.currentThread().name} Hello!")
//}
//
//suspend fun doWorld() {
//    delay(1000L)
//    println("${Thread.currentThread().name} World!")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    repeat(100) {
//        println("${Thread.currentThread().name} $it")
//        delay(500L)
//    }
//    delay(1300L)
//    println("${Thread.currentThread().name} Finish!")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val job = launch {
//        repeat(100) {
//            println("${Thread.currentThread().name} $it")
//            delay(500L)
//        }
//    }
//    delay(2000L)
//    println("${Thread.currentThread().name} waiting!")
//    job.cancel()
//    job.join()
//    println("${Thread.currentThread().name} Finish!")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//    val job = launch(Dispatchers.Default) {
//        var nextPrintTime = startTime
//        var i = 0
//        while (i < 5) { // computation loop, just wastes CPU
//            // print a message twice a second
//            if (System.currentTimeMillis() >= nextPrintTime) {
//                println("I'm sleeping ${i++} ...")
//                nextPrintTime += 500L
//            }
//        }
//    }
//    delay(1300L) // delay a bit
//    println("main: I'm tired of waiting!")
//    job.cancelAndJoin() // cancels the job and waits for its completion
//    println("main: Now I can quit.")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//    val job = launch(Dispatchers.Default) {
//        var nextPrintTime = startTime
//        var i = 0
//        while (isActive) { // cancellable computation loop
//            // print a message twice a second
//            if (System.currentTimeMillis() >= nextPrintTime) {
//                println("I'm sleeping ${i++} ...")
//                nextPrintTime += 500L
//            }
//        }
//    }
//    delay(1300L) // delay a bit
//    println("main: I'm tired of waiting!")
//    job.cancelAndJoin() // cancels the job and waits for its completion
//    println("main: Now I can quit.")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val job = launch {
//        try {
//            repeat(1000) {
//                println("i am sleeping $it ...")
//                delay(500)
//            }
//        } finally {
//            println("i am running finally")
//        }
//    }
//    delay(1300L)
//    println("main: I'm tired of waiting!")
//    //job.cancel()
//    job.cancelAndJoin() // cancels the job and waits for its completion
//    println("main: Now I can quit.")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val job = launch {
//        try {
//            repeat(1000) { i ->
//                println("I'm sleeping $i ...")
//                delay(500L)
//            }
//        } finally {
//            withContext(NonCancellable) {
//                println("I'm running finally")
//                delay(1000L)
//                println("And I've just delayed for 1 sec because I'm non-cancellable")
//            }
//        }
//    }
//    delay(1300L) // delay a bit
//    println("main: I'm tired of waiting!")
//    job.cancelAndJoin() // cancels the job and waits for its completion
//    println("main: Now I can quit.")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    withTimeout(1300L) {
//        repeat(1000) {
//            println("I'm sleeping $it ...")
//            delay(500L)
//        }
//    }
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    try {
//        withTimeout(1300L) {
//            repeat(1000) {
//                println("I'm sleeping $it ...")
//                delay(500L)
//            }
//        }
//    } catch (e: TimeoutCancellationException) {
//        println(e)
//    }
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val result = withTimeoutOrNull(1300L) {
//        repeat(1000) {
//            println("I'm sleeping $it ...")
//            delay(500L)
//        }
//        "Done"
//    }
//    println("Result is $result")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val result = withTimeoutOrNull(1300L) {
//        repeat(2) {
//            println("I'm sleeping $it ...")
//            delay(500L)
//        }
//        "Done"
//    }
//    println("Result is $result")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val channel = Channel<Int>()
//    launch {
//        // this might be heavy CPU-consuming computation or async logic, we'll just send five squares
//        for (x in 1..5) channel.send(x * x)
//    }
//// here we print five received integers:
//    repeat(5) { println(channel.receive()) }
//    println("Done!")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val channel = Channel<Int>()
//    launch {
//        for (x in 1..5) channel.send(x * x)
//        channel.close() // we're done sending
//    }
//// here we print received values using `for` loop (until the channel is closed)
//    for (y in channel) println(y)
//    println("Done!")
//}


////-----------------------------------------------------------------
//fun CoroutineScope.produceSquares1(): ReceiveChannel<Int> = produce {
//    for (x in 1..5) send(x * x)
//}
//fun main() = runBlocking {
//    val squares = produceSquares1()
//    squares.consumeEach { println(it) }
//    println("Done!")
//}

////-----------------------------------------------------------------
//fun CoroutineScope.produceNumbers() = produce<Int> {
//    var x = 1
//    while (true) send(x++) // infinite stream of integers starting from 1
//}
//fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
//    for (x in numbers) send(x * x)
//}
//fun main() = runBlocking {
//    val numbers = produceNumbers() // produces integers from 1 and on
//    val squares = square(numbers) // squares integers
//    for (i in 1..5) println(squares.receive()) // print first five
//    println("Done!") // we are done
//    coroutineContext.cancelChildren() // cancel children coroutines
//}

////-----------------------------------------------------------------
//fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
//    var x = start
//    while (true) send(x++) // infinite stream of integers from start
//}
//fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
//    for (x in numbers) if (x % prime != 0) send(x)
//}
//fun main() = runBlocking {
//    var cur = numbersFrom(2)
//    for (i in 1..10) {
//        val prime = cur.receive()
//        println(prime)
//        cur = filter(cur, prime)
//    }
//    coroutineContext.cancelChildren() // cancel all children to let main finish
//}

////-----------------------------------------------------------------
//suspend fun doSomethingUsefulOne(): Int {
//    delay(1000L)
//    return 13
//}
//suspend fun doSomethingUsefulTwo(): Int {
//    delay(2000L)
//    return 29
//}
//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        val one = doSomethingUsefulOne()
//        val two = doSomethingUsefulTwo()
//        println("The answer is ${one + two}")
//    }
//    println("Completed in $time ms")
//}

////-----------------------------------------------------------------
//suspend fun doSomethingUsefulOne(): Int {
//    delay(1000L)
//    return 13
//}
//suspend fun doSomethingUsefulTwo(): Int {
//    delay(2000L)
//    return 29
//}
//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        val one = async { doSomethingUsefulOne()}.await()
//        val two = async { doSomethingUsefulTwo()}.await()
//        println("The answer is ${one + two}")
//    }
//    println("Completed in $time ms")
//}

////-----------------------------------------------------------------
//suspend fun doSomethingUsefulOne(): Int {
//    delay(1000L)
//    return 13
//}
//suspend fun doSomethingUsefulTwo(): Int {
//    delay(2000L)
//    return 29
//}
//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        val one = async { doSomethingUsefulOne()}
//        val two = async { doSomethingUsefulTwo()}
//        println("The answer is ${one.await() + two.await()}")
//    }
//    println("Completed in $time ms")
//}

////-----------------------------------------------------------------
//suspend fun doSomethingUsefulOne(): Int {
//    delay(1000L)
//    return 13
//}
//suspend fun doSomethingUsefulTwo(): Int {
//    delay(2000L)
//    return 29
//}
//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
//        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
//        // some computation
//        one.start() // start the first one
//        two.start() // start the second one
//        println("The answer is ${one.await() + two.await()}")
//    }
//    println("Completed in $time ms")
//}

////-----------------------------------------------------------------
//suspend fun doSomethingUsefulOne(): Int {
//    delay(1000L)
//    return 13
//}
//suspend fun doSomethingUsefulTwo(): Int {
//    delay(2000L)
//    return 29
//}
//// The result type of somethingUsefulOneAsync is Deferred<Int>
//fun somethingUsefulOneAsync() = GlobalScope.async {
//    doSomethingUsefulOne()
//}
//// The result type of somethingUsefulTwoAsync is Deferred<Int>
//fun somethingUsefulTwoAsync() = GlobalScope.async {
//    doSomethingUsefulTwo()
//}
//fun main(){
//    val time = measureTimeMillis {
//        val one = somethingUsefulOneAsync()
//        val two = somethingUsefulTwoAsync()
//        runBlocking {
//            println("The answer is ${one.await() + two.await()}")
//        }
//    }
//    println("Completed in $time ms")
//}

////-----------------------------------------------------------------
//suspend fun doSomethingUsefulOne(): Int {
//    delay(1000L)
//    return 13
//}
//suspend fun doSomethingUsefulTwo(): Int {
//    delay(2000L)
//    return 29
//}
//// The result type of somethingUsefulOneAsync is Deferred<Int>
//fun somethingUsefulOneAsync() = GlobalScope.async {
//    doSomethingUsefulOne()
//}
//// The result type of somethingUsefulTwoAsync is Deferred<Int>
//fun somethingUsefulTwoAsync() = GlobalScope.async {
//    doSomethingUsefulTwo()
//}
//suspend fun concurrentSum():Int = coroutineScope {
//    val one = somethingUsefulOneAsync()
//    val two = somethingUsefulTwoAsync()
//    one.await() + two.await()
//}
//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        println("The answer is ${concurrentSum()}")
//    }
//    println("Completed in $time ms")
//}

////-----------------------------------------------------------------
//suspend fun failedConcurrentSum():Int = coroutineScope {
//    val one = async<Int> {
//        try {
//            delay(Long.MAX_VALUE)
//            42
//        } finally {
//            println("First child was cancelled")
//        }
//    }
//    val two = async<Int> {
//        println("Second child throws an exception")
//         throw ArithmeticException()
//    }
//    one.await() + two.await()
//}
//fun main() = runBlocking<Unit> {
//    try {
//        failedConcurrentSum()
//    } catch (e: ArithmeticException) {
//        println("Computation failed with ArithmeticException")
//    }
//}

////-----------------------------------------------------------------
//fun main() = runBlocking<Unit> {
//    launch { // context of the parent, main runBlocking coroutine
//        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
//    }
//    launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
//        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
//    }
//    launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
//        println("Default               : I'm working in thread ${Thread.currentThread().name}")
//    }
//    launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
//        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
//    }
//}

////-----------------------------------------------------------------
//fun main()  = runBlocking<Unit>(Dispatchers.IO){
//    launch { // context of the parent, main runBlocking coroutine
//        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
//    }
//    launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
//        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
//    }
//    launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
//        println("Default               : I'm working in thread ${Thread.currentThread().name}")
//    }
//    launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
//        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
//    }
//}

////-----------------------------------------------------------------
//fun main() = runBlocking<Unit> {
//    launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
//        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
//        delay(500)
//        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
//    }
//    launch { // context of the parent, main runBlocking coroutine
//        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
//        delay(1000)
//        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
//    }
//}

////-----------------------------------------------------------------
//fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
//fun main() = runBlocking<Unit> {
//    val a = async {
//        log("I'm computing a piece of the answer")
//        6
//    }
//    val b = async {
//        log("I'm computing another piece of the answer")
//        7
//    }
//    println("The answer is ${a.await() * b.await()}")
//}

////-----------------------------------------------------------------
//fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
//fun main() = runBlocking<Unit> {
//    newSingleThreadContext("Ctx1").use { ctx1 ->
//        newSingleThreadContext("Ctx2").use { ctx2 ->
//            runBlocking(ctx1) {
//                log("Started in ctx1")
//                withContext(ctx2) {
//                    log("Working in ctx2")
//                }
//                log("Back to ctx1")
//            }
//        }
//    }
//    println(" My job is ${coroutineContext[Job]} ")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking<Unit> {
//    // launch a coroutine to process some kind of incoming request
//    val request = launch {
//        // it spawns two other jobs, one with GlobalScope
//        GlobalScope.launch {
//            println("job1: I run in GlobalScope and execute independently!")
//            delay(1000)
//            println("job1: I am not affected by cancellation of the request")
//        }
//        // and the other inherits the parent context
//        launch {
//            delay(100)
//            println("job2: I am a child of the request coroutine")
//            delay(1000)
//            println("job2: I will not execute this line if my parent request is cancelled")
//        }
//    }
//    delay(500)
//    request.cancel() // cancel processing of the request
//    delay(1000) // delay a second to see what happens
//    println("main: Who has survived request cancellation?")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking<Unit> {
//    // launch a coroutine to process some kind of incoming request
//    val request = launch {
//        // it spawns two other jobs, one with GlobalScope
//        GlobalScope.launch {
//            try {
//                println("job1: I run in GlobalScope and execute independently!")
//                delay(1000)
//                println("job1: I am not affected by cancellation of the request")
//            } finally {
//                println("job1:Finally")
//            }
//        }
//        // and the other inherits the parent context
//        launch {
//            try {
//                delay(100)
//                println("job2: I am a child of the request coroutine")
//                delay(1000)
//                println("job2: I will not execute this line if my parent request is cancelled")
//            }finally {
//                println("job2:Finally")
//            }
//        }
//    }
//    delay(500)
//    request.cancel() // cancel processing of the request
//    delay(1000) // delay a second to see what happens
//    println("main: Who has survived request cancellation?")
//}

////-----------------------------------------------------------------
//fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
//fun  main() = runBlocking {
//    log("Started main coroutine")
//// run two background value computations
//    val v1 = async(CoroutineName("v1coroutine")) {
//        delay(500)
//        log("Computing v1")
//        252
//    }
//    val v2 = async(CoroutineName("v2coroutine")) {
//        delay(1000)
//        log("Computing v2")
//        6
//    }
//    log("The answer for v1 / v2 = ${v1.await() / v2.await()}")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking<Unit> {
//    launch(Dispatchers.Default + CoroutineName("test")) {
//        println("I'm working in thread ${Thread.currentThread().name}")
//    }
//}

////-----------------------------------------------------------------
//val threadLocal = ThreadLocal<String?>() // declare thread-local variable
//fun main() = runBlocking<Unit> {
//    threadLocal.set("main")
//    println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
//    val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
//        println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
//        yield()
//        println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
//    }
//    job.join()
//    println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val job = GlobalScope.launch(CoroutineName("launch")) {
//        println("Throwing exception from launch")
//        throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
//    }
//    job.join()
//    println("Joined failed job")
//    val deferred = GlobalScope.async(CoroutineName("async")) {
//        println("Throwing exception from async")
//        throw ArithmeticException() // Nothing is printed, relying on user to call await
//    }
//    try {
//        deferred.await()
//        println("Unreached")
//    } catch (e: ArithmeticException) {
//        println("Caught ArithmeticException")
//    }
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val handler = CoroutineExceptionHandler { _, throwable ->
//        println("Caught $throwable")
//    }
//    val job = GlobalScope.launch(handler) {
//        throw AssertionError()
//    }
//    val deferred = GlobalScope.async(handler) {
//        throw ArithmeticException()
//    }
//    joinAll(job, deferred)
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val job = launch {
//        val child = launch {
//            try {
//                delay(Long.MAX_VALUE)
//            } finally {
//                println("Child is cancelled")
//            }
//        }
//        yield()
//        println("Cancelling child")
//        child.cancel()
//        child.join()
//        yield()
//        println("Parent is not cancelled")
//    }
//    job.join()
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val handler = CoroutineExceptionHandler { _, exception ->
//        println("Caught $exception")
//    }
//    val job = GlobalScope.launch(handler) {
//        launch { // the first child
//            try {
//                delay(Long.MAX_VALUE)
//            } finally {
//                withContext(NonCancellable) {
//                    println("Children are cancelled, but exception is not handled until all children terminate")
//                    delay(100)
//                    println("The first child finished its non cancellable block")
//                }
//            }
//        }
//        launch { // the second child
//            delay(10)
//            println("Second child throws an exception")
//            throw ArithmeticException()
//        }
//    }
//    job.join()
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val handler = CoroutineExceptionHandler { _, exception ->
//        println("Caught $exception with suppressed ${exception.suppressed.contentToString()}")
//    }
//    val job = GlobalScope.launch(handler) {
//        launch {
//            try {
//                delay(Long.MAX_VALUE)
//            } finally {
//                throw ArithmeticException()
//            }
//        }
//        launch {
//            delay(100)
//            throw IOException()
//            println("will not execute here")
//        }
//        delay(Long.MAX_VALUE)
//    }
//    job.join()
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val handler = CoroutineExceptionHandler { _, exception ->
//        println("Caught original $exception")
//    }
//    val job = GlobalScope.launch(handler) {
//        val inner = launch {
//            launch {
//                launch {
//                    throw IOException()
//                }
//            }
//        }
//        try {
//            inner.join()
//        } catch (e: CancellationException) {
//            println("Rethrowing CancellationException with original cause")
//            throw e
//        }
//    }
//    job.join()
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    val supervisor = SupervisorJob()
//    with(CoroutineScope(coroutineContext + supervisor)) {
//        // launch the first child -- its exception is ignored for this example (don't do this in practice!)
//        val firstChild = launch(CoroutineExceptionHandler { _, _ ->  }) {
//            println("First child is failing")
//            throw AssertionError("First child is cancelled")
//        }
//        // launch the second child
//        val secondChild = launch {
//            firstChild.join()
//            // Cancellation of the first child is not propagated to the second child
//            println("First child is cancelled: ${firstChild.isCancelled}, but second one is still active")
//            try {
//                delay(Long.MAX_VALUE)
//            } finally {
//                // But cancellation of the supervisor is propagated
//                println("Second child is cancelled because supervisor is cancelled")
//            }
//        }
//        // wait until the first child fails & completes
//        firstChild.join()
//        println("Cancelling supervisor")
//        supervisor.cancel()
//        secondChild.join()
//    }
//}

////-----------------------------------------------------------------
//fun main() = runBlocking {
//    try {
//        supervisorScope {
//            val child = launch {
//                try {
//                    println("Child is sleeping")
//                    delay(Long.MAX_VALUE)
//                } finally {
//                    println("Child is cancelled")
//                }
//            }
//            // Give our child a chance to execute and print using yield
//            yield()
//            println("Throwing exception from scope")
//            throw AssertionError()
//        }
//    } catch(e: AssertionError) {
//        println("Caught assertion error")
//    }
//}

////-----------------------------------------------------------------
//fun CoroutineScope.fizz() = produce<String> {
//    while (true) { // sends "Fizz" every 300 ms
//        delay(300)
//        send("Fizz")
//    }
//}
//fun CoroutineScope.buzz() = produce<String> {
//    while (true) { // sends "Buzz!" every 500 ms
//        delay(500)
//        send("Buzz!")
//    }
//}
//suspend fun selectFizzBuzz(fizz: ReceiveChannel<String>, buzz: ReceiveChannel<String>) {
//    select<Unit> { // <Unit> means that this select expression does not produce any result
//        fizz.onReceive { value ->  // this is the first select clause
//            println("fizz -> '$value'")
//        }
//        buzz.onReceive { value ->  // this is the second select clause
//            println("buzz -> '$value'")
//        }
//    }
//}
//fun main() = runBlocking {
//    val fizz = fizz()
//    val buzz = buzz()
//    repeat(7) {
//        selectFizzBuzz(fizz, buzz)
//    }
//    coroutineContext.cancelChildren() // cancel fizz & buzz coroutines
//}

////-----------------------------------------------------------------
//suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>): String =
//    select<String> {
//        a.onReceiveOrNull { value ->
//            if (value == null)
//                "Channel 'a' is closed"
//            else
//                "a -> '$value'"
//        }
//        b.onReceiveOrNull { value ->
//            if (value == null)
//                "Channel 'b' is closed"
//            else
//                "b -> '$value'"
//        }
//    }
//fun main() = runBlocking {
//    val a = produce<String> {
//        repeat(4) { send("Hello $it") }
//    }
//    val b = produce<String> {
//        repeat(4) { send("World $it") }
//    }
//    repeat(8) { // print first eight results
//        println(selectAorB(a, b))
//    }
//    coroutineContext.cancelChildren()
//}

////-----------------------------------------------------------------
//fun CoroutineScope.asyncString(time: Int) = async {
//    delay(time.toLong())
//    "Waited for $time ms"
//}
//fun CoroutineScope.asyncStringsList(): List<Deferred<String>> {
//    val random = Random(3)
//    return List(12) { asyncString(random.nextInt(1000)) }
//}
//fun main() = runBlocking {
//    val list = asyncStringsList()
//    val result = select<String> {
//        list.withIndex().forEach { (index, deferred) ->
//            deferred.onAwait { answer ->
//                "Deferred $index produced answer '$answer'"
//            }
//        }
//    }
//    println(result)
//    val countActive = list.count { it.isActive }
//    println("$countActive coroutines are still active")
//}

////-----------------------------------------------------------------
//suspend fun CoroutineScope.massiveRun1(action: suspend () -> Unit) {
//    val n = 100  // number of coroutines to launch
//    val k = 1000 // times an action is repeated by each coroutine
//    val time = measureTimeMillis {
//        val jobs = List(n) {
//            launch {
//                repeat(k) { action() }
//            }
//        }
//        jobs.forEach { it.join() }
//    }
//    println("Completed ${n * k} actions in $time ms")
//}
//@Volatile // in Kotlin `volatile` is an annotation
//var counter = 0
//fun main() = runBlocking<Unit> {
//    GlobalScope.massiveRun1 {
//        counter++
//    }
//    println("Counter = $counter")
//}

////-----------------------------------------------------------------
//suspend fun CoroutineScope.massiveRun1(action: suspend () -> Unit) {
//    val n = 100  // number of coroutines to launch
//    val k = 1000 // times an action is repeated by each coroutine
//    val time = measureTimeMillis {
//        val jobs = List(n) {
//            launch {
//                repeat(k) { action() }
//            }
//        }
//        jobs.forEach { it.join() }
//    }
//    println("Completed ${n * k} actions in $time ms")
//}
//var counter = AtomicInteger()
//fun main() = runBlocking<Unit> {
//    GlobalScope.massiveRun1 {
//        counter.incrementAndGet()
//    }
//    println("Counter = $counter")
//}

////-----------------------------------------------------------------
//suspend fun CoroutineScope.massiveRun1(action: suspend () -> Unit) {
//    val n = 100  // number of coroutines to launch
//    val k = 1000 // times an action is repeated by each coroutine
//    val time = measureTimeMillis {
//        val jobs = List(n) {
//            launch {
//                repeat(k) { action() }
//            }
//        }
//        jobs.forEach { it.join() }
//    }
//    println("Completed ${n * k} actions in $time ms")
//}
//val counterContext = newSingleThreadContext("CounterContext")
//var counter = 0
//fun main() = runBlocking<Unit> {
//    GlobalScope.massiveRun1 {
//        withContext(counterContext) {
//            counter++
//        }
//    }
//    println("Counter = $counter")
//}

////-----------------------------------------------------------------
//suspend fun CoroutineScope.massiveRun1(action: suspend () -> Unit) {
//    val n = 100  // number of coroutines to launch
//    val k = 1000 // times an action is repeated by each coroutine
//    val time = measureTimeMillis {
//        val jobs = List(n) {
//            launch {
//                repeat(k) { action() }
//            }
//        }
//        jobs.forEach { it.join() }
//    }
//    println("Completed ${n * k} actions in $time ms")
//}
//val counterContext = newSingleThreadContext("CounterContext")
//var counter = 0
//fun main() = runBlocking<Unit> {
//    CoroutineScope(counterContext).massiveRun1 {
//            counter++
//    }
//    println("Counter = $counter")
//}

////-----------------------------------------------------------------
//// Message types for counterActor
//sealed class CounterMsg
//object IncCounter : CounterMsg() // one-way message to increment counter
//class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // a request with reply
//// This function launches a new counter actor
//fun CoroutineScope.counterActor() = actor<CounterMsg> {
//    var counter = 0 // actor state
//    for (msg in channel) { // iterate over incoming messages
//        when (msg) {
//            is IncCounter -> counter++
//            is GetCounter -> msg.response.complete(counter)
//        }
//    }
//}
//suspend fun CoroutineScope.massiveRun1(action: suspend () -> Unit) {
//    val n = 100  // number of coroutines to launch
//    val k = 1000 // times an action is repeated by each coroutine
//    val time = measureTimeMillis {
//        val jobs = List(n) {
//            launch {
//                repeat(k) { action() }
//            }
//        }
//        jobs.forEach { it.join() }
//    }
//    println("Completed ${n * k} actions in $time ms")
//}
//fun main() = runBlocking {
//    val counter = counterActor() // create the actor
//    GlobalScope.massiveRun1 {
//        counter.send(IncCounter)
//    }
//// send a message to get a counter value from an actor
//    val response = CompletableDeferred<Int>()
//    counter.send(GetCounter(response))
//    println("Counter = ${response.await()}")
//    counter.close() // shutdown the actor
//}