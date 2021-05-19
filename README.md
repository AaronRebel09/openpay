REST API To TEST OPEN PAY

requirements

java
mvn

how to run service from console

$>: mvn spring-boot:run 

check in port 8080

how to test from postman
GET methods
endpoints 

{{localhost}}:{{port}}/openpay/healthCheck
{{localhost}}:{{port}}/openpay/customer
{{localhost}}:{{port}}/openpay/createCreditCard
{{localhost}}:{{port}}/openpay/chargingCreditCard
