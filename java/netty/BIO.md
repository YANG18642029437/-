# 1 I/O 模型

## 1.1 I/O 模型基本说明

+ I/O 模型简单的理解： 就是用什么样的通道进行数据的发送和接收，很大程度上决定了程序通讯的性能
+ Java 共支持3种网络编程模型 IO 模型 : BIO NIO AIO 
+ Java BIO ： 同步并阻塞(传统阻塞) , 服务器实现模式为 一个连接一个线程，即客户端有连接请求时服务器端就需要启动一个线程进行处理，如果这个连接不做任何事情会造成不必要的线程开销
+ java NIO： 同步非阻塞，服务器实现模式为一个线程处理多个请求（连接），即客户端发送的请求都会注册到多路复用器上，多路复用器轮询到连接有 I/O 请求就进行处理

## 1.2 IO 使用场景

+ BIO 方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4 以前唯一选择
+ NIO 方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，弹幕系统，服务间通讯等，编程比较复杂，JDK 1.4支持
+ AIO方式使用于**连接数目多且连接比较长**（重操作）的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂，JDK7开始支持。

# 2，BIO

## 2.1 BIO 基本介绍

+ BIO （blocking I/O）就是传统的 Java IO 编程，其相关的类再接口在 Java.IO
+ BIO 的线程阻塞情况，可以通过连接池进行改善
+ BIO 编程流程
  + 服务器端启动一个ServerSocket
  + 客户端启动Socket 对服务器进行通信，默认情况下服务器端需要对每个客户，建立一个线程与之通讯
  + 客户端发出请求后，先咨询服务器是否有线程响应，如果没有则会等待，或者被拒接
  + 如果有响应，客户端线程会等待请求结束后，在继续执行 

## 2.2 BIO应用实例

+ 服务端的 代码

