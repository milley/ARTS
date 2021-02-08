# ubuntu使用阿里云upgrade失败

执行sudo apt upgrade失败： Temporary failure resolving 'mirrors.aliyun.com'

编辑resolv.conf配置：

```cmd
milley@milley-VirtualBox:~$ sudo vi /etc/resolv.conf
```

增加两行：

```cmd
nameserver 8.8.8.8
nameserver 8.8.4.4
```

重启网卡：

```cmd
milley@milley-VirtualBox:~$ sudo /etc/init.d/network-manager restart
Restarting network-manager (via systemctl): network-manager.service.
```

重新执行sudo apt upgrade，成功。
