package extensions.java.util.stream.Stream;

import com.github.cc007.utils.Indexed;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.IndexedFunction;
import manifold.ext.rt.api.This;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Stream;

@Extension
public class StreamExt {
    public static <T, R> Stream<R> map(@This Stream<Indexed<T>> thiz, IndexedFunction<T, R> mapper) {
        return thiz.map(elem -> mapper.apply(elem.index, elem.elem));
    }

    public static <T> Stream<Indexed<T>> toIndexed(@This Stream<T> thiz, Iterable<T> indexSource) {
        return thiz.map(elem -> indexSource.getIndexed(elem));
    }

    public static <T, R> Stream<Indexed<R>> mapIndexed(@This Stream<T> thiz, IndexedFunction<T, Indexed<R>> mapper, Iterable<T> indexSource) {
        return thiz
            .toIndexed(indexSource)
            .map(elem -> mapper.apply(elem.index, elem.elem));
    }

    public static <T, R> Stream<Indexed<R>> mapToIndexed(@This Stream<T> thiz, Function<T, R> mapper, Iterable<R> indexSource) {
        return thiz
            .map(mapper)
            .toIndexed(indexSource);
    }

    public static <T> T single(@This Stream<T> thiz) {
        try {
            return thiz.toList().single();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Stream is empty.");
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Stream contains more than one element.");
        }
    }
}
