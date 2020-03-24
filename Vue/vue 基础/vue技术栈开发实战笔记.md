# 共计26讲

[TOC]





# 1，使用vue-cli3创建项目

## 1.1 创建一个简单的项目

+ cmd 命令窗口中使用 vue ui 命令打开创建项目窗口
+ 选择创建一个新的项目 选择功能
  + Bable 编译高版本语言
  + Router 路由
  + Vuex 状态管理
  + CSS Pre-processors css 预编译处理
  + Linter/Formatter 代码规范检测

+ 

## 1.2 添加vcCode配置文件

+ 添加一个名字为.editorconfig

```
[*.{js,jsx,ts,tsx,vue}]
indent_style = tabs //修改缩进格式
indent_size = 2 //修改缩进大小
trim_trailing_whitespace = true
insert_final_newline = true
charset = utf-8 //修改编码格式
```

## 1.3修改文件夹目录便于以后管理

> 分别创建一下文件夹
>
> api //一些api 接口
>
> config //定义配置文件
>
> directive //自定义指令
>
> lib //工具类
>
> mock //临时假数据
>
> roter //细化路由
>
> store //细化Vuex

## 1.4 临时数据访问 mock

+ 安装mockjs 依赖 npm i mockjs -D
+ 其他后续补充

## 1.5 vue.config.js 文件配置

```js
const path = require('path')
// 获取到文件的绝对路劲 并返回绝对路径加上输入的路径
const resolve = dir => {
  return path.join(__dirname, dir)
}

// 如果是开发就设置默认路径为 / 如果为生产环境就设置路径为 iview-admin
const BASE_URL = process.env.NODE_ENV === 'procution' ? '/iview-admin/' : '/'
module.exports = {
  lintOnSave: false,
  publicPath: BASE_URL,
  chainWebpack: config => {
    config.resolve.alias.set('@', resolve('src')) // 设置引入组件或者资源的时候可以使用 @来代替 src路径
    config.resolve.alias.set('_c', resolve('src/components'))
  },
  productionSourceMap: false, // 在打包时不生成.map文件
  devServer: {
    proxy: 'http://localhost:4000' // 将所有没有匹配到的资源都代理到本路径下
  }
}

```

# 2,vue路由讲解

## 2.1 路由基础篇

### 2.2.1路由组件

+ router-link 路由跳转组件
+ router-view 视图渲染组件

### 2.2.2 路由配置

```js
import Home from '../views/Home.vue'
export default [{
  // 一个路由文件必须包含的两个部分，路由路径和路由组件
  path: '/', // 匹配的路由路径
  alias: 'home_page', // 给当前的路径添加一个别名可以作用和 path一样可以直接通过别名进行页面的跳转
  name: 'home', // 路由名称 用于命名路由的加载 用于一个在一个父组件中添加多个子组件
  component: Home // 路由组件
}, {
  path: '/about',
  name: 'about',
  // route level code-splitting
  // this generates a separate chunk (about.[hash].js) for this route
  // which is lazy-loaded when the route is visited.
  // 下面属于组件懒加载，只有当路由到这个组件的时候才会加载组件内容，下面的 webpackChunkName 注解标示将会把该组件打包成 about加一个 hash值的形式
  component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
},
{
  path: '/argu/:name', // 后面的:name是 跳转的参数和 java的RestFul风格类型 获取参数使用 $route.params.参数站位名称 即可获取参数
  name: 'argu',
  component: () => import('../views/argu.vue')
},
{
  path: '/parent',
  component: () => import('@/views/parent.vue'),
  children: [{ // 当前组件的子路由组件 ，如果钱吗路径上添加/匹配的是从根路径开始的，如果不添加是以当前路径的父路径为前缀
    path: '/child',
    name: 'child',
    component: () => import('@/views/child.vue')
  }]
},
{
  path: '/named_view',
  name: 'named_view',
  components: { // 命名视图 组件
    default: () => import('@/views/child.vue'), // 当router-view组件未使用 name标记的时候会匹配到当前组件
    email: () => import('@/views/email.vue'), // 当组件以 name命名为 email 的时候会加载本组件
    tel: () => import('@/views/tel.vue') // 当组件以 name命名为 tel 的时候会加载本组件
  }
},
{
  path: '/main',
  // redirect: '/named_view' // 将 匹配到的 /main 重定向到 /named_view,
  // redirect: {
  //   name: 'home' // 使用 name的形式进行重定向
  // },
  redirect: to => {
    // to 这个对象是一个路由对象，可以通过路由对象进行一些逻辑的判断
    // 页面的跳转使用 返回值返回一个路径字符串，或者返回一个路径对象
    return {
      name: 'about'
    }
  }
}
]
```



### 2.2.3 命名路由的跳转

```
// 可以直接通过 name 指定跳转组件
<router-link :to="{name : 'child'}">添加子组件</router-link>
```

### 2.2.4命名视图

+ 通过命名视图可以再同一个路径下加载多个组件

### 2.2.4 router js 方式跳转路径

+ 返回上一步

```
this.router.go(-1);
this.router.back();
```



+ 跳转指定路径

```
简单方式的跳转
this.router.push("/parent")
```

