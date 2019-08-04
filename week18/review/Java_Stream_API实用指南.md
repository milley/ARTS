# Java Stream API实用指南

[Practical Guide to Java Stream API](https://link.medium.com/2jnxYA7uSY)

Java8中引入了Stream API。它提供了声明式的方法来迭代和执行集合的操作方法。直到Java7，for和for each是仅有的供选择的选项。在这篇文章我将会介绍你使用Stream API来完成集合提供的抽象的公共操作。

> 在使用命令式编程语言，开发者使用语言构建为什么做和怎么做。然而当使用声明式编程，开发者需要聚焦在如何去做然后语言或者框架去操心如何去做。因此在声明式编程中，代码是简洁的并且错误率比较低。

通常集合的操作都会按下面分类。虽然下面的列表不是详尽的，但是也覆盖了我们日常操作的大部分。我将会使用下面提及的操作示例来介绍Stream API。

- Transforming(转换)
- Filtering(过滤)
- Searching(搜索)
- Reordering(重排序)
- Summarizing(总结)
- Grouping(分组)

在示例中，我将会使用一个Person对象的集合。为了方便理解我将Person类写在下面。

```java
public class Person {
    private final String name;
    private final int age;
    private final Gender gender;

    public Person(String name, int age, Gender gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
```

## 一个Stream API的快速介绍

在深入了解集合的Stream API之前，我们先用一个例子来介绍下Stream API它自己来帮助理解。

```java
List<Person> people = new QuickExample().getPersons();
// building a stream
List<String> namesOfPeopleBelow20 = people.stream()
        // pipelining a computation
        .filter(person -> person.getAge() < 20)
        // pipelining another computation
        .map(Person::getName)
        // teminating a stream
        .collect(Collectors.toList());
System.out.println(namesOfPeopleBelow20);
```

上面的例子中，多个操作可以被串行到一起执行就像一个管道一样。这就是为什么我们提及流管道的原因。流管道可以分为下面3个部分：

1. 创建流--在上面例子中，我们有一个persons代表Person的集合。stream方法是Java8中增加进来的Collection接口，调用后可以创建一个流。比起Collection相同点就是都是数组(Arrays.stream())和生产者函数(Stream.iterate()和Stream.generate())。
2. 串联操作--一旦一个流对象被创建，你可以申请0、1或者更多操作在这个流上串联起来，就像建造者模式。上面示例中所有的方法，filter和map，都是在流接口上允许串联然后返回流的实例。在返回流的操作，就被称作串联操作。
3. 终止操作--一旦所有的操作都完成，你可以通过强制结束操作来结束管道。终止操作也是Stream接口的方法并且返回了一个合成的结果。上面例子中collect(Collectors.toList())返回了一个List的实例。合成类型可以是集合类型也可以不是取决于如何使用结束操作。它可以是原值也可以是一个对象的实例这样就不是集合。

现在让我们看基本操作。虽然我们学习的是个别的流操作，你可以扩展其他的不同用法。

## Transforming(转换)

转换意味着从一个值类型转换到另外一种类型。这里我希望通过从Person集合转换成名字的集合。在这种情况下我需要使用转换操作把Person转换到name。

在下面的例子中，我使用map串联操作将Perple转换到一个String类型的name字段。Person::getName是方法引用等同于person->person.getName()是一个方法的实例。

```java
List<String> namesOfPeople = people.stream.map(Person::getName).collect(Collectors.toList());
```

## Filtering(过滤)

根据字面意思，过滤操作当满足断言重点的条件时才允许流下去。过滤操作由Predicate组成。

过滤还可以根据数量限制部分元素。Stream API提供了skip()和limit()操作。

在第一个例子中，person->person.getAge() < 20 断言就是用来创建一个包含比20岁小的条件的集合。在第二个例子中，先跳过最先的前2个后选择10个。

```java
// count based filtering
List<Person> smallerListOfPeople = people.stream().skip(2).limit(10).collect(Collectors.toList());
```

## Search(搜索)

从字面意思理解，在集合上搜索表示搜索集合的元素或者搜索元素是否存在。搜索可以返回也可以不返回值，因此你得到一个Optional。搜索是否存在会返回一个boolean。

下面的例子，通过findAny()搜索元素和anyMatch()搜索是否存在。

```java
// searching for a element
Optional<Person> any = people.stream().filter(person -> person.getAge() < 20).findAny();

// searching for existence
boolean isAnyOneInGroupLessThan20Years = people.stream().anyMatch(person -> person.getAge() < 20);
```

## Reordering(重排序)

如果你希望将集合中的元素排序，你可以使用sorted中间操作。它将会取一个Comparator接口实例。创建这个实例我一般使用Comparator的comparing工厂方法。

下面的例子重排序了年龄。

```java
List<Person> peopleSortedEldestToYoungest = people.stream().sorted(Compator.comparing(Person::getAge).reversed())
    .collect(Collectors.toList());
```

> 不像其他的操作，sorted操作是有状态的。它意味着在流操作结束前这个操作对所有的元素都可以串联。另外一个类似的操作时distinct。

## Summarizing(总结)

有的时候你想从集合中获得一些信息。例如获取年龄总和。在Stream API中，这个可以用结束操作来完成。reduce和collect都是终止操作都可以达成这个目的。也可以使用高级的操作例如sum,count,summaryStatistics等等。下面是reduce和collect的用法。

```java
// calculating sum using reduce terminal operator
people.stream().mapToInt(Person::getAge).reduce(0, (total, currentValue) -> total + currentValue);

// calculating sum using sum terminal operator
people.stream().mapToInt(Person::getAge).sum();

// calculating count using count terminal operator
people.stream().mapToInt(Person::getAge).count();

// calculating summary
IntSummaryStatistics ageStatistics = people.stream()
    .mapToInt(Person::getAge).summaryStatistics();
ageStatistics.getAverage();
ageStatistics.getCount();
ageStatistics.getMax();
ageStatistics.getMin();
ageStatistics.getSum();
```

> reduce和collect都是分解操作。reduce意味着多次分解然而collect也意味着多次分解。多次分解是优先的选择。然而，实际情况是性能优先，可变分解优先于不变。

## Grouping(分组)

分组也可以称为分类。有的时候我们会把一个集合分为几个组。返回的数据结构就像Map，key代表了分组因素value代表了特征。Stream API提供了Collectors.groupingBy来提供这样的功能。

在下面的例子，使用gender来进行分组。不通之处在于values。第一个例子的Person集合被每个分组创建。第二个Collectors.mapping()提取了Person的name创建一个name集合。第三个，每个Person的age被提取然后求平均值。

```java
// Grouping people by gender
Map<Gender, List<Person>> peopleByGender = people.stream()
    .collect(Collectors.groupingBy(Person::getGender, Collectors.toList()));

// Grouping person names by gender
Map<Gender, List<String>> nameByGender = people.stream()
    .collect(Collectors.groupingBy(Person::getGender));

// Grouping average age by gender
Map<Gender, Double> averageAgeByGender = people.stream()
    .collect(Collectors.groupingBy(Person::getGender,
        Collectors.averagingInt(Person::getAge)));
```

## 总结

这个教程展示了Java Stream API的常用用法来帮助我们使用流管道来操作集合。这种声明式用法使代码更简洁且不易出错。希望这个指南能让你开始更好的使用Java Stream API。
