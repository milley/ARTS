# Java中比较HashMap

## HashMap

### 使用Maps.equals方法

哈希表的比较有多种方式，最先想到的就是使用equals方法，示例如下:

```java
@Test
public void whenCompareTwoHashMapsUsingEquals_thenSuccess() {
    Map<Character, Integer> sMap = new HashMap<>();
    Map<Character, Integer> tMap = new HashMap<>();

    sMap.put('a', 1);
    sMap.put('b', 0);
    sMap.put('c', 3);

    tMap.put('c', 3);
    tMap.put('a', 1);
    tMap.put('b', 0);
    assertTrue(sMap.equals(tMap));
    assertFalse(sMap == tMap);
}
```

这里调用了Maps的equals方法来比较2个HashMap是否相等。如果自定义类型是不是还能正常使用呢，下面来测试：

```java
public class Person {
    private String name;
    private int age;
    private String email;
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    // omit getter and setter...
}

@Test
public void whenCompareTowHashMapUsingEquals_withNoEquals_thenSuccess() {
    Map<String, Person> sMap = new HashMap<>();
    Map<String, Person> tMap = new HashMap<>();
    sMap.put("zhangsan", new Person("zhangsan", 18, "zhangsan@123.com"));
    sMap.put("lisi", new Person("lisi", 20, "lisi@123.com"));
    tMap.put("lisi", new Person("lisi", 20, "lisi@123.com"));
    tMap.put("zhangsan", new Person("zhangsan", 18, "zhangsan@123.com"));
    assertFalse(sMap.equals(tMap));
}
```

自定义类没有实现equals通过equals比较两个map，返回是false。如果继承了Object并且实现equals呢？

```java
public class PersonImplEquals extends Object {
    private String name;
    private int age;
    private String email;
    public PersonImplEquals(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    // emit getter and setter ...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonImplEquals that = (PersonImplEquals) o;
        return age == that.age &&
                name.equals(that.name) &&
                email.equals(that.email);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, age, email);
    }
}
```

```java
@Test
public void whenCompareTowHashMapUsingEquals_withEquals_thenSuccess() {
    Map<String, PersonImplEquals> sMap = new HashMap<>();
    Map<String, PersonImplEquals> tMap = new HashMap<>();
    sMap.put("zhangsan", new PersonImplEquals("zhangsan", 18, "zhangsan@123.com"));
    sMap.put("lisi", new PersonImplEquals("lisi", 20, "lisi@123.com"));
    tMap.put("lisi", new PersonImplEquals("lisi", 20, "lisi@123.com"));
    tMap.put("zhangsan", new PersonImplEquals("zhangsan", 18, "zhangsan@123.com"));
    assertTrue(sMap.equals(tMap));
}
```

在覆盖了equals和hashCode方法后，可以使用equals来比较2个map。这里用的Maps.equals实际上是调用keys和values的Object.equals方法。

### 使用Java Stream API

```java
@Test
public void whenCompareTwoHashMapUsingStream_thenSuccess() {
    Map<Character, Integer> sMap = new HashMap<>();
    Map<Character, Integer> tMap = new HashMap<>();

    sMap.put('a', 1);
    sMap.put('b', 0);
    sMap.put('c', 3);

    tMap.put('c', 3);
    tMap.put('a', 1);
    tMap.put('b', 0);

    assertTrue(sMap.entrySet().stream().allMatch(e->e.getValue().equals(tMap.get(e.getKey()))));
}
```

通过Stream API可以正常处理包装类的情况，其实这种还可以处理非包装类比如数组，示例如下：

```java
Map<String, String[]> first = new HashMap<>();
Map<String, String[]> second = new HashMap<>();

first.put("abc", new String[]{"China", "Japan"});
first.put("def", new String[]{"1", "2", "3"});

second.put("def", new String[]{"1", "2", "3"});
second.put("abc", new String[]{"China", "Japan"});

assertTrue(first.entrySet().stream().allMatch(e -> Arrays.equals(e.getValue(), second.get(e.getKey()))));
```

### 比较HashMap的Keys和Values

#### 比较HashMap的Keys

使用keySet()来判断是否有相同的Keys：

```java
@Test
public void whenCompareTwoHashMapKeys_thenSuccess() {
    Map<Character, Integer> sMap = new HashMap<>();
    Map<Character, Integer> tMap = new HashMap<>();
    sMap.put('a', 1);
    sMap.put('b', 0);
    sMap.put('c', 3);
    tMap.put('c', 3);
    tMap.put('a', 1);
    tMap.put('b', 0);
    assertTrue(sMap.keySet().equals(tMap.keySet()));
}
```

#### 比较HashMap的Values

首先实现一个简单方法来检查key含有同样的value：

```java
@Test
public void whenCompareTwoHashMapKeyValuesUsingStreamAPI_thenSuccess() {
    Map<String, String> asiaCapital3 = new HashMap<String, String>();
    asiaCapital3.put("Japan", "Tokyo");
    asiaCapital3.put("South Korea", "Seoul");
    asiaCapital3.put("China", "Beijing");

    Map<String, String> asiaCapital4 = new HashMap<String, String>();
    asiaCapital4.put("South Korea", "Seoul");
    asiaCapital4.put("Japan", "Osaka");
    asiaCapital4.put("China", "Beijing");

    Map<String, Boolean> result = areEqualKeyValues(asiaCapital3, asiaCapital4);

    assertEquals(3, result.size());
    assertFalse(result.get("Japan"));
    assertTrue(result.get("South Korea"));
    assertTrue(result.get("China"));
}
```