> ### 2.安装vue-cookies
>
> ```undefined
> npm install vue-cookies --save
> ```
>
> ### 3.引入vue-cookies
>
> 安装完毕后，我们需要在vue项目中明确引入vue-cookies。
>
> ```jsx
> import Vue from 'vue'
> import VueCookies from 'vue-cookies'
> Vue.use(VueCookies)
> ```
>
> ### 4.API
>
> -设置全局配置，设置cookie过期时间和url
>
> ```cpp
> this.$cookies.config(expireTimes[,path])  // default: expireTimes = 1d , path=/
> ```
>
> -设置一个cookie
>
> ```csharp
> this.$cookies.set(keyName, value[, expireTimes[, path[, domain[, secure]]]])   //return this
> ```
>
> -获取一个cookie
>
> ```csharp
> this.$cookies.get(keyName)       // return value   
> ```
>
> -删除一个cookie
>
> ```csharp
> this.$cookies.remove(keyName [, path [, domain]])   // return this
> ```
>
> -检查某个 cookie name是否存在
>
> ```bash
> this.$cookies.isKey(keyName)        // return false or true
> ```
>
> -获取所有 cookie name，以数组形式返回
>
> ```cpp
> this.$cookies.keys()  // return a array
> ```
>
> ### 5.设置cookie过期时间
>
> ##### 5.1全局设置
>
> ```jsx
> // 30天后过期
> this.$cookies.config('30d')
> 
> this.$cookies.config(new Date(2019,03,13).toUTCString())
> 
> this.$cookies.config(60 * 60 * 24 * 30,'');
> 
> // window object
> window.$cookies.config('30d')
> ```
>
> ##### 5.2单个name设置
>
> ```cpp
> //不写过期时间，默认为1天过期
> this.$cookies.set("user_session","25j_7Sl6xDq2Kc3ym0fmrSSk2xV2XkUkX")
> 
> // 1天过期，忽略大小写
> this.$cookies.set("user_session","25j_7Sl6xDq2Kc3ym0fmrSSk2xV2XkUkX","1d")
> this.$cookies.set("user_session","25j_7Sl6xDq2Kc3ym0fmrSSk2xV2XkUkX","1D")
> 
> // 以秒为单位，设置1天过去
> this.$cookies.set("user_session","25j_7Sl6xDq2Kc3ym0fmrSSk2xV2XkUkX",60 * 60 * 24)
> 
> // 填写Date对象，明确指定过期时间
> this.$cookies.set("user_session","25j_7Sl6xDq2Kc3ym0fmrSSk2xV2XkUkX", new Date(2017, 03, 12))
> 
> // 填写一个时间字符串，指定过期时间
> this.$cookies.set("user_session","25j_7Sl6xDq2Kc3ym0fmrSSk2xV2XkUkX", "Sat, 13 Mar 2017 12:25:57 GMT")
> 
> //浏览器会话结束时过期
> this.$cookies.set("default_unit_second","input_value","0");
>  
> //永不过期
> this.$cookies.set("default_unit_second","input_value",-1); 
> ```
>
> ##### 5.3字符串单位形式设置
>
> 设置过期时间，输入字符串类型(字符均忽略大小写)：
>
> | Unit | full name |
> | ---- | --------- |
> | y    | year      |
> | m    | month     |
> | d    | day       |
> | h    | hour      |
> | min  | minute    |
> | s    | second    |
>
> ```cpp
> this.$cookies.set("token","GH1.1.1689020474.1484362313","60s");  // 60秒后过去
> 
> this.$cookies.set("token","GH1.1.1689020474.1484362313","30MIN");  // 30分钟后过去
> 
> this.$cookies.set("token","GH1.1.1689020474.1484362313","24d");  // 24天后过期
> 
> this.$cookies.set("token","GH1.1.1689020474.1484362313","4m");  // 4个月后过期
> 
> this.$cookies.set("token","GH1.1.1689020474.1484362313","16h");  // 16小时后过期
> 
> this.$cookies.set("token","GH1.1.1689020474.1484362313","3y");  // 3年后过期
> ```
>
> ### 6.其他操作
>
> ```csharp
> // set path
> this.$cookies.set("use_path_argument","value","1d","/app");  
> 
> // set domain
> this.$cookies.set("use_path_argument","value",null, null, "domain.com");  
> 
> // set secure
> this.$cookies.set("use_path_argument","value",null, null, null,true);
> ```

## 2.2路由进阶篇

### 2.2.1 路由组件传递参数

+ 类似于  RestFul 的参数类型直接将参数传递到 props 中对应

```
-- router.js 文件中配置
{
  path: '/argu/:name', // 后面的:name是 跳转的参数和 java的RestFul风格类型 获取参数使用 $route.params.参数站位名称 即可获取参数
  name: 'argu',
  component: () => import('../views/argu.vue'),
  props: true
}

-- 使用组件中配置
  props: {
    name: {
      type: [String, Number],
      default: 'list'
    }
  }
  就能获取到传递的参数了
```

+ 直接在路由上传递参数并且不显示在路径上

```
-- router.js
{
  path: '/about',
  name: 'about',
  // route level code-splitting
  // this generates a separate chunk (about.[hash].js) for this route
  // which is lazy-loaded when the route is visited.
  // 下面属于组件懒加载，只有当路由到这个组件的时候才会加载组件内容，下面的 webpackChunkName 注解标示将会把该组件打包成 about加一个 hash值的形式
  component: () => import(/* webpackChunkName: "about" */ '../views/About.vue'),
  props: {
    foot: 'now'
  }
}
-- 使用组件
 props: {
    foot: {
      type: String,
      default: ''
    }
  }
```

