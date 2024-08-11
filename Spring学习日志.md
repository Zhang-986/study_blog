### 1. IoC (Inversion of Control)

​	IoC是一种设计原则，它的核心思想是将对象创建和依赖关系管理的责任从对象本身移除到外部容器中。这样做的好处是减少对象间的耦合度，提高系统的可维护性和可测试性。

​	在Spring框架中，IoC容器负责管理bean的生命周期和依赖关系。Spring IoC容器的主要组成部分包括：

- **BeanFactory**：这是最基础的IoC容器，提供了配置和管理bean的基本功能。
- **ApplicationContext**：这是一个高级的IoC容器，除了BeanFactory的功能之外，还提供了国际化支持、事件传播、资源访问等功能。

### 2. DI (Dependency Injection)

​	DI是IoC的一种实现方式，它允许对象的依赖项在其创建过程中被注入，而不是由对象自己去创建或查找依赖。在Spring框架中，可以通过以下几种方式实现依赖注入：

- **构造器注入**：通过构造函数参数传递依赖。
- **字段注入**：直接在类的成员变量上使用注解来注入依赖。
- **setter方法注入**：通过setter方法传递依赖。

### 3. Bean注解形式

​	Spring支持通过注解的方式配置bean，这种方式比传统的XML配置更加简洁。以下是一些常用的注解：

- **@Component**：将类标记为一个bean，可以被Spring IoC容器管理。
- **@Repository**：专门用于数据访问层的组件。
- **@Service**：用于业务逻辑层的组件。
- **@Controller**：用于Web层的控制器组件。
- **@Autowired**：用于自动装配bean的依赖项。
- **@Qualifier**：当存在多个相同类型的bean时，用于指定具体的bean。
- **@Primary**：当存在多个相同类型的bean时，优先考虑作为默认选择。
- **@Bean**：用于在配置类中定义bean，通常用于管理第三方库中的bean。

#### 示例

​	这里有一个简单的例子来展示如何使用注解配置bean：

​	假设我们有两个类：`MessageService` 和 `GreetingService`。

```java
@Component("messageService")
public class MessageService {
    public String getMessage() {
        return "Hello, World!";
    }
}

@Component("greetingService")
public class GreetingService {
    private final MessageService messageService;

    @Autowired
    public GreetingService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void greet() {
        System.out.println(messageService.getMessage());
    }
}
```

​	在这个例子中，`MessageService` 和 `GreetingService` 都被声明为Spring管理的bean。`GreetingService` 使用构造器注入来获取对`MessageService`的依赖。

​	为了启动Spring容器并使用这些bean，你需要配置一个配置类，并使用`@Configuration`和`@EnableAutoConfiguration`（如果是在Spring Boot应用中）或者`@ComponentScan`来扫描bean。

```java
@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig {
    // 如果需要，你可以在这里定义额外的bean
}
```

以上就是IoC、DI和bean注解形式的基本概述。如果你还有其他具体问题或需要更深入的解释，请随时告诉我！

---

### 4.Spring AOP

​	4.Spring AOP（Aspect Oriented Programming，面向切面编程）是Spring框架中的一个重要特性，用于实现横切关注点（cross-cutting concerns）的模块化。在面向对象编程中，应用程序的逻辑通常被组织成多个对象之间的交互。然而，有些功能（如日志记录、性能监控、安全控制、事务管理等）会跨越多个对象，这些功能被称为横切关注点。

下面是一些关于Spring AOP的关键概念：

1. **Aspect（切面）**：
   - 切面是横切关注点的模块化实现。
   - 在Spring AOP中，切面可以使用带有`@Aspect`注解的Java类来表示。
   - 切面可以包含多个通知（advice）。
2. **Join Point（连接点）**：
   - 应用程序执行过程中的某个特定点，比如方法执行或异常抛出。
   - 在Spring AOP中，连接点通常是方法执行。
3. **Pointcut（切点）**：
   - 定义何处应用切面的规则。
   - 切点指定了哪些连接点将被切面所关心。
   - 可以使用表达式语言来指定切点。
4. **Advice（通知/增强）**：
   - 在切点指定的连接点上执行的代码。
   - 有几种不同类型的通知，包括：
     - `Before`：在连接点之前执行。
     - `After`：无论方法是否成功完成都会执行。
     - `After Returning`：在方法正常返回之后执行。
     - `After Throwing`：在方法抛出异常之后执行。
     - `Around`：环绕通知，在连接点之前和之后都执行。
5. **Weaving（织入）**：
   - 将切面与程序的其余部分连接的过程。
   - 在Spring AOP中，织入通常发生在运行时，通过动态代理来实现。
6. **Proxy（代理）**：
   - Spring AOP使用代理模式来实现通知的插入。
   - 如果目标对象实现了接口，Spring将使用JDK动态代理；如果没有实现接口，则使用CGLIB代理。
7. **Introduction（引入）**：
   - 允许在不修改类的情况下向类添加新接口的实现。
8. **Target Object（目标对象）**：
   - 被一个或多个切面所通知的对象。
9. **AOP Proxy（AOP代理）**：
   - 由Spring AOP创建的对象，用来拦截方法调用并应用切面。

Spring AOP的实现主要是基于动态代理技术，这意味着它不会像AspectJ那样在编译阶段修改字节码，而是通过在运行时创建代理对象来实现增强。Spring AOP虽然不如AspectJ强大和灵活，但它与Spring IoC容器紧密集成，使得开发人员可以很容易地使用它来实现诸如事务管理、日志记录等功能。



