# PostgreSQL基本操作

## 1. 创建用户和数据库

```sql
-- 创建用户postgres和数据库mydb
sudo -u postgres psql postgres
sudo -u postgres createdb mydb
```

## 2. 登陆mydb数据库

```sql
psql -U postgres -d mydb -h 127.0.0.1 -p 5432
```

## 3. 创建表

```sql
CREATE TABLE user_tbl(name VARCHAR(20), signup_date DATE);
INSERT INTO user_tbl(name, signup_date) VALUES('张三', '2013-12-22');
SELECT * FROM user_tbl;
```

## 4. 常用控制台指令

- \h：查看SQL命令的解释，比如\h select。
- \?：查看psql命令列表。
- \l：列出所有数据库。
- \c [database_name]：连接其他数据库。
- \d：列出当前数据库的所有表格。
- \d [table_name]：列出某一张表格的结构。
- \du：列出所有用户。
- \e：打开文本编辑器。
- \conninfo：列出当前数据库和连接的信息。
