[TOC]



## 1，SpringMVC知识点的使用，及其特性

### 1-1，@RequestMapping  

```
1，作用:
	用于建立请求 URL 和处理请求方法之间的对应关系
2，属性:
	value：用于指定请求的 URL。 它和 path 属性的作用是一样的。
	method：用于指定请求的方式。
	params：用于指定限制请求参数的条件。 它支持简单的表达式。 要求请求参数的 key 和 value 必须和配置的一模一样
	headers：用于指定限制请求消息头的条件
```

### 1-2，请求参数绑定

```
1，基本数据类型和String作为参数
	public String findAccount(Integer accountId,String accountName)
		直接在方法的形参上添加请求参数同名的变量，会自动进行类型转换
2，POJO 类型作为参数
	public String saveAccount(Account account)
		在方法的形参上添加pojo类型的形参，spring会自动根据对象变量名自动注入
3，POJO 类中包含集合类型参数
	1，list注入格式 ： 使用下标注入
		账户 1 名称： <input type="text" name="accounts[0].name" ><br/>
        账户 1 金额： <input type="text" name="accounts[0].money" ><br/>
        账户 2 名称： <input type="text" name="accounts[1].name" ><br/>
        账户 2 金额： <input type="text" name="accounts[1].money" ><br/>
    2，map集合注入 ： 使用键值对注入
    	账户 3 名称： <input type="text" name="accountMap['one'].name" ><br/>
        账户 3 金额： <input type="text" name="accountMap['one'].money" ><br/>
        账户 4 名称： <input type="text" name="accountMap['two'].name" ><br/>
        账户 4 金额： <input type="text" name="accountMap['two'].money" ><br/>
```

### 1-3，中文乱码问题

```xml
在 web.xml 中配置一个过滤器
<!-- 配置 springMVC 编码过滤器 -->
<filter>
<filter-name>CharacterEncodingFilter</filter-name>
<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
<!-- 设置过滤器中的属性值 -->
<init-param>
<param-name>encoding</param-name>
<param-value>UTF-8</param-value>
</init-param>
<!-- 启动过滤器 -->
<init-param>
<param-name>forceEncoding</param-name>
<param-value>true</param-value>
</init-param>
</filter>

<!-- 过滤所有请求 -->
<filter-mapping>
<filter-name>CharacterEncodingFilter</filter-name>
<url-pattern>/*</url-pattern>
</filter-mapping>

在 springmvc 的配置文件中可以配置，静态资源不过滤：
<!-- location 表示路径， mapping 表示文件， **表示该目录下的文件以及子目录的文件 -->
<mvc:resources location="/css/" mapping="/css/**"/>
<mvc:resources location="/images/" mapping="/images/**"/>
<mvc:resources location="/scripts/" mapping="/javascript/**"/>


get 请求方式：
<!--tomacat 对 GET 和 POST 请求处理方式是不同的， GET 请求的编码问题， 要改 tomcat 的server.xml
配置文件，如下：-->
<Connector connectionTimeout="20000" port="8080"
protocol="HTTP/1.1" redirectPort="8443"/>
改为：
<Connector connectionTimeout="20000" port="8080"
protocol="HTTP/1.1" redirectPort="8443"
useBodyEncodingForURI="true"/>
<!--如果遇到 ajax 请求仍然乱码，请把：
useBodyEncodingForURI="true"改为 URIEncoding="UTF-8"
即可-->
```

### 1-4，自定义类型转换

```
1，定义一个类，实现 Converter 接口，该接口有两个泛型
	public interface Converter<S, T> {//S:表示接受的类型， T：表示目标类型}
2，实现convert()方法，进行类型转换
3，在 spring 配置文件中配置类型转换器。
	spring 配置类型转换器的机制是，将自定义的转换器注册到类型转换服务中去
	<!-- 配置类型转换器工厂 -->
    <bean id="converterService"
    class="org.springframework.context.support.ConversionServiceFactoryBean">
    <!-- 给工厂注入一个新的类型转换器 -->
    <property name="converters">
    <array>
    <!-- 配置自定义类型转换器 -->
    <bean class="com.itheima.web.converter.StringToDateConverter"></bean>
    </array>
    </property>
    </bean>
    第三步：在 annotation-driven 标签中引用配置的类型转换服务
    <!-- 引用自定义类型转换器 -->
    <mvc:annotation-driven conversion-service="converterService"></mvc:annotation-driven>
```

