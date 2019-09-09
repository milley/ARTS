# 返回零长度的数组或者集合，而不是null

相信有不少人会有写这样的经历：

```java
private final List<Cheese> cheesesInStock = ...;

public List<Cheese> getCheeses() {
    return cheesesInStock.isEmpty() ? null : new ArrayList<>(cheeseInStock);
}
```

把没有奶酪(cheese)可买的情况当做是一种特例，这是不合常理的。这样做会要求客户端中必须有额外的代码来处理null返回值，例如：

```java
List<Cheese> cheeses = shop.getCheeses();
if (cheeses != null && cheeses.contains(Cheeses.STILTON)) {
    System.out.println("Jolly good, just the thing.");
}
```

对于一个返回null而不是零长度数组或者集合的方法，几乎每次用到该方法时都需要这种曲折的处理方法。这样做很容易出错，因为编写客户端的程序员可能会忘记判断null返回值。返回null而不是零长度的容器，也会使返回该容器的方法实现代码变得更加复杂。

有时候会有人认为：null返回值比零长度集合或者数组更好，因为它避免了分配零长度的容器所需要的开销。这种观点是站不住脚的，原因有两点。第一，在这个级别上担心性能问题是不明智的，除非分析表明这个方法正是造成性能问题的真正源头。第二，不需要分配零长度的集合或者数组，也可以返回它们。下面是返回可能的零长度集合的一段典型代码。一般情况下，这些都是必须的：

```java
public List<Cheese> getCheeses() {
    return new ArrayList<>(cheesesInStock);
}
```

万一有证据表示分配了零长度的集合损害了程序的性能，可以通过重复返回同一个不可变的零长度集合，避免了分配的执行，因为不可变对象可以被自由共享。下面的代码正是这么做的，它使用了Collections.emptyList方法。如果返回的是集合，最好使用Collections.emptySet；如果返回的时映射，最好使用Collections.emptyMap。但是要记住，这是一个优化，并且几乎用不上。如果你认为确实需要，必须在行动前后分别测试性能，确保这么做是有帮助的：

```java
public List<Cheese> getCheeses() {
    return cheesesInStock.isEmpty() ? Collections.emptyList() : new ArrayList<>(cheesesInStock);
}
```

数组的情形与集合的情形一样，永远不会返回null，而是返回零长度的数组。一般来说，应该只返回一个正确长度的数组，这个长度可以是零。

```java
public Cheese[] getCheeses() {
    return cheesesInStock.toArray(new Cheese[0]);
}
```

如果确信分配零长度的数组会伤害性能，可以重复返回同一个零长度的数组，因为所有的零长度的数组都是不可变的：

```java
private static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];

public Cheese[] getCheeses() {
    return cheesesInStock.toArray(EMPTY_CHEESE_ARRAY);
}
```

在优化性能的版本中，我们将同一个零长度的数组传进了每一次的toArray调用，每当cheesesInStock为空时，就会从getCheese返回这个数组。千万不要指望通过预先分配传入toArray的数组来提升性能。

```java
// Don't do this - preallocating the array harms performance!
return cheesesInStock.toArray(new Cheese[cheesesInStock.size()]);
```

简而言之，永远不要返回null。
