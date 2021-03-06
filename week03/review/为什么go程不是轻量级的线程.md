# 为什么go程不是轻量级的线程

[Why goroutines are not lightweight threads?](https://link.medium.com/y4GFC6xs3V)

GoLang近期令人难以置信的普及。其中最重要的一个原因就是它给开发者提供了goroutines和channels这种简单且轻量级的并发技术。

在很久以前几乎所有的应用都是通过线程来支撑并发任务。在理解为什么go程不是轻量级的线程之前，我们首先要理解线程在操作系统中是怎么运行的。

## 什么是线程？

一个线程就是一个能被处理器独立执行的指令序列。线程比进程更轻量级因此可以开启大量的线程。现实中这样的就应用代表就是web服务器。

web服务器设计之初就是可以同时接收多个请求。并且那些请求相互间不影响。因此一个线程可以被创建（或者从线程池取出）并且可以委派给请求。现代的处理器都可以同时运行多个线程，也可以切换到获取线程来并行。

## 所有的线程都比进程轻量级？

是也不是。概念上，

1. 线程共享内存并且不需要创建一个新的虚拟内存空间，当创建的时候也不需要MMU(内存管理单元)和上下文切换
2. 线程间通更简单的类似共享内存比起进程间的各种各样的IPC(内部处理通讯)类似于信号量、消息队列、管道等。

话虽这么说，在多核处理器下这并不能保证性能一定好过进程。

例如Linux从来不区分进程和线程，都统称为任务。每个任务被克隆的时候都有一个最大最小共享等级。当调用fork()时，一个新的没有共享文件描述符、父进程ID和内存空间的任务就会被创建。当使用pthread_create()，这个新的任务就带有上面所有的属性。

因此，运行不同的独立的内存中，在共享内存同步数据是比起L1缓存运行在多个核心下会付出更大的代价。Linux开发者在任务切换中视图减少损耗。创建一个新的任务比起一个新的线程来说仍然是一个不小的开销。

## 在线程中能提高什么？

有以下三点会造成线程变慢：

1. 线程会从他们的栈空间消耗不少的内存(>=1MB)。所以创建1000个以上的线程意思至少需要1GB的内存
2. 线程需要还原一些寄存器包括AVX,SSE,FPR,PC,SP都会降低应用的性能
3. 线程的开启和清理都需要调用系统资源（例如内存）

## Goroutines

Goroutines是存在于go运行时的虚拟空间而没有存在于操作系统中。因此，go运行时调度程序需要管理他们的生命周期。

为这个目的Go运行时需要维护3C结构：

1. 结构G：这个代表单独的go程和它的属性类似于栈指针，栈基础，ID，缓存和状态
2. 结构M：这个代表系统级线程。它包含了一个指针指向全局的可运行go程的队列，当前运行的go程和调度程序的引用
3. 结构Sched：这是一个全局的结构并且包含了空闲和等待的go程

因此，当启动的时候，go运行时会启动一些go程给GC，任务调度和用户代码。一个系统级线程可以创建很多go程。这些线程可以接近GOMAXPROCS。

## 从底层开始！

一个go程创建的时候初始化需要2KB的栈空间。go里面的每个函数在需要更多栈空间时都会检查，如果不满足就会从其他的区域拷贝2倍于当前内存。这就会让go程占用资源变得非常轻量级。

## 阻塞是好的！

如果一个go程在系统调用时阻塞了，它就会阻塞运行的线程。但是另一个正在等待的线程就会被调度程序调度到需要运行的go程中。但是，如果你使用在go中使用channels进行通讯，只会存在于虚拟空间中，不会阻塞系统级线程。这就是go程简单使用方法。

## 不要中断！

go运行时调度器会合作调度，也就是说当另一个go程整准备调度但是当前的一个是阻塞状态或者挂起了。下面试这些可能涉及到的：

- Channel接受和发送操作，操作也有可能被阻塞
- go的声明，尽管这没有保证但是一个新的go程也会立即调度
- 阻塞调用一般就像file或者网络
- 在垃圾回收周期之后

这比起系统调度的调度器好的多，新的线程去认领一个任务或者在低优先级任务运行时一个优先级更高的任务需要调度。另一个高级特性就是，当代码中显式的调用时，当休眠或者channel等待的时候，编译器仅仅需要还原寄存器活动的点。在Go中，这就意味着只有3个寄存器例如PC,SP和DX需要更新当时的上下文并切换。

