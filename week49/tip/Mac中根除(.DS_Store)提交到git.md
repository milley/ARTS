# Mac中根除(.DS_Store)提交到git

在Mac系统中，每个文件夹下都有.DS_Store文件来告诉Finder如何显示它。99%的情况我们的Git仓库不需要包含它，所以我们需要将其添加到我们的.gitignore文件中。

如何能设置全局的忽略文件以便将.DS_Store添加进去？

## core.excludesfile

git允许我们使用变量core.excludesfile来指定全局的忽略。

创建你自己的全局忽略文件，我自己创建的是.gitignore_global，然后将其作为.gitignore链接到我的home路径。

```bash
ln -s /Users/0xmachos/Documents/Projects/dotfiles/.gitignore_global /Users/0xmachos/.gitignore
```

## 告诉git

通过如下命令告诉git忽略全局文件：

```bash
git config --global core.excludesfile ~/.gitignore
```

或者将其添加到.gitconfig中：

```bash
excludesfile = ~/.gitignore
```
