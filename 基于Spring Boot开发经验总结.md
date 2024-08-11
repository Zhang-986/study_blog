## 基于简单的Spring Boot开发经验总结

----

​	首先，我们从浏览器对于一个Url的请求，通过前端发送请求然后经过Web三大组件，通过Filter过滤器进入TomCat服务器中的Servlet容器，由SpringMVC中的DispatcherServlet组件进行捕获，然后通过Spring自带的Interceptor拦截器，最后进入Controller控制器中，由控制器分发到Service服务当中，最后与Dao层与数据库进行交互处理。

![web流程图](C:\Users\lenovo\OneDrive\桌面\web流程图.png)

**对于整个项目，分为配置类，操作类，依赖项类**

---



### 配置类

有`yml`文件和`properties`文件来配置`Spring Boot`中的项目

<u>`yml`文件结构清晰，目录分类明显，易于管理和调控，缩进形式来配置</u>。

<u>`properties`文件配置文件更加直观，`java`对此的解读性较高，数据情况下多的时候不利于管理和维护</u>

#### 数据库信息的配置

将项目与数据库进行连接的基本信息，*这些在idea旗舰版中的数据源配置中都具有*

+ 驱动名字
+ 数据库URL
+ 用户名
+ 密码

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver 
spring.datasource.url=jdbc:mysql://localhost:3306
spring.datasource.username=root
spring.datasource.password=123456
```

#### mybatis信息显示配置

<u>*`mybatis`更加简便地实现了SQL操作*</u>，与传统的JDBC配置信息较比，更加简洁。

* 控制台打印日志的输出
* 获取`#{}`中的变量，将Pojo类中的驼峰属性名字映射成为Sql数据库中的字段名

```properties
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
mybatis.configuration.map-underscore-to-camel-case=true
```

#### 其他非必须第三方配置信息

像阿里云服务器配置信息，文件上传之类的第三方配置信息

```properties
aliyun.oss.endpoint = https://oss-cn-beijing.aliyuncs.com
aliyun.oss.accessKeyId = YourID
aliyun.oss.accessKeySecret = YourKey
aliyun.oss.bucketName = YourBucketName

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=100MB
```

----



### 依赖类

​	这些依赖就像游戏中的mod，你缺少某样必须的东西，就像从Steam创意工坊中进行获取某样资源，同样，得益于Maven项目，我们可以在*`pom`文件中导入依赖项*，随后进行maven项目文件的更新,加载到本地。

#### 基础Web配置类

* web依赖项
* mybatis依赖项
* mysql连接驱动依赖项

#### 工具依赖项

* lombok: 通过注解来简化Pojo类中的set,get方法的实现(**非常推荐**)
* pagehelper: 用来分页
* 第三方依赖项的配置(缺什么找什么，这里就不细说了)

----



### 操作类

​	在main包中的创建一个总包，包含后端交互所需要的各种功能包和一个项目的启动类(因为是SpringBoot项目，一键启动)，再细化到每个包中的每个类文件，实现对于整个后端请求的处理，所有操作类通用注释**@Slf4j**负责日志的打印输出，便于对操作的运行状态进行检查。

​	

#### Controller控制层

​	@RestController**=**@Controller**+**@ReponseBody在每次响应的数据通过自动序列化转化为JSON格式。

​	@RequestMapping()请求的映射地方，括号内指定映射的URL值，然后转到指定位置的Controller方法。

​	在请求URL中，头部的映射可以作为目录，下边的各种Mapping可以作为子目录进行操作

##### 通常的请求方式

* @PostMapping:  **创建新的资源**
* @GetMapping: **检索资源**
* @PutMapping: **更新现有的资源**
* @deleteMapping: **删除资源**

##### 通常的参数获取方式

* @RequestBody 将 JSON 数据转换为对应的 Java 对象。

```java
@PutMapping
public Result add(@RequestBody Dept dept ){
        log.info("新增部门");
        deptService.add(dept);
        return Result.success();
    }
```

* @PathVariable 声明路径参数，假如Url：`http://localhost:8080/emps/3`,3可能作为路径参数，传入到Controller方法当中进行相关信息的操作。

