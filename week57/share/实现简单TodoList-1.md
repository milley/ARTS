# 实现简单TodoList-1

## 1. 创建MYSQL数据库和表结构

创建数据库todo，然后通过如下命令创建两张表：

```mysql
CREATE TABLE todo_list(
    id int NOT NULL AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE todo_item(
    id int NOT NULL AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    checked BOOLEAN NOT NULL DEFAULT FALSE,
    list_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (list_id) REFERENCES todo_list(id)
);

INSERT INTO todo_list (title) VALUES ('清单1'), ('清单2');

INSERT INTO todo_item (title, list_id) VALUES
    ('清单项目 1', 1),
    ('清单项目 2', 1),
    ('清单项目 A', 2);

-- select * from todo_list t;
-- select * from todo_item t;
```

## 2. 创建todo后端项目

### 2.1 定义restful接口协议

请求方式 | 路由 | 说明
-------|------|--------
GET | /todos | 获取所有TodoList
POST | /todo | 为指定的TodoList添加Item
GET | /todo/:list_id | 获取TodoList详情
DELETE | /todo/:list_id | 删除指定的TodoList及其Item
PUT | /todo/:list_id | 修改TodoList
GET | /todo/:list_id/items | 获取TodoList关联的Item详情
PUT | /todo/:list_id/items/:item_id | 修改TodoList关联的Item详情
DELETE | /todo/:list_id/items/:item_id | 删除TodoList关联的Item详情

### 2.2 创建todo项目

执行cargo new todo-server创建后端项目工程，数据库操作是必须的。当前比较主流的sql工具可以使用sqlx，它是一个异步的纯rust实现的SQL crate，具有编译时检查查询且不需要依赖DSL。

首先需要引入sqlx，注意需要打开mysql和tls特性，简单练习下数据库的基本操作:

```toml
[dependencies]
sqlx = { version = "0.6.3", features = ["mysql", "runtime-tokio-rustls"] }
tokio = { version = "1.26.0", features = ["full"] }
```

### 2.3 创建数据库example

```rust
use sqlx::mysql::MySqlPoolOptions;

#[allow(dead_code)]
#[derive(Debug, sqlx::FromRow)]
struct Todo {
    id: i64,
    title: String,
}

#[tokio::main]
async fn main() -> Result<(), sqlx::Error> {
    let pool = MySqlPoolOptions::new()
        .max_connections(5)
        .connect("mysql://root:123456@localhost/todo")
        .await?;
    let row: (i64,) = sqlx::query_as("SELECT ?")
        .bind(150_i64)
        .fetch_one(&pool)
        .await?;

    assert_eq!(row.0, 150);

    let todos = sqlx::query_as::<_, Todo>("SELECT id, title FROM todo_list")
        .fetch_all(&pool)
        .await?;

    assert_eq!(todos.len(), 2);
    for todo in todos.iter() {
        println!("{:#?}", todo);
    }

    Ok(())
}
```

打开数据库，执行了两条SQL语句：

```sql
select 150;
select id, title from todo_list;
```

分别输出了两条语句的结果，和MySQL数据库中完全符合。
