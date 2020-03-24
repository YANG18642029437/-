# 1，Netty 简述

## 1.1 Netty 介绍

+ Netty 是由JBOSS提供的一个Java开源框架，现为Github上的独立项目
+ Netty是一个异步的，基于事件驱动的网络应用框架，用以快速开发高性能，高可靠性的网络IO程序
  + 异步表示浏览器不需要等待服务器完成响应才能干别的事情，就是页面的线程不会被阻塞
+ Netty主要针对在TCP协议下，面向Clients端的高并发应用，或者peer-to-Peer 场景下的大量数据持续传输的应用
  + 结构层次
    + TCP/IP
    + JDK 原生/网络开发
    + NIO 网络开发
    + Netty 
+ Netty本质是一个NIO框架，使用于服务器通讯相关的多种应用场景
+ 要透彻理解Netty，需要先学习NIO。 

## 1.2 Netty 应用场景

> 互联网行业
>
> 1）互联网行业： 在分布式系统中，各个节点之间需要远程服务调用，高性能的RPC框架必不可少，Netty作为异步高性能的通讯框架，往往作为基础通讯组件被这些RPC框架使用
>
> RPC 就是 远程过程调用
>
> 2）典型的应用有：阿里分布式服务框架Dubbo的 RPC 框架使用 Duboo 协议进行节点间通讯，Dubbo 协议默认使用 Netty 作为基础通讯组件，用于实现各进程节点之间的内部通信
>
> 游戏行业
>
> 1） 无论手机服务端还是大型的网络游戏，都是使用Netty作为协议
>
> 2）Netty 作为高性能的基础通讯组件，提供 TCP/UDP 和 HTTP 协议栈，方便定制和开发私有协议栈，账号登录服务器
>
> 3）地图服务器之间可以方便通过 Netty 进行高性能的通讯
>
> 大数据领域
>
> 1）经典的Hadoop 的高性能通信和序列化组件（AVRO 实现数据文件共享） 的RPC框架，默认采用Netty进行跨界点通讯
>
> 2）他的Netty Service 基于 Netty 框架二次封装实现

### 1.3 Netty 的学习参考书籍

+ Netty In Action
+ Netty 权威指南

## 1.4 为什么要学习 Netty

### 1.4.1 原生 NIO 存在的问题

+ NIO 的类库和 API 繁杂，适用麻烦：需要熟练掌握 Selector，ServerSocketChannel，SocketChannel，ByteBuffer 等
+ 需要具备其他的额外技能：要熟悉 Java 多线程编程，因为 NIO 编程 涉及到 Reactor 模式，你必须对多线程和网络编程非常熟悉，才能编写出高质量的 NIO 程序
+ 开发工作量和难度都非常大：例如客户端面临断连重连，网络闪断，半包读写，失败缓存，网络拥塞和异常流处理等等。
+ JDK NIO 的 Bug：例如 臭名昭著的 Epoll Bug 它会导致 Selector 空轮询，最终导致 CPU 100%。直到 JDK 1.7 版本该问题仍旧存在，没有被根本解决。

### 1.4.2 Netty 官方说明

+ Netty 是由 JBoss 提供的一个 Java 开源框架。 Netty 提供异步的，基于事件驱动的网络应用程序框架，用于快速开发高性能，高可靠性的网络 IO 程序
+ Netty 可以帮助你快速，简单的开发出一个网络应用，相当于简化和流程化了 NIO 的开发过程
+ Netty 是目前最流行的 NIO 框架， Netty 在互联网领域，大数据分布式计算领域，游戏行业，通讯行业等获得了广泛的应用，知名的 Elasticsearch，Dubbo 框架内部都采用了 netty

### 1.4.3 Netty 的优点

+ 设计优雅： 使用于各种传输类型的统一 API 阻塞和非阻塞 Socket；基于灵活且可扩展的事件模型，可以清晰地分离关注点；高度可定制的线程模型 - 单线程，一个或多个线程池
+ 使用方便：想想记录的 Javadoc，用户指南和示例；没有其他依赖项， JDK 5（Netty 3.x） 或者 6 （netty4.x）就足够了
+ 高性能，吞吐量更高：延迟更低；减少资源消耗；最小化不必要的内存复制
+ 安全：完整的 SSL/TLS 和 StartTLS 支持
+ 社区活跃，不断更新：社区活跃，版本迭代周期短，发现 Bug 可以被及时修复，同时，更多的新功能会被加入

# 2， netty 的线程

## 2.1 线程模型的基本介绍

+ 不同的线程模式，对程序的性能有很大影响，为了搞清楚 Netty 线程模式，我们来系统的讲解下各个线程模式，最后看看 Netty 线程模型有什么优越性
+ 目前存在的线程模型有：
  + 传统阻塞 I/O 服务模型
  + Reactor 模式 （反应器模式 ）
+ 根据 Reactor 的数量和处理资源池的数量不同，有3种典型的实现
  + 单Reactor 单线程
  + 单Reactor 多线程
  + 主从Reactor 多线程
+ Netty 线程模式 （Netty 主要基于 主从 Reactor 多线程模型做了一定的改进，其中主从 Reactor 多线程模型有多个 Reactor）

## 2.2 线程模型 原理

### 2.2.1 传统阻塞 I/O 服务模型

![image-20191222100313630](D:\temp\xmind\netty 学习\images\image-20191222100313630.png)

+ 工作原理
  + 黄色框表示对象，蓝色框表示线程，白色的框表示方法
+ 模型特点
  + 采用阻塞IO模式获取输入的数据
  + 每个连接都需要独立的线程完成数据的输入，业务处理，数据返回
+ 问题分析
  + 当并发数很大，就会创建大量的线程，占用很大系统资源
  + 连接创建后，如果当前线程暂时没有数据可读，会阻塞在read操作，造成线程资源浪费

### 2.2.1 Reactor 模式

针对传统阻塞I/O服务模型的2个缺点

+ 基于 I/O 模型：多个连接共用一个阻塞对象，应用程序只需要在一个阻塞对象等待，无需阻塞等待所有连接。当某个连接有新的数据可以处理时，操作系统通知应用程序，线程从阻塞状态返回，开始进行业务处理
+ Reactor 对用的叫法： 1.反应器模式，2：分发者模式 3：通知者模式
+ 基于线程池复用线程资源：不必再为每个连接创建线程，将连接完成后的业务处理任务分配给线程进行处理，一个线程可以处理多个连接的业务

![image-20191222104527513](D:\temp\xmind\netty 学习\images\image-20191222104527513.png)

+ I/O 复用连接池

  + Reactor模式，通过一个或多个输入同时传递给服务处理器的模式（基于事件驱动）

  + 服务器端的程序，处理传入的多个请求，并将它们同步分派到相应的处理线程，因此Reactor模式也叫Dispatcher模式

  + Reactor 模式使用 IO 复用监听事件 收到事件后进行分发给某个进程（进程）这点就是网络服务器高并发处理关键

# 3，Reactor 模式

## 3.1 Reactor 模式分类

根据 Reactor 的梳理和处理资源线程池的数量不同，有3中典型的实现

+ 单 Reactor 单线程
+ 单 Reactor 多线程
+ 主从 Reactor 多线程

## 3.2 单 Reactor 单线程

![image-20191222110011618](D:\temp\xmind\netty 学习\images\image-20191222110011618.png)

### 3.2.1 实现方式

+ Select 是前面 I/O 复用模型介绍的标准网络编程 API，可以实现应用程序通过一个阻塞对象监听多路连接请求

+ Reactor 对象通过 Select 监控客户端请求事件，收到事件后通过 Dispatch 进行分发

+ 如果是建立连接请求事件，则由 Acceptor 通过 Accept 处理连接请求，然后创建一个 Handler 对象处理连接完成后的后续业务处理

+ 如果不是建立连接事件，则 Reactor 会分发调用连接对应的 Handler 来响应

+ Handler 会完成 Read→业务处理→Send 的完整业务流程

> 服务器端用一个线程通过多路复用搞定所有的 IO 操作（包括连接，读、写等），编码简单，清晰明了，但是如果客户端连接数量较多，将无法支撑  

### 3.2.2 优缺点分析

+ 优点 ：  模型简单，没有多线程、进程通信、竞争的问题，全部都在一个线程中完成  
+ 缺点：性能问题，只有一个线程，无法完全发挥多核 CPU 的性能。Handler 在处理某个连接上的业务时，整个进程无法处理其他连接事件，很容易导致性能瓶颈  
+ 缺点：可靠性问题，线程意外终止，或者进入死循环，会导致整个系统通信模块不可用，不能接收和处理外部消息，造成节点故障  
+ 使用场景：客户端的数量有限，业务处理非常快速，比如 Redis在业务处理的时间复杂度 O(1) 的情况  

## 3.3 单 Reactor 多线程

### 3.3.1 工作原理图

![image-20191222111909435](D:\temp\xmind\netty 学习\images\image-20191222111909435.png)

+ 方案说明
  + Reactor 对象 通过 select 监控客户端请求事件，收到事件后，通过 Dispathch 进行分发
  + 如果是建立连接请求，则右 Acceptor 通过 accept 处理连接请求，然后创建一个 Handler 对象处理完成连接后的各种事件
  + 如果不是连接请求，则由reactor 分发调用连接对应的 handler 来处理
  + handler 只负责响应事件，不做具体的业务处理，通过 Read 读取数据后，会分发给 后面的 Worker 线程池的某个线程处理业务
  + worker 线程池会分配独立线程完成真正的业务，并将结果返回给 Handler
  + handler 收到响应后，通过send将结果返回给 client

### 3.3.2 单 Reactor 多线程 优缺点分析

+ 优点：可以充分利用多核 cpu 的处理能力
+ 缺点：多线程会数据共享，访问比较复杂 Reactor 处理了所有的事件的监听和响应，在单线程运行，在高并发场景下容易出现性能瓶颈

## 3.4 主从 Reactor 多线程

### 3.4.1 主从Reactor多线程 原理示意图

![image-20191222113545131](D:\temp\xmind\netty 学习\images\image-20191222113545131.png)

+ 概述
  + 针对单 reactor 多线程模型中，Reactor 在单线程中运行，高并发场景下容易成为性能瓶颈，可以让 Reactor在多线程中运行
+ 方案说明
  + Reactor 主线程 MainReactor 对象通过 select 监听连接事件，收到事件后，通过 Acceptor 处理连接事件
  + 当 Acceptor 处理连接事件后，MainReactor 将连接分配给 SubReactor
  + subReactor 将连接加入到 连接队列进行监听，并创建 handler 进行各种事件处理
  + 当有新事件发生时， subreactor 就会调用对应的 handler 处理
  + handler 通过 read 读取数据 ，分发给后面的 worker 线程处理
  + worker 线程池会分配独立的 worker 线程进行业务处理，并返回结果
  + handler 收到响应的结果后，再通过 send 将结果返回 给 client 
  + reactor 主线程可以对应多个 reactor 子线程，即 MainRecator 可以关联多个 SubReactor 

### 3.4.1 优缺点说明：

+ 优点： 父线程与子线程的数据交互简单职责明确，父线程只需要需要接收新连接，子线程完成后续的业务处理。
+ 优点：父线程与子线程的数据交互简单，Reactor 主线程只需要 把新连接传给子线程，子线程无需返回数据
+ 缺点：编程复杂度较高

> 综合实例： 这种模型在许多项目中广泛使用，包括 Nginx 主从 Reactor 多进程模型， Memcached 主从多线程， Netty 主从多线程模型的支持

# 4，Netty 模型

## 4.1 Netty 模型简介

### 4.1.1 原理示意图简单版

![image-20191222145211779](D:\temp\xmind\netty 学习\images\image-20191222145211779.png)



+ BossGroup 线程维护 Selector，只关注 Accecpt
+ 当接收到 Accept 事件，获取到对应的 SocketChannel ， 封装成 NIOScoketChannel 并主从到 Worker 线程 （事件循环），并进行维护
+ 当 Worker 线程 监听到 Selector 中的通道 发生感兴趣的事件后，就进行处理（就由 Handler） ，注意Handler 已经加入到通道了 

### 4.1.2 原理示意图-进阶版

+ 概述

  > Netty 主要基于 主从 Reactors 多线程模型 做了一定的改进，其中主从 Reactor 多线程模型有多个 Reactor    

![image-20191222145712525](D:\temp\xmind\netty 学习\images\image-20191222145712525.png) 

### 4.1.3 原理示意图 详细版

![image-20191222153214481](D:\temp\xmind\netty 学习\images\image-20191222153214481.png)

+ Netty抽象出两组线程池 BossGroup 专门负责 接收 客户端的连接，WorkerGroup 专门负责网络的读写
+ BossGroup 和 WorkerGroup 类型都是 NioEventLoopGroup 
+ NioEventLoopGroup 相当于一个事件循环组，这个组中含有多个事件循环，每个事件循环是 NioEventLoop
+ NioEventLoop 表示一个不断循环的执行处理任务的线程，每个 NioEventLoop 都有一个 Selector ，用户监听绑定在其上的 socket 的网络通讯
+ NioEventLoopGroup 可以有多个线程，即可以含有多个 NioEventLoop
+ 每个 BossNioEventLoop 执行的步骤有3步
  + 轮询 accept 事件
  + 处理 accept 事件 ， 与 client 建立连接，生成 NioSocketChannel，并将其注册到 某个 worker NIOEventLoop 上的 Selector （监视器上）
  + 处理任务队列的任务，即runAllTasks
+ Worker NIOEventLoop 循环执行的步骤
  + 轮询read，write事件
  + 处理IO事件，即read/write 事件，在对应的NioSocketChannel 处理
  + 处理任务队列的任务，即 runAllTasks
+ 每个WorkerNIOEventLoop处理业务时，会使用 pipeline（管道），pipeline中包含了 channel，即通过pipeline 可以获取到对应通道，管道中维护了很多的处理器 

## 4.2  Netty 实战 1

### 4.2.1 导入 maven 坐标

```xml
<!-- 导入maven的坐标 -->
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.20.Final</version>
</dependency>
```

### 4.2.2 Netty 服务器端编写

+ 处理器 Handler

```java
package com.yyx.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 说明
 * 1，我们自定义一个 handler 需要继续 netty 规定好的某个 handlerAdapter
 * 2，这时我们自定义的 Handler ， 才能称之为一个 Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据事件的处理方法
     * 这里可以读取客户端发送的消息
     * @param ctx 上下文对象： 含有（管道 pipeLine ， 通道 Channel，地址）
     * @param msg 客户端发送的数据： 默认是 Object 类型
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Server ctx = " + ctx);
        // 将 msg 转换为一个 byteBuf
        // byteBuf 是 Netty 提供的，不是 NIO 提供的 ByteBuffer （效率更高）
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送消息是：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+ ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕后执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写回到 channel 中
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,客户端", CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 出现异常时要执行的方法
     * 将通道进行关闭
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
//        ctx.close();
    }
}

```

+ 服务器端 main 

```java
package com.yyx.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty 的服务器端方法
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        // 创建 BoosGroup 和 workerGroup
        /**
         * 说明
         * 1，创建了两个线程组 BoosGroup 和 workerGroup
         * 2，BoosGroup 处理连接请求
         * 3，WorkerGroup 真正的和客户端业务处理，会交给 workerGroup
         * 4，两个都是无限循环
         */
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 使用链式编程来进行设置
            bootstrap.group(boosGroup,workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用 NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128) // 设置线程队列得到连接个数
                    .childHandler(new ChannelInitializer<SocketChannel>(){ // 创建一个通道测试对象（匿名对象）
                        // 给 Pipeline （管道） 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 设置处理器处理 读写 操作
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });// 给 workerGroup 的 EventLoop 对应的管道设置处理器

            System.out.println("服务器端 is ready ...");

            // 绑定一个端口并且同步，生成了一个 ChannelFuture 对象
            // 启动服务器（并绑定端口）
            ChannelFuture cf = bootstrap.bind(6668).sync();

            // 对通道的关闭进行监听
            cf.channel().closeFuture().sync();
        }finally {
            // 优雅的关闭资源
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

```

### 4.2.3 Netty 客户端编写

+ handler 编写

```java
package com.yyx.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * netty 客户端的处理器类
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪时会触发本方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client" + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hell 福福 (✪ω✪)", CharsetUtil.UTF_8));
    }

    /**
     * 当通道有读取事件时，会触发
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址： " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

```

+ client  main 编写

~~~ java
package com.yyx.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty 客户端
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        // 客户端需要一个事件循环组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            // 创建一个启动助手对象
            // 注意客户端使用的不是 ServerBootstrap 而是 Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors) // 设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类 （反射）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 加入自己的处理器
                         * @param ch
                         * @throws Exception
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("客户端 is ok");

            //启动客户端 去连接服务器端
            // sync 方法的调用会使本方法会不堵塞在这里
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            // 关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}

~~~

### 4.2.4 源码解析

+ 创建 new NioEventLoopGroup() 的时候 线程池内默认又几个线程

  > 为你当前电脑的 核心数 * 2
  >
  > ```java
  > NettyRuntime.availableProcessors() // 获取当前电脑的核心数量
  > ```

+ 如果 NioEventLoopGroup（） 线程池中有多个线程 selector 对象 就会采用轮询的形式，将连接 SockerChannel 注册到 不同的 线程中

+ Channel 和 ChannelPipeline 有什么区别

  + channelPipeline 底层是一个双向链表的形式，出栈进栈
  + ChannelPipeline 和 Channe 是一个相互包含的关系，你中有我我中有你

