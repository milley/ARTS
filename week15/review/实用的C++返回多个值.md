# 实用的C++返回多个值

[Applied C++: Return Multiple Values](https://link.medium.com/O68PHr4OjY)

## 提问"我知道已经支持了吗"

使用C++17函数返回多个值的最佳方案是什么？

- 使用输出参数：

```c++
auto output_1(int &i1) { i1 = 11; return 12; }
```

- 使用一个本地结构体：

```c++
auto struct_2() { struct _{int i1, i2;}; return _{21, 22}; }
```

- 使用std::pair:

```c++
auto pair_2() { return std::make_pair(31, 32); }
```

- 使用std::tuple:

```c++
auto tuple_2() { return std::make_tuple(41, 42); }
```

答案就在文章的最后面。

## 使用案例：为什么要返回多个值

一个典型的例子就是

```c++
std::from_chars()
```

C++17函数例如strtol()。但是from_chars()返回3个值：一个解析过的数值，一个错误编码，和一个指向第一个无效字符的指针。

这个函数使用了一个混合技术：数值是以输出参数的形式返回，但是错误编码和指针是以结构体返回。为什么是这样？让我们接着分析...

## 分析

### 使用多个输出参数返回

示例代码：

```c++
auto output_1(int &i1) {
    i1 = 11;    // Output first parameter
    return 12;  // Return second value
}

// Use volatile pointers so compiler could not inline the function
auto (*volatile output_1_ptr)(int &i1) = output_1;
int main() {
    int o1, o2;     // Define local variables
    o2 = output_1_ptr(o1);  // Output 1st param and assign the 2nd
    printf("output_1 o1 = %d, o2 = %d\n", o1, o2);
}
```

代码编译成：

```Assembly
output_1(int&):                          # @output_1(int&)
        mov     dword ptr [rdi], 11
        mov     eax, 12
        ret
main:                                   # @main
        push    rax
        mov     rax, qword ptr [rip + output_1_ptr]
        lea     rdi, [rsp + 4]
        call    rax
        mov     ecx, eax
        mov     esi, dword ptr [rsp + 4]
        mov     edi, offset .L.str
        xor     eax, eax
        mov     edx, ecx
        call    printf
        xor     eax, eax
        pop     rcx
        ret
_GLOBAL__sub_I_example.cpp:             # @_GLOBAL__sub_I_example.cpp
        push    rax
        mov     edi, offset std::__ioinit
        call    std::ios_base::Init::Init() [complete object constructor]
        mov     edi, offset std::ios_base::Init::~Init() [complete object destructor]
        mov     esi, offset std::__ioinit
        mov     edx, offset __dso_handle
        pop     rax
        jmp     __cxa_atexit            # TAILCALL
output_1_ptr:
        .quad   output_1(int&)

.L.str:
        .asciz  "output_1 o1 = %d, o2 = %d\n"
```

编译器：
[godbolt](https://godbolt.org/z/Fan8OH)

优点：

- 传统的。容易理解。
- 使用了C(指针)， 工作在C++标准下
- 支持函数重载

缺点：

- 函数调用之前需要先加载第一个参数的地址
- 第一个参数需要用栈，比较慢
- 通过System V AMD64 ABI,我们可以超过注册6个地址。加载超过6个参数，更慢

通过这个例子输出7个参数来解释最后一个缺点：

```c++
// Output more than 6 params
int output_7(int &i1, int &i2, int &i3, int &i4, int &i5, int &i6, int &i7) {
    i1 = 11;
    i2 = 12;
    i3 = 13;
    i4 = 14;
    i5 = 15;
    i6 = 16;
    i7 = 17;
    return 18;
}
```

反编译output_7():

```Assembly
output_7(int&, int&, int&, int&, int&, int&, int&):              # @output_7(int&, int&, int&, int&, int&, int&, int&)
        mov     rax, qword ptr [rsp + 8]
        mov     dword ptr [rdi], 11
        mov     dword ptr [rsi], 12
        mov     dword ptr [rdx], 13
        mov     dword ptr [rcx], 14
        mov     dword ptr [r8], 15
        mov     dword ptr [r9], 16
        mov     dword ptr [rax], 17
        mov     eax, 18
        ret
```

第7个地址使用了栈，所以我们把地址压入栈，然后我们使用栈读取，然后我们输出那个地址...太多的内存操作。比较慢。

### 使用本地结构体返回多个值

实例代码：

```c++
#incude <stdio.h>

auto struct_2() {
    struct _ {          // Declare a local structure with 2 integers
        int i1, i2;
    };
    return _{21, 22};   // Return the local structure
}

// Use volatile pointers so compiler could not inline the function
auto (*volatile struct_2_ptr)() = struct_2;

int main() {
    auto [s1, s2] = struct_2_ptr();     // Structured binding declaration
    printf("struct_2 s1 = %d, s2 = %d\n", s1, s2);
}
```

反编译：

```Assembly
struct_2():                           # @struct_2()
        movabs  rax, 94489280533
        ret
main:                                   # @main
        push    rax
        mov     rax, qword ptr [rip + struct_2_ptr]
        call    rax
        mov     rcx, rax
        mov     rdx, rax
        shr     rdx, 32
        mov     edi, offset .L.str
        xor     eax, eax
        mov     esi, ecx
        call    printf
        xor     eax, eax
        pop     rcx
        ret
_GLOBAL__sub_I_example.cpp:             # @_GLOBAL__sub_I_example.cpp
        push    rax
        mov     edi, offset std::__ioinit
        call    std::ios_base::Init::Init() [complete object constructor]
        mov     edi, offset std::ios_base::Init::~Init() [complete object destructor]
        mov     esi, offset std::__ioinit
        mov     edx, offset __dso_handle
        pop     rax
        jmp     __cxa_atexit            # TAILCALL
struct_2_ptr:
        .quad   struct_2()

.L.str:
        .asciz  "struct_2 s1 = %d, s2 = %d\n"
```

编译链接：
[godbolt](https://godbolt.org/z/Q7P4q0)

优点：

- 使用了部分C++标准，包含了C，必须在函数作用域外面定义结构体
- 在寄存器中返回128位，没有使用栈，速度快
- 不需要额外的参数地址，允许编译器更好的优化代码

缺点：

- 需要C++17结构体绑定定义
- 函数不能被重载，返回类型不是函数声明的一部分

当我们试着返回多个值出现了什么？据System V AMD64 ABI来说，值大于128位会储存在RAX和RDX。所以多于4个32位整数将会返回在寄存器中。多余一个字节都会使用到栈。

我们不需要加载输出参数的地址，所以这个比输出参数方法更快。

### 使用std::pair返回多个参数

示例代码：

```c++
#include <iostream>
#include <utility> /* for std::pair */

auto pair_2() { return std::make_pair(31, 32); } // Just one line!

// Use volatile pointers so compiler could not inline the function
auto (*volatile pair_2_ptr)() = pair_2;

int main() {
  auto [p1, p2] = pair_2_ptr();  // Structured binding declaration
  printf("pair_2 p1 = %d, p2 = %d\n", p1, p2);
}
```

```Assenbly
pair_2():                             # @pair_2()
        movabs  rax, 137438953503
        ret
main:                                   # @main
        push    rax
        mov     rax, qword ptr [rip + pair_2_ptr]
        call    rax
        mov     rcx, rax
        mov     rdx, rax
        shr     rdx, 32
        mov     edi, offset .L.str
        xor     eax, eax
        mov     esi, ecx
        call    printf
        xor     eax, eax
        pop     rcx
        ret
_GLOBAL__sub_I_example.cpp:             # @_GLOBAL__sub_I_example.cpp
        push    rax
        mov     edi, offset std::__ioinit
        call    std::ios_base::Init::Init() [complete object constructor]
        mov     edi, offset std::ios_base::Init::~Init() [complete object destructor]
        mov     esi, offset std::__ioinit
        mov     edx, offset __dso_handle
        pop     rax
        jmp     __cxa_atexit            # TAILCALL
pair_2_ptr:
        .quad   pair_2()

.L.str:
        .asciz  "pair_2 p1 = %d, p2 = %d\n"
```

编译链接：
[godbolt](https://godbolt.org/z/9iXzSb)

优点：

- 仅仅一行代码
- 不用声明本地的结构体
- 和本地结构体一样，用寄存器返回多于128位，不用栈

缺点：

- Pair仅仅返回2个值
- 和本地结构体一样，不能被重载

### 使用std::tuple返回多个参数

示例代码：

```c++
#include <iostream>
#include <tuple>

auto tuple_2() { return std::make_tuple(41, 42); } // Just one line!

// Use volatile pointers so compiler could not inline the function
auto (*volatile tuple_2_ptr)() = tuple_2;

int main() {
  auto [t1, t2] = tuple_2_ptr();  // Structured binding declaration
  printf("tuple_2 t1 = %d, t2 = %d\n", t1, t2);
}
```

```Assembly
tuple_2():                            # @tuple_2()
        movabs  rax, 176093659178
        mov     qword ptr [rdi], rax
        mov     rax, rdi
        ret
main:                                   # @main
        push    rax
        mov     rax, qword ptr [rip + tuple_2_ptr]
        mov     rdi, rsp
        call    rax
        mov     edx, dword ptr [rsp]
        mov     esi, dword ptr [rsp + 4]
        mov     edi, offset .L.str
        xor     eax, eax
        call    printf
        xor     eax, eax
        pop     rcx
        ret
_GLOBAL__sub_I_example.cpp:             # @_GLOBAL__sub_I_example.cpp
        push    rax
        mov     edi, offset std::__ioinit
        call    std::ios_base::Init::Init() [complete object constructor]
        mov     edi, offset std::ios_base::Init::~Init() [complete object destructor]
        mov     esi, offset std::__ioinit
        mov     edx, offset __dso_handle
        pop     rax
        jmp     __cxa_atexit            # TAILCALL
tuple_2_ptr:
        .quad   tuple_2()

.L.str:
        .asciz  "tuple_2 t1 = %d, t2 = %d\n"
```

编译链接：
[godbolt](https://godbolt.org/z/hSVV72)

优点：

- 代码仅仅一行，和std::pair一样
- 不像std::pair，可以轻易的增加多个值

缺点：

- 不幸的是，反编译出来是个混合的包。我们需要将一个地址和一个输出对应起来，一个对应一个tuple。
- 相对于2个整形(64位)，返回值还是用到了栈，比较慢。

当我们返回多余一个值用tuple是怎么样？增加一个值不会改变反编译结果：我们仍然需要将一个地址指向栈，然后在哪个地址上放入值，然后在使用printf从栈上加载他们。

寄存器在返回多于128位，这比起pair和结构体更慢。但是比起输出参数更快，我们需要传入函数一部分地址，不是仅仅一个。

### 要点

1. 使用C++17最快的返回多个参数的方法是使用std::pair
2. std::pair必须是返回2个值并且可以方便快速的使用
3. 如果需要重载使用输出参数。这也是为什么std::from_chars()使用了输出参数和一个返回结构体。
