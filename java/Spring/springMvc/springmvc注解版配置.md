[TOC]



# 1,实现步骤

## 1-1 创建一个springmvc配置类

```java
@Configuration
@ComponentScan(basePackages = {"cn.czxy.demo1.controller"})
public class MVCConfiguration {

}
```

## 1-2 创建一个加载配置文件的类似监听器的类

```java
/**
 *  在项目启动时加载springmvc的配置文件
 *
 */
public class WebInitalizer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext application = new AnnotationConfigWebApplicationContext();
        //注册 配置文件 加载spring和mybatis的配置
        application.register(MVCConfiguration.class);
        //将servlet添加到spring容器中
        application.setServletContext(servletContext);
        //向 servletContext中添加一个前端控制器 就是一个servlet
        ServletRegistration.Dynamic springmvc = servletContext.addServlet("springmvc", new DispatcherServlet(application));
        //控制器仅拦截*.action 的请求
        springmvc.addMapping("/");
        springmvc.setLoadOnStartup(2);
    }
}
```

## 1-3 创建一个视图解析器

```java
@Configuration
@ComponentScan(basePackages = {"cn.czxy.demo1.controller"})
//开启springMvc配置的加载
@EnableWebMvc
public class MVCConfiguration {
    /**
     * 创建视图解析器
     * @return
     */
    @Bean
    public InternalResourceViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}

//1，绕开视图解析器的方法使用 forward 或 redirect 可以不使用视图解析器
```

## 1-4 解决post中文乱码问题

```java
package cn.czxy.demo1.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


/**
 *  在项目启动时加载springmvc的配置文件
 */
public class WebInitalizer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext application = new AnnotationConfigWebApplicationContext();
        //注册 配置文件 加载spring和mybatis的配置
        application.register(MVCConfiguration.class);
        //将servlet添加到spring容器中
        application.setServletContext(servletContext);

        //添加一个解决中文乱码的过滤器
        FilterRegistration.Dynamic characterEncodingFiler
                = servletContext.addFilter("characterEncodingFiler", new CharacterEncodingFilter("utf-8"));
        //设置拦截路径
        characterEncodingFiler.addMappingForUrlPatterns(null,true,"/*");


        //向 servletContext中添加一个前端控制器 就是一个servlet
        ServletRegistration.Dynamic springmvc = servletContext.addServlet("springmvc", new DispatcherServlet(application));
        //控制器仅拦截*.action 的请求
        springmvc.addMapping("/");
        springmvc.setLoadOnStartup(2);
    }
}
```

## 1-5 通配符

| 通配符 | 说明                        |
| ------ | --------------------------- |
| **     | 表示任意多个字符下/任意层级 |
| *      | 表示任意多个字符            |
| ？     | 表示任意单个字符            |





# 2，参数获取及注解

## 2-1 @RequestParam(name="参数名")

```
1，获取指定名字的参数，可以进行基本的数据转换
2，当指定参数不存在时，会抛出异常
3，避免抛出异常的手段
	1，指定默认值 defaultValue="默认值"
	2, 设置该参数不是必须的 required=false
```

## 2-2 绑定请求参数类型转换

```java

//1,创建一个转换类
package cn.czxy.demo1.config;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter<String,Date> {

    private static SimpleDateFormat sb = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date convert(String source) {

        try {
            return sb.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

//2，让mvc配置类 继承 webMvcConfigurerAdapter 对象 
package cn.czxy.demo1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
/* EnableWebMvc:开启MVC配置*/
@Configuration
@ComponentScan(basePackages = {"cn.czxy.demo1.controller"})
@EnableWebMvc
public class MVCConfiguration extends WebMvcConfigurerAdapter {
    /**
     * 创建视图解析器
     * @return
     */
    @Bean
    public InternalResourceViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * 添加字符转换日期对象的转换对象到springMvc中 重写父类的 addFormatter方法 将日期转换对象添加到，转换中
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        //创建抓换对象添加到对象中
        registry.addConverter(new DateConverter());
    }
}
```

## 2-3 特殊对象的值填充

```
1， 为对象中的对象中的变量赋值  <input type="text" name="classes.name"> classes 是一个对象变量名 name是其中的一个成员变量 map集合也可以用这种方式添加值 classes 为 map对象名 name 为键 value 为值
2，为list集合赋值
	<input type="text" name="list[0]">
    <input type="text" name="list[1]">
    <input type="text" name="list[2]">
    !被忽略的索引会被创建 但是所有的值都会null
    一索引添加值
    
```

## 2-4 字符形式转换为 Date 类型

```java
@DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
private Date pdate; //根据pattern的格式进行数据的转换
```

## 2-5 多个name 属性相同的值 封装数据

```
1，使用 一个数组获取参数
	获取到的所有值的一个数组
2，使用 一个字符串获取
	获取一个字符串，所有的参数拼接到一起以“，”做分隔
```

## 2-6 文件上传

### 2-6-1 单文件上传

