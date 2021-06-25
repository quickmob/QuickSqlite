# Android 数据库

  [![](https://jitpack.io/v/quickmob/QuickSqlite.svg)](https://jitpack.io/#quickmob/QuickSqlite)

#### 集成步骤

* 在项目根目录下的 `build.gradle` 文件中加入

```
buildscript {
    ......
}
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

* 在项目 app 模块下的 `build.gradle` 文件中加入

```
android {
    compileOptions {
        targetCompatibility JavaVersion.VERSION_1_8
        sourceCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    //框架依赖：其中latest.release指代最新版本号，也可以指定明确的版本号
    implementation 'com.github.quickmob:QuickSqlite:latest.release'
}
```

#### 使用方式
  具体看demo

#### 混淆配置
  此框架库自带混淆规则，并且会自动导入，正常情况下无需手动导入
