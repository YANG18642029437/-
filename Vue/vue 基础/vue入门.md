[TOC]



# 1，Vue的基本结构

## 1-1 前端MVVM结构

> M model 这是页面的数据源
>
> vm 数据源和页面之间的调度
>
> v 页面展示数据源中的数据

## 1-2 Vue中的常见指令

### 1-2-1  v-cloak  指令

> 用于解决 插值表达式 闪烁问题
>
> 标签添加属性 v-cloak 
>
> **<style> 标签中 添加 [v-cloak]{display:none} 加载完成后 v-cloak 自动删除**

###   	1-2-2 v-text 指令

> 用于向标签中添加值  
>
> **<h2 v-text="属性名"></h2>**
>
> v-text 不会出现闪烁问题
>
> v-text 会清空标签的内容体  插值表达式不会清空标签的内容

### 	1-2-3 v-html 指令

> 用法和 v-text 几乎相同 但是可以向标签中添加 为 html 内容
>
>  **<p v-html="msg2"></p>**

### 	1-2-4 v-bind 指令

```javascript
<div id="app" >
    <button @click="flag = !flag">点我切换</button>
	//使用 v-bind 进行数据绑定的时候可以使用 数组或者对象进行控制
    <div id="box" :class="{red:flag,green:!flag}"> 
        我是盒子
    </div>
</div>
```



>  
>
>  v-bind 式vue中用于 提供绑定属性的指令
>
>  **<button  v-bind:title="title">打人了</button>**
>
>  vue 会将 v-bind:tiele="" 中间的值当作一个变量 可以使用 字符串拼接 
>
>  **<button  v-bind:title="title + '爱你'">打人了</button>**
>
>  简写格式 去掉v-bind 只留下 ：
>
>  **<button  :title="title + '爱你'">打人了</button>**



### 	1-2-5 v-on 事件绑定 指令

> 用于绑定事件 值是一个js表达式
>
> **<button  :title="title + '爱你'" v-on:click="show('福福我爱你你你你')">打人了</button>**
>
> 缩写格式 去掉 v-on: 换成 @ 符号
>
> v-on 修饰的事件中的值除了能填写 方法名外还能直接写 方法的内容,就像 html的内敛方法设定一样
>
> v-on 事件的修饰符 使用方式 v-on:clicke.stop="" 阻止事件的冒泡 ，事件修饰符可以进行串联
>
> + .stop 阻止冒泡
> + .prevent 阻止默认事件
> + .capture 添加事件监听器时使用事件捕获模式 //和冒泡机制相反是由内向外的
> + .self 只当事件在该元素本身(比如不是子元素) 触发式触发回调函数 //只有点我自己本身才会触发事件，别的都不会触发
> + .once 事件只触发一次 //被修饰分方法只会触发一次



### 1-2-6 v-model 实现双向数据绑定

>  v-bind 只能实现单向数据绑定，从 M 自动绑定到 V 中
>
> v-model 可以实现双向数据绑定，可以实现 M 和 V 之间的自由切换 只适用于表单元素
>
> v-model:value 可以简写省略 :value             v-model='变量值'

### 	1-2-7 v-for 实现循环

#### 1-2-7-1 使用数组进行迭代

```html
<div id="app">
    <ul>
        <!-- 使用表达式对数组进行遍历, ("临时遍历","索引") in "遍历对象" -->
        
        <!-- 一般写法 v-for="item in list" -->
        <li v-for="(item,i) in list">{{ item }}  ,  索引{{ i }}</li>
        <!--直接获取到list中的索引对应值-->
        <li>{{ list[0] }}</li>
    </ul>
</div>

<script>
    var vm = new Vue({
        el:"#app",
        data:{
            <!-- 预先声明出要遍历的对象 -->
            list:['福福','我','想你']
        },
        methods:{}
    })
</script>
```

#### 1-2-7-2 对象循环

```html
<div id="app">
    <ul>
        <!--("值","键名") 可以通过 obj['键名'] 获取值-->
        <li v-for="(item,key) in obj">{{ key }}   ===> {{ item }} ====> {{obj[key]}}</li>
    </ul>
</div>

<script>
    var vm = new Vue({
        el:"#app",
        data:{
            list:['福福','我','想你'],
            obj:{name:"福福",age:20,sex:"男"}
        },
        methods:{}
    })
</script>
```

#### 1-2-7-3 对数字进行遍历

```html
<div id="app">
    <ul>
        <li v-for="count in 10">{{ count }}</li>
    </ul>
</div>

<script>
    var vm = new Vue({
        el:"#app",
        data:{
            list:['福福','我','想你'],
            obj:{name:"福福",age:20,sex:"男"}
        },
        methods:{}
    })
</script>
```

#### 1-2-7-4 v-for 循环中 key 使用

```html
<!--为标签添加 唯一标识 使用 key属性 再使用 v-bind:key="对象" 实现对象对当前标签的绑定
可能是版本原因,教学数 key的值不能使用对象只能使用数字或者字符串,但是我可以使用对象作为key值
默认情况下是使用 索引对页面的数据进行绑定,但是可能出现问题,使用 :key="唯一值" 使用值绑定 再下一次渲染页面的时候就会保留上次的渲染结果
-->
<div id="app">
    <div>
        <input type="text" v-model="id">
        <input type="text" v-model="name">
        <input type="button" value="添加user" @click="add">
        <p v-for="item in user" :key="item">
            <input type="checkbox" >
            id为{{ item.id }}=====> 名字{{ item.name }}
        </p>
    </div>
</div>

<script>
    var vm = new Vue({
        el:"#app",
        data:{
            id:"",
            name:"",
            user:[
                {id:1,name:"福福"},
                {id:2,name:"牛牛"},
                {id:3,name:"六六"},
                {id:4,name:"爱爱"},
                {id:5,name:"香香"}
            ]
        },
        methods:{
            add(){
                var user = {id:this.id,name:this.name};
                this.user.unshift(user);
            }
        }
    })
</script>
```

#### 1-2-8 v-showand v-if 的使用

```html
<!-- 
v-if 为false是会将元素删除 
	由较高的切换性能消耗
	if可以使用 v-else 开进行分支判断 两个span之间不能使用别的标签
v-show 会将元素隐藏而不是删除
	有较高的初始渲染消耗

-->

<div id="app">
    <ul>
        <li v-for="(item,i) in users">
            <span v-if="i%2==0">
                姓名：{{ item.name }} <br>
                性别：{{ item.sex }}
            </span>
            <span v-else>我是奇数哦</span>
        </li>
    </ul>
    <input type="button" v-model="name" @click="update">
	<!--是否显示 本标签 如果为true 输出-->	
    <h3 v-if="flag">我爱昌福</h3>
    <!--是否显示 本标签 如果为true 输出-->
    <h3 v-show="flag">我不爱昌福</h3>
</div>

<script>
    var vm = new Vue({
        el:"#app",
        data:{
            flag:true,
            name:"隐藏"
        },
        methods:{
            update(){
                this.name= this.name == "隐藏" ? "显示" : "隐藏";
                this.flag = !this.flag;
            }
        }
    })
</script>
```

