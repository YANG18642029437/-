# redis 客户端操作

## 1, 基本的数据操作

### 1.1  字符串操作 Strings

+ 基本字符串操作

  + set mystr "我"
  + get mystr

+ 数值自增 自减

  > set mynum "2"
  >
  > inct mynum 自增
  >
  > INCRBY mynum 4 增加指定的长度
  >
  > DECR mynum 自减1
  >
  > DECRBY mynum 4 减少指定的长度 
  >
  > 在某种场景下有3个客户端同时读取了mynum的值（值为2），然后对其同时进行了加1的操作，那么，最后mynum的值一定是5。不少网站都利用redis的这个特性来实现业务上的统计计数需求

### 1.2 列表 Lists 

+ 简介

  > 首先要明确一点，redis中的lists在底层实现上并不是数组，而是链表，也就是说对于一个具有上百万个元素的lists来说，在头部和尾部插入一个新元素，其时间复杂度是常数级别的，比如用LPUSH在10个元素的lists头部插入新元素，和在上千万元素的lists头部插入新元素的速度应该是相同的。
  >
  > 虽然lists有这样的优势，但同样有其弊端，那就是，链表型lists的元素定位会比较慢，而数组型lists的元素定位就会快得多。
  >
  > lists的常用操作包括LPUSH、RPUSH、LRANGE等。我们可以用LPUSH在lists的左侧插入一个新元素，用RPUSH在lists的右侧插入一个新元素，用LRANGE命令从lists中指定一个范围来提取元素

+ 基本操作

  > //新建一个list叫做mylist，并在列表头部插入元素"1"
  > 127.0.0.1:6379> lpush mylist "1" 
  > //返回当前mylist中的元素个数
  > (integer) 1 
  > //在mylist右侧插入元素"2"
  > 127.0.0.1:6379> rpush mylist "2" 
  > (integer) 2
  > //在mylist左侧插入元素"0"
  > 127.0.0.1:6379> lpush mylist "0" 
  > (integer) 3
  > //列出mylist中从编号0到编号1的元素
  > 127.0.0.1:6379> lrange mylist 0 1 
  > 1) "0"
  > 2) "1"
  > //列出mylist中从编号0到倒数第一个元素
  > 127.0.0.1:6379> lrange mylist 0 -1 
  > 1) "0"
  > 2) "1"
  > 3) "2"

+ 使用场景

  > 1.我们可以利用lists来实现一个消息队列，而且可以确保先后顺序，不必像MySQL那样还需要通过ORDER BY来进行排序。
  > 2.利用LRANGE还可以很方便的实现分页的功能。
  > 3.在博客系统中，每片博文的评论也可以存入一个单独的list中。

### 1.3 集合 set

+ 概述

  > redis的集合，是一种无序的集合，集合中的元素没有先后顺序。
  >
  > 集合相关的操作也很丰富，如添加新元素、删除已有元素、取交集、取并集、取差集等。

+ 基本操作

  > //向集合myset中加入一个新元素"one"
  > 127.0.0.1:6379> sadd myset "one" 
  > (integer) 1
  > 127.0.0.1:6379> sadd myset "two"
  > (integer) 1
  > //列出集合myset中的所有元素
  > 127.0.0.1:6379> smembers myset 
  > 1) "one"
  > 2) "two"
  > //判断元素1是否在集合myset中，返回1表示存在
  > 127.0.0.1:6379> sismember myset "one" 
  > (integer) 1
  > //判断元素3是否在集合myset中，返回0表示不存在
  > 127.0.0.1:6379> sismember myset "three" 
  > (integer) 0
  > //新建一个新的集合yourset
  > 127.0.0.1:6379> sadd yourset "1" 
  > (integer) 1
  > 127.0.0.1:6379> sadd yourset "2"
  > (integer) 1
  > 127.0.0.1:6379> smembers yourset
  > 1) "1"
  > 2) "2"
  > //对两个集合求并集
  > 127.0.0.1:6379> sunion myset yourset 
  > 1) "1"
  > 2) "one"
  > 3) "2"
  > 4) "two"

+ 使用方式

  > 对于集合的使用，也有一些常见的方式，比如，QQ有一个社交功能叫做“好友标签”，大家可以给你的好友贴标签，比如“大美女”、“土豪”、“欧巴”等等，这时就可以使用redis的集合来实现，把每一个用户的标签都存储在一个集合之中

