# Gradle 

## 什么是 Gradle 

+ Gradle 概述

> Gradle 就是和Maven 类似的 程序构建工具
>
> 使用 kotlin 来进行控制的一种智能工具
>
> 使用代码进行控制，不使用 xml 配置文件

+ Gradle 的作用
  + 编译测试
  + 依赖管理
  + DSL 自定义扩展任务
  + 可以构建 kotlin java pathon c++ 等都可以构建

## 使用 Gradle来构建项目

+ 选择创建项目

![image-20200221153935848](D:\temp\xmind\kotlin\phase3Image\image-20200221153935848.png)

+ 选择 本地 gradle 版本

![image-20200221154120118](D:\temp\xmind\kotlin\phase3Image\image-20200221154120118.png)

## Task 任务

+ 表达任务

```groovy
task("openDoor",{
    println "打开冰箱门"
})


task("putelephant",{
    println "放入大象"
}).dependsOn("openDoor")

task("closeDoor",{
    println "关上冰箱门"
}).dependsOn("putelephant")
```

+ Task 的生命周期
+  













