# Zookeeper-Overview

[ZookeeperOverview](http://zookeeper.apache.org/doc/r3.4.14/zookeeperOver.html)

## Zookeeper: 一个分布式应用的分布式协调服务

Zookeeper是一个开源的分布式服务用来协调分布式应用。它提供了一整套简单的同步、配置维护、分组和命名来给分布式应用程序使用。它被设计出来操作、变成就像操作熟悉的文件系统目录树。它在Java中运行同时绑定Java和C。

协调服务是众所周知的很难的保持正确。特别是倾向于出错比如竞态条件和死锁。Zookeeper出现的动机就是减轻分布式应用需要实现服务协调的责任。

## 设计目标

Zookeeper是简单的。Zookeeper允许分布式进程通过一个共享体系的命名空间就像标准的文件系统一样，互相之间可以协调。命名空间由数据寄存器组成-用Zookeeper的说法叫做znodes。严格的顺序意味着复杂的同步基元可以由客户端来实现。

Zookeeper是重复的。就像分布式进程互相协调一样，通过一套主机称为全部，Zookeeper故意被设计成重复的。

<img src="image/zkservice_1.jpg" alt="zk_1" width="600" />

确保Zookeeper服务必须知道每一个服务。他们维护了一个内存镜像的状态，通过可持久化存储记录事务日志和快照。和大多数可用的服务一样，Zookeeper服务也是可用的。

客户端连接到单个的Zookeeper服务。客户端维护了一个TCP连接用来发送消息，接收消息，获取观察时间，发送心跳包。如果TCP连接断掉了，客户端将会连接到另外一个服务。

Zookeeper是有序的。Zookeeper的修改体现在Zookeeper事务所有的顺序。后来的操作可以使用顺序来实现高等级的抽象，就像同步基元。

Zookeeper是快速的。特别是在读取多的工作负荷下。Zookeeper应用运行在成千台机器上，在读取多余写的情况下能提供很好的性能，比例大约是10：1.

## 数据模型和分层命名空间

Zookeeper的命名空间和标准文件系统很类似。一个名字就是被序列的路径元素(/)分割。每一个Zookeeper命名空间的节点都是被指定为路径。

<img src="image/zknamespace.jpg" alt="zk_2" width="400" />

## 节点和临时节点

不像标准文件系统，Zookeeper命名空间的节点可以和子节点很好的关联。它就像有一个文件系统允许文件同时也是一个目录。（Zookeeper被设计来存储竞态类数据：状态信息，配置，位置信息等，因此每个节点存储的都比较小，大概在kb范围）。我们使用术语znode来清晰的描述Zookeeper的节点。

Znode维护了一个统计结构，包含了数据变更的版本号，访问控制列表，时间戳和缓存生效和协作更新。每一次znode数据变更后，版本号就会增加。例如，任何时候客户端接收数据都会接收版本号。

在命名空间中读写数据都会自动存储在znode中。读取数据会从关联的znode中获取，写数据会替换所有的数据。每个节点都有访问控制列表来决定哪个可以做什么。

Zookeeper也有一个临时节点的概念。那些znodes在创建znode成为活动的会和session一致存在。当session失效的时候znode也会删掉。当你要实现他们的时候临时节点很有用。

## 条件更新和观察

Zookeeper支持观察的概念。客户端可以在znode上设置一个观察点。当znode变更的时候观察点可以触发和删除。当观察点触发时，客户端就会收到一个包告诉它znode已经被修改了。如果客户端和Zookeeper服务间的连接断掉后，客户端将收到一个局部通知。

## 确定性

Zookeeper是非常快速和简单的。从它设计之初，就为了成为构建复杂服务的基础，比如同步化，提供了几点来保证。

- 连续一致性-在命令发送后才会从客户端更新
- 原子的-更新要么成功要么失败。不存在不确定的状态
- 单系统镜像-不管服务有没有连接客户端将看到相同的视图
- 可靠性-当一个变更申请后，它将会持续到客户端重新这个更新
- 时效性-系统的客户端视图会被最新的时间来约束

## 简单API

Zookeeper其中一个设计目标就是提供一个简单的编程接口。结果，支持下面这些操作：

- create：从树状存储中创建一个节点
- delete：删除一个节点
- exists：判断节点是否在存储中
- get data：从节点获取数据
- set data：写数据到节点
- get children：获取节点的子节点列表
- sync：等待数据传输

## 实现

Zookeeper组件展示了服务的高级组件。请求处理程序带有异常处理，每个服务确保Zookeeper服务复制每一个组件。

<img src="image/zkcomponents.jpg" alt="zk_3" width="600" />

复制数据库包含整个数据树的内存数据库。所有的修改都记录到磁盘上以便恢复，写操作在提交到内存库之前先会序列化到磁盘上。

每个Zookeeper服务客户端。客户端可以精确的提交请求到服务端。从每个服务数据库的副本读取请求。请求可以改变服务状态，写消息，用一致性协议来处理。

作为一致性协议的一部分所有请求都是从客户端发送到单个服务端，被称为领导者。剩下的Zookeeper服务，被称为追随者，从领导者那获取提议最终达成一致。消息层在失败的时候会替换调领导者，然后同步追随者到新的领导者。

Zookeeper使用了约定原子消息协议。从消息层以来就是原子的，Zookeeper能保证本地副本不能偏离。当领导者接受到一个写请求时，当它接受这个写操作时会计算系统的状态，把这个捕获到的新状态放到一个事务中。

## 用户

Zookeeper提供的编程接口非常的简单。你可以实现更高阶的操作，例如同步基元，组成员，所有权等等。

## 性能

Zookeeper设计的高性能实现。但是Zookeeper的开发团队在雅虎！在读比写多的情况下能提供特别好的性能。

<img src="image/zkperfRW-3.2.jpg" alt="zk_4" width="600" />

Zookeeper读写比例变化的生产力代表了Zookeeper 3.2版本运行在2Ghz Xeon和2个15K RPM的SATA硬盘上。一个是Zookeeper的专用日志设备。快照是被操作协同写入的。

## 可靠性

随着时间的推移故障被注入进去，我们运行了一个由7台机器组成的Zookeeper集群并展示出系统的行为图。我们在这之前运行同样饱和的基准测试，但是这次我们保持写操作占比30%，这是一个保守的负载预期。

<img src="image/zkperfreliability.jpg" alt="zk_5" width="600" />

从这个图中能看到一些有用的观点。首先，它紧接着失败后立马恢复，尽管有失败发生Zookeeper还是能支持比较高的吞吐量。但是更重要的是，领导者选举算法可以在大幅下降的情况下很快的恢复吞吐量。在我们看来，Zookeeper选举一个新的领导者不会超过200ms。第三，跟随者恢复，Zookeeper可以很快的恢复吞吐量。