+ 有序集合 sorted sets

  + 使用方式

    > 127.0.0.1:6379> zadd myzset 1 baidu.com 
    > (integer) 1
    > //向myzset中新增一个元素360.com，赋予它的序号是3
    > 127.0.0.1:6379> zadd myzset 3 360.com 
    > (integer) 1
    > //向myzset中新增一个元素google.com，赋予它的序号是2
    > 127.0.0.1:6379> zadd myzset 2 google.com 
    > (integer) 1
    > //列出myzset的所有元素，同时列出其序号，可以看出myzset已经是有序的了。
    > 127.0.0.1:6379> zrange myzset 0 -1 with scores 
    > 1) "baidu.com"
    > 2) "1"
    > 3) "google.com"
    > 4) "2"
    > 5) "360.com"
    > 6) "3"
    > //只列出myzset的元素
    > 127.0.0.1:6379> zrange myzset 0 -1 
    > 1) "baidu.com"
    > 2) "google.com"
    > 3) "360.com"

## 1.4 Hashes 哈希

+ 概述

  >  hashes存的是字符串和字符串值之间的映射，比如一个用户要存储其全名、姓氏、年龄等等，就很适合使用哈希。 

+ 基础使用

  >  //建立哈希，并赋值
  > 127.0.0.1:6379> HMSET user:001 username antirez password P1pp0 age 34 
  > OK
  > //列出哈希的内容
  > 127.0.0.1:6379> HGETALL user:001 
  > 1) "username"
  > 2) "antirez"
  > 3) "password"
  > 4) "P1pp0"
  > 5) "age"
  > 6) "34"
  > //更改哈希中的某一个值
  > 127.0.0.1:6379> HSET user:001 password 12345 
  > (integer) 0
  > //再次列出哈希的内容
  > 127.0.0.1:6379> HGETALL user:001 
  > 1) "username"
  > 2) "antirez"
  > 3) "password"
  > 4) "12345"
  > 5) "age"
  > 6) "34" 

# 2. redis 安装

## 2.1 linux 安装

### 2.1.1 安装

> **1,安装gcc**
>
> gcc的安装很简单，首先要确保root登录，其次就是Linux要能连外网
>
>  yum -y install gcc automake autoconf libtool make 
>
>  2,运行yum时出现/var/run/yum.pid已被锁定,PID为xxxx的另一个程序正在运行的问题解决 
>
>  rm -f /var/run/yum.pid 
>
> 3,安装
>
>  wget http://download.redis.io/releases/redis-4.0.1.tar.gz  下载指定版本
>
>  **tar zxvf redis-4.0.1.tar.gz**  解压
>
>  **cd redis-4.0.1**  进入目录
>
>  make   或 **make MALLOC=libc**    编译

<img src="D:\temp\xmind\sql\redis\images\image-20191117085108228.png" alt="image-20191117085108228" style="zoom:67%;" />

+ 安装编译后的文件   PREFIX必须大写、同时会自动为我们创建redis目录，并将结果安装此目录 
+  **make PREFIX=/usr/local/redis install** 

<img src="D:\temp\xmind\sql\redis\images\image-20191117085537629.png" alt="image-20191117085537629" style="zoom: 67%;" />

+ 查看 bin 下的目录结构  cd /usr/local/redis/bin

![image-20191117090123655](D:\temp\xmind\sql\redis\images\image-20191117090123655.png)

+ 启动  **./bin/redis-server** 

> 启动服务端 
>
> redis-server
>
> **启动Redis 客户端命令：**
>
>    **redis-cli –h IP地址 –p 端口**
>
> **退出客户端命令：Ctrl+C**  

### 2.1.2 配置

+ 复制解压后的配置文件

>  **cp redis.conf /usr/local/redis**  

+ 详细配置 详解

