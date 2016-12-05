@echo off
title Run
echo Building CA and its client
cd ca-ws
start "CA-WS" cmd /c "mvn exec:java"
cd ..
cd smartrestaurant-ws
timeout 7
start "SmartRestaurant" cmd /c "mvn exec:java"
cd ..
cd kitchen-smartrestaurant
timeout 7
start "Kitchen" cmd /c "mvn exec:java"
cd ..
cd waiter-smartrestaurant
timeout 7
start "Waiter" cmd /c "mvn exec:java"
cd ..
echo Building Customer
cd customer-smartrestaurant-ws
timeout 7
start "Client" cmd /c "mvn exec:java"
cd ..
echo Customer Ready
echo "Everything is ready"
exit