```java
package com.yyx.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        // 创建一个线程池
        ExecutorService ex = Executors.newCachedThreadPool();
        //如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）

        //创建一个ServerSocket 进行通讯
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器开启");
        // 循环获取 客户端的连接
        while (true){
            // 监听等待客户端连接
            System.out.println("等待客户端连接");
            final Socket accept = serverSocket.accept();
            System.out.println("连接到一个客户端");
            //启动一个线程与之通讯
            ex.execute(new Runnable() {
                public void run() {
                    System.out.println(Thread.currentThread().getId() +"   " + Thread.currentThread().getName());
                    // 可以和客户端进行通讯
                    handler(accept);
                }
            });
        }
    }

    public static void handler(Socket socket){
        // 进行通讯的方法
        byte[] bytes = new byte[1024];
        try {
            InputStream inputStream = socket.getInputStream();
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1){
                System.out.print(new String(bytes,0,len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 关闭和 client 的连接
            try {
                System.out.println("连接被关闭了");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

+ 客户端请求 方式

  > 使用 telnet 进行连接发送请求
  >
  > 开启 telnet window 的服务默认是关闭的
  >
  > telnet 连接地址 端口号
  >
  > 按 ctrl + ] 进入输入模式
  >
  > send 传输的值 // 发送数据

## 2.3 BIO 的问题

+ 每个请求都需要创建独立的线程，与对应的客户端进行数据Read，业务处理，数据 Write
+ 当并发数较大时，需要创建大量线程来处理连接，系统资源占用较大
+ 连接建立后，如果当前线程暂时没有数据可读，则线程就阻塞在Read操作上，造成线程资源的浪费

# 3, NIO 

## 3.1 NIO 基本介绍

+ java NIO 全称 java non-blocking IO 是指 JDK 提供的新 API 。从JDK1.4 开始， JAVA 提供了一系列改进的输入/输出的新特性，被称为 NIO (即 New IO) 是 同步非阻塞的
+ NIO 相关类都被放在 java.nio 包 下 并且对 原 java.io 包中的很多类进行改写
+ NIO 有三个核心 ： Channel （通道） Buffer （缓存区） Selector（选择器）
+ NIO 是面向 缓冲区，或者面向块编程的，数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动，这就增加了处理过程中的灵活性，使用它可以提供非阻塞式的高伸缩性网络

+ Java NIO 的非阻塞模式，使用一个线程从某通道 发送请求或者读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取，而不是保持线程阻塞，所以直至数据改变的可以读取之前，该线程可以继续做其他的事情。非阻塞写也是如此，一个线程请求写入到一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情
+ 通俗理解： NIO是可以做到用一个线程来处理多个操作的。假设有10000个请求过来，根据实际情况，可以分配50或者100个线程来处理，不像之前的阻塞IO那样，非得分配 10000个
+ HTTP2.0 使用了多路复用技术，做到同一个连接并发处理多个请求，而且并发请求的数量比HTTP1.1大了好几个数量级



## 3.2  NIO 和 BIO 的比较

+ BIO 以流的方式处理数据，而 NIO 以块的方式处理数据块 I/O 的效率比流 I/O 高很多
+ BIO 是阻塞的 NIO是非阻塞的
+ BIO 基于字节流和字符流进行操作，而NIO基于Channel通道和Buffer（缓冲区） 进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写到通道中。Selector（选择器）用于监听多个通道的实践，因此使用单个线程就可以监听多个客户端通道

## 3.3 NIO 三大核心原理示意图

![image-20191218163707172](D:\temp\xmind\netty 学习\images\image-20191218163707172.png)

+ Selector，Channel 和 Buffer 的关系图
  + 每个 channel 都对应一个 Buffer
  + 一个Selector 会对应一个线程 一个线程对应多个 channel（连接）
  + 多个 Channel 注册到同一个 Select 
  + 程序切换到那个 channel 是由事件决定的 Event 就是一个重要的概念
  + Select 会根据不同的事件在各个通道上切换
  + Buffer 就是一个内存块，是有一个数组底层
  + 数据的读取写入是通过Buffer  这个和 BIO ， BIO中要么是输入流，或者是输出流，并不能双向的，但是NIO的Buffer是可以读也可以写的 但是需要 filp 方法进行切换
  + Channel 是双向的 ， 可以返回底层操作系统的情况，比如 Linux 底层的操作系统 通道就是双向的

## 3.4 Buffer 实战 Ctrl + H 用作打开类的继承结构

+ 基本介绍

> 缓冲区（Buffer）： 缓冲区本质上是一个可以读写数据的内存块可以理解成是一个**容器对象（含数组）** 该对象提供了一组方法，可以更轻松的使用内存块，缓冲区对象内置了一些机制，能够跟踪和记录缓冲区的状态变化情况。Channel 提供从文件，网络读取数据的渠道，但是读取或写入的数据必须经由 Buffer
>
> Buffer 是一个 顶级抽象类
>
> 旗下子类 包含除 Boolean 外所有的基本数据类型 的基础实现子类
>
> 子类中都包含一个当前类型的 数组

![image-20191218172150034](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20191218172150034.png)

+ Buffer 中包含的 四个核心属性
  + Capacity 容量 表示可以容纳的最大容量，创建后就不可改变 
  + limit  表示缓冲区的当前终点，不能对缓冲区超过极限的位置进行读写操作。且极限是可以修改的
  + Position  位置表示下一个要被读或者写的元素的索引，每次读写缓冲区数据时都会改变值，就是索引指针
  + Mark  标记，用作记录

![image-20191218162548240](D:\temp\xmind\netty 学习\images\image-20191218162548240.png)


+ Buffer 中的相关方法

```java
public abstract class Buffer {
    //JDK1.4时，引入的api
    public final int capacity( )//返回此缓冲区的容量
    public final   int position( )//返回此缓冲区的位置
    public final Buffer position (int newPositio)//设置此缓冲区的位置
    public final int limit( )//返回此缓冲区的限制
    public final Buffer limit (int newLimit)//设置此缓冲区的限制
    public final Buffer mark( )//在此缓冲区的位置设置标记
    public final Buffer reset( )//将此缓冲区的位置重置为以前标记的位置
    public final Buffer clear( )//清除此缓冲区, 即将各个标记恢复到初始状态，但是数据并没有真正擦除, 后面操作会覆盖
    public final Buffer flip( )//反转此缓冲区
    public final Buffer rewind( )//重绕此缓冲区
    public final int remaining( )//返回当前位置与限制之间的元素数
    public final boolean hasRemaining( )//告知在当前位置和限制之间是否有元素
    public abstract boolean isReadOnly( );//告知此缓冲区是否为只读缓冲区
 
