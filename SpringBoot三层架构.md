​	Spring Boot 应用程序通常采用分层架构，这种架构将应用分为三个主要层次：表示层、业务逻辑层和服务层（或数据访问层）。这种分层有助于保持代码的清晰和模块化，便于维护和扩展。

### 分层架构概述

1. **表示层 (Presentation Layer)**:
   - 这一层处理用户界面相关的逻辑，通常包括 Web 控制器 (Controller)。
   - 控制器接收来自用户的请求，处理这些请求，并将它们传递给服务层。

2. **业务逻辑层 (Business Logic Layer)**:
   - 这一层包含了应用程序的核心业务逻辑。
   - 它处理业务规则和流程，通常包括服务 (Service) 类。
   - 服务类负责协调不同的组件来完成特定的业务任务。

3. **服务层 (Data Access Layer)**:
   - 这一层处理数据访问相关的逻辑。
   - 它通常包括数据访问对象 (DAO) 或者映射器 (Mapper)，这些组件负责与数据库交互。
   - 这一层可以使用 JPA、Hibernate、MyBatis 等技术来实现。

### 示例：基于 Spring Boot 的三层架构应用程序

假设我们要构建一个简单的用户管理系统，我们将展示如何使用 Spring Boot 和 MyBatis 来实现这个系统。

#### 步骤 1: 创建项目结构

1. **src/main/java/com/example/demo/controller/UserController.java**:
   - 用户控制器，处理 HTTP 请求。

2. **src/main/java/com/example/demo/service/UserService.java**:
   - 用户服务接口和实现。

3. **src/main/java/com/example/demo/mapper/UserMapper.java**:
   - 用户数据访问接口。

4. **src/main/resources/mapper/UserMapper.xml**:
   - MyBatis 映射文件，定义 SQL 查询。

5. **src/main/java/com/example/demo/pojo/User.java**:
   - 用户实体类。

#### 步骤 2: 定义实体类

```java
package com.example.demo.pojo;

public class User {
    private Integer id;
    private String name;
    private short age;
    private short gender;

    public User() {}

    public User(String name, Integer id, short age, short gender) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.gender = gender;
    }

    // 省略 getter 和 setter 方法

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }
}
```

#### 步骤 3: 定义数据访问接口

```java
package com.example.demo.mapper;

import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users")
    List<User> getAllUsers();
}
```

#### 步骤 4: 定义服务接口

```java
package com.example.demo.service;

import com.example.demo.pojo.User;
import java.util.List;

public interface UserService {
    List<User> getUsers();
}
```

#### 步骤 5: 实现服务接口

```java
package com.example.demo.service.impl;

import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getUsers() {
        return userMapper.getAllUsers();
    }
}
```

#### 步骤 6: 定义控制器

```java
package com.example.demo.controller;

import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
```

#### 步骤 7: 配置文件

在 `application.properties` 文件中添加数据库连接配置：

```properties
spring.application.name=demo
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/firstsql
spring.datasource.username=root
spring.datasource.password=123456
```

#### 步骤 8: 启动类

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

#### 步骤 9: 测试

您可以使用 Postman 或类似的工具发送 HTTP GET 请求到 `/api/users`，或者在 Spring Boot 应用程序中编写 JUnit 测试来验证功能是否按预期工作。

通过这种方式，您可以构建一个基于 Spring Boot 的分层架构应用程序。每一层都专注于特定的功能，使得代码更加模块化和易于维护。