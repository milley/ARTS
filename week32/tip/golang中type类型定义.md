# golang中type类型定义

在golang中，type的使用有两种方式：

- 类型定义

```golang
type Student struct {
    name string
    age int
}
```

- 类型等价定义

```golang
var cnt I = 123
fmt.Printf("%d", cnt)
```

- 类型别名

```golang
type strMap2Any = map[string]interface {}
```

相当于别名alias，这样在json操作中，使用别名就能让代码看起来更加简洁。
