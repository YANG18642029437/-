---
我爱你中国
---

[TOC]







## 1，spring Security 概述

### 1-1，简介

```
1，springSecuity是 spring用户提供安全认证服务的框架
2，主要概念
	1，认证 用户登录的认证过程
	2，授权 用户的访问权限
```

## 2，使用步骤

### 2-1，导入依赖坐标

```xml
		<spring.security.version>5.0.1.RELEASE</spring.security.version>
		<dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-web</artifactId>
            <version>${spring.security.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-config</artifactId>
            <version>${spring.security.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-core</artifactId>
            <version>${spring.security.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-taglibs</artifactId>
            <version>${spring.security.version}</version>
        </dependency>
```

### 2-2，添加web-xml的过滤器配置

```xml
 <filter>
     	<!-- 配置登录的过滤器 filter-name 不能进行改变 -->
        <filter-name>springSecurityFilterChain</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>springSecurityFilterChain</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

### 2-3，创建spring security 核心配置文件的创建

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:security="http://www.springframework.org/schema/security"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/security
    http://www.springframework.org/schema/security/spring-security.xsd">

    <!-- 配置不拦截的资源 -->
    <security:http pattern="/login.jsp" security="none"/>
    <security:http pattern="/failer.jsp" security="none"/>
    <security:http pattern="/css/**" security="none"/>
    <security:http pattern="/img/**" security="none"/>
    <security:http pattern="/plugins/**" security="none"/>
    <!--
    	配置具体的规则
    	auto-config="true"	不用自己编写登录的页面，框架提供默认登录页面
    	use-expressions="false"	是否使用SPEL表达式（没学习过）
    -->
    <security:http auto-config="true" use-expressions="false">
        <!-- 配置具体的拦截的规则 pattern="请求路径的规则" access="访问系统的人，必须有ROLE_USER的角色"                                        角色信息使用集合进行填装-->
        <security:intercept-url pattern="/**" access="ROLE_USER,ROLE_ADMIN"/>

        <!-- 定义跳转的具体的页面 
				login-page="/login.jsp"  //登陆用的页面
                login-processing-url="/login.do" //登陆用户的访问路径
                default-target-url="/index.jsp"		//默认登陆后的访问路径
                authentication-failure-url="/failer.jsp" // 登陆失败后访问路径
                authentication-success-forward-url="/pages/main.jsp" // 登陆成功后访问路径
		-->
        <security:form-login
                login-page="/login.jsp" 
                login-processing-url="/login.do"
                default-target-url="/index.jsp"
                authentication-failure-url="/failer.jsp"
                authentication-success-forward-url="/pages/main.jsp"
        />

        <!-- 关闭跨域请求 -->
        <security:csrf disabled="true"/>
        <!-- 退出 定义退出是用的路径-->
        <security:logout invalidate-session="true" logout-url="/logout.do" logout-success-url="/login.jsp"/>

    </security:http>

    <!-- 切换成数据库中的用户名和密码 -->
    <security:authentication-manager>
        <security:authentication-provider user-service-ref="userService">
            <!-- 配置加密的方式 使用加密方式不用再password上拼接 {noop}了 -->
            <security:password-encoder ref="passwordEncoder"/>
        </security:authentication-provider>
    </security:authentication-manager>

    <!-- 配置加密类 -->
    <bean id="passwordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"/>

    <!-- 提供了入门的方式，在内存中存入用户名和密码
    <security:authentication-manager>
    	<security:authentication-provider>
    		<security:user-service>
    			<security:user name="admin" password="{noop}admin" authorities="ROLE_USER"/>
    		</security:user-service>
    	</security:authentication-provider>
    </security:authentication-manager>
    -->

</beans>
```

### 2-4，创建service的类

```java
1，Service接口实现 UserDetailsService接口
2，实现 LoadUserByUserName方法
	返回一个UserDetail对象
	在方法内将对象封装为一个UserDetailService对象
		创建一个User对象直接返回即可
			有三个参数
				username
				password
				权限数组
		创建步骤
			 User user = new User(userInfo.getUsername(), userInfo.getPassword(), userInfo.getStatus() == 0 ? false : true, true, true, true, getAuthority(userInfo.getRoles()));
				1，password的使用 在没有使用密文的时候要加上 {noop} 表是不使用密文解密
				2，封装一个SimpleGrantedAuthority集合用户做用户的权限校验，值是之前指定的路径访问权限字符串
				3，后面的集合布尔值
					1，第一个布尔值 表示账号是否能用
					2，第二个布尔值 账户是否忽略
					3，第三个布尔值 账户是否过期
					4，第四个布尔值 账户是否锁定 
```

### 2-5，用户退出

```xml
<security:logout invalidate-session="true" logout-url="/logout.do" logout-success-url="/login.jsp"/>
<!-- 
	invalidate-session 是否清空session
	logout-url 退出用的路径
	logout-success-url 退出后跳转到那个页面
-->
```

