# windows重命名dll后重新生成lib

近期在windows下需要修改dll的名称后重新加载，发现直接通过手动修改dll和lib是无法成功加载起来。在linux环境下试了下，可以直接调用成功。通过查找相关资料[how-do-i-rename-a-dll-but-still-allow-the-exe-to-find-it](https://stackoverflow.com/questions/280485/how-do-i-rename-a-dll-but-still-allow-the-exe-to-find-it)，是需要使用LIB工具重新生成下lib文件才可以正常加载。但是需要手工组装def文件，颇麻烦。无意中在github找到了现成的python脚本可以完成以上功能。

## 1. git clone rename_dll

```git
git clone https://github.com/cmberryau/rename_dll/
```

## 2. 安装所需依赖库

```python
pip install --upgrade pip
pip3 install argparse
```

中间会网络断开，重新修改了国内的Pip源：

```python
# C:\Users\Administrator\pip\目录新建pip.ini
[global]
index-url = http://mirrors.aliyun.com/pypi/simple/
[install]
trusted-host = mirrors.aliyun.com
```

## 3. 通过Visual Studio命令行执行

```python
# 如果需要转换为X86则需要修改：
# process = subprocess.Popen(['lib', '/MACHINE:X86', '/DEF:' + deffile_name], )
python rename_dll.py MyAdd.dll MyAdd_r.dll
```

转换完成后重新调用，OK。

## 4. 附录[CPP文件]

### 4.1. MyAdd工程生成MyAdd.dll

```cpp
// MyAdd.h
#pragma once

extern "C" __declspec(dllexport) int my_add(int a, int b);
```

```cpp
// MyAdd.cpp
#include "pch.h"
#include "Myadd.h"

int my_add(int a, int b)
{
	return a + b;
}
```

### 4.2. AddResult工程调用MyAdd.dll

```main.cpp

#include <iostream>
#include "Myadd.h"

int main()
{
    std::cout << my_add(3, 5) << std::endl;
    std::cout << "Hello World!\n";
}
```