## 4.3 Netty 任务队列

###  4.3.1 任务队列中的Task 有三种经典使用场景

+ 用户程序自定义的普通任务
+ 用户自定义定时任务
+ 非当前 Reactor 线程调用 Channel 的各种方法

> 再推送系统的业务线程里面，根据用户的标识，找到对应的 Channel 引用，然后 调用 Write 类方法向该用户推送消息，就会进入到这种 场景。最终的 Write 会提交到 任务队列中后被异步消费

### 4.3.2 用户程序自定义普通任务

```java
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据事件的处理方法
     * 这里可以读取客户端发送的消息
     *
     * @param ctx 上下文对象： 含有（管道 pipeLine ， 通道 Channel，地址）
     * @param msg 客户端发送的数据： 默认是 Object 类型
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 模型有长时间操作的任务


        // 事件循环对象
        EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    ByteBuf byteBuf = Unpooled.copiedBuffer("hello,客户端1", CharsetUtil.UTF_8);
                    ctx.writeAndFlush(byteBuf);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("go to .....");
    }
}
```

+ 注意事项
  + 当一个队列中有多个需要等待的执行线程，后续的执行会等待上一个完全执行完毕之后才会执行

### 4.3.3 用户自定义定时任务

```java
EventLoop eventLoop = ctx.channel().eventLoop();
eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    ByteBuf byteBuf = Unpooled.copiedBuffer("hello,客户端1", CharsetUtil.UTF_8);
                    ctx.writeAndFlush(byteBuf);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },5,TimeUnit.SECONDS);
```



### 4.3.4 非当前线程 Reactor 线程调用 Channel 的各种方法

+ 这是一种理念，在不同的 Channel 线程里调用 其他 Channel 的实例对象
+ 使用 一个集合 对其进行统一管理，只要能获取到这个集合就能获取到其中的所有 channel 对象，但是要注意 并发安全问题

### 4.3.5 方案再说明

+ Netty抽象出两组线程池，BoosGroup专门负责接收客户端连接，WorkerGroup 专门负责网络读写操作

+ NioEventLoop 表示一个不断循环执行处理任务的线程，每个 NioEventLoop 都有一个 selector ，用于监听绑定在其上的 socket 网络通道。

+ NioEventLoop 内部采用串行化设计，从消息的读取-> 解码 -> 处理 -> 编码 -> 发送 始终有 IO 线程 NioEventLoop 负责

+ 包含结构

  + NioEventLoopGroup 下包含多个 NioEventLoop
  + 每个 NioEventLoop 中包含有一个 Selector ， 一个 taskQueue
  + 每个 NioEventLoop 的 Selector 上可以注册监听多个 NioChannel
  + 每个 NioChannel 只会绑定在唯一 的 NioEventLoop上
  + 每个 NioChannel 都绑定有一个自己的 ChannelPipeline

## 4.4 异步模型

### 4.4.1 异步模型基本介绍

+ 异步的概念和同步相对。当一个异步过程调用发出后，调用者不能立刻得到结果。实际处理这个调用的组件在完成后，通过状态，通知和回调来通知调用者
+ Netty 中 I/O 操作是异步的，包括 Bind，Write，Connect 等操作会简单的返回一个 ChannelFuture
+ 调用者并不能立刻获得结果，而是通过 Future-Listenet 机制，用户可以方便的主动获取或者通过通知机制获得 IO 操作结果
+ Netty的异步模型是建立在 future 和 callback 的之上的。callback 就是回调。重点说 Future，它的核心思想是：假设一个方法 fun，计算过程可能非常 耗时，等待 fun 返回显然不合适。那么可以再调用 fun 的时候，立马返回一个 future，后续 可以通过 future 去监控方法 fun 的处理过程（即： future-Listener 机制）

### 4.4.2 Future 说明

+ 表示异步的执行结果，可以通过它提供的方法来检测执行是否完成，比如 检索计算等等
+ ChannelFuture 是一个接口 继承与 Future 接口
  + 我们可以添加监听器，当监听的事件发生时 ，就会通知到监听器

### 4.4.3 异步工作原理图

![image-20191223162234857](D:\temp\xmind\netty 学习\images\image-20191223162234857.png)

+ 说明
  + 在使用 Netty 进行编程时,拦截操作和转换出入站数据只需要您提供 callback 或利用 future 即可，这使得链式操作简单，高效，并有利于编写出可重用的，通用的代码。
  + Netty 框架的目标就是让你的业务逻辑从网络基础应用编码中分离出来，解脱出来

### 4.4.4 Future-Listener机制

+ 当 Future 对象刚刚创建时，处于非完成状态，调用者可以通过返回的 ChannelFuture 来获取操作执行的状态，注册监听函数来执行完成后的操作
+ 常见操作如下
  + 通过 isDone 方法来判断当前操作是否完成
  + 通过 isSuccess 方法来判断已完成的当前操作是否完成
  + 通过 getCause 方法来获取已完成的当前操作的失败原因
  + 通过 isCancelled 方法来判断已完成的当前操作是否被取消
  + 通过 addListener 方法来注册监听器，当操作已完成（isDone 方法返回完成），将会通知指定的监听器；如果 Future 对象已完成，则通知指定的监听器

# 5，HTTP 服务

## 5.1 HTTP服务实战

+ Server 服务端

~~~java
package com.yyx.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
    public static void main(String[] args) throws Exception{
        // 创建两个 处理事件组 一个处理请求事件，一个处理 读写事件
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建服务器端启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).childHandler(new TestServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

~~~

+ ServerInitializer 管道处理器

```java
package com.yyx.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer  extends ChannelInitializer<SocketChannel> {
    // 向管道添加处理器
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 向管道添加处理器

        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();
        // 加入一个 netty 提供的 HttpServerCodec codec [coder - decoder] 的缩写
        /**
         * httpServerCode 说明
         * 1，httpServerCodec 是Netty提供的处理 Http的
         * 2，添加一个自定义的 handler
         */
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        // 用来处理 读取信息
        pipeline.addLast("TestHttpServerHandler",new TestHttpServerHandler());
    }
}

```

+ 配置处理 读写请求的处理器

~~~java
package com.yyx.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 1.判断是否为 HttpRequest请求
        if (msg instanceof HttpRequest){

            System.out.println("ChannelHandlerContext hashCode"  + ctx.hashCode() + "\n TestHttpServerHandler hashCode" + this.hashCode());

            HttpRequest request = (HttpRequest) msg;


            System.out.println("msg 类型 = " + msg.getClass());
            System.out.println("客户端地址 " + ctx.channel().remoteAddress());

            //回复信息给浏览器 [http 协议]
            ByteBuf buf = Unpooled.copiedBuffer("hello , 我是服务器", CharsetUtil.UTF_8);
            // 构造一个 http 的响应 即 HttpResponse
            /**
             * 第一个参数： 指定HTTP协议版本
             * 第二个参数： 指定返回的状态码
             * 第三个参数： 指定返回内容
             */
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,buf);
            // 设置头部信息 设置返回数据应该如何展示
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            // 设置头部信息 返回数据的长度
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,buf.readableBytes());
            //将构建好的 response 返回
            ctx.writeAndFlush(response);
        }
    }
}

~~~

## 5.2 注意事项

+ 一个浏览器在同一时间发出的请求会被添加到同一个 处理器对象，并被分配给同一个 上下文对像，但是当浏览器关闭时，Netty会关闭所有的连接，下次请求时就会是一个全新的 处理器对象，和上下文对象了
+ http 是一个 无连接，无状态，无记忆

# 6，Netty 核心模型组件

## 6.1 Bootstrap ServerBootstrap 

### 6.1.1 Bootstrap 概述

> Bootstrap 的意思是引导，一个 Netty 应用通常由一个 Bootstrap 开始，主要作用是配置整个 Netty 程序，串联各个组件，Netty 中 Bootstrap 类是客户端程序的启动引导类，ServerBootstrap 是服务器端启动引导类

### 6.1.2 常见方法

```java
public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup)//，该方法用于服务器端，用来设置两个 EventLoopGroup
public B group(EventLoopGroup group) //，该方法用于客户端，用来设置一个 EventLoopGroup
public B channel(Class<? extends C> channelClass)//，该方法用来设置一个服务器端的通道实现
public <T> B option(ChannelOption<T> option, T value)//，用来给 ServerChannel 添加配置
public <T> ServerBootstrap childOption(ChannelOption<T> childOption, T value)//，用来给接收到的通道添加配置
public ServerBootstrap childHandler(ChannelHandler childHandler)//，该方法用来设置业务处理类（自定义的 handler）
public ChannelFuture bind(int inetPort) //，该方法用于服务器端，用来设置占用的端口号
public ChannelFuture connect(String inetHost, int inetPort); //，该方法用于客户端，用来连接服务器
    
// 两个方法 handler 和 childHandler 这两个初始化通道的方法 handler 用来对用 BossGroup 而 childHandler 对应 workerGroup
// 同理 option 和 childOption 的区别也是 option 用于 BossGroup 而 childOption 对用着 workerGroup45
```

## 6.2 Future，channelFuture

+ Netty 中所有的IO操作都是异步的，不能立刻得知消息是否被正确处理。但是可以过一会等它执行完成或者直接注册一个监听，具体的实现就是通过 Future 和 ChannelFutures ， 他们可以注册一个监听，当操作执行成功或者失败时监听会自动触发注册的监听事件
+ 常见的方法有
  + channel channel() 返回当前正在进行的 IO 操作的通道
  + channelFuture sync() 等待异步操作执行完毕，相当于将一个异步操作转换为了同步操作

## 6.3 Channel 

+ Netty 网络通讯的组件，能够用于执行 网络 I/O 操作
+ 通过 Channel 可获得当前网络连接的通道的状态
+ 通过 Channel 可获得 网络连接的配置参数 （例如接收缓冲区大小）
+ Channel 提供异步的网络 I/O 操作 （如建立连接，读写，绑定端口），异步调用意味着任何 I/O 调用都将立即返回，并且不保证在 调用结束时所请求的 I/O 操作已完成
+ 调用立即返回一个 ChannelFuture 实例，通过注册监听器到 ChannelFuture 上，可以 I/O 操作成功，失败或取消时回调通知调用方
+ 支持关联 I/O 操作与对应的处理程序
+ 不同协议，不同的阻塞类型的连接都有不同的 Channel 类型与之对应，常用的 Channel 类型
  + NioSocketChannel 异步的客户端 TCP  Socket 连接
  + NioServerSocketChannel 异步的服务器端 TCP Socket 连接
  + NioDatagramChannel 异步的 UDP 连接
  + NioSctpChannel 异步的客户端 Sctp 连接
  + NioSctpServerChannel 异步的 Sctp 服务器端连接，这些通道涵盖了 UDP 和 TCP 网络 IO 以及文件 IO

## 6.4 ChannelHandler 及其实现类

+ ChannelHandler 是一个接口，处理 I/O 事件 或拦截 I/O 操作，并将其转发到其 ChannelPipeline （业务处理链）中的下一个处理程序
+ ChannelHandler 本身并没有提供很多方法，因为这个接口有许多的方法需要实现，方便使用期间，可以继承它的子类
+ ChannelHandler 及其实现类一览图

![image-20191224100749101](D:\temp\xmind\netty 学习\images\image-20191224100749101.png)

> •ChannelInboundHandler 用于处理入站 I/O 事件。 就是读操作
>
> •ChannelOutboundHandler 用于处理出站 I/O 操作。 就是写操作
>
> 
>
> •ChannelInboundHandlerAdapter 用于处理入站 I/O 事件。处理入站事件
>
> •ChannelOutboundHandlerAdapter 用于处理出站 I/O 操作。 处理出站事件
>
> •ChannelDuplexHandler 用于处理入站和出站事件。 处理入栈和出栈的事件

### 6.4.2 ChannelHandler 及其实现类

```java
public class ChannelInboundHandlerAdapter extends ChannelHandlerAdapter implements ChannelInboundHandler {    
    public ChannelInboundHandlerAdapter() {
    }    
    // ChannelHandler 的注册事件
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {       
        ctx.fireChannelRegistered();
                                                                              }
    // ChannelHandler 的注销事件
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {        
        ctx.fireChannelUnregistered();
                                                                                        }
    //通道就绪事件    
    public void channelActive(ChannelHandlerContext ctx) throws Exception {       
        ctx.fireChannelActive();
                                                                          }
    // 通道未就绪事件
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {        
        ctx.fireChannelInactive();
                                                                            }
    //通道读取数据事件    
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
                                                                                    }
    //数据读取完毕事件    
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
                                                                                }
    
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {        
        ctx.fireUserEventTriggered(evt);
    }
    
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {        
        ctx.fireChannelWritabilityChanged();
    }
    //通道发生异常事件    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {       
        ctx.fireExceptionCaught(cause);	    
                                                                                             }
    
}

```

## 6.5 Pipeline 和 ChannelPipeline

+ ChannelPipeline 是一个 Handler 的集合，它负责处理和拦截 inbound 或者 outbound 的事件和操作，相当于一个贯穿 Netty 的链。（也可以这样理解：ChannelPipeline 是保存 ChannelHandler 的List ，用于处理货拦截 Channel 的入站事件和出站操作）

+ ChannelPipeline 实现了一种高级形式的拦截过滤器模式，使用户可以完全控制事件的处理方式，以及 Channel 中各个的 ChannelHandler 如歌想回交互

+ 在Netty中每个 Channel 都有且仅有一个 ChannelPipeline 与之对应，它们关系如图
![image-20191224130922297](D:\temp\xmind\netty 学习\images\image-20191224130922297.png)
	
	+ 一个 Channel 包含一个 ChannelPipelline 而 ChannelPipeline 中又维护了一个由 ChannelHandlerContext 组成的双向链表，并且每个 ChannelHandlerContext 中又关联着一个 ChannelHandler
	+ 入站事件和出站事件在一个双向链表中，入站事件会从链表 head 往后传递到最后一个入站的 handler，出站事件会从链表 tail 往前传递到最前一个 出站的 handler ， 两种类型的 handler 互不干扰
	
+ 常用方法

  + ChannelPipeline addFirst(ChannelHandler... handlers)，把一个业务处理类（handler）添加到链中的第一个位置
 +  ChannelPipeline addLast(ChannelHandler... handlers)，把一个业务处理类（handler）添加到链中的最后一个位置

## 6.6 ChannelHandlerContext

+ 保存Channel 相关的所有上下文信息，同时关联一个 ChannelHandler 对象
+ 即 ChannelHandlerContext 中包含一个具体的事件处理器 ChannelHandler 同时 ChannelHandlerContext 中也绑定了对应的 pipeline 和 Channel 的信息，方便对 ChannelHandler 进行调用
+ 这个具体的事件处理就是当前处理 请求的 Handler 对象
+ 常用方法

  + ChannelFuture close()，关闭通道

  +  ChannelOutboundInvoker flush()，刷新

  +  ChannelFuture writeAndFlush(Object msg) ， 将 数 据 写 到 ChannelPipeline 中 当 前

  + ChannelHandler 的下一个 ChannelHandler 开始处理（出站）

## 6.7 ChannelOption

+ Netty 在创建 Channel实例后，一般都需要设置 ChannelOption 参数

+ ChannelOption 参数如下

  + ChannelOption.SO_BACKLOG

  > 对应 TCP/IP 协议 listen 函数中的 backlog 参数，用来初始化服务器可连接队列大小。服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接。多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog 参数指定了队列的大小
  >
  > 如果超过队列大小的数据呢，会不会请求就直接被放弃了

  + ChannelOption.SO_KEEPALIVE

  > 一直保持连接活动状态 

## 6.8 EventLoopGroup 和 其实现类 NioEventLoopGroup

+ EventLoopGroup 是一组 EventLoop 的抽象，Netty 为了更好的利用多核 CPU 资源，一般会有多个 EventLoop 同时工作，每个 EventLoop 维护着一个 Selector 实例
+ EventLoopGroup 提供 next 接口，可以从组里面按照一定规则获取其中一个 EventLoop 来处理任务。在 Netty 服务端编程中，我们一般都需要提供两个 EventLoopGroup，例如：BossEventLoopGroup 和 WorkerEventLoopGroup 。
+ 通常一个服务端口即一个 ServerSocketChannel 对应一个 Selector 和一个 EventLoop 线程。 BossEventLoop 负责接收客户端的连接并将 SocketChannel 交给 WorkerEvenetLoopGroup 来进行 IO 处理，如下图所示   

![image-20191224145931979](D:\temp\xmind\netty 学习\images\image-20191224145931979.png)

+ 图解
  + BossEventLoopGroup 通常是一个单线程的 EventLoop，EventLoop 维护着一个注册了 ServerSocketChannel 的 Selector 实例 BossEventLoop 不断轮询 Selector 将连接事件分离出来
  + 通常是 OP_ACCEPT 事件，然后将接收到的 SocketChannel 交给 WorkerEventLoopGroup
  + WorkerEventLoopGroup 会由 next 选择其中一个 EventLoop 来将这个 SocketChannel 注册到其维护的 Selector 并对其后续的 IO 事件进行处理
+ 常用方法
  + public NioEventLoopGroup() 构造方法
  + public Future<?> shutdownGracefully() 断开连接。关闭线程

## 6.9 Unpooled 类

### 6.9.1 基本概述

> Netty 提供一个专门用来操作缓冲区（即 Netty 的数据容器） 的工具类

### 6.9.2 常用方法

