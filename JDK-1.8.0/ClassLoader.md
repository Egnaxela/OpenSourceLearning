###ClassLoader原理：
<p>
   &nbsp;&nbsp;当Java程序运行时，会调用该程序的一个入口函数来调用系统的相关功能，而这些功能都被<br/>
封装在不同的class文件中，所以要当一个class文件要调用另一个Class文件的方法时，如果另一个<br/><br/>
文件不存在，则会出现异常。程序在启动时，为了性能方面等原因，并不会一次加载所有的Class文件，<br/>
而是根据程序的需要，通过Java的类加载机制来动态加载某个Class文件到内存中。<br/>
    Java默认提供了三个ClassLoader类加载器<br/>
    1. BootStrap 启动类加载器（原始类加载器），是Java中最顶级的类加载器，负责加载JDK中的核心类库。<br/>
</p>    
```java
URL[]  urls=sun.misc.Launcher.getBootstrapClassPath().getURLs();
for(URL url:urls){
  System.out.println(url.toExternalForm());
}
```

![结果](https://github.com/WikiDown/OpenSourceStructure/blob/master/JDK-1.8.0/picture/b7df0874-ef49-4209-890b-2be7c217d0fe.png)
 
