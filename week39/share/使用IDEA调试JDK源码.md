# 使用IDEA调试JDK源码

[学习自CodeSheep](https://www.bilibili.com/video/BV1V7411U78L)

学习完程序羊的教程后，这里自己做下记录。

## 1. 新建JDK工程导入src

使用Intellij IDEA新建一个Java项目，视频中的Mac下是将JDK安装目录下src.zip解压后复制到新建的java工程中。而我本机环境是Windows10，使用安装目录下Oracle的src.zip解压后会提示缺少sun包下的类，因此使用OpenJDK8的UNIXToolkit和FontConfigManager放到工程下对应的目录。示例中使用的JDK8，我们也使用同样版本来进行。导入代码后，首次编译会提示OOM，需要在设置中修改JVM的默认内存大小：

Settings->Build,Execution,Deloyment->Compiler->Build process heap size (Mbytes): 1700

## 2. 添加tools.jar

在Project Structure->libraries下添加jdk安装目录下lib/tools.jar

## 3. 修改SDKs下Sourcepath

要debug进入到java的类库中，需要将Sourcepath设置成本次导入进来的src：

Project Structure->Platform Settings->SDKs下选择1.8下的Sourcepath，删除已有的src.zip，添加本次导入的工程内目录。

## 4. 修改单步进入选项

在Settings->Build,Execution,Deloyment->Debugger->Stepping下去掉Do not step into the classes选项。

以上设置后，可以使用新建的测试代码，通过F7单步进入到jdk源码的世界了。
