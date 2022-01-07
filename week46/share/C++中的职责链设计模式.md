# C++中的职责链设计模式

职责链模式在很多开源项目中使用很广泛，近期工作中遇到的一个比较大的项目中就有很多地方使用了职责链的设计模式。本文打算用一个简单易用的例子来表示下职责链设计模式的思想。

## 责任

- 主要是为了避免请求发送方多于一个请求一种处理。将接收对象链接起来直到一个对象过来可以处理它
- 使用包含多个处理方式的单个处理流水线来发送和接收请求
- 含有递归遍历的面向对象链表

## 问题

有一个潜在的请求变量和一系列请求必须被处理。需要高效的不用硬编码方式或者映射请求方式来处理。

例如： Client --> Processing element --> Processing element --> Processing element --> Processing element

## 数据结构

派生类知道如何满足于客户端的请求。如果当前对象不能处理或者不是可用的，那么它代表基类，传递给下一个对象，循环传递下去。

## 示例

基类有一个指向自身的指针，然后提供了setNext和add方法以便串联其他子类的对象。调用链最终调用handle方法来依次处理请求。

```cpp
#include <ctime>
#include <iostream>
#include <vector>

using namespace std;

class Base {
  Base* next;

 public:
  Base() { next = 0; }

  void setNext(Base* n) { next = n; }

  void add(Base* n) {
    if (next) {
      next->add(n);
    } else {
      next = n;
    }
  }

  virtual void handle(int i) { next->handle(i); }
};

class Handle1 : public Base {
 public:
  void handle(int i) {
    if (rand() % 3) {
      // Don't handle request 3 times out of 4
      cout << "H1 passed " << i << " ";
      Base::handle(i);
    } else {
      cout << "H1 handled " << i << " ";
    }
  }
};

class Handle2 : public Base {
 public:
  void handle(int i) {
    if (rand() % 3) {
      cout << "H2 passed " << i << " ";
      Base::handle(i);
    } else {
      cout << "H2 handled " << i << " ";
    }
  }
};

class Handle3 : public Base {
 public:
  void handle(int i) {
    if (rand() % 3) {
      cout << "H3 passed " << i << " ";
      Base::handle(i);
    } else {
      cout << "H3 handled " << i << " ";
    }
  }
};


int main() {
  srand(time(0));
  Handle1 root;
  Handle2 two;
  Handle3 thr;
  root.add(&two);
  root.add(&thr);
  thr.setNext(&root);
  for (int i = 1; i < 10; i++) {
    root.handle(i);
    cout << "\n";
  }
}
```

如例子所示，只有当前对象的随机值能被3整除，就会继续传递下一个对象继续执行，知道当前对象随机值不能被3整除，就会接着下一次循环。
