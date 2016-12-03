#!/usr/bin/python

import os
import subprocess
import time

folders = ["ca-ws", "ca-ws-cli", "ws-handlers", "kitchen-smartrestaurant-ws-server", "smartrestaurant-ws", "kitchen-smartrestaurant-ws-cli", "smartrestaurant-ws-cli", "customer-smartrestaurant-ws"]
cmds = ["mvn clean install exec:java", "mvn clean install", "mvn clean install", "mvn clean install exec:java", "mvn clean install exec:java", "mvn clean install", "mvn clean install", "mvn clean install"]

for idx, folder in enumerate(folders):
	os.chdir(folder)
	
	if os.name == "posix":
		subprocess.Popen(args=["gnome-terminal", "--command=" + cmds[idx]])
	elif os.name == "nt":
		subprocess.Popen("cmd /K " + cmds[idx])
	
	if folder == "smartrestaurant-ws":
		time.sleep(15)
	else:
		time.sleep(5)
	os.chdir("..")