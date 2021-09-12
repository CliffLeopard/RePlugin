#!/usr/bin/env bash
#chmod a+x gradlew
#
rm -rvf ./sample-plugin/plugin-demo1/build
rm -rvf ./sample-plugin/plugin-demo2/build
rm -rvf ./sample-plugin/plugin-webview/build
rm -rvf ./sample-plugin/plugin-demo3-kotlin/build


rm ./sample-host/host-app/src/main/assets/plugins/demo1.jar
rm ./sample-host/host-app/src/main/assets/plugins/demo2.jar
rm ./sample-host/host-app/src/main/assets/plugins/webview.jar
rm ./sample-host/host-app/src/main/assets/external/demo3.apk


startTime=$(date '+%s')

./gradlew :sample-host:host-app:clean
./gradlew :sample-plugin:plugin-demo1:assembleDebug   #--info
./gradlew :sample-plugin:plugin-demo2:assembleDebug  #--info
./gradlew :sample-plugin:plugin-webview:assembleDebug  #--info
./gradlew :sample-plugin:plugin-demo3-kotlin:assembleDebug  #--info

cp ./sample-plugin/plugin-demo1/build/outputs/apk/debug/plugin-demo1-debug.apk                    ./sample-host/host-app/src/main/assets/plugins/demo1.jar
cp ./sample-plugin/plugin-demo2/build/outputs/apk/debug/plugin-demo2-debug.apk                    ./sample-host/host-app/src/main/assets/plugins/demo2.jar
cp ./sample-plugin/plugin-webview/build/outputs/apk/debug/plugin-webview-debug.apk                ./sample-host/host-app/src/main/assets/plugins/webview.jar
cp ./sample-plugin/plugin-demo3-kotlin/build/outputs/apk/debug/plugin-demo3-kotlin-debug.apk      ./sample-host/host-app/src/main/assets/external/demo3.apk


#主程序编译
./gradlew :sample-host:host-app:assembleDebug #--info

endTime=$(date '+%s')
timeGap=$((endTime-startTime))
echo "startTime:$startTime"
echo "endTime:$endTime"
echo "timeGap:$timeGap"

adb uninstall com.qihoo360.replugin.sample.host
adb uninstall com.qihoo360.replugin.sample.demo1
adb uninstall com.qihoo360.replugin.sample.demo2
adb uninstall com.qihoo360.replugin.sample.demo3
adb uninstall com.qihoo360.replugin.sample.webview
adb uninstall com.qihoo360.replugin.fresco.host
adb uninstall com.qihoo360.replugin.fresco.plugin

#安装
adb install -r  ./sample-host/host-app/build/outputs/apk/debug/host-app-debug.apk
adb shell am start com.qihoo360.replugin.sample.host/com.qihoo360.replugin.sample.host.MainActivity
