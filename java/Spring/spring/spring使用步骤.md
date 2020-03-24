[TOC]

## 0，spring知识点

### 0.1，@RequestMapping   和  @GetMapping @PostMapping 区别

```
1，@GetMapping是一个组合注解，是@RequestMapping(method = RequestMethod.GET)的缩写。
	只接受get请求
2，@PostMapping是一个组合注解，是@RequestMapping(method = RequestMethod.POST)的缩写。
	只接受post请求
3，只要执行测试run1，就会加载spring配置类，
	//并执行里面所有 非 static 方法 //并不是所有的方法都会执行，只有添加过注解的方法才会执行
```

### 0.2 依赖注入

```  
1，@Resource(name="对象id名字") 根据指定的ioc容器id名字注入 使用字段 setter方法
2，@Resource() 按照类型注入对象 			使用setter方法
3,@value("值")		注入普通值	字段setter方法 参数 ，  可以使用特殊的表达式
	1，该注解是使用再成员变量的时候可以使用表达式
	2，该注解使用再方法上时spring加载的时候会自动加载该方法，如果该方法上有成员变量的时候还可对其进行赋值，所有的参数都会被赋值
	3，表达式 @Value("${properties中的键值}") 用于获取properties 配置文件中的值
		@Value("#{对象的bean ID.成员变量}") 获取bean对象中的成员变量值


4，@PropertySource() 	加载properties 配置文件
						格式 @PropertySource("classPath:XX.properties")  类
	1、配置类上使用@PropertySource(“classpath:xx.properties”)使用@PropertySource 加载 properties 配置文件，“classpath:”固定前缀，表示从类路径下加载配置文件。
	2、使用@Bean 配置 property 加载的第三方插件 用于解析 spring 中@Value中的 ${}表达式
            @Bean
            public static PropertySourcesPlaceholderConfigurer propertyPlaceholder(){
                return new PropertySourcesPlaceholderConfigurer();
            }
    3、 @Value(${key}) 获取配置文件中指定 key 的内容
    4，此配置是配置再配置类上的，如果如果配置再别处的时候要再配置类上使用@improt 注解指定类位置 
```

### 0.3 @Bean创建对象

```
1，作用：
	@Bean 用于修饰方法，
        作用： 将方法返回的对象注册到 spring 容器
        向方法形参注入数据
    属性：
    	name：声明bean对象的id值
    	@Bean
		public Object test(Demo6Dao dao//此对象就取值于Spring的容器中，如果容器中没有此类就直接会报错)
2，使用技巧
	1，再配置类中使用此注解声明出的对象，可以直接注入到配置类中的某个方法的形参列表中
	2，再方法的形参上可以使用@Value注解直接对形参对象进行注入
		public Object get1(@Value("${jdbc.driver}")String driver,
									@Value("${jdbc.username}")String username)
3，总结
@Component、 @Repository、 @Service 等注解， 用于修饰类， 可以将自定义的类注册到 Spring 中
```

### 0.4 Bean的作用域

```
1，概述
	@Bean 可以通过添加@Scope 注解，设置 Bean 作用域。
2，取值，及作用
	1，singleton ： 【默认值，无需书写】单实例对象
	2，prototype ： 多实例对象
	//不重要
	3. request :WEB 项目中,Spring 创建一个 Bean 的对象,将对象存入到 request 域中.
	4. session :WEB 项目中,Spring 创建一个 Bean 的对象,将对象存入到 session 域中.
	5. globalSession :WEB 项目中,应用在 Portlet 环境.如果没有 Portlet 环境那么 globalSession 相当于session
3，使用步骤
	1，再声明@Bean时添加@Scope注解修改Bean对象的作用域范围  @Scope("prototype")//多例对象
	2，此注解同样可以使用再 @Component、 @Repository、 @Service、 @Controller 直接中声明对象的作用域范围
4，生命周期
	概述：
		生命周期指单实例对象由创建到销毁的整个过程。
	使用：
		1，再配置类中声明出创建和销毁方法
            1，@PostConstruct 初始化方法，项目启动时执行，只会被调用一次。//方法注解
            2，@PreDestroy 销毁方法， 项目关闭时执行， 只会被调用一次。	//方法注解
        2,使用@Bean注解
        	1，initMethod属性 声明 初始化方法
        	2，destroyMethod属性 声明 关闭执行方法
	
```