+ 使用函数的形式进行参数的传递

```
-- router.js 文件中进行配置
{
  props: route => {
    // 这个函数的 route形参表示当前的路由对象
    return { // 返回一个参数对象
      foot: route.query.foot
    }
  }
}
-- 组件中使用和 之前完全相同
```



### 2.2.2 HTML5 History模式

> 在配置文件中设置路由的模式 设置属性为 mode  
>
> 默认值为 hash模式 中间有一个 # 进行路径的分割
>
> 可以通过设置History 设置当前路径模式为 就是没有 # 号的形式
>
> 但是这种路由形式有相当的弊端，如果不存在定义的路径就会发生页面的跳转
>
> 所以要在匹配路由的最后添加一条 匹配所有的路由信息
>
> {
>
>   path: '*',
>
>   component: () => import('@/views/error_404.vue')
>
> }

### 2.2.3 导航守卫

#### 全局守卫

```js
// 设置全局路由守卫
const HAS_LOGINED = true
// 路由前置方法可以对路由进行拦截阻止跳转
router.beforeEach((to, from, next) => {
  // to 代表即将跳转的路由对象 from 代表当前将要离开的路由对象 next 是一个函数执行函数将会跳转页面
  if (to.name !== 'login') { // 判断如果不是登录页面
    if (HAS_LOGINED) next() // 如果已经登录了就放行跳转
    else {
      next({ // 如果没有登录就跳转到登录页面
        name: 'login'
      })
    }
  } else { // 如果是登录页面
    if (HAS_LOGINED) {
      next({ // 登录完成后就直接跳转到 home
        name: 'home'
      })
    } else next() // 否则就直接放行到登录页面
  }
})
```

#### 路由独享守卫

+ 在配置路由信息的数组中定义

```js
{
  // 一个路由文件必须包含的两个部分，路由路径和路由组件
  path: '/', // 匹配的路由路径
  alias: 'home_page', // 给当前的路径添加一个别名可以作用和 path一样可以直接通过别名进行页面的跳转
  name: 'home', // 路由名称 用于命名路由的加载 用于一个在一个父组件中添加多个子组件
  component: Home, // 路由组件
  props: route => {
    // 这个函数的 route形参表示当前的路由对象
    return { // 返回一个参数对象
      foot: route.query.foot
    }
  },
  beforeEnter: (to, from, next) => {
    // 专供当前路由页面使用的路由守卫
    next()
  }
}
```

#### 组件内守卫

```js
 // 在.vue文件中使用一下钩子函数达到 路由守卫的目的
 beforeRouteEnter (to, from, next) {
    // 当前路由的前置守卫 to代表当前组件，from代表上一组件内容
    console.log(to.name)
    next()
  },
  beforeRouteLeave (to, from, next) {
    // 当要离开当前路由的时候会执行当前函数
    alert('我要离开了')
    next()
  },
  beforeRouteUpdate (to, from, next) {
    // 同样是url发生变化的时候，且是复用组件的时候会执行本函数
    console.log('我已经渲染过了')
  }
```

#### 路由的完整执行过程

+ 导航被触发 this.router.push() 手动触发或者通过a链接达成触发
+ 修改失活组件中（即将离开的组件） 在这里调用离开守卫  **beforeRouteLeave**
+ 调用全局的前置守卫 beforeEach
+ 在重用的组件里调用 beforeRouteUpdate /beforeRouteEnter (同级的两个状态，只有当组件是被复用的时候才会调用 beforeRouteUpdate)
+ 调用路由独享的守卫 beforeEnter
+ 解析异步路由组件
+ 在被激活的组件(即将进入的页面组件) 里调用 beforeRouterEnter
+ 调用全局的解析守卫 beforeResolve
+ 导航确认
+ 调用全局的后置守卫 afterEach 所有的构子完成
+ 触发Dom更新
+ 用创建好的实例调用beforeRouterEnter 守卫里传递给 next 的回调函数

### 2.2.4  路由元信息

> 就是在路由配置文件数组中使用 meta 用于配置当前路由的元信息

### 2.2.5 过渡效果

#### 基础过渡效果

```vue
 -- template
 <transition-group name="router">
      <router-view key="default" />
      <router-view key="email" name="email"></router-view>
      <router-view key="tel" name="tel"></router-view>
 </transition-group>

  
-- css
.router-enter {
  opacity: 0;
}
.router-enter-active {
  transition: opacity 1s ease;
}
.router-enter-to {
  opacity: 1;
}
.router-leave {
  opacity: 1;
}
.router-leave-active {
  transition: opacity 1s ease;
}
.router-leave-to {
  opacity: 0;
}
```



# 3, VueX 使用

## 3.1Bus 简单版的状态管理

+ 创建一个vue实例对象挂载到 main.js 文件中

```
-- Bus文件夹下
 -- index.js文件中
 		import Vue from 'vue'
		const Bus = new Vue() //创建Vue实例对象并导出
		export default Bus

import Bus from '@/Bus' //引入文件
Vue.prototype.$bus = Bus
```



+ 使用$bus挂载事件

```
// 进行切换的按钮 点击按钮触发 on-click 事件传递参数福福
$emit 是触发一个事件
this.$bus.$emit('on-click', '福福')
```



+ 使用$bus触发事件获取参数

