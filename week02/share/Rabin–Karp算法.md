# 字符串匹配算法(II)

## 1. Rabin–Karp算法介绍

Rabin–Karp[^1]算法是在brute-force基础上改进而来，BF算法每次模式串只移动一位，如果主字符串是一千万个字符a组成，模式串是一万个字符a后面跟一个字符b，这样BF算法的最坏复杂度就是O(mn)。

RK算法使用了哈希算法，对主串中n-m+1个子串分别求哈希值，来和模式串的哈希值对比，如果哈希值相等，为了避免哈希碰撞，就需要把主字符串的子串和模式串本身对比是否相等。伪代码如下：

```code
function RabinKarp(string s[1..n], string pattern[1..m])
  hpattern := hash(pattern[1..m])
  for i from 1 to n-m+1
    hs := hash(s[i..i+m-1])
    if hs = hpattern
      if s[i..i+m-1] = pattern[1..m]
  return not found
```

## 2. 哈希算法

为了提高算法的效率，可以使用浮动哈希(rolling hash)，大体意思如下：

```code
s[i+1..i+m] = s[i..i+m-1]-s[i] + s[i+m]
```

在构建主字符串的时候，为了减少哈希的次数，可以采用Rabin fingerprint来实现。具体步骤如下：

```code
// 模式串hi,base为256,取余系数为101
[(104 % 101) x 256 + 105] % 101 = 65
(ASCII of 'h' is 104 and of 'i' is 105)
```

如果主字符串为"abracadabra",搜索的长度为3，第一个子串"abr"使用上面的哈希算法如下：

```code
// ASCII a = 97, b = 98, r = 114
hash("abr") =  [ ( [ ( [  (97 % 101) × 256   + 98 ] % 101 ) × 256 ] %  101 ) + 114 ]   % 101   =  4
```

通过"abr"来计算下一个子串"bra",减去第一个字符"a"($97 * 256^2$)，加上最后的字符"a"($97 * 256^0$)：

```code
// old hash -ve avoider old 'a' left base offset base shift new 'a' prime modulus
hash("bra") = [ ( 4 + 101 - 97 * [(256%101)*256] % 101 ) * 256 + 97 ] % 101 = 30
```

如果使用"abr"相同的方法来计算"bra"：

```code
hash'("bra") =  [ ( [ ( [ ( 98 %101) × 256 + 114] % 101 ) × 256 ] % 101) + 97 ] % 101 = 30
```

这样就产生了哈希碰撞，算法优化如下：

```code
function RabinKarpSet(string s[1..n], set of string subs, m):
    set hsubs := emptySet
    foreach sub in subs
        insert hash(sub[1..m]) into hsubs
    hs := hash(s[1..m])
    for i from 1 to n-m+1
        if hs ∈ hsubs and s[i..i+m-1] ∈ subs
            return i
        hs := hash(s[i+1..i+m])
    return not found
```

[^1]:(https://en.wikipedia.org/wiki/Rabin%E2%80%93Karp_algorithm)