```java
@DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("删除"+id+"数据");
        deptService.delete(id);
        return Result.success();
    }

```

* @RequestParam 进行参数绑定`@RequestParam(name = "username") String name`可以将传进来的username转化为name关键词，使用 `defaultValue` 和 `required` 属性来指定参数的默认值和是否必需

```java
@GetMapping()
public Result complex(@RequestParam(defaultValue = "1") Integer page,
                      @RequestParam (defaultValue = "10") Integer pageSize,
                      String name,
                      Short gender,
                      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
    PageBean pageBean =empService.page(page,pageSize,name,gender,begin,end);
    return Result.success(pageBean);
}
```

* @DateTimeFormat<u>参数用来接受日期类的参数设置，固定日期的参数</u>

​	最后返回的值以自定义的Result类来返回数据，这个Result类中封装了一些数据，有状态码和返回信息，以JSON格式进行返回。



----



#### Service服务层

​	`Service`有接口层和实现层，创建出接口类，然后创建impl包中实现接口的类，这些接口类中要被`Controlller`层来调用，使用接口更加灵活，<u>*一个同样的接口，两个不同实现功能的类，利用接口调用会更加灵活，更方便地实现更多的业务*</u>。

**PageHelper分页器**

​	在依赖中导入过后，获得PageHelper类，常见用法就是调用PageHelper.startPage(页数，每页展示信息的条数)

<u>*无论是什么查询，只要是查询过后的结果都会通过分页器加工，然后得到数据成页出现*</u>，起到的作用只不过是加工，最后展示出来。

```java
@Override
public PageBean page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end) {
    PageHelper.startPage(page,pageSize);

    List<Emp> list = empMapper.list(name,gender,begin,end);

    Page<Emp> p= (Page<Emp>) list;

    PageBean pageBean = new PageBean(p.getTotal(), p.getResult());
    return pageBean;
}
```

​	

服务层具体实现思路

1. 接口层提供统一的方法
2. 在impl包中实现接口的方法，可以拓展多个，更加方便灵活。
3. 在具体类中调用DAO层的mapper方法，做到与数据库进行交互。



---



####Mapper DAO层

​	Mapper DAO层（Data Access Object）是用于与数据库进行交互的一层。<u>*它负责数据的持久化操作，如增删改查（CRUD）等*</u>。通常，Mapper DAO层会使用一些ORM（对象关系映射）框架，如MyBatis、Hibernate等，来简化数据库操作。

DAO层具体实现思路

​	![image-20240811211208229](../../AppData/Roaming/Typora/typora-user-images/image-20240811211208229.png)



#### POJO 包

​	POJO（Plain Old Java Object）是指那些没有继承或实现任何特殊接口或类的普通Java对象。它们通常用于表示业务逻辑中的实体，如用户、订单等。POJO的特点是简单、轻量，易于理解和维护。

​	这里装载着各种对象的封装属性，我们要添加注解，使其成为一个标准的JavaBean类,使其具备无参构造和所有的有参构造方法

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dept {
    private Integer id; //ID
    private String name; //部门名称
    private LocalDateTime createTime; //创建时间
    private LocalDateTime updateTime; //修改时间
}

```

**特殊的Result类**： 用于返回给前端的状态码和信息

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String msg;  //响应信息 描述字符串
    private Object data; //返回的数据

    //增删改 成功响应
    public static Result success(){
        return new Result(1,"success",null);
    }
    //查询 成功响应
    public static Result success(Object data){
        return new Result(1,"success",data);
    }
    //失败响应
    public static Result error(String msg){
        return new Result(0,msg,null);
    }
}
```

---



#### Utils 包

##### 阿里云OSS工具类

* 这里装载着项目中所需要的工具,比如说前边用到的阿里云OSS上,JWT令牌的生成。

```java
// 对于阿里云OSS方法的调用在Controller中
@Autowired
private AliOSSUtils aliOSSUtils;
@PostMapping ("/upload")
public Result upload(MultipartFile image ) throws IOException {
    log.info("文件上传 :{}",image.getOriginalFilename());
    String upload = aliOSSUtils.upload(image);
    return Result.success(upload);
}
```