```
 mounted () {
    // 在本生命周期中监听 事件触发当事件触发的时候就会执行该回调函数
    // 监听和这是挂载事件都是以一个 Vue实例为基础的
$on是监听一个事件的触发
    this.$bus.$on('on-click', mes => {
      console.log(mes)
      this.message = mes
    })
  }
```



## 3.2Vuex-基础-state&getter

#### store 文件中 index.js 定义

```js
import Vue from 'vue'
import Vuex from 'vuex'
import state from './state'
import mutations from './mutations'
import actions from './actions'
import user from './module/user'
Vue.use(Vuex)

export default new Vuex.Store({
  state,
  mutations,
  actions,
  modules: {
    user
  }
})

```



#### 基础形式的 Vuex的参数定义和获取

```
用于定义全局的 状态值
const state = {
  appName: 'admin'
}
export default state
取值 在vue实例对象的计算属性中
computed: {
    appName () {
      return this.$store.state.appName
    }
}

模块的定义值和取值
在module文件中的 模块中的 state 定义状态值
-- user模块中
const state = {
  userName: '三三'
}
-- 计算属性中
userName () {
      return this.$store.state.user.userName
}
```

#### 简单形式取出状态值

```
import { mapState } from 'vuex' //引入mapState

//计算属性中定义参数
computed: {
    ...mapState({
      appName: stat => stat.appName,
      userName: stat => stat.user.userName
    })
  }
 
 -- 使用命名空间获取参数
 开启命名空间
 export default {
  namespaced: true,//开启命名空间
  state,
  mutations,
  actions
}

-- 计算属性中 user就是模块名称
...mapState('user', {
      userName: stat => stat.userName
}) 
createNamespacedHelpers 可以用于获取当前模块的 mapState对象
```

#### getters 进行一个简单的计算属性

```
-- store 配置文件中添加getters信息
const getters = {
  appNameWithVersion (state) {
    return state.appName + 'v2.0'
  }
}

export default getters

-- 在组件中调用getters 文件
-- 在计算属性中添加配置信息
appNameWithVersion () {
      return this.$store.getters.appNameWithVersion
}

-- 在store组件中定义getters 属性
const getters = {
  firstLetter: (state) => {
    return state.userName.substr(0, 1)
  }
}


-- 使用方式 获取模块中的属性

import { mapState, mapGetters } from 'vuex'
...mapGetters('user', [
      'firstLetter'
])


```

+ 注意在不使用命名空间的时候不能采用 mapstate 等的命名空间形式进行快捷获取，只能使用最笨的方法，但是 getters 中如果没有采用命名空间可以直接进行获取，因为当前getters 就变成根路径下的了

## 3.3Vuex-基础-mutation&action/module

### 3.3.1 mutation

> 用于改变 state参数的变换 在组件中不能直接改变 state 参数的变化只能通过 commit 进行数据的提交 在mutation 方法中对数据进行改变

```
-- 在mutation中进行定义 修改方法
const mutations = {
  // 方法的参数 第一个参数是state对象本身 第二个参数是传递的新的值
  SET_APP_NAME (state, param) {
    state.appName = param
  }
}

export default mutations

-- 在组件中调用方法

reAppname () {
      this.$store.commit('SET_APP_NAME', 'newAppName')
}

-- 第二种方法使用传递对象的方式修改属性

const mutations = {
  // 方法的参数 第一个参数是state对象本身 第二个参数是传递的新的值
  SET_APP_NAME (state, param) {
  	// 如果传递的是一个对象的话，要通过点对象参数的形式获取到对象中的参数
    state.appName = param.appName
  }
}

-- 组件中调用
reAppname () {
      this.$store.commit({
        type: 'SET_APP_NAME',
        appName: 'new APP Name'
      })
}
```

> 在 state 中添加一个新的状态属性

```
1，使用 Vue 的 set 方法向 state 对象中添加一个新的属性值
import Vue from 'vue'

SET_APP_VERSION (state, param) {
	// 调用 set 方法 这里为什么需要调用 set 方法因为直接使用 state 添加参数是不会为该参数添加 get set 方法的 也不会触发视图的更新，但是通过 Vue.set 方法添加的参数就会添加 get set 方法 也会触发视图的更新
    Vue.set(state, 'appVersion', param)
}

-- 组件中使用
addVersion () {
      this.$store.commit('SET_APP_VERSION', 'v2.0')
}
```

> 使用 map 形式进行简写

```
// 引入 mapMutations 对象
import { mapState, mapGetters, mapMutations } from 'vuex'
// 在Vue方法对象中使用 展开操作符
  methods: {
    ...mapMutations([
      'SET_APP_NAME'
    ]),
  }
// 就可以直接 在组件中调用 SET_APP_NAME 方法了
reAppname () {
      this.SET_APP_NAME({
        appName: 'new APP NAME 1'
      })
}
```

> 获取模块中的mutations 

```
//1，直接使用展开操作符就行了 actions muations getters 在没有打开命名空间的时候都是默认注册在 store 根路径下的
...mapMutations([
      'SET_APP_NAME'
]),
```



### 3.3.2 action

> 以异步的形式调用 mutations