    //JDK1.6时引入的api
    public abstract boolean hasArray();//告知此缓冲区是否具有可访问的底层实现数组
    public abstract Object array();//返回此缓冲区的底层实现数组
    public abstract int arrayOffset();//返回此缓冲区的底层实现数组中第一个缓冲区元素的偏移量
    public abstract boolean isDirect();//告知此缓冲区是否为直接缓冲区
}

```

+ ByteBuffer

```java
public abstract class ByteBuffer {
    //缓冲区创建相关api
    public static ByteBuffer allocateDirect(int capacity)//创建直接缓冲区
    public static ByteBuffer allocate(int capacity)//设置缓冲区的初始容量
    public static ByteBuffer wrap(byte[] array)//把一个数组放到缓冲区中使用
    //构造初始化位置offset和上界length的缓冲区
    public static ByteBuffer wrap(byte[] array,int offset, int length)
     //缓存区存取相关API
    public abstract byte get( );//从当前位置position上get，get之后，position会自动+1
    public abstract byte get (int index);//从绝对位置get
    public abstract ByteBuffer put (byte b);//从当前位置上添加，put之后，position会自动+1
    public abstract ByteBuffer put (int index, byte b);//从绝对位置上put
 }

```




+ 实战案例

```java
package com.yyx.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        // 举例说明 Buffer
        //创建一个 Buffer
        IntBuffer buffer = IntBuffer.allocate(5);
        // 向buffer 中存放数据
        buffer.put(10);
        buffer.put(11);
        buffer.put(12);
        buffer.put(13);
        buffer.put(14);

        // 获取buffer 中的数据
        // 此方法进行读写切换
        buffer.flip();

        // 判断 当前缓冲是否还有可读数据
        while (buffer.hasRemaining()){
            // 获取当前指针指向的 buffer 中的一个参数 并将指针向下移动一格
            int i = buffer.get();
            System.out.println(i);
        }
    }
}
```

### 3.4.2 MappedBuffer

+  MappedBuffer简述

> 可以让文件直接在内存（堆外的内存）中进行修改，而如何同步到文件由 NIO 来完成
>
> 可以让文件直接在内存中进行修改，操作系统不需要拷贝一次

+ 实战

```java
package com.yyx.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedBufferTest01 {
    public static void main(String[] args) throws Exception{
        // 读写流
        RandomAccessFile file = new RandomAccessFile("file01.text", "rw");
        FileChannel channel = file.getChannel();
        /**
         * 参数1： 表示使用读写模式
         * 参数2： 表示可以直接修改的起始位置
         * 参数3： 表示映射到内存的大小（非索引位置），文件映射多少个字节映射到内存
         * MappedByBuffer 实际类型是 DirectByteBuffer
         */
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
//        buffer.put("我爱福福".getBytes());
        buffer.put(1,(byte) 10);
        buffer.put("福福".getBytes(),0,6);
        channel.write(buffer);
        channel.close();
        file.close();
    }
}
```



## 3.5 Channel 

### 3.5.1 基本介绍

+ NIO 的通道和流之间的区别
  + 通道可以同时进行读写，而流只能读或者写
  + 通道可以实现异步读写数据
  + 通道可以从缓冲读数据，也可以写数据到缓冲
+ BIO 中的Stream 是单向的，例如 FileInputStream 对象只能进行读取数据的操作，而 NIO 中的通道（Channel） 是双向的，可以读操作，也可以写操作。
+ Channel 在 NIO 中是一个接口
  + 继承于 Closeable 接口
+ 常用的 Channel 类有： FileChannel，DatagramChannel，ServerSocketChannel 和 SocketChannel
+ FileChannel 用于文件的数据读写， DatagramChannel 用于 UDP 的数据读写 ， ServerSocketChannel 和 SocketChannel 用于 TCP 的数据读写

### 3.5.2 各种 Channel 的类型介绍

#### 3.5.2.1 FileChannel

+ FileChannel 类 主要用来对本地文件进行IO 操作

  > 1)public int read(ByteBuffer dst) ，从通道读取数据并放到缓冲区中
  >
  > 2)public int write(ByteBuffer src) ，把缓冲区的数据写到通道中
  >
  > 3)public long transferFrom(ReadableByteChannel src, long position, long count)，从目标通道中复制数据到当前通道
  >
  > 4)public long transferTo(long position, long count, WritableByteChannel target)，把数据从当前通道复制给目标通道

+ 文件读取

```java
package com.yyx.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class NIOFileChannel02 {
    public static void main(String[] args)throws Exception {
        // 获取到 Channel 对象
        FileChannel channel = new FileInputStream("file01.text").getChannel();
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 创建字符集对象
        Charset charset = Charset.forName("utf-8");
        // 循环读取文件
        while (channel.read(buffer) != -1){
            // 将文件中的光标移动到指定位置
            buffer.flip();
            // 解码输出
            System.out.println(charset.decode(buffer).toString());
            // 清理缓存 以便下次读取
            buffer.clear();
        }
    }
}
```

+ 基础实践 文件写入

```java
package com.yyx.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {
        // 创建一个 Buffer
        ByteBuffer buffer = ByteBuffer.wrap("hello,尚硅谷".getBytes("utf-8"));
        File file = new File("file01.text");
        // 通过输出流过去 FileChannel  NIO 包裹住了 BIO
        FileChannel open = new FileOutputStream(file).getChannel();
        open.write(buffer);
        open.close();
        buffer.clear();
    }
}
```

+ 使用一个 Channel 完成读写

```java
package com.yyx.nio;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("file01.text", "rw");
        FileChannel channel = file.getChannel();
        channel.position(channel.size());
        byte[] bytes = "我爱佳佳".getBytes("utf-8");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        channel.write(byteBuffer);
        byteBuffer.clear();
        channel.position(0);
        channel.read(byteBuffer);
        String str = new String(byteBuffer.array(), 0, byteBuffer.limit(), "utf-8");
        System.out.println(str);
    }
}
```

+ 知识点

> FileChannel 由 FileInputStream 创建的 并且 FileInputStream 中就包含一个 FileChannel 对象

![image-20191218204659163](D:\temp\xmind\netty 学习\images\image-20191218204659163.png)

### 3.5.3 Buffer 和 Channel 的注意事项和细节

+ ByteBuffer 支持类型化的 put 和 get，put放入的是什么数据类型，get 就应该使用相应的数据类型来取出，否则可能有 BufferUnderFlowException 异常

  + 如果 是以类型的形式放入数据，必须以相同的类型取出否则会抛出异常

+ 可以将一个普通 Buffer 转成只读 Buffer

  ```java
  ByteBuffer buffer = allocate.asReadOnlyBuffer(); // 返回一个只读buffer 如果继续进行 写操作会抛出 ReadOnlyBufferException 异常
  ```

+ NIO 还提供了 MapperdByteBuffer，可以让文件直接在内存（堆外的内存）中进行修改，而如何同步到文件由 NIO 来完成

+ NIO 支持使用多个 Buffer 来完成读写操作 Scattering 和 Gatering 

 * ```
 package com.yyx.nio;

    import java.net.InetSocketAddress;
    import java.nio.ByteBuffer;
    import java.nio.channels.ServerSocketChannel;
    import java.nio.channels.SocketChannel;
    import java.util.Arrays;
    
    /**
    
     * Scattering 将数据写入到Buffer时，采用Buffer数组依次写入 【分散】
    
     * Gathering 从buffer读取数据时。可以采用buffer数组，依次读取
   */
       public class ScatteringAndGatheringTest {
       public static void main(String[] args) throws Exception{
           // 创建服务器对象
           ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
           // 创建服务器端口地址对象
           InetSocketAddress address = new InetSocketAddress(7000);
           // 绑定端口并启动
           serverSocketChannel.socket().bind(address);
           // 创建Buffer数组
           ByteBuffer[] byteBuffers = new ByteBuffer[2];
           byteBuffers[0] = ByteBuffer.allocate(5);
           byteBuffers[1] = ByteBuffer.allocate(3);
    
       ​    // 等待客户端的连接
       ​    SocketChannel socketChannel = serverSocketChannel.accept();
   ​    int messageLength = 8;
       ​    while (true){
       ​        int byteRead = 0;
       ​        // 进行读取如果读取的长度小于 messageLength 就表示可以使用一直循环读取
       ​        // 直到 将所有的 buffer 装满为止
       ​        while (byteRead < messageLength){
       ​            long read = socketChannel.read(byteBuffers);
       ​            byteRead += read;
       ​            System.out.println("byteRead: " + byteRead);
       ​            // 使用流打印数据
       ​            Arrays.asList(byteBuffers).stream().map(item -> "position=" + item.position() + ", Limit=" + item.limit()).forEach(System.out::println);
       ​        }
       ​        Arrays.asList(byteBuffers).stream().forEach(item -> item.flip());

    ​        long byteWrite = 0;
    ​        // 如果写入的长度小于规定的长度表示 Buffer 还未被全部写入完成不过这是不可能的
    ​        // 在上面都已经规定好了，上面不装满是不会放到下面来的
    ​        while (byteWrite < messageLength) {
    ​            // 使用数组Buffer进行写入
    ​            long l = socketChannel.write(byteBuffers);
    ​            byteWrite += l;
    ​        }
    ​        Arrays.asList(byteBuffers).stream().forEach(item -> item.clear());
    ​        System.out.println("byteRead=" + byteRead + " byteWrite=" + byteWrite + " messageLength=" +messageLength);
    ​    }
    }
    }
 ```

## 3.6 Selector（选择器）

### 3.6.1 基本介绍

+ java 的 NIO ， 用非阻塞的IO方式。可以用一个线程，处理多个客户端连接，就会使用到 **Selector （选择器）**
+ Selector 能够检测多个注册的通道上是否有事件发生（多个Channel可以注册到同一个 Selector ） 如果有事件发生，边获取事件然后针对每个事件进行相应的处理。这样就可以只用一个单线程去管理多个通道，也就是管理多个连接和请求。
+ 只有在连接真正有读写事件发生时，才会进行读写，就大大地减少了系统开销，并且不必为每个连接都创建一个线程，不用去维护多个线程
+ 避免了多线程之间的上下文 切换导致的开销

### 3.6.2 Selector 的特点

+ Netty 的Io线程 NIOEventLoop 聚合了 Selector（选择器/多路复用器）可以同时并发处理成百上千个客户端连接
+ 当线程从某客户端 Socket 通道进行读写数据时，若没有可用数据时，线程可以进行其他任务
+ 线程通常将非阻塞IO的空闲时间用于在其他通道上执行IO操作，所以单独的线程可以管理多个输入和输出通道
+ 由于读写操作都是非阻塞的，这就可以充分提升IO线程的运行效率，避免由于频繁IO阻塞导致线程挂起
+ 一个 IO 线程可以并发处理 N 个客户端连接和读写操作,这从根本上解决了传统同步阻塞 IO 一连接一线程的模型，构架性能，弹性伸缩能力和可靠性都得到了极大的提升

### 3.6.3 类相关方法

+ Selector 这是一个抽象类

```java
public abstract class Selector implements Closeable { 
public static Selector open();//得到一个选择器对象
// 返回的是将要有IO操作的 SelectionKey
public int select(long timeout);//监控所有注册的通道，当其中有 IO 操作可以进行时，将对应的 SelectionKey 加入到内部集合中并返回，参数用来设置超时时间
// 返回的是所有的 SelectorKey 是所有注册到 Selector 中的 Chanel 的 key值
public Set<SelectionKey> selectedKeys();//从内部集合中得到所有的 SelectionKey	
}