### 使用Guava比较

最后来使用Guava的Maps.difference()方法来判断。这个方法返回一个MapDifference对象。

#### MapDifference.entriesDiffering()

```java
@Test
public void givenDifferentMaps_whenGetDiffUsingGuava_thenSuccess() {
    Map<String, String> asia1 = new HashMap<String, String>();
    asia1.put("Japan", "Tokyo");
    asia1.put("South Korea", "Seoul");
    asia1.put("India", "New Delhi");

    Map<String, String> asia2 = new HashMap<String, String>();
    asia2.put("Japan", "Tokyo");
    asia2.put("China", "Beijing");
    asia2.put("India", "Delhi");

    MapDifference<String, String> diff = Maps.difference(asia1, asia2);
    Map<String, MapDifference.ValueDifference<String>> entriesDiffering = diff.entriesDiffering();

    assertFalse(diff.areEqual());
    assertEquals(1, entriesDiffering.size());
    assertNotNull(entriesDiffering.get("India"));
    assertEquals("New Delhi", entriesDiffering.get("India").leftValue());
    assertEquals("Delhi", entriesDiffering.get("India").rightValue());
}
```

#### MapDifference.entriesOnlyOnRight()和MapDifference.entriesOnlyOnLeft()

```java
@Test
public void givenDifferentMaps_whenGetEntriesOnOneSideUsingGuava_thenSuccess() {
    Map<String, String> asia1 = new HashMap<String, String>();
    asia1.put("Japan", "Tokyo");
    asia1.put("South Korea", "Seoul");
    asia1.put("India", "New Delhi");

    Map<String, String> asia2 = new HashMap<String, String>();
    asia2.put("Japan", "Tokyo");
    asia2.put("China", "Beijing");
    asia2.put("India", "Delhi");

    MapDifference<String, String> diff = Maps.difference(asia1, asia2);
    Map<String, String> entriesOnlyOnRight = diff.entriesOnlyOnRight();
    Map<String, String> entriesOnlyOnLeft = diff.entriesOnlyOnLeft();

    assertEquals(1, entriesOnlyOnRight.size());
    assertEquals(1, entriesOnlyOnLeft.size());
    assertEquals(entriesOnlyOnRight.get("China"), "Beijing");
    assertEquals(entriesOnlyOnLeft.get("South Korea"), "Seoul");
}
```

#### MapDifference.entriesInCommon()

```java
@Test
public void givenDifferentMaps_whenGetCommonEntriesUsingGuava_thenSuccess() {
    Map<String, String> asia1 = new HashMap<String, String>();
    asia1.put("Japan", "Tokyo");
    asia1.put("South Korea", "Seoul");
    asia1.put("India", "New Delhi");

    Map<String, String> asia2 = new HashMap<String, String>();
    asia2.put("Japan", "Tokyo");
    asia2.put("China", "Beijing");
    asia2.put("India", "Delhi");

    MapDifference<String, String> diff = Maps.difference(asia1, asia2);
    Map<String, String> entriesInCommon = diff.entriesInCommon();

    assertEquals(1, entriesInCommon.size());
    assertEquals(entriesInCommon.get("Japan"),"Tokyo");
}
```

#### 定制Maps.difference()行为

```java
@Test
public void givenSimilarMapsWithArrayValue_whenCompareUsingGuava_thenFail() {
    Map<String, String[]> asiaCity1 = new HashMap<String, String[]>();
    asiaCity1.put("Japan", new String[] { "Tokyo", "Osaka" });
    asiaCity1.put("South Korea", new String[] { "Seoul", "Busan" });

    Map<String, String[]> asiaCity2 = new HashMap<String, String[]>();
    asiaCity2.put("South Korea", new String[] { "Seoul", "Busan" });
    asiaCity2.put("Japan", new String[] { "Tokyo", "Osaka" });

    MapDifference<String, String[]> diff = Maps.difference(asiaCity1, asiaCity2);
    assertFalse(diff.areEqual());
}
```

可以定义Equivalence为类型String[]来比较数组：

```java
@Test
public void givenSimilarMapsWithArrayValue_whenCompareUsingGuavaEquivalence_thenSuccess() {
    Map<String, String[]> asiaCity1 = new HashMap<String, String[]>();
    asiaCity1.put("Japan", new String[] { "Tokyo", "Osaka" });
    asiaCity1.put("South Korea", new String[] { "Seoul", "Busan" });

    Map<String, String[]> asiaCity2 = new HashMap<String, String[]>();
    asiaCity2.put("South Korea", new String[] { "Seoul", "Busan" });
    asiaCity2.put("Japan", new String[] { "Tokyo", "Osaka" });

    Map<String, String[]> asiaCity3 = new HashMap<String, String[]>();
    asiaCity3.put("South Korea", new String[] { "Seoul", "Busan" });
    asiaCity3.put("Japan", new String[] { "Tokyo", "Osaka", "Yokohama" });

    Equivalence<String[]> eq = new Equivalence<String[]>() {
        @Override
        protected boolean doEquivalent(String[] a, String[] b) {
            return Arrays.equals(a, b);
        }

        @Override
        protected int doHash(String[] value) {
            return value.hashCode();
        }
    };

    MapDifference<String, String[]> diff = Maps.difference(asiaCity1, asiaCity2, eq);
    assertTrue(diff.areEqual());

    diff = Maps.difference(asiaCity1, asiaCity3, eq);
    assertFalse(diff.areEqual());
}
```