## 1-3 过滤器的定义

```html
	//定义一个全局过滤器 对数据进行筛选
    //过滤器的定义一定要放到 vue 实例对象创建之前
    //获取参数 但是第一个参数固定式 管道符前的字符串，后面的可以随意拼接
    //一个方法的·参数可以设置默认值 function run1 (v1='福福') 如果没有传入参数 v1 的默认值就是 '福福'
    Vue.filter("convert",function (data,pattern) {
    	return '返回数据参数'
    }

	<!--使用过滤器对数据进行格式化-->
    <!-- | convert("传递参数") 但是第一个参数永远是管道符前面的变量值-->
    <!--过滤器可以多次调用 {{ item.ctime | filter1 | filter2 }}
      过滤器是顺序执行的 先执行前面的过滤器，再执行后面的过滤器
    -->
     <td>{{ item.ctime | convert('') }}</td>
```

## 1-4 绑定按键修饰

### 1-4-1 enter

```html
//用于绑定键盘的enter监听事件
<input type="text" class="form-control" v-model="name" @keyup.enter="add">
```

### 1-4-2 其他内置 按键修饰

```
 .tab
 .delete
 .esc
 .space
 .up
 .down
 .left
 .right
```

### 1-4-3 自定义键值修饰符

```
1，使用Vue.config.keyCodes 进行定义
Vue.config.keyCodes."自定义名字" = 112 //f1 的键盘码的值 就能将键盘码绑定到 vue对象中
														//直接使用码值
<input type="text" class="form-control" v-model="name" @keyup.112="add">

Vue.config.keyCodes.f1 = 112;
```

## 1-5 自定义Vue指令

```javascript
// 注册一个全局自定义指令 `v-focus`
Vue.directive('focus', {
  // 当被绑定的元素插入到 DOM 中时……
  inserted: function (el) {
    // 聚焦元素
    el.focus()
  }
})


//如果想注册局部指令，组件中也接受一个 directives 的选项：
directives: {
  focus: {
    // 指令的定义
    inserted: function (el) {
      el.focus()
    }
  }
}
```

### 1-5-2 钩子函数

```
	//和渲染样式有关的可以放到 bind 执行
    bind：只调用一次，指令第一次绑定到元素时调用。在这里可以进行一次性的初始化设置。(只执行一次)
	//和行为有关的 最好放到 inserted 中
    inserted：被绑定元素插入父节点时调用 (仅保证父节点存在，但不一定已被插入文档中) (只执行一次)。

    update：所在组件的 VNode 更新时调用，但是可能发生在其子 VNode 更新之前。指令的值可能发生了改变，也可能没有。但是你可以通过比较更新前后的值来忽略不必要的模板更新 (会执行多次)

    componentUpdated：指令所在组件的 VNode 及其子 VNode 全部更新后调用。

    unbind：只调用一次，指令与元素解绑时调用
```

### 1-5-3 钩子函数的参数 - 就是钩子函数上的参数值

```
    //第一个参数
    el：指令所绑定的元素，可以用来直接操作 DOM 。
    //第二个参数
    binding：一个对象，包含以下属性：
        name：指令名，不包括 v- 前缀。
        value：指令的绑定值，例如：v-my-directive="1 + 1" 中，绑定值为 2。
        oldValue：指令绑定的前一个值，仅在 update 和 componentUpdated 钩子中可用。无论值是否改变都可用。
        expression：字符串形式的指令表达式。例如 v-my-directive="1 + 1" 中，表达式为 "1 + 1"。
        arg：传给指令的参数，可选。例如 v-my-directive:foo 中，参数为 "foo"。
        modifiers：一个包含修饰符的对象。例如：v-my-directive.foo.bar 中，修饰符对象为 { foo: true, bar: true }。
    vnode：Vue 编译生成的虚拟节点。移步 VNode API 来了解更多详情。
    oldVnode：上一个虚拟节点，仅在 update 和 componentUpdated 钩子中可用。

```

### 1-5-4 函数的简写形式

```javascript
//在很多时候，你可能想在 bind 和 update 时触发相同行为，而不关心其它的钩子。比如这样写:

Vue.directive('color-swatch', function (el, binding) {
  el.style.backgroundColor = binding.value
})
```



### 1-5-5 对象字面量参数

```javascript
//如果指令需要多个值，可以传入一个 JavaScript 对象字面量。记住，指令函数能够接受所有合法的 JavaScript 表达式。

<div v-demo="{ color: 'white', text: 'hello!' }"></div>

Vue.directive('demo', function (el, binding) {
  console.log(binding.value.color) // => "white"
  console.log(binding.value.text)  // => "hello!"
})
```

## 1-6 Vue 实例的生命周期

### 1-6-0 生命周期的定义

```
1，生命周期钩子 = 生命周期函数 = 生命周期事件
```

![生命周期的图片](D:\temp\xmind\vue\img\lifecycle.png)

### 1-6-1 创建期间的生命周期函数

```javascript
		beforeCreate(){//第一个生命周期函数，创建实例完全创建之前会执行本函数

        },
        created(){//这是遇到的第二个生命周期函数 这是data 和 methods 对象中的数据已经被加载到vue实例中了

        },
        beforeMount(){//这是遇到的第三个生命周期函数，表示模板已经在内存中编辑完成了，但是没有将模板渲染到 页面中

        },
        mounted(){//这是遇到的第四个生命周期函数，表示，内存中的模板已经加载到页面中了，用户可以看到渲染好的页面了，这是实例创建最后一个生命周期函数，执行完本方法表示实例已经完全创建好了

        }
```

### 1-6-2 运行阶段生命周期函数

```javascript
		beforeUpdate(){//这个方法执行时表示页面还未进行更新，【但是数据已经被更新了】

        },
        updated(){//这个方法执行之后表示页面和数据都已经进行过更新了

        }
```

## 1-7 computed 属性 对参数进行格式化