```

+ 注意事项

  + NIO 中的 ServerSocketChannel 功能类似 ServerSocket，SocketChannel 类似 Socket

  + Selector 相关方法说明

    > selector.select()//阻塞
    >
    > selector.select(1000);//阻塞1000毫秒，在1000毫秒后返回
    >
    > selector.wakeup();//唤醒selector
    >
    > selector.selectNow();//不阻塞，立马返还

### 3.6.4 NIO 非阻塞网络编程原理分析

![image-20191219095436097](D:\temp\xmind\netty 学习\images\image-20191219095436097.png)

+ 当客户端连接时，会通过ServerSocketChannel 得到 SocketChannel
+ Selector 进行监听 Select 方法 返回有事件发生的通道个数
+ 将SocketChannel 注册到 Selector 上 reginster（Selector sel,int ops）, 一个 selector 上可以注册多个 SocketChannel
+ 注册后返回一个 SelectionKey，会和该 Selector 关联（集合）
+ 进一步得到各个 SelectionKey （有事件发生）
+ 在通过 SelectionKey 反向获取 SocketChannel ， 方法channel() 
+ 可以通过 得到的 channel 完成业务处理
+ 代码撑腰

### 3.6.5 实战演练

+ 服务器端代码

```java
package com.yyx.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception{
        // 创建一个 ServerSocketChannel 对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 获取 Selector 对象
        Selector selector = Selector.open();
//        ServerSocket socket = serverSocketChannel.socket();
        // 绑定一个端口地址，并在服务器端进行监听
        serverSocketChannel.bind(new InetSocketAddress(7000));
        // 设置Channel为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 将 ServerSocketChannel 注册到 Selector 并指定关心的事件是什么事件
        // 由于是服务器的 Channel 所以 关心的事件为 OP_ACCEPT 有服务来连接的事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 循环等待 客户端的连接
        while (true){
            // 等待一秒获取连接 如果返回 0 表示没有获取到 可以任何事件发生
            // 返回的是 当前有几个可以执行的 Channel 事件 返回 0 表示没有
            if (selector.select(1000) == 0){
                System.out.println("服务器等待了 1 秒，无连接");
                continue;
            }
            // 获取到 相关的 SelectionKey 集合 这是有事件发生的 SelectionKey
            // 如果返回的 > 0 表示已经获取到关注的事件
            // 使用Selector.selectedKeys() 返回关注事件的集合
            // 通过SelectionKey 可以反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 获取迭代器 使用迭代器是为了 方便后续删除集合中的参数
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            // 迭代循环获取 SelectionKey
            while (keyIterator.hasNext()){
                // 获取到 key
                SelectionKey selectionKey = keyIterator.next();
                // 根据 key 对应通道发生的事件进行对应的处理
                // 判断是 连接请求 有新的客户端进行连接
                if (selectionKey.isAcceptable()){
                    // 获取到一个客户端的 SocketChannel accept 本来是阻塞的但是
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 将其设置为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 将其注册到 selection 中 指定为 读事件 并绑定 Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                // 如果是读事件
                if (selectionKey.isReadable()){
                    // 将其强转为 SocketChannel 对象
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    // 获取到该 Channel 绑定的 Buffer
                    ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();
                    // 将通道内的数据读取到 缓存中
                    socketChannel.read(buffer);
                    // 展示通道中读取到的数据
                    System.out.println("form 客户端 " + new String(buffer.array()));
                }
                // 执行结束将 SelectionKey 从结果中剔除
                keyIterator.remove();
            }
        }
//        SocketChannel socketChannel = serverSocketChannel.accept();
//        SelectionKey register = socketChannel.register(selector, 100);
    }
}

