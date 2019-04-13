# 生成SQL中in语句

平常经常用到PL/SQL和Navicat,SQL中in语句也会经常使用，但是原来的插件在Windows10下不好用，资源占用率经常很高。最近看了下GoLang，撸了一个小程序在cmd下执行并生成in语句。

```Go
package main

import (
    "bufio"
    "fmt"
    "os"
)

func main() {
    for {
        var result []string

        input := bufio.NewScanner(os.Stdin)
        seen := make(map[string]string)

        for input.Scan() {
            line := input.Text()
            if line != "" {
                seen[line] = line
            }
        }

        if err := input.Err(); err != nil {
            fmt.Fprintf(os.Stderr, "indistinct: %v\n", err)
            os.Exit(1)
        }

        if seen != nil {
            result = append(result, "(")
            index := 0
            for _, v := range seen {
                index = index + 1
                result = append(result, "'")
                result = append(result, v)
                result = append(result, "'")
                if index != len(seen) {
                    result = append(result, ",")
                }
            }
            result = append(result, ")")
        }

        for _, v := range result {
            fmt.Printf("%s", v)
        }
        fmt.Println()
    }

}

```
