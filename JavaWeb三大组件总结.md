​	Java Web 开发中，Servlet、Filter 和 Listener 是三个非常重要的组件。它们在 Java Web 应用程序中扮演着不同的角色，并且相互协作来处理 HTTP 请求和响应。

### 1. Servlet
Servlet 是 Java Web 应用的基础，它是一个 Java 类，用于扩展服务器的功能。Servlet 被容器（如 Tomcat 或 Jetty）管理，并且可以接收和响应客户端的请求。

- **生命周期**：
  - 初始化：当容器加载 Servlet 时，会调用 `init()` 方法。
  - 服务：容器根据客户端的请求调用 `service()` 方法。通常，我们会重写 `doGet()` 和 `doPost()` 方法来处理 GET 和 POST 请求。
  - 销毁：当容器关闭或卸载 Servlet 时，会调用 `destroy()` 方法释放资源。

- **示例**：
  ```java
  public class MyServlet extends HttpServlet {
      @Override
      public void init() throws ServletException {
          // 初始化代码
      }
  
      @Override
      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          // 处理 GET 请求
      }
  
      @Override
      protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          // 处理 POST 请求
      }
  
      @Override
      public void destroy() {
          // 清理资源
      }
  }
  ```

### 2. Filter
Filter 用于拦截进入 Servlet 的请求和离开 Servlet 的响应。它可以对请求和响应进行预处理或后处理。

- **生命周期**：
  - 初始化：容器加载 Filter 时调用 `init()` 方法。
  - 过滤：通过 `doFilter()` 方法实现过滤逻辑。
  - 销毁：容器卸载 Filter 时调用 `destroy()` 方法。

- **示例**：
  ```java
  public class MyFilter implements Filter {
      @Override
      public void init(FilterConfig filterConfig) throws ServletException {
          // 初始化代码
      }
  
      @Override
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
          HttpServletRequest req = (HttpServletRequest) request;
          HttpServletResponse res = (HttpServletResponse) response;
  
          // 在请求到达目标资源前的操作
          chain.doFilter(request, response); // 让请求继续传递到下一个过滤器或目标资源
          // 在响应离开目标资源后的操作
      }
  
      @Override
      public void destroy() {
          // 清理资源
      }
  }
  ```

### 3. Listener
Listener 监听特定类型的事件，例如应用程序上下文的变化或者用户会话的创建/销毁等。当这些事件发生时，容器会自动调用 Listener 中的方法。

- **类型**：
  - `ServletContextListener`：监听上下文的启动和关闭。
  - `HttpSessionListener`：监听会话的创建和销毁。
  - `ServletRequestListener`：监听请求的开始和结束。

- **示例**：
  ```java
  public class MyContextListener implements ServletContextListener {
  
      @Override
      public void contextInitialized(ServletContextEvent sce) {
          // 当上下文初始化时执行
      }
  
      @Override
      public void contextDestroyed(ServletContextEvent sce) {
          // 当上下文被销毁时执行
      }
  }
  ```

### 总结
- **Servlet** 处理具体的业务逻辑。
- **Filter** 用于对请求和响应进行预处理或后处理。
- **Listener** 监听特定的事件，并在事件发生时执行相应的代码。

这三种组件共同构成了 Java Web 应用的基础架构，使得开发者能够构建复杂且功能丰富的 Web 应用程序。