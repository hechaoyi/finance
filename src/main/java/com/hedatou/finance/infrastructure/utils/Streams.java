package com.hedatou.finance.infrastructure.utils;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 实现描述：JDK8 stream 工具
 *
 * @author hechaoyi@me.com
 * @since 2016年9月14日 下午3:39:42
 */
public abstract class Streams {

    // ----------------------------------------
    // list
    // ----------------------------------------

    /**
     * list -> list
     * @param list
     * @param mapper T -> R
     * @return
     */
    public static <T, R> List<R> map(List<T> list, Function<? super T, ? extends R> mapper) {
        return list.stream().map(mapper).collect(toList());
    }

    /**
     * list -> set
     * @param list
     * @param mapper T -> R
     * @return
     */
    public static <T, R> Set<R> mapToSet(List<T> list, Function<? super T, ? extends R> mapper) {
        return list.stream().map(mapper).collect(toSet());
    }

    // ----------------------------------------
    // set
    // ----------------------------------------

    /**
     * set -> set
     * @param set
     * @param mapper T -> R
     * @return
     */
    public static <T, R> Set<R> map(Set<T> set, Function<? super T, ? extends R> mapper) {
        return set.stream().map(mapper).collect(toSet());
    }

    /**
     * set findAny
     * @param set
     * @param predicate
     * @return
     */
    public static <T> Optional<T> find(Set<T> set, Predicate<? super T> predicate) {
        return set.stream().filter(predicate).findAny();
    }

    // ----------------------------------------
    // array
    // ----------------------------------------

    /**
     * array -> list
     * @param array
     * @param mapper
     * @return
     */
    public static <T, R> List<R> map(T[] array, Function<? super T, ? extends R> mapper) {
        return Arrays.stream(array).map(mapper).collect(toList());
    }

    /**
     * array -> set
     * @param array
     * @param mapper
     * @return
     */
    public static <T, R> Set<R> mapToSet(T[] array, Function<? super T, ? extends R> mapper) {
        return Arrays.stream(array).map(mapper).collect(toSet());
    }

}
