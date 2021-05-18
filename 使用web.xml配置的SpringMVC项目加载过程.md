使用web.xml进行配置的Spring MVC项目示例，[项目地址](https://github.com/Sygnux/springframework-tutorials/tree/main/springmvc-with-web-tutorial)

##### web.xml文件的配置

```xml
<web-app>
    <display-name>Archetype Created Web Application</display-name>
    <!-- ContextLoaderListener加载的是配置是作为父容器的配置信息，一个MVC项目可以不配置父容器 -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/root-context.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <!-- 子容器 -->
    <servlet>
        <servlet-name>app</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/app-context.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>app</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```


1. listener-clsss的加载(父容器的加载)

   如果有配置listener-class，则Servlet容器(如Tomcat)启动时会先加载并实例化listener-class所配置的类；

   示例中所配置的ContextLoaderListener是spring-web中对于servlet规范中ServletContextListener的一个实现，其继承了ServletContextListener；所以在servlet容器启动时ContextLoaderListener的contextInitialized(ServletContextEvent event)方法就会被调用，从而创建父容器
   **contextInitialized(ServletContextEvent event):**

   ```java
   @Override
   public void contextInitialized(ServletContextEvent event) {
       initWebApplicationContext(event.getServletContext());
   }
   ```
   **initWebApplicationContext(ServletContext servletContext):**
   
   ```java
   public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
       // 判断父容器是否已经存在
       if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
         throw new IllegalStateException(
               "Cannot initialize context because there is already a root application context present - " +
               "check whether you have multiple ContextLoader* definitions in your web.xml!");
      }
   
      servletContext.log("Initializing Spring root WebApplicationContext");
      Log logger = LogFactory.getLog(ContextLoader.class);
      if (logger.isInfoEnabled()) {
         logger.info("Root WebApplicationContext: initialization started");
      }
      long startTime = System.currentTimeMillis();
   
      try {
         if (this.context == null) {
             // 创建父容器
            this.context = createWebApplicationContext(servletContext);
         }
         if (this.context instanceof ConfigurableWebApplicationContext) {
            ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) this.context;
            if (!cwac.isActive()) {
               if (cwac.getParent() == null) {
                  ApplicationContext parent = loadParentContext(servletContext);
                  cwac.setParent(parent);
               }
               configureAndRefreshWebApplicationContext(cwac, servletContext);
            }
         }
          // 将父容器保存到servlet上下文中
         servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
         ClassLoader ccl = Thread.currentThread().getContextClassLoader();
         if (ccl == ContextLoader.class.getClassLoader()) {
            currentContext = this.context;
         }
         else if (ccl != null) {
            currentContextPerThread.put(ccl, this.context);
         }
         if (logger.isInfoEnabled()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("Root WebApplicationContext initialized in " + elapsedTime + " ms");
         }
         return this.context;
      }
      catch (RuntimeException | Error ex) {
         logger.error("Context initialization failed", ex);
         servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex);
         throw ex;
      }
   }
   ```

2. DispatcherServlet的加载(子容器的加载)

   DispatcherServlet也是servlet规范中GenericServlet抽象类的一个子类，所以在servlet容器启动时会在实例化DispatcherServlet后调用其init()方法；init()方法的实现逻辑是在DispatcherServlet的父中HttpServletBean中实现的

   ```java
   public final void init() throws ServletException {
   
      // Set bean properties from init parameters.
      PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
      if (!pvs.isEmpty()) {
         try {
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
            ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
            bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, getEnvironment()));
            initBeanWrapper(bw);
            bw.setPropertyValues(pvs, true);
         }
         catch (BeansException ex) {
            if (logger.isErrorEnabled()) {
               logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
            }
            throw ex;
         }
      }
   
      // 调用子类FrameworkServlet实现的initServletBean()方法
      initServletBean();
   }
   ```

   **FrameworkServlet#initServletBean():**  

   ```java
   protected final void initServletBean() throws ServletException {
      getServletContext().log("Initializing Spring " + getClass().getSimpleName() + " '" + getServletName() + "'");
      if (logger.isInfoEnabled()) {
         logger.info("Initializing Servlet '" + getServletName() + "'");
      }
      long startTime = System.currentTimeMillis();
      try {
          // 创建子容器
         this.webApplicationContext = initWebApplicationContext();
         initFrameworkServlet();
      }
      catch (ServletException | RuntimeException ex) {
         logger.error("Context initialization failed", ex);
         throw ex;
      }
      if (logger.isDebugEnabled()) {
         String value = this.enableLoggingRequestDetails ?
               "shown which may lead to unsafe logging of potentially sensitive data" :
               "masked to prevent unsafe logging of potentially sensitive data";
         logger.debug("enableLoggingRequestDetails='" + this.enableLoggingRequestDetails +
               "': request parameters and headers will be " + value);
      }
      if (logger.isInfoEnabled()) {
         logger.info("Completed initialization in " + (System.currentTimeMillis() - startTime) + " ms");
      }
   }
   ```

   **initWebApplicationContext()--> ..... -->WebApplicationContext createWebApplicationContext(@Nullable ApplicationContext parent):**

   ```java
   protected WebApplicationContext createWebApplicationContext(@Nullable ApplicationContext parent) {
      Class<?> contextClass = getContextClass();
      if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
         throw new ApplicationContextException(
               "Fatal initialization error in servlet with name '" + getServletName() +
               "': custom WebApplicationContext class [" + contextClass.getName() +
               "] is not of type ConfigurableWebApplicationContext");
      }
      ConfigurableWebApplicationContext wac =
            (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
   
      wac.setEnvironment(getEnvironment());
      // 将父容器设置为当前容器的parent
      wac.setParent(parent);
      String configLocation = getContextConfigLocation();
      if (configLocation != null) {
         wac.setConfigLocation(configLocation);
      }
      configureAndRefreshWebApplicationContext(wac);
   
      return wac;
   }
   ```

   

   