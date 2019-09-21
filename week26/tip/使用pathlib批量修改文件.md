# 使用pathlib批量修改文件

在上周的review中，由于medium中图片是用手机截图然后传到PC上，在整理图片需要传到github上，看了下手机截图的规则名称都是一样的前缀，只是文件名的时间不一样，因此就可以使用pathlib来批量重命名文件。

```python
from pathlib import Path

x = 0
for file in sorted(Path("E:/screenshot").iterdir()):
    x = x + 1
    print("E:/screenshot/" + "TreeSet_" + x.__str__() + ".png")
    Path.replace(file, "E:/screenshot/" + "TreeSet_" + x.__str__() + ".png")
```

这样就全部重命名成了markdown中需要的名字了！
