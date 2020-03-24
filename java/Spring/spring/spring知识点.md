[TOC]



# 1,spring 

## 1.1 spring的基本概念

Spring框架是企业使用最多的框架，没有之一。Spring是一站式框架，称之为一站式框架的原因是Spring可以整合其他框架。

 

### l   Spring  IoC：控制反转。

对对象的创建和销毁的管理，控制权由程序员 交给 spring管理

对象工厂及依赖注入；

用于管理对象的创建和销毁，用于向变量中注入实例对象

 IOC：控制反转。把对象创建、销毁权力由程序 交给 spring容器。降低代码耦合性

### l  Spring  AOP：面向切面编程技术，

 为Spring事务管理打下基础。

 不修改原来代码，就能把新代码切入到原来代码的执行中

 反射技术（被封装了）

AOP：面向切面编程。在不修改原有代码的基础上，把新代码插入到原有代码的执行中。

​              发生运行时期（反射机制）

### | 依赖注入（DI）

 创建对象/定义变量时，通过spring为对象赋予引用/为变量赋予变量值

### l  Spring  Transaction management：Spring数据库事务管理。

 

向spring抛出异常，spring帮你回滚事务

未向spring抛出异常，spring帮你提交事务

## 1.2 spring 基本注释及使用情况

### l  SpringConfiguration类注解：

| **注解**           | **描述**                                                     |
| ------------------ | ------------------------------------------------------------ |
| **@Configuration** | 用于标明当前类是一个Spring配置类。                           |
| **@ComponentScan** | 用于自动扫描某个包下的注册组件。   （扫描所有的@Component等注解修饰的类）   格式：@ComponentScan(basePackages={"包名1","包名2"})   说明：会自动扫描包名1及包名2下所有的注册组件   **扫描的注解包括：@Component****、@Repository****、@Service****、@Controller** |

 

### l  注册组件注解：

| **注解**        | **描述**                                                     |
| --------------- | ------------------------------------------------------------ |
| **@Component**  | 用于注册组件。   将当前类注册，交给spring管理。   注意：注册名不能重复，否则报错。   格式：@Component("注册名") |
| **@Repository** | 专业用于Dao组件注册，用于替换Dao类上的@Component   格式：@Repository("注册名") |
| **@Service**    | 专业用于Service组件注册，用于替换Service类上的@Component   格式：@Service("注册名") |
| **@Controller** | 专业用于Servlet组件注册，用于替换Servlet类上的@Component   格式：@Controller("注册名") |

 

### l  注入实例对象/基本数据类型注解/注册实例对象的注解：

| **注解**      | **描述**                                                     |
| ------------- | ------------------------------------------------------------ |
| **@Resource** | 用于使用已注册组件创建实例对象，并注入到此处修饰的变量中   注意：name必须和之前的注册名保持一致，否则报错   格式：@Resource(name="注册名") |
| **@Bean**           | 将方法的返回对象注册到spring容器,   **未赋予注册名，将方法名作为注册名**   **注：若使用方法名作为注册名，方法名就不能相同了** | 方法 |
| **@Bean(name=”…”)** | 将方法的返回对象注册到spring容器，   并赋予注册名            | 方法 |
| @Value | **1、为String/基本数据类型 的成员变量赋值**     **2、为方法的参数进行赋值**   **3、被Value修饰的方法，spring会执行一次该方法** |

> @Value(**"${jdbc.username}"**) 一种注入数据的方式  获取properties 配置文件
>
> @Value(“${properties的key}”)

#### | @Resource 的使用特性

**@Resource(name=”****组件名”)****：** **按名称注入。**

​                                                        根据组件名，让spring注入对象

​                                                        注意：1、如果组件名不存在，报错

​                                                                 2、组件名不能重复的

 

**@Resource**                             **：按类型注入**

​                                                        根据成员变量类型，让spring注入对象

​                                                        注意：1、如果类型找不到，报错

​                                                                 2、如果该类型找到多个对象，报错

#### | @Bean 使用特性

​              **1****、向spring**去注册方法的返回值**

​              **2****、被@Bean**修饰的方法会被spring**执行

​              **3****、被@Bean**修饰的方法，若存在参数**：

