# tengine安装以及开机自动启动脚本

安装前准备(centos)

```shell
yum ``install` `gcc gcc-c++ openssl openssl-devel pcre pcre-devel
```



tengine官方网站 http://tengine.taobao.org/

```shell
wget http://tengine.taobao.org/download/tengine-2.0.3.tar.gz
tar -zxvf tengine-2.0.3.tar.gz
cd tengine-2.0.3
./configure
make && make install
 
# 开机自启配置文件
vim /lib/systemd/system/nginx.service
#加入如下内容
```

```shell
[Unit]
Description=The nginx HTTP and reverse proxy server
After=syslog.target network.target remote-fs.target nss-lookup.target

[Service]
Type=forking
PIDFile=/usr/local/nginx/logs/nginx.pid
ExecStartPre=/usr/local/nginx/sbin/nginx -t
ExecStart=/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf
ExecReload=/bin/kill -s HUP $MAINPID
ExecStop=/bin/kill -s QUIT $MAINPID
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

```shell
chmod +x /etc/init.d/nginx
chkconfig nginx on
service nginx start
```

配置nginx反代

```nginx
server
        {
        listen      80;
        server_name  www.domain.net;
     
        location / {
                proxy_pass        http://www.domain.com;
                proxy_set_header  Host            $host;
                proxy_set_header  X-Real-IP        $remote_addr;
                proxy_set_header  X-Forwarded-For  $proxy_add_x_forwarded_for;
        }
 
      access_log  off;
  }
```

## 查看端口号

> lsof -i:端口号