# Hook 修改配置说明

## 1.在定义处修改

### 1.0 类修改

* 修改父类
* 添加接口，以及默认接口方法实现
* 添加方法: 意义不大
* 添加属性: 意义不大


### 1.1 方法

```
public class TargetClass() {
    private TargetField tf;
    
    public TargetReturnType targetMethod(ParamB b, ParameC) {
        。。。。。
        return result;
    }
}

```

#### 1.1.1 方法前调用，无返回值

```
public class TargetClass() {
    private TargetField tf;
    
    public TargetReturnType targetMethod(ParamB b, ParameC) {
         HookClass.dobeforeTargetMethod(targetClass obj, PatramB b, ParameC c)
            。。。。。
        return result;
    }
}

```

#### 1.1.2 方法后调用，具有返回值
```
public class TargetClass() {
    private TargetField tf;
    
    public TargetReturnType targetMethod(ParamB b, ParameC) {
            。。。。。
            result = ...
        TargetReturnType finalResult = HookClass.doAfterTargetMethod(targetClass obj, PatramB b, ParameC c,TargetReturnType result)
        return finalResult;
    }
}

```

#### 1.1.3 方法替换: 不经过原有方法逻辑 （1.1.1和1.1.2原有的方法逻辑还是执行的。这里原有的方法完全不执行）
```
public class TargetClass() {
    private TargetField tf;
    public TargetReturnType targetMethod(ParamB b, ParameC) {
         return HookClass.doAfterTargetMethod(targetClass obj, PatramB b, ParameC c)
    }
}

```

### 1.2 属性

#### 1.2.1 设置默认初始值

```
public class TargetClass() {
    private TargetField tf = HookClass.tf;
}
```


## 2.在调用处修改

SHADOW_METHOD\:
android/webkit/WebView.loadUrl(Ljava/lang/String;)V
=
com/growingio/android/sdk/autoburry/VdsAgent.loadUrl(Landroid/view/View;Ljava/lang/String;)V

STATIC_SHADOW_METHOD\:
android/app/PendingIntent.getActivity(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;
=
com/growingio/android/sdk/autoburry/VdsAgent.onPendingIntentGetActivityShortAfter
(Landroid/content/Context;ILandroid/content/Intent;ILandroid/app/PendingIntent;)
V

STATIC_SHADOW_METHOD_BEFORE\:
android/app/PendingIntent.getActivity(Landroid/content/Context;ILandroid/content/Intent;ILandroid/os/Bundle;)Landroid/app/PendingIntent;
=
com/growingio/android/sdk/autoburry/VdsAgent.onPendingIntentGetActivityBefore(Landroid/content/Context;ILandroid/content/Intent;ILandroid/os/Bundle;)V

STATIC_SHADOW_METHOD_BEFORE\:
android/app/PendingIntent.getBroadcast(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;
=
com/growingio/android/sdk/autoburry/VdsAgent.onPendingIntentGetBroadcastBefore(Landroid/content/Context;ILandroid/content/Intent;I)V


### 2.1 方法

#### 方法替换

```
TargetClass tc = new TargetClass()
tc.targetMethod(ParameB b, ParameC c)

---->

TargetClass tc = new TargetClass()
HookClass.doAfterTargetMethod(tc,b,c)

```


### 2.2 属性
 
```
TargetClass tc = new TargetClass()
TargetFiled tf = tc.tf

---->

targetFiled tf =TargetFiled HookClass.hookTf(tc.tf)

```