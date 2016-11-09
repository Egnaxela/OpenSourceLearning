###精读源码之一
提供利用Java语言进行编程的基础类
<hr/>
####接口

1. [Appendable](https://github.com/WikiDown/OpenSourceStructure/blob/master/JDK-1.8.0/java.lang/Appendable.java) 能够被添加char序列和值的对象<br/>
  如果某个类的实例要接收来自Formatter的格式化输出，就必须实现Appendable接口。
  ```java
  public interface Appendable{

	Appendable append(CharSequence csq) throws IOException;

	Appendable append(CharSequence csq,int start,int end) throws IOException;

	Appendable append(char c) throws IOException;

}
  ```
  
2. [CharSequence](https://github.com/WikiDown/OpenSourceStructure/blob/master/JDK-1.8.0/java.lang/CharSequence.java) 是char值的一个可读序列

####类
