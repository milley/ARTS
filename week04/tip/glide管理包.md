# glide管理go包

## 1. glide初始化

```bash
glade init
```

## 2. 更新依赖包

```bash
glide up
```

通过glide.yaml查找依赖并下载到vendor文件夹中。Glide会创建一个glide.lock文件，这个文件包含整个的依赖树。

## 3. 安装依赖

```bash
glide install
```

- 如果glide.lock文件已存在，如果没有vendor/文件夹，依赖就会按glide.lock文件来设置。依赖和版本就会并发执行，因此速度非常快。
- 如果glide.lock不存在，upate就会被执行。

## 4. 增加额外的依赖

例如：

```bash
glide get github.com/Masterminds/semver
```