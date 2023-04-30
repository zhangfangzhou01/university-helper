# university-helper

## 项目介绍

本项目是一个面向广大大学生的在线平台，旨在服务大学生，包括外卖最后一公里难题等的在线平台

## 项目结构

```
university-helper
├── README.md
├── pom.xml
├── main
│   ├── java.com.universityhelper
│   │   ├── authentication
│   │   │   ├── token
│   │   │   ├── password
│   │   │   ├── email
│   │   ├── config
│   │   ├── controller
│   │   ├── dao
│   │   ├── entity
│   │   │   ├── dto
│   │   │   ├── po
│   │   │   ├── vo
│   │   ├── service
│   │   │   ├── impl
│   │   ├── util
│   │   ├── exception
│   │   ├── validation
│   │   ├── UniversityhelperApplication.java
│   ├── resources
│   │   ├── static
│   │   ├── application.yml
```

## 项目涉及

- SpringBoot
- SpringSecurity
- Druid
- Mybatis
- MybatisPlus
- JWT
- Swagger
- WebSocket
- MySQL
- Redis
- Knife4j
- Hutool
- Lombok
- Jackson

## 项目部署

### 1.环境准备

- JDK 19
- MySQL 8.0.28
- Maven 3.6.3

### 2.数据库配置

- 创建数据库universityhelper
- 执行sql文件夹下的sql文件

### 3.项目部署

- 修改application.yml中的数据库配置
- 启动项目
- 访问 http://localhost:8080/swagger-ui.html
- 或访问 http://localhost:8080/doc.html
- 登录账号：2027405037 密码：123456

## 项目演示页面

- swagger: http://47.120.32.226:8080/swagger-ui.html
- knife4j: http://47.120.32.226:8080/doc.html
- druid: http://47.120.32.226:8080/druid/index.html

## 项目成员

- zfz 2027405037
- wsj 2027405036

## 项目进度

- [x] 项目初始化
- [x] 登录模块
- [x] 用户模块
- [x] 任务模块
- [x] 论坛模块
- [x] 聊天模块