```
//1. 在api文件中添加 简单配置的获取参数信息
export var getAppName = () => {
  return new Promise((resolve, reject) => {
    const err = null
    setTimeout(() => {
      if (!err) {
        resolve({
          code: 200,
          info: {
            appName: 'newAppName'
          }
        })
      } else reject(err)
    })
  })
}
// actions 中定义一个参数

import {
  getAppName
} from '@/api/app.js'

const actions = {
  updateAppName ({
    commit
  }) {
    getAppName().then(res => {
      console.log(res)
      console.log(commit)
      commit('SET_APP_NAME', res.info)
    }).catch(err => {
      console.log(err)
    })
  }
}

export default actions


// 组件中调用的两种方式
1，使用dispatch
this.$store.dispatch('updateAppName', '1234')
2，使用 map 简易的方式
...mapActions([
      'updateAppName'
]),

```

> 异步的形式更新更新数据

```
// 异步更新数据
const actions = {
  async updateAppName ({
    commit
  }) {
  	// 使用try catch 对异常进行捕捉
 	try {
      const {
        info
      } = await getAppName()
      commit('SET_APP_NAME', info)
    } catch (err) {
      console.log(err)
    }
  }
}
```



### 3.3.3 module

> 多模块的嵌套 使用 map 形式进行获取的时候命名空间使用 '模块名/模块名' 的形式表现出嵌套关系



> actions 中的方法参数 第一个有三个属性 commit 永于提交数据 state 用于表示当前模块的 state 对象 rootState 表示根路径下的 state 对象 dispatch 用于异步的提交数据

### 3.3.4 使用 $store 实例动态的创建一个模块实例

```
// 在方法的定义中使用 $store.registerModule 方法动态的添加一个 模块实例 创建的实例是在根路径下的
registerModule () {
      this.$store.registerModule('todo', {
        state: {
          todoList: [
            '我爱学习',
            '我爱学习',
            '我爱学习',
            '我爱学习'
          ]
        }
      })
}
// 在 user 模块中添加一个新的 模块
this.$store.registerModule(['user','todo'], {})

```



## 3.4Vuex-进阶

### 3.4.1 插件

> 用于store 创建 和 数据修改时做一些事情

```
-- 定义一个插件对象  其实就是一个函数
export default store => {
  console.log('stotr 已经初始化完成了')
  store.subscribe((mucation, state) => {
    console.log('数据提交修改了')
  })
}

-- 将这个插件对象引入到 store 对象实例的插件中
import saveInLocal from './plugin/saveInLocal'

export default new Vuex.Store({
  state,
  mutations,
  actions,
  getters,
  modules: {
    user
  },
  plugins: [ //将插件导入到 store 实例中
    saveInLocal
  ]
})
```

> 将数据写入到浏览器本身

```
export default store => {
  console.log('stotr 已经初始化完成了')
  if (localStorage.state) {
    console.log('重新加载数据')
    //查看是否有保存原有数据，如果有就进行还原
    store.replaceState(JSON.parse(localStorage.state))
  }
  store.subscribe((mucation, state) => {
  	//将数据保存到 localStorage 中
    localStorage.state = JSON.stringify(state)
    console.log('数据写入成功')
  })
}
```



### 3.4.2 严格模式

> 在 实例对象中 添加 strict: true 来开启严格模式
>
> 开启严苛模式后就不能直接改变 store中的参数了



> 获取当前是开发环境还是生产环境
>
> process.env.NODE_ENV === '环境的值'
>
> development 是 开发环境
>
> production 就是生产环境

### 3.4.3 vuex + 双向绑定

> vue 中的双向绑定 v-model 其实就是一个语法糖，使用v-bind进行单向绑定，然后又绑定input事件，在输入的时候动态修改 值或者参数

> 想使用双向绑定对 vuex 进行修改就是用 ：value 加 @input 在 input 事件中使用 mutations 中的方法对参数进行修改
>
> 也可以使用 定义参数的get和set方法对数据进行封装

```
 stateValue: {
      get () { //获取 store 对象中实例参数
        return this.$store.state.stateValue
      },
      set (value) { // 对store中的参数进行赋值
        this.$store.commit('SET_STATE_VALUE', value)
      }
}
```



# 4，Ajax使用

## 4.1 解决跨域问题

> 什么叫跨域问题 当请求数据的 端口或者域名不相同时就会发生跨域问题

+ 解决跨域问题两种方式

  + 在vue.config.js 文件中配置请求代理 将本域名访问不到的请求转发到别的地方

    ```js
    const path = require('path')
    // 获取到文件的绝对路劲 并返回绝对路径加上输入的路径
    const resolve = dir => {
      return path.join(__dirname, dir)
    }
    
    // 如果是开发就设置默认路径为 / 如果为生产环境就设置路径为 iview-admin
    const BASE_URL = process.env.NODE_ENV === 'procution' ? '/iview-admin/' : '/'
    module.exports = {
      lintOnSave: false,
      publicPath: BASE_URL,
      chainWebpack: config => {
        config.resolve.alias.set('@', resolve('src')) // 设置引入组件或者资源的时候可以使用 @来代替 src路径
        config.resolve.alias.set('_c', resolve('src/components'))
      },
      productionSourceMap: false, // 在打包时不生成.map文件
      devServer: {
        proxy: 'http://localhost' // 将所有没有匹配到的资源都代理到本路径下
      }
    }
    ```

    

  + 在服务器端使用添加响应头的方式 解除跨域问题

    ```
    无代码
    ```

    

## 4.2 封装axios

### 4.2.1 请求拦截/响应拦截

> 设置请求拦截
>
> -- lib/axios.js 中添加配置文件

