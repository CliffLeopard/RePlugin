#!/usr/bin/env bash
chmod a+x gradlew

rm -rvf ./plugin-demo1/build
rm -rvf ./plugin-demo2/build

rm ./host-app/src/main/assets/plugins/demo1.jar
rm ./host-app/src/main/assets/plugins/demo2.jar

./gradlew :host-app:clean
./gradlew :plugin-demo1:assembleRelease   #--info
./gradlew :plugin-demo2:assembleRelease  #--info