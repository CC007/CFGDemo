package extensions.java.lang.Iterable;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Extension
public class IterableExt {
    public static <T, U> Stream<T> filterBy(@This Iterable<T> thiz, Function<T, U> mapper, Predicate<U> predicate) {
        Stream<T> stream = thiz instanceof Collection<T> collection
            ? collection.stream()
            : StreamSupport.stream(thiz.spliterator(), false);
        return stream.filter(elem -> predicate.test(mapper.apply(elem)));
    }
}
