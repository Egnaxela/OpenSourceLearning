#Spring 源码解析<br/>
![Spring](https://spring.io/img/spring-logo-3b6f842fa77c3bea3bac17dbce36a101.png)

##Spring 的结构组成
###Spring Bean的生命周期
<hr/>
###spring-beans

1.核心类<br/>
* [**DefaultListableBeanFactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/support/DefaultListableBeanFactory.java):是整个bean加载的核心部分，是Spring注册及加载bean的默认实现。

* [**AliasRegistry**](https://github.com/WikiDown/spring-framework/blob/master/spring-core/src/main/java/org/springframework/core/AliasRegistry.java): 定义对alias的简单增删改等操作

* [**SimpleAliasRegistry**](https://github.com/WikiDown/spring-framework/blob/master/spring-core/src/main/java/org/springframework/core/SimpleAliasRegistry.java):主要使用map作为alias的缓存，并对接口AliasRegistry进行了实现。

* [**BeanFactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/BeanFactory.java): 定义获取bean及bean的各种属性

* [**DefaultSingletonBeanRegistry**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/support/DefaultSingletonBeanRegistry.java): 对接口SingletonBeanRegistry的实现

* [**HierarchicalBeanFactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/HierarchicalBeanFactory.java):继承BeanFactory,在其基础上增加了对parentFactory的支持

* [**BeanDefinitionRegistry**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/BeanDefinitionRegistry.java)：定义对BeanDefinition的各种增删改操作

* [**FactoryBeanRegistrySupport**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/FactoryBeanRegistrySupport.java)：在DefaultSingletonBeanRegistry的基础上，增加了对FactortBean的特殊处理。

* [**ConfigurableBeanFactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/config/ConfigurableBeanFactory.java)：提供配置Factory的各种方法

* [**ListableBeanFactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/ListableBeanFactory.java)：根据各种条件获取bean的配置清单

* [**AbstractBeanFactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/support/AbstractBeanFactory.java)：综合FactoryBeanRegistrySupport和ConfigurableBeanFactory的功能

* [**AutowireCapableBeanFactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/config/AutowireCapableBeanFactory.java)：提供创建bean，自动注入、初始化以及应用bean的后置处理器

* [**AbstractAutowireCapableBeanfactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/support/AbstractAutowireCapableBeanfactory.java)：综合AbstractBeanFactory并对接口Autowire Capable BeanFactory进行实现

* [**ConfigurableListableBeanFactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/config/ConfigurableListableBeanFactory.java)：BeanFactory配置清单，指定忽略类型及接口

* [**DefaultListableBeanFactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/support/DefaultListableBeanFactory.java)：综合上述功能，主要是对Bean注册后的处理

* [**XmlBeanFactory**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/xml/XmlBeanFactory.java)：对DefaultListableBeanFactory类进行了扩展，主要用于从xml文件中读取BeanDefinition,对于注册及获取Bean都是使用从父类继承的方法去实现，与父类不同的是增加了XmlBeanDefinitionReader类型的reader属性。在XmlBeanFactory中主要使用reader属性对资源文件进行读取和注册。

2.XmlBeanDefinitionReader<br/>
XML配置文件的读取是Spring中重要的功能，Spring中大部分功能都是以配置作为切入点
 
* [**ResourceLoader**](https://github.com/WikiDown/spring-framework/blob/master/spring-core/src/main/java/org/springframework/core/io/ResourceLoader.java)：定义资源加载器，主要用于根据给定的资源文件地址返回对应Resource

* [**BeanDefinitionReader**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/support/BeanDefinitionReader.java):主要用于定义资源文件读取并转换为BeanDefinition的各个功能

* [**EnvironmentCapable**](https://github.com/WikiDown/spring-framework/blob/master/spring-core/src/main/java/org/springframework/core/env/EnvironmentCapable.java):定义获取Environment方法

* [**DocumentLoader**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/xml/DocumentLoader.java)：定义从资源文件加载到Document的功能

* [**AbstractBeanDefinitionReader**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/support/AbstractBeanDefinitionReader.java)：对BeanDefinitionReader、EnvironmentCapable接口进行了实现

* [**BeanDefinitionDocumentReader**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/xml/BeanDefinitionDocumentReader.java)：定义读取Document并注册BeanDefinition功能

* [**BeanDefinitionParserDelegate**](https://github.com/WikiDown/spring-framework/blob/master/spring-beans/src/main/java/org/springframework/beans/factory/xml/BeanDefinitionParserDelegate.java)：定义解析Element的各种方法、

XmlBeanDefinitionReader处理xml文件的流程：<br/>

1. 通过继承AbstractBeanDefinitionReader的方法，来使用ResourceLoader将资源文件路径转为对应的Resource文件
2. 通过DocumentLoader对Resource文件进行转换，将Resource文件转为Document文件
3. 通过实现接口BeanDefinitionDocumentReader的DefaultBeanDefinitionDocumentReader类对Document进行解析，并使用BeanDefinitionParserDelegate对Element进行解析。


XmlBeanFactory

###[默认标签的解析](https://github.com/WikiDown/mvn-repository/blob/master/Spring/Chapter%203.md)


<hr/>
[IOC与DI小结](http://www.importnew.com/13619.html)<br/>
[Spring知识点](http://www.importnew.com/19933.html)