* 配置类中的`@ConfigurationProperties(prefix="aliyun.oss")`专门提供一个配置类,能够衔接私有属性中的属性名字，对于配置类较多且前缀名字高度一致的时候，更适合使用这个配置类，然后在工具类中进行调用配置类。

```java
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliOSSProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
```



##### JWT令牌

![image-20240811214242671](../../AppData/Roaming/Typora/typora-user-images/image-20240811214242671.png)



```java
  public static String generateJwt(Map<String, Object> claims){
	//         创建令牌
        String jwt = Jwts.builder()
                .addClaims(claims)   // claims 自定义内容
                .signWith(SignatureAlgorithm.HS256, signKey)     //  前边是签名算法,singKey令牌类型
                .setExpiration(new Date(System.currentTimeMillis()+ expire))  // 有效时间
                .compact();     // 去除无效字符
        return jwt;
    }
```

* JWT令牌的解析

```java
public static Claims parseJWT(String jwt){
    Claims claims = Jwts.parser()
            .setSigningKey(signKey)
            .parseClaimsJws(jwt)
            .getBody();
    return claims;
}
```



----

####Interceptor 包

​	过滤器封装性不够高，我这个直接跳过讲Spring全家桶中的interceptor拦截器了，唯一麻烦的需要在Config包中进行配置。

##### `Configuration`类

提前声明配置类，确保Spring扫描到。

* 调用`WebMvcConfigurer`接口实现`addInterceptors`方法，

```java
@Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
                        registry
                        .addInterceptor(loginCheckInterceptor)    // 注册拦截器类
                        .addPathPatterns("/**")				// 对所有路径进行添加拦截
                        .excludePathPatterns("/login");	//设置 不包含拦截的路径,比如说像登入之类的
    }
```



​	

分为三个方法(一般只使用第一种)

**preHandle**

```java
boolean preHandle(HttpServletRequest request,
                  HttpServletResponse response, 
                  Object handler)throws Exception;
```

- 如果返回 `true`，则控制器方法将会继续执行；如果返回 `false`，则控制器方法不会执行，并且后续的拦截器也不会被调用。我们可以使用调用它来实现不同会话的拦截请求，比如说JWT令牌的验证。

  

1. `preHandle` 用于在控制器方法执行前进行检查和准备。

2. `postHandle` 用于在控制器方法执行后，视图渲染前进行操作。

```java
void postHandle(HttpServletRequest request,
                HttpServletResponse response,
                Object handler, ModelAndView modelAndView) throws Exception;
```



3. `afterCompletion` 用于在整个请求结束后进行清理工作。

```java
void afterCompletion(HttpServletRequest request,
                     HttpServletResponse response,
                     Object handler, Exception ex) throws Exception;
```



---

#### Expectation 处理

**@RestControllerAdvice** 使用该关键词对此类进行一个声明，确保它就是一个异常处理站（截获所有异常）。

**@ExceptionHandler** (Exception.class) 声明该处理的异常类型，Exception是所有的父类，所以能够处理所有的异常。

在这个异常处理类中，我们可以返回一个JSON格式的数据，能够展示给前端，还是调用Result类。

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result ex(Exception ex){
        ex.printStackTrace();
        return Result.error("联系管理员去");
    }
}
```



----

#### Spring事务处理

**前提**

​	如果我们在银行进行转账，那么A-1000，另一个B+1000。

1. 非事务方式提交:  出现异常情况下，A-1000，B+0，显而易见，这种方法不可取。
2. Spring注解@Transactional方式，A-1000,B+1000,如果异常，事务回滚，操作没有完成。**要么同时完成要么同时失败，这就是事务的原子性**

  在Spring中提供事务的优先级，可以设置注解指明事务的优先级别。

```java
@Transactional (propagation = Propagation.REQUIRED)
```

如果说一个事务出现问题，在回滚过程，如果说我们要将这个记录记录下来，那么我们就可以挂起要回滚的事务，添加另一个新的事务，叫做事务挂起，创建出一个新的事务用来记录当前记录。

```
@Transactional (propagation = Propagation.REQUIRES_NEW)
```