### 0.5 AOP入门

```
1，概述
	AOP： 全称是 Aspect Oriented Programming 即： 面向切面编程。面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术
	作用：
		1. 在程序运行期间，不修改源码对已有方法进行增强
	优势：
        1. 减少重复代码
        2. 提高开发效率
        3. 维护方便
    实现方式
    	Spring AOP 主要通过 2 种代理技术来实现：动态代理、 CGLIB
            动态代理：用于对接口+实现类情况进行代理。
            CGLIB：用于对仅有实现类情况进行代理。
2，使用步骤
	1，创建一个切面类
		1，使用@Component注解标记为组件
		2，使用@Aspect 标记本类为切面类
	2，创建切面
		1，创建一个普通方法
		2，在方法上使用注解标记本方法是什么阶段的通知，并配置切入点表达式
			1，@Before("p1()") 前置通知
			2，@AfterReturning("p1()") 后置通知
			3，@AfterThrowing("p1()") 异常通知
			4，@After("p1()") 最终通知
			5，@Around("p1()") 环绕通知
			6，@Pointcut（）用于定义通用切入点表达式
	3，修改配置类添加 @EnableAspectJAutoProxy 表示开启AOP注解扫描
3，注意事项
	1，在Aop切面方法上可以使用 JoinPoint 对象作为形参用于获取到一个特殊属性
		joinPoint.getTarget() 获取切入点方法的对象
		joinPoint.getSignature().getName() 获取切入点方法名
	
```

### 0.6 Aop的术语

```
3，AOP专业术语
	1，Joinpoint(连接点):
		所谓连接点是指那些被拦截到的点。在 spring 中,这些点指的是方法,因为 spring 只支持方法类型的连接点。
	2，Pointcut(切入点):
		所谓切入点是指我们要对哪些 Joinpoint 进行拦截的定义。
	3，Advice(通知/增强):
		所谓通知是指拦截到 Joinpoint 之后所要做的事情就是通知。
		通知的类型： 前置通知,后置通知,异常通知,最终通知,环绕通知
			前置通知，在被代理方法之前执行的通知
			后置通知，在被代理方法之后执行的通知
			异常通知，在代理方法抛出异常后执行的通知
			最终通知，在代理方法中一定必须执行的方法
	4，Introduction(引介)
		引介是一种特殊的通知在不修改类代码的前提下, Introduction 可以在运行期为类动态地添加一些方法或 Field。
	5，Target(目标对象):
		代理的目标对象。
	6，Weaving(织入):
		是指把增强应用到目标对象来创建新的代理对象的过程。
		spring 采用动态代理织入，而 AspectJ 采用编译期织入和类装载期织入。
	7，Proxy（代理） 
		一个类被 AOP 织入增强后，就产生一个结果代理类
	8，Aspect(切面):
		是切入点和通知（引介）的结合。
```

### 0.7 AOP 环绕通知配置

```
1，环绕通知使用
	1，创建一个切入点方法
	2，使用@Around("p1()")注解表明本方法是一个环绕通知
	3，在形参列表添加 ProceedingJoinPoint 用于执行 切入点方法
```

### 0.8 通知的特性

```
1，异常通知 获取异常对象
	1，@AfterThrowing(value = "p1()",throwing = "ex")
		throwing用于获取异常信息对象，要在方法的形参上声明Throwable 对象
2，后置通知获取返回值结果
	@AfterReturning(value="execution(* com.czxy.demo4.service..*.*(..))",returning="obj")
	returning 用于获取返回值对象 在方法形参上要声明出对象
3，前置通知获取执行方法对象
```