> **redis.conf 配置文件详解**
>
> redis.conf 配置项说明如下：
>
> **1. Redis默认不是以守护进程的方式运行，可以通过该配置项修改，使用yes启用守护进程**
>
> ​    **daemonize no**
>
> \2. 当Redis以守护进程方式运行时，Redis默认会把pid写入/var/run/redis.pid文件，可以通过pidfile指定
>
> ​    **pidfile /var/run/redis.pid**
>
> \3. **指定Redis监听端口，默认端口为6379，**为什么选用6379作为默认端口，因为6379在手机按键上MERZ对应的号码，而MERZ取自意大利歌女Alessia Merz的名字
>
> ​    **port 6379**
>
> **4. 绑定的主机地址**
>
> ​    **bind 127.0.0.1**
>
> 5.当 客户端闲置多长时间后关闭连接，如果指定为0，表示关闭该功能
>
> ​    **timeout 300**
>
> \6. 指定日志记录级别，Redis总共支持四个级别：debug、verbose、notice、warning，默认为verbose
>
> ​    **loglevel verbose**
>
> \7. 日志记录方式，默认为标准输出，如果配置Redis为守护进程方式运行，而这里又配置为日志记录方式为标准输出，则日志将会发送给/dev/null
>
> ​    **logfile stdout**
>
> \8. 设置数据库的数量，默认数据库为0，可以使用SELECT <dbid>命令在连接上指定数据库id
>
> ​    **databases 16**
>
> \9. **指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合**
>
> ​    **save  **
>
> ​    **Redis默认配置文件中提供了三个条件：**
>
> ​    **save 900 1**
>
> ​    **save 300 10**
>
> ​    **save 60 10000**
>
> ​    分别表示900秒（15分钟）内有1个更改，300秒（5分钟）内有10个更改以及60秒内有10000个更改。
>
>  
>
> \10. 指定存储至本地数据库时是否压缩数据，默认为yes，Redis采用LZF压缩，如果为了节省CPU时间，可以关闭该选项，但会导致数据库文件变的巨大
>
> ​    **rdbcompression yes**
>
> \11. **指定本地数据库文件名，默认值为dump.rdb**
>
> ​    **dbfilename dump.rdb**
>
> \12. 指定本地数据库存放目录
>
> ​    **dir ./**
>
> \13. 设置当本机为slav服务时，设置master服务的IP地址及端口，在Redis启动时，它会自动从master进行数据同步
>
> ​    **slaveof  **
>
> \14. 当master服务设置了密码保护时，slav服务连接master的密码
>
> ​    **masterauth **
>
> **15. 设置Redis连接密码，如果配置了连接密码，客户端在连接Redis时需要通过AUTH 命令提供密码，默认关闭**
>
> ​    **requirepass foobared**
>
> \16. 设置同一时间最大客户端连接数，默认无限制，Redis可以同时打开的客户端连接数为Redis进程可以打开的最大文件描述符数，如果设置 maxclients 0，表示不作限制。当客户端连接数到达限制时，Redis会关闭新的连接并向客户端返回max number of clients reached错误信息
>
> ​    **maxclients 128**
>
> \17. 指定Redis最大内存限制，Redis在启动时会把数据加载到内存中，达到最大内存后，Redis会先尝试清除已到期或即将到期的Key，当此方法处理 后，仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。Redis新的vm机制，会把Key存放内存，Value会存放在swap区
>
> ​    **maxmemory **
>
> \18. 指定是否在每次更新操作后进行日志记录，Redis在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为 redis本身同步数据文件是按上面save条件来同步的，所以有的数据会在一段时间内只存在于内存中。默认为no
>
> ​    **appendonly no**
>
> \19. 指定更新日志文件名，默认为appendonly.aof
>
> ​     **appendfilename appendonly.aof**
>
> \20. 指定更新日志条件，共有3个可选值：     **n**o：表示等操作系统进行数据缓存同步到磁盘（快）     **alwa**ys：表示每次更新操作后手动调用fsync()将数据写到磁盘（慢，安全）     **every**sec：表示每秒同步一次（折中，默认值）
>
> ​    **appendfsync everysec**
>
>  
>
> \21. 指定是否启用虚拟内存机制，默认值为no，简单的介绍一下，VM机制将数据分页存放，由Redis将访问量较少的页即冷数据swap到磁盘上，访问多的页面由磁盘自动换出到内存中（在后面的文章我会仔细分析Redis的VM机制）
>
> ​     **vm-enabled no**
>
> \22. 虚拟内存文件路径，默认值为/tmp/redis.swap，不可多个Redis实例共享
>
> ​     **vm-swap-file /tmp/redis.swap**
>
> \23. 将所有大于vm-max-memory的数据存入虚拟内存,无论vm-max-memory设置多小,所有索引数据都是内存存储的(Redis的索引数据 就是keys),也就是说,当vm-max-memory设置为0的时候,其实是所有value都存在于磁盘。默认值为0
>
> ​     **vm-max-memory 0**
>
> \24. Redis swap文件分成了很多的page，一个对象可以保存在多个page上面，但一个page上不能被多个对象共享，vm-page-size是要根据存储的 数据大小来设定的，作者建议如果存储很多小对象，page大小最好设置为32或者64bytes；如果存储很大大对象，则可以使用更大的page，如果不 确定，就使用默认值
>
> ​     **vm-page-size 32**
>
> \25. 设置swap文件中的page数量，由于页表（一种表示页面空闲或使用的bitmap）是在放在内存中的，，在磁盘上每8个pages将消耗1byte的内存。
>
> ​     **vm-pages 134217728**
>
> \26. 设置访问swap文件的线程数,最好不要超过机器的核数,如果设置为0,那么所有对swap文件的操作都是串行的，可能会造成比较长时间的延迟。默认值为4
>
> ​     **vm-max-threads 4**
>
> \27. 设置在向客户端应答时，是否把较小的包合并为一个包发送，默认为开启
>
> ​    **glueoutputbuf yes**
>
> \28. 指定在超过一定的数量或者最大的元素超过某一临界值时，采用一种特殊的哈希算法
>
> ​    **hash-max-zipmap-entries 64**
>
> ​    **hash-max-zipmap-value 512**
>
> \29. 指定是否激活重置哈希，默认为开启（后面在介绍Redis的哈希算法时具体介绍）
>
> ​    **activerehashing yes**
>
> \30. 指定包含其它的配置文件，可以在同一主机上多个Redis实例之间使用同一份配置文件，而同时各个实例又拥有自己的特定配置文件
>
> ​    **include /path/to/local.conf**
>
> **Redis中的内存维护策略**
>
> redis作为优秀的中间缓存件，时常会存储大量的数据，即使采取了集群部署来动态扩容，也应该即使的整理内存，维持系统性能。
>
> **在redis中有两种解决方案**，
>
> **一是为数据设置超时时间**，
>
> **二是采用LRU算法动态将不用的数据删除**。内存管理的一种页面置换算法，对于在内存中但又不用的数据块（内存块）叫做LRU，操作系统会根据哪些数据属于LRU而将其移出内存而腾出空间来加载另外的数据。
>
>  1.volatile-lru：设定超时时间的数据中,删除最不常使用的数据.
>
> **2.allkeys-lru：查询所有的key中最近最不常使用的数据进行删除，这是应用最广泛的策略.**
>
> 3.volatile-random：在已经设定了超时的数据中随机删除.
>
> **4.allkeys-random：查询所有的key,之后随机删除**.
>
> 5.volatile-ttl：查询全部设定超时时间的数据,之后排序,将马上将要过期的数据进行删除操作.
>
> 6.noeviction：如果设置为该属性,则不会进行删除操作,如果内存溢出则报错返回.
>
> - volatile-lfu：从所有配置了过期时间的键中驱逐使用频率最少的键
> - allkeys-lfu：从所有键中驱逐使用频率最少的键

+ 简易配置

  > **1、进入对应的安装目录** **/usr/local/redis**
  >
  > 
  >
  > **2、Redis配置默认必须修改：** 
  >
  > 
  >
  > **daemonize no  修改为 daemonize yes** 
  >
  > 
  >
  > **bind 127.0.01  注释掉**               
  >
  > 
  >
  > **requirepass 设置密码** 
  >
  >  Redis采用的是单进程多线程的模式。当redis.conf中选项daemonize设置成yes时，代表开启守护进程模式。在该模式下，redis会在后台运行，并将进程pid号写入至redis.conf选项pidfile设置的文件中，此时redis将一直运行，除非手动kill该进程。但当daemonize选项设置成no时，当前界面将进入redis的命令行界面，exit强制退出或者关闭连接工具(putty,xshell等)都会导致redis进程退出。 **服务端开发的大部分应用都是采用后台运行的模式**  

