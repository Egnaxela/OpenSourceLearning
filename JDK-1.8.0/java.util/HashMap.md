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
JDK6、7中的HashMap(以下为1.6)
HashMap底层维护的是一个数组，数组的每一项都是一个Entry
```java
  transient Entry<k,v>[] table;
```
向HashMap中放置的对象实际上是存储在该数组中；
而HashMap中的key、value则以Entry的形式存放在数组中
```java
    static class Entry<K,V> implements Map.Entry<K,V> {
        final K key;
        V value;
        Entry<K,V> next;
        final int hash;

        /**
         * Creates new entry.
         */
        Entry(int h, K k, V v, Entry<K,V> n) {
            value = v;
            next = n;
            key = k;
            hash = h;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final V setValue(V newValue) {
	    V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry e = (Map.Entry)o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2)))
                    return true;
            }
            return false;
        }

        public final int hashCode() {
            return (key==null   ? 0 : key.hashCode()) ^
                   (value==null ? 0 : value.hashCode());
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }

        /**
         * This method is invoked whenever the value in an entry is
         * overwritten by an invocation of put(k,v) for a key k that's already
         * in the HashMap.
         */
        void recordAccess(HashMap<K,V> m) {
        }

        /**
         * This method is invoked whenever the entry is
         * removed from the table.
         */
        void recordRemoval(HashMap<K,V> m) {
        }
    }

```
hash值相同的Entry会放在同一位置，用链表相连，通过key的hashCode来计算；
JDK1.8中已经不用了。
```java
   static int hash(int h){
     h ^=(h >>>20 )^(h >>> 12);
     return h^ (h >>> 7) ^ (h >>> 4);
   }
```
通过hash计算出来的值将会使用indexFor方法找到它所在的table下标
```java
   static int indexFor(int h,int length){
     return h & (length-1);
   }
```
当两个key通过hashCode计算相同时，则发生了hash冲突，在1.6中HashMap解决冲突的是使用链表。
当发生hash冲突时，则将存放在数组中Entry设置为新值，新值放在数组中，旧值放在新值的链表上。

<hr/>
一直到JDK1.7为止，HashMap都是基于数组和链表实现的，当hash值冲突时，就将对应节点以链表的形式存储。
JDK1.7采用的是位桶+链表的方式，即散列链表的方式，而在JDK1.8中采用的是位桶+链表/红黑树，是非线程安全的，
当某个位桶的链表长度达到某个阈值的时候，这个链表就将转换为红黑树。
```java
  transient Node<K,V>[] table;
```
1.8中不在使用Entry而使用Node,因为红黑树的实现和TreeNode关联。
```java
  static final int hash(Object key){
    int h;
    return (key == null)? 0:(h =key.hashCode()) ^ (h>>>16);
  }
``


####[JDK7与JDK8中HashMap的实现](https://my.oschina.net/hosee/blog/618953)
