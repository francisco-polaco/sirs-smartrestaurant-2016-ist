#!/bin/bash

vm=1


cd ca-ws
if [ $vm -eq 1 ];then
	mvn install -Pvm
else
	mvn install
fi
if [ $? -ne 0 ];then
	echo -e "\e[0;31mca-ws mvn unsuccessful\e[0m"
	exit 1
fi
if [ $vm -eq 1 ];then
	gnome-terminal -e "mvn exec:java -Pvm"
else
	gnome-terminal -e "mvn exec:java"
fi
cd ..
sleep 3

cd ca-ws-cli
if [ $vm -eq 1 ];then
	mvn install -Pvm
else
	mvn install
fi
if [ $? -ne 0 ];then
	echo -e "\e[0;31mca-ws-cli mvn unsuccessful\e[0m"
	echo -e "\e[0;31mProbably ca-ws is down\e[0m"

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

echo -e "\e[0;32mAfter \e[0;93mkitchen-smartrestaurant-ws-server\e[0;32m and\e[0;93m waiter-smartrestaurant-ws\e[0;32m are running\n"
read -rsp $'Press any key to continue...\n' -n1 key
echo -e "\e[0m"

cd smartrestaurant-ws
if [ $vm -eq 1 ];then
	mvn install -Ptomcat-vm
else
	mvn install
fi
if [ $? -ne 0 ];then
	echo -e "\e[0;31msmart-restaurant mvn unsuccessful\e[0m"
	echo -e "\e[0;31mProbably kitchen-smartrestaurant-ws-server or waiter-smartrestaurant-ws is down\e[0m"
	exit 1
fi
if [ $vm -eq 1 ];then
	gnome-terminal -e "mvn exec:java -Pvm"
else
	gnome-terminal -e "mvn exec:java"
fi
cd ..
sleep 3

echo -e "\e[0;31mDon't forget to shutdown \e[0;93mkitchen-smartrestaurant-ws-server\e[0;31m and\e[0;93m waiter-smartrestaurant-ws\e[0m"
