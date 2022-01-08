# Rust中错误类型Result

在做rustlings中关于错误处理的第一节时，直接就被卡住了。

贴下[代码](https://play.rust-lang.org/?version=stable&mode=debug&edition=2021&gist=e6c35ec8d0eb68f9de02d467e3ad5ed4)

```rust
pub fn generate_nametag_text(name: String) -> Option<String> {
    if name.len() > 0 {
        Some(format!("Hi! My name is {}", name))
    } else {
        // Empty names aren't allowed.
        None
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    // This test passes initially if you comment out the 2nd test.
    // You'll need to update what this test expects when you change
    // the function under test!
    #[test]
    fn generates_nametag_text_for_a_nonempty_name() {
        assert_eq!(
            generate_nametag_text("Beyoncé".into()),
            Some("Hi! My name is Beyoncé".into())
        );
    }

    #[test]
    fn explains_why_generating_nametag_text_fails() {
        assert_eq!(
            generate_nametag_text("".into()),
            Err("`name` was empty; it must be nonempty.".into())
        );
    }
}
```

错误提示如下：

```bash
   Compiling playground v0.0.1 (/playground)
error[E0308]: mismatched types
  --> src/lib.rs:27:9
   |
27 | /         assert_eq!(
28 | |             generate_nametag_text("".into()),
29 | |             Err("`name` was empty; it must be nonempty.".into())
30 | |         );
   | |_________^ expected enum `Option`, found enum `Result`
   |
   = note: expected enum `Option<String>`
              found enum `Result<_, _>`
   = note: this error originates in the macro `assert_eq` (in Nightly builds, run with -Z macro-backtrace for more info)

For more information about this error, try `rustc --explain E0308`.
error: could not compile `playground` due to previous error
```

错误主要是因为对于传入参数是空的情况下，应该返回一个Err类型，但是目前接收的是Option类型，因此匹配不上单元测试失败。

根据Rust的枚举定义:

```rust
pub enum Result<T, E> {
    Ok(T),
    Err(E),
}
```

这里参数代表成功返回Ok，失败返回Err。所以需要将generate_nametag_text函数中的出参修改为Result<String, String>类型，Ok情况也要响应的修改，Err情况添加单元测试中explains_why_generating_nametag_text_fails的期望出错。

修改后[代码](https://play.rust-lang.org/?version=stable&mode=debug&edition=2021&gist=bea6947eb3a1764fe0388a95015fbf0c)

```rust
pub fn generate_nametag_text(name: String) -> Result<String, String> {
    if name.len() > 0 {
        Ok(format!("Hi! My name is {}", name))
    } else {
        // Empty names aren't allowed.
        Err("`name` was empty; it must be nonempty.".to_string())
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    // This test passes initially if you comment out the 2nd test.
    // You'll need to update what this test expects when you change
    // the function under test!
    #[test]
    fn generates_nametag_text_for_a_nonempty_name() {
        assert_eq!(
            generate_nametag_text("Beyoncé".into()),
            Ok("Hi! My name is Beyoncé".into())
        );
    }

    #[test]
    fn explains_why_generating_nametag_text_fails() {
        assert_eq!(
            generate_nametag_text("".into()),
            Err("`name` was empty; it must be nonempty.".into())
        );
    }
}
```

这样就能通过单元测试了。
