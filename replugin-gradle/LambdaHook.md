# LambdaHook说明

## 功能说明

Lambda表达式Hook,支持如下情况

### Kotlin Java 全覆盖

1. Lambda表达式在Kotlin类中，目标函数在Kotlin中
2. Lambda表达式在Kotlin类中，目标函数在Java中
3. Lambda表达式在Java类中，目标函数在Kotlin中
3. Lambda表达式在Java类中，目标函数在Java中


### 类型全覆盖

* 目标函数是对象函数
* 目标函数是静态函数
* 目标函数是Kotlin Object类函数
* 目标函数是Kotlin Companion Object 类函数
* @JVMStatic函数
* 目标函数是函数类型参数

### HooK方式：

* before
* after
* replace

#### tips: 
与HookMethod的不同,我没有将原函数的方法信息存储在目标回调函数中。 如果具体业务需要，可以联系我加上

### demos

* sample/sample-plugin项目
* 配置：sample/sample-plugin build.gradle hookLambdas配置
* 演示:点击LambdaCases按钮，分别点击四个按钮，查看Log
* Hook类与Target类:
    * `com.cleo.sample.plugin.hook.lam.LambdaOriginActivity`
    * `com.cleo.sample.plugin.hook.lamLambdaTarget`
  
### 单独使用HookPlugin插件

* 根目录build.gradle引入`classpath "com.qihoo360.replugin:replugin-gradle:2.4.7"`
* module build.gradle引入`apply plugin: 'replugin-hook-gradle' `
* 引入配置,如:

 ```groovy
hookConfig {
  hookLambdas {
    callBefore {
      className = "com/cleo/sample/plugin/hook/lam/LambdaOriginActivity"
      methodName = "action1"
      methodDesc = "()V"
      lambdaMethodName = "onClick"
      lambdaMethodDesc = "(Landroid/view/View;)V"
      targetClass = "com/cleo/sample/plugin/hook/lam/LambdaTarget"
      targetMethod = "targetBefore"
      hookType = 0x01
    }
  }
  hookMethods {
    // 方法调用时Hook
    callBefore {
      className = "com/cleo/sample/plugin/hook/Origin"
      methodName = "beforeF"
      methodDesc = "(Lcom/cleo/sample/plugin/hook/A;Lcom/cleo/sample/plugin/hook/B;)Lcom/cleo/sample/plugin/hook/Res;"
      targetClassName = "com/cleo/sample/plugin/hook/Target"
      targetMethodName = "callBeforeF"
      targetMethodDesc = "(Lcom/qihoo360/replugin/base/hook/MethodInfo;Ljava/lang/Object;Lcom/cleo/sample/plugin/hook/A;Lcom/cleo/sample/plugin/hook/B;)V"
      hookType = 0x07
    }
  }
}
```
