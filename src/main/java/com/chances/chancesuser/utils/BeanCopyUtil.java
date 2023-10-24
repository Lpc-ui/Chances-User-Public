package com.chances.chancesuser.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;
import java.util.function.Supplier;

/**
 * 对象拷贝，对象集合拷贝
 *
 * @author lipengcheng
 */
public class BeanCopyUtil extends BeanUtils {
    /**
     * @param sources 待拷贝集合
     * @param target  目标对象
     * @return List<T>
     */
    public static <S, T> List<T> copyListProperties(Collection<S> sources, Supplier<T> target) {
        List<T> list = new ArrayList<>();
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
        }
        return list;
    }


    public static <S, T> T copyBeanProperties(S source, Supplier<T> target) {
        T target1 = target.get();
        copyProperties(source, target1);
        return target1;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
}