## 2，常用注解，及其使用

### 2-1，@RequestParam 

```
作用：
	把请求中指定名称的参数给控制器中的形参赋值
属性：
    value： 请求参数中的名称。
    required：请求参数中是否必须提供此参数。 默认值： true。表示必须提供，如果不提供将报错。
使用：
	public String useRequestParam(@RequestParam("name")String username,
@RequestParam(value="age",required=false)Integer age)
```

### 2-2，@RequestBody 

```
作用：
    用于获取请求体内容。 直接使用得到是 key=value&key=value...结构的数据。
    get 请求方式不适用。
属性：
    required：是否必须有请求体。默认值是:true。当取值为 true 时,get 请求方式会报错。如果取值为 false， get 请求得到是 null。
使用：
	public String useRequestBody(@RequestBody(required=false) String body)
```



### 2-3，@PathVaribale 

```
作用：
    用于绑定 url 中的占位符。 例如：请求 url 中 /delete/{id}， 这个{id}就是 url 占位符。
    url 支持占位符是 spring3.0 之后加入的。是 springmvc 支持 rest 风格 URL 的一个重要标志。
属性：
    value： 用于指定 url 中占位符名称。
    required：是否必须提供占位符。
使用：
	@RequestMapping("/usePathVariable/{id}")
	public String usePathVariable(@PathVariable("id") Integer id)
```



### 2-4，@RequestHeader 

```
作用：
	用于获取请求消息头。
属性：
    value：提供消息头名称
    required：是否必须有此消息头
使用：
	@RequestMapping("/useRequestHeader")
	public String useRequestHeader(@RequestHeader(value="Accept-Language",
									required=false)String requestHeader)
```



### 2-5，@CookieValue 

```
作用：
	用于把指定 cookie 名称的值传入控制器方法参数。
属性：
    value：指定 cookie 的名称。
    required：是否必须有此 cookie。
使用：
	@RequestMapping("/useCookieValue")
	public String 							useCookieValue(@CookieValue(value="JSESSIONID",required=false)
	String cookieValue)
```



### 2-6，@ModelAttribute 

```
作用：
    该注解是 SpringMVC4.3 版本以后新加入的。它可以用于修饰方法和参数。
    出现在方法上，表示当前方法会在控制器的方法执行之前，先执行。它可以修饰没有返回值的方法，也可
    以修饰有具体返回值的方法。
    出现在参数上，获取指定的数据给参数赋值。
属性：
	value：用于获取数据的 key。 key 可以是 POJO 的属性名称，也可以是 map 结构的 key。
使用：
	1，基于map不使用返回值
		@ModelAttribute 
		public void showModel(String username,Map<String,User> map)
		map中的值会自动存储到Model中
	2，基于返回值
		@ModelAttribute
		public User showModel(String username)
		返回值会填充到Model中
```



### 2-7，
### 2-8，





## 3，springMVC拦截器

### 3-1，拦截器和过滤器的区别

```
过滤器是 servlet 规范中的一部分， 任何 java web 工程都可以使用。
拦截器是 SpringMVC 框架自己的，只有使用了 SpringMVC 框架的工程才能用。
过滤器在 url-pattern 中配置了/*之后，可以对所有要访问的资源拦截。
拦截器它是只会拦截访问的控制器方法，如果访问的是 jsp， html,css,image 或者 js 是不会进行拦截的。
它也是 AOP 思想的具体应用。
我们要想自定义拦截器， 要求必须实现： HandlerInterceptor 接口。
```

### 3-2，使用步骤

```
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



