package com.forsrc.common.core.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cglib.beans.BeanCopier;

import com.forsrc.common.core.sso.dto.UserTccDto;

public class BeanCopierUtils {

    private static final String TYPE_NAME_PREFIX = "class ";
    private static final Map<String, BeanCopier> MAP = new HashMap<>();

    public static <S, T> void copy(S s, T t) {
        BeanCopier beanCopier = getBeanCopier(s, t);
        beanCopier.copy(s, t, null);
    }

    public static <S, T> List<T> copy(List<S> list, Class<T> tc) throws InstantiationException, IllegalAccessException {

        BeanCopier beanCopier = getBeanCopier(list.get(0).getClass(), tc);
        List<T> l = new ArrayList<>(list.size());
        for (S s : list) {
            T t = tc.newInstance();
            beanCopier.copy(s, t, null);
            l.add(t);
        }
        return l;

    }

    public static <S, T> BeanCopier getBeanCopier(S s, T t) {
        Class<?> sc = s.getClass();
        Class<?> tc = t.getClass();
        return getBeanCopier(sc, tc);
    }

    public static BeanCopier getBeanCopier(Class<?> sc, Class<?> tc) {
        String key = sc.getName() + tc.getName();
        BeanCopier beanCopier = MAP.get(key);
        if (beanCopier == null) {
            synchronized (MAP) {
                if (beanCopier == null) {
                    beanCopier = BeanCopier.create(sc, tc, false);
                    MAP.put(key, beanCopier);
                }
            }
        }
        return beanCopier;
    }

    public static String getClassName(Type type) {
        String className = type.toString();
        if (className.startsWith(TYPE_NAME_PREFIX)) {
            className = className.substring(TYPE_NAME_PREFIX.length());
        }
        return className;
    }

    public static abstract class TypeReference<T> implements Comparable<TypeReference<T>> {
        protected final Type _type;

        protected TypeReference() {
            Type superClass = getClass().getGenericSuperclass();
            if (superClass instanceof Class<?>) { // sanity check, should never happen
                throw new IllegalArgumentException(
                        "Internal error: TypeReference constructed without actual type information");
            }
            _type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        }

        public Type getType() {
            return _type;
        }

        @Override
        public int compareTo(TypeReference<T> o) {
            return 0;
        }
    }

    public static void main(String[] args) {
        System.out.println(new BeanCopierUtils.TypeReference<String>() {
        });
        System.out.println(new BeanCopierUtils.TypeReference<String>() {
        }.getType());
    }
}
