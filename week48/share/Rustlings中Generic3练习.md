# Rustlings中Generic3练习.md

rustingls中关于泛型的练习3，题目[代码](https://play.rust-lang.org/?version=stable&mode=debug&edition=2021&gist=de82d8b59353541d37edd9d690d11ce5)：

```rust
pub struct ReportCard {
    pub grade: f32,
    pub student_name: String,
    pub student_age: u8,
}

impl ReportCard {
    pub fn print(&self) -> String {
        format!("{} ({}) - achieved a grade of {}",
            &self.student_name, &self.student_age, &self.grade)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn generate_numeric_report_card() {
        let report_card = ReportCard {
            grade: 2.1,
            student_name: "Tom Wriggle".to_string(),
            student_age: 12,
        };
        assert_eq!(
            report_card.print(),
            "Tom Wriggle (12) - achieved a grade of 2.1"
        );
    }

    #[test]
    fn generate_alphabetic_report_card() {
        // TODO: Make sure to change the grade here after you finish the exercise.
        let report_card = ReportCard {
            grade: 2.1,
            student_name: "Gary Plotter".to_string(),
            student_age: 11,
        };
        assert_eq!(
            report_card.print(),
            "Gary Plotter (11) - achieved a grade of A+"
        );
    }
}
```

运行单元测试，会在generate_alphabetic_report_card出现panic。

```bash
   Compiling playground v0.0.1 (/playground)
    Finished test [unoptimized + debuginfo] target(s) in 1.33s
     Running unittests (target/debug/deps/playground-0f8b1386ecd10d2b)
error: test failed, to rerun pass '--lib'
Standard Output

running 2 tests
test tests::generate_numeric_report_card ... ok
test tests::generate_alphabetic_report_card ... FAILED

failures:

---- tests::generate_alphabetic_report_card stdout ----
thread 'tests::generate_alphabetic_report_card' panicked at 'assertion failed: `(left == right)`
  left: `"Gary Plotter (11) - achieved a grade of 2.1"`,
 right: `"Gary Plotter (11) - achieved a grade of A+"`', src/lib.rs:39:9
note: run with `RUST_BACKTRACE=1` environment variable to display a backtrace


failures:
    tests::generate_alphabetic_report_card

test result: FAILED. 1 passed; 1 failed; 0 ignored; 0 measured; 0 filtered out; finished in 0.00s

```

测试不通过原因主要是结构体中的grade目前定义为f32，而失败的单元测试传入的是字符串。所以需要通过泛型让grade可以是浮点数或者字符串。

首先修改struct定义:

```rust
pub struct ReportCard<T> {
    pub grade: T,
    pub student_name: String,
    pub student_age: u8,
}
```

在结构体定义修改为泛型后，那么实现它的方法print也需要支持泛型参数。

```rust
impl<T> ReportCard<T> {
    pub fn print(&self) -> String {
        format!("{} ({}) - achieved a grade of {}",
            &self.student_name, &self.student_age, &self.grade)
    }
}
```

如果直接将实现方法变为泛型T类型，那么这时还会有错误输出提示&self.grade没有实现std::fmt::Display::

```bash
   Compiling playground v0.0.1 (/playground)
error[E0277]: `T` doesn't implement `std::fmt::Display`
  --> src/lib.rs:10:52
   |
10 |             &self.student_name, &self.student_age, &self.grade)
   |                                                    ^^^^^^^^^^^ `T` cannot be formatted with the default formatter
   |
   = note: in format strings you may be able to use `{:?}` (or {:#?} for pretty-print) instead
   = note: this error originates in the macro `$crate::__export::format_args` (in Nightly builds, run with -Z macro-backtrace for more info)
help: consider restricting type parameter `T`
   |
7  | impl<T: std::fmt::Display> ReportCard<T> {
   |       +++++++++++++++++++

For more information about this error, try `rustc --explain E0277`.
error: could not compile `playground` due to previous error
warning: build failed, waiting for other jobs to finish...
error: build failed
```

因此为了能支持Display trait，我们需要指定参数T是Display的子trait:

```rust
impl<T: std::fmt::Display> ReportCard<T> {
    pub fn print(&self) -> String {
        format!("{} ({}) - achieved a grade of {}",
            &self.student_name, &self.student_age, &self.grade)
    }
}
```

通过这样的修改，编译报错都消除了，但是单元测试还是没通过，因为我们还没有修改generate_alphabetic_report_card中grade的值，修改为字符串"A+"后解决。最终[代码](https://play.rust-lang.org/?version=stable&mode=debug&edition=2021&gist=0a1247789be2d863c2cdfc5fd7384080)如下:

```rust
pub struct ReportCard<T> {
    pub grade: T,
    pub student_name: String,
    pub student_age: u8,
}

impl<T: std::fmt::Display> ReportCard<T> {
    pub fn print(&self) -> String {
        format!("{} ({}) - achieved a grade of {}",
            &self.student_name, &self.student_age, &self.grade)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn generate_numeric_report_card() {
        let report_card = ReportCard {
            grade: 2.1,
            student_name: "Tom Wriggle".to_string(),
            student_age: 12,
        };
        assert_eq!(
            report_card.print(),
            "Tom Wriggle (12) - achieved a grade of 2.1"
        );
    }

    #[test]
    fn generate_alphabetic_report_card() {
        // TODO: Make sure to change the grade here after you finish the exercise.
        let report_card = ReportCard {
            grade: "A+",
            student_name: "Gary Plotter".to_string(),
            student_age: 11,
        };
        assert_eq!(
            report_card.print(),
            "Gary Plotter (11) - achieved a grade of A+"
        );
    }
}
```