```javascript
var vm = new Vue({
        el:"#app",
        data:{
            bgc:"red",
            flag:true,
            birthdayTime:new Date().getTime()
        },
        methods:{},
        // 使用计算获取到参数
        computed:{
            // 看着像是一个 方法其实其本质还是一个参数 在方法中经过计算将返回值赋值给参数
            birthday(){
                const date = new Date(this.birthdayTime);
                return `${date.getFullYear()}年${date.getMonth()}月${date.getDate()}日`;
            }
        }
    })
```

## 1-8 watch 监控vue实例对象中 参数值的变化

```javascript
        watch:{
            //浅监控只能监控基本数据类型
            num(agr1,agr2){ //num 参数发生变化时执行方法 agr1是表示新值 agr2表示旧值
                console.log("num发生变化了   "+this.num);
            }
            user:{ //深度监控 可以用于监控对象中的值变化
                deep:true,
                handler(var1,var2){
                    console.log(var1,var2);
                }
            }
        }
```

## 1-9 组件 的定义

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="node_modules/vue/dist/vue.js"></script>
</head>
<body>

<div id="app">
    <button @click="add">点击我添加组件</button>
    <div  v-html="counter">
        <!--  局部组件能够定义  -->
        <counter></counter><counter></counter>
    </div>
    <!--  声明出一个id同名的标签就会自动解析组件进行填充  全局组件才能使用 -->
    <counter></counter><counter></counter><counter></counter>
</div>

<script>
    //添加全局组件
    Vue.component("counter",{ //声明出组件的id
        //绑定到 template上 避免数据和页面的紧密关系
        template:"<button @click=\"count++\">爱你偶：爱了{{ count }} 次</button>",
        // 参数的声明必须使用 返回值的形式
        /**
         * 为什么使用 返回值的形式，就是为了避免一个组件使用多次的时候，多个组件间的参数相互影响
         * 所以使用返回值的形式 每一次创建实例对象的时候都会执行一次
         * @returns {{count: number}}
         */
        data(){
            return{
                count:0
            }
        }
    });

    //定义局部组件
    const  counter = {
        template:"<button @click=\"count++\">爱你偶：爱了{{ count }} 次</button>",
            data(){
            return{
                count:0
            }
        }
    };

    const vm = new Vue({
        el:"#app",
        data:{
            counter:""
        },
        methods:{
            add(){
                this.counter="<counter></counter>";
            }
        },
        components:{
            //当key和value的值相同的时候就可以使用 简写版本
            //counter:counter
            counter
        }
    })
</script>
</body>
</html>
```

## 1-10 组件间的参数传递

### 1-10-1 父子间的参数传递

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="node_modules/vue/dist/vue.js"></script>
</head>
<body>

<div id="app">
    <!--  创建一个组件 并且通过属性传递参数到子组件中  -->
    <cc :title="msg"></cc>
</div>

<script>
    const cc = {
        template:"<h2>{{ title }}</h2>",
        //子组件通过这种方式就可以获取到 父组件传递的参数并展示
        props:["title"]
    };
    
    
    //复杂用法
    const cc = {
        template:"<h2>{{ title }}</h2>",
        //子组件通过这种方式就可以获取到 父组件传递的参数并展示
        props:{
            title:{
                type:Array, //指定参数类型
                default:['java'] //指定参数的默认值
            }
        }
    };


    var vm = new Vue({
        el:"#app",
        data:{
            msg:"我真的是好爱福福偶！！！！"
        },
        methods:{},
        components:{
            cc
        }
    })
</script>
</body>
</html>
```



# 2，Vue入门,注解解析

## 2-1，入门案例

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <!--导入vue的包-->
    <script src="lib/vue.min.js"></script>
</head>
<body>
<!--将来 new 的 vue 实例会控制这个元素中的所有内容-->
<!--vue实例所控制的实例区域就是 MVVM 中的 V 用于展示数据-->
<div id="app">
    <!--将数据引入到页面上-->
    <p>{{ msg }}</p>
</div>

<!--创建一个vue的实例-->
<script>
    //2,创建vue的实例
    //当我们导入包之后，在浏览器内存中，就多了一个 Vue 函数
    // 这个new 出来的 vue 对象就是mvvm 中 vm 属于调度者
    var vm = new Vue({
        el:"#app", //设置当前的vue对象作用于页面中的那块区域
        // 这个 data 就相当于 MVVM 中的 M 相当于数据
        data:{
            msg:"hello word"
        }//data中的属性存放的 是 el 中用到的数据 通过vue的指令，很方便就能把数据渲染到页面上程序员不在手动操作Dom元素了
        methods:{//定于vue实例中所有可用的方法
        
    	}
    });
</script>
</body>
</html>
```



# 3，案例实践

## 3-1跑马灯案例

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="lib/vue.min.js"></script>
</head>
<body>
<div id="app">
    <input type="button" value="浪浪" @click="long">
    <input type="button" value="低调" @click="stop">
    <p>{{ msg }}</p>
</div>
<script>
    var vm = new Vue({
        el: "#app",
        data: {
            msg: "猥琐发育，别浪 ~ ~ ~ ~",
            timeId:null
        },
        methods: {
            long() {
                clearInterval(this.timeId);
                //嵌套函数写法，相当于是一个自调用函数 对象还是自己本身
                this.timeId = setInterval(() => {
                    console.log(this.msg);
                    var top = this.msg.substring(0, 1);
                    var str = this.msg.substring(1) + top;
                    this.msg = str;
                },200)
            },
            //vue 会监听自己身上所有 data 中所有数据的改变只要数据发送变化就会自动把数据同步到页面中去【好处：只需要关系数据，不需要关系数据就行了，不需要】
            stop(){
                clearInterval(this.timeId);
            }
        }
    });
</script>
</body>
</html>
```

## 3-2 品牌列表案例

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="lib/vue.js"></script>
    <link rel="stylesheet" href="lib/bootstrap.css"/>
</head>
<body>

<div id="app">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class='panel-title'>添加品牌</h3>
        </div>
        <div class="panel-body form-inline">
            <label>
                id:
                <input type="text" class="form-control" v-model="id">
            </label>
            <label>
                name:
                <input type="text" class="form-control" v-model="name" @keyup.f1="add">
            </label>
            <button class="btn btn-default" @click="add">添加</button>
            <label>
                搜索的关键字:
                <input type="text" v-model="keywords" class="form-control" v-focus v-color="{color:'red',text:600}">
            </label>
            <input type="button" class="btn btn-primary" value="搜索">
        </div>
    </div>


    <table class="table table-bordered table-hover table-striped">
        <thead style="background-color: #5bc0de">
        <tr>
            <th>id</th>
            <th>name</th>
            <th>ctime</th>
            <th>oper</th>
        </tr>
        </thead>
        <tbody>
        <!--循环展示数据的信息-->
        <!--对方法的返回值进行遍历，当keywords发生变化的时候，会执行这个方法，并且会重新渲染页面的展示数据-->
        <tr v-for="item in search(keywords)" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <!--使用过滤器对数据进行格式化-->
            <!-- | convert("传递参数") 但是第一个参数永远是第一个-->
            <!--过滤器可以多次调用 {{ item.ctime | filter1 | filter2 }}
                过滤器是顺序执行的 先执行前面的过滤器，再执行后面的过滤器
            -->
            <td>{{ item.ctime | convert('') }}</td>
            <!--阻止点击的默认事件-->
            <td><a href="" @click.prevent="remove(item.id)">删除</a></td>
        </tr>

        </tbody>
    </table>
