## Replugin

### 说明

* 本项目Fork自[Qihoo360/Replugin](https://github.com/Qihoo360/RePlugin)
* 进行了一系列更新

### 更新内容

* gradle-plugin 更新至4.2.2
* gradle 更新至6.7.1
* 仅支持AndroidX，不再支持Support包
* 使用Kotlin重构Gradle-plugin
* 使用并发Transform和InstantRun提升编译速度。插件构建不需要clean
* 重构还未经过严格测试。稳定版本请使用[Master](https://github.com/CliffLeopard/RePlugin/tree/master)

### 配置

* 将支持项目发布到本地:
    * `gradle.properties`中修改配置`SDK_PUBLISH=true`
    * `gradle.properties`中修改配置`android.useAndroidX=true` `android.enableJetifier=true`
    * 版本号见`rp-config.gradle`: 默认版本号为:`2.4.7-SNAPSHOT-androix`
    * 执行工程Task的`publishToMavenLocal`

* 发布到个人部署的Maven仓库:
    * `local.properties`中添加以下配置
        * SONATYPE_NEXUS_USERNAME
        * SONATYPE_NEXUS_PASSWORD
        * RELEASE_REPOSITORY_URL
        * SNAPSHOT_REPOSITORY_URL
    * 执行 `publishReleasePublicationToMavenRepository`:四个组件一起发布

* 运行Demo项目:
    * `gradle.properties`中修改配置`SDK_PUBLISH=false`
    * 运行工程目录下的`publish_plugin_install_to_host.sh`
    * fresco-sample:运行工程目录下的`build_fresco_host.sh`

* 本工程的Demo项目已经全部更改为依赖`androix`,所以运行demo请保证一下配置
    * 项目已经发布到本地maven
    * `gradle.properties`中修改配置`SDK_PUBLISH=false`
    * `gradle.properties`中修改配置`android.useAndroidX=true` `android.enableJetifier=true`
    
### Gradle和Hook相关

* [MethodHookReadMe](./replugin-gradle/README.md)
* [LambdaHookReadMe](./replugin-gradle/LambdaHook.md)

### 关于Android限制访问隐藏API的说明

#### Hook点:

Replugin只对ClassLoader进行了Hook. Hook的主要内容为以下:

1. 拿到`android.app.ContextImpl`的`mPaclageInfo`字段，类型为`android.app.LoadedApk`
2. 修改`mPaclageInfo`的`mClassLoader`字段，改为自己的`RePluginClassLoader`
3. 将`mPaclageInfo`的原来的`mClassLoader`的放入`RePluginClassLoader`中，供反射调用各种方法,和`pathList`字段
4. 由于原来的`mClassLoader`为`dalvik.system.PathClassLoader`,没有重写以上方法，所以实际调用的是其父类`dalvik.system.BaseDexClassLoader`的`pathList`字段和如下方法:
    * `findResource`
    * `findResources`
    * `findLibrary`
    * `getPackage`


#### Android SDK 限制

* 查看[Android11 API限制](https://developer.android.com/about/versions/11/non-sdk-11),下载 `hiddenapi-flags.csv`. 可知，以上的hook点如下:
    * `Landroid/app/ContextImpl;->mPackageInfo:Landroid/app/LoadedApk;,greylist`
    * `Landroid/app/LoadedApk;->mClassLoader:Ljava/lang/ClassLoader;,greylist`
    * `Landroid/app/ContextImpl;->mClassLoader:Ljava/lang/ClassLoader;,greylist`
    * `Ldalvik/system/BaseDexClassLoader;->pathList:Ldalvik/system/DexPathList;,greylist`
    * `Ldalvik/system/BaseDexClassLoader;->findLibrary(Ljava/lang/String;)Ljava/lang/String;,core-platform-api,public-api,system-api,test-api,whitelist`
    * `Ldalvik/system/BaseDexClassLoader;->findResource(Ljava/lang/String;)Ljava/net/URL;,core-platform-api,public-api,system-api,test-api,whitelist`
    * `Ldalvik/system/BaseDexClassLoader;->findResources(Ljava/lang/String;)Ljava/util/Enumeration;,core-platform-api,public-api,system-api,test-api,whitelist`
    * `Ldalvik/system/BaseDexClassLoader;->getPackage(Ljava/lang/String;)Ljava/lang/Package;,core-platform-api,public-api,system-api,test-api,whitelist`

* 查看[Android12 API限制](https://developer.android.com/about/versions/12/non-sdk-12),下载 `hiddenapi-flags.csv`. 可知，以上的hook点如下:
    * `Landroid/app/ContextImpl;->mPackageInfo:Landroid/app/LoadedApk;,unsupported`
    * `Landroid/app/LoadedApk;->mClassLoader:Ljava/lang/ClassLoader;,unsupported`
    * `Landroid/app/ContextImpl;->mClassLoader:Ljava/lang/ClassLoader;,unsupported`
    * `Ldalvik/system/BaseDexClassLoader;->pathList:Ldalvik/system/DexPathList;,unsupported`
    * `Ldalvik/system/BaseDexClassLoader;->findLibrary(Ljava/lang/String;)Ljava/lang/String;,core-platform-api,public-api,sdk,system-api,test-api`
    * `Ldalvik/system/BaseDexClassLoader;->findResource(Ljava/lang/String;)Ljava/net/URL;,core-platform-api,public-api,sdk,system-api,test-api`
    * `Ldalvik/system/BaseDexClassLoader;->findResources(Ljava/lang/String;)Ljava/util/Enumeration;,core-platform-api,public-api,sdk,system-api,test-api`
    * `Ldalvik/system/BaseDexClassLoader;->getLdLibraryPath()Ljava/lang/String;,core-platform-api,unsupported`
    * `Ldalvik/system/BaseDexClassLoader;->getPackage(Ljava/lang/String;)Ljava/lang/Package;,core-platform-api,public-api,sdk,system-api,test-api`

* 查看[Android10 注释释意](https://developer.android.google.cn/about/versions/10/non-sdk-q#greylist-now-public)可知。
    * `greylist`和`unsupported`都是不受限制的灰名单
    * `whitelist`在白名单中，更不受限制
* 所以综合上述可知。Replugin至少在`Android11:API30`和`Android12:API31`上依然是可用的。