package com.axialeaa.doormat.function;

@FunctionalInterface
public interface ToBooleanTriFunction<T, U, V> {

    boolean applyAsBoolean(T t, U u, V v);

}
