# 1,使用 nohup  来进行服务的启动就能保持后台长连接

```
命令   java  编译  服务jar包  这个jar包名后续还要用作关闭服务使用
nohup java -jar dowork-zuul-1.0.0-SNAPSHOT.jar &
```

# 2，关闭长连接

```
指令         服务名称
ps -ef|grep dowork-zuul-1.0.0-SNAPSHOT.jar
               这个就是id
root      7451  7419  0 15:54 pts/1    00:00:00 grep --color=auto dowork-zuul-1.0.0-SNAPSHOT.jar

使用 id 关闭服务
kill -s 9 7419
```

# 3，亲测可用

















# 																		-- by 杨