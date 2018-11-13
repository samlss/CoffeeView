# CoffeeView
A cup of coffee loading view.

You can specify the color of the cup, the color of the coaster, the color of the vapors.

### [中文](https://github.com/samlss/CoffeeView/blob/master/README-ZH.md)

### [More](https://github.com/samlss/FunnyViews)

 <br/>

[![Api reqeust](https://img.shields.io/badge/api-11+-green.svg)](https://github.com/samlss/CoffeeView)  [![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://github.com/samlss/CoffeeView/blob/master/LICENSE) [![Blog](https://img.shields.io/badge/samlss-blog-orange.svg)](https://blog.csdn.net/Samlss)

### The default effect:
![gif1](https://github.com/samlss/CoffeeView/blob/master/screenshots/screenshot1.gif)

### The custom colors effect:
![gif2](https://github.com/samlss/CoffeeView/blob/master/screenshots/screenshot2.gif)



### Use<br>
Add it in your root build.gradle at the end of repositories：
```java
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```

Add it in your app build.gradle at the end of repositories:
```java
dependencies {
    implementation 'com.github.samlss:CoffeeView:1.1'
}
```


in layout.xml：
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

in java code：
```java
  cofeeView.setCupColor(Color.BLUE); //Set the color of the cup.
  coffeeView.setCoasterColor(Color.RED); //Set the color of the coaster.
  coffeeView.setVaporColor(Color.GREEN); //Set the color of the vapors.
  
  coffeeView.start(); //start animation
  coffeeView.stop(); //stop animation
  
  coffeeView.release(); //release when you do net need the view anyway.
```
<br>

Attributes description：

| attr            |             description              |
| --------------- | :----------------------------------: |
| cupColor  |         the color of cup         |
| coasterColor |        the color of cup coaster         |
| vaporColor  |         the color of vapors         |

<br>

# Note
You can adjust the style you want by setting the width and height of the CoffeView.  If the animation setting of the vapors does not meet your requirements, you can download the source code to modify it. If you like it please don't marry your star.


## [LICENSE](https://github.com/samlss/CoffeeView/blob/master/LICENSE)
