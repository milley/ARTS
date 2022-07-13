# Rust过程宏

[Procedural Macros](https://doc.rust-lang.org/nightly/reference/procedural-macros.html)

过程宏允许像执行函数一样创建语法扩展。有三种方法使用过程宏：

- 类函数宏 - custom!(...)
- 推导宏 - #[derive(CustomDerive)]
- 属性宏 - #[CustomAttribute]

过程宏允许你在编译器使用rust语法运行代码，无论是消费还是生产语法。你可以想象成过程宏是从一个抽象语法树转换到另外一个抽象语法树。

过程宏必须在crate中定义为proc-macro类型。

```toml
[lib]
proc-macro = true
```

作为函数，它们必须返回语法，恐慌或者无休止的循环。返回语法依赖于各种类型的过程宏的替换或者添加。恐慌是编译器产生并返回一个编译错误。无限循环不会被编译器捕获，将会挂起整个编译器。

过程宏在编译期运行，并且和编译器共享相同的资源。例如，标准输入、输出、错误和编译器访问的相同。类似的，文件访问也是相同。正如这些，过程宏和cargo编译脚本拥有相同的安全性。

过程宏有两种方式来报告错误。第一种是panic，第二种是发送compile_error的宏调用。

## proco_macro trait

过程宏通常被链接到proc_macro crate。proc_macro crate提供了需要写入过程宏的类型使得使用起来更容易。

这个crate主要包含了TokenStream类型。过程宏运行token streams来代替抽象语法树节点，随着时间的推移，编译器和过程宏对目标接口更加稳定。一个token stream大约等同于Vec<TokenTree>，TokenTree大概可以理解成文本标记。例如foo是一个Ident标记，.是一个Punct标记，1.2是一个文本标记。Tokenstream类型，不同于Vec<TokenTree>，可以更廉价的clone。

所有标记都要指定的Span。一个Span不能被修改但是可以批量产生。Span S描述了程序中代码的扩展，主要被用来错误报告。当你不能修改Span自身，可以修改有关的标记，例如通过另外一个标记来获取Span。

## 过程宏事项

过程宏是不健康的。这意味着如果output token stream被简单写入到内联代码中。这意味着被外部元素影响着并且影响外部导入。

宏作者需要非常小心确保他们的宏可以大多数限制的上下文。这需要使用相对路径包含库中的元素，或者确保生成函数可以和其他函数很好区分。

## 类函数过程宏

类函数过程宏可以使用宏调用操作符(!)来调用。

那些宏可以使用proc_macro属性来定义公共函数，签名是(TokenStream) -> TokenStream。输入的TokenStream在调用的分隔符内，输出的TokenStream会替换整个调用。

例如，下面的函数在它的作用域内忽略了输入输出answer：

```rust
extern crate proc_macro;
use proc_macro::TokenStream;

#[proc_macro]
pub fn make_answer(_item: TokenStream) -> TokenStream {
    "fn answer() -> u32 { 42 }".parse().unwrap()
}
```

我们可以在crate中使用它打印42：

```rust
extern crate proc_macro_examples;
use proc_macro_examples::make_answer;

make_answer!();

fn main() {
    println!("{}", answer());
}
```

类函数过程宏可能在宏调用的任意地方调用，包含声明，表达式，模式匹配，类型表达式，元素位置，通过extern块包裹，继承和实现trait，和trait定义。

## Derive宏

Derive macros定义了derive属性的输入。那些宏可以通过给定的struct,enum,union创建元素。他们也可以定义derive macro helper attributes。

用户派生宏可以使用proc_macro_derive属性和(TokenStream) -> TokenStream来定义。

输入的TokenStream是包含derive属性的元素标记流。输出TokenStream必须是输入流中一系列添加到模块或者块的元素。

下面是派生宏的示例。它没有做任何事情，只是添加了一个函数answer:

```rust
extern crate proc_macro;
use proc_macro::TokenStream;

#[proc_macro_derive(AnswerFn)]
pub fn derive_answer_fn(_item: TokenStream) -> TokenStream {
    "fn answer() -> u32 { 42 }".parse().unwrap()
}
```

接着使用了派生宏：

```rust
extern crate proc_macro_examples;
use proc_macro_examples::AnswerFn;

#[derive(AnswerFn)]
struct Struct;

fn main() {
    assert_eq!(42, answer());
}
```

## 派生宏属性帮助

派生宏可以在其元素的作用域内添加属性。所说的属性是派生宏的帮助属性。这些属性是惰性的，他们的唯一目的是提供给定义他们的派生宏。意味着，可以被所用宏发现。

定义帮助属性的方式是将属性关键字放在proc_macro_derive宏中用逗号隔开各自的帮助属性名称。

例如下面的派生宏定义了帮助属性helper，最终没有做任何事：

```rust
#[proc_macro_derive(HelperAttr, attributes(helper))]
pub fn derive_helper_attr(_item: TokenStream) -> TokenStream {
    TokenStream::new()
}
```

在struct的派生宏中使用：

```rust
#[derive(HelperAttr)]
struct Struct {
    #[helper] field: ()
}
```

## 属性宏

属性宏定义了外部属性，可以被附加到元素，extern block包括的元素，继承和实现trait，和trait定义。

属性宏使用proc_macro_attribute属性定义为公共函数，签名为(TokenStream, TokenStream) -> TokenStream。第一个TokenStream紧跟着属性名称的分割标识树，不包含外部分割。如果属性被写成空的名称，TokenStream属性就是空的。第二个TokenStream包含属性的剩余的元素。返回的TokenStream替换任意数量元素。

例如，这个属性宏获取输入流然后返回它，有效的成为属性的开始。

```rust
#[proc_macro_attribute]
pub fn return_as_is(_attr: TokenStream, item: TokenStream) -> TokenStream {
    item
}
```

下面示例展示了字符串化TokenStream S。将会在编译器看到输出。输出将出现在"out:"前缀的注释后：

```rust
// my-macro/src/lib.rs

#[proc_macro_attribute]
pub fn show_streams(attr: TokenStream, item: TokenStream) -> TokenStream {
    println!("attr: \"{}\"", attr.to_string());
    println!("item: \"{}\"", item.to_string());
    item
}
```

```rust
// src/lib.rs
extern crate my_macro;

use my_macro::show_streams;

// Example: Basic function
#[show_streams]
fn invoke1() {}
// out: attr: ""
// out: item: "fn invoke1() { }"

// Example: Attribute with input
#[show_streams(bar)]
fn invoke2() {}
// out: attr: "bar"
// out: item: "fn invoke2() {}"

// Example: Multiple tokens in the input
#[show_streams(multiple => tokens)]
fn invoke3() {}
// out: attr: "multiple => tokens"
// out: item: "fn invoke3() {}"

// Example:
#[show_streams { delimiters }]
fn invoke4() {}
// out: attr: "delimiters"
// out: item: "fn invoke4() {}"
```

## 声明宏标记和过程宏标记

声明macro_rules宏和过程宏类似，但是标记有所不同。

macro_rules中的标记树定义如下：

- 分割分组((...), {...}, etc)
- 所有语言支持的操作，包括单字符和多字符(+, +=)，注意不包含单引号'
- 字符串，注意负的(e.g. -1)不是字符串的一部分，是分割操作标识
- 标识符，包括关键字(ident, r#ident, fn)
- 生命周期 ('ident)
- macro_rules中代替元变量(e.g. $my_expr in macro_rules! mac { ($my_expr: expr) => { $my_expr } }，在mac扩展后，会被识别成单个标识树

过程宏中标识树定义如下：

- 分割分组((...), {...}, etc)
- 语言支持的所有标点符号(+, but not +=)，切包含单引号'
- 字符串，注意(e.g. -1)支持整数的一部分，浮点的字符串
- 标识符，包括关键字(ident, r#ident, fn)

考虑到token streams传出或传入给过程宏，这两个定义的不匹配。

注意下面转换会惰性发生，如果标记没被检查是不会发生。

什么时候传入proc-macro：

- 所有多字符标识并入单字符
- '字符标识的生命周期
- 所有元变量用下面的token stream代替：当需要保留解析优先级，这样的token stream可以使用显示的分隔符(Delimiter::None)包装成分割的组(Group)。tt和ident转换从来没有包装到这个组，并且经常被描述为潜在的标识树。

当使用proc触发宏：

- 标点符号在使用的情况下合入多字符操作符
- 单引号标识连接的生命周期
- 当需要保留解析优先级，负的常量被转换到两个标识，可用隐式分隔符将他们分成组

注意，声明宏和过程宏都不支持文档注释 (e.g. /// Doc)。所以他们经常转换成#[doc = r"str"]属性来转换成token stream。
