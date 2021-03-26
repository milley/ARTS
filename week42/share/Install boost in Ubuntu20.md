# Install boost in Ubuntu20

## 1. 更新系统

- sudo apt-get update
- sudo apt-get upgrade

## 2. 安装boost依赖库

- sudo apt-get install libboost-all-dev

## 3. build boost库

- ./bootstrap.sh --prefix=/usr/
- ./b2
- sudo ./b2 install