## 3，springSecurity源码分析

```
1，web.xml配置文件中的Filter 的过滤器不是真正的执行者，成员变量的过滤器才是真正的加载配置文件的执行者
2，在声明SpringSecurity.xml配置文件时候 是由一个配置文件类将所有的配置信息加载到spring容器中的
3，springSecurityFilterChain 过滤器的名字是不能够进行修改的，必须用这个名字，这是一个spring容器中的一个bean的名字，使用这个过滤器名就可以找到这个Bean对象
```



## 4，服务端方法级别权限控制

```
1.开启注解使用
	<security:global-method-security jsr250-annotations="enabled"/>
    <security:global-method-security secured-annotations="enabled"/>
    <security:global-method-security pre-post-annotations="disabled"/>
```

#### 4-1 JSR-250注解 

```xml
<!--  1，在springSecurity开启jsr-250的使用 -->
<security:global-method-security jsr250-annotations="enabled"/>
<!-- 2，在指定的方法上指定注解 -->
@RolesAllowed({"ADMIN"}) 表示本方法只有ADMIN权限的用户才能访问
1，@RolesAllowed表示访问对应方法时所应该具有的角色
2，@PermitAll表示允许所有的角色进行访问，也就是说不进行权限控制
3，@DenyAll是和PermitAll相反的，表示无论什么角色都不能访问
<!-- 3,在pom文件中导入依赖坐标 -->
		<dependency>
            <groupId>javax.annotation</groupId>
            <artifactId>jsr250-api</artifactId>
            <version>1.0</version>
        </dependency>
```

### 4-2 @Secured注解 

```xml
<!-- 开启注解的使用 -->
<security:global-method-security secured-annotations="enabled"/>
<!-- 导入maven坐标 -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-taglibs</artifactId>
    <version>version</version>
</dependency>
<!--注意事项
		1,jsr-250在使用的时候注解后的权限访问可以省略 Role_
		但是在使用@Secured注解的时候就不可以省略Role_
-->
```

### 4-3,基于方法上表达式的控制

```xml
<!-- 开启注解的使用 -->
    <security:global-method-security pre-post-annotations="disabled"/>
<!-- 表达式的使用 -->
@PreAuthorize("hasRole('Role_ADMIN')")//表示只有ADMIN权限的用户才能访问
@PreAuthorize("authentication.principal.username == 'tom'")//只有tom能访问
```

### 4-4,基于页面的权限控制

```xml
<!--1，导入maven坐标-->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-taglibs</artifactId>
    <version>version</version>
</dependency>
<!--2, jsp页面导入自定义标签-->
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<!--3，标签的使用-->
1,authentication 获取当前正在使用用户对象 获取对象的用户名
<security:authentication property="principal.username" htmlEscape="" scope="" var=""/>
<!--
property： 只允许指定Authentication所拥有的属性，可以进行属性的级联获取,如“principle.username”，不允许直接通过方法进行调用

htmlEscape：表示是否需要将html进行转义。默认为true。

scope：与var属性一起使用，用于指定存放获取的结果的属性名的作用范围，默认我pageContext。Jsp中拥有的作用范围都进行进行指定

var： 用于指定一个属性名，这样当获取到了authentication的相关信息后会将其以var指定的属性名进行存放，默认是存放在pageConext中 
-->
    
2,authorize 用来使某些标签不再页面中显示
<security:authorize access="直接写上面判断权限的表达式" method="" url="" var="">
	<h2>可能被隐藏的标签</h2>	    
</security:authorize>
<!--
access： 需要使用表达式来判断权限，当表达式的返回结果为true时表示拥有对应的权限

method：method属性是配合url属性一起使用的，表示用户应当具有指定url指定method访问的权限，method的默认值为GET，可选值为http请求的7种方法

url：url表示如果用户拥有访问指定url的权限即表示可以显示authorize标签包含的内容

var：用于指定将权限鉴定的结果存放在pageContext的哪个属性中
-->
    
3, accesscontrollist  accesscontrollist标签是用于鉴定ACL权限的。其一共定义了三个属性：hasPermission、domainObject和var，其中前两个是必须指定的
<security:accesscontrollist hasPermission="" domainObject="" var=""></security:accesscontrollist>
<!--
hasPermission：hasPermission属性用于指定以逗号分隔的权限列表

domainObject：domainObject用于指定对应的域对象

var：var则是用以将鉴定的结果以指定的属性名存入pageContext中，以供同一页面的其它地方使用 
-->

```

## 5,获取监听器中的内容

```
//获取user对象 
SecurityContext context = SecurityContextHolder.getContext();
User user = (User) context.getAuthentication().getPrincipal();
username = user.getUsername();
```





