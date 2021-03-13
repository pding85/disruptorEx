package com.pding85.fanxing;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.Test;



/**
 * @Author: Travelsky_CLSUN
 * @Date: Created on 17-5-8
 * @Description: 测试类
 */
public class TestGenric
{

    @Test
    public void testGenricTypeExit() throws Exception
    {
        Class<?> sellersTemp = Sellers.class;
        ParameterizedType actualType = null;
        log("====根据反射获取泛型类、泛型字段以及泛型方法的类型====");
        // 测试使用反射获取泛型类的实际参数
        log("泛型类Sellers的泛型实参：" + sellersTemp.getTypeParameters()[0].getBounds()[0].toString());
        // 测试使用反射获取泛型类下泛型变量的实际参数
        Field fieldBerry = sellersTemp.getField("BerryDateList");
        log("泛型字段BerryDateList的类型：" + fieldBerry.getGenericType().toString());
        Method methodAppleList = sellersTemp.getMethod("getAppleList", new Class[]
                { List.class });
        actualType = (ParameterizedType) methodAppleList.getGenericParameterTypes()[0];
        log("泛型方法getAppleList的形式参数的类型：" + actualType.getActualTypeArguments()[0].toString());
        actualType = (ParameterizedType) methodAppleList.getGenericReturnType();
        log("泛型方法getAppleList的返回值的类型：" + actualType.getActualTypeArguments()[0].toString());
        Class<?> selfEmployed = SelfEmployed.class;
        actualType = (ParameterizedType) selfEmployed.getGenericSuperclass();
        log("泛型子类SelfEmployed的主类实参：" + actualType.getActualTypeArguments()[0]);

    }

    /**
     * @Author: Travelsky_CLSUN
     * @Date: Created on 17-5-8
     * @Description: 输出函数
     */
    private void log(String str)
    {
        System.out.println(str);
    }

    // 获取某种类水果日期销售列表
    public static  <T> List<T> getList(List<T> eProductList)
    {
        Class<?> paramClass = eProductList.getClass();
        System.out.println("before transfromer: " + paramClass);
        Type paramType = paramClass.getTypeParameters()[0].getBounds()[0];
        if (paramType instanceof ParameterizedType)
        {
            System.out.println(paramType);
            ParameterizedType genricType = (ParameterizedType) ((ParameterizedType) paramType)
                    .getActualTypeArguments()[0];
            System.out.println("actualType of eProductList is :" + genricType);
        }
        else
        {
            System.out.println("Type of eProductList is :" + paramType);
        }
        return null;
    }
}