> // 通过给定的数据和字符编码返回一个 ByteBuf 对象 (类似于 NIO中的 ByteBuffer 但有区别)
>
> public static ByteBuf copiedBuffer(CharSequence String,Charset charset)

### 6.9.3 实例演示

![image-20191224171335730](D:\temp\xmind\netty 学习\images\image-20191224171335730.png)

+ 图解
  + 写入的范围是 writerIndex 到 capacity
  + 读取的范围是 readerIndex 到 writerIndex
  + 0 ---> readerIndex 是已读区域
  + readerIndex ---> writerIndex 可读区域
  + writerIndex ---> capacity  可以写入的区域

~~~java
package com.yyx.netty.buf;

//import com.sun.org.apache.xpath.internal.operations.String;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * 用于演示 Netty 的 ByteBuf
 */
public class NettyByteBuf01 {
    public static void main(String[] args) {
        /**
         * 创建一个 ByteBuf
         * 说明
         * 1，创建一个对象，该对象包含一个数组，是一个 byte[10] 的一个数组
         * 2，循环填充值
         * 3，循环读取值
         * 4，在 Buf 中不需要进行 flip 进行反转 因为其底层维护了 readerIndex 和 writerIndex
         *      在写入的时候只会移动 writerIndex 指针 而读取的时候只会 移动 readerIndex 指针
         */
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.print(buffer.readByte());
        }

        /**
         * 这种形式的填充会将之前的 byte 数组替换掉
         */
        buffer.writeBytes("我爱猴猴小小小".getBytes(CharsetUtil.UTF_8));
        System.out.println(buffer.toString(CharsetUtil.UTF_8));
        buffer.clear();


        /**
         * 复制一个 Buffer 返回 ByteBuf
         */
        ByteBuf buffer1 = Unpooled.copiedBuffer("我爱佳佳~~~", CharsetUtil.UTF_8);
        // 判断是否包含一个定义好的 Array
        if (buffer1.hasArray()){
            byte[] array = buffer1.array();
            String str = new String(array, CharsetUtil.UTF_8);
            System.out.println(str);
        }
        System.out.println(buffer1.arrayOffset()); // 返回已读数
        System.out.println(buffer1.readerIndex()); // 返回读取指针所在位置
        System.out.println(buffer1.writerIndex()); // 返回写入指针所在位置
        System.out.println(buffer1.capacity());    // 返回最大写入返回 默认为 21
        System.out.println(buffer1.readableBytes());// 返回还有多少可读数据

        // 取出指定偏差，指定长度的 数据 第一个参数是开始位置，第二个是截取长度
        System.out.println(buffer1.getCharSequence(0,6,CharsetUtil.UTF_8));
    }
}

~~~

# 7，实战演示

## 7.1 群聊实战

### 7.1.1 服务器端

+ Server  main

```java
package com.yyx.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 群聊系统的服务器端
 */
public class GroupChatServer {
    private int port; // 监听端口

    public GroupChatServer(int port) {
        this.port = port;
    }

    /**
     * 编写一个 run 方法处理客户端请求
     */
    public void run() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 添加处理器
                            ChannelPipeline pipeline = ch.pipeline();
                            // 向 pipeline 中添加一个解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("endCoder",new StringEncoder());
                            //加入自己的业务处理 Handler
                            pipeline.addLast(new GroupCharServerHandler());
                        }
                    });
            System.out.println("Netty 服务器，启动成功");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // 监听关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }



    public static void main(String[] args) throws Exception {
        GroupChatServer groupChatServer = new GroupChatServer(8080);
        groupChatServer.run();
    }
}

```



+ Server Handler

~~~java
package com.yyx.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupCharServerHandler extends SimpleChannelInboundHandler<String> {

    // 定义一个 Channel 组,管理所有的 Channel
    // GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 读取的消息，并转发给所有的 channel
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (ch != channel){ // 不是当前的 channel 直接转发消息
                ch.writeAndFlush("[客户"+channel.remoteAddress()+"]"+sb.format(new Date())+" 发送消息："+msg + "\n");
            }else {
                // 回显自己发送的消息给自己
                ch.writeAndFlush("[自己]发送了消息：" + msg + "\n");
            }
        });
    }


    /**
     * 发生异常后关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    /**
     * 表示连接建立，一旦连接建立，第一个被执行
     * 将当前 channel 加入到 channelGroup 中
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户加入聊天的信息推送给其他在线的客户端 我们无需自己遍历
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "  "+sb.format(new Date())+"  加入聊天\n");
        // 将该通道添加到 channelGroup
        channelGroup.add(channel);
    }


    /**
     * 表示 channel 处于活动状态，提示 XX 上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }

    /**
     * 这个方法再 channel 处于非活动状态，提示 XX 离线了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了");
    }

    /**
     * 断开连接的时候会被触发,发送给所有的Channel用户
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + " "+sb.format(new Date())+"  断开连接\n");
        // Netty 会自动断开连接
        System.out.println("ChannelGroup size :" + channelGroup.size());
    }
}

~~~



### 7.1.2 客户端

+ Client main

~~~java
package com.yyx.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {
    /** 服务器地址 */
    private final String host;
    /** 服务器端口 */
    private final  int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                // 执行处理器任务
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 得到 pipeline
                    ChannelPipeline pipeline = ch.pipeline();
                    // 加入编解码的 handler
                    pipeline.addLast("decoder",new StringDecoder());
                    pipeline.addLast("encoder",new StringEncoder());
                    // 加入自定义 handler
                    pipeline.addLast(new GroupCharClientHandler());
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            //获取到通道
            Channel channel = channelFuture.channel();
            // 客户端需要输入信息，创建一个扫描器
            Scanner sc = new Scanner(System.in);
            // 判断是否还有下一次输入
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                // 判断是否想要进行输出
                if ("exit".equals(line)){
                    break;
                }
                channel.writeAndFlush(line + "\r\n");
            }
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        GroupChatClient chatClient = new GroupChatClient("127.0.0.1", 8080);
        chatClient.run();
    }
}

~~~



+ client Handler

~~~java
package com.yyx.netty.groupchat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GroupCharClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 读取服务器发送来的消息的方法 并展示数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg.trim());
    }
}

~~~



## 7.2 私聊实战

### 7.2.1 服务器端

> 等会自己写 
>
> 实现步骤，用户要进行登录才能群聊
>
> 登录成功后将 对象保存在一个  List<Channel> list = Collections.synchronizedList(new ArrayList<Channel>());
>
> 使用同步容器来保存 对象 解决并发问题 

+ 只需要修改 服务器端

```java
/**
     * 读取的消息，并转发给所有的 channel
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        // 判断读取的内容 是否为登录请求
        if (msg.indexOf("#") != -1) {
            String[] split = msg.split("#");
            User user = new User(split[0], split[1]);
            map.put(user, ctx.channel());
            // 并提示全局
            map.forEach(new BiConsumer<User, Channel>() {
                @Override
                public void accept(User user1, Channel channel1) {
                    if (channel != channel1) { // 不是当前的 channel 直接转发消息
                        channel1.writeAndFlush("用户" + user.getId() + "登录成功");
                    } else {
                        // 回显自己发送的消息给自己
//                        channel1.writeAndFlush("[自己]发送了消息：" + msg + "\n");
                    }
                }
            });
            return;
        }

        // 表示要进行私发
        if (msg.indexOf("&") != -1) {
            String[] split = msg.split("&");
            map.forEach(new BiConsumer<User, Channel>() {
                @Override
                public void accept(User user, Channel channel) {
                    if (user.getId().equals(split[1])) {
                        // 如果相等就将消息转发
                        channel.writeAndFlush("[客户" + channel.remoteAddress() + "]" + sb.format(new Date()) + " 发送私密消息：" + msg + "\n");
                    }
                }
            });
            return;
        }

        map.forEach(new BiConsumer<User, Channel>() {
            @Override
            public void accept(User user, Channel ch) {
                if (ch != channel) { // 不是当前的 channel 直接转发消息
                    ch.writeAndFlush("[客户" + channel.remoteAddress() + "]" + sb.format(new Date()) + " 发送消息：" + msg + "\n");
                } else {
                    // 回显自己发送的消息给自己
                    ch.writeAndFlush("[自己]发送了消息：" + msg + "\n");
                }
            }
        });
    }
```



### 7.2.2 客户端

## 7.3 Netty 心跳检测机制 案例

+ 案例要求
  + 编写一个 Netty 心跳检测机制，当服务器超过3秒没有读时，就提示读空闲
  + 当服务器超过5秒没有写操作时，就提示写空闲
  + 当服务器超过7面没有读或者写操作

+ 案例代码：

  + MyServer

  ```java
  package com.yyx.netty.heartbeat;
  
  import io.netty.bootstrap.ServerBootstrap;
  import io.netty.channel.ChannelFuture;
  import io.netty.channel.ChannelInitializer;
  import io.netty.channel.ChannelPipeline;
  import io.netty.channel.EventLoopGroup;
  import io.netty.channel.nio.NioEventLoopGroup;
  import io.netty.channel.socket.SocketChannel;
  import io.netty.channel.socket.nio.NioServerSocketChannel;
  import io.netty.handler.logging.LogLevel;
  import io.netty.handler.logging.LoggingHandler;
  import io.netty.handler.timeout.IdleStateHandler;
  
  import java.util.concurrent.TimeUnit;
  
  public class MyServer {
      public static void main(String[] args) throws InterruptedException {
          EventLoopGroup bossGroup = new NioEventLoopGroup();
          EventLoopGroup workerGroup = new NioEventLoopGroup();
          try {
              ServerBootstrap serverBootstrap = new ServerBootstrap();
              serverBootstrap.group(bossGroup,workerGroup)
                      .channel(NioServerSocketChannel.class)
                      .handler(new LoggingHandler(LogLevel.INFO))//设置日志处理器
                      .childHandler(new ChannelInitializer<SocketChannel>() {
                          @Override
                          protected void initChannel(SocketChannel ch) throws Exception {
                              ChannelPipeline pipeline = ch.pipeline();
                              /**
                               * 加入一个 netty 提供 IdleStateHandler
                               * 说明:
                               * IdleStateHandler ： 是 Netty 提供的处理空闲状态的处理器
                               * long readerIdleTime, 表示多长时间没有读了，就会发送一个心跳检测包，检测是否还是连接的状态
                               * long writerIdleTime, 表示多长时间没有写操作， 就会发送一个心跳检测包，检测是否还是连接的状态
                               * long allIdleTime,    表示多久时间没有读写操作，就会发送一个心跳检测包，检测是否还是连接的状态
                               * 当 长时间没有进行 读 ，写 或者读和写的操作时就会触发 一个 事件
                               * 当 IdleStateEvent 触发后，就会传递给管道 的下一个 Handler 去处理 通过触发下一个 Handler 的 UserEventTiggered 在该方法中去处理 （读空闲，写空闲，读写空闲）
                               */
                              pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                              // 加入一个对空闲检测进一步处理的 Handler （自定义） 谁会触发这些事件 ： 下一加入的 Handler 会去触发这些事件
                              pipeline.addLast(new MyServerHandler());
                          }
                      });
              ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
              channelFuture.channel().closeFuture().sync();
          }finally {
              bossGroup.shutdownGracefully();
              workerGroup.shutdownGracefully();
          }
      }
  }
  
  ```

  

  + MyServerHandler

  ~~~java
  package com.yyx.netty.heartbeat;
  
  import io.netty.channel.ChannelHandlerContext;
  import io.netty.channel.ChannelInboundHandlerAdapter;
  import io.netty.handler.timeout.IdleStateEvent;
  
  /**
   * 处理心跳事件的 handler 类
   */
  public class MyServerHandler extends ChannelInboundHandlerAdapter {
      /**
       * 处理心跳事件的 方法
       * @param ctx 上下文对象
       * @param evt 事件
       * @throws Exception
       */
      @Override
      public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
          // 判断类型进行强转
          if (evt instanceof IdleStateEvent) {
              IdleStateEvent event = (IdleStateEvent) evt;
              String eventType = null;
              switch (event.state()){
                  case READER_IDLE:
                      eventType = "读空闲";
                      break;
                  case WRITER_IDLE:
                      eventType = "写空闲";
                      break;
                  case ALL_IDLE:
                      eventType = "读写空闲";
                      break;
              }
              System.out.println(ctx.channel().remoteAddress()+ "---超时时间---" + eventType);
              System.out.println("在此做出对应的处理");
          }
      }
  }
  
  ~~~

  + 就是处理连接的一种方式

# 8，长连接 WebSocket

## 8.1 实例

+ 实例要求

  + Http 协议是无状态的，浏览器和服务器间的请求响应一次，下一次会重新创建连接
  + 要求： 实现基于 webSocket 的长连接的全双工的交互
  + 改变 Http 协议多次请求的约束，实现长连接了，服务器可以发送消息给浏览器
  + 客户端浏览器和服务器端会相互感知，比如服务器关闭了，浏览器会感知，同样浏览器关闭了，服务器会感知