</div>
<script>
    //正则表达式的定义 全局的函数定义 字符串中所有的单纯都能匹配到
    var rex = /单纯/g;

    //自定义全局的按键修饰符
    Vue.config.keyCodes.f1 = 112;

    //自定义全局的指令
    //其中参数一是指令的名称，指令名称不需要加入 v- 前缀使用的使用要加入 v- 前缀
    //参数二是一个对象：身上有指令相关的的钩子函数，这些函数可以在特定的阶段，执行相关的操作
    //添加指令对象
    Vue.directive("focus", {
        //函数的第一个参数就是绑定了 指令的元素Dom对象
        bind: function (e1) {
            console.log("弟弟");
        },
        inserted:function (e1) {
            console.log("gg");
            e1.focus();
        }
    });
  
    

    /*//修改元素的字体颜色的指令定义
    Vue.directive("color",{
       bind:function (ele,color) {
           console.log(color.expression);
           ele.style.color = color.value;
       }
    });*/
    
    //修改字体颜色的快捷定义方法式 //相当于 同时写了 bind 和 update
    Vue.directive("color",function (ele,binding) {
        ele.style.color = binding.value.color;
        ele.style.fontWeight = binding.value.text;
    });
    


    //定义一个全局过滤器 对数据进行筛选
    //过滤器的定义一定要放到 vue 实例对象创建之前
    //获取参数 但是第一个参数固定式 管道符前的字符串，后面的可以随意拼接
    //一个方法的·参数可以设置默认值 function run1 (v1='福福') 如果没有传入参数 v1 的默认值就是 '福福'
    Vue.filter("convert", function (data, pattern) {
        //获取时间
        var year = data.getFullYear();
        var month = data.getMonth();
        month = (month + "").padEnd(2, "10");

        var day = data.getDate();

        //字符串格式化模板 使用 ~ 键 就能开启字符串模板 ${}是开启一个占位符
        var str = `${year}-${month}-${day}`;

        return str;
    });


    var vm = new Vue({
        el: "#app",
        data: {
            list: [
                //临时数据
                {id: 1, name: "宝马", ctime: new Date()},
                {id: 2, name: "奔驰", ctime: new Date()},
                {id: 3, name: "长安", ctime: new Date()},
                {id: 4, name: "解放", ctime: new Date()}
            ],
            id: null,
            name: null,
            keywords: ''
        },
        methods: {
            remove(id) {
                //forEach 第一个元素是 list集合中的一个元素 第二个是循环的索引
                //some 循环 写法和 forEach一样但是 可以使用return true 终止循环
                this.list.forEach((p1, p2) => {
                    if (p1.id == id) {
                        //删除数组元素的方法 第一个元素是 开始删除索引 后面元素是删除的个数
                        this.list.splice(p2, 1);
                    }
                });

            },
            add() {
                // console.log(this.list);
                this.list.push({id: this.id, name: this.name, ctime: new Date()});
                this.id = this.name = '';
            },
            //筛选 当keywords 发生变化的时候这个方法就会被重新调用，返回晒选后的数组
            search(text) {
                /*var arr = [];
                this.list.forEach((item) => {
                    if (item.name.indexOf(text) != -1){
                        arr.push(item);
                    }
                    //this.keywords = '';
                });*/
                //数组方法 将符合条件的对象返回，就是进行筛洗
                var arr = this.list.filter(item => {
                    //判断string是否包含另一个字符串
                    if (item.name.includes(text)) {
                        //返回值会填充到一个数组中
                        return item;
                    }
                });
                return arr;
            }
        },
        //用于定义私有过滤器 定义过滤器方法和 method 方法一样 当有私有过滤器的时候要会优先使用同名的私有过滤器
        filters: {}
    });
</script>

<!--
    知识小结
        1，什么叫做全局什么叫局部
            一个vue实例对象只能影响一个div标签的内容，外面的数据操控不了 这个div就是这个vue实例的局部环境 这个vue实例对象中的数据只能再本局部变量中使用

        2，设置一个局部的过滤器
            默认情况下Vue.filter() 设置的过滤器是全局过滤器，所有的vue实例对象
            使用Vue实例对的一个filters 属性定义局部的过滤器
            当有私有过滤器的时候要会优先使用同名的私有过滤器

        3,为支付串补长 如果不够长就在前面或者后面添加填充物
                                    设定字符规定长度，不够时填充的组件
            month = (month+"").padStart(2,"0"); //向头部添加数据
            month = (month+"").padEnd(2,"10");  //向尾部添加数据