+ 再次启动

  > **服务端启动：**
  >
  > **./bin/redis-server ./redis.conf**
  >
  > **客户端启动****：**
  >
  > **本地客户端登录**
  >
  > **用redis-cli 密码登陆（redis-cli -a  password）** 
  >
  > **远程服务上执行命令**
  >
  > 如果需要在远程 redis 服务上执行命令，同样我们使用的也是 **redis-cli** 命令。
  >
  > 语法：
  >
  > redis-cli -h host -p port -a password
  >
  > **redis-cli –h IP地址 –p 端口 –a 密码**
  >
  > **Redis关闭**
  >
  > **第一种关闭方式：（断电、非正常关闭。容易数据丢失）**
  >
  > **查询PID   ps -ef | grep -i redis**
  >
  > **kill -9 PID**
  >
  > **第二种关闭方式（正常关闭、数据保存）**
  >
  > **./bin/redis-cli shutdown**     关闭redis服务，通过客户端进行shutdown
  >
  > 如果redis设置了密码，需要先在客户端通过密码登录，再进行shutdown即可关闭服务端
  >
  > **通过 ps –ef | grep –i redis  查看当前进程：**

## 100 小问题解决

### 100.1 客户端中文乱码 

> 启动客户端的时候使用 redis-cli --raw 就能解决