+ 实例代码

  + Server

    + mian Server

    ~~~java
    package com.yyx.netty.websocket;
    
    import io.netty.bootstrap.ServerBootstrap;
    import io.netty.channel.ChannelFuture;
    import io.netty.channel.ChannelInitializer;
    import io.netty.channel.ChannelPipeline;
    import io.netty.channel.EventLoopGroup;
    import io.netty.channel.nio.NioEventLoopGroup;
    import io.netty.channel.socket.SocketChannel;
    import io.netty.channel.socket.nio.NioServerSocketChannel;
    import io.netty.handler.codec.http.HttpObjectAggregator;
    import io.netty.handler.codec.http.HttpServerCodec;
    import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
    import io.netty.handler.logging.LogLevel;
    import io.netty.handler.logging.LoggingHandler;
    import io.netty.handler.stream.ChunkedWriteHandler;
    
    public class MyServer {
    
        public static void main(String[] args) throws Exception{
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(bossGroup,workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))//设置日志处理器
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                // 由于是 http 请求所以要添加编解码器
                                pipeline.addLast(new HttpServerCodec());
                                // 由于 http 是以块的形式写的，添加 ChunkedWriteHandler 处理器
                                pipeline.addLast(new ChunkedWriteHandler());
                                /**
                                 * 说明
                                 * 1，http数据在传输过程中是分段的，HttpObjectAggregator 就是可以将多个段聚合在一起
                                 * 2，这就是为什么，当浏览器发送大量数据时，就会发出多次 http 请求
                                 */
                                pipeline.addLast(new HttpObjectAggregator(8192));
                                /**
                                 * 说明
                                 * 1，对应 webSocket 它的数据是以帧（frame） 形式进行传递
                                 * 2，可以看到 WebSocketFrame 下面有六个子类
                                 * 3，浏览器请求时 ws://localhost:7000/hello 表示请求的 url
                                 * 4，webSocketServerProtocolHandler 核心功能是将 http 协议升级为 ws 协议，保存长连接
                                 */
                                pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
    
                                // 自定义 Handler 处理业务逻辑
                                pipeline.addLast(new MyTextWebSocketFrameHandel());
                            }
                        });
                ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
                channelFuture.channel().closeFuture().sync();
            }finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
    
    }
    
    ~~~

    +  Handel 处理器

    ~~~java
    package com.yyx.netty.websocket;
    
    import io.netty.channel.ChannelHandlerContext;
    import io.netty.channel.SimpleChannelInboundHandler;
    import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
    
    import java.time.LocalDateTime;
    
    /**
     * 处理这个泛型，表示一个文本帧 TextWebSocketFrame
     */
    public class MyTextWebSocketFrameHandel extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            System.out.println("服务器收到消息 " + msg.text());
            //回复消息 也要封装消息
            ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间 " + LocalDateTime.now() + "信息：" + msg.text()));
        }
    
        /**
         * 当 web 客户端连接后，就会触发方法
         * @param ctx
         * @throws Exception
         */
        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            // id 表示一个唯一的值 ： LongText 是唯一的
            System.out.println("handlerAdded 被调用" + ctx.channel().id().asLongText());
            // id 表示一个唯一的值 ： ShortText 不一定是唯一的
            System.out.println("handlerAdded 被调用" + ctx.channel().id().asShortText());
        }
    
    
        /**
         * 当 web 客户端 被移除的时候就会触发本方法
         * @param ctx
         * @throws Exception
         */
        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            System.out.println("handlerRemoved 被调用: " + ctx.channel().id().asLongText());
            System.out.println("handlerRemoved 被调用: " + ctx.channel().id().asShortText());
        }
    
        /**
         * 异常发生时
         * @param ctx
         * @param cause
         * @throws Exception
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("异常发生 " + cause.getMessage());
            // 关闭通道
            ctx.close();
        }
    }
    
    ~~~

  + 前段 HTML

  ~~~html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>Title</title>
  </head>
  <body>
  <form onsubmit="return false" id="from">
      <textarea style="height: 300px;width: 300px;" name="message"></textarea>
      <input type="button" value="发送消息" onclick="send(this.form.message)">
      <br>
      <textarea id="responseText" cols="30" rows="10"></textarea>
      <input type="button" value="清空内容" onclick="document.getElementById('responseText').value = ''">
  </form>
  <script>
      var socket;
      // 判断浏览器是否支持 socket 编程
      if(window.WebSocket){
          socket = new WebSocket("ws://localhost:8080/hello");
          // 相当于 channel Read0 ev 收到服务器端回送的消息
          // 开启读取事件
          socket.onmessage = function (ev) {
              console.log(ev)
              var rt = document.getElementById("responseText")
              // 将服务器端的消息 累加到 页面上
              rt.value = rt.value + "\n" + ev.data;
          }
          // 开启连接事件
          socket.onopen = function (ev) {
              console.log(ev)
              var rt = document.getElementById("responseText")
              rt.value = "开启连接"
          }
  
          socket.onclose = function (ev) {
              var rt = document.getElementById("responseText")
              // 将服务器端的消息 累加到 页面上
              rt.value = rt.value + "\n连接关闭了";
          }
      }else {
          alert("你的浏览器不支持 webSocket ")
      }
  
      function send(document){
          // 先判断浏览器是否支持
          if(!window.WebSocket){
              return;
          }
          // 判断是否已经开启了 WebSocket
          if(socket.readyState == WebSocket.OPEN){
              // 通过 socket 发送消息到服务器端
              socket.send(document.value)
          }
      }
  </script>
  </body>
  </html>
  ~~~

  



# 9, Google Protobuf

## 9.1 编码和解码的基本介绍

+ 在编写网络应用程序时，因为数据在网络中传输的都是 二进制字节码数据，在发送数据时就需要编码，接收数据时就需要解码
+ codec（编码器）的组成部分有两个 ：decoder（解码器） 和 encoder（编码器）
  + encoder 负责吧业务数据转换为字节码数据
  + decoder 负责吧字节码数据转换成业务数据

## 9.2 Netty 本身的编码解码机制

+ Netty 自身提供了一些 codec （编解码器）
+ Netty 提供的编码器
  + StringEncoder 对字符串数据进行编码
  + ObjectEncoder 对java对象进行编码
  + ...
+ Netty 提供的解码器
  + StringDecoder 对字符串数据进行解码
  + ObjectDecoder 对 java 对象进行解码
  + ...
+ Netty 本身自带的 ObjectDecoder 和 ObjectEncoder 可以用来实现 POJO 对象或各种业务对象的编码和解码，底层使用的仍是 java 序列化技术，而 java 序列化技术本身效率就不高，存在如下问题
  + 无法跨语言
  + 序列化后体积太大，是二进制编码的 5 倍多
  + 序列化性能太低
+ =》 引出新的解决方案 【Google 的 Protobuf】

## 9.3 Protobuf

### 9.3.1 Protobuf 基本介绍和使用示意图

+ Protobuf 是 Google 发布的开源项目，全称 Google Protocol Buffers 是一种轻便高效的结构化数据存储格式，可以用于结构化数据串行化，或者说序列化。它很适合做数据存储 或 RPC 【远程过程调用 remote procedure call】 数据交换格式
  + 目前很多公司正在转换 ： http + json  => tcp + protobuf
+ 参考文档 ：      [https://](https://developers.google.com/protocol-buffers/docs/proto)[developers.google.com/protocol-buffers/docs/proto](https://developers.google.com/protocol-buffers/docs/proto)    要翻墙
+ Protobuf 是以 message 的方式来管理数据的
+ 支持跨平台，跨语言，即【客户端和服务器端可以是不同的语言编写的】（支持目前绝大多数语言，例如 c++ ， c# ， java ， python，等）
+ 高性能，高可靠性
+ 使用 protobuf 编译器能自动生成代码，protobuf 是将类的定义使用 proto 文件进行扫描，说明，在 idea 中 编写 .proto 文件时，会自动提示是否下载 .proto 编写插件，可以让语法高亮
+ 然后通过 protoc.exe 编译器根据 .proto 自动生成 .java 文件
+ protobuf 基本 演示

### 9.3.2 基本数据类型/基本使用

<img src="D:\temp\xmind\netty 学习\images\image-20191225175328675.png" alt="image-20191225175328675" style="zoom:150%;" />

<img src="D:\temp\xmind\netty 学习\images\image-20191225175341187.png" alt="image-20191225175341187" style="zoom:150%;" />



+ 基本使用

~~~protobuf
syntax = "proto3"; // 指定版本
option java_outer_classname = "studentPOJO"; // 生成外部类名，同时也是文件名
// protobuf 使用 message 管理数据
message Student { // 会在 studentPOJO 外部类生成一个内部类 student , 他是真正发送的 pojo 对象
    int32 id = 1; // Student 类中有一个属性 名字为 id 类型为 int32(protobuf 类型) 1,表示属性序号 不是值
    string name = 2;
}
~~~

+ 使用个 protoc.exe 对文件进行编译

  + 编译指令 protoc.exe --java_out=. ss.proto 输出一个 java类

+ 案例演示

  + Server main

  ```java
  // 使用 ServerBootstrap 对象进行配置
  bootstrap.group(boosGroup,workerGroup) // 设置两个线程组
                      .channel(NioServerSocketChannel.class) // 使用 NioServerSocketChannel 作为服务器的通道实现
                      .option(ChannelOption.SO_BACKLOG,128) // 设置线程队列得到连接个数
                      .childHandler(new ChannelInitializer<SocketChannel>(){ // 创建一个通道初始化对象（匿名对象）
                          // 给 Pipeline （管道） 设置处理器
                          @Override
                          protected void initChannel(SocketChannel ch) throws Exception {
                              // 设置处理器处理 读写 操作
                              // 添加编码操作 并指定解码对象
                              ch.pipeline().addLast(new ProtobufDecoder(studentPOJO.Student.getDefaultInstance()));
                              ch.pipeline().addLast(new NettyServerHandler());
                          }
                      });// 给 workerGroup 的 EventLoop 对应的管道设置处理器
  ```

  + Server Handler

  ```java
  // 直接展示 客户端发送过来的数据
  public class NettyServerHandler extends SimpleChannelInboundHandler<studentPOJO.Student> {
      @Override
      protected void channelRead0(ChannelHandlerContext ctx, studentPOJO.Student msg) throws Exception {
          System.out.println("直接展示 数据");
          System.out.println(msg.getId() + "    " + msg.getName());
      }
  
  }
  ```

  + client main

  ```java
  // 添加编码 处理器 
  Bootstrap bootstrap = new Bootstrap();
              bootstrap.group(eventExecutors) // 设置线程组
                      .channel(NioSocketChannel.class) // 设置客户端通道的实现类 （反射）
                      .handler(new ChannelInitializer<SocketChannel>() {
  
                          @Override
                          protected void initChannel(SocketChannel ch) throws Exception {
                              // 添加编码
                              ch.pipeline().addLast(new ProtobufEncoder());
                              ch.pipeline().addLast(new NettyClientHandler());
                          }
                      });
  ```

  + client handler

  ```java
  	/**
       * 当通道就绪时会触发本方法
       * @param ctx
       * @throws Exception
       */
      @Override
      public void channelActive(ChannelHandlerContext ctx) throws Exception {
          // 在通道就绪的时候 创建一个 Student 对象 发送给服务器端
          studentPOJO.Student student = studentPOJO.Student.newBuilder().setId(10).setName("你可真是个弟弟").build();
          ctx.channel().writeAndFlush(student);
      }
  ```

  

### 9.3.3 protobuf 进阶 使用同一个服务器，接收不同的数据类型并转换

+ student.proto 

~~~protobuf
syntax="proto3";
option optimize_for = SPEED; // 加快解析
option java_package = "com.yyx.netty.codec"; // 指定生成到那个包下
option java_outer_classname = "MyDataInfo"; // 指定将要输出的外部类名

message MyMessage {
    // 定义一个枚举类型
    enum DataType {
        studentType = 0; // 在 protobuf 中 enum 类型的编号要从 0 开始
        workerType = 1;
    }
    // 用 data_type 来标识传的是哪一个枚举类型
    DataType data_type = 1;

    // 表示每次枚举类型最多只能出现 一个
    oneof dataBody {
        Student student = 2;
        Worker worker = 3;
    }
}

message Student {
    int32 id = 1;
    string name = 2;
}

message Worker {
    string name = 1;
    int32 age = 2;
}
~~~

+ server mian

```java
// 修改服务器端 解码绑定的对象
ServerBootstrap bootstrap = new ServerBootstrap();
            // 使用链式编程来进行设置
            bootstrap.group(boosGroup,workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用 NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128) // 设置线程队列得到连接个数
                    .childHandler(new ChannelInitializer<SocketChannel>(){ // 创建一个通道初始化对象（匿名对象）
                        // 给 Pipeline （管道） 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 设置处理器处理 读写 操作
                            // 添加编码操作 并指定解码对象
                            ch.pipeline().addLast(new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });// 给 workerGroup 的 EventLoop 对应的管道设置处理器

```



+ server handler

~~~java
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    // 针对不同的类型展示不同的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        if (msg.getDataType() == MyDataInfo.MyMessage.DataType.studentType){
            System.out.println("student");
            System.out.println(msg.getStudent().getName() + "   " + msg.getStudent().getId());
        }else if (msg.getDataType() == MyDataInfo.MyMessage.DataType.workerType){
            System.out.println("worker");
            System.out.println(msg.getWorker().getName() + "  " + msg.getWorker().getAge());
        }
    }
}
~~~



+ client main

~~~java
// 代码完全不变
Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors) // 设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类 （反射）
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 添加编码
                            ch.pipeline().addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });
~~~



+ client handler

~~~java
	/**
     * 当通道就绪时会触发本方法 根据随机数创建不同的数据类型
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Random random = new Random();
        int rand = random.nextInt(3);
        MyDataInfo.MyMessage myMessage = null;
        if (rand == 0) {
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.studentType).setStudent(MyDataInfo.Student.newBuilder().setName("克鹏").setId(10)).build();
        }else if (rand == 1){
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.workerType).setWorker(MyDataInfo.Worker.newBuilder().setName("佳佳").setAge(18)).build();
        }else {
            System.out.println("类型错误请重新输入");
        }
        ctx.channel().writeAndFlush(myMessage);
    }
~~~

+ 总结

> 其实原理也很简单，在一个对象中包含了多个对象的持有对象 这样就可以保证即使只传递了一个对象，也可以拥有多种不同的数据类型












# 10，Netty 编解码器 和 Handler 的调用机制

## 10.1 基本说明

+ netty 的组件设计： Netty 的主要组件有 Channel，EventLoop，ChannelFuture，ChannelHandler，ChannelPipe 等
+ ChannelHandler 充当了处理入站和出站数据的应用程序逻辑的容器。例如，实现 ChannelInboundHandler 接口（或ChannelInboundHandlerAdapter） ，你就可以接收入站事件和数据，这些数据随后会被你的应用程序的业务逻辑处理。当你要给连接客户端发送响应时，也可以从 ChannelInboundHandler 冲刷数据，你的业务逻辑通常写在一个或者多个 ChannelInboundHandler中。ChannelOutboundHandler 原理一样，只不过它是用来处理出站数据的
  + 入站程序由我们编写
  + 出站程序则由 Netty 来控制 （起码现在是，感觉以后可能会打脸....）
+ ChannelPipeline 提供了 ChannelHandler 链的容器。以客户端应用程序为例，如果事件的运动方向是从客户端到服务端的，那么我么称这些事件为出站的，即客户端发送给服务器端的数据会通过pipeline中的一系列ChannelOutboundHandler，并被这些Handler处理，反之则称为入站的

![image-20191225221645547](D:\temp\xmind\netty 学习\images\image-20191225221645547.png)

## 10.2 编码解码器

+ 当 Netty 发送或接受一个请求消息的时候，就将会发生一次数据转换。入站消息会被解码：从字节码转换为另一种格式（比如 java 对象）；如果是出站消息，它会被编码成字节
+ Netty 提供一系列实用的编解码器，他们都实现了 ChannelInboundHandler 或者 ChannelOutboundHandler 接口。在这些类中，channelRead 方法已经被重写了。以入站为例，对于每个从入站 Channel 读取的消息，这个方法会被调用，随后，它将调用由解码器所提供的 decode（） 方法进行解码，并将已经解码的字节码发给 channelPipeline 中下一个 ChannelInboundHandler

### 10.2.2 解码器 ByteToMessageDecoder

+ 关系继承图

![image-20191226075011031](D:\temp\xmind\netty 学习\images\image-20191226075011031.png)

+ 由于不可能知道远程节点是否会一次性发送一个完整的信息，tcp有可能出现粘包，拆包的问题，这个类会对入站数据进行缓存，直到它准备好被处理

+ ByteToMessageDecoder 案例分析

  + 案例

  ```java
  public class ToIntegerDecoder extends ByteToMessageDecoder {
      /**
       * 这个方法用于对输入参数的解码
       * 
       */
      @Override
      protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
          // 判断通道内是否还包含有4个byte 一个 int 是 4个字节 当有 4 个字节的时候才能读取出一个完整的 int 值
          if (in.readableBytes() >= 4) {
              // 使用输出方法 输出到 List<Object> out 中用于 下一个 handler 处理器进行处理
              out.add(in.readInt());
          }
      }
  }
  ```

  

  + 分析 

  > 将要读取一个 int 的值，要先判断 通道中是否还包含有 4 个可读字节

+ 自己实现 Decoder Encoder 

## 10.3 解码器 ReplayingDecoder

+ public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder
+ ReplayingDecoder 扩展了 ByteToMessageDecoder 类，使用这个类，我们不必调用 ReadableBytes() 方法。参数 S 指定了用户状态管理的类型，其中 Void 表示不需要状态管理
+ 案例实战

~~~java
/**
 * 进行解码的 handler
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 不在需要判断 是否有可读取的内容了
        System.out.println("对文件进行解码");
            // 读取并添加到 out 集合中方便下次赋值
            out.add(in.readLong());
    }
}
~~~



+ ReplayingDecoder 的局限性
  + 并不是所有的 ByteBuf 操作都被支持，如果调用了一个不被支持的方法，将会抛出一个 UnsupportedOperationException
  + ReplayingDecoder 在某种情况下可能会稍慢于 ByteToMessageDecoder 例如网络缓慢并消息格式复杂时，消息被拆分为多个碎片，速度缓慢

## 10.4 其他内置编码器

+ LineBasedFrameDecoder  ： 这个类在 Netty 内部也有使用，它使用行尾控制字符（“\n” 或者 "\r\n"）作为分隔符来解析数据
+ ​     DelimiterBasedFrameDecoder ： 使用 自定义的 特殊字符作为消息分隔符
+ ​     HttpObjectDecoder  ： 一个 HTTP 数据的解码器
+ ​     LengthFieldBasedFrameDecoder  ： 通过指定长度来标识整包消息，这样就可以自动处理半包和黏包消息

![image-20191227094535091](D:\temp\xmind\netty 学习\images\image-20191227094535091.png)

# 11 , Netty 的handler链的调用机制

## 11.1 案例实战

+ MyServer

~~~java
package com.yyx.netty.inboundhandlerandoutboundhandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {
    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).childHandler(new MyServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

~~~



+ MyServerHandler

~~~java
package com.yyx.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("读取到 客户端发来的请求");
        System.out.println(msg);
    }

    /**
     * 服务器端读取完成够发送数据
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(5678L);
    }
}

~~~



+ MyServerInitializer

~~~java
package com.yyx.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * 用于初始化 Channel 通道
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 添加一个 入站的 handler 进行解码 MyByteToLongDecoder
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new ByteToMessageEncoder());
        pipeline.addLast(new MyServerHandler());
    }
}

~~~



+ MyClient

~~~java
package com.yyx.netty.inboundhandlerandoutboundhandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {
    public static void main(String[] args) throws Exception{
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new MyClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}

~~~



+ MyClientHandler

~~~java
package com.yyx.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    /**
     * 读取服务器端发送的请求
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("接收到 服务器端发送的 请求");
        System.out.println(msg);
    }

    /**
     * 连接创建完成后 直接发送数据
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClient 发送数据");
        /**
         * 我们注意到一件事情， wioqjasdjklasdkljasdhjkasdjkh 这是一个很长的字符串
         * 但是我们并没有定义一个用于 处理字符的 Handler 所以当我们写出 一个字符串的时候就会发现定义的
         * LongHandler 并没有被执行，数据直接被写出去了 如果是相同的类型就进行处理，如果不是就跳过，使用下一个 Handler 处理数据
         */
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("wioqjasdjklasdkljasdhjkasdjkh", CharsetUtil.UTF_8));
    }
}

~~~



+ MyClientInitializer

~~~java
package com.yyx.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 为客户端添加 解码
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new ByteToMessageEncoder());
        pipeline.addLast(new MyClientHandler());
    }
}

~~~



+ ByteToMessageEnCoder

~~~java
package com.yyx.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ByteToMessageEncoder extends MessageToByteEncoder<Long> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        // 将数据写出
        System.out.println("ByteToMessageEncoder 对文件进行编码");
        out.writeLong(msg);
    }
}

~~~



+ MyByteToLongDecoder

~~~java
package com.yyx.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 进行解码的 handler
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * 对文件进行编码，如果方法结束后依然还有可读数据，这个方法会执行多次
     * 使用 while 循环可以解决这个问题，但是依然会执行两次因为还会有一次是在进行判断
     * 直到确定没有新的元素被添加到 list ，或者 ByteBuf，没有更多的可读字节为止
     * 如果 list out 数据不为空 就会将 list 的内容传递给下一个 ChannelInboundHandler 处理
     * 每次调用 向 out 填充值，方法结束后就会执行 MyClientHandler 对象中的 read 方法
     * @param ctx 上下文对象
     * @param in  入站的 ByteBuf
     * @param out list 集合，将解码后的数据传给下一个 handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("对文件进行解码");
        // 表示还要数据可读
        if (in.readableBytes() >= 8) {
            // 读取并添加到 out 集合中方便下次赋值
            out.add(in.readLong());
        }
    }
}

~~~

## 11.2 总结

+ 不论解码器 Handler 还是编码器 Handler 即接收数据类型必须与待处理的消息类型一致，否则该 Handler 就不会执行
+ 在解码器进行数据解码时，需要判断缓存区（ByteBuf）的数据是否足够，否则接收到的结果会和期望结果不一致

## 11.3 Netty  Log4j 整合

+ 添加 pom.xml 文件

~~~xml
	<dependency>
    	<groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.26</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.25</version>
    </dependency>
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.7.26</version>
	</dependency>
~~~

+ 添加 log4j.properties 配置文件

~~~properties
log4j.rootLogger=DEBUG, stdout
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=[%p] [%C]{1} - %m%n
~~~

+ 直接启动就可以看到打印信息了

# 12，TCP 粘包和拆包 及解决方案

## 12.1 TCP 粘包和拆包基本介绍

+ TCP是面向连接，面向流的，提供高可靠性服务。收发两端（客户端，服务器端）都要有一一成对的Socket，因此发送端为了将多个发送给接收端的包，更有效的发给对方，使用优化方法（Nagle 算法），将多次间隔较小且数据量小的数据，合并成一个大的数据块，然后进行封包。这样做虽然提高了效率，但是接收端就难于分辨出完整的数据包了，因为**面向流的通讯是无消息边界的**

+ 由于TCP无消息保护边界，需要在接收端处理消息边界问题，也就是我们说的粘包，拆包问题，

+ 图解： 假设客户分别发送了两个数据包 D1 D2 给服务器端，由于服务端一次读取到的字节数是不确定的，故可能存在以下四种情况

  ![image-20191227105724792](D:\temp\xmind\netty 学习\images\image-20191227105724792.png)

  + 服务器端分 两次进行读取到两个 独立的 数据包 分别是 D1 D2 是一种正确的解决方案
  + 服务器端一次接受到了两个数据包，D1 和 D2 粘合在一起 称为 TCP 粘包
  + 服务器端分两次读取到了数据包，第一次读取到完整的 D1 和 D2 包的部分内容 第二次读取读到了 D2的剩余全部内容，这就叫做 TCP 的拆包
  + 服务器端分两次读取到了数据包，第一次读取到了D1 的部分内容，第二次读取到了 D2 的全部内容和 D1 的剩余部分内容，这也是 TCP 拆包
  

## 12.2 粘包和拆包的 解决方案

### 12.2.1 解决方案的概述

+ 使用自定义协议 ——编解码器 来解决
+ 关键就是要解决 服务器端每次读取数据长度问题，这个问题解决，就不会出现服务器多读，少读数据的问题，从而避免 tcp 粘包，拆包

![image-20200101151855956](D:\temp\xmind\netty 学习\images\image-20200101151855956.png)

### 12.2.1 解决方案 案例

+ 自己定义 数据传输 对象

```java
package com.yyx.netty.protocoltcp;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MessageProtocol {
    // 定义传输长度
    private Integer len;
    private byte[] count;
}
```

+ 数据进行编码器

```java
package com.yyx.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyMessageEncoder extends MessageToByteEncoder<MessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println(" MyMessageEncoder  encode  方法被调用 ");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getCount());
    }
}
```

+ 数据进行解码器

```java
package com.yyx.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder<MessageProtocol> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder decode 被调用");
        int len = in.readInt();
        byte[] count = new byte[len];
        in.readBytes(count);
        // 将数据封装成对象
        MessageProtocol mp = new MessageProtocol();
        mp.setLen(len);
        mp.setCount(count);
        out.add(mp);
    }
}
```

> 就这样理解，一个 channel 是一个流的管道，对于流来说里面存储的东西都是有序的 写入流中，再读出来都是有顺序的，这种情况下，每次都在头部加入 下次要读取的长度，就能保证数据的每次都正确读取

# 13, Netty 源码剖析

## 13.1 netty 启动过程中源码剖析

### 13.1.1 netty 源码剖析目的

用源码分析的方式走一下 Netty （服务器）的启动过程，更好的理解整个设计和运行机制

### 13.1.2 启动过程梳理

+ 1)创建2个 EventLoopGroup 线程池数组。数组默认大小CPU*2，方便chooser选择线程池时提高性能

+ 2)BootStrap 将 boss 设置为 group属性，将 worker 设置为 childer 属性

+ 3)通过 bind 方法启动，内部重要方法为 initAndRegister 和 dobind 方法

+ 4)initAndRegister 方法会反射创建 NioServerSocketChannel 及其相关的 NIO 的对象， pipeline ， unsafe，同时也为 pipeline 初始了 head 节点和 tail 节点。

+ 5)在register0 方法成功以后调用在 dobind 方法中调用 doBind0 方法，该方法会 调用 NioServerSocketChannel 的 doBind 方法对 JDK 的 channel 和端口进行绑定，完成 Netty 服务器的所有启动，并开始监听连接事件

### 13.1.3 源码剖析

~~~java
//服务器启动类源码
/*
* Copyright 2012 The Netty Project
*
* The Netty Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
*
*
http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package atguigu.netty.example.echo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
/**
* Echoes back any received data from a client.
*/
public final class EchoServer {
static final boolean SSL = System.getProperty("ssl") != null;
static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
public static void main(String[] args) throws Exception {
// Configure SSL.
final SslContext sslCtx;
if (SSL) {
SelfSignedCertificate ssc = new SelfSignedCertificate();
sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();

} else {
sslCtx = null;
}
// Configure the server.
EventLoopGroup bossGroup = new NioEventLoopGroup(1);
EventLoopGroup workerGroup = new NioEventLoopGroup();
try {
ServerBootstrap b = new ServerBootstrap();
b.group(bossGroup, workerGroup)
.channel(NioServerSocketChannel.class)
.option(ChannelOption.SO_BACKLOG, 100)
.handler(new LoggingHandler(LogLevel.INFO))
.childHandler(new ChannelInitializer<SocketChannel>() {
@Override
public void initChannel(SocketChannel ch) throws Exception {
ChannelPipeline p = ch.pipeline();
if (sslCtx != null) {
p.addLast(sslCtx.newHandler(ch.alloc()));
}
//p.addLast(new LoggingHandler(LogLevel.INFO));
p.addLast(new EchoServerHandler());
}
});
// Start the server.
ChannelFuture f = b.bind(PORT).sync();
// Wait until the server socket is closed.
f.channel().closeFuture().sync();
} finally {
// Shut down all event loops to terminate all threads.
bossGroup.shutdownGracefully();
workerGroup.shutdownGracefully();
}
}
}
~~~

+ 1)先看启动类：main方法中，首先创建了关于  SSL的配置类。 

+ 2)重点分析下创建了两个  EventLoopGroup对象：  EventLoopGroup  bossGroup = new NioEventLoopGroup(1);  EventLoopGroup  workerGroup = new NioEventLoopGroup();  

  +  (1)这两个对象是整个  Netty的核心对象，可以说，整个  Netty的运作都依赖于他们。bossGroup用于接受  Tcp请求，他会将请求交给  workerGroup，workerGroup会获取到真正的连接，然后和连接进行通信，比如读  写解码编码等操作。  

  + (2) EventLoopGroup是事件循环组（线程组）含有多个    EventLoop，可以注册  channel  ,用于在事件循  环中去进行选择（和选择器相关）.。[debug看]  

  +  (3) new NioEventLoopGroup(1);这个  1表示   bossGroup事件组有   1个线程你可以指定，如果     new  NioEventLoopGroup()会含有默认个线程  cpu核数*2,即可以充分的利用多核的优势，【可以  dubug一把】  DEFAULT_EVENT_LOOP_THREADS  = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads",  NettyRuntime.availableProcessors() * 2));  

      会创建  EventExecutor数组   children = new EventExecutor[nThreads]; //debug一下  每个元素的类型就是 NIOEventLoop,  NIOEventLoop实现了  EventLoop接口和   Executor接口  try块中创建了一个  ServerBootstrap对象，他是一个引导类，用于启动服务器和引导整个程序的初始化（看下源  码 allows easy  bootstrap of {@link ServerChannel}）。它和  ServerChannel关联，而   ServerChannel继承  了 Channel，有一些方法  remoteAddress等   [可以  Debug下]  随后，变量 b调用了  group方法将两个  group放入了自己的字段中，用于后期引导使用

  + (4)然后添加了一个    channel，其中参数一个  Class对象，引导类将通过这个     Class对象反射创建  ChannelFactory。然后添加了一些  TCP的参数。[说明：Channel的创建在  bind方法，可以  Debug下 bind ,会找  到  channel = channelFactory.newChannel(); ]  

  + (5)再添加了一个服务器专属的日志处理器  handler。

  + (6)再添加一个  SocketChannel（不是 ServerSocketChannel）的 handler。  (7)然后绑定端口并阻塞至连接成功。

  + (8)最后 main线程阻塞等待关闭。

  + (9) finally块中的代码将在服务器关闭时优雅关闭所有资源     

## 13.2 EventLoopGroup 的创建过程

+ 源码

```java
// 构造器
public NioEventLoopGroup(int nThreads) {
	this(nThreads, (Executor) null);
}
// 向下一直追踪到 MultithreadEventExecutorGroup 方法该方法用于构建 EventLoopGroup
/**
  * @param nThreads  使用的线程数，默认为  core *2 [可以追踪源码]
  * @param executor 执行器:如果传入 null,则采用 Netty默认的线程工厂和默认的执行器  ThreadPerTaskExecutor
  * @param chooserFactory  单例 new DefaultEventExecutorChooserFactory()
  *	@param args  args在创建执行器的时候传入固定参数
  */
