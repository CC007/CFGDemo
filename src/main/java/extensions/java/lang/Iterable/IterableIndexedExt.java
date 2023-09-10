package extensions.java.lang.Iterable;

import com.github.cc007.utils.Indexed;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.IndexedFunction;
import manifold.ext.rt.api.IndexedPredicate;
import manifold.ext.rt.api.This;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Extension
public class IterableIndexedExt {

    public static <T> Indexed<T> getIndexed(@This Iterable<T> thiz, int index) {
        return new Indexed<>(index, thiz[index]);
    }

    public static <T> Indexed<T> getIndexed(@This Iterable<T> thiz, T t) {
        return new Indexed<>(thiz.indexOf(t), t);
    }

    public static <T> Stream<Indexed<T>> streamIndexed(@This Iterable<T> thiz) {
        return thiz.mapIt(elem -> thiz.getIndexed(elem));
    }

    public static <T> Stream<Indexed<T>> filterIndexed(@This Iterable<T> thiz, IndexedPredicate<T> predicate) {
        return thiz.streamIndexed()
            .filter(t -> predicate.test(t.index, t.elem));
    }

    public static <T, R> Stream<Indexed<R>> mapIndexedToStream(@This Iterable<T> thiz, IndexedFunction<T, Indexed<R>> mapper) {
        return thiz.streamIndexed()
            .map(t -> mapper.apply(t.index, t.elem));
    }

    public static <T, R> Stream<R> map(@This Iterable<T> thiz, IndexedFunction<T, R> mapper) {
        return thiz.streamIndexed()
            .map(t -> mapper.apply(t.index, t.elem));
    }
}
