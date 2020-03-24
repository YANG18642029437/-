[TOC]



# 1，mybatis异常

### 1.关于pageHelper的版本问题


  - 旧版的pageHelper:


```xml
	<plugins>
        <-- 旧版PageHelper自动实现了Interceptor -->
		<plugin interceptor="com.github.pagehelper.PageHelper">
            <-- 旧版本需要指定数据库类型 -->
			<property name="dialect" value="mysql"/>                        
			<property name="rowBoundsWithCount" value="true"/>
		</plugin>
	</plugins>
```


  - 新版的pageHelper


```xml
	<plugins>
             <-- 新版PageHelper需要手动实现了Interceptor -->
		<plugin interceptor="com.github.pagehelper.PageInterceptor">
              <-- 新版本不需要指定数据库类型,pageHelper会自动识别使用的是什么数据库 -->
			<-- <property name="dialect" value="mysql"/> -->
			<property name="rowBoundsWithCount" value="true"/>
		</plugin>
	</plugins>
```
### 2，在通用mapper抛出String 索引越界异常 

```
因为在Dao层的接口继承 Mapper的时候泛型使用了本身而不是一个对应的javaBean
```

### 3，在使用PageHelper用来分页的时候出现 limet 0 , 5 但是输入的页数是正数

```
因为在xml配置或者注解中的sql语句中结尾处拼接 “；” 所以拼接分页语句的时候就会报错
```

### 4， 再测试类方法上忘了添加@Test注解

```xml
java.lang.Exception: No tests found matching Method run4(cn.czxy.test.UserTest) from org.junit.internal.requests.ClassRequest@531be3c5

添加@Test注解就好了
```

# 2，spring异常

### 1, 配置类中被@Bean修饰的方法返回值为viod

```java
异常信息：defined in cn.czxy.config.SpringConfiguration: Invalid factory method 'getUserDao4': needs to have a non-void return type!
```

### 2，配置类中出现重名的@Bean方法 方法名或者@Bean的name值重复

```
只会执行其中一个方法，另一个不会执行，执行完全随机
```

### 3，为@Bean方法执行的方法参数进行类型注入时spring容器中不能有多个相同类型的javaBean

```java
解决方式：public void getUserDao4(@Qualifier("userDao3") UserDao userDao)
		//使用@Qualifier("组件名")进行名字注入
```

### 4，当使用@value注入数据时不能为static方法/变量进行赋值

```
解决方式：为静态成员变量赋值时，只能使用一个非静态的方法为这个变量进行赋值 使用@Value方法赋值
```

# 3，spring Boot 异常

## 3-1  @ConfigurationProperties 使用报错

```
 1，是因为没有指定配置文件的路径
 	@ConfigurationProperties(prefix = "") 就可以解决
 2，当 springBoot的默认加载文件名是 application.properties 
 	但是即使能加载到文件 也会报错 忽略就行
```

## 3-2 @Data 不能生成 get 和 set 方法

```
1，添加配置lombok 配置插件
2，同样我们在Settings设置页面，我们点击Build，Execution，Deployment-->选择Compiler-->选中Annotation Processors，然后在右侧勾选Enable annotation processing即可。
就可以使用 @Data 生成默认方法了
```