protected MultithreadEventExecutorGroup(int nThreads, Executor executor,
                                            EventExecutorChooserFactory chooserFactory, Object... args) {
        if (nThreads <= 0) {
            throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", nThreads));
        }

        if (executor == null) { //如果传入的执行器是空的则采用默认的线程工厂和默认的执行器
            executor = new ThreadPerTaskExecutor(newDefaultThreadFactory());
        }
		//创建指定线程数的执行器数组
        children = new EventExecutor[nThreads];
		//初始化线程数组
        for (int i = 0; i < nThreads; i ++) {
            boolean success = false;
            try {
                //创建   new NioEventLoop
                children[i] = newChild(executor, args);
                success = true;
            } catch (Exception e) {
                // TODO: Think about if this is a good exception type
                throw new IllegalStateException("failed to create a child event loop", e);
            } finally {
                //如果创建失败，优雅关闭
                if (!success) {
                    for (int j = 0; j < i; j ++) {
                        children[j].shutdownGracefully();
                    }

                    for (int j = 0; j < i; j ++) {
                        EventExecutor e = children[j];
                        try {
                            while (!e.isTerminated()) {
                                e.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
                            }
                        } catch (InterruptedException interrupted) {
                            // Let the caller handle the interruption.
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            }
        }

        chooser = chooserFactory.newChooser(children);

        final FutureListener<Object> terminationListener = new FutureListener<Object>() {
            @Override
            public void operationComplete(Future<Object> future) throws Exception {
                if (terminatedChildren.incrementAndGet() == children.length) {
                    terminationFuture.setSuccess(null);
                }
            }
        };
		//为每一个单例线程池添加一个关闭监听器 监听器就是上面那个 匿名内部类
        for (EventExecutor e: children) {
            e.terminationFuture().addListener(terminationListener);
        }

        Set<EventExecutor> childrenSet = new LinkedHashSet<EventExecutor>(children.length);
    	//将所有的单例线程池添加到一个  HashSet中。
        Collections.addAll(childrenSet, children);
        readonlyChildren = Collections.unmodifiableSet(childrenSet);
    }
```

+ 说明

  + 1)如果  executor是  null，创建一个默认的 ThreadPerTaskExecutor，使用  Netty默认的线程工厂。  

  + 2)根据传入的线程数（CPU*2）创建一个线程池（单例线程池）数组。 

  + 3)循环填充数组中的元素。如果异常，则关闭所有的单例线程池。  
  + 4)根据线程选择工厂创建一个线程选择器。  

  +  5)为每一个单例线程池添加一个关闭监听器。  

  + 6)将所有的单例线程池添加到一个  HashSet中。  

## 13.3   ServerBootstrap创建和构造过程  

+ ServerBootstrap是个空构造，但是有默认的成员变量

```java
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ServerBootstrap.class);

    private final Map<ChannelOption<?>, Object> childOptions = new LinkedHashMap<ChannelOption<?>, Object>();
    private final Map<AttributeKey<?>, Object> childAttrs = new LinkedHashMap<AttributeKey<?>, Object>();
    private final ServerBootstrapConfig config = new ServerBootstrapConfig(this);
    private volatile EventLoopGroup childGroup;
    private volatile ChannelHandler childHandler;

    public ServerBootstrap() { }
```

+ 说明：
  + 1)链式调用：group方法，将  boss和 worker传入，boss赋值给  parentGroup属性,worker赋值给  childGroup属性  
  + 2) channel方法传入  NioServerSocketChannel  class对象。会根据这个  class创建  channel对象。  
  + 3) option方法传入  TCP参数，放在一个 LinkedHashMap中。  
  + 4) handler方法传入一个  handler中，这个  hanlder只专属于  ServerSocketChannel而不是  SocketChannel  
  + 5) childHandler传入一个  hanlder，这个  handler将会在每个客户端连接的时候调用。供  SocketChannel使用  
  +  

## 13.4 绑定端口的分析

+ 服务器就是在这个 bind方法里启动完成的
+ bind方法代码,追踪到创建了一个端口对象，并做了一些空判断，核心代码

```java
	public ChannelFuture bind(SocketAddress localAddress) {
        validate();
        if (localAddress == null) {
            throw new NullPointerException("localAddress");
        }
        return doBind(localAddress);
    }
```

+ doBind 方法  doBind源码剖析,核心是两个方法   initAndRegister和   doBind0

```java
	private ChannelFuture doBind(final SocketAddress localAddress) {
        final ChannelFuture regFuture = initAndRegister();
        final Channel channel = regFuture.channel();
        if (regFuture.cause() != null) {
            return regFuture;
        }

        if (regFuture.isDone()) {
            
            ChannelPromise promise = channel.newPromise();
            //============================================
            //说明:执行  doBind0方法，完成对端口的绑定
            //============================================
            doBind0(regFuture, channel, localAddress, promise);
            return promise;
        } else {
            // Registration future is almost always fulfilled already, but just in case it's not.
            final PendingRegistrationPromise promise = new PendingRegistrationPromise(channel);
            regFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Throwable cause = future.cause();
                    if (cause != null) {
                        // Registration on the EventLoop failed so fail the ChannelPromise directly to not cause an
                        // IllegalStateException once we try to access the EventLoop of the Channel.
                        promise.setFailure(cause);
                    } else {
                        // Registration was successful, so set the correct executor to use.
                        // See https://github.com/netty/netty/issues/2586
                        promise.registered();

                        doBind0(regFuture, channel, localAddress, promise);
                    }
                }
            });
            return promise;
        }
    }
```

+ initAndRegister 方法

```java
final ChannelFuture initAndRegister() {
        Channel channel = null;
        try {
            //说明：    channelFactory.newChannel()方法的作用通过             ServerBootstrap的通道工厂反射创建一个NioServerSocketChannel,具体追踪源码可以得到下面结论
			//(1)通过   NIO的   SelectorProvider的    openServerSocketChannel方法得到  JDK的    channel。目的是让  Netty包装   JDK的   channel。
			//(2)创建了一个唯一的   ChannelId，创建了一个  NioMessageUnsafe，用于操作消息，创建了一个  DefaultChannelPipeline管道，是个双向链表结构，用于过滤所有的进出的消息。
			//(3)创建了一个   NioServerSocketChannelConfig对象，用于对外展示一些配置。channel = channelFactory.newChannel();//NioServerSocketChannel
			
            channel = channelFactory.newChannel();
            //说明：init初始化这个   NioServerSocketChannel,具体追踪源码可以得到如下结论
			//(1) init方法，这是个抽象方法  (AbstractBootstrap类的)，由    ServerBootstrap实现（可以追一下源码  //setChannelOptions(channel, options, logger);）。
			//(2)设置   NioServerSocketChannel的    TCP属性。
			//(3)由于   LinkedHashMap是非线程安全的，使用同步进行处理。
			//(4)对   NioServerSocketChannel的    ChannelPipeline添加   ChannelInitializer处理器。
			//(5)可以看出，   init的方法的核心作用在和    ChannelPipeline相关。
			//(6)从    NioServerSocketChannel的初始化过程中，我们知道，  pipeline是一个双向链表，并且，他本身就初始化了  head和    tail，这里调用了他的  addLast方法，也就是将整个    handler插入到    tail的前面，因为  tail永远会在后面，需要做一些系统的固定工作。
            init(channel);
        } catch (Throwable t) {
            if (channel != null) {
                channel.unsafe().closeForcibly();
                return new DefaultChannelPromise(channel, GlobalEventExecutor.INSTANCE).setFailure(t);
            }
            return new DefaultChannelPromise(new FailedChannel(), GlobalEventExecutor.INSTANCE).setFailure(t);
        }

        ChannelFuture regFuture = config().group().register(channel);
        if (regFuture.cause() != null) {
            if (channel.isRegistered()) {
                channel.close();
            } else {
                channel.unsafe().closeForcibly();
            }
        }
        return regFuture;
    }
