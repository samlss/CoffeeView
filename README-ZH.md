# CoffeeView
一杯咖啡的loading view.

你可以指定杯子颜色，杯垫颜色，烟雾的颜色

### [更多](https://github.com/samlss/FunnyViews)

 <br/>

[![Api reqeust](https://img.shields.io/badge/api-11+-green.svg)](https://github.com/samlss/CoffeeView)  [![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://github.com/samlss/CoffeeView/blob/master/LICENSE) [![Blog](https://img.shields.io/badge/samlss-blog-orange.svg)](https://blog.csdn.net/Samlss)

### 默认效果:
![gif1](https://github.com/samlss/CoffeeView/blob/master/screenshots/screenshot1.gif)

### 自定义颜色效果:
![gif2](https://github.com/samlss/CoffeeView/blob/master/screenshots/screenshot2.gif)



### 使用<br>
在根目录的build.gradle添加这一句代码：
```java
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```

在app目录下的build.gradle添加依赖使用：
```java
dependencies {
    implementation 'com.github.samlss:CoffeeView:1.0'
}
```


布局中：
```java
 <com.iigo.library.CoffeeView
            app:cupColor="#42b6b7"
            app:coasterColor="#72c7c8"
            app:vaporColor="#bbc8c8"
            android:layout_marginTop="20dp"
            android:layout_width="100dp"
            android:layout_height="100dp" />

```

<br>

代码：
```java
  cofeeView.setCupColor(Color.BLUE); //设置杯子颜色
  coffeeView.setCoasterColor(Color.RED); //设置杯垫颜色
  coffeeView.setVaporColor(Color.GREEN); //设置烟雾颜色
  
  coffeeView.start(); //开始动画
  coffeeView.stop(); //停止动画
  
  coffeeView.release(); //不需要使用该loading view的时候可手动释放，例如在activity的ondestroy()中
```
<br>

属性说明：

| 属性            |             说明              |
| --------------- | :----------------------------------: |
| cupColor  |         杯子颜色         |
| coasterColor |        杯垫颜色         |
| vaporColor  |         烟雾颜色         |

<br>

# Note
你可以通过设置CoffeView的宽高来调整你想要的样式，关于烟雾的动画设置如果不符合你的要求的话，你可以下载源码进行修改，如果你喜欢请不要吝啬你的star


## [LICENSE](https://github.com/samlss/CoffeeView/blob/master/LICENSE)