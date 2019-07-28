# microservice_in_spring_cloud_netflix


Microservice:
=========================================================
Services which expose by rest .Small deployment unit these should me cloud enable .

Problem in monolythic problem 
=========================================================
Bound Context;-
Configration menagement 
Dynamic scale up and Scale Down
Visibility 
Pack of Cards:-if one service is down whole application will down.

Spring Cloud:
========================================================
Configration Management:- Spring Cloud Config Server in GitRepositery
Dynamic scale up and scale down:- 
           1:- Naming Server(Eureka) 
		   2:- Ribbon(Client Side Load Balancing)
		   3:- Feign(Easier REST Clients)

Visibility and Monitoring
           1:- Zipkin Distributed Tracing
		   2:- Netflix API Gateway
		   
Fault torence:-
		   1:-Hytrix
		   
Miroservice Advantage :
 1:-New Technology & Process Adaption 
 2:-Dynamic Scaling
 3:- Faster Release Cycle
 

For H2 data base 
varify that JDBC URL has value 
jdbc:h2:mem:testdb

Feign !-
========================================================
when we talk about microservice there will be lot of call to other services  you want to call much simplers 
Feign make it very easy to invock other micro services to other restfull services
other best think Feign provide it provides degradetion called ribben (Client side load balance) 

Ribbon
============================================================
Distributted loadbalance to all the services.

Eureka Naming Server(Service Descovery):
============================================================
How many service are active or down 
service monitering 
service mantaince

All the Microservice are register in Eureka naming server(service registration).


API Gateway(ZUUL)
=====================================================================
1.Authentication,authorization and security
2.Rate Limits
3.Fault Tolerance
4.Service Aggregation

 http://localhost:8100/currency-converter-fiegn/from/USD/to/INR/quantity/100
 http://localhost:8765/currency-conversion-service/currency-converter-fiegn/from/USD/to/INR/quantity/100
 
 
 Distributed tracing (Springcloud sleuth zipkin)
 =====================================================================
 How to find defact ?
 one single centralization location where we  complete chain of request.
 
 Zinkin Distributed Tracing
 =====================================================================
centralization all the log base on Unique_id we can find the log
and you can find very and trace which api is giving error .
 
(RabitMQ)-->ZinkinDistributedTracingServer
How to start Zipkin server 
hit -> https://zipkin.io/quickstart.sh on the browser
or read this  https://zipkin.io/pages/quickstart

install rabbitMQ
https://www.rabbitmq.com/install-windows.html
and erlang download this one 
https://www.erlang-solutions.com/resources/download.html

check RAbbitMQ is running  >set RABBIT_URI=amqp://localhost
and java -jar zipkin.jar

it will start localhost:9411/zipkin

and then you need to add dependecy in service to get log
```
 <dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
 <dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```

Circuit Breaker(Hytrix)
===============================================================================
Isolate service failures with a basic state machine around (by definition risky) 
service to service communication 

also called a bulk head

Hytrix is easy to plugin and easy to moniter services


 


