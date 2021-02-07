# virtualbox中NAT模式下Windows10访问ubuntu

默认的NAT网络模式下，宿主Windows10和虚拟机ubuntu不在一个网段，因此从windows无法访问到ubuntu。通过配置全局网络可以解决。

## 1. NAT方式尝试

## 1.1. 新建全局网络配置

VirtualBox->配置->全局设定->网络，新建一个名为NatNetwork的网络配置，CIDE配置为：192.168.1.0/24，开启DHCP

## 1.2. 虚拟机的网络配置中选择上一步新建的配置

界面名称选择以上新建的NatNetwork。

## 1.3. 查看虚拟机中的ip

虚拟机中使用ifconfig查看当前ip，route -n查看路由信息。

## 1.4. 宿主机中配置虚拟机的路由表

在Host-Only网卡中，配置虚拟机的ip: 192.168.1.4，网关和子网掩码使用默认配置。

以上配置完成后可以通过宿主windows主机ping通虚拟机ubuntu。

## 1.5. ubuntu中开启SSH服务

虚拟机中输入：service sshd status，发现当前没有安装sshd服务。

安装命令： sudu apt install openssh-server。安装完成后检查是否开启：

```cmd
milley@milley-VirtualBox:~$ sudo systemctl status ssh
● ssh.service - OpenBSD Secure Shell server
     Loaded: loaded (/lib/systemd/system/ssh.service; enabled; vendor preset: enabled)
     Active: active (running) since Sun 2021-02-07 20:58:56 CST; 2min 54s ago
       Docs: man:sshd(8)
             man:sshd_config(5)
   Main PID: 2584 (sshd)
      Tasks: 1 (limit: 7253)
     Memory: 1.2M
     CGroup: /system.slice/ssh.service
             └─2584 sshd: /usr/sbin/sshd -D [listener] 0 of 10-100 startups

2月 07 20:58:56 milley-VirtualBox systemd[1]: Starting OpenBSD Secure Shell server...
2月 07 20:58:56 milley-VirtualBox sshd[2584]: Server listening on 0.0.0.0 port 22.
2月 07 20:58:56 milley-VirtualBox sshd[2584]: Server listening on :: port 22.
2月 07 20:58:56 milley-VirtualBox systemd[1]: Started OpenBSD Secure Shell server.
```

一番操作后，发现在宿主windows下还是无法使用putty连接到ubuntu。算了，还是老老实实配置bridge方式吧。

## 2. bridge方式配置

1. 虚拟机设置->网络->网卡1->连接方式： 桥接网卡，选择本机连接外网的网卡。
2. 进入虚拟机ubuntu中，手动配置ip和宿主主机在一个网段，配置完成后重启。
3. 验证宿主主机和虚拟主机能互相ping通。

以上都成功后，使用putty配置虚拟机ip，成功登陆。
