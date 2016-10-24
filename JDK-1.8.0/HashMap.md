###HashMap不是线性安全的
1.resize 死循环
HashMap的初始容量为16
```java
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
```

如果有数据要插入时，会检查有没有超过设定的threshold，如果超过需要增大Hash表的尺寸。<br/>
整个Hash表里的元素都需要被重新算一片。即做rehash操作<br/>

```java
void resize(int newCapacity){
  Entry[] oldTable =table;
  int oldCapacity=oldTable.length;
  if(oldCapacity==MAXIMUM_CAPACITY){
     threshold=Integer.MAX_VALUE;
     return;
  }
  Entry[] newTable=new Entry[newCapacity];
  transfer(newTable);
  table=newTable;
  threshold=(int)(newCapacity*loadFactor);
}
```


```java
void transfer(Entry[] newTable) {
        Entry[] src = table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry<K,V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry<K,V> next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }
```
transfer：<br/>
1. 对索引数组中的元素遍历<br/>
2. 对链表上的每一个节点遍历<br/>
3. 循环2，直到链表节点全部转移<br/>
4. 循环1，直到所有索引数组全部转移<br/>
如果转移链表前为1-2-3，转移后为3-2-1，死锁的问题就是同时1-2,2-1造成。<br/>
HashMap的死锁问题就出在，transfer( )方法。<br/>
<hr/>
JDK6、7中的HashMap
HashMap底层维护的是一个数组，数组的每一项都是一个Entry
```java
transient Entry<k,v>[] table;
```
向HashMap中放置的对象实际上是存储在该数组中；


####[JDK7与JDK8中HashMap的实现](https://my.oschina.net/hosee/blog/618953)
