# 用enum代替int常量

摘自：Effective Jave chapter6 枚举和注解

枚举类型(enum type)是指由一组固定的常量组成合法值的类型，例如一年中的季节、太阳系中的行星或者一副牌的花色。在Java引入枚举类型之前，通常是用一组int常量来表示枚举类型，其中一个int常量表示枚举类型的一个成员：

```java
// The int enum pattern - severely deficient!
public static final int APPLE_FUJI = 0;
public static final int APPLE_PIPPIN = 1;
public static final int APPLE_GRANNY_SMITH = 2;

public static final int ORANGE_NAVEL = 0;
public static final int ORANGE_TEMPLE = 1;
public static final int ORANGE_BLOOD = 2;
```

这种方式叫做int枚举模式(int enum pattern)，它存在着很多的不足。int枚举模式不具有类型安全性，几乎没有描述性可言。例如你将apple传到oracle的方法中，编译器也不会发出警告。

```java
// Tasty citrus flavored applesauce!
int i = (APPLE_FUJI - ORANGE_TEMPLE) / APPLE_PIPPIN;
```

还有一种变体，它使用的时String常量，而不是int常量。它虽然为这些常量提供了可打印的字符串，但是会导致初级用户用户直接把字符串常量硬编码到客户端代码中，而不是使用对应的常量字段名。一旦这样的硬编码字符串常量中包含书写错误，在编译时不会被检测到，但是在运行的时候都会报错。而且它会导致性能问题，因为它依赖于字符串的比较操作。

Java提供了一种替代的解决方案，可以避免int和String枚举模式的缺点，并提供更多好处。这就是枚举类型(enum type)。

```java
public enum Apple {FUJI, PIPPIN, GRANNY_SMITH}
public enum ORACLE {NAVEL, TEMPLE, BLOOD}
```

Java枚举类通过公有的静态final域为每个枚举常量导出一个实例。枚举类型没有可以访问的构造器，所以它是真正的final类。客户端不能创建枚举类型的实例，也不能对它进行扩展，因此不存在实例，而只存在声明过的枚举常量。

枚举类型保证了编译时的类型安全。例如声明参数的类型为Apple，它就能保证传到该参数上的任何非空的对象引用一定属于三个有效的Apple值之一，而其他任何视图传递类型错误的值都会导致编译时错误，就像视图将某种枚举类型的表达式赋给另一种枚举类型的变量，或者视图利用==操作符比较不同枚举类型的值都会导致编译时错误。

包含同名常量的多个枚举类型可以在一个系统中和平共处，因为每个类型都有自己的命名空间。你可以增加或者重新排列枚举类型中的常量，而无须重新编译它的客户端代码，因为导出的常量的域在枚举类型和它的客户端之间提供了一个隔离层：常量并没有被编译到客户端代码中，而是在int枚举模式之中。最终可以调用toString方法，将枚举转换成可打印的字符串。

除了完善int枚举模式的不足之外，枚举类型还允许添加任意的方法和域，并实现任意的接口。它们提供了所有Object方法的高级实现，实现了Comparable和Serializable接口，并针对枚举类型的可任意改变性设计了序列化方式。

举个有关枚举类型的例子，比如太阳系中的8颗行星。每颗行星都有质量和半径，通过这两个属性可以计算出它的表面重力。从而给定物体的质量，进而计算出一个物体在行星表面上的重量。

```java
// Enum type with data and behavior
public enum Planet {
    MERCURY(3.302e+23, 2.439e6),
    VENUS(4.8693+24, 6.052e6),
    EARTH(5.975e+24, 6.378e6),
    MARS(6.419e+23, 3.393e6),
    JUPITER(1.899e+27, 7.149e7),
    SATURN(5.685e+26, 6.024e7),
    URANUS(8.683e+25, 2.556e7),
    NEPTUNE(1.024e+26, 2.477e7);

    private final double mass;      // In kilograms
    private final double radius;    // In meters
    private final double surfaceGravity;    // In m / s^2
    // Universal gravitational constant in m^3 / kg s^2
    private static final double G = 6.67300E-11;

    // Constructor
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
        surfaceGravity = G * mass / (radius * radius);
    }

    public double mass() {return mass;}
    public double radius() {return radius;}
    public double surfaceGravity() {return surfaceGravity;}

    public double surfaceWeight(double mass) {
        return mass * surfaceGravity;   // F = ma
    }
}
```

为了将数据与枚举常量关联起来，得声明实例域，并编写一个带有数据并将数据保存在域中的构造器。枚举天生就是不可变的，因此所有的域都应该为final的。他们可以是公有的，但是最好将它们做成私有的，并提供公有的访问方法。在Planet这个示例中，构造器还计算和保存表面重力，但正是一种优化。每当surfaceWeight方法用到重力时，都会根据质量和半径重新计算，并返回它在常量所表示的行星上的重量。

