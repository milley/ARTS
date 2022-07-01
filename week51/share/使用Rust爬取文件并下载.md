# 使用Rust爬取文件并下载

最近学习Rust，想用Rust实现下从网站爬取rust安装文件并后台下载保存到本地。

之前学习过reqwest crate，可以用来发送http GET/POST等请求来访问页面。访问到页面后可以使用scraper crate来爬取响应页面的内容。

## 1. 发送Http GET请求并解析

首先在Cargo.toml中引入两个crate:

```toml
[dependencies]
reqwest = { version = "0.11.11", features = ["blocking", "stream"] }
scraper = "0.13.0"
```

由于reqwest::Client是异步的，因此需要引入tokio来异步调用reqwest。

```toml
tokio = { version = "1.19.2", features = ["full"] }
```

第一步先调用reqwest::get来打开rust下载的页面，页面返回的响应保存到body中：

```rust
#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let body = reqwest::get("https://www.rust-lang.org/tools/install")
    .await?
    .text()
    .await?;

    // ...
	
	Ok(())
}
```

获取到响应后，需要使用scraper来爬取我们需要的下载地址。

```rust
let document = scraper::Html::parse_document(&body);
let main_div_selector = scraper::Selector::parse("div.ph2-ns").unwrap();
let main_div = document.select(&main_div_selector).next().unwrap();

let u_div_selector = scraper::Selector::parse("div.pa2>a").unwrap();
```

```html
<div class="cf ph2-ns">
    <div class="fl w-100 w-50-ns pa2">
    <a href="https://static.rust-lang.org/rustup/dist/i686-pc-windows-msvc/rustup-init.exe" class="button button-secondary">Download <span class="nowrap">rustup-init.exe</span> <span class="nowrap">(32-bit)</span></a>
    </div>
    <div class="fl w-100 w-50-ns pa2">
    <a href="https://static.rust-lang.org/rustup/dist/x86_64-pc-windows-msvc/rustup-init.exe" class="button button-secondary">Download <span class="nowrap">rustup-init.exe</span> <span class="nowrap">(64-bit)</span></a>
    </div>
</div>
```

其中main_div_selector定义了最外层的div，其class定义为：cf和ph2-ns，尝试了只包含其中一个样式就可以获取到这个div。然后通过调用select方法来后去到外层的div。u_div_selector是定义了内层的div，其class定义为class="fl w-100 w-50-ns pa2"，尝试了只需要指定pa2就可以获取到内部的div。然后再接着指定链接类型a。

获取到<a>标签类型后，需要从属性href中判断选择哪个exe文件，这里使用查找x86_64来指定本机的链接，然后需要下载链接即可：

```rust
for div in main_div.select(&u_div_selector) {
    let download_url = div.value().attr("href").unwrap();
    if download_url.find("x86_64") != None {
        println!("Downloading {}", download_url);

        // download file
        // ...
    }
}
```

## 2. 异步下载文件

要异步下载文件，需要指定下载链接和保存路径，还需要传入http客户端来用来下载。因此函数原型可以定义为：

```rust
pub async fn download_file(client: &Client, url: &str, path: &str) -> Result<(), String> {
    // ...
}
```

其中client可以使用reqwest来创建：

```rust
let client = reqwest::Client::new();
```

到了download_file内部，需要使用传入的client，调用get方法返回RequestBuilder，接着调用send方法发送请求到url，然后返回一个future响应。

```rust
let res = client
    .get(url)
    .send()
    .await
    .or(Err(format!("Failed to GET from '{}'", &url)))?;
```

紧接着计算返回的响应大小：

```rust
let total_size = res
    .content_length()
    .ok_or(format!("Failed to get content length from '{}'", &url))?;
```

下载过程中为了实时展示下载的进度，我们一般会使用进度条来展示。在命令行下可以使用indicatif trait来显示进度条相关功能，引入:

```toml
futures-util = "0.3.21"
indicatif = "0.16.2"
```

定义ProcessBar：

```rust
// Indicatif setup
let pb = ProgressBar::new(total_size);
pb.set_style(ProgressStyle::default_bar()
    .template("{msg}\n{spinner:.green} [{elapsed_precise}] [{wide_bar:.cyan/blue}] {bytes}/{total_bytes} ({bytes_per_sec}, {eta})")
    .progress_chars("#>-"));
pb.set_message(format!("Downloading {}", url));
```

然后定义文件块：

```rust
// download chunks
let mut file = File::create(path).or(Err(format!("Failed to create file '{}'", path)))?;
let mut downloaded: u64 = 0;
let mut stream = res.bytes_stream();
```

这里stream是从Response返回的字节流。通过循环来获取并写入到本地文件中：

```rust
while let Some(item) = stream.next().await {
    let chunk = item.or(Err(format!("Error while downloading file")))?;
    file.write_all(&chunk)
        .or(Err(format!("Error while writing to file")))?;
    let new = min(downloaded + (chunk.len() as u64), total_size);
    downloaded = new;
    pb.set_position(new);
}

pb.finish_with_message(format!("Downloaded {} to {}", url, path));
return Ok(());
```

## 3. 完整代码参考

```rust
use std::cmp::min;
use std::fs::File;
use std::io::Write;

use reqwest::Client;
use indicatif::{ProgressBar, ProgressStyle};
use futures_util::StreamExt;

pub async fn download_file(client: &Client, url: &str, path: &str) -> Result<(), String> {
    // Reqwest setup
    let res = client
        .get(url)
        .send()
        .await
        .or(Err(format!("Failed to GET from '{}'", &url)))?;
    let total_size = res
        .content_length()
        .ok_or(format!("Failed to get content length from '{}'", &url))?;
    
    // Indicatif setup
    let pb = ProgressBar::new(total_size);
    pb.set_style(ProgressStyle::default_bar()
        .template("{msg}\n{spinner:.green} [{elapsed_precise}] [{wide_bar:.cyan/blue}] {bytes}/{total_bytes} ({bytes_per_sec}, {eta})")
        .progress_chars("#>-"));
    pb.set_message(format!("Downloading {}", url));

    // download chunks
    let mut file = File::create(path).or(Err(format!("Failed to create file '{}'", path)))?;
    let mut downloaded: u64 = 0;
    let mut stream = res.bytes_stream();

    while let Some(item) = stream.next().await {
        let chunk = item.or(Err(format!("Error while downloading file")))?;
        file.write_all(&chunk)
            .or(Err(format!("Error while writing to file")))?;
        let new = min(downloaded + (chunk.len() as u64), total_size);
        downloaded = new;
        pb.set_position(new);
    }

    pb.finish_with_message(format!("Downloaded {} to {}", url, path));
    return Ok(());
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let body = reqwest::get("https://www.rust-lang.org/tools/install")
    .await?
    .text()
    .await?;

    let client = reqwest::Client::new();
    // println!("{:#?}", body);

    let document = scraper::Html::parse_document(&body);
    let main_div_selector = scraper::Selector::parse("div.ph2-ns").unwrap();
    let main_div = document.select(&main_div_selector).next().unwrap();

    let u_div_selector = scraper::Selector::parse("div.pa2>a").unwrap();
    
    for div in main_div.select(&u_div_selector) {
        let download_url = div.value().attr("href").unwrap();
        if download_url.find("x86_64") != None {
            println!("Downloading {}", download_url);
            download_file(&client, download_url, "rustup-init.exe").await.unwrap();
        }
    }
	
	Ok(())
}
```
