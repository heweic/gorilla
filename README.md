# gorilla

轻量级、方便使用的RPC框架

### 示例

```java
//接口
public interface Inter1 {
	public String helloRpc(String arg);
}
```

```java
//实现
public class Impl1 implements Inter1{
	@Override
	public String helloRpc(String arg) {
		return arg;
	}
}
```

```xml
<!--  服务端配置 -->

	<!-- 注册中心 -->
	<gorilla:registry ip="127.0.0.1" port="2181" />
	<!--服务端口 -->
	<gorilla:servicePort value="3936" />
	
	<bean id="impl1" class="org.myframe.gorilla.test.impl.Impl1"></bean>
	<!--服务列表 -->
	<gorilla:service interface="org.myframe.gorilla.test.inter.Inter1" ref="impl1" />
```

```xml
<!--  客户端配置 -->

	<!-- 注册中心 -->
	<gorilla:registry ip="127.0.0.1" port="2181" />
	<!--服务列表 -->
	<!--依赖调用 -->
	<gorilla:referer id="inter1" interface="org.myframe.gorilla.test.inter.Inter1" />
```

```java
//测试代码
public static void main(String[] args) {
		ApplicationContext  context = new ClassPathXmlApplicationContext(new String[] {"classpath*:client.xml" });
		System.out.println(context.getBean(Inter1.class).test("111"));
｝
```