```js
-- lib/axios.js 中添加配置文件

import axios from 'axios'
import {
  baseURL
} from '@/config'

class httpRequest { // 相当于java中的类
  constructor (baseUrl = baseURL) { // 相当于java中类的构造器
    this.baseUrl = baseUrl // 这个 this 就是指当前对象的实例
    this.queue = {} //暂时不知道是什么作用
  }
  getInsideConfig () {
    const config = {
      baseURL: this.baseUrl, // 指定 基础路径
      headers: {

      }
    }
    return config
  }
  /** 设置一个拦截器 */
  interceptors (instance) {
    // 设置请求
    instance.interceptors.request.use(config => {
      // 添加全局的loading 加载时状态
      return config
    }, error => {
      // 当出现异常的时候 就直接将 错误信息返回
      return Promise.reject(error)
    })
    instance.interceptors.response.use(res => {
      // console.log(res)
      return res
    }, error => {
      return Promise.reject(error)
    })
  }
  request (options) {
    const instance = axios.create() // 创建出一个新实例
    // 作用： 第一个参数是全局的参数定义，后面的参数是传递进来的，所以要使用后面的参数顶替掉前面的参数
    options = Object.assign(this.getInsideConfig(), options) // 该方法的作用是将两个对象合并为同一个对象 如果两个对象有相同的属性名的时候 就会用后面的那个属性名顶替掉前面的那个属性名
    this.interceptors(instance)
    return instance(options)
  }
}
export default httpRequest
```

> --  config/index.js

```js
export default {
  // 导出一个配置文件
}
export const baseURL = process.env.NODE_ENV === 'production' ? 'http://production.com' : 'http://location'
```

> -- api/index.js

```js
import HttpRequest from '@/lib/axios'
const axios = new HttpRequest()
export default axios
```

> api/user.js

```js
import axios from './index'
export const getUserInfo = ({ username }) => {
  return axios.request({
    url: '/user/getInfo',
    method: 'post',
    data: {
        username // 传递参数使用 data 参数
    }
  })
}
```

> 使用方式

```js
-- 导入文件配置
import { getUserInfo } from '@/api/user'


-- method 添加方法
getInfo () {
      getUserInfo().then(res => {
        console.log(res)
      })
}

```

> 操作队列 进行队列设置

```js
interceptors (instance, url) {
    // 设置请求
    instance.interceptors.request.use(config => {
      // 添加全局的loading 加载时状态
      if (!Object.keys(this.queue).length) {
        // Spin.show() // 判断如果队列中没有请求的话 就不调用 页面遮挡方法
      }
      this.queue[url] = true // 将请求的 url 保存到 queue 队列中
      return config
    }, error => {
      // 当出现异常的时候 就直接将 错误信息返回
      return Promise.reject(error)
    })
    instance.interceptors.response.use(res => {
      // console.log(res)
      delete this.queue[url] // 使用删除关键字对队列中的属性进行删除
      return res
    }, error => {
      delete this.queue[url] // 使用删除关键字对队列中的属性进行删除
      return Promise.reject(error)
    })
  }
```

## 4.3 Mock模拟 Ajax 响应

### 4.3.1 响应模拟

> --  mock/index.js 添加基础配置

```js
import Mock from 'mockjs'

import {
  getUserInfo
} from './response/user'
// 使用mock方法对 字段路径进行拦截并转发到 后面提供的方法上
// 第一种写法
Mock.mock('http://localhost/user/getInfo', getUserInfo)
// 第二种写法 使用正则表达式来实现路径匹配 ， 使用模板代替方法返回参数
Mock.mock(/\/getUserInfo/, {name: '丽丽'})
export default Mock
```

> -- mock/response/user.js

```js
// 定义模拟请求的方法
export const getUserInfo = (options) => {
  console.log(options)
  return {
      name: '佳佳' //返回的数据会在响应体的 data 中
  }
}
```

> 在 mian.js 中引入 mock 文件
>
> if (process.env.NODE_ENV !== 'production') require('./mock') *// 只有不是生产环境下的时候才会引入 mock 文件*

> 使用模板返回参数

```js
import Mock from '..'

export const getUserInfo = (options) => {
  const template = { // 定义一个简单的模板
    'str|2-4': '佳佳'
  }

  return Mock.mock(template) //将模板使用mock 方法生成返回值
}
```

> 自行拓展方法 -- src/mock/index.js

```js
Random.extend({ //扩展的方法
  fruit () {
    const fruit = ['apple', 'peach', 'lemon']
    return this.pick(fruit)
  }
})
Random.fruit() // => 'peach' // 调用方式
Mock.mock('@fruit')  // => 'lemon'
Mock.mock({
  fruit: '@fruit' // => 'peach'
})
```



### 4.3.2 Mock用法精讲

# 5，Vue的第三方组件

## 5.1 组件的封装基础

