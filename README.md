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
│   │   │   ├── JwtAccessDeniedHandler.java
│   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   ├── JwtAuthenticationFailure.java
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   ├── JwtAuthenticationLogout.java
│   │   │   ├── JwtAuthenticationSuccess.java
│   │   │   ├── JwtAuthenticationProvider.java
│   │   ├── config
│   │   │   ├── SecurityConfig.java
│   │   │   ├── SwaggerConfig.java
│   │   │   ├── WebSocketConfig.java
│   │   ├── controller
│   │   │   ├── ChatController.java
│   │   │   ├── LoginController.java
│   │   │   ├── TaskController.java
│   │   │   ├── UserController.java
│   │   │   ├── WebviewController.java
│   │   ├── entity
│   │   │   ├── dto
│   │   │   │   ├── ChatUserDto.java
│   │   │   │   ├── LoginUserDto.java
│   │   │   ├── po
│   │   │   │   ├── Chat.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── Task.java
│   │   │   │   ├── User.java
│   │   │   │   ├── UserRole.java
│   │   │   │   ├── UserTask.java
│   │   │   ├── vo
│   │   │   │   ├── ResponseResult.java
│   │   │   │   ├── ResultEnum.java
│   │   ├── service
│   │   │   ├── ChatService.java
│   │   │   ├── LoginService.java
│   │   │   ├── TaskService.java
│   │   │   ├── UserService.java
│   │   │   ├── RoleService.java
│   │   │   ├── UserRoleService.java
│   │   │   ├── UsertaketaskService.java
│   │   │   ├── UserDetailService.java
│   │   │   ├── impl
│   │   │   │   ├── ChatServiceImpl.java
│   │   │   │   ├── LoginServiceImpl.java
│   │   │   │   ├── TaskServiceImpl.java
│   │   │   │   ├── UserServiceImpl.java
│   │   │   │   ├── RoleServiceImpl.java
│   │   │   │   ├── UserRoleServiceImpl.java
│   │   │   │   ├── UsertaketaskServiceImpl.java
│   │   │   │   ├── UserDetailServiceImpl.java
│   │   ├── util
│   │   │   ├── JwtUtils.java
│   │   │   ├── JsonUtils.java
│   │   │   ├── ReflectUtils.java
│   │   ├── UniversityhelperApplication.java
│   ├── resources
│   │   ├── application.yml
```

## 项目技术

- SpringBoot
- SpringSecurity
- Mybatis
- MybatisPlus
- JWT
- Swagger
- WebSocket
- MySQL
- Knife4j
- Hutool
- Lombok
- Jackson


## 项目部署

### 1.环境准备

- JDK1.8
- MySQL8.0
- Maven4.0

### 2.数据库配置

- 创建数据库universityhelper
- 执行sql文件夹下的sql文件

### 3.项目部署
- 修改application.yml中的数据库配置
- 启动项目
- 访问http://localhost:8080/swagger-ui.html
- 或访问http://localhost:8080/doc.html
- 登录账号：2027405037 密码：123

## 项目说明

- 软工23-Group18 大学互助平台-后端

## 项目成员

- zfz 2027405037
- wsj 2027405036

## 项目进度

- [x] 项目初始化
- [x] 用户模块
- [ ] 任务模块
- [x] 聊天模块
- [x] 登录模块