```

+ 说明：  
  + 1)基本说明：  initAndRegister()初始化  NioServerSocketChannel通道并注册各个  handler，返回一个  future  
  + 2)通过  ServerBootstrap的通道工厂反射创建一个  NioServerSocketChannel。  
  + 3) init初始化这个  NioServerSocketChannel。  
  + 4) config().group().register(channel)通过  ServerBootstrap的  bossGroup注册  NioServerSocketChannel。  
  + 5)最后，返回这个异步执行的占位符即  regFuture。  

+ addLast 方法 再 init 方法中被调用了

```java
@Override
    public final ChannelPipeline addLast(EventExecutorGroup group, String name, ChannelHandler handler) {
        final AbstractChannelHandlerContext newCtx;
        synchronized (this) {
            checkMultiplicity(handler);

            newCtx = newContext(group, filterName(name, handler), handler);

            addLast0(newCtx);
            if (!registered) {
                newCtx.setAddPending();
                callHandlerCallbackLater(newCtx, true);
                return this;
            }

            EventExecutor executor = newCtx.executor();
            if (!executor.inEventLoop()) {
                newCtx.setAddPending();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        callHandlerAdded0(newCtx);
                    }
                });
                return this;
            }
        }
        callHandlerAdded0(newCtx);
        return this;
    }
```

+ 说明：  
  + 1) addLast方法，在 DefaultChannelPipeline类中  
  + 2) addLast方法这就是 pipeline方法的核心  
  + 3)检查该  handler是否符合标准。  
  + 4)创建一个    AbstractChannelHandlerContext对象，这里说一下，      ChannelHandlerContext 对象是  ChannelHandler和  ChannelPipeline之间的关联，每当有    ChannelHandler添加到   Pipeline中时，都会创建  Context。Context的主要功能是管理他所关联的  Handler和同一个  Pipeline中的其他  Handler之间的交互。  
  + 5)将  Context添加到链表中。也就是追加到  tail节点的前面。  
  + 6)最后，同步或者异步或者晚点异步的调用  callHandlerAdded0方法

+ boBind0 方法的执行

~~~java
private static void doBind0(
            final ChannelFuture regFuture, final Channel channel,
            final SocketAddress localAddress, final ChannelPromise  ) {
        channel.eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                if (regFuture.isSuccess()) {
                    // 这个方法将数据进行绑定
                    channel.bind(localAddress, promise).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    promise.setFailure(regFuture.cause());
                }
            }
        });
    }
~~~

+  说明：  
  + 1)该方法的参数为  initAndRegister的  future，NioServerSocketChannel，端口地址，NioServerSocketChannel的promise  

+ 真正的绑定一个 ServerSocketChannel 函数

```java
public ServerSocketChannel bind(SocketAddress var1, int var2) throws IOException {
        synchronized(this.lock) {
            if (!this.isOpen()) {
                throw new ClosedChannelException();
            } else if (this.isBound()) {
                throw new AlreadyBoundException();
            } else {
                InetSocketAddress var4 = var1 == null ? new InetSocketAddress(0) : Net.checkAddress(var1);
                SecurityManager var5 = System.getSecurityManager();
                if (var5 != null) {
                    var5.checkListen(var4.getPort());
                }

                NetHooks.beforeTcpBind(this.fd, var4.getAddress(), var4.getPort());
                Net.bind(this.fd, var4.getAddress(), var4.getPort());
                Net.listen(this.fd, var2 < 1 ? 50 : var2);
                synchronized(this.stateLock) {
                    this.localAddress = Net.localAddress(this.fd);
                }

                return this;
            }
        }
    }
```

+ 这就是 NioEventLoop 中的循环处理方法 循环接收请求进行处理

~~~java
@Override
    protected void run() {
        for (;;) {
            try {
                switch (selectStrategy.calculateStrategy(selectNowSupplier, hasTasks())) {
                    case SelectStrategy.CONTINUE:
                        continue;
                    case SelectStrategy.SELECT:
                        select(wakenUp.getAndSet(false));

                        // 'wakenUp.compareAndSet(false, true)' is always evaluated
                        // before calling 'selector.wakeup()' to reduce the wake-up
                        // overhead. (Selector.wakeup() is an expensive operation.)
                        //
                        // However, there is a race condition in this approach.
                        // The race condition is triggered when 'wakenUp' is set to
                        // true too early.
                        //
                        // 'wakenUp' is set to true too early if:
                        // 1) Selector is waken up between 'wakenUp.set(false)' and
                        //    'selector.select(...)'. (BAD)
                        // 2) Selector is waken up between 'selector.select(...)' and
                        //    'if (wakenUp.get()) { ... }'. (OK)
                        //
                        // In the first case, 'wakenUp' is set to true and the
                        // following 'selector.select(...)' will wake up immediately.
                        // Until 'wakenUp' is set to false again in the next round,
                        // 'wakenUp.compareAndSet(false, true)' will fail, and therefore
                        // any attempt to wake up the Selector will fail, too, causing
                        // the following 'selector.select(...)' call to block
                        // unnecessarily.
                        //
                        // To fix this problem, we wake up the selector again if wakenUp
                        // is true immediately after selector.select(...).
                        // It is inefficient in that it wakes up the selector for both
                        // the first case (BAD - wake-up required) and the second case
                        // (OK - no wake-up required).

                        if (wakenUp.get()) {
                            selector.wakeup();
                        }
                        // fall through
                    default:
                }

                cancelledKeys = 0;
                needsToSelectAgain = false;
                final int ioRatio = this.ioRatio;
                if (ioRatio == 100) {
                    try {
                        processSelectedKeys();
                    } finally {
                        // Ensure we always run tasks.
                        runAllTasks();
                    }
                } else {
                    final long ioStartTime = System.nanoTime();
                    try {
                        processSelectedKeys();
                    } finally {
                        // Ensure we always run tasks.
                        final long ioTime = System.nanoTime() - ioStartTime;
                        runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                    }
                }
            } catch (Throwable t) {
                handleLoopException(t);
            }
            // Always handle shutdown even if the loop processing threw an exception.
            try {
                if (isShuttingDown()) {
                    closeAll();
                    if (confirmShutdown()) {
                        return;
                    }
                }
            } catch (Throwable t) {
                handleLoopException(t);
            }
        }
    }
~~~

## 13.5 netty 接受请求过程的源码剖析

+ 服务器端启动后肯定要接受客户端请求并返回客户端想要的信息的，下面源码分析 netty 在启动之后是如何接受客户端请求的

  

### 13.5.1 EventLoop的使用方式 这个循环中包含的3件事

+ EventLoop的作用是一个死循环，而这个循环中做  3件事情：  

  + 1)有条件的等待   Nio事件。  
  +  2)处理   Nio事件。  
  +  3)处理消息队列中的任务。  

  + 4)仍用前面的项目来分析：进入到   NioEventLoop源码中后，在 private void  processSelectedKey(SelectionKey k, 
  + 5)  AbstractNioChannel ch)方法开始调试最终我们要分析到   AbstractNioChannel的  doBeginRead方法，当到这  个方法时，针对于这个客户端的连接就完成了，接下来就可以监听读事件了  

### 13.5.2 源码解析

+ 1,NioEventLoop 的方法如下方法 processSelectedKey

>   if ((readyOps  & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0 || readyOps == 0)  {  
>
> ​		// readOps = 16
>
>   		unsafe.read();  //断点位置  
>
> }

+ 2.执行浏览器   http://localhost:8007/,客户端发出请求  

+ 3.从的断点我们可以看到，  readyOps是  16，也就是  Accept事件。说明浏览器的请求已经进来了。  

+ 4.这个  unsafe是  boss线程中  NioServerSocketChannel的 AbstractNioMessageChannel$NioMessageUnsafe对象。我们进入到  AbstractNioMessageChannel$NioMessageUnsafe的 read方法中  

+ 5.read 方法源码解析

  ```java
  public void read() {
      		// 判断当前线程的 eventLoop 是否是本对象的 eventLoop
              assert eventLoop().inEventLoop();
              final ChannelConfig config = config();
              final ChannelPipeline pipeline = pipeline();
              final RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
              allocHandle.reset(config);
  
              boolean closed = false;
              Throwable exception = null;
              try {
                  try {
                      do {
                          int localRead = doReadMessages(readBuf);
                          if (localRead == 0) {
                              break;
                          }
                          if (localRead < 0) {
                              closed = true;
                              break;
                          }
  
                          allocHandle.incMessagesRead(localRead);
                      } while (allocHandle.continueReading());
                  } catch (Throwable t) {
                      exception = t;
                  }
  
                  int size = readBuf.size();
                  for (int i = 0; i < size; i ++) {
                      readPending = false;
                      pipeline.fireChannelRead(readBuf.get(i));
                  }
                  readBuf.clear();
                  allocHandle.readComplete();
                  pipeline.fireChannelReadComplete();
  
                  if (exception != null) {
                      closed = closeOnReadError(exception);
  
                      pipeline.fireExceptionCaught(exception);
                  }
  
                  if (closed) {
                      inputShutdown = true;
                      if (isOpen()) {
                          close(voidPromise());
                      }
                  }
              } finally {
                  if (!readPending && !config.isAutoRead()) {
                      removeReadOp();
                  }
              }
          }
      }
  ```
  
  +   说明：  
  
    + 1)检查该  eventloop线程是否是当前线程。assert  eventLoop().inEventLoop()  
  + 2)执行  doReadMessages方法，并传入一个  readBuf变量，这个变量是一个  List，也就是容器。  
    
    + 3)循环容器，执行  pipeline.fireChannelRead(readBuf.get(i));  
  + 4) doReadMessages是读取  boss线程中的  NioServerSocketChannel接受到的请求。并把这些请求放进容器,  一会我们  debug下 doReadMessages方法.  
+ 5)循环遍历容器中的所有请求，调用   pipeline的  fireChannelRead方法，用于处理这些接受的请求或者其  他事件，在 read方法中，循环调用   ServerSocket的  pipeline的  fireChannelRead方法,开始执行管道中的  handler的  ChannelRead方法(debug进入)  
  
+ doReadMessages 方法
  
    ```java
    // 获取到原生的 SocketChannel 对象	
  SocketChannel ch = SocketUtils.accept(javaChannel());
    
            try {
                if (ch != null) {
                    // 将其封装为 NioSocketChannel 对象 并添加到 buf 中
                    buf.add(new NioSocketChannel(this, ch));
                    return 1;
                }
            } 
    }
    说明：
    1)通过工具类，调用    NioServerSocketChannel内部封装的   serverSocketChannel的    accept方法，这是   Nio做
    法。
    2)获取到一个   JDK的    SocketChannel，然后，使用 NioSocketChannel进行封装。最后添加到容器中
    3)这样容器  buf中就有了   NioSocketChannel [如果有兴趣可以追一下 NioSocketChannel是如何创建的,我就不追
    了]
  ```
  
  + fireChannelRead 方法
  
    ```
    1)前面分析   doReadMessages方法的作用是通过    ServerSocket的   accept方法获取到    Tcp连接，然后封装成
    Netty的   NioSocketChannel对象。最后添加到容器中
    2)在  read方法中，循环调用    ServerSocket的   pipeline的    fireChannelRead方法,开始执行管道中的       handler
    的  ChannelRead方法(debug进入)
    3)经过   dubug (多次)，可以看到会反复执行多个    handler的  ChannelRead ,我们知道， pipeline里面又    4个handler，分别是   Head，LoggingHandler，ServerBootstrapAcceptor，Tail。
    4)我们重点看看   ServerBootstrapAcceptor。debug之后，断点会进入到    ServerBootstrapAcceptor中来。我们来
    看看  ServerBootstrapAcceptor的   channelRead方法(要多次   debug才可以)
    5)  channelRead方法 
    ```
  
  + ServerBootstrap 中的 channelRead 方法
  
  ```java
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
      final Channel child = (Channel) msg;
  
      child.pipeline().addLast(childHandler);
  
      setChannelOptions(child, childOptions, logger);
  
      for (Entry<AttributeKey<?>, Object> e: childAttrs) {
          child.attr((AttributeKey<Object>) e.getKey()).set(e.getValue());
      }
  
      try {
          // 进入 register 方法
          childGroup.register(child).addListener(new ChannelFutureListener() {
              @Override
              public void operationComplete(ChannelFuture future) throws Exception {
                  if (!future.isSuccess()) {
                      forceClose(child, future.cause());
                  }
              }
          });
      } catch (Throwable t) {
          forceClose(child, t);
      }
  }
  说明：
      1)  msg强转成   Channel，实际上就是   NioSocketChannel。
      2)添加   NioSocketChannel的    pipeline的    handler，就是我们   main方法里面设置的    childHandler方法里
      的。
      3)设置   NioSocketChannel的各种属性。
      4)将该   NioSocketChannel注册到    childGroup中的一个   EventLoop上，并添加一个监听器。
      5)这个   childGroup就是我们    main方法创建的数组     workerGroup。
  
  ```
  
  + 进入  register方法查看(步步追踪会到)
  
  + 最终会调用  doBeginRead方法，也就是  AbstractNioChannel类的方法  
  
    ```java
    @Override
    protected void doBeginRead() throws Exception {
        // Channel.read() or ChannelHandlerContext.read() was called
        final SelectionKey selectionKey = this.selectionKey; //断点
        if (!selectionKey.isValid()) {
        return;
        }
        readPending = true;
        final int interestOps = selectionKey.interestOps();
        if ((interestOps & readInterestOp) == 0) {
        selectionKey.interestOps(interestOps | readInterestOp);
        }
    }
    
    ```
  
  +   10.这个地方调试时，请把前面的断点都去掉，然后启动服务器就会停止在  doBeginRead（需要先放过该断点，然  后浏览器请求，才能看到效果）  
  
  + 11.执行到这里时，针对于这个客户端的连接就完成了，接下来就可以监听读事件了  

### 13.5.3 Netty 接受请求过程梳理

+ 总体流程：接受连接  ----->创建一个新的 NioSocketChannel----------->注册到一个  worker  EventLoop上-------->  注册  selecot Read事件。  

+ 1)服务器轮询   Accept事件，获取事件后调用  unsafe的  read方法，这个  unsafe是  ServerSocket的内部类，该  方法内部由  2部分组成  

+ 2)  doReadMessages用于创建  NioSocketChannel对象，该对象包装  JDK的  Nio Channel客户端。该方法会像创  建 ServerSocketChanel类似创建相关的  pipeline，  unsafe，config  

+  3)随后执行执行   pipeline.fireChannelRead方法，并将自己绑定到一个  chooser选择器选择的  workerGroup中的  一个 EventLoop。并且注册一个  0，表示注册成功，但并没有注册读（1）事件  

## 13.6 Pipeline Handler HandlerContext 创建源码刨析

### 13.6.1 源码剖析目的

> Netty 中的 ChannelPipeline ，ChannelHandler 和 ChannelHandlerContext 是非常核心的组件，我们从源码来分析 Netty 是如何设计这三个核心组件的，并分析是如何创建和协调工作的

### 13.6.2 源码剖析

+ ChannelPipeline | ChannelHandler | ChannelHandlerContext 介绍

  + 三者关系

  > 1，每当 ServerSocket 创建一个新的连接，就会创建一个 Socket 对应的就是目标客户端
  >
  > 2，每一个新创建的 Socket 都将分配一个全新的 ChannelPipeline  （以下简称 pipeline）
  >
  > 3，每一个 ChannelPipeline 内部都含有多个  channelHandlerContext （以下简称 Context）
  >
  > 4，他们一起组成了双向链表，这些 Context 用于包装我们调用 addLast 方法时添加的 ChannelHandler （以下简称 handler）

  ![image-20200106203541895](D:\temp\xmind\netty 学习\images\image-20200106203541895.png)

  >   1)上图中：ChannelSocket和    ChannelPipeline是一对一的关联关系，而  pipeline内部的多个  Context形成了链  表，Context只是对  Handler的封装。  
  >
  >   2)当一个请求进来的时候，会进入   Socket对应的  pipeline，并经过 pipeline所有的  handler，对，就是设计模式  中的过滤器模式。  

+ ChannelPipeline 作用及设计

  + Pipeline 的接口设计

  ![image-20200106205509414](D:\temp\xmind\netty 学习\images\image-20200106205509414.png)

  > 可以看到 该接口继承了 inBound，outBound ， Iterable 接口，表示他可以调用数据出站的方法和入站的方法，同时也能遍历内部的链表，看韩他的几个代表性的方法，基本上都是针对 Handler 链表的插入。追加，删除，替换操作，类似是一个 LinkedList。同时，也能返回 Channel （也就是 socket）

  + 在 Pipeline 的接口文档上，提供了一幅图

  ![image-20200107080644460](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20200107080644460.png)

  ```
  对上图的解释说明：
  *这是一个handler的list，handler用于处理或拦截入站事件和出站事件，pipeline实现了过滤器的高级形
  式，以便用户控制事件如何处理以及handler在pipeline中如何交互。
  *上图描述了一个典型的handler在pipeline中处理I/O事件的方式，IO事件由inboundHandler或者
  outBoundHandler处理，并通过调用ChannelHandlerContext.fireChannelRead方法转发给其最近的处理程序。
  
  *入站事件由入站处理程序以自下而上的方向处理，如图所示。入站处理程序通常处理由图底部的I/O线程生成
  入站数据。入站数据通常从如SocketChannel.read(ByteBuffer)获取。
  *通常一个pipeline有多个handler，例如，一个典型的服务器在每个通道的管道中都会有以下处理程序
  协议解码器-将二进制数据转换为Java对象。
  协议编码器-将Java对象转换为二进制数据。
  业务逻辑处理程序-执行实际业务逻辑（例如数据库访问）
  *你的业务程序不能将线程阻塞，会影响IO的速度，进而影响整个Netty程序的性能。如果你的业务程序很快，
  就可以放在IO线程中，反之，你需要异步执行。或者在添加handler的时候添加一个线程池，例如：
  //下面这个任务执行的时候，将不会阻塞IO线程，执行的线程来自group线程池
  pipeline.addLast（group，“handler”，newMyBusinessLogicHandler（））;
  
  ```

### 13.6.3 ChannelHandler作用及设计

+ 源码

```java
//当把  ChannelHandler添加到   pipeline时被调用
void handlerAdded(ChannelHandlerContext ctx) throws Exception;
//当从  pipeline中移除时调用
void handlerRemoved(ChannelHandlerContext ctx) throws Exception;

