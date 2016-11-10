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
  
2. [CharSequence](https://github.com/WikiDown/OpenSourceStructure/blob/master/JDK-1.8.0/java.lang/CharSequence.java) 是char值的一个可读序列,对不同类型的char序列提供统一的只读访问。<br/>
   ```java
   int length();
   
   char charAt(int index);
   
   CharSequence subSequence(int start,int end);
   
   public String toString();
   
   public default IntStream chars(){...}
   
   public default InStream codePoints(){...}

   ```
   JDK1.8新特性之：接口中可以有default或static 修饰的方法的方法体<br/>
   
3. [Cloneable](https://github.com/WikiDown/OpenSourceStructure/blob/master/JDK-1.8.0/java.lang/Cloneable.java),指示Object.clone()方法可以合法的对该类的实例进行按字段复制。<br/>
   ```java
   public interface Cloneable(){
   
   }
   ```
   如果某实例没有实现Cloneable接口，就直接调用Object的clone方法就会报CloneNotSupportException
   ![代码](https://github.com/WikiDown/OpenSourceStructure/blob/master/JDK-1.8.0/java.lang/images/Cloneable-001.png)
   ![报错](https://github.com/WikiDown/OpenSourceStructure/blob/master/JDK-1.8.0/java.lang/images/Cloneable-000.png)
   		
   
   
	



####类
