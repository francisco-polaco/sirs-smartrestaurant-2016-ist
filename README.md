# SmartRestaurant
Project for the SIRS course 2016 @ IST.
# Setup
This project has two main ways to deploy, using VMs or using your own machine.
## Virtual Machines
Using the VMs available here you will be able to effortless setup the project. There is 5 VMs:
* The Gateway
* The Main Server
* The Kitchen
* The Waiter
* The Device

Each one of them is properly configured and ready to use.
## Your own setup
Using your own setup we provide a couple of scripts, however you need to consider the following
### Support Software
* Ubuntu x64 16.04 LTS
* Oracle Java 1.8.0_112
* MySQL Community Server 5.7
* Maven 3.3.3
* Tomcat 

Note that we try Windows 10 x64 and it worked, however we don't support it, use it at your own risk.
### Scripts
* Go to the extras/mysql directory and run both scripts, one will create an user and other will create a database. The tables will be automatically created by Fénix Framework.
* Use ```keytool``` to generate certificates, after that copy .cer files to ca-ws/certs and .jks file to the folders of the modules. We already provide default certificates and keystores..
* Use the class provided in the ```extras``` folder to generate two aes keys, copy the two to ```smartrestaurant-ws/extras``` and which key for the specific module, i.e, kitchen and waiter.
* If you are using Windows, run the ```install.bat``` script located in the INSTALL folder. Follow the on-screen instructions. (It only supports running everything at the same machine)
* If you are using Linux, run the specific script of what you want to do. Note that each script has a variable at the top, if it's 0 it will assume you will be running everything locally, if it's 1, it will assume that you will be using our VMs.
### Tomcat
Tomcat is what ensures https connection
## Get a client to work
For the client to recognize the methods from the server, you will need to get the server running and then get the wsdl from it, since this project is done in a implementation-first way. Check the client example for concrete code.
