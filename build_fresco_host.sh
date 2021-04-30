#!/usr/bin/env bash
chmod a+x gradlew

rm -rvf ./sample-plugin/fresco-plugin/build


rm ./sample-host/fresco-host-app/src/main/assets/plugins/plugin1.jar

./gradlew :sample-host:fresco-host-app:clean
./gradlew :sample-plugin:fresco-plugin:assembleDebug   #--info


cp ./sample-plugin/fresco-plugin/build/outputs/apk/debug/fresco-plugin-debug.apk    ./sample-host/fresco-host-app/src/main/assets/plugins/plugin1.jar


#主程序编译
./gradlew :sample-host:fresco-host-app:assembleDebug #--info

adb uninstall com.qihoo360.replugin.fresco.host

#安装
adb install -r  ./sample-host/fresco-host-app/build/outputs/apk/debug/fresco-host-app-debug.apk
adb shell am start com.qihoo360.replugin.fresco.host/com.qihoo360.replugin.fresco.host.MainActivity
