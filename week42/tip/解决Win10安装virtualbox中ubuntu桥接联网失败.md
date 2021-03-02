# 解决Win10安装virtualbox中ubuntu桥接联网失败

[参考链接](https://liushiming.cn/article/virtualbox-vm-bridge-network-connect-internet-solution.html)

步骤：

- 添加环回虚拟网卡，输入hdwwiz，添加网络适配器，选择Microsoft KM-Test环回适配器
- 设置网络共享
- 虚拟机网络设置桥接网卡为环回虚拟网卡
- 重启后虚拟机自动获取IP，可以正常上网后手动配置IP，可以从宿主主机直接连接上来
