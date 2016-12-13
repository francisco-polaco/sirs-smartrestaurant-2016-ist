@echo off
title Install
echo Building CA and its client
cd ca-ws
start "CA-WS" cmd /c "mvn clean install exec:java"
cd ..
cd ca-ws-cli
timeout 7
start "CA-WS-CLI" cmd /c "mvn clean install & exit"
echo CA stuff is ready
cd ..
echo Building Handlers
cd ws-handlers
timeout 7
start "handlers" cmd /c "mvn clean install & exit"
cd ..
echo Handlers Ready
echo Building SmartRestaurant and Kitchen
cd kitchen-smartrestaurant-ws-server
timeout 7
start "kitchen-ws" cmd /c "mvn clean install exec:java"
cd ..
cd waiter-smartrestaurant-ws
timeout 7
start "waiter-ws" cmd /c "mvn clean install exec:java & exit"
cd ..
cd smartrestaurant-ws
timeout 7
start "SmartRestaurant" cmd /c "mvn clean install exec:java"
cd ..
echo After building SmartRestaurant please press enter on the waiter-ws and kitchen-ws windows and then press any here.
pause
cd kitchen-smartrestaurant-ws-cli
start "Kitchen Client" cmd /c "mvn clean install"
cd ..
cd kitchen-smartrestaurant
timeout 7
start "Kitchen" cmd /c "mvn clean install exec:java"
cd ..
cd smartrestaurant-ws-cli
timeout 7
start "sr-cli" cmd /c "mvn clean install & exit"
cd ..
echo SR and Kitchen Ready
echo Building Waiter terminal
cd waiter-smartrestaurant-ws-cli
timeout 7
start "waiter-cli" cmd /c "mvn clean install & exit"
cd ..
cd waiter-smartrestaurant
timeout 7
start "Waiter" cmd /c "mvn clean install exec:java"
cd ..
echo Building Customer
cd customer-smartrestaurant-ws
timeout 7
start "Client" cmd /c "mvn clean install exec:java"
cd ..
echo Customer Ready
echo "Everything is ready"
exit