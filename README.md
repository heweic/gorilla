
# gorilla

轻量级、方便使用的RPC框架

## 说明

gorilla中分为服务提供方(RPC Server)，服务调用方(RPC Client)和服务注册中心(Registry)三个角色。

Server提供服务，向Registry注册自身服务，并向注册中心定期发送心跳汇报状态。
Client使用服务，需要向注册中心订阅RPC服务，Client根据Registry返回的服务列表，与具体的Sever建立连接，并进行RPC调用。
当Server发生变更时，Registry会同步变更，Client感知后会对本地的服务列表作相应调整。
三者的交互关系如下图：

![avatar][https://raw.githubusercontent.com/heweic/gorilla/master/img/14612349319195.jpg]

## 示例

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
	ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"classpath*:client.xml" });
	System.out.println(context.getBean(Inter1.class).test("111"));
｝
```
