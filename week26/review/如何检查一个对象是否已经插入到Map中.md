# 如何检查一个对象是否已经插入到Map中

[How to Check If an Inserted Object Was Already in a Map (with Expressive Code)](https://www.fluentcpp.com/2019/09/27/how-to-check-if-an-inserted-object-was-already-in-a-map-with-expressive-code/)

往STL set或者map插入一个条目，或者任何其他的multi-和unordered-类似的，我们使用insesrt方法：

```c++
std::map<int, std::string> myMap = // myMap is initialized with stuff...
myMap.insert({12, "twelve});
```

insert执行了将新的条目插入到容器中的动作，如果那个条目不存在。但是insert不仅仅执行那个动作：当插入完成后返回两条信息：

- 以iterator的形式表示新的元素当前的位置
- 以boolean的形式表示新的元素是否已插入（如果一个相同的值存在就不会再次插入）

返回那两个信息，STL相关容器的insert接口都以这种方式执行：它们返回一个std::pair<iterator, bool>。

这个接口使得插入操作变得扑朔迷离。让我们看看哪些是错的，如何提升它们。

## insert接口的问题

让我们聚焦在判断是否把元素插入的boolean值，因为它有和和iterator一样的所有问题，再增加一个。如果原来元素已经在map中比如我们要采取一定的行动。这里有几种方法来写这种代码。一个就像这样：

```c++
std::pair<std::map<int, std::string>::iterator, bool> insertionResult = myMap.insert({12, "twelve"});
if (!insertionResult.second)
{
    std::cout << "the element was already in the set.\n";
}
```

这个代码非常可怕有以下几个原因：

- std::pair<std::map<int, std::string>::iterator, bool>是一大块代码
- insertionResult不是你业务代码中期望的可读事情
- bool没有表明意义
- 即使你知道insert接口和bool值取决于是否元素已经存在，实际上是否插入成功或者已经存在也是扑朔迷离
- insertionResult.second是无意义的
- !insertionResult.second是无意义的并且非常复杂

你可以通过隐藏auto修饰返回值来缓解这些问题，通过明确的名字来修饰bool变量：

```c++
auto const insertionResult = mySet.insert(12);
auto const wasAlreadyInTheSet = !insertionResult.second;

if (wasAlreadyInTheSet)
{
    std::cout << "the element was already in the set.\";
}
```

如果你什么事情都不做，至少这样做，如果你需要检查元素是否在容器中。

我想那个代码是OK的，但是insert接口的技术方面仍在显示，尤其是.second。更进一步，我们可以将其封装到一个函数中。

## 一个小函数可以执行检查

从调用代码隐藏冒犯的pair有一个简单的途径，将.second包装到一个函数中，它的名字表名了意义：

```c++
template<typename Iterator>
bool wasAlreadyInTheMap(std::pair<Iterator, bool> const& insertionResult)
{
    return !insertionResult.second;
}
```

然后调用代码就像这样：

```c++
auto const insertionResult = myMap.insert({12, "twelve"});
if (wasAlreadyInTheMap)
{
    std::cout << "the element was already in the map.\n";
}
```

丑陋的.second就消失了。

## 其他类型的关联容器

注意这个函数不止是用在std::map。自从所有的STL关联容器有一个相似的insert接口，它也可以用在std::multimap，std::unordered_map，std::unordered_multimap，std::set，std::multiset，std::unordered_set和std::unordered_multiset。

所以wasAlreadyInTheMap名字比起能接受的函数不太通用。我们可以把名字改为wasAlreadyInAssociativeContainer。但是即使比wasAlreadyInTheMap更准确，后者在调用时看起来更好。

为所有STL关联容器设置一组重载是非常诱人的：

```c++
template<typename Key, typename Value>
bool wasAlreadyInTheMap<std::pair<typename std::map<Key, Value>::iterator, bool> const& insertionResult>
{
    return !resultionResult.second;
}

template<typename Key, typename Value>
bool wasAlreadyInTheMap<std::pair<typename std::multimap<Key, Value>::iterator, bool> const& insertionResult>
{
    return !resultionResult.second;
}

// ...
```

但是这个不能工作，因为这种类型的推导是不可能的。确实，嵌套类型迭代器不足以推断出容器类型。

如果我们需要两个不同的名字，我们可以实现两个仅仅名字不同的函数，但是它没有强制必须是std::map或者std::set。

```c++
template<typename Iterator>
bool wasAlreadyInTheMap<std::pair<Iterator, bool> const& insetionResult>
{
    return !insertionResult.second;
}

template<typename Iterator>
bool wasAlreadyInTheSet(std::pair<Iterator, bool> const& iterationResult)
{
    return !insertionResult.second;
}
```

我希望你在检查元素是否插入到STL关联容器中，这些建议能帮助你澄清你的代码。尽情的反馈吧！
