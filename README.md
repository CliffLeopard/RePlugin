## Replugin

### 说明

* 本项目Fork自[Qihoo360/Replugin](https://github.com/Qihoo360/RePlugin)

### 更新内容

* gradle-plugin 更新至4.1.3
* gradle 更新至6.5
* 支持AndroidX

### 配置

* 将支持`AndroidX`的项目发布到本地:
    * `gradle.properties`中修改配置`SDK_PUBLISH=true`
    * `gradle.properties`中修改配置`android.useAndroidX=true` `android.enableJetifier=true`
    * 版本号见`rp-config.gradle`: 默认版本号为:`2.3.5-SNAPSHOT-androix`
    * 执行工程Task的`publishToMavenLocal`
    
* 将支持`support`的项目发布到本地:
    * `gradle.properties`中修改配置`SDK_PUBLISH=true`
    * `gradle.properties`中修改配置`android.useAndroidX=false` `android.enableJetifier=false`
    * 版本号见`rp-config.gradle`: 默认版本号为:`2.3.5-SNAPSHOT-support`
    * 执行工程Task的`publishToMavenLocal`
    
* 运行Demo项目:
    * `gradle.properties`中修改配置`SDK_PUBLISH=false`
    * 运行工程目录下的`publish_plugin_install_to_host.sh`
    
* 本工程的Demo项目已经全部更改为依赖`androix`
    
    
