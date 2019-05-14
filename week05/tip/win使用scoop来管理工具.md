# win使用scoop来管理工具包

这两天路上听代码时间的广播，余凡推荐所有程序员不管是用什么平台都应该使用包管理工具来管理工具。之前在玩虚拟机时候Linux下的包管理工具比较方便，但是一直没使用过Windows下的包管理工具。下面介绍安装：

## 1. 打开PowerShell，输入下面脚本，允许执行本地脚本：

```bash
set-executionpolicy remotesigned -scope currentuser
```

## 2. 安装Scoop

```bash
iex (new-object net.webclient).downloadstring('https://get.scoop.sh')
```

## 3. 安装完成，查看帮助

```bash
scoop help
```

## 4. 尝试安装

```bash
scoop install python
```

安装目录默认安装到：C:\Users\milley\scoop\apps
