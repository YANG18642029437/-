# 面向对象

## 基本入门

### 基本类的定义

+ 定义属性

```kotlin
class Rect (var Height:Int,var Width:Int)

fun main(args: Array<String>) {
    var r1 = Rect(10,20)

}
```



+ 定义行为

```kotlin
class Girl (var sex : String,var age: Int){
    fun eat(){
        println("女朋友吃饭")
    }
}

fun main(args: Array<String>) {
    var girl01 = Girl("男",18)
    println("girl01 性别为${girl01.sex},年龄为 ${girl01.age}")
    girl01.eat()
}
```

## 面向对象的三大特性

### 封装 ： private 外界不可改变类的内部结构

```kotlin
package 面向对象.入门


class WashMachine(var Module:String,var Size :Int) {
    var isOpen = true
    var currentMode = 0
    fun openDoor() {
        println("打开洗衣机的门。。。")
        isOpen = true
    }

    fun closeDoor(){
        println("关闭洗衣机的门。。。")
        isOpen = false
    }
    fun selectMode(Mode:Int){
        currentMode = Mode
        when(Mode){
            0 -> println("初始")
            1 -> println("轻柔")
            2 -> println("狂揉")
            else -> {
                println("不要瞎揉")
            }
        }
    }
    fun wash() {
        if (isOpen){
            println("请关闭洗衣机门")
        }else {
            when(currentMode) {
                0 -> {
                    throw RuntimeException("请设置洗衣模式")
                }
                1 -> print("开始轻柔模式")
                2 -> print("开始狂揉模式")
                else -> throw RuntimeException("出现异常，请重新启动")

            }
            println("开始洗衣服")
            println("放水")
            println("转动电机")
            println("洗好了")
        }
    }
}
```

### 继承

+ father

```
package 面向对象.继承


/**
 * 要想本类被继承就必须使用 open 标记本类
 */
open class Father {
    var character:String = "性格内向"
    /**
     * 要想本方法可以被重写 就要使用 open 标记本方法
     */
    open fun action(){
        println("公共场合喜欢大声喧哗")
    }
}
```

+ son

```
package 面向对象.继承


/**
 * 孩子
 */
class Son : Father() {
    override fun action(){
        println("非常安静")
    }
}
```

### 多态

```kotlin
package 面向对象.多态

/**
 * 抽象类不用使用的 open 打开继承
 */
abstract class Human(var name:String) {
    abstract fun eat()
    abstract fun pee()
}
```



```kotlin
package 面向对象.多态


/**
 * 继承抽象类后 必须实现抽象方法
 */
class Man (name:String) : Human(name) {
    override fun eat() {
        println("$name 坐着吃饭")
    }

    override fun pee() {
        println("$name 去卫生间上厕所")
    }
}
```



```kotlin
package 面向对象.多态


class Woman(name:String) : Human(name) {
    override fun eat() {
        println("$name 小口吃饭饭")
    }

    override fun pee() {
        println("$name 在家里卫生间上厕所")
    }
}
```

### 接口

```kotlin
package 面向对象.接口


interface IMan {
    fun houjie()
}
```

## 代理和委托

### 代理

+ 接口

```kotlin
interface IWashBowl {
    fun washing()
}
```

+ 代理的实现类	

```kotlin
package 面向对象.委托

/**
 * 代理和委托
 * 实现接口可以借用另一个 来实现接口中方法
 * 这里创建一个接口实现类 BigHeadSon 来实现 接口中的方法
 * 这时候其实创建了一个对象了
 */
class SmallHeadFather:IWashBowl by BigHeadSon() {
    /**
     * 这里的方法先被 BigHeadSon 实现 
     * 再通过 SmallHeadFather 重写方法	
     */
    override fun washing(){
        println("我是小头爸爸，我赚了10块钱")
        // 这里又创建了一个实现类 BigHeadSon 再次调用 washing 方法
        BigHeadSon().washing()
        println("我看着儿子把碗洗好了")
    }
}
```

+ 被借用代理的实现类

```kotlin
package 面向对象.委托

/**
 * 被借用的实现类
 */
class BigHeadSon:IWashBowl {
    override fun washing() {
        println("洗完获取1元钱")
    }
}
```