//当处理过程中在   pipeline发生异常时调用
@Deprecated
void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;

2)  ChannelHandler的作用就是处理  IO事件或拦截  IO事件，并将其转发给下一个处理程序  ChannelHandler。  Handler处理事件时分入站和出站的，两个方向的操作都是不同的，因此，Netty定义了两个子接口继承  ChannelHandler
```

+ ChannelInboundHandler入站事件接口

  ![image-20200107090437490](D:\temp\xmind\netty 学习\images\image-20200107090437490.png)

  + channelActive用于当  Channel处于活动状态时被调用； 
+   channelRead当从 Channel读取数据时被调用等等方法。  
  
+ 程序员需要重写一些方法，当发生关注的事件，需要在方法中实现我们的业务逻辑，因为当事件发生时，Netty会回调对应的方法。
  
+ ChannelOutboundHandler出站事件接口 

  + bind方法，当请求将  Channel绑定到本地地址时调用 
  + close方法，当请求关闭  Channel时调用等等 
  + 出站操作都是一些连接和写出数据类似的方法。  

+ ChannelDuplexHandler处理出站和入站事件

  ![image-20200107090654526](D:\temp\xmind\netty 学习\images\image-20200107090654526.png)

  * ChannelDuplexHandler间接实现了入站接口并直接实现了出站接口。  
  * 是一个通用的能够同时处理入站事件和出站事件的类。  

+ ChannelHandlerContext作用及设计

  ![image-20200107091728274](D:\temp\xmind\netty 学习\images\image-20200107091728274.png)

  +  ChannelHandlerContext继承了出站方法调用接口和入站方法调用接口  1)
  +  ChannelOutboundInvoker和  ChannelInboundInvoker部分源码  
  + ChannelHandlerContext 同时继承了 ChannelOutboundInvoker 和 ChannelInboundInvoker 两个接口可以同时处理 入站和出站的双向的 Handler 

### 13.6.4    ChannelPipeline  | ChannelHandler | ChannelHandlerContext创建过程  

+ 分为三个步骤看到

  + 任何一个  ChannelSocket创建的同时都会创建一个   pipeline。  

  + 当用户或系统内部调用  pipeline的  add 方法添加  handler时，都会创建一个包装这  handler的  Context。  
  + 这些  Context在  pipeline中组成了双向链表。  

+ Socket 创建的时候创建 pipeline

  ```java
  protected AbstractChannel(Channel parent, ChannelId id) {
      this.parent = parent;
      this.id = id;
      unsafe = newUnsafe();
      pipeline = newChannelPipeline();
  }
  ```

  + 进入 newChannelPipeline 方法

  ```java
  protected DefaultChannelPipeline(Channel channel) {
      this.channel = ObjectUtil.checkNotNull(channel, "channel");
      succeededFuture = new SucceededChannelFuture(channel, null);
      voidPromise =  new VoidChannelPromise(channel, true);
  
      tail = new TailContext(this); // 创建 tail
      head = new HeadContext(this); // 创建 head
  
      head.next = tail;
      tail.prev = head;
  }
  ```

  + 说明：  
    + 1）将channel赋值给channel字段，用于pipeline操作channel。  
    +   2）创建一个future和promise，用于异步回调使用。  
    + 3）创建一个inbound的tailContext，创建一个既是inbound类型又是outbound类型的headContext.  
    + 4）最后，将两个Context互相连接，形成双向链表。  
    + 5）tailContext和HeadContext非常的重要，所有pipeline中的事件都会流经他们，

+ 在 add* 处理器的时候创建 Context**

  ```java
  @Override
  public final ChannelPipeline addLast(EventExecutorGroup executor, ChannelHandler... handlers) {
      if (handlers == null) {
          throw new NullPointerException("handlers");
      }
  
      for (ChannelHandler h: handlers) {
          if (h == null) {
              break;
          }
          addLast(executor, null, h);
      }
  
      return this;
  }
  
  
  @Override
      public final ChannelPipeline addLast(EventExecutorGroup group, String name, ChannelHandler handler) {
          final AbstractChannelHandlerContext newCtx;
          synchronized (this) {
              checkMultiplicity(handler);
  
              newCtx = newContext(group, filterName(name, handler), handler);
  
              addLast0(newCtx);
  
              // If the registered is false it means that the channel was not registered on an eventloop yet.
              // In this case we add the context to the pipeline and add a task that will call
              // ChannelHandler.handlerAdded(...) once the channel is registered.
              if (!registered) {
                  newCtx.setAddPending();
                  callHandlerCallbackLater(newCtx, true);
                  return this;
              }
  
              EventExecutor executor = newCtx.executor();
              if (!executor.inEventLoop()) {
                  newCtx.setAddPending();
                  executor.execute(new Runnable() {
                      @Override
                      public void run() {
                          callHandlerAdded0(newCtx);
                      }
                  });
                  return this;
              }
          }
          callHandlerAdded0(newCtx);
          return this;
      }
  说明
  1)  pipeline添加   handler，参数是线程池，name是   null， handler是我们或者系统传入的   handler。Netty为了防止
  多个线程导致安全问题，同步了这段代码，步骤如下：
  2)检查这个     handler实例是否是共享的，如果不是，并且已经被别的   pipeline使用了，则抛出异常。
  3)调用     newContext(group, filterName(name, handler), handler)方法，创建一个   Context。从这里可以看出来了，
  每次添加一个  handler都会创建一个关联   Context。
  4)调用   addLast方法，将    Context追加到链表中。
  5)如果这个通道还没有注册到   selecor上，就将这个    Context添加到这个   pipeline的待办任务中。当注册好了以
  后，就会调用  callHandlerAdded0方法（默认是什么都不做，用户可以实现这个方法）。
  6)到这里，针对三对象创建过程，了解的差不多了，和最初说的一样，每当创建    ChannelSocket的时候都会创建
  一个绑定的  pipeline，一对一的关系，创建 pipeline的时候也会创建    tail节点和   head节点，形成最初的链表。tail
  是入站  inbound类型的   handler， head既是   inbound也是    outbound类型的   handler。在调用  pipeline的    addLast
  方法的时候，会根据给定的  handler创建一个   Context，然后，将这个  Context插入到链表的尾端（tail前面）。
  到此就  OK了
  ```

### 13.6.5    Pipeline Handler HandlerContext创建过程梳理  

+ 1)每当创建    ChannelSocket的时候都会创建一个绑定的  pipeline，一对一的关系，创建 pipeline的时候也会创建  tail节点和  head节点，形成最初的链表。  

+ 2)在调用   pipeline的  addLast方法的时候，会根据给定的  handler创建一个  Context，然后，将这个 Context插  入到链表的尾端（tail前面）。  

+ 3)  Context包装  handler，多个 Context在  pipeline中形成了双向链表  
+ 4)入站方向叫   inbound，由  head节点开始，出站方法叫  outbound，由  tail节点开始  

## 13.7 ChannelPipeline调度  handler的源码剖析

### 13.7.1 源码解析目的

>   1)当一个请求进来的时候，ChannelPipeline是如何调用内部的这些   handler的呢？我们一起来分析下。  
>
>   2)首先，当一个请求进来的时候，会第一个调用   pipeline的相关方法，如果是入站事件，这些方法由   fire开头，  表示开始管道的流动。让后面的 handler继续处理  

### 13.7.2 源码剖析

```java
@Override
public final ChannelPipeline fireChannelActive() {
    AbstractChannelHandlerContext.invokeChannelActive(head);
    return this;
}

@Override
public final ChannelPipeline fireChannelInactive() {
    AbstractChannelHandlerContext.invokeChannelInactive(head);
    return this;
}

@Override
public final ChannelPipeline fireExceptionCaught(Throwable cause) {
    AbstractChannelHandlerContext.invokeExceptionCaught(head, cause);
    return this;
}

@Override
public final ChannelPipeline fireUserEventTriggered(Object event) {
    AbstractChannelHandlerContext.invokeUserEventTriggered(head, event);
    return this;
}

@Override
public final ChannelPipeline fireChannelRead(Object msg) {
    AbstractChannelHandlerContext.invokeChannelRead(head, msg);
    return this;
}

@Override
public final ChannelPipeline fireChannelReadComplete() {
    AbstractChannelHandlerContext.invokeChannelReadComplete(head);
    return this;
}

@Override
public final ChannelPipeline fireChannelWritabilityChanged() {
    AbstractChannelHandlerContext.invokeChannelWritabilityChanged(head);
    return this;
}

说明：
可以看出来，这些方法都是  inbound的方法，也就是入站事件，调用静态方法传入的也是     inbound的类型    head
handler。这些静态方法则会调用   head的   ChannelInboundInvoker接口的方法，再然后调用     handler的真正方
法

    
    
@Override
    public final ChannelFuture bind(SocketAddress localAddress) {
        return tail.bind(localAddress);
    }

    @Override
    public final ChannelFuture connect(SocketAddress remoteAddress) {
        return tail.connect(remoteAddress);
    }

    @Override
    public final ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return tail.connect(remoteAddress, localAddress);
    }

    @Override
    public final ChannelFuture disconnect() {
        return tail.disconnect();
    }

    @Override
    public final ChannelFuture close() {
        return tail.close();
    }

    @Override
    public final ChannelFuture deregister() {
        return tail.deregister();
    }

    @Override
    public final ChannelPipeline flush() {
        tail.flush();
        return this;
    }

    @Override
    public final ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
        return tail.bind(localAddress, promise);
    }

    @Override
    public final ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
        return tail.connect(remoteAddress, promise);
    }

    @Override
    public final ChannelFuture connect(
            SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        return tail.connect(remoteAddress, localAddress, promise);
    }

    @Override
    public final ChannelFuture disconnect(ChannelPromise promise) {
        return tail.disconnect(promise);
    }

说明：
1)这些都是出站的实现，但是调用的是   outbound类型的    tail handler来进行处理，因为这些都是   outbound事
件。
2)出站是   tail开始，入站从    head开始。因为出站是从内部向外面写，从  tail开始，能够让前面的    handler进
行处理，防止  handler被遗漏，比如编码。反之，入站当然是从   head往内部输入，让后面的    handler能够处理这
些输入的数据。比如解码。因此虽然  head也实现了   outbound接口，但不是从   head开始执行出站任务

```

![image-20200108085814616](D:\temp\xmind\netty 学习\images\image-20200108085814616.png)

+ 说明：  
  + 1)pipeline首先会调用Context的静态方法fireXXX，并传入Context  

  + 2)然后，静态方法调用Context的invoker方法，而invoker方法内部会调用该Context所包含的  Handler的真正的XXX方法，调用结束后，如果还需要继续向后传递，就调用Context的fireXXX2方法，循环往复。

+ ChannelPipeline 调度 Handler 梳理
  + 1)  Context包装  handler，多个 Context在  pipeline中形成了双向链表，入站方向叫  inbound，由 head节点开始， 出站方法叫 outbound，由  tail节点开始。  
  + 2)而节点中间的传递通过   AbstractChannelHandlerContext类内部的  fire系列方法，找到当前节点的下一个节点  不断的循环传播。是一个过滤器形式完成对  handler的调度  

## 13.8   Netty心跳(heartbeat)服务源码剖析  

### 13.8.1  源码剖析目的  

> Netty作为一个网络框架，提供了诸多功能，比如编码解码等，  Netty还提供了非常重要的一个服务-----心跳  机制  heartbeat。通过心跳检查对方是否有效，这是 RPC框架中是必不可少的功能。下面我们分析一下  Netty 内部心  跳服务源码实现。

### 13.8.2 源码剖析

>   Netty提供了  IdleStateHandler，ReadTimeoutHandler，WriteTimeoutHandler三个   Handler检测连接的有效性，  
>
> 重点分析 IdleStateHandler 

![image-20200110092405959](D:\temp\xmind\netty 学习\images\image-20200110092405959.png)

### 13.8.3 IdleStateHandler 源码分析

+ 该 Handler 包含 4 个属性
  + private final  boolean observeOutput; //是否考虑出站时较慢的情况。默认值是  false  
  + private final  long readerIdleTimeNanos;//读事件空闲时间，0则禁用事件  
  + private final long writerIdleTimeNanos;//写事件空闲时间，0则禁用事件
  + private final  long allIdleTimeNanos;//读或写空闲时间，0则禁用事件  

+ handlerAdded 方法

  +   当该 handler被添加到  pipeline中时，则调用  initialize方法  

  ```java
  private void initialize(ChannelHandlerContext ctx) {
      // Avoid the case where destroy() is called before scheduling timeouts.
      // See: https://github.com/netty/netty/issues/143
      switch (state) {
      case 1:
      case 2:
          return;
      }
  
      state = 1;
      initOutputChanged(ctx);
  
      lastReadTime = lastWriteTime = ticksInNanos();
      if (readerIdleTimeNanos > 0) {
          readerIdleTimeout = schedule(ctx, new ReaderIdleTimeoutTask(ctx),
                  readerIdleTimeNanos, TimeUnit.NANOSECONDS);
      }
      if (writerIdleTimeNanos > 0) {
          writerIdleTimeout = schedule(ctx, new WriterIdleTimeoutTask(ctx),
                  writerIdleTimeNanos, TimeUnit.NANOSECONDS);
      }
      if (allIdleTimeNanos > 0) {
          allIdleTimeout = schedule(ctx, new AllIdleTimeoutTask(ctx),
                  allIdleTimeNanos, TimeUnit.NANOSECONDS);
      }
  }
  只要给定的参数大于 0，就创建一个定时任务，每个事件都创建。同时，将 state状态设置为    1，防止重复初始化。
  调用  initOutputChanged方法，初始化“监控出站数据属性”
  
  ```

+ 该类内部的  3个定时任务类   都有一个共同的 父类 AbstractIdleTask

```java
private abstract static class AbstractIdleTask implements Runnable {

    private final ChannelHandlerContext ctx;

    AbstractIdleTask(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        if (!ctx.channel().isOpen()) {
            return;
        }

        run(ctx);
    }

    protected abstract void run(ChannelHandlerContext ctx);
}
说明：当通道关闭了，就不执行任务了。反之，执行子类的   run方法
```

+ ReaderIdleTimeoutTask 中的 run 方法

```java
@Override
protected void run(ChannelHandlerContext ctx) {
    long nextDelay = readerIdleTimeNanos;
    if (!reading) {
        nextDelay -= ticksInNanos() - lastReadTime;
    }

    if (nextDelay <= 0) {
        // Reader is idle - set a new timeout and notify the callback.
        readerIdleTimeout = schedule(ctx, this, readerIdleTimeNanos, TimeUnit.NANOSECONDS);

        boolean first = firstReaderIdleEvent;
        firstReaderIdleEvent = false;

        try {
            IdleStateEvent event = newIdleStateEvent(IdleState.READER_IDLE, first);
            channelIdle(ctx, event);
        } catch (Throwable t) {
            ctx.fireExceptionCaught(t);
        }
    } else {
        // Read occurred before the timeout - set a new timeout with shorter delay.
        readerIdleTimeout = schedule(ctx, this, nextDelay, TimeUnit.NANOSECONDS);
    }
}

1)得到用户设置的超时时间。
2)如果读取操作结束了（执行了   channelReadComplete方法设置），就用当前时间减去给定时间和最后一次读（执操作的时间行了  channelReadComplete方法设置），如果小于  0，就触发事件。反之，继续放入队列。间隔时间是新的计算时间。
3)触发的逻辑是：首先将任务再次放到队列，时间是刚开始设置的时间，返回一个    promise对象，用于做取消操作。然后，设置  first属性为   false，表示，下一次读取不再是第一次了，这个属性在   channelRead方法会被改成  true。
4)创建一个    IdleStateEvent类型的写事件对象，将此对象传递给用户的    UserEventTriggered方法。完成触发事件的操作。
5)总的来说，每次读取操作都会记录一个时间，定时任务时间到了，会计算当前时间和最后一次读的时间的间隔，如果间隔超过了设置的时间，就触发  UserEventTriggered方法。//前面介绍   IdleStateHandler说过,可以看一下
```

+ WriterIdleTimeoutTask 的 run 方法

```java
@Override
protected void run(ChannelHandlerContext ctx) {

    long lastWriteTime = IdleStateHandler.this.lastWriteTime;
    long nextDelay = writerIdleTimeNanos - (ticksInNanos() - lastWriteTime);
    if (nextDelay <= 0) {
        // Writer is idle - set a new timeout and notify the callback.
        writerIdleTimeout = schedule(ctx, this, writerIdleTimeNanos, TimeUnit.NANOSECONDS);

        boolean first = firstWriterIdleEvent;
        firstWriterIdleEvent = false;

        try {
            if (hasOutputChanged(ctx, first)) {
                return;
            }

            IdleStateEvent event = newIdleStateEvent(IdleState.WRITER_IDLE, first);
            channelIdle(ctx, event);
        } catch (Throwable t) {
            ctx.fireExceptionCaught(t);
        }
    } else {
        // Write occurred before the timeout - set a new timeout with shorter delay.
        writerIdleTimeout = schedule(ctx, this, nextDelay, TimeUnit.NANOSECONDS);
    }
}

