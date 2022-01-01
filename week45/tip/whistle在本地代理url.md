# whistle在本地代理url

在本地可以通过npm install -g whistle，安装完成后可以进行web的抓包和debug、代理等。

node版本如下:

```cmd
C:\Users\Administrator>node -v
v14.18.2

C:\Users\Administrator>npm -v
6.14.15
```

## 1. 启动、停止、重启、调试

- w2 start
- w2 stop
- w2 restart
- w2 debug

## 2. 配置代理

默认管理页面127.0.0.1:8899打开配置页面，新建Rules：

```cmd
127.0.0.1:9001 http://www.milley.org
```

这样就在使用了whistle代理的情况下将<http://www.milley.org>发送到<http://127.0.0.1:9001>地址上。