-->
</body>
</html>
```

## 3-3 品牌案例 使用后台获取数据

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="lib/vue.js"></script>
    <script src="lib/axios-0.18.0.js"></script>
    <link rel="stylesheet" href="lib/bootstrap.css">
</head>
<body>

<div id="app">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class='panel-title'>添加品牌</h3>
        </div>
        <div class="panel-body form-inline">
            <label>
                name:
                <input type="text" class="form-control" v-model="name">
            </label>
            <button class="btn btn-default" @click="add">添加</button>
            <label>
                搜索的关键字:
                <input type="text" class="form-control">
            </label>
            <input type="button" class="btn btn-primary" value="搜索" @click="findAllList">
        </div>
    </div>


    <table class="table table-bordered table-hover table-striped">
        <thead style="background-color: #5bc0de">
        <tr>
            <th>id</th>
            <th>name</th>
            <th>ctime</th>
            <th>oper</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in list" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.ctime | conver }}</td>
            <td><a @click="remove(item.id)">删除</a></td>
        </tr>

        </tbody>
    </table>
</div>

<script>
    //创建一个 axios 的实例对象 添加一些默认属性 相当于全局配置
    var axios = axios.create({
        //默认域名开头
        baseURL:"http://localhost:8080/vue_day03_1/",
        //默认超时时间
        timeout:1000,
    });

    var vm;
    vm = new Vue({
        el: "#app",
        data: {
            list: [
                {id: 1, name: "宝马", ctime: new Date()},
                {id: 2, name: "奔驰", ctime: new Date()},
                {id: 3, name: "长安", ctime: new Date()},
                {id: 4, name: "解放", ctime: new Date()}
            ],
            name: "",
            contextPath:""
        },
        // 使用声明周期函数在 vue 对象创建过程中对 vue 中参数使用 ajax 进行交互获取值
        created: function () {
            this.findAllList();
        },
        methods: {
            //查询所有的方法
            findAllList() {
                axios.post("/car/findAll.action")
                    .then(response => {
                        this.list = response.data;
                        this.list.forEach(item => {
                            item.ctime = new Date(item.ctime);
                        });
                    })
                    .catch(error => {
                        console.log(error)
                    });
            },
            //添加方法
            add() {
                axios.post("/car/add.action", this.castJosn({name:this.name}))
                    .then(response => {
                        this.findAllList();
                    })
                    .catch(erroe => {
                        console.log(erroe)
                    });
                this.name = '';
            },
            //删除的方法
            remove(id){
                axios.post("/car/remove.action",this.castJosn({id:id}))
                    .then(res => {
                        this.findAllList()
                    })
                    .catch(err => {
                        console.log(err)
                    })
            },
            //转换json对象为 URLSearchParams 对象用于将数据以键值对的形式填充到 响应体中
            castJosn: function (data) {
                var param = new URLSearchParams();
                for (var key in data) {
                    param.append(key,data[key]);
                }
                return param;
            }

        },
        filters: {
            conver(time) {
                var year = time.getFullYear();
                var month = time.getMonth();
                var day = time.getDate();
                month = (month + "").padStart(2, "0");
                day = (day + "").padStart(2, "0");
                var str = `${year}-${month}-${day}`;
                return str;
            }
        }
    });
</script>
</body>
</html>
```

## 3-4 使用 vue 过渡类实现动画效果

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="lib/vue.js"></script>
    <link href="lib/animate.css" rel="stylesheet"/>
</head>
<body>

<div id="app">
    <input type="button" value="切换" @click="flag=!flag">

    <!--  enter-active-class 是指定入场时的 class 类型
          leave-active-class 是指定离场时 class 的样式状态
          类前面要加入 animated 类
      -->
    <!--<transition enter-active-class="animated bounceIn" leave-active-class="animated bounceOut">
        <h1 v-if="flag">我爱福福</h1>
    </transition>-->
    <!--  第二种写法   duration 是指定 动画时长 前面使用"："表示使用参数绑定 可以使用vue动态赋值-->
    <!-- <transition enter-active-class="bounceIn" leave-active-class="bounceOut" :duration="100">
         <h1 v-if="flag" class="animated">我爱福福</h1>
     </transition>-->


    <!--  分别设置进入和离场时间  -->
    <transition enter-active-class="bounceIn" leave-active-class="bounceOut" :duration="{enter:200,leaver:1000}">
        <h1 v-if="flag" class="animated">我爱福福</h1>
    </transition>
</div>

<script>
    var vm = new Vue({
        el: "#app",
        data: {
            flag: false
        },
        methods: {}
    })
</script>
```



# 4，Vue中使用样式

## 4-1 使用class样式 :class

```
1，使用数组的形式添加类样式
	<h2 :class="['red']">我爱福福</h2>
2,使用三元表达式判断类是否加载，或者使用json对象指定是否加载本类
	<h2 :class="[flag?'red':'',{pink:flag}]">牛牛，牛牛，牛牛</h2>
3，<!--表达式中可以直接使用 vue 对象中的变量-->
4，使用 json 对象进行class样式控制
	<h1 :class="{red:true}">你爱我吗</h1>
```

### 4-2 使用内联样式，就是style样式

```html
1，:style=“{color:'red'}” 使用:style 加样式对象进行设置
	<h2 :style="{color:'pink'}">我爱你</h2> //如果属性上包含特殊字符，要使用引号包裹住，
2，使用预先设定好的样式信息，使用数组添加样式信息
	<div id="app">
    	<h2 :style="[style1,style2]">我爱你</h2>
	</div>

    <script>
        var vm = new Vue({
            el:"#app",
            data:{
                style1:{color:"green",'font-size':"18px"},
                style2:{border:'1px skyblue solid'}
            },
            methods:{}
        })
    </script>
```



# 5,Vue中的注意事项

## 5-1 vue 会将属性值当作一个合法的 js 表达式

```
 v-text="msg + '20'" 展示结果 msg 的值 加上 20
```

## 5-2 使用josnp实现跨域请求

```
同源策略，它是由Netscape提出的一个著名的安全策略。
现在所有支持JavaScript 的浏览器都会使用这个策略。所谓同源是指，域名，协议，端口相同。
当一个浏览器的两个tab页中分别打开百度和谷歌的页面
当一个百度浏览器执行一个脚本的时候会检查这个脚本是属于哪个页面的
即检查是否同源，只有和百度同源的脚本才会被执行


//当出现要使用跨域请求的时候，就要使用jsonp来实现效果
1，jsonp的实现原理是利用 script 标签的跨域性，不受域名的限制
<script type="text/javascript" src="http://localhost:20002/test.js"></script>
访问任何域名都会返回结果，并且由于是加载js文件还会加载和执行 返回结果的 JavaScript代码，这就使得返回的数据可以直接以方法的调用返回
2，实现 先定义一个 script 标签，用于返回数据
3，后台返回值修改
	1，基础写法 return callback+"("+s+");";  返回一个方法调用的
	2，springmvc实现返回一个 import com.fasterxml.jackson.databind.util.JSONPObject 包
	的 JSONPObject对象 第一个参数是回调函数名，后一个方法为 返回值
```

## 5-3 使用后端设置 CORS  过滤器实现跨域请求访问

```java
package cn.czxy.filter;

import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "corsFilter", urlPatterns = "/*",
        initParams = {@WebInitParam(name = "allowOrigin", value = "*"),
                @WebInitParam(name = "allowMethods", value = "GET,POST,PUT,DELETE,OPTIONS"),
                @WebInitParam(name = "allowCredentials", value = "true"),
                @WebInitParam(name = "allowHeaders", value = "Content-Type,X-Token")})
public class CorsFilter implements Filter {

