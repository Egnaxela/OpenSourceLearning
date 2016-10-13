  String实质是一个字符数组，String类不可被继承，不可变性。<br/>
  String的基本方法：
  * length( )<br/>

    ```java
      public int length(){
        return value.length;
      }
    ```

&nbsp;&nbsp;通过String的length（）方法来获取String字符串的长度，因为String本质是一个字符数组，即该方法通过该字符
    数组的length属性来获取。
    而数组的长度获取是Java底层用c实现，可以通过jdk安装路径下include下的jni.h中得到一点点启发, 
    有一个函数GetArrayLength用来支持获取数组长度. 更详细的源码-openJDK？

  * isEmpty( )

  ```java
    public boolean isEmpty(){
      return value.length==0;
    }
  ```

  * charAt(int index)

  ```java
    public char charAt(int index){
      if((index<0)||(index>=value.length)){
        throw new StringIndexOutOfBoundsException(index);
      }
      return value[index];
    }
  ```


  * codePointAt(int index)

  ```java
    public int codePointAt(int index){
      if((index<0)||(index>=value.length)){
        throw new StringIndexOutOfBoundsException(index);
      }
      return Character.codePointAtImpl(value,index,value.length);
    }
   ```

   charAt(int index)返回指定位置的字符<br/>
   codePointAt(int index)返回指定索引处的字符（Unicode代码点）<br/>
   即返回的是对应字符的Unicode值，如 charAt() 返回 a,codePointAt()返回 97<br/>

   * codePointBefore(int index)
   ```java
     public int codePointBefore(int index){
        int i=index-1;
        if((i<0)||(i>=value.length)){
          throw new StringIndexOutOfBoundsException(index);
        }
        return Character.codePointBeforeImpl(value,index,0);
     }
   ```
   codePointBefore(int index)返回指定元素的前一个代码点

  * <font size=4>equals(Object anObject)</font>
  ```java
    public boolean equals(Object anObject){
      if(this==anObject){
        return true;
      }
      if(anObject instanceof String){
        String anotherString =(String)anObject;
        int n=value.length;
        if(n==anotherString.value.length){
          char v1[]=value;
          char v2[]=antherString.value;
          int i=0;
          while(n-- !=0){
            if(v1[i]!=v2[i])
              return false; 
            i++;
          }
          return true;
        }
      }
      return false;
    }
  ```



      
    
      

  [Java堆、栈和常量池以及相关String的详细讲解](http://www.cnblogs.com/xiohao/p/4296088.html)<br/>
  [为知String 外链](http://fromwiz.com/share/s/2NznIz3wXkFc23YdT71GMR-R1AbjcT2VykIL2-8d1h1RMlsx)<br/>
   密码：ak17 <br/>
