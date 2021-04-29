#!/usr/bin/env bash
chmod a+x gradlew

rm -rvf ./plugin-demo1/build
rm -rvf ./plugin-demo2/build
rm -rvf ./plugin-webview/build
rm -rvf ./plugin-demo3-kotlin/build


rm ./host-app/src/main/assets/plugins/demo1.jar
rm ./host-app/src/main/assets/plugins/demo2.jar
rm ./host-app/src/main/assets/plugins/webview.jar
rm ./host-app/src/main/assets/external/demo3.apk

./gradlew :host-app:clean
./gradlew :plugin-demo1:assembleDebug   #--info
./gradlew :plugin-demo2:assembleDebug  #--info
./gradlew :plugin-webview:assembleDebug  #--info
./gradlew :plugin-demo3-kotlin:assembleDebug  #--info

cp ./plugin-demo1/build/outputs/apk/debug/plugin-demo1-debug.apk                    ./host-app/src/main/assets/plugins/demo1.jar
cp ./plugin-demo2/build/outputs/apk/debug/plugin-demo2-debug.apk                    ./host-app/src/main/assets/plugins/demo2.jar
cp ./plugin-webview/build/outputs/apk/debug/plugin-webview-debug.apk                ./host-app/src/main/assets/plugins/webview.jar
cp ./plugin-demo3-kotlin/build/outputs/apk/debug/plugin-demo3-kotlin-debug.apk      ./host-app/src/main/assets/external/demo3.apk


#主程序编译
./gradlew :host-app:assembleDebug #--info

adb uninstall com.qihoo360.replugin.sample.host
adb uninstall com.qihoo360.replugin.sample.demo1
adb uninstall com.qihoo360.replugin.sample.demo2
adb uninstall com.qihoo360.replugin.sample.demo3
adb uninstall com.qihoo360.replugin.sample.webview
adb uninstall com.qihoo360.replugin.fresco.host
adb uninstall com.qihoo360.replugin.fresco.plugin


#安装
adb install -r  ./host-app/build/outputs/apk/debug/host-app-debug.apk
adb shell am start com.qihoo360.replugin.sample.host/com.qihoo360.replugin.sample.host.MainActivity