​              JavaBean（dao、service、domain等）：spring根据类型注入（前提该类型必须注册[[刘1\]](#_msocom_1) ）

​              String/基本数据类型： @Value 进行注入

#### 总结 

```

—— 特点 
 
@Value：用于为成员变量或者成员方法的参数赋值
注：被Value修饰的方法，会被spring执行，该方法的所有参数会被Value统一赋值

@Bean 用于修饰方法，
作用：将返回值对象注册到spring容器
将为方法参数赋值（注入）
		   方法会被spring执行
		   
—— 区别
@Bean和@Component都是向spring进行注册组件。


	@Bean用于修饰方法，可以将返回值对象注册到spring中，默认组件名：方法名
@Component、@Repository、@Service等注解，用于修饰类，可以将自定义的类注册到Spring中。默认组件名：类名小驼峰式
    
	如果程序员能够修改JavaBean，可以使用@Component、@Repository、@Service
	如果程序员不能修改JavaBean，可以使用@Bean
```

#### 扩展 

##### @Bean 的单例和多例

| **注解** | **描述**             | **取值**                                                     |
| -------- | -------------------- | ------------------------------------------------------------ |
| @Scope   | 用于设置Bean的作用域 | singleton      ：【默认值，无需书写】单实例对象   **prototype**    **：多实例对象** |

##### 1.1    注册组件的生命周期

@Component

| **注解**       | **描述**                                     |
| -------------- | -------------------------------------------- |
| @PostConstruct | 初始化方法，项目启动时执行，只会被调用一次。 |
| @PreDestroy    | 销毁方法，项目关闭时执行，只会被调用一次。   |

##### @Bean 的配置生命周期方法

 	 ``initMethod``：配置初始化方法
  	``destroyMethod``：配置销毁方法
	  格式：@Bean(initMethod="方法名",destroyMethod="方法名2")

### l  测试类注解：

| **注解**              | **描述**                                                     |
| --------------------- | ------------------------------------------------------------ |
| @RunWith              | 告诉程序如何运行代码。   固定结构：   @RunWith(SpringJUnit4ClassRunner.class)      代码使用spring自带的junit4测试 |
| @ContextConfiguration | 向spring工厂加载配置类，并开始使用spring管理对象   固定结构：   **@ContextConfiguration(classes={SpringConfiguration.class})** |



### | @PropertySource 数据类型的注入

| 注解            | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| @PropertySource | 告诉程序将会加载那个 properties 文件  @PropertySource(**"classpath:demo4.properties"**)  指定加载文件 注解要标注在 能够被 springBoot 扫描到配置文件类上 |

## 1.3 AOP

### | 使用AOP对方法进行增强

+ 在配置上添加 配置注解

  > @EnableAspectJAutoProxy 表示开启 AOP 自动配置 

+ 创建一个 AOP 配置类

  > 使用 注解 表明本类是一个 AOP 切面类
  >
  > @Component
  >  @Aspect

  ![img](D:\temp\xmind\springBoot\img\clip_image002.jpg)

  > ​	   什么是连接点：指那些被spring拦截到的方法
  >
  > ​       什么是切入点：通常是一个表达式，用来表示spring需要对哪些连接点进行拦截
  >
  > ​       什么是通知：指拦截到连接点之后要做的事情
  >
  > ​       什么是切面：包含了切入点和通知,用@Aspect注解修饰

+ 配置切入点表达式

![img](D:\temp\xmind\springBoot\img\clip_image003.jpg)

+ 各个 通知的执行时机

  > @Before：前置通知。 在连接点执行之前执行。
  >
  > ​                     JoinPoint：连接点封装对象
  >
  > @After：最终通知。 在连接点执行之后执行。
  >
  > @AfterReturning：后置通知。在连接点执行之后执行。可以获取连接点的返回值
  >
  > @Around：环绕通知。可以控制连接点是否执行。
  >
  > @AfterThrowing：异常通知。 仅在连接点出现异常时才执行。
  >
  > ​                            可以获取连接点出现的具体异常信息

+ @PointCut：公共切入点。（相当于常量）

+ 通知的执行顺序

![img](D:\temp\xmind\springBoot\img\clip_image004.jpg)

+ 获取一个方法的返回值 完整的 AOP切面类 

```java

@Component
@Aspect
public class MyAspect {
    /*
    * 定义公共的切入点
    * */
    @Pointcut("execution(* com.czxy.demo2.service..*.*(..))")
    private void myCut1(){}
    /*
    * 环绕通知：控制连接点是否执行
    * */
    @Around("myCut1()")
    public Object a1(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("环绕之前---");
        //获取到连接点返回值
        Object proceed = pjp.proceed();
        System.out.println("环绕中："+pjp.getSignature().getName()+"的返回值是："+proceed);
        System.out.println("环绕之后----");
        //若执行连接点获取到返回值，返回值需要向后传递
        return proceed;
    }
    /*
    * 前置通知：在连接点之前执行
    * */
    @Before("myCut1()")
    public void b1(JoinPoint joinPoint){
        System.out.println("前置通知------"+joinPoint.getSignature().getName());
    }
    /**
     * 最终通知：在连接点之后执行，无论如何都会被执行的通知
     * 例如：关流，写日志
     */
    @After("myCut1()")
    public void a2(JoinPoint joinPoint){
        System.out.println("最终通知------"+joinPoint.getSignature().getName());
    }
    /*
    * 后置通知：在连接点之后执行。用来接收返回值。连接点出现异常，不会被执行
    * */
    @AfterReturning(value="myCut1()",returning = "obj")
    public void a2(JoinPoint joinPoint,Object obj){
        System.out.println("后置通知------"+joinPoint.getSignature().getName()+"的返回值是："+obj);
    }
    /**
     * 异常通知：仅连接点出现异常之后。 用来在连接点出现异常后做其他操作的。例如：写日志
     */
    @AfterThrowing(value="myCut1()",throwing = "ex")
    public void a2(JoinPoint joinPoint,Throwable ex){
        System.out.println("异常通知------"+joinPoint.getSignature().getName()+"出了异常："+ex.getMessage());
    }
}

```

## 1.4 Spring 的事务管理

### 1.4.1 @Transactional 标记在类上表示本类是一个 被spring事务管理的一个类

+ 要在配置类中添加 @EnableTransactionManagement 开启事务的管理

| **注解**                          | **描述**                                                   |
| --------------------------------- | ---------------------------------------------------------- |
| **@Transactional(readOnly=true)** | **只读事务、提升运行效率，DQL** **使用**                   |
| @Transactional(readOnly=false)    | 默认值，**非只读事务，可以进行增删改操作。**DML** **使用** |
| @Transactional(timeout=60) | 设置超时为60秒，如果还没有操作结束，将抛异常。 |

# 2 Spring MVC