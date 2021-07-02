# PostgreSQL

[PostgreSQL](https://help.ubuntu.com/community/PostgreSQL)

## 介绍

PostgreSQL是一个强大的关系型数据库管理系统，提供了灵活的BSD类型许可。PostgreSQL包含了很多高级特性，非常快并且符合标准。

PostgreSQL绑定了很多的编程语言，比如C,C++,Python,Java,PHP,Ruby等等。它可以从简单的web应用到数以万计的大规模数据库提供任何东西。

## 客户端安装

如果你仅仅想连接已有的PostgreSQL服务端，不用安装main PostgreSQL包，使用PostgreSQL client包来代替。为此，使用如下命令：

```cmd
sudo apt-get install postgresql-client
```

然后使用下面命令连接：

```cmd
psql -h server.domain.org database user
```

在你插入密码行之后你就可以使用命令行了。你应该插入如下来操作实例：

```sql
SELECT * FROM table WHERE 1;
```

退出连接使用：

```cmd
\q
```

## 安装

使用如下命令安装服务端：

```cmd
sudo apt-get install postgresql postgresql-contrib
```

这将使用Ubuntu release中最新的版本来进行安装，同时也会安装常用的插件。下面的"External Links"命令选项列出最新的版本。

安装PostGIS,procedural languages,client interface等等。

额外的包包含了程序运行时，例如PostGIS插件，语言客户端接口例如Python的psycopg2等等。你可以用下面获取：

```cmd
apt-cache search postgres
```

管理员

pgAdmin III是一个方便的PostgreSQL GUI程序，对初学者非常有用。使用下面命令安装：

```cmd
sudo apt-get install pgadmin3
```

## 基本服务设置

开始之前，我们需要给PostgreSQL用户（称作postgres）设置密码。我们将无法访问其他外部服务器。作为本地"postgres"linux用户，我们允许使用psql命令连接和操作服务。

在命令行中输入：

```cmd
sudo -u postgres psql postgres
```

这作为与本地用户同名的连接角色，例如"postgres"，在数据库中也叫"postgres"。给"postgres"设置密码使用如下命令：

```cmd
\password postgres
```

然后根据提示输入密码。为了安全的目的密码文本会被隐藏。使用Ctrl+D或者\q来退出posgreSQL命令行。

## 创建数据库

创建第一个数据库，我们称为"mydb"，只需输入：

```cmd
sudo -u postgres createdb mydb
```

为Postgresql 8.4或者9.3安装服务看板

PgAdmin需要安装全功能的插件。插件"adminpack"，被称作服务看板，是postgresql-contrib的一部分，所以必须安装那个插件：

```cmd
sudo apt-get install postgresql-contrib
```

然后激活扩展，"Postgresql 8.4"运行adminpack.sql脚本，只需输入：

```cmd
sudo -u postgres psql < /usr/share/postgresql/8.4/contrib/adminpack.sql
```

"Postgresql 9.3"从"postgres"数据库安装adminpack扩展：

```cmd
sudo -u postgres psql
CREATE EXTENSION adminpack;
```

## 替代服务器设置

如果你不打算连接其他机器上的数据库，这种替代方式更简单。

默认情况下在ubuntu，PostgreSQL配置为对来自同一机器的任何连接使用"ident sameuser"身份认证。查看优秀的PostgreSQL文档以获得更多帮助，但本质上意味着如果你的ubuntu用户名是'foo'并且你将'foo'添加为PostgreSQL用户，那么你可以连接到数据库而不需要密码。

由于唯一可以连接到全新安装的用户是postgres用户，以下是如何为自己创建一个与登录用户名同名的数据库账号（在这种情况下也是超级用户）然后为用户创建密码：

```cmd
 sudo -u postgres createuser --superuser $USER
 sudo -u postgres psql
```

客户端程序，默认情况下，使用你的ubuntu登录名连接到本地主机，并期望找到具有该名称的连接。所以为了让事情变得简单，使用上面授权的新超级用户权限创建与登录名同名的数据库：

```cmd
sudo -u postgres createdb $USER
```

连接到你自己的数据库以尝试一些SQL：

```cmd
psql
```

创建新的数据库也非常容易，例如这样输入：

```cmd
create database amarokdb;
```

你可以直接告诉Amarok使用postgresql来存储它的音乐目录。数据库名称是amarokdb，用户名是你的登陆名，由于"ident sameuser"甚至不需要输入密码，因此可以留空。

## 使用pgAdmin III GUI

要了解PostgreSQL可以做什么，你可从启动图形界面客户端开始，在终端中输入：

```cmd
pgadmin3
```

你将使用pgAdmin III界面。点击"增加一个连接到服务"按钮，在新的对话框，输入地址127.0.0.1，一个服务描述，和默认的数据库（上面例子中的"mydb"），你的用户名和密码。还需要一个步骤才能允许pgAdmin III连接到服务器，那就是编辑pg_hba.conf文件并将身份验证方法从对等改为md5.

```cmd
sudo nano /etc/postgresql/9.3/main/pg_hba.conf
```

改变一行：

```cmd
# Database administrative login by Unix domain socket
local   all             postgres                                peer
```

至：

```cmd
# Database administrative login by Unix domain socket
local   all             postgres                                md5
```

这样加载服务配置然后连接到PostgreSQL数据库服务：

```cmd
sudo /etc/init.d/postgresql reload
```

使用GUI你可以新建或者操作数据库，查询数据库，执行SQL等等。

## 管理服务

管理用户和权限

管理用户，你需要先编辑/etc/postgresql/current/main/pg_hba.conf然后修改默认配置，这是非常必要和安全的。例如，如果你用postgres管理用户，你可以添加如下命令：

```cmd
8<-------------------------------------------
# TYPE  DATABASE    USER        IP-ADDRESS        IP-MASK           METHOD
host    all         all         10.0.0.0       255.255.255.0    md5
8<-------------------------------------------
```

意味着在你的本地网络上，postgresql可以通过网络连接到数据库，提供典型的一堆用户名/密码。

除了允许用户通过网络连接到服务器的数据库上之外，你必须启用PostgreSQL以跨不通网络进行监听。要做这一点，打开/etc/postgresql/current/main/postgresql.conf然后修改listen_addresses：

```cmd
listen_addresses = '*'
```

监听所有的网络接口。有关其他选项，参阅listen_addresses文档。

要使用对数据库具有完全权限的用户创建数据库，请使用如下命令：

```cmd
sudo -u postgres createuser -D -A -P myuser
sudo -u postgres createdb -O myuser mydb
```

第一个命令行创建没有数据库创建权限的用户（-D）没有添加用户权限（-A）并会提示你输入密码（-P）。第二个命令行以"myuser"为所有者创建数据库"mydb"。

这个小例子可能会满足你大部分需求。更多细节，请参考响应的手册或在线文档。

重启服务

配置网络/用户后，你可能只需要重新加载服务，建议用如下命令：

```cmd
sudo /etc/init.d/postgresql reload
```

postgresql.conf中某些配置更改需要重启服务，这将终止活动连接并终止未提交的事务。

```cmd
sudo /etc/init.d/postgresql restart
```

## 进一步阅读

如果你不熟悉SQL，你可能想研究一下这种强大的语言，虽然一些简单的PostgreSQL使用可能不需要这些知识。

PostgreSQL 网站包含有关使用此数据库的大量信息。特别是，本教程是一个有用的起点，但您可以跳过安装步骤，因为您已经使用 Ubuntu 软件包安装了它。

## 故障排除

fe_sendauth: no password supplied

你的pg_hba.conf指定md5身份验证用于基于原始主机的此连接，连接方法和请求的用户名/数据库，但你的应用程序没有提供密码。

更改身份认证模式或为你要设置的连接设置密码，然后在应用程序的连接设置中指定该密码。

FATAL: role "myusername" does not exist

默认情况下，PostgreSQL连接到与当前unix用户同名的PostgreSQL用户。你尚未在数据库中使用该名称创建PostgreSQL用户。

创建一个合适的用户，或指定一个不通的用户名来连接。在命令行工具中-U标志执行此操作。

FATAL: database "myusername" does not exist

存在名为"myusername"的用户，但没有同名的数据库。默认情况下，PostgreSQL 连接到与您连接的用户同名的数据库，但如果数据库不存在，它不会自动创建数据库。

创建数据库，或指定要连接的不同数据库。

FATAL: Peer authentication failed for user "myusername"

你正在通过unix套接字连接到本地主机。存在名为"myusername"的用户，但当前的unix用户与该用户不同。PostgreSQL设置为在unix套接字上为此用户/数据库组合使用“peer”身份验证，因此它需要您的unix和postgresql用户名匹配。

从与所需 PostgreSQL 用户匹配的 unix 用户进行连接 - 可能使用 sudo -u theusername psql - 或更改 pg_hba.conf 以为此用户名使用不同的身份验证模式，例如“md5”。

could not connect to server: No such file or directory

一个类似这样的错误：

```cmd
 psql: could not connect to server: No such file or directory
 Is the server running locally and accepting
 connections on Unix domain socket "/tmp/.s.PGSQL.5432"?
```

可能以为如下问题：

- 服务没有运行
- 由于不通的编译默认值或不匹配的设置，服务器具有与客户端libpq中的默认值不同的unix_socket_directories。
- 这个服务监听在不同的端口上。PostgreSQL 通过使用端口号作为套接字文件的后缀来模拟 unix 套接字上的 TCP/IP 端口，例如5432。

依次取消这些。

首先确保服务正在运行。在ubuntu，ps -u postgres -f将显示以用户postgres身份运行的任何进程，你希望看到多个postgres进程。现在确保服务器在监听你客户端认为的端口。找出你的 PostgreSQL 服务器的套接字目录：

```cmd
sudo -u postgres psql -c "SHOW unix_socket_directories;"
```

或在较旧的 PostgreSQL 版本上， unix_socket_directory 作为参数更改名称。显示服务器的端口（适用于 TCP/IP 和 unix 套接字）。

```cmd
sudo -u postgres psql -c "SHOW port;"
```

如果您甚至无法在 unix 用户 postgres 下与 psql 连接，您可以使用 lsof 检查套接字目录:

```cmd
 $ sudo lsof -U -a -c postgres
 COMMAND   PID     USER   FD   TYPE             DEVICE SIZE/OFF   NODE NAME
 postgres 6565 postgres    5u  unix 0xffff88005a049f80      0t0 183009 /tmp/.s.PGSQL.5432
 postgres 6566 postgres    1u  unix 0xffff88013bc22d80      0t0 183695 socket
 postgres 6566 postgres    2u  unix 0xffff88013bc22d80      0t0 183695 socket
```

在这种情况下，第一行是套接字位置。此服务器具有套接字目录 /tmp，端口为 5432。

如果你的客户端在不同的套接字目录中查找，你可能正在尝试通过unix套接字连接到默认套接字路径和目录，并且您的客户端应用程序链接到的 libpq 与您正在运行的 PostgreSQL 具有不同的编译进 Unix 套接字路径和/或端口。很可能你的LD_LIBRARY_PATH或者/etc/ld.so.conf在你的PostgreSQL版本附带的libpq之前具有不同的libpq。这通常无关紧要，您可以覆盖套接字目录。

要指定要连接的备用套接字目录和/端口端口，请在连接选项中将套接字目录指定为主机参数，例如以用户 bob 的身份连接到在端口 5433 上侦听 /tmp 的服务器：

```cmd
psql -h /tmp -p 5433 -U bob ...
```

或以连接字符串形式:

```cmd
psql "host=/tmp port=5433 user=bob ..."
```

同样适用于任何使用libpq的客户端.它不适用于非libpq客户端，如PgJDBC、py-postgresql等，但其中大多数根本不支持unix套接字。请参阅非基于libpq的客户端的客户端文档。