    private String allowOrigin;
    private String allowMethods;
    private String allowCredentials;
    private String allowHeaders;
    private String exposeHeaders;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowOrigin = filterConfig.getInitParameter("allowOrigin");
        allowMethods = filterConfig.getInitParameter("allowMethods");
        allowCredentials = filterConfig.getInitParameter("allowCredentials");
        allowHeaders = filterConfig.getInitParameter("allowHeaders");
        exposeHeaders = filterConfig.getInitParameter("exposeHeaders");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (!StringUtils.isEmpty(allowOrigin)) {
            if(allowOrigin.equals("*")){
                response.setHeader("Access-Control-Allow-Origin", allowOrigin);
            }else{
                List<String> allowOriginList = Arrays.asList(allowOrigin.split(","));
                if (allowOriginList != null && allowOriginList.size() > 0) {
                    String currentOrigin = request.getHeader("Origin");
                    if (allowOriginList.contains(currentOrigin)) {
                        response.setHeader("Access-Control-Allow-Origin", currentOrigin);
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(allowMethods)) {
            response.setHeader("Access-Control-Allow-Methods", allowMethods);
        }
        if (!StringUtils.isEmpty(allowCredentials)) {
            response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
        }
        if (!StringUtils.isEmpty(allowHeaders)) {
            response.setHeader("Access-Control-Allow-Headers", allowHeaders);
        }
        if (!StringUtils.isEmpty(exposeHeaders)) {
            response.setHeader("Access-Control-Expose-Headers", exposeHeaders);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
```



# 6 js 的新增方法

## 6-1 padStart 方法

```
为支付串补长 如果不够长就在前面或者后面添加填充物
                                    设定字符规定长度，不够时填充的组件
month = (month+"").padStart(2,"0"); //向头部添加数据
month = (month+"").padEnd(2,"10");  //向尾部添加数据
```

## 6-2 字符串格式化工具

```
//字符串格式化模板 使用 ~ 键 就能开启字符串模板 ${}是开启一个占位符
var str = `${year}-${month}-${day}`;
```

## 6-3 数组新增方法

```
//1，数组方法 将符合条件的对象返回，就是进行筛洗
var arr = this.list.filter(item => {
	return item;
}) // 将返回值添加到一个数组中进行返回
//2,some 数组的循环方法  可以使用return true 终止循环 
```

## 6-4 字符串判断是否包含另一个字符串

```
字符串1.includes(字符串);
```

# 7, 使用vue-resource实现 get post jsonp 请求

[^除了使用 vue-resource 实现外还可以使用 'axios' 的第三方包实现数据的请求]: 

## 7-1，get请求实例

```javascript

window.onload = function(){
    var vm = new Vue({
        el:'#box',
        data:{
            msg:'Hello World!',
        },
        methods:{
            get:function(){
                //发送get请求
                this.$http.get('/try/ajax/ajax_info.txt').then(function(res){
                    document.write(res.body);    
                },function(){
                    console.log('请求失败处理');
                });
            }
        }
    });
}

```

## 7-2, post 请求实例

```

```

## 7-3，vue-resource 提供了 7 种请求 API(REST 风格)： 

```
get(url, [options])
head(url, [options])
delete(url, [options])
jsonp(url, [options])
post(url, [body], [options]) //url 地址 【body】 请求参数  [options] 请求
put(url, [body], [options])
patch(url, [body], [options])
```



| 参数        | 类型                           | 描述                                                         |
| ----------- | ------------------------------ | ------------------------------------------------------------ |
| url         | `string`                       | 请求的目标URL                                                |
| body        | `Object`, `FormData`, `string` | 作为请求体发送的数据                                         |
| headers     | `Object`                       | 作为请求头部发送的头部对象                                   |
| params      | `Object`                       | 作为URL参数的参数对象                                        |
| method      | `string`                       | HTTP方法 (例如GET，POST，...)                                |
| timeout     | `number`                       | 请求超时（单位：毫秒） (`0`表示永不超时)                     |
| before      | `function(request)`            | 在请求发送之前修改请求的回调函数                             |
| progress    | `function(event)`              | 用于处理上传进度的回调函数 ProgressEvent                     |
| credentials | `boolean`                      | 是否需要出示用于跨站点请求的凭据                             |
| emulateHTTP | `boolean`                      | 是否需要通过设置`X-HTTP-Method-Override`头部并且以传统POST方式发送PUT，PATCH和DELETE请求。 |
| emulateJSON | `boolean`                      | 设置请求体的类型为`application/x-www-form-urlencoded`        |

| 属性       | 类型                       | 描述                                                |
| ---------- | -------------------------- | --------------------------------------------------- |
| url        | `string`                   | 响应的 URL 源                                       |
| body       | `Object`, `Blob`, `string` | 响应体数据                                          |
| headers    | `Header`                   | 请求头部对象                                        |
| ok         | `boolean`                  | 当 HTTP 响应码为 200 到 299 之间的数值时该值为 true |
| status     | `number`                   | HTTP 响应码                                         |
| statusText | `string`                   | HTTP 响应状态                                       |
| **方法**   | **类型**                   | **描述**                                            |
| text()     | `约定值`                   | 以字符串方式返回响应体                              |
| json()     | `约定值`                   | 以格式化后的 json 对象方式返回响应体                |
| blob()     | `约定值`                   | 以二进制 Blob 对象方式返回响应体                    |

# 8,使用 axios 实现数据交互

> 默认情况下 axios 是串行化的请求 只有当使用 并行化请求格式返送请求时才时并行

## 8-1多种请求实例

### 8-1-1 get 实例

```javascript
// Make a request for a user with a given ID
axios.get('/user?ID=12345')
  .then(function (response) {
    console.log(response);
  })
  .catch(function (error) {
    console.log(error);
  });

// Optionally the request above could also be done as
axios.get('/user', { // 添加请求数据 数据保存在请求头上
    params: {
      ID: 12345
    }
  })
  .then(function (response) {
    console.log(response);
  })
  .catch(function (error) {
    console.log(error);
  });

```

### 8-1-2 post 请求实例

```javascript
axios.post('/user', {
    firstName: 'Fred',
    lastName: 'Flintstone'
  })
  .then(function (response) {
    console.log(response);
  })
  .catch(function (error) {
    console.log(error);
  });
```

### 8-1-3 多请求并发

```javascript
function getUserAccount() {
  return axios.get('/user/12345');
}

function getUserPermissions() {
  return axios.get('/user/12345/permissions');
}

axios.all([getUserAccount(), getUserPermissions()])
  .then(axios.spread(function (acct, perms) {
    // 两个请求现在都执行完成
  }));
```

### 8-1-4 使用config 传递信息

```javascript
// 发送 POST 请求
axios({
  method: 'post',
  url: '/user/12345',
  data: {
    firstName: 'Fred',
    lastName: 'Flintstone'
  }
});

// 获取远端图片
axios({
  method:'get',
  url:'http://bit.ly/2mTM3nY',
  responseType:'stream'
})
  .then(function(response) {
  response.data.pipe(fs.createWriteStream('ada_lovelace.jpg'))
});


//axios(url[, config])
// 发送 GET 请求（默认的方法）
axios('/user/12345');
```

### 8-1-5 请求方法别名

```
axios.request(config)
axios.get(url[, config])
axios.delete(url[, config])
axios.head(url[, config])
axios.options(url[, config])
axios.post(url[, data[, config]])
axios.put(url[, data[, config]])
axios.patch(url[, data[, config]])


并发

处理并发请求的助手函数
axios.all(iterable)
axios.spread(callback)
```

### 8-1-6 创建实例

```javascript
//axios.create([config])

const instance = axios.create({
  baseURL: 'https://some-domain.com/api/',
  timeout: 1000,
  headers: {'X-Custom-Header': 'foobar'}
});

实例方法

以下是可用的实例方法。指定的配置将与实例的配置合并。
axios#request(config)
axios#get(url[, config])
axios#delete(url[, config])
axios#head(url[, config])
axios#options(url[, config])
axios#post(url[, data[, config]])
axios#put(url[, data[, config]])
axios#patch(url[, data[, config]])
```

## 8-2 参数配置

### 8-2-1 请求参数配置

```javascript
//这些是创建请求时可以用的配置选项。只有 url 是必需的。如果没有指定 method，请求将默认使用 get 方法。

{
   // `url` 是用于请求的服务器 URL
  url: '/user',

  // `method` 是创建请求时使用的方法
  method: 'get', // default

  // `baseURL` 将自动加在 `url` 前面，除非 `url` 是一个绝对 URL。
  // 它可以通过设置一个 `baseURL` 便于为 axios 实例的方法传递相对 URL
  baseURL: 'https://some-domain.com/api/',

  // `transformRequest` 允许在向服务器发送前，修改请求数据
  // 只能用在 'PUT', 'POST' 和 'PATCH' 这几个请求方法
  // 后面数组中的函数必须返回一个字符串，或 ArrayBuffer，或 Stream
  transformRequest: [function (data, headers) {
    // 对 data 进行任意转换处理
    return data;
  }],

  // `transformResponse` 在传递给 then/catch 前，允许修改响应数据
  transformResponse: [function (data) {
    // 对 data 进行任意转换处理
    return data;
  }],

  // `headers` 是即将被发送的自定义请求头
  headers: {'X-Requested-With': 'XMLHttpRequest'},

  // `params` 是即将与请求一起发送的 URL 参数
  // 必须是一个无格式对象(plain object)或 URLSearchParams 对象
  params: {
    ID: 12345
  },

   // `paramsSerializer` 是一个负责 `params` 序列化的函数
  // (e.g. https://www.npmjs.com/package/qs, http://api.jquery.com/jquery.param/)
  paramsSerializer: function(params) {
    return Qs.stringify(params, {arrayFormat: 'brackets'})
  },

  // `data` 是作为请求主体被发送的数据
  // 只适用于这些请求方法 'PUT', 'POST', 和 'PATCH'
  // 在没有设置 `transformRequest` 时，必须是以下类型之一：
  // - string, plain object, ArrayBuffer, ArrayBufferView, URLSearchParams
  // - 浏览器专属：FormData, File, Blob
  // - Node 专属： Stream
  data: {
    firstName: 'Fred'
  },

  // `timeout` 指定请求超时的毫秒数(0 表示无超时时间)
  // 如果请求话费了超过 `timeout` 的时间，请求将被中断
  timeout: 1000,

   // `withCredentials` 表示跨域请求时是否需要使用凭证
  withCredentials: false, // default

  // `adapter` 允许自定义处理请求，以使测试更轻松
  // 返回一个 promise 并应用一个有效的响应 (查阅 [response docs](#response-api)).
  adapter: function (config) {
    /* ... */
  },

 // `auth` 表示应该使用 HTTP 基础验证，并提供凭据
  // 这将设置一个 `Authorization` 头，覆写掉现有的任意使用 `headers` 设置的自定义 `Authorization`头
  auth: {
    username: 'janedoe',
    password: 's00pers3cret'
  },

   // `responseType` 表示服务器响应的数据类型，可以是 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
  responseType: 'json', // default

  // `responseEncoding` indicates encoding to use for decoding responses
  // Note: Ignored for `responseType` of 'stream' or client-side requests
  responseEncoding: 'utf8', // default

   // `xsrfCookieName` 是用作 xsrf token 的值的cookie的名称
  xsrfCookieName: 'XSRF-TOKEN', // default

  // `xsrfHeaderName` is the name of the http header that carries the xsrf token value
  xsrfHeaderName: 'X-XSRF-TOKEN', // default

   // `onUploadProgress` 允许为上传处理进度事件
  onUploadProgress: function (progressEvent) {
    // Do whatever you want with the native progress event
  },

  // `onDownloadProgress` 允许为下载处理进度事件
  onDownloadProgress: function (progressEvent) {
    // 对原生进度事件的处理
  },

   // `maxContentLength` 定义允许的响应内容的最大尺寸
  maxContentLength: 2000,

  // `validateStatus` 定义对于给定的HTTP 响应状态码是 resolve 或 reject  promise 。如果 `validateStatus` 返回 `true` (或者设置为 `null` 或 `undefined`)，promise 将被 resolve; 否则，promise 将被 rejecte
  validateStatus: function (status) {
    return status >= 200 && status < 300; // default
  },

  // `maxRedirects` 定义在 node.js 中 follow 的最大重定向数目
  // 如果设置为0，将不会 follow 任何重定向
  maxRedirects: 5, // default

  // `socketPath` defines a UNIX Socket to be used in node.js.
  // e.g. '/var/run/docker.sock' to send requests to the docker daemon.
  // Only either `socketPath` or `proxy` can be specified.
  // If both are specified, `socketPath` is used.
  socketPath: null, // default

  // `httpAgent` 和 `httpsAgent` 分别在 node.js 中用于定义在执行 http 和 https 时使用的自定义代理。允许像这样配置选项：
  // `keepAlive` 默认没有启用
  httpAgent: new http.Agent({ keepAlive: true }),
  httpsAgent: new https.Agent({ keepAlive: true }),

  // 'proxy' 定义代理服务器的主机名称和端口
  // `auth` 表示 HTTP 基础验证应当用于连接代理，并提供凭据
  // 这将会设置一个 `Proxy-Authorization` 头，覆写掉已有的通过使用 `header` 设置的自定义 `Proxy-Authorization` 头。
  proxy: {
    host: '127.0.0.1',
    port: 9000,
    auth: {
      username: 'mikeymike',
      password: 'rapunz3l'
    }
  },

  // `cancelToken` 指定用于取消请求的 cancel token
  // （查看后面的 Cancellation 这节了解更多）
  cancelToken: new CancelToken(function (cancel) {
  })
}
```

### 8-2-2 响应参数配置

```javascript
{
  // `data` 由服务器提供的响应
  data: {},

  // `status` 来自服务器响应的 HTTP 状态码
  status: 200,

  // `statusText` 来自服务器响应的 HTTP 状态信息
  statusText: 'OK',

  // `headers` 服务器响应的头
  headers: {},

   // `config` 是为请求提供的配置信息
  config: {},
 // 'request'
  // `request` is the request that generated this response
  // It is the last ClientRequest instance in node.js (in redirects)
  // and an XMLHttpRequest instance the browser
  request: {}
}
```

### 8-2-3 发送键值对请求体

```javascript
//1，这种情况发送的请求就是 普通的 post 请求以键值对形式
var params = new URLSearchParams();
params.append('param1', 'value1');
params.append('param2', 'value2');
axios.post('/foo', params);



const qs = require('qs');
axios.post('/foo', qs.stringify({ 'bar': 123 }));

// Or in another way (ES6),
//2，使用 JSON.stringify（）方法将一个json对象转换为一个字符串形式


import qs from 'qs';
const data = { 'bar': 123 };
const options = {
  method: 'POST',
  headers: { 'content-type': 'application/x-www-form-urlencoded' },
  
  data: qs.stringify(data),
  url,
};
axios(options);
```

## 8-3 为axios 对象添加一些默认值

```javascript
	//创建一个 axios 的实例对象 添加一些默认属性
    var axios = axios.create({
        //默认域名开头
        baseURL:"http://localhost:8080/vue_day03_1/",
        //默认超时时间
        timeout:1000,
    });
```

# 9 vue 动画

## 9-1 vue 原生动画

### 9-1-1 过渡类名实现动画

```html
	<script src="lib/vue.js"></script>
    <script src="lib/axios-0.18.0.js"></script>
    <style>
        /*
            v-enter 定义开始的 元素进入之前的起始状态
            v-leave 定义动画结束时的状态 此时动画已经结束了
        */
        .v-enter,
        .v-leave-to {
            opacity: 0;
            transform: translateX(80px);
        }
        /*
            v-enter-active 入场动画时间段
            v-leave-active 离场动画时间段
        */
        .v-enter-active,
        .v-leave-active {
            transition: all 1s ease;
        }
    </style>

</head>
<body>

<div id="app">
    <input type="button" value="切换" @click="flag=!flag">

    <!--  transition 是vue官方的用于动画的展示  -->
    <transition>
        <h2 v-if="flag">我爱福福</h2>
    </transition>

</div>

<script>
    var vm = new Vue({
        el: "#app",
        data: {
            flag: false
        },
        methods: {}
    })
</script>
```

### 9-1-2 自定义动画

```html
	<style>
		.my-enter,
        .my-leave-to {
            opacity: 0;
            transform: translateX(80px);
        }
       
        .my-enter-active,
        .my-leave-active {
            transition: all 1s ease;
        }
	</style>
	<!--使用 name 属性对样式表进行区分 不写默认都是使用 v- 前缀-->
	<transition name="my">
        <h2 v-if="flag">我爱福福</h2>
    </transition>
```

## 9-2 使用第三方类库 animate.css 来实现动画效果

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="lib/vue.js"></script>
    <link href="lib/animate.css" rel="stylesheet"/>
</head>
<body>

<div id="app">
    <input type="button" value="切换" @click="flag=!flag">

    <!--  enter-active-class 是指定入场时的 class 类型
          leave-active-class 是指定离场时 class 的样式状态
          类前面要加入 animated 类
      -->
    <!--<transition enter-active-class="animated bounceIn" leave-active-class="animated bounceOut">
        <h1 v-if="flag">我爱福福</h1>
    </transition>-->
    <!--  第二种写法   duration 是指定 动画时长 前面使用"："表示使用参数绑定 可以使用vue动态赋值-->
    <!-- <transition enter-active-class="bounceIn" leave-active-class="bounceOut" :duration="100">
         <h1 v-if="flag" class="animated">我爱福福</h1>
     </transition>-->


    <!--  分别设置进入和离场时间  -->
    <transition enter-active-class="bounceIn" leave-active-class="bounceOut" :duration="{enter:200,leaver:1000}">
        <h1 v-if="flag" class="animated">我爱福福</h1>
    </transition>
</div>

<script>
    var vm = new Vue({
        el: "#app",
        data: {
            flag: false
        },
        methods: {}
    })
</script>
```

# 10 vue的路由 vue-router

## 10.1.vue-router简介和安装

> 使用vue-router和vue可以非常方便的实现 复杂单页应用的动态路由功能。

> 官网：https://router.vuejs.org/zh-cn/

> 使用npm安装：`npm install vue-router --save` 

> 在index.html中引入依赖：

```html
<script src="../node_modules/vue-router/dist/vue-router.js"></script>
```

## 10.2 快速入门

新建vue-router对象，并且指定路由规则：

```js
// 创建VueRouter对象
const router = new VueRouter({
    routes:[ // 编写路由规则
        {
            path:"/login", // 请求路径，以“/”开头
            component:loginForm // 组件名称
        },
        {
            path:"/register",
            component:registerForm
        }
    ]
})
```

- 创建VueRouter对象，并指定路由参数
- routes：路由规则的数组，可以指定多个对象，每个对象是一条路由规则，包含以下属性：
  - path：路由的路径
  - component：组件名称

在父组件中引入router对象：

```js
var vm = new Vue({
    el:"#app",
    components:{// 引用登录和注册组件
        loginForm,
        registerForm
    },
    router // 引用上面定义的router对象
})
```

页面跳转控制：

```html
<div id="app">
    <!--router-link来指定跳转的路径-->
    <span><router-link to="/login">登录</router-link></span>
    <span><router-link to="/register">注册</router-link></span>
    <hr/>
    <div>
        <!--vue-router的锚点-->
        <router-view></router-view>
    </div>
</div>
```

- 通过`<router-view>`来指定一个锚点，当路由的路径匹配时，vue-router会自动把对应组件放到锚点位置进行渲染
- 通过`<router-link>`指定一个跳转链接，当点击时，会触发vue-router的路由功能，路径中的hash值会随之改变