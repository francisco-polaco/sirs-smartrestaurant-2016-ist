#!/bin/bash

vm=0

cd ca-ws-cli
mvn install
if [ $? -ne 0 ];then
	echo -e "\e[0;31mca-ws-cli mvn unsuccessful\e[0m"
	exit 1
fi
cd ..

cd ws-handlers
mvn install
if [ $? -ne 0 ];then
	echo -e "\e[0;31mws-handlers mvn unsuccessful\e[0m"
	exit 1
fi
cd ..

cd waiter-smartrestaurant-ws
mvn install
if [ $? -ne 0 ];then
	echo -e "\e[0;31mwaiter-smartrestaurant-ws mvn unsuccessful\e[0m"
	exit 1
fi
if [ $vm -eq 1 ];then
	gnome-terminal -e "mvn exec:java -Pvm"
else
	gnome-terminal -e "mvn exec:java"
fi
cd ..
sleep 3

echo -e "\e[0;32mAfter \e[0;93msmartrestaurant-ws\e[0;32m is running"
echo -e "\e[0;31mDon't forget to run \e[0;93mkitchen-smartrestaurant-ws-server\e[0;32m"
read -rsp $'Press any key to continue...\n' -n1 key
echo -e "\e[0m"

cd waiter-smartrestaurant-ws-cli
mvn install
if [ $? -ne 0 ];then
	echo -e "\e[0;31mwaiter-smartrestaurant-ws-cli mvn unsuccessful\e[0m"
	echo -e "\e[0;31mProbably smartrestaurant-ws is down\e[0m"
	exit 1
fi
cd ..

cd waiter-smartrestaurant
mvn install
if [ $? -ne 0 ];then
	echo -e "\e[0;31mwaiter-smartrestaurant mvn unsuccessful\e[0m"
	echo -e "\e[0;31mProbably smartrestaurant-ws is down\e[0m"
	exit 1
fi
if [ $vm -eq 1 ];then
	gnome-terminal -e "mvn exec:java -Pvm"
else
	gnome-terminal -e "mvn exec:java"
fi
cd ..
sleep 3
