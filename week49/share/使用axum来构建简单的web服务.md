# 使用axum来构建简单的web服务

axum是一个Rust下开源的web应用框架，主要聚焦于易用性和模块化。

## 1. 高级别特性

- 使用宏API发送Route请求
- 使用提取器来声明式的解析请求
- 简单和可预见的错误模式处理
- 使用最小样板生成应答
- 使用tower的所有特性和tower-http的中间件、服务、公共的生态系统

特别是最后一点将axum与其他框架分开。axum没有自己的中间件系统，它使用了tower::Service。这意味着axum可以自由的获取超时、跟踪、压缩、鉴权等等。它也允许你使用hyper或者tonic来共享你的应用程序中间件。

## 2. 兼容性

axum被设计成可以和tokio、hyper兼容的工作。运行时和独立的传输层不是目的，至少目前是这样。

## 3. GET请求示例

由于axum基于tokio来工作，因此入口main函数必须使用#[tokio::main]，函数定义为async类型：

```rust
use axum::{
    routing::get,
    Router,
};

#[tokio::main]
async fn main() {
    let app = Router::new()
        .route("/", get(|| async { "Hello, World!" }))
        .route("/health", get(|| async { "Health: 100% OK!" }));

    axum::Server::bind(&"0.0.0.0:3001".parse().unwrap())
        .serve(app.into_make_service())
        .await
        .unwrap();
}
```

这样编译完成后启动服务，访问<http://localhost:3001/>会返回"Hello, World!"到页面。访问<http://localhost:3001/health>则会返回"Health: 100% OK!"到页面。

## 4. 解析url

使用RESTful风格的请求，需要有解析url的手段。axum可以使用extract::Path来配合serde处理url的解析，这里使用json来解析并返回一个json响应。

### 4.1 增加route

通过增加一个/users/:user_id/team/:team_id的请求，调用异步函数users_teams_show：

```rust
let app = Router::new()
        .route("/", get(|| async { "Hello, World!" }))
        .route("/health", get(|| async { "Health: 100% OK!" }))
        .route("/users/:user_id/team/:team_id", get(users_teams_show));
```

### 4.2 定义请求参数

通过定义一个请求的参数结构体，这里用Params来表示，其中包含user_id和team_id两个参数：

```rust
#[derive(Deserialize)]
struct Params {
    user_id: String,
    team_id: String,
}
```

### 4.3 实现异步函数

通过Path(Params { user_id, team_id })来接收请求参数的解析，可以直接将请求中的参数解析成变量。然后通过serde-json库来将这两个参数简单的序列化成json对象，并输出到前台页面。

```rust
async fn users_teams_show(
    Path(Params { user_id, team_id }): Path<Params>,
) -> Result<String, StatusCode> {
    let msg = format!("user_id: {}, team_id: {}", user_id, team_id);
    println!("{}", msg);

    let result = json!({
        "user_id": user_id,
        "team_id": team_id
    });
    let result = format!("{}", result);
    Ok(result)
}
```

以上就可以针对请求很方便的解析参数后返回到前台页面。
