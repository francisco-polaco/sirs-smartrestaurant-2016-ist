echo "Ca"
cd ca-ws
start mvn clean install exec:java
cd ..
cd ca-ws-cli
timeout 7
start mvn clean install
echo "Ca ready"
cd ..
cd ws-handlers
timeout 7
start mvn clean install
cd ..
echo "Handlers Ready"
cd kitchen-smartrestaurant-ws-server
timeout 7
start mvn clean install exec:java
cd ..
cd smartrestaurant-ws
timeout 7
start mvn clean install exec:java
cd ..
cd kitchen-smartrestaurant-ws-cli
timeout 14
start mvn clean install
cd ..
cd smartrestaurant-ws-cli
timeout 7
start mvn clean install
cd ..
echo "SR and Kitchen Ready"
cd customer-smartrestaurant-ws
timeout 7
start mvn clean install
echo "Customer Ready"
echo "Everything is ready"
pause
exit