说明：
写任务的  run代码逻辑基本和读任务的逻辑一样，唯一不同的就是有一个针对出站较慢数据的判断
hasOutputChanged
```

+ AllIdleTimeoutTask 读写 事件的 run方法

```java
@Override
protected void run(ChannelHandlerContext ctx) {

    long nextDelay = allIdleTimeNanos;
    if (!reading) {
        nextDelay -= ticksInNanos() - Math.max(lastReadTime, lastWriteTime);
    }
    if (nextDelay <= 0) {
        // Both reader and writer are idle - set a new timeout and
        // notify the callback.
        allIdleTimeout = schedule(ctx, this, allIdleTimeNanos, TimeUnit.NANOSECONDS);

        boolean first = firstAllIdleEvent;
        firstAllIdleEvent = false;

        try {
            if (hasOutputChanged(ctx, first)) {
                return;
            }

            IdleStateEvent event = newIdleStateEvent(IdleState.ALL_IDLE, first);
            channelIdle(ctx, event);
        } catch (Throwable t) {
            ctx.fireExceptionCaught(t);
        }
    } else {
        // Either read or write occurred before the timeout - set a new
        // timeout with shorter delay.
        allIdleTimeout = schedule(ctx, this, nextDelay, TimeUnit.NANOSECONDS);
    }
}


说明：
1)表示这个监控着所有的事件。当读写事件发生时，都会记录。代码逻辑和写事件的的基本一致：
2)需要大家注意的地方是
long nextDelay = allIdleTimeNanos;
    if (!reading) {
    //当前时间减去最后一次写或读的时间，若大于        0，说明超时了
    nextDelay -= ticksInNanos() - Math.max(lastReadTime, lastWriteTime);
}
3)这里的时间计算是取读写事件中的最大值来的。然后像写事件一样，判断是否发生了写的慢的情况
```

### 13.8.4 心跳源码分析小结

```
1)  IdleStateHandler可以实现心跳功能，当服务器和客户端没有任何读写交互时，并超过了给定的时间，则会触发用户  handler的   userEventTriggered方法。用户可以在这个方法中尝试向对方发送信息，如果发送失败，则关闭连接。

2)  IdleStateHandler的实现基于   EventLoop的定时任务，每次读写都会记录一个值，在定时任务运行的时候，通过计算当前时间和设置时间和上次事件发生时间的结果，来判断是否空闲。

3)内部有   3个定时任务，分别对应读事件，写事件，读写事件。通常用户监听读写事件就足够了。

4)同时，IdleStateHandler内部也考虑了一些极端情况：客户端接收缓慢，一次接收数据的速度超过了设置的空闲时间。Netty通过构造方法中的   observeOutput属性来决定是否对出站缓冲区的情况进行判断。

5)如果出站缓慢，Netty不认为这是空闲，也就不触发空闲事件。但第一次无论如何也是要触发的。因为第一次无法判断是出站缓慢还是空闲。当然，出站缓慢的话，可能造成 OOM , OOM比空闲的问题更大。

6)所以，当你的应用出现了内存溢出，OOM之类，并且写空闲极少发生（使用了    observeOutput为    true），那么就需要注意是不是数据出站速度过慢。

7)还有一个注意的地方：就是   ReadTimeoutHandler，它继承自    IdleStateHandler，当触发读空闲事件的时候，就触发  ctx.fireExceptionCaught方法，并传入一个   ReadTimeoutException，然后关闭  Socket。

8)而   WriteTimeoutHandler的实现不是基于    IdleStateHandler的，他的原理是，当调用   write方法的时候，会创建一个定时任务，任务内容是根据传入的  promise的完成情况来判断是否超出了写的时间。当定时任务根据指定时间开始运行，发现  promise的   isDone方法返回   false，表明还没有写完，说明超时了，则抛出异常。当  write方法完成后，会打断定时任务。
```

## 13.9 Netty核心组件  EventLoop源码剖析

### 13.9.1   源码剖析目的  

> Echo第一行代码就是：EventLoopGroup    bossGroup = new NioEventLoopGroup(1);下面分析其最核心的组件  EventLoop

### 13.9.2 NioEventLoop 的继承图

![image-20200110112045933](D:\temp\xmind\netty 学习\images\image-20200110112045933.png)

```
说明重点：
    1)  ScheduledExecutorService接口表示是一个定时任务接口，EventLoop可以接受定时任务。
    2)  EventLoop接口：Netty接口文档说明该接口作用：一旦      Channel注册了，就处理该   Channel对应的所有I/O操作。
    3)  SingleThreadEventExecutor表示这是一个单个线程的线程池
    4)  EventLoop是一个单例的线程池，里面含有一个死循环的线程不断的做着   3件事情：监听端口，处理端口事件，处理队列事件。每个  EventLoop都可以绑定多个   Channel，而每个  Channel始终只能由一个   EventLoop来处理
```

### 13.9.3  execute源码剖析 NioEventLoop

+ Execute 方法

```java
@Override
public void execute(Runnable task) {
    if (task == null) {
        throw new NullPointerException("task");
    }

    boolean inEventLoop = inEventLoop();
    if (inEventLoop) {
        addTask(task);
    } else {
        startThread();
        addTask(task);
        if (isShutdown() && removeTask(task)) {
            reject();
        }
    }

    if (!addTaskWakesUp && wakesUpForTask(task)) {
        wakeup(inEventLoop);
    }
}

说明:
    1)首先判断该   EventLoop的线程是否是当前线程，如果是，直接添加到任务队列中去，如果不是，则尝试启动线程（但由于线程是单个的，因此只能启动一次），随后再将任务添加到队列中去。
	2)如果线程已经停止，并且删除任务失败，则执行拒绝策略，默认是抛出异常。
	3)如果   addTaskWakesUp是    false，并且任务不是  NonWakeupRunnable类型的，就尝试唤醒    selector。这个时候，阻塞在  selecor的线程就会立即返回
	4)可以下断点来追踪

```

+ addTask 和 offerTask 方法

```java
protected void addTask(Runnable task) {
    if (task == null) {
        throw new NullPointerException("task");
    }
    if (!offerTask(task)) {
        reject(task);
    }
}

final boolean offerTask(Runnable task) {
      if (isShutdown()) {
          reject();
      }
      return taskQueue.offer(task);
}
```

### 13.9.4 NioEventLoop的父类  SingleThreadEventExecutor的  startThread方法

+   当执行  execute方法的时候，如果当前线程不是  EventLoop所属线程，则尝试启动线程，也就是  startThread方  法，dubug代码如下：  

```java
private void startThread() {
    if (state == ST_NOT_STARTED) {
        if (STATE_UPDATER.compareAndSet(this, ST_NOT_STARTED, ST_STARTED)) {
            try {
                doStartThread();
            } catch (Throwable cause) {
                STATE_UPDATER.set(this, ST_NOT_STARTED);
                PlatformDependent.throwException(cause);
            }
        }
    }
}

说明:
该方法首先判断是否启动过了，保证  EventLoop只有一个线程，如果没有启动过，则尝试使用   Cas将state状态改为  ST_STARTED，也就是已启动。然后调用 doStartThread方法。如果失败，则进行回滚

```





```java
private void doStartThread() {
    assert thread == null;
    executor.execute(new Runnable() {
        @Override
        public void run() {
            thread = Thread.currentThread();
            if (interrupted) {
                thread.interrupt();
            }

            boolean success = false;
            updateLastExecutionTime();
            try {
                SingleThreadEventExecutor.this.run();
                success = true;
            } catch (Throwable t) {
                logger.warn("Unexpected exception from an event executor: ", t);
            } finally {
                for (;;) {
                    int oldState = state;
                    if (oldState >= ST_SHUTTING_DOWN || STATE_UPDATER.compareAndSet(
                            SingleThreadEventExecutor.this, oldState, ST_SHUTTING_DOWN)) {
                        break;
                    }
                }

                // Check if confirmShutdown() was called at the end of the loop.
                if (success && gracefulShutdownStartTime == 0) {
                    logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " +
                            SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " +
                            "before run() implementation terminates.");
                }

                try {
                    // Run all remaining tasks and shutdown hooks.
                    for (;;) {
                        if (confirmShutdown()) {
                            break;
                        }
                    }
                } finally {
                    try {
                        cleanup();
                    } finally {
                        STATE_UPDATER.set(SingleThreadEventExecutor.this, ST_TERMINATED);
                        threadLock.release();
                        if (!taskQueue.isEmpty()) {
                            logger.warn(
                                    "An event executor terminated with " +
                                            "non-empty task queue (" + taskQueue.size() + ')');
                        }

                        terminationFuture.setSuccess(null);
                    }
                }
            }
        }
    });
}

说明：
1)首先调用     executor的     execute方法，这个      executor就是在创建      Event LoopGroup的时候创建的ThreadPerTaskExecutor类。该   execute方法会将    Runnable包装成  Netty的    FastThreadLocalThread。
    
2)任务中，首先判断线程中断状态，然后设置最后一次的执行时间。
    
3)执行当前   NioEventLoop的    run方法，注意：这个方法是个死循环，是整个   EventLoop的核心
    
4)在   finally块中，使用   CAS不断修改   state状态，改成    ST_SHUTTING_DOWN。也就是当线程  Loop结束的时候。关闭线程。最后还要死循环确认是否关闭，否则不会  break。然后，执行 cleanup操作，更新状态为
    
5)  ST_TERMINATED，并释放当前线程锁。如果任务队列不是空，则打印队列中还有多少个未完成的任务。并回调  terminationFuture方法。
    
6)其实最核心的就是   Event Loop自身的   run方法。再继续深入   run方法

```

+ EventLoop中的  Loop是靠  run实现的,我们分析下  run方法(该方法在  NioEventLoop)

```java
@Override
protected void run() {
    for (;;) {
        try {
            switch (selectStrategy.calculateStrategy(selectNowSupplier, hasTasks())) {
                case SelectStrategy.CONTINUE:
                    continue;
                case SelectStrategy.SELECT:
                    select(wakenUp.getAndSet(false));

                    // 'wakenUp.compareAndSet(false, true)' is always evaluated
                    // before calling 'selector.wakeup()' to reduce the wake-up
                    // overhead. (Selector.wakeup() is an expensive operation.)
                    //
                    // However, there is a race condition in this approach.
                    // The race condition is triggered when 'wakenUp' is set to
                    // true too early.
                    //
                    // 'wakenUp' is set to true too early if:
                    // 1) Selector is waken up between 'wakenUp.set(false)' and
                    //    'selector.select(...)'. (BAD)
                    // 2) Selector is waken up between 'selector.select(...)' and
                    //    'if (wakenUp.get()) { ... }'. (OK)
                    //
                    // In the first case, 'wakenUp' is set to true and the
                    // following 'selector.select(...)' will wake up immediately.
                    // Until 'wakenUp' is set to false again in the next round,
                    // 'wakenUp.compareAndSet(false, true)' will fail, and therefore
                    // any attempt to wake up the Selector will fail, too, causing
                    // the following 'selector.select(...)' call to block
                    // unnecessarily.
                    //
                    // To fix this problem, we wake up the selector again if wakenUp
                    // is true immediately after selector.select(...).
                    // It is inefficient in that it wakes up the selector for both
                    // the first case (BAD - wake-up required) and the second case
                    // (OK - no wake-up required).

                    if (wakenUp.get()) {
                        selector.wakeup();
                    }
                    // fall through
                default:
            }

            cancelledKeys = 0;
            needsToSelectAgain = false;
            final int ioRatio = this.ioRatio;
            if (ioRatio == 100) {
                try {
                    processSelectedKeys();
                } finally {
                    // Ensure we always run tasks.
                    runAllTasks();
                }
            } else {
                final long ioStartTime = System.nanoTime();
                try {
                    processSelectedKeys();
                } finally {
                    // Ensure we always run tasks.
                    final long ioTime = System.nanoTime() - ioStartTime;
                    runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                }
            }
        } catch (Throwable t) {
            handleLoopException(t);
        }
        // Always handle shutdown even if the loop processing threw an exception.
        try {
            if (isShuttingDown()) {
                closeAll();
                if (confirmShutdown()) {
                    return;
                }
            }
        } catch (Throwable t) {
            handleLoopException(t);
        }
    }
}

说明:
	1)从上面的步骤可以看出，整个   run方法做了   3件事情：
        select获取感兴趣的事件。
        processSelectedKeys处理事件。
        runAllTasks执行队列中的任务


```

+  select 方法

```java
private void select(boolean oldWakenUp) throws IOException {
    Selector selector = this.selector;
    try {
        int selectCnt = 0;
        long currentTimeNanos = System.nanoTime();
        long selectDeadLineNanos = currentTimeNanos + delayNanos(currentTimeNanos);
        for (;;) {
            long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
            if (timeoutMillis <= 0) {
                if (selectCnt == 0) {
                    selector.selectNow();
                    selectCnt = 1;
                }
                break;
            }

            // If a task was submitted when wakenUp value was true, the task didn't get a chance to call
            // Selector#wakeup. So we need to check task queue again before executing select operation.
            // If we don't, the task might be pended until select operation was timed out.
            // It might be pended until idle timeout if IdleStateHandler existed in pipeline.
            if (hasTasks() && wakenUp.compareAndSet(false, true)) {
                selector.selectNow();
                selectCnt = 1;
                break;
            }

            int selectedKeys = selector.select(timeoutMillis); //阻塞给定时间，默认一秒
            selectCnt ++;
			//如果  1秒后返回，有返回值   || select被用户唤醒   ||任务队列有任务    ||有定时任务即将被执行；则跳出循环
            if (selectedKeys != 0 || oldWakenUp || wakenUp.get() || hasTasks() || hasScheduledTasks()) {
                // - Selected something,
                // - waken up by user, or
                // - the task queue has a pending task.
                // - a scheduled task is ready for processing
                break;
            }
            if (Thread.interrupted()) {
                // Thread was interrupted so reset selected keys and break so we not run into a busy loop.
                // As this is most likely a bug in the handler of the user or it's client library we will
                // also log it.
                //
                // See https://github.com/netty/netty/issues/2426
                if (logger.isDebugEnabled()) {
                    logger.debug("Selector.select() returned prematurely because " +
                            "Thread.currentThread().interrupt() was called. Use " +
                            "NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop.");
                }
                selectCnt = 1;
                break;
            }

            long time = System.nanoTime(); 
            if (time - TimeUnit.MILLISECONDS.toNanos(timeoutMillis) >= currentTimeNanos) {
                // timeoutMillis elapsed without anything selected.
                selectCnt = 1;
            } else if (SELECTOR_AUTO_REBUILD_THRESHOLD > 0 &&
                    selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
                // The selector returned prematurely many times in a row.
                // Rebuild the selector to work around the problem.
                logger.warn(
                        "Selector.select() returned prematurely {} times in a row; rebuilding Selector {}.",
                        selectCnt, selector);

                rebuildSelector();
                selector = this.selector;

                // Select again to populate selectedKeys.
                selector.selectNow();
                selectCnt = 1;
                break;
            }

            currentTimeNanos = time;
        }

        if (selectCnt > MIN_PREMATURE_SELECTOR_RETURNS) {
            if (logger.isDebugEnabled()) {
                logger.debug("Selector.select() returned prematurely {} times in a row for Selector {}.",
                        selectCnt - 1, selector);
            }
        }
    } catch (CancelledKeyException e) {
        if (logger.isDebugEnabled()) {
            logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector {} - JDK bug?",
                    selector, e);
        }
        // Harmless exception - log anyway
    }
}

说明：
调用  selector的   select方法，默认阻塞一秒钟，如果有定时任务，则在定时任务剩余时间的基础上在加上    0.5
秒进行阻塞。当执行  execute方法的时候，也就是添加任务的时候，唤醒    selecor，防止  selecotr阻塞时间过长

```

### 13.9.5   EventLoop作为  Netty的核心的运行机制小结  

```
1)每次执行   ececute方法都是向队列中添加任务。当第一次添加时就启动线程，执行    run方法，而   run方法是整个  EventLoop的核心，就像   EventLoop的名字一样，Loop   Loop，不停的   Loop，Loop做什么呢？做    3件事情。

调用selector的   select方法，默认阻塞一秒钟，如果有定时任务，则在定时任务剩余时间的基础上在加上    0.5秒进行阻塞。当执行  execute方法的时候，也就是添加任务的时候，唤醒    selecor，防止  selecotr阻塞时间过长

当selector返回的时候，回调用   processSelectedKeys方法对    selectKey进行处理。

当processSelectedKeys方法执行结束后，则按照    ioRatio的比例执行   runAllTasks方法，默认是   IO任务时间和非  IO任务时间是相同的，你也可以根据你的应用特点进行调优。比如非        IO任务比较多，那么你就将ioRatio调小一点，这样非   IO任务就能执行的长一点。防止队列积攒过多的任务。

```













# 100 大实话

## 1，服务器优化

> 服务器优化要么进行分级，要么就是加缓存，就这两种模式