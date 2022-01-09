# C++11,C++14,C++17和C++20中lambda的演进

[The Evolutions of Lambdas in C++14, C++17 and C++20](https://www.fluentcpp.com/2021/12/13/the-evolutions-of-lambdas-in-c14-c17-and-c20/)

在现代C++中labmda是很重要的特性。自从C++11开始出现，现在它们变的无处不在。

但是自从C++11开始出现，他们一直在进化和获得更重要的特性。因为现在使用lambda很普遍，有一些特性可以帮助我们写出更有表现力的代码，所以花一些时间来学习它们也是非常值得的。

在这里我们的目的是为了覆盖主要的演进，可能会遗漏一些细节。lambda综合的覆盖范围更适用于一本书而不是一篇文章。如果你想了解更多，我建议你学习Bartek’的书[C++ Lambda Story](https://leanpub.com/cpplambda)，这会告诉你所有的点。

一般的lambda演进是给函数对象更多手动定义的能力。

这个文章假设你了解C++11中labmda基础。让我们从C++14开始。

## C++14中的lambda

在C++14中，labmda获得了4个主要的特性：

- 默认参数
- 模板参数
- 广义捕获
- 从函数中返回lambda

### 默认参数

在C++14中，lambda可以获取默认参数，例如函数：

```cpp
auto myLambda = [](int x, int y = 0){ std::cout << x << '-' << y << '\n'; };

myLambda(1, 2);
myLambda(1);
```

代码将会输出：

```bash
1-2
1-0
```

### 模板参数

C++11中我们可以定义一个lambda中参数的类型：

```cpp
auto myLambda = [](int x){ std::cout << x << '\n'; };
```

在C++14中我们可以让它接受任何类型：

```cpp
auto myLambda = [](auto&& x){ std::cout << x << '\n'; };
```

即使你不需要处理更多类型，这样也能让你避免重复代码并且让代码可读性更好。例如这种类型的lambda:

```cpp
auto myLambda = [](namespace1::namespace2::namespace3::ACertainTypeOfWidget const& widget) { std::cout << widget.value() << '\n'; };
```

可以这样替代:

```cpp
auto myLambda = [](auto&& widget) { std::cout << widget.value() << '\n'; };
```

### 广义捕获

在C++11中，labmda仅仅可以在其作用域中捕获已经存在的对象：

```cpp
int z = 42;
auto myLambda = [z](int x){ std::cout << x << '-' << z + 2 << '\n'; };
```

但是通过广义捕获，我们可以使用任意值初始化捕获对象。这是简单的例子：

```cpp
int z = 42;
auto myLambda = [y = z + 2](int x){ std::cout << x << '-' << y << '\n'; };

myLambda(1);
```

这个会输出:

```bash
100-44
```

### 从函数返回labmda

C++14中labmda的特性：从函数中返回auto并且不指定返回类型。当labmda的类型被编译器生成，在C++11无法从函数返回一个lambda：

```cpp
/* what type should we write here ?? */ f()
{
    return [](int x){ return x * 2; };
}
```

在C++14中返回一个使用auto的lambda作为返回类型。这个当lambda作为一个大的函数中间时非常有用。

```cpp
void f()
{
    // ...
    int z = 42;
    auto myLambda = [z](int x)
                    {
                        // ...
                        // ...
                        // ...
                    };
    // ...
}
```

我们也可以将lambda包装到另外一个函数中，介绍抽象的层级：

```cpp
auto getMyLambda(int z)
{
    return [z](int x)
           {
               // ...
               // ...
               // ...
           };
}

void f()
{
    // ...
    int z = 42;
    auto myLambda = getMyLambda(z);
    // ...
}
```

## C++17中的labmda

C++17中引入了一个重要的特性：可以将labmda定义为constexpr:

```cpp
constexpr auto times2 = [] (int n) { return n * 2; };
```

可以在编译时使用上下文断言：

```cpp
static_assert(times2(3) == 6);
```

这个在模板编程非常有用。

请注意constexpr labmda在C++20变的更加重要。的确，仅仅在C++20中std::vector和大多数STL算法也变成了constexpr，在编译时可以使用constexpr lambda来创建操纵的集合。

虽然有一个容易例外：std::array。非变异访问操作的std::array在C++14变为了constexpr，然后变异访问操作的在C++17变为了constexpr。

### 捕获*this的拷贝

另一个在C++17中增加的特性是通过简单的语法捕获*this的拷贝。下面的代码可以阐述：

```cpp
struct MyType{
    int m_value;
    auto getLambda()
    {
        return [this](){ return m_value; };
    }
};
```

这个lambda捕获了*this的拷贝。如果labmda比对象寿命长，这可能会导致内存错误。就像下面这个代码：

```cpp
auto lambda = MyType{42}.getLambda();
lambda();
```

当MyType在第一行末尾析构后，调用labmda来访问m_value，但是this已经指向了释放的对象。这将会是未定义的行为，有可能使得应用程序崩溃。

解决这个情况可以使用在lambda内部捕获整个对象的拷贝。C++17提供了下面的语法：

```cpp
struct MyType
{
    int m_value;
    auto getLambda()
    {
        return [*this](){ return m_value; };
    }
};
```

注意在C++14中使用广义捕获已经提供了相同的结果：

```cpp
struct MyType
{
    int m_value;
    auto getLambda()
    {
        return [self = *this](){ return self.m_value; };
    }
};
```

C++17的语法更加好。

## C++20中的lambda

C++20中labmda中的特性比起C++14和C++17少了很多。

C++20中lambda的一项增强，使得更加接近手动定义函数对象，就是定义模板的经典语法：

```cpp
auto myLambda = []<typename T>(T&& value){ std::cout << value << '\n'; };
```

这个比起C++14中使用auto&&表达式，更加容易能获取模板参数的类型。

另一个提升是捕获可变参数：

```cpp
template<typename... Ts>
void f(Ts&&... args)
{
    auto myLambda = [...args = std::forward<Ts>(args)](){};
}
```

## 深入了解lambda

我们主要了解了从C++14到C++20主要的lambda特性。这些主要的特性伴随着labmda可以用容易的编写小的东西。

深入了解lambda是更好的了解C++的好机会，我认为也是值得投入时间。如果要走的更远，我了解的最好的资源是[C++ Lambda Story](https://leanpub.com/cpplambda)，强烈推荐。
