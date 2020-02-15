# 编写一个C程序来判断底层结构是大端还是小端

[Write a C program to check if the underlying architecture is little endian or big endian](https://www.cs-fundamentals.com/tech-interview/c/c-program-to-check-little-and-big-endian-architecture.php)

在计算机内存中有大端字节序和小端字节序两种格式存储多字节数据类型。这两种格式也分别被称作网络字节序和主机字节序。在一个多字节类型中，例如int或者long或者其他多字节数据类型，右边的字节被称作最低有效字节，左边的字节被称作最高有效字节。在大端字节序首先保存的是最高有效字节，然后保存的是最低有效字节，在小端字节序则先存储最低有效字节。

作为一个例子，如果一个4字节的整形x包含一个16进制的值0x76543210('0x'代表16进制)，最低有效字节包含0x10而最高有效字节包含0x76。现在如果你使用一个char类型的指针c指向x的地址，将x转换为char指针，那么在小端字节序上你打印*c将会看到0x10，在大端字节序上你将得到0x76。从而你可以获取到机器的字节序。

```console
int x = 0x76543210;
char *c = (char*) &x;

Big endian format:
------------------
Byte address  | 0x01 | 0x02 | 0x03 | 0x04 |
              +++++++++++++++++++++++++++++
Byte content  | 0x76 | 0x54 | 0x32 | 0x10 |

Little endian format:
---------------------
Byte address  | 0x01 | 0x02 | 0x03 | 0x04 |
              +++++++++++++++++++++++++++++
Byte content  | 0x10 | 0x32 | 0x54 | 0x76 |
```

使用C程序可以检测大端和小端结果：

```c
#include <stdio.h>
int main() {
  unsigned int x = 0x76543210;
  char *c = (char*)&x;

  printf("*c is 0x%x\n", *c);
  if (*c == 0x10) {
    printf("Underlying architecture is little endian. \n");
  } else {
    printf("Underlying architecture is big endian. \n");
  }

  return 0;
}
```

正如前面所说，大端字节序也是网络字节序，小端字节序也称作主机字节序。有一套函数可以从16位和32位整数转换到网络字节序，反之亦然。htons(host-to-network-short)和htonl(host-to-network-long)函数分别将16位和32位整数转换到网络字节序，ntohs和ntohl将网络字节序转换成主机字节序。你可以从[这里](https://www.cs-fundamentals.com/c-programming/endianness-little-and-big-endian.php)获取更多的字节序和信息描述。

我们也可以通过编写一个小的函数来确定机器的底层结构是大端还是小端。函数check_for_endianness()返回1代表小端字节序，其他返回0。

```c
int check_for_endianness() {
  unsigned int x = 1;
  char *c = (char*) &x;
  return (int)*c;
}
```

希望你通过这些C代码能理解大端小端字节序。