## 单例模式



+ 接口

```
interface IWashBowl {
    fun washing()
}
```



+ 代理类

```
/**
 * 代理和委托
 * 实现接口可以借用另一个 来实现接口中方法
 */
// 这种情况取消 BigHeadSon 中的 () 就可以创建实例对象
class SmallHeadFather:IWashBowl by BigHeadSon {
    override fun washing(){
        println("我是小头爸爸，我赚了10块钱")
        BigHeadSon.washing()
        println("我看着儿子把碗洗好了")
    }
}
```

+ 被代理类

```
/**
 * 被借用的实现类
 */
// 使用 Object 来声明对象的表达方法 这时类本身就变成了单例模式 创建对象不需要使用 () 来进行声明
object BigHeadSon:IWashBowl {
    override fun washing() {
        println("洗完获取1元钱")
    }

}
```

## 枚举

> 和 java 中的枚举一模一样

```kotlin
enum class Week {
    星期一,星期二,星期三,星期四,星期五,星期六,星期日;
    fun weak(){
        println("开始工作")
    }
}

fun main(args: Array<String>) {
    Week.星期一.weak()
    // 获取枚举中定义的位置数
    println(Week.星期一.ordinal)
}
```

## 图片处理

```kotlin
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


fun main(args: Array<String>) {
    // 内存中创建了一个图片 有宽和高
    var image = BufferedImage(100,100,BufferedImage.TYPE_INT_BGR)
    // 设置 图片 rgb 的坐标
    image.setRGB(0,0,0xff0000)
    // 高阶函数
    image.apply {
        var x = 0 .. 99
        var y = 0 .. 99
        for (i in x){
            for (j in y){
                setRGB(i,j,0xff0000)
            }
        }
    }
    // 将内存中的图片输出到 硬盘的指定位置
    ImageIO.write(image,"bmp", File("a.bmp"))

}
```

# 高阶函数

## 什么是高阶函数

```kotlin
package 高阶函数.kotlin函数演示


fun main(args: Array<String>) {
    // 使用高阶函数对 数据进行过滤 并返回过滤后的数据
    println(非诚勿扰数据库.maxBy { it.age })
    // 使用多条件对数据进行过滤 返回过滤后的数据
    println(非诚勿扰数据库.filter {
        it.age < 18 && it.address.equals("河南")
    })
    // 对数据库进行遍历，将返回的数据重新组装为一个新的集合
    println(非诚勿扰数据库.map {
        "${it.age} : ${it.address}"
    })

    // 判断集合中是否包含条件 返回值是一个 boolean 类型的数据
    println(非诚勿扰数据库.any {
        "河南".equals(it.address)
    })
    // 统计 匹配到查询条件数据的个数
    println(非诚勿扰数据库.count { it.address.equals("河南") })

    println("我的 find 分割线------------")
    // 查询出 符合条件的 数据并返回第一个
    println(非诚勿扰数据库.find { it.address.equals("河南") })

    println("我是 group by 的分割线 -------------")
    非诚勿扰数据库.groupBy { it.address }.get("河南")?.forEach { print(it) }

}
```

> 就是返回值或者参数为函数的 函数就是高阶函数

## DSL (领域特定语言)

+ 扩展函数

```kotlin
/**
 * 扩展函数
 * 这个相当于 js 的原型链 可以在原有类上添加新的方法
 */
fun List<Girl>.查找河南年龄小于(age:Int):List<Girl>{
    return filter { it.age < age && it.address.equals("河南") }
}
```

+ 中缀表达式

  ```kotlin
  /**
   * 中缀表达式
   * 方法声明上添加 infix 前缀 在方法的调用的时候可以省略 "点"
   */
  infix fun List<Girl>.查找上海年龄小于(age:Int):List<Girl>{
      return filter { it.age < age && it.address.equals("上海") }
  }
  ```

+ 测试

```kotlin
fun main(args: Array<String>) {
    非诚勿扰数据库.查找河南年龄小于(18).forEach(::println)
    // 就是使用了中缀表达式 省略点调用
    var ss = 非诚勿扰数据库 查找上海年龄小于 20
    ss.forEach(::println)
}
```





