```

+ 客户端代码

```java
package com.yyx.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception{
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞模式
        socketChannel.configureBlocking(false);
        // 设置连接地址
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 7000);
        // 如果连接失败
        if (!socketChannel.connect(inetSocketAddress)){
            // while 循环重试
            while (!socketChannel.finishConnect()){
                System.out.println("连接需要时间！！！ 等待连接");
            }
        }
        // !!! 如果连接成功，就发送数据
        String str  = "hell 佳佳---";
        // 根据传递的字节数组 转换为 Buffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        // 发送数据，将 Buffer 写入 Channel
        socketChannel.write(byteBuffer);
        // 阻塞线程
        System.in.read();
    }
}

```

### 3.6.6 SelectionKey

+ SelectionKey,表示 Select 和网络通道的注册关系，共四种
  + int OP_ACCEPT ： 有新的网络连接可以 accept 值为 16
  + int OP_CONNECT：代表连接已经建立 值为 8
  + int OP_READ： 代表读操作，值为1
  + int OP_WRITE： 代表写操作，值为4
+ 源代码
  + public static final int OP_READ = 1 << 0
  + public static final int OP_WRITE = 1 << 2
  + public static final int OP_CONNECT = 1 << 3
  + public static final int OP_ACCEPT = 1 << 4

+ 相关方法

  > **public abstract class** SelectionKey {
  >
  > public abstract Selector selector();//得到与之关联的 Selector 对象
  >
  > public abstract SelectableChannel channel();//得到与之关联的通道
  >
  > public final Object attachment();//得到与之关联的共享数据
  >
  > public abstract SelectionKey interestOps(int ops);//设置或改变监听事件
  >
  > public final boolean isAcceptable();//是否可以 accept
  >
  > public final boolean isReadable();//是否可以读
  >
  > public final boolean isWritable();//是否可以写
  >
  > }

### 3.6.7 ServerSocketChannel 

+ ServerSocketChannel 在服务器端监听新的客户端Socket连接

+ 相关方法如下

  > **public abstract class** **ServerSocketChannel**
  >    **extends** AbstractSelectableChannel
  >    **implements** NetworkChannel{
  >
  > public static ServerSocketChannel open()，得到一个 ServerSocketChannel 通道
  >
  > public final ServerSocketChannel bind(SocketAddress local)，设置服务器端端口号
  >
  > public final SelectableChannel configureBlocking(boolean block)，设置阻塞或非阻塞模式，取值 false 表示采用非阻塞模式
  >
  > public SocketChannel accept()，接受一个连接，返回代表这个连接的通道对象
  >
  > public final SelectionKey register(Selector sel, int ops)，注册一个选择器并设置监听事件
  >
  > }

### 3.6.8 SocketChannel

+ 1)SocketChannel，网络 IO 通道，**具体负责进行读写操作**。NIO 把缓冲区的数据写入通道，或者把通道里的数据读到缓冲区。

+ 相关方法

  > **public** **abstract class** SocketChannel
  >    **extends** AbstractSelectableChannel
  >    **implements** ByteChannel, ScatteringByteChannel, GatheringByteChannel, NetworkChannel{
  >
  > public static SocketChannel open();//得到一个 SocketChannel 通道
  >
  > public final SelectableChannel configureBlocking(boolean block);//设置阻塞或非阻塞模式，取值 false 表示采用非阻塞模式
  >
  > public boolean connect(SocketAddress remote);//连接服务器
  >
  > public boolean finishConnect();//如果上面的方法连接失败，接下来就要通过该方法完成连接操作
  >
  > public int write(ByteBuffer src);//往通道里写数据
  >
  > public int read(ByteBuffer dst);//从通道里读数据
  >
  > public final SelectionKey register(Selector sel, int ops, Object att);//注册一个选择器并设置监听事件，最后一个参数可以设置共享数据
  >
  > public final void close();//关闭通道
  >
  > }

# 4，NIO 与 零拷贝

## 4.1 零拷贝基本介绍

+ 零拷贝是网络编程的关键，很多性能优化都离不开
+ 在java程序中，常用的零拷贝有 mmap (内存映射) 和 sendFile 。 那么，他们在 OS 里，到底是怎么样的一个设计？ 我们分析 mmap 和 sendFile 这两个零拷贝
+ 另外我们看下 NIO 中如何使用零拷贝

## 4.2  零拷贝的几种类型、

+ DMA: direct  memory access 直接内存拷贝 不通过 CPU

+ 传统 IO 

  + 拷贝模式

  > Hard Drive （磁盘） DMA coyp-> Kernel buffer (核心缓存)  cpu copy-> user buffer (用户缓存)  -> socket buffer (通道缓存/网络缓存)  DMA copy—> protocol engine (协议引擎) 经历了 四次 copy

  + 模式转换

  > user context (用户上下文)-> kernel context (Syscall read)  (核心上下文，系统读)-> user context(用户上下文) -> kernel context (Syscall write) (核心上下文，系统写) 
  >
  > 经历 三次 上下文切换

+ mmap 优化

  + 概念

  > mmap 通过内存映射，将文件映射到内核缓冲区，同时，用户空间可以共享内核空间的数据。这样，在进行网络传输时，就可以减少内核空间到用户控件的拷贝次数。  

  + 拷贝模式

  > 减少了一次到用户缓存的 copy 操作 通道直接 通过
  >
  > kernel buffer 进行 copy

  + 模式转换

  > 不变 和 传统 IO 一样

+ sendFile 优化 linux 2.1

  + 概念、

  > Linux 2.1 版本 提供了 sendFile 函数，其基本原理如下：数据根本不经过用户态，直接从内核缓冲区进入到 Socket Buffer，同时，由于和用户态完全无关，就减少了一次上下文切换

  + 提示

  > 提示：零拷贝从操作系统角度，是没有cpu 拷贝

  + 拷贝模式

  > 减少了一次到用户缓存的 copy 操作 通道直接 通过
  >
  > kernel buffer 进行 copy

  + 模式转换

  > 由于 不经过 用户状态：直接从内核缓冲区 进入到 Socket Buffer 同时 由于可用户 态无关，就减少了一次上下文切换 只有三次了

+ sendFile 优化 linux 2.4

  + 概述

  >  Linux 在 2.4 版本中，做了一些修改，避免了从内核缓冲区拷贝到 Socket buffer 的操作，直接拷贝到协议栈，从而再一次减少了数据拷贝。  

  + 提示

  > 虽然也是三次拷贝但是 中间的 CPU 拷贝的 信息很少 
  >
  > 只有 比如 ： length offset  等基础信息 ，可以忽略不计

  + 上下文切换

  > 上下文切换依然是 两次切换

## 4.3 零拷贝 细致理解

+ 我们说零拷贝，是从操作系统的角度来说的，因为内核缓冲区之间，没有数据是重复的 （只有 kernel buffer 有一份数据）
+ 零拷贝不仅仅带来更少的数据复制，还能带来其他的性能优势，例如更少的上下文切换，更少的 CPU 缓冲伪共享以及无 CPU校验和计算

## 4.4 mmap 和 sendFile 的区别

+ mmap 适合 小数据量读写，sendFile 适合大文件传输
+ mmap 需要 4 次上下文切换， 3 次数据拷贝； sendFile 需要 3 次上下文切换，最少 2 次数据拷贝
+ sendFile 可以利用 DMA 方式，减少 CPU 拷贝，mmap 则不能 （必须从内核拷贝到 Socket 缓冲区）

## 4.5 NIO 实现 零拷贝 实例

### 4.5.1 老版

+ server

```java
/**
 * 传统IO 服务端
 */
