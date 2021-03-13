package com.pding85.fanxing;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Travelsky_CLSUN
 * @Date: Created on 17-5-8
 * @Description: 泛型销售员群体，该群体只卖水果
 */
public class Sellers<T extends Fruit>
{
    // 使用属性记录售卖某种水果的最佳日期
    public List<Strawberry> BerryDateList = new ArrayList<Strawberry>();
    public List<Apple> AppleDateList = new ArrayList<Apple>();
    public List<Orange> OrangeDateList = new ArrayList<Orange>();

    // 获取具体的水果品种售卖时间列表
    public List<Apple> getAppleList(List<Apple> eList)
    {

        ArrayList<Apple> appleList  =  new  ArrayList<Apple> ();
        return null;
    }

    // 使用泛型extends解决调用实体方法名称带来的异常
    public void getInfo(T t)
    {
        t.showInfo();
    }

    // 获取某种类水果日期销售列表
    public List<T> getList(List<T> eProduuctList)
    {
        return null;
    }

    public static <T> T getListByStatic(List<T> eList)
    {
        return null;
    }




}
