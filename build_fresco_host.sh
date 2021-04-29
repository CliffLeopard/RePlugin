#!/usr/bin/env bash
chmod a+x gradlew

rm -rvf ./fresco-plugin/build


rm ./fresco-host-app/src/main/assets/plugins/plugin1.jar

./gradlew :fresco-host-app:clean
./gradlew :fresco-plugin:assembleDebug   #--info


cp fresco-plugin/build/outputs/apk/debug/fresco-plugin-debug.apk    ./fresco-host-app/src/main/assets/plugins/plugin1.jar


#主程序编译
./gradlew :fresco-host-app:assembleDebug #--info

adb uninstall com.qihoo360.replugin.fresco.host

#安装
adb install -r  ./fresco-host-app/build/outputs/apk/debug/fresco-host-app-debug.apk
adb shell am start com.qihoo360.replugin.fresco.host/com.qihoo360.replugin.fresco.host.MainActivity
