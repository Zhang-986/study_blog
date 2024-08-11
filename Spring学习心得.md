### `Spring`学习心得

### 接口的再巩固

#### 定义接口 `Service`

```java
1 public interface Service {
2    void performAction();
3 }
```

#### 实现接口 `BookService`

```java
1 public class BookService implements Service {
2    @Override
3    public void performAction() {
4        System.out.println("Performing action for BookService.");
5    }
6 }
```

#### 实现接口 `UserService`

```java
1 public class UserService implements Service {
2    @Override
3    public void performAction() {
4        System.out.println("Performing action for UserService.");
5    }
6 }


App:
 	   Service service1 = (Service) context.getBean("bookService");
        service1.performAction();  // 输出 "Performing action for BookService."

        Service service2 = (Service) context.getBean("userService");
        service2.performAction();  // 输出 "Performing action for UserService."
```

​	对于接口的了解，因为接口本身具有多态性，<u>*接口可以被用作引用类型，这意味着你可以声明一个接口类型的变量*</u>。



###  @Bean 注解的使用

1、使用说明
	@Bean 注解作用在方法上，产生一个 Bean 对象，然后这个 Bean 对象交给 Spring 管理，剩下的你就不用管了。产生这个 Bean 对象的方法 Spring 只会调用一次，随后这个 Spring 将会将这个 Bean 对象放在自己的 IOC 容器中。
	@Bean 方法名与返回类名一致，首字母小写。
	@Component、@Repository、@Controller、@Service 这些注解只局限于自己编写的类，而 @Bean 注解能把第三方库中的类实例加入 IOC 容器中并交给 Spring 管理。
	@Bean 一般和 @Component 或者 @Configuration 一起使用.

------------------------------------------------



原文链接：https://blog.csdn.net/yuxiangdeming/article/details/122876550

### `Bean`的生命周期

​	<u>*我们一般采用set方法来实现对于对象的引用和基本数据类型的创建*</u>

* 通过`spring xml`文件中的bean方法中使用标签的`property`来进行配置，最后经常使用Set方法进行注入。

```java
<bean id="service" class="t1.bookService">
    <property name="Jok" value="13"/>   // 这里是值的输入，然后还可以对象的引用
    <property name="anInt" value="13"/>
</bean>
    
public void setAnInt(int anInt) {
        this.anInt = anInt;
}
```





​	![image-20240728071659305](../../AppData/Roaming/Typora/typora-user-images/image-20240728071659305.png)

### 	实例化`Bean`类的方法(`new` 对象的过程)

1. 最常见的方法，通过在spring提供的xml文件中，创建出对应的格式，然后调用`getBean`方法（底层逻辑使用了JAVA的反射方式，通过对于构造器的调用来实现对于实例对象的创建）

```java
 <bean id="save" class="t1.dao"/>
 
App:
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("springContextFile.xml");
service s1 =(service) applicationContext.getBean("save");
s1.service();
```

2. 后期接触`Spring`常用的工厂模式来实现对于对象的创建

* 通过工厂类应用接口`FactoryBean`类的接口（标明泛型的变量可以是接口，具有更好的拓展性）
* `getObject()`方法获得一个对象，`getobjectType()`来获取目标类的`class`文件
* 还有`isSingleton()`来获取当前是不是唯一的实例，也就是单例。
  1. 单例重复使用一个地址值，可以复用，降低了内存的开销。
  2. 多例的话就是包含数据的那种，用完就丢。

```java
public class bookServiceFactory implements FactoryBean<bookService> {
    @Override
    public bookService getObject() throws Exception {
        System.out.println("moving bookServiceFactory");
        return new bookService();
    }

    @Override
    public Class<?> getObjectType() {
        return bookService.class;
    }
}

App:
bookServiceFactory bookServiceFactory = new bookServiceFactory();
service object =(service) bookServiceFactory.getObject();
object.service();   // 使用接口来接受返回的实例，方便下次调用应用接口的子类方法。
```

### IoC管理Bean实现

​	IoC对于bean的对象进行管理

1. 导入`spring`坐标
2. 定义`Spring`管理的接口或者类
3. 创建`Spring`中的`bean`
4. 初始化IoC容器
5. 调用`getBean`方法来实现

### DI实现关系的绑定和注入依赖

1. 提供Set方法对创建对象，但没有分配指针的对象。
2. 在`Spring`的xml文件中配置两者依赖的关系，





当然可以！下面是一些练习题，帮助您巩固Spring框架中的注解、自动装配和bean注入的概念：

### 练习题 1: 使用`@Configuration`和`@Bean`定义Bean

**任务**:
1. 创建一个`Configuration`类，定义一个名为`emailService`的bean。
2. 创建一个`EmailService`类，它包含一个`sendEmail`方法。
3. 在`Configuration`类中定义`EmailService`的bean。

**代码示例**:
```java
// EmailService.java
public class EmailService {
    public void sendEmail(String recipient, String message) {
        System.out.println("Sending email to " + recipient + ": " + message);
    }
}

// AppConfig.java
@Configuration
public class AppConfig {

    @Bean
    public EmailService emailService() {
        return new EmailService();
    }
}
```

