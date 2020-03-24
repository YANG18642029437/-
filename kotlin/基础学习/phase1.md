# 基础学习

## 1，Hello Word

```kotlin
// 声明函数 函数名 接收参数 
fun main(args:Array<String>){
    // 打印输出
    println("Hello Word")
}
```

## 2，变量和容器

### var And Val

~~~ kotlin
fun varAndVal(){
    // 定义一个变量，可以自动推断数据类型，一旦数据类型固定就不可再次被修改
    var name = "张三";
    // 报错 已经确定为 字符串类型，不能再修改为 数字int 类型
    // name = 18;
    // 定义一个不可修改变量 ， 相当于被 final 修饰为常量
    val ss = name + "弟弟";
    // 报错，不可被修改
    // ss = "11"
    print(name + "李四")
    // 可以提前声明出数据的类型
    var aa:String;
}
~~~

### 变量取值范围

```kotlin
fun baseType(){
    // 最大值
    val aByte:Byte = Byte.MAX_VALUE;
    // 最小值
    val bByte:Byte = Byte.MIN_VALUE;
    print(aByte.toString() + "   " + bByte.toString())
}
```

### 基础计算

```

```

## 3，函数进阶

### 函数定义规则

![image-20200204155715436](D:\temp\xmind\kotlin\phase1Image\image-20200204155715436.png)

![image-20200204160554595](D:\temp\xmind\kotlin\phase1Image\image-20200204160554595.png)

### 简易计算机

```kotlin
fun main(args: Array<String>) {
    println(plus(1, 2))
}
fun plus(x: Int, y: Int): Int {
    return x + y;
}
fun sub(x: Int, y: Int): Int {
    return x - y;
}
fun ride(x: Int,y: Int): Int {
    return x * y;
}
fun div(x: Int,y:Int):Int {
    return x / y;
}
```

## 4，参数值

### 字符串

+ 字符串拼接

```kotlin
var temple = "今天天气晴朗，万里无云，我们去${placeName}"+placeName+ "首先映入眼帘的是，"+placeName+ placeName.length + " 个鎏金大字";

```

+ 字符串比较

> 使用 == 相当于java 中的 equals 
>
> kotlin 中的对比方式 equals 可以输入两个参数 第二个参数决定是否忽略大小写

### 参数传递

> ```kotlin
> // 方法上参数类型加入 一个 ? 表示参数可以为 null
> // 如果没有加入 ? 传递参数如果为 null 就会报错
> fun heat(str: String?): String {
>     return "热" + str
> }
> ```

### when 表达式 (相当于java switch)

```kotlin
fun gradeStudent(score: Int): Unit {
    when(score){
        10 -> println("考了满分")
        9 -> println("干的不错")
        8 -> println("还可以")
        else -> println("还需要努力了呀")
    }
}

fun main(args: Array<String>) {
    gradeStudent(8)
}
```

## 4，循环

### loop 和 Range

```kotlin
fun main(args: Array<String>) {
    var nums = 1 .. 100 // [1 ... 100]
    var nums2 = 1 until  100 // [1 ... 99] 前闭后开
    var result = 0
    for(num in nums) {
        result += num
    }
    for (num in nums step 2){ // 步长 2
        println(num)
    }
    nums.reversed() // 反转
    println(result)
}
```

## 5，容器

### List 



```kotlin
fun main(args:Array<String>) {
    var lists = listOf<String>("卖鸡蛋", "卖鸭蛋" , "卖鹅蛋")
    for ((i,e) in lists.withIndex()) {
        println("$i $e")
    }
}
```

### Map

```kotlin
fun main(args: Array<String>) {
    var maps = TreeMap<String,String>()
    maps["好好"] = "hood"
    maps["学习"] =  "student"
    maps["天天"] = "dayday"
    maps["向上"] = "向上"
    maps.forEach { t, u -> println("$t $u")}

}
```

## 6，函数和函数式表达式

### 函数的简写方式

```kotlin
fun main(args: Array<String>) {
    println(add1(2, 3))
}

fun add1 (x:Int , y :Int) : Int {
    return x + y;
}
fun add2 (x:Int , y :Int) : Int  = x + y
```

### 注意事项 ： kotlin 是一个 函数式的语言，函数也是一个基本类型 （和js极度相似）都只是语法糖而已

```kotlin
fun main(args: Array<String>) {
    // 第三种函数的声明形式
    var fun1 = {x : Int, y : Int -> x + y}
    // 第四种函数的声明形式
    var fun2 : (Int,Int) -> Int  = {x,y -> x+y}
}
// 第一种函数的声明形式
fun add1 (x:Int , y :Int) : Int {
    return x + y;
}
// 第二种函数声明形式
fun add2 (x:Int , y :Int) : Int  = x + y

```

### 形参默认值，具体赋值

```kotlin
var x = 6f

fun main(args: Array<String>) {
    var fun1 = {x : Int, y : Int -> x * y}
    println(fun1(4, 5))

    var fun2 : (Double,Double) -> Double = {x,π ->  x * x * π}
    println(fun2(4.0,Math.PI))


    var fun3 = {x : Double,y:Double -> x * y}
    var fun4 = {x : Double, y:Double -> fun3(fun2(x,Math.PI) , y)}
    var fun5 = {x : Double, y:Double -> 4 * fun2(x,y)}
    var fun6 = {y : Float -> y}
    fun6(6f)
    // 使用指定的形式为某一个形参进行赋值
    println(fun1(π = 3.14f))
}
// 给一个形参赋值一个默认值 
fun fun1 (PI:Float = x , π : Float): Float{
    return x * π
}	
```

###  异常处理

```kotlin
fun main(args: Array<String>) {
    println("请输入一个数字")
    // 获取到一个 键盘录入的
    var number1Str = readLine()
    println("请输入下一个数字")
    var number2Str = readLine()
    // 因为readLine 获取到可能为null所以
    // 但是这个 toInt 可能会抛出异常 所以抛出异常后会直接停止程序 所以要使用 try catch 进行处理
    try {
        var num1 = number1Str!!.toInt()
        var num2 = number2Str!!.toInt()
    }catch (e : Exception){
        e.printStackTrace()
    }

    println("相加结果为 " + (number1Str + number2Str))
}
```

### kotlin 递归处理

```kotlin
fun main(args: Array<String>) {
//    println(recursion(BigInteger("100")))
    var result = 0
    println(plus1(100000,result))
}

fun recursion (n : BigInteger) : BigInteger {
    if (n == BigInteger.ONE){
        return BigInteger.ONE
    }else {
        return n * recursion(n - BigInteger.ONE)
    }
}

/**
 * 进行尾递归优化 强制要求 jvm 执行结束所有的递归，而不会堆栈溢出
 */
tailrec fun plus1 (n : Int,result: Int) : Int{
    println("计算机的第 $n 次运算 ， result=$result")
    if (n == 1){
        return 1
    }else
        return plus1(n -1,result + n)
}
```





































































