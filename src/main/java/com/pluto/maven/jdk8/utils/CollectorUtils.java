package com.pluto.maven.jdk8.utils;


import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author 徐明龙 XuMingLong 2019-05-27
 * @program: java-expert-platform
 * @description: ${description}
 **/
/**
 *
 * @author 徐明龙 XuMingLong 2019-05-28
 */
public class CollectorUtils {

    /**
     * 合并Map
     * @author 徐明龙 XuMingLong 2019-05-27
     * @param m1
     * @param m2
     * @param mergeFunction
     * @return void
     */
    public static <K, V> void  mergerMap(Map<K, V> m1, Map<K, V> m2, BinaryOperator<V> mergeFunction) {
        for (Map.Entry<K,V> e : m2.entrySet())
            m1.merge(e.getKey(), e.getValue(), mergeFunction);
    }

    /**
     * 获取一个Map的记录数累加收集器，如果指定了已存在的Map，则会自动累加到指定的Map元素里，不支持并发
     * @author 徐明龙 XuMingLong 2019-05-27
     * @param keyMapper
     * @param mapFactory
     * @return java.util.stream.Collector<T,?,java.util.Map<K,java.lang.Long>>
     */
    public static<T,K,M extends Map<K, Long>> Collector<T, ?, Map<K, Long>> countWithMap(
        Function<T, K> keyMapper,
        Supplier<Map<K,Long>> mapFactory
        ){
        return Collectors.toMap(
            keyMapper,
            e -> 1l,
            Long::sum,
            mapFactory);
    }
    
    /**
     * 获取一个Map的记录数累加收集器，如果指定了已存在的Map，则会自动累加到指定的Map元素里，支持并发
     * @author 徐明龙 XuMingLong 2019-05-28 
     * @param keyMapper
     * @param mapFactory
     * @return
     */
    public static<T,K,M extends ConcurrentMap<K, Long>> Collector<T, ?, ConcurrentMap<K, Long>> countWithConcurrentMap(
        Function<T, K> keyMapper,
        Supplier<ConcurrentMap<K,Long>> mapFactory
        ){
        return Collectors.toConcurrentMap(
            keyMapper,
            e -> 1l,
            Long::sum,
            mapFactory);
    }
}