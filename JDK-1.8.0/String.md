  String实质是一个字符数组，String类不可被继承，不可变性。<br/>
  [Java堆、栈和常量池以及相关String的详细讲解](http://www.cnblogs.com/xiohao/p/4296088.html)<br/>
  [知乎String 外链](http://fromwiz.com/share/s/2NznIz3wXkFc23YdT71GMR-R1AbjcT2VykIL2-8d1h1RMlsx)<br/>
   密码：ak17 <br/>
    常量池（constant pool）指的是在编译器被确定，并被保存在已编译的.class文件中的一些数据。
  包括了关于类、方法、接口等中的常量，也包括了字符串常量。常量池还具备动态性，运行期间
  可以将新的常量放入池中，String类的intern（）就是这一特性的应用。
  虚拟机为每一个被装载的类型维护一个常量池，池中为该类型所用常量的一个有序集合，包括
  直接常量（String、Integer和Float常量）和其他类型、字段和方法的符号引用
  String的定义方法有三种方式：
    1、使用关键字new，如：String s1=new String("myString");
          在程序编译期间，编译程序先去字符串常量池检查，是否存在"myString"，如果不存在，
       则在常量池中开辟一个内存空间存放"myString"，如果存在的话，则不用重新开辟，保证常量池中有一个“myString”常量，节省内存空间。
       然后在内存堆中开辟一块空间存放new出来的String实例，在栈中开辟一块空间，命名为“s1”，
       存放的值为堆中String实例的内存地址，这个过程就是将引用s1指向new出来的String实例。
        
    2、直接定义，如：String s1="myString";
          在程序编译期间，编译程序先去字符串常量池检查，是否存在“myString”，如果不存在，则在常量池中
       开辟一个内存空间存放"myString"；如果存在的话，则不用重新开辟空间。然后在栈开辟一块空间，命名为
       "s1",存放的值为常量池中"myString"的内存地址。
    
    3、串联生成，如：String s1="my" + "String"；
          堆中new 出来的实例和常量池中的"myString"是什么关系？
          常量池中的字符串常量与堆中的String对象有什么区别？
          为什么直接定义的字符串同样可以调用String对象的各种方法？
          
      
      StringBuilder和StringBuffer都继承了抽象类AbstractStringBuilder，这个抽象类和String一样也定义了
    char[] value和 int count，但是没有使用final修饰，故在进行连接操作时，String 每次返回一个新的
    String实例，而StringBuffer与StringBuilder则在原来的基础上添加，所以在进行大量字符串连接运算时，
    不推荐使用String，而推荐StringBuffer和StringBuilder。














