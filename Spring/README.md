#Spring 源码深度解析<br/>

##Spring 的结构组成

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

AbstractBeanFactory：综合FactoryBeanRegistrySupport和ConfigurableBeanFactory的功能

AutowireCapableBeanFactory：提供创建bean，自动注入、初始化以及应用bean的后置处理器

AbstractAutowireCapableBeanfactory：综合AbstractBeanFactory并对接口Autowire Capable BeanFactory进行实现

ConfigurableListableBeanFactory：BeanFactory配置清单，指定忽略类型及接口

DefaultListableBeanFactory：综合上述功能，主要是对Bean注册后的处理

XmlBeanFactory对DefaultListableBeanFactory类进行了扩展，主要用于从xml文件中读取BeanDefinition，
对于注册及获取Bean都是使用从父类继承的方法去实现，与父类不同的是增加了XmlBeanDefinitionReader类型的
reader属性。在XmlBeanFactory中主要使用reader属性对资源文件进行读取和注册。

2.XmlBeanDefinitionReader
XML配置文件的读取是Spring中重要的功能，Spring中大部分功能都是以配置作为切入点
 
ResourceLoader：定义资源加载器，主要用于根据给定的资源文件地址返回对应Resource

BeanDefinitionReader:主要用于定义资源文件读取并转换为BeanDefinition的各个功能

EnvironmentCapable:定义获取Environment方法


DocumentLoader：定义从资源文件加载到Document的功能

AbstractBeanDefinitionReader：对BeanDefinitionReader、EnvironmentCapable接口进行了实现

BeanDefinitionDocumentReader：定义读取Document并注册BeanDefinition功能

BeanDefinitionParserDelegate：定义解析Element的各种方法、

XmlBeanDefinitionReader处理xml文件的流程：<br/>
1. 通过继承AbstractBeanDefinitionReader的方法，来使用ResourceLoader将资源文件路径转为对应
    的Resource文件
   
2.通过DocumentLoader对Resource文件进行转换，将Resource文件转为Document文件

3.通过实现接口BeanDefinitionDocumentReader的DefaultBeanDefinitionDocumentReader类对
Document进行解析，并使用BeanDefinitionParserDelegate对Element进行解析。


XmlBeanFactory



