<h1>Student Conduct Points Management System</h1>
<p align="center">
    <a href="https://react.dev/">
        <img src="https://img.shields.io/badge/ReactJS-61DAFB.svg?logo=react&labelColor=white"/>
    </a>
    <a href="https://spring.io/">
        <img src="https://img.shields.io/badge/Spring MVC-6DB33F.svg?logo=spring&labelColor=white"/>
    </a>
    <a href="https://spring.io/">
        <img src="https://img.shields.io/badge/Spring Security-6DB33F.svg?logo=springsecurity&labelColor=white"/>
    </a>
    <a href="https://spring.io/">
        <img src="https://img.shields.io/badge/Hibernate-59666C.svg?logo=hibernate&labelColor=white&logoColor=59666C"/>
    </a>
    <a href="https://www.mysql.com/">
        <img src="https://img.shields.io/badge/MySQL-4479A1.svg?logo=mysql&labelColor=white"/>
    </a>
    <a href="https://www.docker.com/">
        <img src="https://img.shields.io/badge/Docker-2496ED.svg?logo=docker&labelColor=white"/>
    </a>
    <a href="https://firebase.google.com/">
        <img src="https://img.shields.io/badge/Firebase-DD2C00.svg?logo=firebase&labelColor=white&logoColor=red">
    </a>
    <a href="https://cloudinary.com/">
        <img src="https://img.shields.io/badge/Cloudinary-3448C5.svg?logo=cloudinary&labelColor=white&logoColor=3448C5"/>
    </a>
    <a href="https://aws.amazon.com/vi/rds/">
        <img src="https://img.shields.io/badge/amazon RDS-527FFF.svg?logo=amazonrds&logoColor=527FFF&labelColor=white"/>
    </a>
    <a href="https://aws.amazon.com/vi/ec2/">
        <img src="https://img.shields.io/badge/Amazone EC2-FF9900.svg?logo=amazonec2&labelColor=white"/>
    </a>
    <a href="https://aws.amazon.com/vi/s3/">
        <img src="https://img.shields.io/badge/Amazone S3-569A31.svg?logo=amazons3&labelColor=white"/>
    </a>
    <a href="https://developer.paypal.com/home/">
        <img src="https://img.shields.io/badge/PayPal-003087.svg?logo=paypal&logoColor=003087&labelColor=white"/>
    </a>
    <a href="https://sandbox.vnpayment.vn/apis/">
        <img src="https://img.shields.io/badge/VnPay-crimson.svg">
    </a>
    <a href="https://docs.zalopay.vn/v2/">
        <img src="https://img.shields.io/badge/ZaloPay-1f88e5.svg">
    </a>
    
# Overview
The Student Conduct Points Management System is designed to manage and monitor the conduct points of students at the university. The system provides various functionalities for different roles such as Student Affairs, student assistants, and students. Each role has specific permissions and functions to efficiently manage conduct points and extracurricular activities