下面根据某个物体在地球上的重量(任何单位)，打印出一张很棒的表格，显示出该物体在所有8颗行星上的重量：

```java
public class WeightTable {
    public static void main(String[] args) {
        double earthWeight = Double.parseDouble(args[0]);
        double mass = earthWeight / Planet.EARTH.surfaceGravity();
        for (Planet p : Planet.values()) {
            System.out.printf("Weight on %s is %f%n", p, p.surfaceWeight(mass));
        }
    }
}
```

注意就像所有的枚举一样，Planet有一个静态的values方法，按照声明顺序返回它的值数组。toString方法返回每个枚举值的声明名称，使得printfln和printf的打印变得更加容易。如果不满意这种字符串表示法，可以覆盖toString方法进行修改。

```terminal
Weight on MERCURY is 69.912739
Weight on VENUS is 0.000000
Weight on EARTH is 185.000000
Weight on MARS is 70.226739
Weight on JUPITER is 467.990696
Weight on SATURN is 197.316494
Weight on URANUS is 167.398264
Weight on NEPTUNE is 210.208751
```

直到2006年，即Java中增加了枚举的两年之后，当时冥王星还属于行星。这引发了一个问题，当把一个元素从一个枚举类型中移除时，会发生什么情况呢？答案是：没有引用该元素的任何客户端程序都会继续正常工作。因此，我们的WeightTable程序只会打印出一个少了一行的表格而已。对于引用了被删除元素的客户端程序又如何呢？如果重新编译客户端代码，就会失败，并在引用被删除行星的那一行出现一条错误消息；如果没有重新编译客户端代码，在运行时就会在这一行抛出一个异常。这是你能期待的最佳行为了，远比使用int枚举模式时好的多。

有些与枚举常量相关的行为，可能只会用在枚举类型的定义类或者所在的包中，那么这些方法最好被实现成私有的或者包级私有的。于是每个枚举常量都带有一组隐藏的行为，这使得枚举类型的类或者所在的包能够运作很好，像其他类一样，除非要将枚举方法导出至它的客户端，否则都应该声明为私有的，或者声明为包级私有的。

加入编写一个枚举类型表示计算器的四大基本操作，你想要提供一个方法来执行每个常量所表示的算术运算。有一种方法是通过启用枚举的值来实现：

```java
package com.milley.effective.enumandannotation;

public enum Operation {
    PLUS, MINUS, TIMES, DIVIDE;

    // Do the arithemtic operation represented by this constant
    public double apply(double x, double y) {
        switch (this) {
            case PLUS:
                return x + y;
            case MINUS:
                return x - y;
            case TIMES:
                return x * y;
            case DIVIDE:
                return x / y;
        }
        throw new AssertionError("Unknown op: " + this);
    }
}
```

这段代码很脆弱。如果添加了新的枚举常量，却忘记给switch添加响应的条件，枚举仍然可以编译，但是当你视图运维新的运算时，就会失败。

有一种更好的方法可以将不同的行为与每个枚举常量关联起来：在枚举类型中声明一个抽象的apply方法，并在特定于常量的类主体中，用具体的方法覆盖每个常量的抽象apply方法。

```java
public enum Operation2 {
    PLUS {public double apply(double x, double y) {return x + y;}},
    MINUS {public double apply(double x, double y) {return x - y;}},
    TIMES {public double apply(double x, double y) {return x * y;}},
    DIVIDE {public double apply(double x, double y) {return x / y;}};

    public abstract double apply(double x, double y);
}
```

如果给Operation2的版本添加新的常量，你就不可能会忘记提供apply方法。因为该方法紧跟在每个常量声明之后。即使你忘记了，编译器也会提醒你，因为枚举类型中的抽象方法必须被它的所有常量中的具体方法锁覆盖。

特定于常量的方法实现可以与特定于常量的数据结合起来。下面的Operation覆盖了toString方法以返回通常与该操作关联的符号：

```java
public enum Operation3 {
    PLUS("+") {
        public double apply(double x, double y) {return x + y;}
    },
    MINUS("-") {
        public double apply(double x, double y) {return x - y;}
    },
    TIMES("*") {
        public double apply(double x, double y) {return x * y;}
    },
    DIVIDE("/") {
        public double apply(double x, double y) {return x / y;}
    };

    private final String symbol;

    Operation3(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public abstract double apply(double x, double y);
}
```

上述的toString实现使得打印算术表达式变得非常容易，如下：

```java
public static void main(String[] args) {
    double x = Double.parseDouble(args[0]);
    double y = Double.parseDouble(args[1]);
    for (Operation3 op : Operation3.values()) {
        System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
    }
}
```

