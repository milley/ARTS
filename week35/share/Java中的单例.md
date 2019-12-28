# Java中的单例

摘自[在Java中如何写一个正确的单例模式](https://time.geekbang.org/dailylesson/detail/100044001)

Java中经常会用到单例模式，比如日志工具、全局信息类，都会创建单例模式来使用。

## 1. 饿汉式

```java
public class Singleton {
  private static Singleton singleton = new Singleton();

  private Singleton() {}

  public static Singleton getInstance() {
    return singleton;
  }
}
```

写法简单，在类装载时完成了实例化，避免了线程同步问题。缺点式没有懒加载的效果，如果不使用会浪费资源。

下面描述静态代码块的方式，加载方式和上面一样：

```java
public class Singleton {
  private static Singleton singleton;

  static {
    singleton = new Singleton();
  }

  private Singleton() {}

  public Singleton getInstance() {
    return singleton;
  }
}
```

## 2. 懒汉式

```java
public class Singleton {
  private static Singleton singleton;
  private Singleton() {}

  public static Singleton getInstance() {
    if (singleton == null) {
      singleton = new Singleton();
    }
    return singleton;
  }
}
```

优点：可以懒加载。缺点：只能在单线程下使用。

通过增加同步来解决线程安全问题：

```java
public class Singleton {
  private static Singleton singleton;
  private Singleton() {}

  public static synchronized Singleton getInstance() {
    if (singleton == null) {
      singleton = new Singleton();
    }
    return singleton;
  }
}
```

缺点：获取实例串行处理，效率太低。

## 3. 双重检查式

```java
public class Singleton {
  private static volatile Singleton singleton;

  private Singleton() {}

  public static Singleton getInstance() {
    if (singleton == null) {
      synchronized (Singleton.class) {
        if (singleton == null) {
          singleton = new Singleton();
        }
      }
    }

    return singleton;
  }
}
```

优点：线程安全，延迟加载，效率较高。通过增加volatile关键字来使得singleton = new Singleton()防止重排序。缺点：不能防止被反序列化。

## 4. 静态内部类

```java
public class Singleton {
  private Singleton() {}

  private static class SingletonInstance {
    private static final Singleton singleton = new Singleton();
  }

  public static Singleton getInstance() {
    return SingletonInstance.singleton;
  }
}
```

优点：通过JVM来保证了线程安全，并且延迟加载，效率较高。缺点：不能防止被反序列化。

## 5. 枚举式

```java
public enum Singleton {
  INSTANCE;
  public void whateverMethod() {
    // ...
  }
}
```

优点：写法简单，线程安全，支持延迟加载，还能防止反序列化（通过java.lang.Enum的valueOf方法根据名字查找对象）。
