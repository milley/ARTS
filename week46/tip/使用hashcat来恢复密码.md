# 使用hashcat来恢复密码

## 压缩软件解密

需下载两个软件:

[john-1.9.0-jumbo](https://www.openwall.com/john/) 和 [hashcat](https://hashcat.net/hashcat/)

针对RAR压缩软件解密情况为例：

### 1. rar2john.exe获取哈希值

```cmd
PS F:\rar_test\john-1.9.0-jumbo-1-win64\run> .\rar2john.exe F:\rar_test\1.rar
! file name: 1.txt
1.rar:$RAR3$*1*be1b58bebb6eb39a*17484c18*32*8*1*5925ae418e77f7b4d9ae129bad3a603306d3a71a77ecbc315f06c7b8e75cc06b*33:1::1.txt
```

[对应码表](https://hashcat.net/wiki/doku.php?id=example_hashes)

### 2. hashcat计算密码

内置类型:

- ?l = abcdefghijklmnopqrstuvwxyz
- ?u = ABCDEFGHIJKLMNOPQRSTUVWXYZ
- ?d = 0123456789
- ?h = 0123456789abcdef
- ?H = 0123456789ABCDEF
- ?s = «space»!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
- ?a = ?l?u?d?s
- ?b = 0x00 - 0xff

```cmd
# output need.hash存入上面的hash值
.\hashcat.exe -m 12500 -a 3 .\need.hash ?a?a?a?a -O -w 4
```
