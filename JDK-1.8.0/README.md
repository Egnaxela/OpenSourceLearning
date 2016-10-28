##关于serialVersionUID的作用
* serialVersionUID适用于Java的序列化机制。
  Java的序列化机制是通过判断类的serialVersionUID来验证版本的一致性，在进行反序列化时，JVM会把
传过来的字节流中的serialVersionUID与本地相对应的serialVersionUID进行比较，如果相同就认为是一致的，
可以进行反序列化，否则就会出现序列化版本不一致的**InvalidCastException**异常。
  serialVersionUID有两种显式的生产方式：
  1. 默认的1L，如：private static final long serialVersionUID=1L;
  2. 根据类名、接口名、成员方法及属性等来生成一个64位的哈希字段，如：<br/>
     <code>private static final serialVersionUID=362498820763181265L</code>  [HashMap.Java](https://github.com/WikiDown/mvn-repository/blob/master/JDK-1.8.0/HashMap.java)<br/>
  当实现Serializable接口的类没有上述显式的定义一个serialVersionUID变量时，Java序列化机制会根据编译的Class
自动生成一个serialVersionUID作序列化版本比较。<br/>
  常用情况如下：<br/>
  1.序列化实体类 <br/>
  ```java
    import java.io.Serializable;
    public class Person implements Serializable{
      private static final long serialVersionUID = 1234567890L;
      public int id;
      public String name;
      public Person(int id, String name){
        this.id = id;
        this.name = name;
      }
      public String toString(){
        return "Person: " + id + " " + name;
      }
    }
    ```
   2.序列化功能<br/>
   3.反序列化功能
  
  #### [String详解](https://github.com/WikiDown/mvn-repository/blob/master/JDK-1.8.0/String.md)
  #### [HashMap详解](https://github.com/WikiDown/mvn-repository/blob/master/JDK-1.8.0/HashMap.md)
  #### [Object详解](https://github.com/WikiDown/mvn-repository/blob/master/JDK-1.8.0/Object.md)
  #### [Class类反射小结](https://github.com/WikiDown/OpenSourceStructure/tree/master/JDK-1.8.0/Class.md)


  