```java
/**单文件上传
* @throws IOException
* @throws IllegalStateException */
@RequestMapping("/run1")
public String run1(@RequestParam(name="pname")String pname,
@RequestParam(name="price")Double price,
@RequestParam(name="pimage")MultipartFile pimage) throws Exception{
System.out.println(pname);
System.out.println(price);
System.out.println("文件名： "+pimage.getOriginalFilename());
System.out.println("文件MIME类型： "+pimage.getContentType());
System.out.println("参数名： "+pimage.getName());
//D盘的temp文件夹必须存在
pimage.transferTo(new File("D:\\temp\\"+pimage.getOriginalFilename()));
return "demo1";
}
```

### 2-6-2多文件上传

```java
/**多文件上传*/
@RequestMapping("/run2")
public String run2(@RequestParam(name="pname")String pname,
@RequestParam(name="price")Double price,
@RequestParam(name="pimage")MultipartFile[] arr) throws Exception{
System.out.println(pname);
System.out.println(price);
System.out.println("文件数量： "+arr.length);
//保存文件到硬盘
for(MultipartFile mf:arr){
mf.transferTo(new File("D:\\temp\\"+mf.getOriginalFilename()));
}
return "demo2";
}
```



### 2-6-4 MultipartFile 对象的方法

| 方法名                       | 描述                                               |
| ---------------------------- | -------------------------------------------------- |
| String getOriginalFilename() | 获得原始上传文件名                                 |
| transferTo(File file)        | 将上传文件转换到一个指定的文件中                   |
| String getContentType()      | 获取文件 MIME 类型，如 image/pjpeg、 text/plain 等 |
| String getName()             | 获取表单中文件组件的                               |

## 2-7 @RestController 表示本类中所有的方法的返回值都是写到响应体中的

# 3，返回值

## 3-1 返回值为String

```
默认为请求转发到页面上 string就是转发路径
使用 redirect: 可以 进行重定向 
使用 redirect:http://路径 可以跳转外网
```

## 3-2 返回值为 ModelAndView 对象

```
用于进行页面跳转，和值填充 默认使用的是请求转发
```

# 4，特殊的机制

## 4-1 异常处理器使用

```java
//1,创建一个异常处理器，并添加组件注解 @Component 并在mvc配置类中扫描此包
package cn.czxy.demo1.resolver;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component								//实现异常处理接口
public class UserResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
        //设置异常信息到request作用域
        mv.addObject("error",ex.getMessage());
        //设置跳转页面
        mv.setViewName("error");
        return mv;
    }
}
```



# 5，springMVC拦截器

## 5-1，拦截器和过滤器的区别

```
过滤器是 servlet 规范中的一部分， 任何 java web 工程都可以使用。
拦截器是 SpringMVC 框架自己的，只有使用了 SpringMVC 框架的工程才能用。
过滤器在 url-pattern 中配置了/*之后，可以对所有要访问的资源拦截。
拦截器它是只会拦截访问的控制器方法，如果访问的是 jsp， html,css,image 或者 js 是不会进行拦截的。
它也是 AOP 思想的具体应用。
我们要想自定义拦截器， 要求必须实现： HandlerInterceptor 接口。
```

## 5-2，使用步骤

```html
1，编写一个普通类实现 HandlerInterceptor 接口 实现三个方法
	//创建在controller执行之前进行执行
	/**
    * 如何调用：
    * 按拦截器定义顺序调用
    * 何时调用：
    * 只要配置了都会调用
    * 有什么用：
    * 如果程序员决定该拦截器对请求进行拦截处理后还要调用其他的拦截器，或者是业务处理器去
    * 进行处理，则返回 true。
    * 如果程序员决定不需要再调用其他的组件去处理请求，则返回 false。
    */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse
response, Object handler)



	//在controller执行之后进行执行
	/**
    * 如何调用：
    * 按拦截器定义逆序调用
    * 何时调用：
    * 在拦截器链内所有拦截器返成功调用
    * 有什么用：
    * 在业务处理器处理完请求后，但是 DispatcherServlet 向客户端返回响应前被调用，
    * 在该方法中对用户请求 request 进行处理。
    */
	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object handler ModelAndView modelAndView) 
	
	/**
    * 如何调用：
    * 按拦截器定义逆序调用
    * 何时调用：
    * 只有 preHandle 返回 true 才调用
    * 有什么用：
    * 在 DispatcherServlet 完全处理完请求后被调用，
    * 可以在该方法中进行一些资源清理的操作。
    */
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex)
2，配置拦截器
    作用路径可以通过在配置文件中配置。
    <!-- 配置拦截器的作用范围 -->
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**" /><!-- 用于指定对拦截的 url -->
            <mvc:exclude-mapping path=""/><!-- 用于指定排除的 url-->
            <bean id="handlerInterceptorDemo1"
            class="com.itheima.web.interceptor.HandlerInterceptorDemo1"></bean>
        </mvc:interceptor>
    </mvc:interceptors>
```