### 练习题 2: 使用`@Autowired`进行自动装配

**任务**:
1. 创建一个`NotificationService`类，它包含一个`EmailService`类型的成员变量。
2. 使用`@Autowired`注解来自动装配`EmailService`。

**代码示例**:
```java
// NotificationService.java
@Service
public class NotificationService {

    private final EmailService emailService;

    @Autowired
    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendNotification(String recipient, String message) {
        emailService.sendEmail(recipient, message);
    }
}
```

### 练习题 3: 使用`@Qualifier`进行特定Bean的装配

**任务**:
1. 创建两个`EmailService`的实现类：`SimpleEmailService`和`SecureEmailService`。
2. 在`AppConfig`中定义两个`EmailService`的bean。
3. 创建一个`NotificationService`类，它应该能够注入特定的`EmailService`实现。

**代码示例**:
```java
// SimpleEmailService.java
public class SimpleEmailService implements EmailService {
    @Override
    public void sendEmail(String recipient, String message) {
        System.out.println("Sending simple email to " + recipient + ": " + message);
    }
}

// SecureEmailService.java
public class SecureEmailService implements EmailService {
    @Override
    public void sendEmail(String recipient, String message) {
        System.out.println("Sending secure email to " + recipient + ": " + message);
    }
}

// AppConfig.java
@Configuration
public class AppConfig {

    @Bean(name = "simpleEmailService")
    public EmailService simpleEmailService() {
        return new SimpleEmailService();
    }

    @Bean(name = "secureEmailService")
    public EmailService secureEmailService() {
        return new SecureEmailService();
    }
}

// NotificationService.java
@Service
public class NotificationService {

    private final EmailService emailService;

    @Autowired
    public NotificationService(@Qualifier("secureEmailService") EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendNotification(String recipient, String message) {
        emailService.sendEmail(recipient, message);
    }
}
```

### 练习题 4: 使用`@Component`和`@ComponentScan`

**任务**:
1. 创建一个`UserService`类，并使用`@Component`注解将其标记为组件。
2. 在`AppConfig`中使用`@ComponentScan`来扫描包含`UserService`的包。
3. 创建一个`UserController`类，注入`UserService`。

**代码示例**:
```java
// UserService.java
@Component
public class UserService {
    public String getUserInfo(String userId) {
        return "User info for " + userId;
    }
}

// UserController.java
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public String getUserInfo(@PathVariable String userId) {
        return userService.getUserInfo(userId);
    }
}

// AppConfig.java
@Configuration
@ComponentScan("com.example")
public class AppConfig {
    // ...
}
```

### 练习题 5: 使用`@Bean`定义带参数的Bean

**任务**:
1. 创建一个`LoggerService`类，它接受一个字符串参数`logLevel`。
2. 在`AppConfig`中定义一个`LoggerService`的bean，并为其传递一个`logLevel`参数。

**代码示例**:
```java
// LoggerService.java
public class LoggerService {
    private final String logLevel;

    public LoggerService(String logLevel) {
        this.logLevel = logLevel;
    }

    public void logMessage(String message) {
        System.out.println(logLevel + ": " + message);
    }
}

// AppConfig.java
@Configuration
public class AppConfig {

    @Bean
    public LoggerService loggerService() {
        return new LoggerService("INFO");
    }
}
```

### 练习题 6: 使用`@Profile`进行环境配置

**任务**:
1. 创建两个`EmailService`实现：`DevelopmentEmailService`和`ProductionEmailService`。
2. 使用`@Profile`注解来区分开发环境和生产环境。
3. 在`AppConfig`中定义两个`EmailService`的bean，并使用`@Profile`注解。

**代码示例**:
```java
// DevelopmentEmailService.java
@Profile("dev")
public class DevelopmentEmailService implements EmailService {
    @Override
    public void sendEmail(String recipient, String message) {
        System.out.println("Sending development email to " + recipient + ": " + message);
    }
}

// ProductionEmailService.java
@Profile("prod")
public class ProductionEmailService implements EmailService {
    @Override
    public void sendEmail(String recipient, String message) {
        System.out.println("Sending production email to " + recipient + ": " + message);
    }
}

// AppConfig.java
@Configuration
public class AppConfig {

    @Bean
    @Profile("dev")
    public EmailService emailServiceDev() {
        return new DevelopmentEmailService();
    }

    @Bean
    @Profile("prod")
    public EmailService emailServiceProd() {
        return new ProductionEmailService();
    }
}
```

完成这些练习题后，您将能够更好地理解Spring框架中的注解、自动装配和bean注入的使用。如果您需要更详细的解释或有其他疑问，请随时提问！

### `Spring(Ioc DI AOP)->SpringMVC->数据库的复习->Mybatis->SpringBoot`

​	Spring IoC容器和DI机制的基本理解。你能够创建配置类、定义组件、使用依赖注入等基本功能。对于初学者来说，这是一个不错的起点。