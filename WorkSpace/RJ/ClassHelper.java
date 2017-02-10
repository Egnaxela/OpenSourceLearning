package com.itown.rcp.core.utils;

import com.itown.rcp.core.exception.SystemException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class ClassHelper
{
  private static final Object[] NO_ARGS = new Object[0];

  private static final Class[] NO_ARGS_CLASS = new Class[0];

  private static ClassLoader _classLoader = null;

  private static Object _mutex = new Object();

  private static final Map<Class, Class> primitiveMap = new HashMap();

  private static final Map<Class, Class> wrapperMap = new HashMap();

  private static final Map<Class, Object> primitiveInitialValueMap = new HashMap();

  private static final Map<Class, Class> interfaceMapping = Collections.synchronizedMap(new HashMap());

  public static Class getWrapper(Class clazz)
  {
    if ((clazz == null) || (!clazz.isPrimitive())) {
      return clazz;
    }
    return (Class)wrapperMap.get(clazz);
  }

  public static Class getPrimitive(Class clazz)
  {
    if ((clazz == null) || (!primitiveMap.containsKey(clazz))) {
      return clazz;
    }
    return (Class)primitiveMap.get(clazz);
  }

  public static Object getPrimitiveInitialValue(Class clazz)
    throws SystemException
  {
    if ((clazz == null) || (!clazz.isPrimitive())) {
      throw new SystemException("类型不能为空，请必须是原始类型。");
    }
    return primitiveInitialValueMap.get(clazz);
  }

  public static void setClassLoader(ClassLoader loader)
  {
    synchronized (_mutex) {
      _classLoader = loader;
    }
  }

  public static <T extends Annotation> T getAnnotation(Class clazz, String fieldName, Class<T> annoClass) {
    Annotation anno = null;
    Class superClazz = clazz;
    Field field = null;
    while ((field == null) && (superClazz != Object.class)) {
      field = getField(superClazz, fieldName);
      superClazz = superClazz.getSuperclass();
    }
    if (field != null) {
      anno = field.getAnnotation(annoClass);
    }
    return anno;
  }

  public static ClassLoader getClassLoader()
  {
    return _classLoader == null ? Thread.currentThread().getContextClassLoader() : _classLoader;
  }

  public static String getShortName(Class clazz)
  {
    String name = clazz.getName();
    if (clazz.isArray()) {
      name = getClassFromArray(clazz).getName();
    }

    int index = name.lastIndexOf(".");
    if (index >= 0) {
      name = name.substring(index + 1);
    }
    return name;
  }

  public static URL getResource(String name)
  {
    return getClassLoader().getResource(name);
  }

  public static Class getClass(String className, boolean initialize)
    throws ClassNotFoundException
  {
    return Class.forName(className, initialize, getClassLoader());
  }

  public static Class getClassFromArray(Class clazz)
  {
    Class ret = clazz;
    if ((clazz != null) && (clazz.isArray())) {
      String pattern = "\\[+[LZBCDFIJS]";
      String arrayClassName = clazz.getName();
      Matcher m = RegExpUtils.generate(pattern, arrayClassName);
      if (m.find()) {
        char endChar = arrayClassName.charAt(m.end() - 1);
        switch (endChar)
        {
        case 'L':
          String temp = arrayClassName.substring(m.end());

          String className = temp.substring(0, temp.length() - 1);
          ret = getClass(className);
          break;
        case 'Z':
          ret = Boolean.TYPE;
          break;
        case 'B':
          ret = Byte.TYPE;
          break;
        case 'C':
          ret = Character.TYPE;
          break;
        case 'D':
          ret = Double.TYPE;
          break;
        case 'F':
          ret = Float.TYPE;
          break;
        case 'I':
          ret = Integer.TYPE;
          break;
        case 'J':
          ret = Long.TYPE;
          break;
        case 'S':
          ret = Short.TYPE;
        case 'E':
        case 'G':
        case 'H':
        case 'K':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y': }  }  } return ret;
  }

  public static <T> T newInstance(Class<T> target)
  {
    if (target == null) {
      return null;
    }
    Class _target = target;
    if ((target.isInterface()) || (Modifier.isAbstract(target.getModifiers()))) {
      _target = (Class)interfaceMapping.get(target);
      if (_target == null)
        throw new SystemException("接口或者抽象类[" + target + "]未定义对应的实体类!");
    }
    try
    {
      return _target.newInstance();
    } catch (InstantiationException e) {
      throw new SystemException("实例化类[" + target + "]异常!", e); } catch (IllegalAccessException e) {
    }
    throw new SystemException("非法存取类[" + target + "]异常!", e);
  }

  public static Object newInstance(Class target, boolean makeAccessible)
    throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
  {
    if (makeAccessible) {
      return newInstance(target, NO_ARGS_CLASS, NO_ARGS, makeAccessible);
    }
    return target.newInstance();
  }

  public static Object newInstance(Class target, Class[] types, Object[] args)
    throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
  {
    return newInstance(target, types, args, false);
  }

  public static Object newInstance(Class target, Class[] types, Object[] args, boolean makeAccessible)
    throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
  {
    Constructor con;
    if (makeAccessible) {
      Constructor con = target.getDeclaredConstructor(types);
      if ((makeAccessible) && (!con.isAccessible()))
        con.setAccessible(true);
    }
    else {
      con = target.getConstructor(types);
    }
    return con.newInstance(args);
  }

  public static Object newPrivateInstance(String className)
    throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException
  {
    Object obj = null;
    Class target = getClass(className);

    Constructor[] cons = target.getDeclaredConstructors();
    for (int i = 0; i < cons.length; i++) {
      Constructor con = cons[i];

      if (con.getParameterTypes().length == 0) {
        con.getParameterTypes();
        con.setAccessible(true);
        obj = con.newInstance(new Object[0]);
        break;
      }
    }

    return obj;
  }

  public static Method getMethod(Class clazz, String methodName, Class[] params)
  {
    try
    {
      Method method = clazz.getMethod(methodName, params);
      if (null == method);
      return method;
    } catch (Exception ignored) {
    }
    return null;
  }

  public static Method getGetterMethod(Class clazz, String fieldName)
  {
    String getter = "get" + StringUtils.uppercaseCapital(fieldName);
    try {
      Method method = clazz.getMethod(getter, new Class[0]);
      return method;
    } catch (Exception ignored) {
    }
    return null;
  }

  public static Method getSetterMethod(Class clazz, String fieldName)
  {
    try
    {
      Method getter = getGetterMethod(clazz, fieldName);

      String setterName = "set" + StringUtils.uppercaseCapital(fieldName);
      return clazz.getMethod(setterName, new Class[] { getter.getReturnType() });
    } catch (Exception ignored) {
    }
    return null;
  }

  public static Field getField(Class clazz, String fieldName)
  {
    Field field = null;
    try {
      field = clazz.getDeclaredField(fieldName);
    } catch (Exception ignored) {
    }
    return field;
  }

  public static Field getField(Class clazz, String fieldName, boolean recursive)
  {
    Field field = null;
    field = getField(clazz, fieldName);

    if (recursive) {
      Class targetClass = clazz;

      while ((field == null) && (targetClass != null) && (targetClass != Object.class)) {
        targetClass = targetClass.getSuperclass();
        field = getField(targetClass, fieldName);
      }
    }

    return field; } 
  public static Class getClass(String name) { // Byte code:
    //   0: aload_0
    //   1: iconst_1
    //   2: invokestatic 81	com/itown/rcp/core/utils/ClassHelper:getClass	(Ljava/lang/String;Z)Ljava/lang/Class;
    //   5: areturn
    //   6: astore_1
    //   7: new 8	com/itown/rcp/core/exception/SystemException
    //   10: dup
    //   11: ldc 83
    //   13: iconst_2
    //   14: anewarray 14	java/lang/Object
    //   17: dup
    //   18: iconst_0
    //   19: ldc_w 84
    //   22: invokevirtual 85	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
    //   25: aastore
    //   26: dup
    //   27: iconst_1
    //   28: aload_0
    //   29: aastore
    //   30: invokestatic 86	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   33: aload_1
    //   34: invokespecial 60	com/itown/rcp/core/exception/SystemException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   37: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   0	5	6	java/lang/ClassNotFoundException } 
  public static Object newInstance(String className) { return newInstance(getClass(className));
  }

  public static Object newInstance(String className, Class[] types, Object[] args)
    throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException
  {
    return newInstance(getClass(className), types, args);
  }

  public static Object newInstance(Class target, Class type, Object arg)
    throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
  {
    return newInstance(target, new Class[] { type }, new Object[] { arg });
  }

  public static Object newInstance(String className, Class type, Object arg)
    throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException
  {
    return newInstance(className, new Class[] { type }, new Object[] { arg });
  }

  public static Method getMethod(Object object, String methodName, Class[] params)
  {
    return getMethod(object.getClass(), methodName, params);
  }

  public static Method getMethod(String className, String methodName, Class[] params)
  {
    try
    {
      return getMethod(getClass(className, false), methodName, params);
    } catch (Exception ignored) {
    }
    return null;
  }

  public static Method getMethod(String className, String methodName)
  {
    try
    {
      return getMethod(getClass(className, false), methodName);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Method getMethod(Class clazz, String methodName)
  {
    try
    {
      Method[] ms = clazz.getMethods();
      for (int i = 0; i < ms.length; i++)
        if (ms[i].getName().equals(methodName))
          return ms[i];
    }
    catch (Exception ignored)
    {
    }
    return null;
  }

  public static Method getMethod(Class clazz, String methodName, int pLength)
  {
    try
    {
      Method[] ms = clazz.getMethods();
      for (int i = 0; i < ms.length; i++) {
        Method method = ms[i];
        if ((method.getName().equals(methodName)) && (method.getParameterTypes().length == pLength))
          return ms[i];
      }
    }
    catch (Exception ignored) {
    }
    return null;
  }

  public static String[] getPropertyNames(Class clazz)
  {
    Method[] methods = clazz.getMethods();
    ArrayList fieldNames = new ArrayList(methods.length / 2);
    for (int j = 0; j < methods.length; j++) {
      Method method = methods[j];
      if ((isGetter(method)) && (hasSetter(methods, method))) {
        String methodName = method.getName();
        if (methodName.length() > 3) {
          fieldNames.add(StringUtils.lowcaseCapital(methodName.substring(3)));
        }
      }
    }

    return (String[])(String[])fieldNames.toArray(new String[fieldNames.size()]);
  }

  public static String[] getPropertyNames(Class clazz, boolean recursive)
  {
    ArrayList fieldNames = new ArrayList();
    String[] currentPropertNames = getPropertyNames(clazz);
    for (String propertyName : currentPropertNames) {
      fieldNames.add(propertyName);
    }

    if (recursive) {
      Class childClass = clazz;
      while ((childClass != null) && (childClass != Object.class)) {
        String[] propertNames = getPropertyNames(childClass);
        for (String propertyName : propertNames) {
          if (fieldNames.contains(propertyName)) {
            fieldNames.add(propertyName);
          }
        }
        childClass = childClass.getSuperclass();
      }
    }

    return (String[])(String[])fieldNames.toArray(new String[fieldNames.size()]);
  }

  public static Class getFieldType(Class clazz, String fieldName)
  {
    String getterName = "get" + StringUtils.uppercaseCapital(fieldName);
    Method m = getMethod(clazz, getterName, NO_ARGS_CLASS);
    if (null != m) {
      return m.getReturnType();
    }

    String isName = "is" + StringUtils.uppercaseCapital(fieldName);
    m = getMethod(clazz, isName, NO_ARGS_CLASS);
    if (null != m) {
      return m.getReturnType();
    }

    return null;
  }

  private static boolean isGetter(Method method)
  {
    if (method.getParameterTypes().length > 0) {
      return false;
    }
    if (method.getName().startsWith("get"))
      return true;
    if (method.getName().startsWith("is")) {
      return method.getReturnType() == Boolean.TYPE;
    }
    return false;
  }

  private static boolean hasSetter(Method[] methods, Method getter)
  {
    for (int i = 0; i < methods.length; i++)
    {
      if ((methods[i].getName().startsWith("set")) && (methods[i].getReturnType() == Void.TYPE) && (methods[i].getName().substring(1).equals(getter.getName().substring(1))))
      {
        return true;
      }
    }
    return false;
  }

  static
  {
    wrapperMap.put(Integer.TYPE, Integer.class);
    wrapperMap.put(Double.TYPE, Double.class);
    wrapperMap.put(Long.TYPE, Long.class);
    wrapperMap.put(Float.TYPE, Float.class);
    wrapperMap.put(Character.TYPE, Character.class);
    wrapperMap.put(Byte.TYPE, Byte.class);
    wrapperMap.put(Boolean.TYPE, Boolean.class);
    wrapperMap.put(Short.TYPE, Short.class);

    primitiveMap.put(Integer.class, Integer.TYPE);
    primitiveMap.put(Double.class, Double.TYPE);
    primitiveMap.put(Long.class, Long.TYPE);
    primitiveMap.put(Float.class, Float.TYPE);
    primitiveMap.put(Character.class, Character.TYPE);
    primitiveMap.put(Byte.class, Byte.TYPE);
    primitiveMap.put(Boolean.class, Boolean.TYPE);
    primitiveMap.put(Short.class, Short.TYPE);

    primitiveInitialValueMap.put(Integer.TYPE, new Integer(0));
    primitiveInitialValueMap.put(Double.TYPE, new Double(0.0D));
    primitiveInitialValueMap.put(Long.TYPE, new Long(0L));
    primitiveInitialValueMap.put(Float.TYPE, new Float(0.0F));
    primitiveInitialValueMap.put(Character.TYPE, new Character('0'));
    primitiveInitialValueMap.put(Byte.TYPE, new Byte(0));
    primitiveInitialValueMap.put(Boolean.TYPE, Boolean.FALSE);
    primitiveInitialValueMap.put(Short.TYPE, new Short(0));

    interfaceMapping.put(List.class, ArrayList.class);
    interfaceMapping.put(Map.class, HashMap.class);
  }
}
