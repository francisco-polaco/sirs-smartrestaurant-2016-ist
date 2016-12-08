# SmartRestaurant
Project for the SIRS course 2016 @ IST.
</br>Group 12
# Setup
This project has two main ways to deploy, using VMs or using your own machine.

We highly recommend you to use the Virtual Machines since it will be faster and save you tedious configuration. Additionally, if you deploy the project on your own machine, you cannot see the IDS, Firewall and Gateway working.
## Virtual Machines
Using the VMs available, you will be able to effortless setup the project. There are 5 VMs:
* The Gateway
* The Main Server
* The Kitchen
* The Waiter
* The Device

Each one of them is properly configured and ready to use.</br>
[Download](https://www.dropbox.com/s/69962uwdw2xb8sw/sirs.ova?dl=0)
## Your own setup
Using your own setup we provide a couple of scripts, however you need to consider the following
### Support Software
* Ubuntu x64 16.04 LTS
* Oracle Java 1.8.0_112
* MySQL Community Server 5.7
* Maven 3.3.3
* Tomcat 
* gnome-terminal

Note that we try Windows 10 x64 and it worked, however we don't support it, use it at your own risk.

### Scripts
* Go to the extras/mysql directory and run both scripts, one will create an user and other will create a database. The tables will be automatically created by Fénix Framework.
* Use ```keytool``` to generate certificates, after that copy .cer files to ca-ws/certs and .jks file to the folders of the modules. We already provide default certificates and keystores.
* Use the class provided in the ```extras``` folder to generate two aes keys, copy the two to ```smartrestaurant-ws/extras``` and which key for the specific module, i.e, kitchen and waiter.

### Tomcat
Tomcat is what ensures HTTPS connection, you can configure it using this [tutorial](https://www.mkyong.com/tomcat/how-to-configure-tomcat-to-support-ssl-or-https/).

# Launch Project
### Using our VMs:
* Check if the corresponding script to the VM/Service has the vm flag set to 1;
* If you are starting the main server service:
    * Start Tomcat using the ```batat``` command;
* Else, run the corresponding script and follow the on-screen instructions.

```smartrestaurant-ws``` is running on Tomcat so you should not need to import any `````.war````` file or run any script.
### Using your machine (Linux):
* Check if the scripts have the vm flag set to 0;
* Run the scripts be the following order and follow the on-screen instructions:
    1. Tiago1;
    2. Tiago2;
    
To ensure all security feature you need to setup Tomcat like the VM and launch the ```smartrestaurant-ws``` there.
    
### Using your machine (Windows) (Not supported):
* Launch ```install.bat``` and follow on-screen instructions.

Note that it only runs the project locally.

# Support Files
We provide well distributed support files examples, like keystores, AES keys and certificates. The project will work without the need of creating any of these. However, you are free to explore at your own will.