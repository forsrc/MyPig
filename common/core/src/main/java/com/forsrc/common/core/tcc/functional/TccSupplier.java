package com.forsrc.common.core.tcc.functional;

import com.forsrc.common.core.tcc.exception.TccException;

@FunctionalInterface
public interface TccSupplier<T> {

    T get() throws TccException;
}
