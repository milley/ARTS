# IDEA下设置Tomcat的CLASSPATH

IDEA使用的Tomcat Server使用的时本机的tomcat，所以加载CLASSPATH时是加载本地tomcat的CLASSPATH，本地的tomcat的CLASSPATH需要在catalina.bat(windows)设置：

```classpath
rem Add on extra jar file to CLASSPATH
rem Note that there are no quotes as we do not want to introduce random
rem quotes into the CLASSPATH
if "%CLASSPATH%" == "" goto emptyClasspath
set "CLASSPATH=%CLASSPATH%;"
:emptyClasspath
set "CLASSPATH=%CLASSPATH%%CATALINA_HOME%\bin\bootstrap.jar"

rem set user defined properties
set "CLASSPATH=%CLASSPATH%;%CATALINA_HOME%\bin\disconf\download"
```

这样设置后可以在项目中使用：

```xml
<!-- 托管配置文件 会自动reload-->
<bean id="configproperties_disconf" class="com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean">
    <property name="locations">
        <list>
            <value>classpath:dubbo.properties</value>
            <value>classpath:autoconfig.properties</value>
            <value>classpath:operCommon.properties</value>
            <value>classpath:mqConfig.properties</value>
        </list>
    </property>
</bean>
```
