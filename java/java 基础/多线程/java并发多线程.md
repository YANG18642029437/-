# java 多线程

## java 多线程概述

### JUC（java.util.concurrent

#### 1.1 进程/线程

#### 1.2 并发/并行

### 三个包

+ java.util.concurrent
+ java.util.concurrent.atomic 原子包
+ java.util.concurrent.locks 锁包

###  什么叫多线程

> 在高内聚低耦合的前提下，线程 操作 资源量

### 多线程的状态

+ NEW
+ RUNNABLE
+ BLOCKED
+ WAITING
+ TIMED_WAITING
+ TERMINATED

## 集合

### 集合的小知识

+ ArrayList 的默认初始容量为 10
+ Map 的默认初始容量为 16 
+ ArrayList每次扩容为 基础容器的一半
+ Map 的扩容是 一倍

## 同步锁

### 同步锁小知识

+ 一个对象里面如果有多个 synchronized 方法，某一个时刻内，只要一个线程去调用其中的synchronized 方法了，其他的线程都只能等待，换句话说，某一个时刻内，只能有唯一一个线程去访问这些 synchronized 方法
+ 锁的是当前对象 this 被锁定后，其他的线程都不能进入到当前对象的其他synchronized 方法
+ 加个普通方法后发现和同步锁无关
+ 换成两个对象后，不是同一把锁了，情况立刻变化
+ synchronize实现同步的基础： Java 中的每一个对象都可以作为锁
+ 具体变现为以下三种形式
  + 对于普通同步方法，锁是当前实例对象
  + 对于同步方法块，锁是 synchonized 括号里配置的对象
  + 对于静态同步方法，锁是当前类的 Class 对
+ wait 要和 synchronize 配合才能使用

### 多线程编程技巧

+ 高内聚低耦合前提下，线程操作资源类
+ 判断/干活/通知
+ 防止虚假唤醒
+ 横向判断唤醒线程条件 使用 while 而不是 if