```js
<template>
  <div>
    <slot name="slotA"></slot>
    <span :id='eleId'></span>
  </div>
</template>

<script>
import CountUp from 'countup' // 引入动态生成参数
import { setTimeout } from 'timers'
export default {
  name: 'CountTo',
  computed: {
    eleId () {
      // 为了保证多个组件间的Id不重复，使用计算属性生成一个随机的ID值
      // console.log(this._uid)
      return `count_up_${this._uid}` // 每个Vue实例种都会有一个唯一 ID 值保证全局的自增
    }
  },
  data: function () {
    return {
      counter: {}
    }
  },
  props: {
    /**
     * 起始值
     */
    startVal: {
      type: Number,
      default: 0
    },
    /**
     * 最终值
     */
    endVal: {
      type: Number,
      required: true
    },
    /**
     * 延迟使用动画 使用毫秒
     */
    delay: {
      type: Number,
      default: 0
    },
    /**
     * 表示小数点后保留几位小数
     */
    decimals: {
      type: Number,
      default: 0
    },
    /**
     * 表示这个动画会持续多久时间
     */
    duration: {
      type: Number,
      default: 2
    },
    /**
     * 是否使用变速动画效果
     */
    useEasing: {
      type: Boolean,
      default: false
    },
    /**
     * 设置是否有分组效果
     */
    useGrouping: {
      type: Boolean,
      default: true
    }

  },
  mounted () {
    // 页面渲染时的钩子函数
    this.$nextTick(() => {
      // 页面完全渲染完成后执行的钩子 在这里创建countUp实例
      this.counter = new CountUp(this.eleId, this.startVal, this.endVal, this.decimals, this.duration, {
        useEasing: this.useEasing, // 动画速度先慢后快
        useGrouping: this.useGrouping,
        separator: ',',
        decimal: '.'
      })

      setTimeout(() => {
        this.counter.start()
      }, this.delay)
    })
  }
}
</script>

<style lang="scss" scoped>
</style>

-- 使用 更新 countUp 组件的内置方法 
-- 使用实例对象 countUp.update('更新的参数') 
```

> 插槽使用

```
vue 的插槽方式传递参数
	1，在父组件调用子组件的时候，在组件中添加任意内容
	2，在子组件使用时 在组件内部添加 <slot></slot> 代表 父组件中 添加子组件的所有 内容
	3，为slot 添加名字
		1，在父组件定义插槽是 添加插槽名
			 <template v-slot:slotA>
        <span  style="font-size='2opx'">{{ item }}</span>
      </template>
		2，在子组件中添加 插槽
			<li class="item">
        <slot name="slotA">{{it}}</slot>
    </li>
	3，作用域插槽
		1，由子组件使用 v-bind 传递一个对象给父组件
			<li class="item">
        <input type="checkbox" v-model="checked">
        <slot name="slotA" v-bind="{checked}"></slot>
    </li>
		2，在父组件中使用 声明插槽名的属性后声明出获取 传递对象的变量名
			<template v-slot:slotA='data'>
        <span  :style="{fontSize:'2opx',color: data.checked ? 'red':'green'}">{{ item }}</span>
      </template>
		3，传递出来的是一个对象 对象的名字就是 声明出的变量名
```



## 5.2 组件中使用ID值

## 5.3 组件中获取DOM

```
获取DOM元素和组件
	获取DOM元素
		1，在元素上添加 ref 属性将标签注册到 vue对象的 $refs 对象中
			<h2 ref="myh2" id="myh2">我爱福福</h2>
		2，在方法上通过 this.$refs.键名 获取到DOM 对象
			this.$refs.myh2.innerText
		ref 等于 reference 是引用的意思
		如果有多个 相同键名的 ref 属性的时候以后面的 为准
	获取组件对象
		1，在组件上添加 ref 属性 一样可以通过 this.$refs.键名获取 组件对象
			<login ref='zi'></login>
		2，有了组件对象就可以获取到组件中的所有方法和参数了
			this.$refs.zi.name
			通过这种方式就可以进行父向子传递参数
```

# 6 SplitPane组件谈Vue中如何操作 DOM

## 6.1 简单两列布局

### 6.1.1 less 语法概述

> less 支持在同一个 选择器中添加 一个新的选择器 表示选择器下的子选择器
>
> 例 
>
> .pane {
>
> ​    height: 100%;
>
> ​    &-left {
>
> ​      *// & 表示父级元素的选择器 这次代表的是 .pane*
>
> ​      background: orange;
>
> ​    }
>
> ​    &-right {
>
> ​      background: red;
>
> ​    }
>
>   }

## 6.2 如何让两个div改变宽度

```less
.split-pane-wrapper {
  width: 100%;
  height: 100%;
  position: relative;
  .pane {
    position: absolute;
    float: left;
    top: 0;
    height: 100%;
    &-left {
      background: orange;
    }
    &-right {
      right: 0;
      bottom: 0;
      // left: 30%;
      background: red;
    }
    &-trigger-con {
      width: 8px;
      height: 100%;
      background: skyblue;
      z-index: 1;
    }
  }
}
```

> 动态修改的一种方式
>
> :style="{ left: leftOffSetPercent }"  使用数据驱动视图模型的改变

## 6.3 鼠标拖动效果

> css 的计算方法
>
> calc( 在这里可以进行简单的计算 ) 

