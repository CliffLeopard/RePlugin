# Gradle Method Hook 说明

## 说明:
* `info`:MethodInfo，originMethod的方法信息:className,methodName,methodDesc. 为更灵活的修改提供条件。
* `o`:  原方法调用者对象; `Origin`:静态方法类型
* `A`,`B`: 参数类型
* `R`:  方法返回类型
* `V`:  方法返回类型为`void`

## 使用范围

* 先支持Java
* 之后再更直观的支持Kotlin. 当然在没有直观支持kotlin之前,也可以通过java的方式通过稍微复杂的配置实现kotlin的支持.需要对编译之后的kotlin与java的区别做一些了解,尤其object,componnet object,@JVMStatic 等的原理

## 1. 在方法定义处Hook

* 原方法：`o.f(a,b):R`

### 1.1 before:在方法开始处hook

```
原方法非静态:
o.f(a,b):R {
    H.f(info,o:O,a:A,b:B):V
    ....
    return ...
}

o.f(a,b):V {
    H.f(info,o:O,a:A,b:B):V
    ...
}

原方法静态:

Origin.s(a,b):R {
    H.f(info,a:A,b:B):V
    ...
    return  ...
}

Origin.s(a,b):V {
    H.f(info,a:A,b:B):V
    ...
}
```

### 1.2 after:在方法结尾处hook. 将原结果传入，返回hook之后的结果

```
原方法非静态:
o.f(a,b):R {
    ....
    return H.f(info,o:O,a:A,b:B,res:R):R
}

o.f(a,b):V {
    ....
    return H.f(info,o:O,a:A,b:B):V
}

原方法静态:

Origin.s(a,b):R {
    val res = .. // 先执行原有逻辑，将结果作为参数产地给替换者方法
    return H.f(info,a:A,b:B,res:R):R
}

Origin.s(a,b):V {
    ...  // 先执行原有逻辑，将结果作为参数产地给替换者方法
    return H.f(info,a:A,b:B):V
}
```

### 1.3 replace:不执行方法体内容，直接hook,返回hook结果。

```
原方法非静态:
o.f(a,b):R {
    return H.f(info,o:O,a:A,b:B):R
}

o.f(a,b):V {
    return H.f(info,o:O,a:A,b:B):V
}

原方法静态:

Origin.s(a,b):R {
    return H.f(info,a:A,b:B):R
}

Origin.s(a,b):V {
    return H.f(info,a:A,b:B):V
}

```

## 2. 在方法调用处Hook

* 原方法调用处:

```
{
    o.f(a,b):R
}

{
    o.f(a,b):V
}

```

### 1.1 before:方法执行之前hook

* 可以在同一个原方法上定义多个beforeMethod

```
原方法非静态:
{
    H.f(info,o:O,a:A,b:B):V
    o.f(a,b):R
}

{
    H.f(info,o:O,a:A,b:B):V
    o.f(a,b):V
}

原方法静态:

{
    H.f(info,a:A,b:B):V
    Origin.f(a,b):R
}

{
    H.f(info,a:A,b:B):V
    Origin.f(a,b):R
}

```

### 1.2 after:方法执行之后Hook，并将参数和结果同时传递给替换者。

* 每个原方法只能定义一个afterMethod

```
原方法非静态:
{
    val res = o.f(a,b):R
    H.f(info,o:O,a:A,b:B,res:R):R 
}

{
    val res = o.f(a,b):V
    H.f(info,o:O,a:A,b:B):V
}

原方法静态:
{
     val res = Origin.f(a,b):R
     H.f(info,a:A,b:B,res:R):R 
}

{
     val res = Origin.f(a,b):V
     H.f(info,a:A,b:B):V
}
```

### 1.3 replace:直接替换

* 每个原方法只能定义一个replaceMethod

```
原方法非静态:
{
    H.f(info,o:O,a:A,b:B):R
}

{
    H.f(info,o:O,a:A,b:B):V
}

原方法静态:

{
    H.f(info,a:A,b:B):R
}

{
    H.f(info,a:A,b:B):V
}

```