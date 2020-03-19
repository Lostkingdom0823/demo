package com.lostkingdom.demo.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: demo
 * @description: 用于DTO集合与实体集合转换
 * @author: jiang.yin
 * @create: 2020-03-17 14:03
 **/
public class CollectionUtil {

    public static <T,F> List<F> listCopy(Class<F> target, List<T> sourceList){
        if(!CollectionUtils.isEmpty(sourceList)){
            List<F> targetList = new ArrayList<>();
            for(T t : sourceList){
                try {
                    F f = target.newInstance();
                    BeanUtils.copyProperties(t,f);
                    targetList.add(f);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return targetList;
        }
        return null;
    }

}
