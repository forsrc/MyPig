package com.forsrc.common.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cglib.beans.BeanCopier;

import com.forsrc.common.core.tcc.dto.TccDto;
import com.forsrc.common.core.tcc.dto.TccLinkDto;

public class BeanCopierUtils {

    private static final Map<String, BeanCopier> MAP = new HashMap<>();
    private static final Map<Class<?>, Object> MAP_INSTANCE = new HashMap<>();

    public static <S, T> T copy(S s, Class<T> cls) {
        T t = getInstance(cls);
        BeanCopier beanCopier = getBeanCopier(s, t);
        beanCopier.copy(s, t, null);
        return t;
    }

    public static <S, T> void copy(S s, T t) {
        BeanCopier beanCopier = getBeanCopier(s, t);
        beanCopier.copy(s, t, null);
    }

    public static <S, T> List<T> copy(List<S> list, Class<T> tc) {

        BeanCopier beanCopier = getBeanCopier(list.get(0).getClass(), tc);
        List<T> targets = new ArrayList<>(list.size());

        for (S s : list) {
            T t = getInstance(tc);
            beanCopier.copy(s, t, null);
            targets.add(t);
        }

        return targets;

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
                beanCopier = MAP.get(key);
                if (beanCopier == null) {
                    beanCopier = BeanCopier.create(sc, tc, false);
                    MAP.put(key, beanCopier);
                }
            }
        }
        return beanCopier;
    }

    public static <T> T getInstance(Class<T> cls) {
        T t = (T) MAP_INSTANCE.get(cls);
        if (t == null) {
            synchronized (MAP_INSTANCE) {
                t = (T) MAP_INSTANCE.get(cls);
                if (t == null) {

                    try {
                        t = cls.newInstance();
                        MAP_INSTANCE.put(cls, t);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return t;
    }

    public static void main(String[] args) {

        TccDto tcc = new TccDto();
        tcc.setId(UUID.randomUUID());
        tcc.setTimes(1);
        TccLinkDto tccLinkDto = BeanCopierUtils.copy(tcc, TccLinkDto.class);
        System.out.println(tccLinkDto);
        List<TccLinkDto> list = new ArrayList<>();
        list.add(tccLinkDto);
        List<TccDto> listAfter = BeanCopierUtils.copy(list, TccDto.class);
        System.out.println(listAfter.get(0));
    }
}
