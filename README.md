<center>
<font size="6" face="Georgia"> <h3> "Final work of the course “Development on the Spring Framework”"
</h3>
</font>
</center>
<center
><font size="3" face="Georgia"> 
<h3>
«The backend component of the hotel booking service with
the ability to manage content through the CMS administrative pane» </h3></font>
</center>
<h2 style="text-align: center;">

![image](./image/3.png )</h2>

</h2>

## Overview:
- The entity “Hotel” is described. Each object must have a name, title
  advertisements, city where the hotel is located, address, distance from the center
  city, rating (from 1 to 5) and the number of ratings on the basis of which it was calculated
  rating.
- The entity “Room” is described. For each object the following is indicated: name,
  description, number, price, maximum number of people allowed
  accommodate, and dates when the room is unavailable. Each room is in
  specific hotel
- The “User” entity is described. Each user has a unique name,
  password, email and role (user or administrator)
- The “Reservation” entity is described. It includes check-in and check-out dates,
  information about the booked room and the user who makes the reservation.



## Features:
- Search by ID of a specific hotel;
- Creation of a hotel;
- Editing a hotel;
- Deleting a hotel;
- Obtaining a list of all available hotels.
- Search by specific room ID;
- Creating a room;
- Editing a room;
- Deleting a room.
- Creating a new user with the specified role (the role is accepted as one of the parameters when creating a user). Verification is required before creating a new user by login and email for the absence of an already registered account with the same data.
- Creating a new room reservation.
- Receipt of all issued reservations.


## Prerequisites
- Java 17
- Maven (for building the application)
- Spring Boot 3.2.3
- Docker Desktop

## Setup and Installation
- Clone the repository:
- git clone [https://github.com/Katas77]
- Navigate to the project directory:
- cd contacts-application
- Build the application using Maven:
- mvn clean install
- Run the application:
- For general use:
- - Work with data-mongodb
- - Launch and configure the database via Docker
- - To run using Docker, you need to enter the following commands in the terminal:
- - cd docker
- - docker-compose up

## Security
- User registration is available without authorization.
- Creating, editing and deleting hotels is only available administrator.
- Creating, editing and deleting rooms is only available administrator.
- Obtaining a list of reservations is available only to the administrator.
- All other methods are available to both the user and the administrator, but only upon authorization.



##  Management

This app are managed through a simple command-line interface.
Input errors are handled gracefully, with prompts for correct input.

## Technologies used:

- Java
- Spring Boot
- Docker
- Kafka
- Mongodb
- Postgresql
- Liquibase
- Spring-boot-starter-security
- Spring-security

## Database:
- Postgresql
- Mongodb




![image](./image/5.png )



____
✉ Почта для обратной связи:
<a href="">krp77@mail.ru</a>