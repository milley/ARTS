# Rust通过DevSidecar请求github

## 1. 安装DevSidecar来访问github

通过[dev-sidecar](https://gitee.com/docmirror/dev-sidecar)下载ubuntu下的安装包，安装后可以确保Firefox可以连接github。

## 2. 转换CA证书

因为Rust给出的证书例子是DER类型证书，因此需要先将ubuntu下生成默认的CRT证书转换到DER证书。

参考链接: [DER、CRT、CER、PEM格式的证书转换](https://blog.csdn.net/xiangguiwang/article/details/76400805)

```bash
openssl x509 -outform der -in dev-sidecar.ca.crt -out dev-sidecar.ca.der
```

## 3. 修改Rust代码

主要涉及修改有2点：

1. 导入证书并添加到根目录
2. 配置本地代理(dev-sidecar使用默认的127.0.0.1:1181)

```rust
use anyhow::Result;
use polars::prelude::*;
use std::io::{Cursor, Read};
use std::fs::File;

#[tokio::main]
async fn main() -> Result<()> {
    tracing_subscriber::fmt::init();

    let mut buf = Vec::new();
    File::open("/home/milley/.dev-sidecar/dev-sidecar.ca.der")?.read_to_end(&mut buf)?;
    let cert = reqwest::Certificate::from_der(&buf)?;
    let client = reqwest::Client::builder()
        .proxy(reqwest::Proxy::all("http://127.0.0.1:1181")?)
        .add_root_certificate(cert)
        .build()?;

    let url = "https://raw.githubusercontent.com/owid/covid-19-data/master/public/data/latest/owid-covid-latest.csv";
    //let data = reqwest::get(url).await?.text().await?;
    let data = client.get(url).send().await?.text().await?;

    // 使用 polars 直接请求
    let df = CsvReader::new(Cursor::new(data))
        .infer_schema(Some(16))
        .finish()?;
    let filtered =  df.filter(&df["new_deaths"].gt(500))?;
    println!(
        "{:?}",
        filtered.select((
            "location",
            "total_cases",
            "new_cases",
            "total_deaths",
            "new_deaths"
        ))
    );

    Ok(())
}
```

## 4. 总结

通过找到dev-sidecar代理工具，配置完ubuntu后保证浏览器可以访问到github，然后修改rust的代码，添加代理和根目录证书，使得reqwest也可以方便的访问到github的csv数据。