用2和4作为命令行参数来运行这段程序，会输出：

```terminal
2.000000 + 4.000000 = 6.000000
2.000000 - 4.000000 = -2.000000
2.000000 * 4.000000 = 8.000000
2.000000 / 4.000000 = 0.500000
```

枚举类型有一个自动产生的valueOf(String)方法，它将常量的名字转变成常量本身。如果在枚举类型中覆盖toString，要考虑编写一个fromString方法，将定制的字符表示法变回相应的枚举。下面代码可以为任意枚举完成这一技巧，只要每个常量都有一个独特的字符串表示法：

```java
// Implementing a fromString method on enum type
private static final Map<String, Operation3> stringToEnum = Stream.of(values()).collect(toMap(Object::toString, e -> e));

// Return Operation for String, if any
public static Optional<Operation3> fromString(String symbol) {
    return Optional.ofNullable(stringToEnum.get(symbol));
}
```

注意，在枚举常量被创建之后，Operation常量从静态代码块中被放入了stringToEnum的映射中。前面的代码在values()方法返回的数组上使用流。

特定于常量的方法实现有一个美中不足的地方，它们使得在枚举常量中共享代码变得更加困难。例如，考虑用一个枚举表示薪资包中的工作天数。这个枚举有一个方法，根据给定的某工人的基本工资（按小时）以及当天的工作时间，来计算他当天的报酬。在五个工作日中，超过正常八小时的工作时间都会产生加班费；在节假日中，所有的工作都产生加班工资。利用switch语句，很容易通过多个case标签分别应用到两个代码片段中，来完成这一计算：

```java
enum PayrollDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    private static final int MINS_PER_SHIFT = 8 * 60;

    int pay(int minutesWorked, int payRate) {
        int basePay = minutesWorked * payRate;

        int overtimePay;
        switch (this) {
            case SATURDAY:case SUNDAY:
                overtimePay = basePay / 2;
                break;
            default:
                overtimePay = minutesWorked <= MINS_PER_SHIFT ?
                        0 : (minutesWorked - MINS_PER_SHIFT) * payRate / 2;
        }

        return basePay + overtimePay;
    }
}
```

这段代码十分简洁，但是从维护的角度来看，它非常危险。假设将一个元素添加到该枚举中，或许是一个假期天数的特殊值，但是忘记给switch语句添加响应的case。程序依然可以编译，但pay方法会悄悄的将节假日的工作计算成正常工作日的工资。

真正想要的时每当添加一个枚举常量时，就强制选择一种加班报酬策略。有一种方法可以实现这一点，通过将加班工资计算移到一个私有的嵌套枚举中，将这个策略枚举的实例传到PayrollDay枚举的构造器中。之后PayrollDay枚举将加班工资计算委托给策略枚举，PayrollDay中就不需要switch语句或者特定于常量的方法实现了。虽然这种模式没有switch语句那么简洁，但更加安全，也更加灵活：

```java
enum PayrollDay2 {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY,
    SATURDAY(PayType.WEEKEND), SUNDAY(PayType.WEEKEND);

    private final PayType payType;

    PayrollDay2(PayType payType) {
        this.payType = payType;
    }

    PayrollDay2() {
        this(PayType.WEEKDAY);
    }

    int pay(int minutesWorked, int payRate) {
        return payType.pay(minutesWorked, payRate);
    }

    // The strategy enum type
    private enum PayType {
        WEEKDAY {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked <= MINS_PER_SHIFT ? 0:
                        (minsWorked - MINS_PER_SHIFT) * payRate / 2;
            }
        },
        WEEKEND {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked * payRate / 2;
            }
        };

        abstract int overtimePay(int mins, int payRate);
        private static final int MINS_PER_SHIFT = 8 * 60;

        int pay(int minsWorked, int payRate) {
            int basePay = minsWorked * payRate;
            return basePay + overtimePay(minsWorked, payRate);
        }
    }
}
```

如果枚举中的switch语句不是在枚举中实现特定于常量的行为的一种很好的选择，那么它们还有什么用处呢？枚举中的switch语句适合于给外部的枚举类型添加特定于常量的行为。假如，假设Operation枚举不受你的控制，你希望它有一个实例方法来返回每个运算的反运算。你可以用下列静态方法模拟这种效果：

```java
// Switch on an enum to simulate a missing method
public static Operation inverse(Operation op) {
    switch(op) {
        case PLUS: return Operation.MINUS;
        case MINUS: return Operation.PLUS;
        case TIMES: return Operation.DIVIDE;
        case DIVIDE: return Operation.TIMES;

        default: throw new AssertionError("Unknown op: " + op);
    }
}
```

那么什么时候应该使用枚举呢？每当需要一组固定常量，并且在编译时就知道其成员的时候，就应该使用枚举。