```vue
<template>
  <div class="split-pane-wrapper" ref="outer">
    <div class="pane pane-left" :style="{ width: leftOffSetPercent }">
      <button @click="handleClick">点击转换</button>
    </div>
    <div class="pane pane-trigger-con" @mousedown="handleMousedown" :style="{ left: triggerLeft,width: `${trigger}px` }"></div>
    <div class=" pane pane-right" :style="{ left: leftOffSetPercent }"></div>
  </div>
</template>

<script>
export default {
  props: {
    trigger: {
      type: Number,
      default: 8
    },
    max: {
      type: Number,
      default: 0.9
    },
    min: {
      type: Number,
      default: 0.1
    }
  },
  data () {
    return {
      leftOffSet: 0.3,
      canMove: false,
      initOffset: 0
    }
  },
  computed: {
    leftOffSetPercent () {
      // 计算出 两个 方块的所处位置
      return this.leftOffSet * 100 + '%'
    },
    triggerLeft () {
      // 使用计算属性计算出 triggerLeft 计算出中间的滑动框所处位置
      return `calc(${this.leftOffSet * 100}% - ${this.trigger / 2}px)`
    }
  },
  methods: {
    handleClick () {
      this.leftOffSet += 0.01
    },
    handleMousedown (event) {
      // 鼠标点击的时候添加 鼠标移动事件
      document.addEventListener('mousemove', this.handleMouseMove)
      document.addEventListener('mouseup', this.handleMouseup)
      // 计算出 中间隔离条鼠标点击位置距离 中间隔离条左边界的范围的
      this.initOffset = event.pageX - event.srcElement.getBoundingClientRect().left
      this.canMove = true
    },
    handleMouseMove (event) {
      if (!this.canMove) return
      const outerRect = this.$refs.outer.getBoundingClientRect()
      // 使用计算的边界返回 将距离边界的范围 加上隔离条的宽度的一半
      let offSet = (event.pageX - this.initOffset + this.trigger / 2 - outerRect.left) / outerRect.width
      if (offSet < this.min) offSet = this.min
      if (offSet > this.max) offSet = this.max
      this.leftOffSet = offSet
    },
    handleMouseup () {
      this.canMove = false
    }
  }
}
</script>

<style lang="less" scoped>
.split-pane-wrapper {
  width: 100%;
  height: 100%;
  position: relative;
  .pane {
    position: absolute;
    float: left;
    top: 0;
    height: 100%;
    &-left {
      background: orange;
    }
    &-right {
      right: 0;
      bottom: 0;
      // left: 30%;
      background: red;
    }
    &-trigger-con {
      width: 8px;
      height: 100%;
      background: skyblue;
      z-index: 1;
      // 取消页面的拖拉选中
      user-select: none;
      cursor: col-resize; // 设置鼠标的样式
    }
  }
}
</style>
```



## 6.4 v-model和.sync 的用法

> v-model 相当于 会自动添加了一个 @input 事件用于在页面改变的时候修改数据
>
> .sync 是用于添加参数

# 7， 渲染函数和JSX 快速掌握

## 7.1 render 函数

```js

const handleClick = event => { // 点击事件绑定的函数
  console.log(event)
  event.stopPropagation()
}

let list = [{
  name: 'lison'
}, {
  name: 'lili'
}]
const getLiEleArr = (h) => {
  console.log('2313')
  return list.map(item => { // map 方法会遍历当前数组 如果有 return 的参数的时候会将返回值组装为一个数组并返回
    return h('li', {
      on: {
        'click': handleClick
      }
    }, item.name)
  })
}

new Vue({
  router,
  store,
  /**
   * 方法需要传入三个参数
   * 第一个 元素类型 或者 一个组件对象
   * 第二个 元素的配置参数
   * 第三个 元素的填充内容
   */
  render: h => h(CountTo, {
    'class': [''], // 为组件添加类名 数组用于添加多个类名 {'count-to': true}  添加类名
    props: { // 用于组件传递参数
      endVal: 100
    },
    domProps: {
      // DoM 上的一些属性参数
    },
    on: { // 用于自定义定义组件事件方法

    },
    nativeOn: { // 绑定本地原生事件
      'click': () => {
        console.log('佳佳')
      }
    },
    directives: [
      // 添加多个自定义指令
    ],
    slot: '', // 插槽当组件被当做插槽使用的时候使用
    key: '', // v-for 中的key值
    ref: '' // 用于获取 当前组件对象或者是原生Dom对象
  }, 'list'),
  render: h => h('div', [
    h('ul', {
      on: {
        'click': handleClick
      }
    }, getLiEleArr(h))
  ])
}).$mount('#app')
```



## 7.2 函数式组件

```js
export default {
  functional: true, // 表示本函数是一个函数式组件
  props: {
    name: String,
    renderFunc: Function
  },
  render: (h, ctx) => { // 在这个函数内对页面进行渲染
    return ctx.props.renderFunc(h, ctx.props.name)
  }
}
```



## 7.3 JSX

```js
methods: {
    renderFunc (h, number) { //更简单的组件函数渲染 ， 可以直接写 HTML 代码
      return ( // 绑定原生事件  绑定自定义事件 on-事件名就行 传递参数都是使用 {} 相当于 “” 来进行组件间传递参数
        <CountTo nativeOn-click={this.handleClick} endVal={number} ></CountTo>
      )
    },
    handleClick (event) {
      console.log(event)
    }

  }
```



## 7.4 作用域插槽

> 作用域插槽 的使用就是在子组件中添加 一个 插槽并 绑定一个对象参数
>
> 在父组件中就可以通过 slot-scope 来获取到子组件插槽中所有的参数

# 8 递归组件的使用

## 8.1 封装简单Menu组件

## 8.2 递归组件 

# 1000 重点 springBoot 解决跨域问题

> 在对应的方法上添加 @CrossOrigin 该方法就解决了跨域问题
>
> 在对应的类上添加一个@CrossOrigin 注解本类的所有方法都解决了跨域问题

# 101 css 样式表

> 盒子塌陷阴影 box-shadow: 0 0 0 gray, 5px 5px 5px gray, 0 5px 5px gray, -5px 5px 5px gray;