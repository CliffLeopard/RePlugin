#!/usr/bin/env bash
chmod a+x gradlew




# plugin-demo1
./gradlew :plugin-demo1:assembleDebug  --info

# plugin-demo2
./gradlew plugin-demo2:assembleDebug  --info

# plugin-webview
./gradlew :plugin-webview:assembleDebug  --info

# plugin-demo3-kotlin
./gradlew :plugin-demo3-kotlin:assembleDebug  --info



rm ./replugin-sample/host/app/src/main/assets/plugins/demo1.jar
rm ./replugin-sample/host/app/src/main/assets/plugins/demo2.jar
rm ./replugin-sample/host/app/src/main/assets/plugins/webview.jar
rm ./replugin-sample/host/app/src/main/assets/external/demo3.apk


cp ./replugin-sample/plugin/plugin-demo1/app/build/outputs/apk/plugin-demo1-debug.apk  ./replugin-sample/host/app/src/main/assets/plugins/demo1.jar
cp ./replugin-sample/plugin/plugin-demo2/app/build/outputs/apk/plugin-demo2-debug.apk  ./replugin-sample/host/app/src/main/assets/plugins/demo2.jar
cp ./replugin-sample/plugin/plugin-webview/app/build/outputs/apk/plugin-webview-debug.apk  ./replugin-sample/host/app/src/main/assets/plugins/webview.jar
cp ./replugin-sample/plugin/plugin-demo3-kotlin/app/build/outputs/apk/plugin-demo3-kotlin-debug.apk  ./replugin-sample/host/app/src/main/assets/external/demo3.apk


#清除build
./gradlew :host-app:clean
#主程序编译
./gradlew :host-app:assembleDebug --info

adb uninstall com.qihoo360.replugin.sample.host

#安装
adb install -r  ./replugin-sample/host/app/build/outputs/apk/host-app-debug.apk
adb shell am start com.qihoo360.replugin.sample.host/com.qihoo360.replugin.sample.host.MainActivity
