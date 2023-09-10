package extensions.java.lang.Iterable;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static manifold.collections.api.range.RangeFun.to_;

@Extension
public class IterableExt {

    public static <T> Stream<T> stream(@This Iterable<T> thiz) {
        return thiz instanceof Collection<T> collection
            ? collection.stream()
            : StreamSupport.stream(thiz.spliterator(), false);
    }

    public static <T> Stream<T> filterIt(@This Iterable<T> thiz, Predicate<T> predicate) {
        return thiz.stream().filter(predicate);
    }

    public static <T, R> Stream<R> mapIt(@This Iterable<T> thiz, Function<T, R> mapper) {
        return thiz.stream().map(mapper);
    }

    public static <T, U> Stream<T> filterBy(@This Iterable<T> thiz, Function<T, U> mapper, Predicate<U> predicate) {
        return thiz.stream()
            .filter(elem -> predicate.test(mapper.apply(elem)));
    }

    public static <T> T get(@This Iterable<T> thiz, int index) {
        if (thiz instanceof List<T> list) {
            return list[index];
        }
        if (!isElementIndex(thiz, index)) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(thiz, index));
        }
        for (T elem : thiz) {
            if (index == 0) {
                return elem;
            }
            index--;
        }
        return null;
    }

    public static <T> int indexOf(@This Iterable<T> thiz, T elem) {
        if (thiz instanceof Collection<T> collection && (collection.isEmpty() || !collection.contains(elem))) {
            return -1;
        }
        if (thiz instanceof List<T> list) {
            return list.indexOf(elem);
        }

        int size = thiz.count();
        for (int i : 0 to_ size) {
            if (elem.equals(thiz[i])) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isElementIndex(Iterable<?> thiz, int index) {
        return index >= 0 && index < thiz.count();
    }

    private static String outOfBoundsMsg(Iterable<?> thiz, int index) {
        return "Index: " + index + ", Size: " + thiz.count();
    }
}
