# 使用C++创建一个网站

[Building a Website with C++](https://link.medium.com/Ur4S7CkA7V)

## 这个可能吗？

是的。

我知道那听起来有点奇怪，有可能看起来是不寻常的徒劳，但其实它不是。

在这片文章中，我将给你演示怎么样使用C++去开发一个网站和为什么要继续这么做的原因。

这个非常有趣，并且这个也是完全可实践。

## 环境变量

你可能认为这么有意思的配置可能需要专用的环境，其实不是这样的。大部分的网站需要专用或者虚拟服务器，在大多数情况下，使用C++可以用共享主机。

所有的支持CGI的web服务器都可以用C++来搭建网站。对于不同的主机环境，I可能需要也可能不需要在本地编译。如果要准备在web服务器上编译需要依赖SSH环境。

## 一个简单例子

在下面的例子，我打算借助于一个共享的嵌入式虚拟主机账户。它始终是容易得到，便宜的。你可以很方便的稍微做一点修改，就可以将其使用Apache配置部署到虚拟主机或者专用服务器或者Amazon EC2上。

cPanel(一个商业的虚拟主机)给我们提供了cgi-bin目录，但是我没必要使用它。在大多数状况下，如果有权限(通常是0755)的扩展名为.cgi的文件都可以自动执行。下面试基本的文件(确保Makefile中用的是TAB)。

Makefile:

```makefile
all:
    g++ -O3 -s hello.cpp -o hello.cgi
clean:
    rm -f hello.cgi
```

hello.cpp:

```c++
#include <iostream>
#include <string>
#include <stdlib.h>

using namespace std;

void set_content_type(string content_type) {
    cout << "Content-type: " << content_type << "\r\n\r\n";
}

void set_page_title(string title) {
    cout << "<title>" << title << "</title>\n";
}

void h1_text(string text) {
    cout << text << "\n";
}

int main() {
    set_content_type("text/html");
    // Output HTML boilerplate
    cout << "<!doctype html>\n";
    cout << "<html lang=\"en\">\n";
    cout << "<head>\n";
    set_page_title("Hello, World!");
    cout << "</head>\n";
    cout << "<body>\n";
    h1_text("Hello, World!");
    cout << "</body>\n";
    cout << "</html>";

    return 0;
}
```

如果你开启你的账户(你需要询问你的web主机客服)编译器，SSH到你的账户上，把这些文件放到公共的html文件夹中，然后运行：

```makefile
make
```

hello.cgi文件就会被编译出来。如果你在浏览器中打开这个文件，像下面这样：

```bash
http://youer-test-site.com/hello.cgi
```

替换your-test-site成你的主机URL。你将会看到"Hello World"在你的屏幕上。

在钻研这段代码前，我们先看下web服务器是怎么工作的。当Apache接受到一个请求，首先会查找一个内部的句柄或者重写规则，然后检查硬盘上的文件有没有和请求配的。在我们的按理中，找到了hello.cgi然后执行。我们的程序没有输入，简单输出了一句hello world的消息。Apache将这个上下文返回给客户端。

在这段代码中，更简单的描述是很有必要的。我不用包含这些分割函数例如set_content_type，set_pate_title，h1_text。这样将会让main函数看起来更简洁。这样修改程序也可以跟之前一样正常运行。

但是我希望你能看出来定义这些函数的高级特性。如果你创建了一个公共的HTML元素方法，你可以很简洁的用它来实现更简洁的代码。C++程序示例如下：

```c++
void p(string text) {
    cout << "<p>" << text << "</p>\n"
}
```

然后你可以这样使用：

```c++
p("This would be paragraph text.");
```

输出一段。

你可以使用同样的方法来包装其他函数，例如p，h1_text，等等。使用cout返回文本代替标准输出。这样，你可以使用模板系统或者响应式变成来构建更复杂的页面。

这个例子就是个开门见山的例子。你有完全的权限对每一个请求头，在请求交互中有控制权。

## 考虑下所有的输入

我们的例子中没有带任何有意义的入参。就简单返回一个"Hello, World"。但是每个请求，Apache都会通过环境变量带很多的信息。在C程序中可以使用getenv()函数获取那些值(不要忘记在程序前面添加"#include <stdlib.h>")。

如果你希望知道所有的请求URI，你可以使用：

```c++
string request_uri = getenv("REQUEST_URI");
```

其他的一些有用的变量如下：

- REMOTE_ADDR--获取访问者的IP
- REQUEST_METHOD--返回方法名(GET,POST等)
- DOCUMENT_ROOT--网站的文根(共享主机常常为~/public_html，虚拟/专用主机常常为/var/www/html)
- QUERY_STRING--GET请求的查询串。可以用来恢复GET变量

## 一个健壮的例子

手动解析GET变量是可行的，从检查标准输入可以处理POST变量。通过修改请求和返回头信息你也可以获取或者设置cookies。但是这些方法是冗长的。

你可以编写属于自己的包装器，或者使用GNU cgicc库。它包含了各种函数可以修改HTML和处理表单。对于一个大项目来说，使用这样的库可以节省不少时间。

在Debian和Ubuntu，你可以安装头文件和库：

```bash
apt install libcgicc5 libcgicc5-dev
```

但是在CentOS/RHEL，没有官方的包。可以这样安装：

```bash
cd /usr/local/src
wget ftp://ftp.gnu.org/gnu/cgicc/cgicc-3.2.19.tar.gz
tar xfz cgicc*.tar.gz
cd cgicc*
./configure --prefix=/usr
make
make install
```

注意：3.2.19是最近的版本，但是你应该使用最新版，从[cgicc](ftp://ftp.gnu.org/gnu/cgicc/)来获取。我经常使用/usr安装目录来避免库连接错误。你可以根据你的习惯考虑要不要更改。

现在已经安装好了，你可以使用编译器来构建它。尝试下面的例子，从表单输入然后显示到浏览器中。

Makefile:

```makefile
all:
    g++ -O3 -s hello.cpp -o hello.cgi
    g++ -O3 -s cgicc.cpp -o cgicc.cgi /usr/lib/libcgicc.a
clean:
    rm -f hello.cgi cgicc.cgi
```

cgicc.html:

```html
<!doctype html>
<html lang="en">
<head>
    <title>cgicc Test</title>
</head>
<body>
    <form method="POST" action="cgicc.cgi">
        <label for="name">Name</label>
        <input name="name" type="text" value="">
        <input name="submit" type="submit" value="Submit">
    </form>
</body>
</html>
```

cgicc.cpp:

```c++
#include <iostream>
#include <string>
#include <stdio.h>
#include <stdlib.h>
#include <cgicc/CgiDefs.h>
#include <cgicc/Cgicc.h>
#include <cgicc/HTTPHTMLHeader.h>
#include <cgicc/HTMLClasses.h>

using namespace std;
using namespace cigcc;

void set_content_type(string content_type) {
    cout << "Content-type: " << content_type << "\r\n\r\n";
}

void set_page_title(string title) {
    cout << "<title>" << title << "</title>\n";
}

void h1_text(string text) {
    cout << text << "\n";
}

int main() {
    Cgicc cgi;
    string name;

    set_content_type("text/html");

    cout << "<!doctype html>\n";
    cout << "<html lang=\"en\">\n";
    cout << "<head>\n";
    set_page_title("cgicc Test");
    cout << "</head>\n";
    cout << "<body>\n";
    cout << "<p>";

    // Grab the "name" variable from the form
    name = cgi("name);
    // Check to make sure it isn't empty
    if (!name.empty()) {
        cout << "Name is " << name << "\n";
    } else {
        cout << "Name was not provided.";
    }

    cout << "</p>\n";
    cout << "</body>\n";
    cout << "</html>";

    return 0;
}
```

你可能注意到了我将cgicc静态连接在makefile中。当不必要时(也可以使用-lcgicc)，我更倾向于使用静态连接二进制文件并绑定程序发送到web服务器。

在这个例子中，cgicc库有效提升解析POST请求返回name字段。

在这个例子中我没有避开POST变量。在生产系统中，你可能要使用数据库。

## 性能

编写好的C++程序可以得到出乎意料的性能。CGI接口稍微差一点，但是这也比其他解释性语言例如PHP好太多了。

这也就是说，总有提升的空间。在专用或虚拟环境中，当程序加载的时候你可以使用Apache或Nginx的FastCGI来减少延迟(当服务负载时候是显而易见的)。在我测试中，从来没有降低速度，但是一个繁忙的网站将会检验那些提高性能的解决方案。

## 额外的好处

使用C++程序可以很方便的部署到Docker中。这样可以使部署变得更加灵活。

你可能在你的C/C++程序中也包含了MySQL数据库的头文件。如果你对使用PHP开发MySQL的话，你将会找到相类似的函数。

比起使用额外的命令行处理图片，你可以用ImageMagick的C++库直接操作图片。

## 这将是一个长久的，奇怪的点

用C++构建一个实际的网站，特别注重它的性能。我不建议使用它来构建博客或个人网站--这些可以用WordPress很容易的完成。如果你需要一个对速度要求极高的网站，使用C++将是合适的选择。
