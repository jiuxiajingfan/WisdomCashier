package com.li.wisdomcashier.base.util;

import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName BeanUtils
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/11 18:02
 * @Version 1.0
 */
public class BeanUtils {

    public static  <V, P> P convert(V base, P target) {
        if(base != null) {
            BeanCopier.create(base.getClass(),target.getClass(),false).copy(base,target,null);
            return target;
        } else {
            return null;
        }
    }

    public static <V, P> P convert(V base, Class<P> target) {
        try {
            P obj = target.newInstance();
            if(base!= null) {
                BeanCopier.create(base.getClass(),obj.getClass(),false).copy(base,obj,null);
                return obj;
            }else{
                return null;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  <V, P> List<P> convertList(List<V> baseList, Class<P> target) {
        if(baseList == null) {
            return null;
        } else {
            ArrayList targetList = new ArrayList();
            Iterator iter = baseList.iterator();

            while(iter.hasNext()) {
                Object vo = iter.next();
                try {
                    targetList.add(convert(vo, target.newInstance()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            return targetList;
        }
    }
}

