#!/bin/bash
## 自动更新服务器上的jar包的脚本
## 需要安装 expect：brew install expect
## 执行前请提升权限： chmod 777 upload.sh

screenname=personfilter # 如果部署服务，防止服务关闭，则使用screen
user=root  # 服务器用户名
address=server2 # 服务器地址
password=Li197910? # 个人密码
args='' # 启动jar包时的参数
jarpath=/root/web  # 服务器 jar包位置
jarname=personfilter-0.0.1-SNAPSHOT.jar # jar包名称
buildpath=target # 本地存放打包文件的路径

# 生成jar包
mvn clean install

# 切换到jar所在目录
cd $buildpath

# 上传到服务器并运行
/usr/bin/expect << EOF
set timeout 60

spawn scp $jarname $user@$address:$jarpath
expect {
    "*password:" { send "$password\r" }
    "yes/no" { send "yes\r";exp_continue }
}
expect eof

spawn ssh $user@$address
expect {
    "*password:" { send "$password\r" }
    "yes/no" { send "yes\r";exp_continue }
}
expect $user@*  {send "screen -S $screenname -X quit\r"}
expect $user@*  {send "screen -S $screenname\r"}
expect $user@*  {send "sleep 5s\r"}
expect $user@*  {send "cd $jarpath\r"}
expect $user@*  {send "java -jar $jarname $args\r"}
expect eof
EOF
