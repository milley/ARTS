# visual studio debug错行

最近在用VS2010调试代码，发现单步debug会错行进入没有调用的函数内。在折腾了一个下午，网上搜了下发现之前也有人遇到过。总结一下就是代码是在Release模式下编译的，然后又debug调试导致代码错行。后来使用Debug模式重新编译后单步debug就正常了。[参考链接](https://stackoverflow.com/questions/4048505/debugger-on-wrong-line-when-debugging-classic-asp)。
