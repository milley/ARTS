# ZooKeeper Getting Started Guide

[Getting Started Guide](http://zookeeper.apache.org/doc/r3.4.14/zookeeperStarted.html)

## 入门指南：使用Zookeeper协调分布式应用

这篇文档包含了快速入门Zookeeper的信息。主要的目标是希望开发者试用它，包含了一个Zookeeper单节点的简单安装教程，一些命令校验运行状态，还有一个简单的程序示例。最后，为了方便起见，有一些章节就复杂的安装来说，比如复杂的部署环境，优化事务日志。但是对于完整性的商业部署说明，请参考[ZooKeeper Administrator's Guide](http://zookeeper.apache.org/doc/r3.4.14/zookeeperAdmin.html)。

## 先决条件

在管理员指南中查看[系统要求](http://zookeeper.apache.org/doc/r3.4.14/zookeeperAdmin.html#sc_systemReq)

## 下载

获取Zookeeper，从Apache下载镜像中选择一个近期的[稳定](http://zookeeper.apache.org/releases.html)版本。

## 单机操作

配置一个单机模式的Zookeeper服务是直截了当的。服务包含了一个单独的Jar文件，因此安装完成后就创建了配置。一旦你下载了一个稳定的Zookeeper Release版本解压后cd到root目录下，启动Zookeeper需要一个配置文件。这里是一个例子，创建到conf/zoo.cfg下：

```bash
tickTime=2000
dataDir=/var/lib/zookeeper
clientPort=2181
```

这个文件可以被命名为任意名称，但是在这个讨论中命名为conf/zoo.cfg。改变dataDir指定一个存在的目录。下面是这些配置项的解释：

- tickTime: Zookeeper使用的基本时间单元，用毫秒表示。它用作心跳包和最小的会话超时时间是tickTime的两倍
- dataDir: 存储快照的内存库位置，除非有其他规定，事务日志更新到数据库
- clientPort: 监听连接的端口

现在你可以创建配置文件并启动Zookeeper:

```bash
bin/zkServer.sh start
```

Zookeeper使用log4j记录日志消息--更多关于变成指南的[日志细节](http://zookeeper.apache.org/doc/r3.4.14/zookeeperProgrammers.html#Logging)。你将会看到日志消息打印到控制台或者日志文件，依赖于log4j的配置文件。这里介绍Zookeeper运行在单机模式下。这里没有从节点，所以当Zookeeper服务宕机，服务就真宕掉了。这适用于大多数开发的情况，但是要使用集群模式，参考[Zookeeper运行集群](http://zookeeper.apache.org/doc/r3.4.14/zookeeperStarted.html#sc_RunningReplicatedZooKeeper)。

## 管理Zookeeper存储

如果长时间在生产环境运行Zookeeper应该使用外部的存储。查看[维护](http://zookeeper.apache.org/doc/r3.4.14/zookeeperAdmin.html#sc_maintenance)章节。

## 连接到Zookeeper

```bash
bin/zkCli.sh -server 127.0.0.1:2181
```

这样可以使你操作一个简单的文件系统。当你连接成功，将会看到如下：

```bash
Connecting to localhost:2181
log4j:WARN No appenders could be found for logger (org.apache.zookeeper.ZooKeeper).
log4j:WARN Please initialize the log4j system properly.
Welcome to ZooKeeper!
JLine support is enabled
[zkshell: 0]
```

通过Shell，输入help获取命令列表的帮助文档：

```bash
[zkshell: 0] help
ZooKeeper host:port cmd args
    get path [watch]
    ls path [watch]
    set path data [version]
    delquota [-n|-b] path
    quit
    printwatches on|off
    create path data acl
    stat path [watch]
    listquota path
    history
    setAcl path acl
    getAcl path
    sync path
    redo cmdno
    addauth scheme auth
    delete path [version]
    deleteall path
    setquota -n|-b val path
```

从这里，你可以试一个简单的命令来点感觉。首先，通过ls命令看效果：

```bash
[zkshell: 8] ls /
[zookeeper]
```

下一步，通过运行create /zk_test my_data创建一个znode。这将创建一个znode并且连接到字符串my_data上，你将会看到：

```bash
[zkshell: 9] create /zk_test my_data
Created /zk_test
```

再次发布ls / 命令，看到如下目录：

```bash
[zkshell: 11] ls /
[zookeeper, zk_test]
```

注意zk_test目录已经被创建了。下一步，使用get命令校验数据和znode的连通性：

```bash
[zkshell: 12] get /zk_test
my_data
cZxid = 5
ctime = Fri Jun 05 13:57:06 PDT 2009
mZxid = 5
mtime = Fri Jun 05 13:57:06 PDT 2009
pZxid = 5
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0
dataLength = 7
numChildren = 0
```

我们可以通过set命令来修改数据连接关系：

```bash
[zkshell: 14] set /zk_test junk
cZxid = 5
ctime = Fri Jun 05 13:57:06 PDT 2009
mZxid = 6
mtime = Fri Jun 05 14:01:52 PDT 2009
pZxid = 5
cversion = 0
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0
dataLength = 4
numChildren = 0
[zkshell: 15] get /zk_test
junk
cZxid = 5
ctime = Fri Jun 05 13:57:06 PDT 2009
mZxid = 6
mtime = Fri Jun 05 14:01:52 PDT 2009
pZxid = 5
cversion = 0
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0
dataLength = 4
numChildren = 0
```

最后，通过delete删除znode:

```bash
[zkshell: 16] delete /zk_test
[zkshell: 17] ls /
[zookeeper]
[zkshell: 18]
```

## Zookeeper编程

Zookeeper有Java绑定和C绑定。它们在功能上是相同的。C绑定有2种形式：单线程和多线程。它们的差异仅在如何让消息循环处理。[这里](http://zookeeper.apache.org/doc/r3.4.14/zookeeperProgrammers.html#ch_programStructureWithExample)查看更多示例。

## 运行Zookeeper集群

运行Zookeeper单机模式在评测、开发、测试环境非常方便。但是对于生产环境来说，你应该运行集群模式。同一个应用的一组服务叫做quorum，在集群模式中，quorum下的所有服务都共享一套配置。

注意： 在集群模式下，最小的必须服务是3台，强烈建议设置为奇数。如果你只有2台服务，如果它们都挂了没有额外的机器来决定多数的quorum。两台服务本质上是比单机稍微稳定一点，因为它们是2个单独的点。

conf/zoo.cfg对于集群和单机一样都是必须的文件，稍微有点差别。这里是一个示例：

```bash
tickTime=2000
dataDir=/var/lib/zookeeper
clientPort=2181
initLimit=5
syncLimit=2
server.1=zoo1:2888:3888
server.2=zoo2:2888:3888
server.3=zoo3:2888:3888
```

新的条目，initLimit是Zookeeper服务限制连接到领导者的时长。条目syncLimit限制成为领导者的过期时长。除了这2个超时时长，你可以使用tickTime指定时间单元。在这个例子中，initLimit设定为5个2000毫秒，也就是10秒。配置项server.X列出了组成Zookeeper服务的所有服务。当服务启动的时候，它知道在数据目录中哪个服务是在哪个myid文件中。那些文件包含了ASCII的服务编号。

最后，注意那2个端口2888和3888，同伴使用端口连接到其他同伴。这样的连接是必须的因此同伴间可以通信，例如可以同意更新订单。进一步来说，Zookeeper服务使用这个去连接到领导者。当一个新的领导者选举后，跟随者使用这个端口建立TCP连接来和领导者连接。因为默认的领导者选举使用TCP，我们需要用到另外的端口来选举。这就是服务条目的第二个端口。

