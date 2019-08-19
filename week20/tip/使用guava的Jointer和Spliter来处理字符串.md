# 使用guava的Jointer和Spliter来处理字符串

平时使用字符串处理还是用JDK自带的方法来处理，今天试用了下guava的Spliter和Joiner来处理分割和连接字符串，简直太好用了，推荐！

例子:

```java
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuickExample {
    private List<Person> persons;

    public List<Person> getPersons() {
        return persons;
    }

    public QuickExample() {
        persons = new ArrayList<>();
        persons.add(new Person("john", 18, Person.Gender.MALE));
        persons.add(new Person("bob", 22, Person.Gender.MALE));
        persons.add(new Person("chris", 23, Person.Gender.FEMALE));
        persons.add(new Person("dave", 17, Person.Gender.OTHER));
    }

    public static void main(String[] args) {
        List<Person> persons = new QuickExample().getPersons();

        // building a stream
        List<String> namesOfPeopleBelow20 = persons.stream()
                // pipelining a computation
                .filter(person -> person.getAge() < 20)
                // pipelining another computation
                .map(Person::getName)
                // teminating a stream
                .collect(Collectors.toList());
        System.out.println(namesOfPeopleBelow20);
    }
}
```
