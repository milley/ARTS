# Rust创建文件上传下载服务

最近学习微信小程序开发的过程中，有一个实战示例需要用到文件的上传和下载功能。其中小程序需要从服务端下载文件，使用微信的wx.downloadFile API。目前微信小程序基础版本2.21.2已经可以访问局域网中的服务端了，所以需要在非本机IP创建一个文件上传下载的服务端。

想着正好是练习Rust的好机会，这样就打算用Rust来写一个简单的demo服务。目前参考了[File upload and download in Rust](https://blog.logrocket.com/file-upload-and-download-in-rust/)来完成基本功能。

首先Rust我这边已经升级到1.57.0 2021版本，所以部分的crate版本有点差异。准备使用warp web framework来完成。

## 1. 安装

首先创建新的Rust工程。

```bash
cargo new rust-upload-download-files
cd rust-upload-download-files
```

在Cargo.toml中引入需要用到的crate:

```toml
[dependencies]
tokio = { version = "1.12", features = ["macros", "rt-multi-thread", "fs"]}
warp = "0.3"
uuid = { version = "0.8", features = ["v4"] }
bytes = "1.0"
futures-util = "0.3.19"
```

我们使用warp来创建web服务，使用tokio的异步功能。原有的文章使用了Uuid来创建上传文件的唯一名字，我们自己改造了直接使用原始的文件名来存储。

## 2. Web service

从创建基础的Warp web应用程序开始，让用户可以使用upload route从本地文件目录上传文件到服务器，也可以使用download route来下载服务端的文件：

```rust
#![allow(unused_imports)]
use std::convert::Infallible;
use futures_util::TryStreamExt;
use warp::{
    http::StatusCode,
    multipart::{FormData, Part},
    Rejection,
    Reply,
    Filter,
};
use uuid::Uuid;
use bytes::BufMut;

#[tokio::main]
async fn main() {
    let download_route = warp::path("files").and(warp::fs::dir("./files/"));
    let upload_route = warp::path("upload").and(warp::post())
        .and(warp::multipart::form().max_length(20_000_000))
        .and_then(upload);


    let router = download_route.or(upload_route).recover(handle_rejection);
    println!("Server started at localhost:9001");
    warp::serve(router).run(([0, 0, 0, 0], 9001)).await;
}

async fn handle_rejection(err: Rejection) -> std::result::Result<impl Reply, Infallible> {
    let (code, message) = if err.is_not_found() {
        (StatusCode::NOT_FOUND, "Not Found".to_string())
    } else if err.find::<warp::reject::PayloadTooLarge>().is_some() {
        (StatusCode::BAD_REQUEST, "Payload too large".to_string())
    } else {
        eprintln!("unhandled error: {:?}", err);
        (
            StatusCode::INTERNAL_SERVER_ERROR,
            "Internal Server Error".to_string()
        )
    };

    Ok(warp::reply::with_status(message, code))
}
```

## 3. upload文件

在main函数中使用了multipart::form()过滤器，通过multipart请求传递。这里定义了最大长度，注意到在handle_rejection可以显式的捕获PayloadTooLarge，当上传文件超过限额就回触发这个错误。

最后我们检查upload句柄。这是这个示例程序比较复杂的核心部分。

```rust
async fn upload(form: FormData) -> Result<impl Reply, Rejection> {
    let parts: Vec<Part> = form.try_collect().await.map_err(|e| {
        eprintln!("form error: {}", e);
        warp::reject::reject()
    })?;

    // --snip--
}
```

这里的入参FormData实际上是warp::filters::multiPart::FormData，是multiPart的元素Part的stream。当我们要使用futures::Stream，需要使用TryStreamExt这个trait。这里使用了try_collect函数来将整个stream异步的收集到集合中，如果失败则记录失败log。

下面的部分需要我们先理解一个发送到此服务的request:

```cmd
curl -H "Content-type: multipart/form-data" -F "file=@[file_path];type=application/pdf" -X POST "http://[ip:port]/upload"
```

可以看到-F选项将文件以application/pdf的格式POST到服务端。

下一步是通过遍历前面收集的集合，首先处理文件类型并且后缀是pdf或者png的类型：

```rust
async fn upload(form: FormData) -> Result<impl Reply, Rejection> {
    let parts: Vec<Part> = form.try_collect().await.map_err(|e| {
        eprintln!("form error: {}", e);
        warp::reject::reject()
    })?;

    for p in parts {
        if p.name() == "file" {
            let content_type = p.content_type();

            let file_ending;
            match content_type {
                Some(file_type) => match file_type {
                    "application/pdf" => {
                        file_ending = "pdf";
                    }
                    "image/png" => {
                        file_ending = "png";
                    }
                    v => {
                        eprintln!("invalid file type found: {}", v);
                        return Err(warp::reject::reject());
                    }
                },
                None => {
                    eprintln!("file type could not be determined");
                    return Err(warp::reject::reject());
                }
            }

            // --snip--
        }
    }

    Ok("Success")
}
```

处理完文件类型后，将文件全名拿到，下一步就将遍历的stream再合并到可变的切片中:

```rust
// --snip--
println!("file_ending: {}", file_ending);

let file_name = format!("./files/{}", p.filename().unwrap());

let value = p
    .stream()
    .try_fold(Vec::new(), |mut vec, data| {
        vec.put(data);
        async move { Ok(vec) }
    })
    .await
    .map_err(|e| {
        eprintln!("reading file error: {}", e);
        warp::reject::reject()
    })?;
// --snip--
```

最后通过tokio的异步方法写入到文件中:

```rust
// --snip--
tokio::fs::write(&file_name, value).await.map_err(|e| {
    eprintln!("error writing file: {}", e);
    warp::reject::reject()
})?;

println!("created file: {}", file_name);
// --snip--
```

最终就可以通过下面的url来GET到pdf的内容并打开了：

```url
http://[ip:port]/files/[file_name].pdf
```
