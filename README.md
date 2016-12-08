# SmartRestaurant
Project for the SIRS course 2016 @ IST.
# Setup
This project has two main ways to deploy, using VMs or using your own machine.
## Virtual Machines
Using the VMs available here you will be able to effortless setup the project. There is 5 VMs:
The Gateway
The Main Server
The Kitchen
The Waiter
The Device
Each one of them is properly configured and ready to use.
## Your own setup
Using your own setup we provide a couple of scripts, however you need to consider the following
### Support Software

Apart from Java and Maven, you will require mysql server installed and running.</br>
Go to the extras/mysql directory and run both scripts, one will create an user and other will create a database. The tables will be automatically created by Fénix Framework.

## Get a client to work
For the client to recognize the methods from the server, you will need to get the server running and then get the wsdl from it, since this project is done in a implementation-first way. Check the client example for concrete code.