public class OldIOService {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7001);
        while (true){
            Socket socket = serverSocket.accept();
            DataInputStream stream = new DataInputStream(socket.getInputStream());
            try {
                byte[] byteArray = new byte[4096];
                while (true){
                    int readCount = stream.read(byteArray);
                    if (-1 == readCount){
                        break;
                    }
                }
                stream.close();
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
```

+ client

```java
/**
 * 客户端
 */
public class OldIOClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost",7001);

        String fileName = "D:\\BaiduNetdiskDownload\\1-58.rar";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[4096];
        long readCount = 0;
        long total = 0;

        long startTime = System.currentTimeMillis();
        while ((readCount = fileInputStream.read(buffer)) >= 0){
            total += readCount;
            dataOutputStream.write(buffer);
        }
        System.out.println("发生字节数量： " + total + " , 耗时： " + (System.currentTimeMillis() - startTime));
        dataOutputStream.close();
        socket.close();
        fileInputStream.close();
    }
}
//发生字节数量： 2016178367 , 耗时： 8637
```

### 4.5.2 NIO

+ server

```java
public class NewIOServer {
    public static void main(String[] args) throws Exception{
        InetSocketAddress address = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while (true){

            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;
            while (-1 != readCount){
                try {
                    readCount = socketChannel.read(byteBuffer);
                }catch (Exception e){
                    e.printStackTrace();
                }
                byteBuffer.rewind();// 倒带 position = 0 mark 作废
            }
            System.out.println("接收完成");
        }
    }
}
```



+ client

```java
public class NewIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost",7001));

        long startTime = System.currentTimeMillis();

        FileChannel channel = new FileInputStream("D:\\BaiduNetdiskDownload\\1-58.rar").getChannel();
        channel.transferTo(0,channel.size(),socketChannel);

        System.out.println("发生字节数量： " + channel.size() + " , 耗时： " + (System.currentTimeMillis() - startTime));

        channel.close();
        socketChannel.close();
    }
}
发生字节数量： 2016178367 , 耗时： 2923
```

# 5，AIO 

## 5.1 AIO 基本介绍

+ JDK 7 引入了 Asynchronous I/O 即 AIO 在进行 I/O 编程中，常用到两种模式： Reactor 和 Proactor 。 Java 的 NIO 就是 Reactor ， 当有事件触发时，服务器端得到通知，进行相应的处理
+ AIO 即 NIO 2.0 叫做异步不阻塞的 IO 。 AIO 引入异步通道的概念，采用了 Proactor 模式，简化了程序编写，有效的请求才启动线程，它的特点是先由操作系统完成后才通知服务端程序启动去处理，一般适用于连接数较多且连接时间较长的应用
+ 目前 AIO 还没有广泛应用，Netty 也是基于 NIO 而不是 AIO，因此我们就不详解 AIO 了。

