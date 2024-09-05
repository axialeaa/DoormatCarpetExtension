package com.axialeaa.doormat.util.function;

@FunctionalInterface
public interface ToBooleanTriFunction<T1, T2, T3> {

    boolean apply(T1 t1, T2 t2, T3 t3);

}