### 0.9 AOP的日志

```java
//获取目标类对象
Object target = joinPoint.getTarget();

 //获取方法访问的参数
 Object[] args = joinPoint.getArgs();
 
 //获取访问方法名
 String name = joinPoint.getSignature().getName()
 
 //获取请求ip地址
 //1.获取请求对象，需要配置 一个监听器用于将spring容器中填充 request对象
 <!-- 配置监听器，用于加载request对象 -->
<listener>
    <listener-class>org.springframework.web.context.request.RequestContextListener</listener-class>
</listener>
//2.获取ip地址
 ip = request.getRemoteAddr();
```

### 0.10 AOP执行详情

```
1，环绕前置通知
2，前置通知
3，方法执行
4，环绕后置通知
5，最终通知
6，后置通知/异常通知
```



## 1,创建一个spring的入门项目

### 1.1创建一个不使用骨架的maven文件

### 1.2创建一个spring的配置类

```
1，创建一个普通的java类使用@Configuration 表明本类是一个配置类
	1.1 再使用@ComponentScan(basePackages={"要进行注解扫描的类"})
2，分层创建MVC层对象 并使用对应的注解标记
	1，Controller 控制层注解
	2，Service 服务层注解
	3，Repository 持久层注解
	4，Component 同用注解
3，创建测试类
	1，使用@RunWith("整合类") 整合spring和junit 使用SpringJUnit4ClassRunner.class
		使用此方法标记的测试类会 去加载配置类 告诉程序如何运行代码。
	2，使用@ContextConfiguration指定要加载的配置类
4，获取到ioc容器中的javaBean 
	1，使用@Resource注解标记 使用name属性指定javaBean的id值
```



## 2,注解版spring整合mybatis

### 2.1  创建spring的配置文件类

```java
package cn.czxy.demo1.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;



@Configuration //表明本类是一个配置类
@ComponentScan(basePackages = {"cn.czxy.demo1"}) //表明组件扫描路径
@PropertySource("classpath:db.properties")  //加载properties 配置文件路径
@Import({MyBatisConfiguration.class})  //导入mybatis配置文件类
public class SpringConfiguration {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    /**
     * 解析properties文件对象
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer config(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * 创建连接池对象 druid 连接池
     * @return
     */
    @Bean
    public DataSource getDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * 配置事务管理器
     * @param dataSource 
     * @return
     */
    @Bean
    public DataSourceTransactionManager txManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }


}
```

### 2.2  创建mybais的配置文件类

```java
package cn.czxy.demo1.config;


import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.util.Properties;


public class MyBatisConfiguration {
    /**
     * 创建SqlSessionFactory 工厂对象
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception {
        //创建spring整合mybatis的对象
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        //填充连接池对象
        factoryBean.setDataSource(dataSource);
        //创建配置类
        Configuration configuration = new Configuration();
        //在工厂中添加配置类
        factoryBean.setConfiguration(configuration);
        //添加分页组件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("dialect","mysql");
        properties.setProperty("rowBoundsWithCount","true");
        pageHelper.setProperties(properties);
        //将组件添加到spring整合mybatis对象中
        factoryBean.setPlugins(new Interceptor[]{pageHelper});
        //返回sqlSessionFactory对象
        return factoryBean.getObject();
    }


    /**
     * 指定mybatis的扫描接口位置
     * @return
     */
    @Bean
    public MapperScannerConfigurer scannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //指定扫描的路径可以用逗号做分离指定多个 dao 包路径
        //例如 ：("cn.czxy.demo1.dao,cn.czxy.demo1.dao")
        mapperScannerConfigurer.setBasePackage("cn.czxy.demo1.dao");
        return mapperScannerConfigurer;
    }
}
```





## 3，IOC总结