# Table of contents
 - [Features](#features)
 - [System Architecture](#system-architecture)
 - [Database](#database)
 - [How To Setup](#how-to-setup)
 - [User Interface](#user-interface)
 - [Administration](#administration)
 - [Deloyment](#deloyment)
 - [Docs](#docs)
# Features
1. <b>Login/Register</b>
    - Students can register for an account using university email and required to upload avatar
    <a href="https://cloudinary.com/">
        <img src="https://img.shields.io/badge/Cloudinary-3448C5.svg?logo=cloudinary&labelColor=white&logoColor=3448C5"/>
    </a>

    - Student Affairs can create accounts for student assistants of various departments

2. <b>Conduct Points Management</b>
    - <i>Student Affairs</i>
        * View statistics of conduct points for the entire school, categorized by department, class, and achievement.

        * Export conduct points data as PDF or CSV files.

        * Access all functionalities available to student assistants

    - <i>Student Assistants</i>
        * View a student's extracurricular achievements and the list of activities reported as missing

        * Confirm or reject conduct points based on provided evidence

        * Create new upcoming extracurricular activities and upload attendance lists from CSV files to update conduct points
    
        * View conduct points statistics by class and achievement levels, and export detailed points data (individual rules, total points, ranking, student information) as PDF or CSV files.

3. <b>Extracurricular Activities Management</b>
    - <i>Student</i>
        * Register for upcoming extracurricular activities posted on the school's bulletin board

        * View history of participation in extracurricular activities, conduct points per regulation, and total conduct points

        * export conduct points as PDF by making payments using
            
            <a href="https://developer.paypal.com/home/">
                <img src="https://img.shields.io/badge/PayPal-003087.svg?logo=paypal&logoColor=003087&labelColor=white"/>
            </a>
            <a href="https://sandbox.vnpayment.vn/apis/">
                <img src="https://img.shields.io/badge/VnPay-crimson.svg">
            </a>
            <a href="https://docs.zalopay.vn/v2/">
                <img src="https://img.shields.io/badge/ZaloPay-1f88e5.svg">
             </a>

        * Report missing points with evidence if they participated in activities but have not received points

    - <i>Bulletin Board</i>
        * A platform for posting extracurricular activities created by student assistants

        * Students can register for activities, like, and comment on them

4. <b>Real-time chat</b>
    - Student can chat with student assistants and friends 
    <a href="https://firebase.google.com/">
        <img src="https://img.shields.io/badge/Firebase-DD2C00.svg?logo=firebase&labelColor=white&logoColor=red">
    </a>

# System Architecture
![sys](/images/system_architecture.svg)
# Database
![database](/images/database.jpg)
# How to Setup
## Requirements
1. [Docker](https://www.docker.com/)

2. [Apache Maven](https://maven.apache.org/download.cgi)

````bash
# Clone the project
git clone https://github.com/locnguyn/TrainingPointsManagement_SpringMVC_ReactJS
````

## Config 
1. Fill <b>Cloudinary</b> & <b>SMTP Server</b> keys
    ````bash
    cd TrainingPointApp/src/main/resources
    # select env.properties
    # fill your keys
        # Mail  
    mail.username =add_your_username
    mail.password =add_your_password
        # Cloudinary
    cloudinary.cloud_name=add_your_clooud_name
    cloudinary.api_key=add_your_api_key
    cloudinary.api_secret=add_your_api_secret
    ````

2. (Optional) <b>FireBase</b>


    ````bash
    # create .env
    cd trainingpointweb

    ## Fill your FireBase keys
    REACT_APP_API_KEY=your_api_key_here
    REACT_APP_AUTH_DOMAIN=your_auth_domain_here
    REACT_APP_DATABASE_URL=your_database_url_here
    REACT_APP_PROJECT_ID=your_project_id_here
    REACT_APP_STORAGE_BUCKET=your_storage_bucket_here
    REACT_APP_MESSAGING_SENDER_ID=your_messaging_sender_id_here
    REACT_APP_APP_ID=your_app_id_here
    REACT_APP_MEASUREMENT_ID=your_measurement_id_here
    ````
    
## Run

````bash 
# Build .war
cd TrainingPointApp
mvn install

# Build Docker
cd ..
docker compose build

# Run Docker
docker compose up -d

# Shutdown Docker (Optional)
docker compose down
````   

# Interface
### Admininstrator Dashboard
### User Interface
![user1](/images/user1.png)
### Payments Screen
### Real-time Chat 
# Deloyment
# Docs
* 🔗 [Spring MVC](https://spring.io/guides/gs/serving-web-content)

* 🔗 [ReactJS](https://react.dev/)
* 🔗 [MySQL](https://www.mysql.com/)
* 🔗 [Nginx](https://nginx.org/en/)
* 🔗 [Docker](https://www.docker.com/)
* 🔗 [Firebse](https://firebase.google.com/)
