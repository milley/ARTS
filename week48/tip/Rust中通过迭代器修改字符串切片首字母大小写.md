# Rust中通过迭代器修改字符串切片大小写

如果需要将给定的一个字符串切片&str的首字母大写，用迭代器如何实现？

rustling练习中有个练习，给定了大致实现：

```rust
pub fn capitalize_first(input: &str) -> String {
    let mut c = input.chars();
    match c.next() {
        None => String::new(),
        Some(first) => ???
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    
    #[test]
    fn test_success() {
        assert_eq!(capitalize_first("hello"), "Hello");
    }
}
```

通过如上例子，可以看到首先将入参input调用chars方法，获取到了char类型的迭代器。然后通过match模式匹配，两种操作情况，None直接转换到空的字符串对象，如果非空那么就需要处理首字母到大写。

首先想到的是需要调用char类型下的to_uppercase方法，查看rust文档中的函数说明：

```rust
#[must_use = "this returns the uppercase character as a new iterator, \
                without modifying the original"]
#[stable(feature = "rust1", since = "1.0.0")]
#[inline]
pub fn to_uppercase(self) -> ToUppercase {
    ToUppercase(CaseMappingIter::new(conversions::to_upper(self)))
}
```

可以看到会返回一个ToUppercase的struct，这个也只是包括了首字母的结构，因此还需要处理剩余的字符。继续查找帮助文档，发现有一个chain的方法和我们要处理的情况类似，首先看函数签名:

```rust
#[stable(feature = "rust1", since = "1.0.0")]
fn chain<U>(self, other: U) -> Chain<Self, U::IntoIter>
where
    Self: Sized,
    U: IntoIterator<Item = Self::Item>,
{
    Chain::new(self, other.into_iter())
}
```

这个方法作用是将两个迭代器合并到一个新的迭代器，将传入的第二个迭代器内容会依次添加到原来的迭代器内容后生成新的迭代器。因为match那一行已经调用了c.next()函数，因此首个要处理的字符已经不存在于c迭代器中了。尝试改写调用:

```rust
pub fn capitalize_first(input: &str) -> String {
    let mut c = input.chars();
    match c.next() {
        None => String::new(),
        Some(first) => first.to_uppercase().chain(c).collect()
    }
}
```

完美通过测试。