#### 1. 基础概念

- **切面 (Aspect)**: 代表了一组关注点的模块化，它封装了横切关注点的实现逻辑。
- **连接点 (Join Point)**: 程序执行过程中的某个特定位置，比如方法调用或异常抛出。
- **通知 (Advice)**: 在切面的特定连接点执行的代码，如前置通知、后置通知、环绕通知等。
- **切入点 (Pointcut)**: 匹配连接点的表达式，用于指定通知应用的位置。
- **引入 (Introduction)**: 向现有类添加新方法或属性的能力。
- **织入 (Weaving)**: 将切面加入到程序中，可以发生在编译时、加载时或运行时。

#### 2. 通知类型

- **前置通知 (@Before)**: 在方法调用前执行。
- **后置通知 (@After)**: 无论方法是否成功执行，都会在方法调用后执行。
- **返回后通知 (@AfterReturning)**: 在方法成功返回后执行。
- **异常通知 (@AfterThrowing)**: 在方法抛出异常后执行。
- **环绕通知 (@Around)**: 在方法调用前后执行，可以控制方法调用的流程。

####3. 切入点表达式

- **`execution`**: 匹配方法执行的连接点。
- **`within`**: 匹配指定类型的所有连接点。
- **`this`**: 匹配代理对象所属类型的连接点。
- **`target`**: 匹配目标对象所属类型的连接点。
- **`args`**: 匹配具有特定参数的连接点。
- **`@target`**: 匹配在目标对象上有特定注解的连接点。
- **`@args`**: 匹配具有特定注解参数的连接点。
- **`@within`**: 匹配在指定类型的类上有特定注解的连接点。![image-20240731222353879](../../AppData/Roaming/Typora/typora-user-images/image-20240731222353879.png)

要实现两个人员进行银行转账的功能，并且使用 Spring AOP 来记录日志，我们可以按照以下步骤进行：

1. **定义转账接口** (`TransferService`): 定义一个接口，用于描述转账操作。
2. **实现转账接口** (`BankService`): 实现转账接口，提供转账功能。
3. **定义转账事务切面** (`TransactionAspect`): 使用 AOP 来记录转账前后的日志。
4. **配置 Spring 容器** (`AppConfig`): 配置 Spring 容器，扫描组件，并启用 AOP 支持。
5. **主程序** (`App`): 创建 Spring 容器，并使用转账服务进行转账操作。

### 步骤 1: 定义转账接口 (`TransferService`)

```java
package Aop.transaction;

public interface TransferService {
    void transfer(account from, account to, double amount);
}
```

### 步骤 2: 实现转账接口 (`BankService`)

```java
package Aop.transaction;

public class BankService implements TransferService {
    @Override
    public void transfer(account from, account to, double amount) {
        from.outMoney(amount);
        to.inMoney(amount);
    }
}
```

### 步骤 3: 定义转账事务切面 (`TransactionAspect`)

```java
package Aop.transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TransactionAspect {
    @Around("execution(* Aop.transaction.BankService.transfer(..))")
    public void logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Starting transaction...");
        joinPoint.proceed();
        System.out.println("Transaction completed.");
    }
}
```

### 步骤 4: 配置 Spring 容器 (`AppConfig`)

```java
package Aop.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("Aop.transaction")
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public account tomAccount() {
        return new account("tom", 1000.0);
    }

    @Bean
    public account mikeAccount() {
        return new account("mike", 1000.0);
    }

    @Bean
    public TransferService bankService() {
        return new BankService();
    }
}
```

### 步骤 5: 主程序 (`App`)

```java
package Aop.transaction;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        account tom = context.getBean("tomAccount", account.class);
        account mike = context.getBean("mikeAccount", account.class);
        TransferService bankService = context.getBean(TransferService.class);

        bankService.transfer(tom, mike, 500.0);

        System.out.println("Tom's balance: " + tom.getMoney());
        System.out.println("Mike's balance: " + mike.getMoney());

        context.close();
    }
}
```

### 代码解析

1. **转账接口 (`TransferService`)**:
   - 定义了一个 `transfer` 方法，用于从一个账户转账到另一个账户。

2. **转账服务 (`BankService`)**:
   - 实现了 `transfer` 方法，从一个账户转账到另一个账户。

3. **转账事务切面 (`TransactionAspect`)**:
   - 使用 `@Around` 注解定义了一个环绕通知，它会在转账操作前后打印日志。

4. **配置 (`AppConfig`)**:
   - 定义了 `tomAccount` 和 `mikeAccount` 的 `@Bean` 方法。
   - 定义了 `bankService` 的 `@Bean` 方法。
   - 启用了 AOP 支持。

5. **主程序 (`App`)**:
   - 创建了一个 Spring 容器，并从中获取了 `tomAccount`、`mikeAccount` 和 `bankService`。
   - 调用 `bankService.transfer(tom, mike, 500.0)` 来进行转账操作。
   - 打印转账后的账户余额。

### 运行结果

当你运行 `App.java` 时，控制台输出将类似于：

```
Starting transaction...
Transaction completed.
Tom's balance: 500.0
Mike's balance: 1500.0
```

### 总结

*<u>Tomcat 是 Servlet 容器，负责托管和运行 Servlet；Servlets 是一种服务器端技术，用于处理 HTTP 请求；而 Spring MVC 是基于 Servlet 规范的 MVC 框架，它利用 Servlets 来实现 MVC 架构，通常在 Tomcat 这样的 Servlet 容器中运行。</u>*