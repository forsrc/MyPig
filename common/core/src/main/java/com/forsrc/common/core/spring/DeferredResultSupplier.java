package com.forsrc.common.core.spring;

import com.forsrc.common.core.tcc.exception.TccException;

@FunctionalInterface
public interface DeferredResultSupplier<T> {

    T get() throws TccException;
}
