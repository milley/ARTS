# __cdecl解释

首先，__cdecl是Microsoft专用，用来为C调用约定和C++程序。由调用方清理堆栈，因为可以执行vararg函数。以下列表表示调用约定的实现。

| 元素 | 实现 |
|---| ----- |
| 参数传递顺序 | 从右往左 |
| 堆栈维护职责 | 调用函数从堆栈中弹出自变量 |
| 名称修饰约定 | 下划线字符(_)前缀的名称，除非当_导出使用C链接的cdecl函数 |
| 大小写转换约定 | 不执行任何大小写转换 |

位置 __cdecl在变量或者函数名称前面的修饰符。 由于 C 命名和调用约定为默认值，时，才必须使用 __cdecl在 x86 代码是在指定/Gv(vectorcall)、 /Gz (stdcall) 或/Gr(fastcall)编译器选项。 /Gd编译器选项强制执行 __cdecl调用约定。

在 ARM 和 x64 处理器， __cdecl是接受，但编译器一般会忽略。 按照 ARM 和 x64 上的约定，自变量将尽可能传入寄存器，后续自变量传递到堆栈中。 在 x64 代码中，使用 __cdecl重写 /Gv编译器选项并使用默认的 x64 调用约定。

对于非静态类函数，如果函数是超行定义的，则调用约定修饰符不必在超行定义中指定。 也就是说，对于类非静态成员方法，在定义时假定声明期间指定的调用约定。 给定此类定义：

```c++
struct CMyClass {
   void __cdecl mymethod();
};
```

此：

```c++
void CMyClass::mymethod() { return; }
```

等效于：

```c++
void __cdecl CMyClass::mymethod() { return; }
```

与以前版本的兼容性cdecl并 _cdecl是的同义词 __cdecl除非编译器选项/Za(禁用语言扩展）指定。
