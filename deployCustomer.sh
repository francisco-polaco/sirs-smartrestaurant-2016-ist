#!/bin/bash

vm=1

cd customer-smartrestaurant-ws
if [ $vm -eq 1 ];then
	mvn install -Pvm
else
	mvn install
fi

if [ $? -ne 0 ];then
	echo -e "\e[0;31mcustomer-smartrestaurant-ws mvn unsuccessful\e[0m"
	exit 1
fi

if [ $vm -eq 1 ];then
	gnome-terminal -e "mvn exec:java -Pvm"
else
	gnome-terminal -e "mvn exec:java"
fi
cd ..
sleep 3
