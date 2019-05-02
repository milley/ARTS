# 在谷歌，Java开发者都使用Guava

[Java Developers, have a Guava on Google](https://medium.com/@ali.muzaffar/java-developers-have-a-guava-on-google-a98c6d79f04e)

所有使用Java开发谷歌产品的开发者都知道，谷歌捆绑了一部分api和库来解决公共的问题或者让编码更容易。同样的，你可以将谷歌的Guava库引入到你的非Google项目中来。

如果你给Google平台开发，这并不意味着你必须要使用Guava，谷歌倾向于很多库但是更多还是用Guava。事实上，注意到Android Open Source Project下所有Andriod APP的安装文件，你大概都能看到谷歌包含了Guava库。

因此，你可以使用私有的谷歌开发者指定的代码，你可以使用它来做什么？

## Guava的顶级特性

### StringJoiner

让我们从最简单的例子开始演示如何减少代码量。你经常会做的一个简单事情，就像连接first name和last name来创建全名？或者连接城市、州、邮编组成一个地址。让我们看下例子，代码大概是这个样子：

```java
public String getFullName(UserObject userObject) {
    String firstName = userObject.getFirstName();
    String lastName  = userObject.getLastName();
    String fullName = "";
    if (firstName != null && firstName.length() > 0) {
        fullName = firstName;
    }

    if (lastName != null && lastName.length() > 0) {
        if (fullName.length() > 0) {
            fullName += " " + lastName;
        } else {
            fullName = lastName;
        }
    }

    return fullName;
}
```

我们可以通过在usesrObject内部方法检查是否为空来清理一点这段代码，如果字符串是null则返回空字符串，但是我们依旧需要检查长度。Guava的Joiner API可以这样使用：

```java
Joiner joiner = Joiner.on(" ").skipNulls();
return joiner.join(userObject.getFirstName(), userObject.getLastName());
```

同样的，对于地址你可以这样使用：

```java
Joiner joiner = Joiner.on(" ").skipNulls();
return joiner.join(userObject.getCity(), userObject.getState(), userObject.getPostCode());
```

nulls也可以这样展示。我知道你可能想让空字符串也要展示，这样谷歌开发者推荐使用Strings.emptyToNull("")所以你的代码将变为：

```java
Joiner joiner = Joiner.on(", ").skipNulls();
return joiner.join(Strings.emptyToNull(userObject.getCity()),
                   Strings.emptyToNull(userObject.getState()),
                   Strings.emptyToNull(userObject.getPostCode()));
```

这样比原版代码简洁了很多。

### 模仿Java7的multi-catch和rethrow

在Java7中multi-catch不仅仅是让代码整洁，他提供了一个方法去捕获多个异常，排除其他的和抛出一些你认为重要的异常。

```java
} catch (JSONException | IOException | NumberFormatException errs) {
    throw errs;
} catch (Exception e) {
    // ignore all else
}
```

然而，人们经常迷恋于Java6，必须指出哪些异常需要忽略或者哪些异常需要处理或者抛出捕获的异常。Guava将使变得更简单：

```java
// in Java 6
// if you don't want to lose the type of exception
// you might be tempted to do something like this:
} catch (Throwable t) {
    if (t instance of IOException ||
        t instance of NumberFormatException ||
        t instance of JSONException) {
            throw t;    /* THIS IS ILLEGAL, COMPILE TIME EXCEPTION */
    }
}
```

备选方案会更好理解一些。我将会给读者展示如何跳过异常，使用Guava就是下面这样：

```java
// using guava
} catch (Throwable t) {
    // using static imports you could simplify this further
    Throwables.propagateIfInstanceOf(t, IOException.class);
    Throwables.propagateIfInstanceOf(t, NumberFormatException.class);
    Throwables.propagateIfInstanceOf(t, JSONException.class);
}
```

### EventBus

是的，Guava附带了EventBus。使用EventBus代替监听模式可以极大的提高代码的灵活性。

### MultiMap API

基本上，很多时候你都可以使用MultiMap来代替Map<Key, Collection<V>>或者Map<Key, HashMap<K, V>>。这样会给你减少很多扑朔迷离的冗余代码。