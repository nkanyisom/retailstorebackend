# Introduction: 
This project consists of RESTful services that implement a checkout counter for an online retail store that scans products and generates itemized invoice.

It provides services for managing products and orders.  Products can be configured with rate and category (A,B or C). Sales tax is applied based on the category of the product:
* Category A - 10%
* Category B - 20%
* Category C- 0%

Invoice details the products, quantity, total cost,sales tax and the total value of the invoice.Data for 10 Products and 1 invoice are added during startup to browse.

# REST endpoints
Client can add/update/modify products and orders using the REST endpoints.Below is overview of REST end points:

## Products
*  GET /products - fetches list of all product data
*  GET /products/{id} - fetch a specific product
*  POST /products - Creates a new product based on request JSON
*  PUT /products/{id} - Updates product data based on request JSON
*  DELETE /products/{id} - Delete an existing product if it is not associated with a invoice.


## Invoices
*  GET /invoices - fetches all invoice data
*  GET /invoices/{id} - fetches invoice of a particular id
*  POST /invoices - creates a invoice Id. Client has to use this invoice Id while adding and removing products
*  PUT /invoices/{id} - Updates invoice data. Client can add or remove products to invoice sending a JSON request.
*  DELETE /invoices/{id} - Delete invoice from the system.

 These REST end points are secured using basic authentication mechanism. Code uses in-memory repository with 'bob' as single user.

# About Implementation

This application has been using SpringBoot as it provides a wide variety of features that aid development and maintainence. Some features that were utilised were: Spring Security, Spring Data/JPA and starters.

This program and instructions have been tested on following versions on Windows laptop.
*  Apache Maven 3.5.0 
*  Java version: 1.8.0_131
*  git version 2.9.0.windows.1


# How to run the application locally?

Pre-requisites to run application are Java, Maven and Git. 
*  Installation instructions for Maven are available at https://maven.apache.org/install.html
*  Java can be installed from http://www.oracle.com/technetwork/java/javase/downloads/index.html
*  Latest Git version can be installed from https://git-scm.com/downloads

Steps to build and run locally:
* Open commandline
* Create a new directory called "retail-store-app" 
* Clone repository using following command=>   git clone https://github.com/nkanyisom/retailstorebackend.git.
* Build the executable jar using maven=> mvn package  
* Go to target folder => cd target
* Run following command to start the server on port 8086=> java -jar retail-store-app-0.0.1-SNAPSHOT.jar 
* Optionally, one can configure port using commandline parameter => --server.port=9090 
* Access and invoke APIs using url => http://localhost:8086/swagger-ui.html
* Application will ask for authentication credentials during invocation of API's. Use 'bob' as user id and password.

This application uses H2 database and does not persist data on application restarts. 

