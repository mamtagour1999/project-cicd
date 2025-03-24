# project-cicd
                                          **Spring Boot + DevOps Project***

# 📷 Image Upload & Management System 🚀  

## 🔹 Project Description  
This is a **Spring Boot REST API** that allows users to **upload, store, and retrieve images** using MySQL database.  
The project is **Dockerized and deployed on AWS EC2 using CI/CD pipeline (GitHub Actions).**  

## 🔹 Technologies Used  
✅ **Backend:** Java, Spring Boot, REST API  
✅ **Database:** MySQL  
✅ **DevOps:** Docker, GitHub Actions, AWS EC2  
✅ **Monitoring:** Prometheus & Grafana  

## 🔹 Features  
✔ Users can **upload images** via REST API  
✔ Images are **stored in MySQL database**  
✔ Secure **JWT Authentication** implemented  
✔ **CI/CD pipeline automates deployment** on AWS  

## 🔹 Installation & Setup  
### **1️⃣ Clone the Repository**  
```bash
git clone https://github.com/yourusername/image-upload-system.git  
cd image-upload-system  

2️⃣ Build & Run Application
mvn clean package  docker build -t image-upload-app .  
java -jar target/image-upload.jar

3️⃣ using Github action write my pipeline then run pipline 
 auto build and depoly application on aws server
