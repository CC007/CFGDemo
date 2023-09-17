package extensions.java.util.Set;

import com.github.cc007.utils.Indexed;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.Set;
import java.util.stream.Stream;

@Extension
public class SetIndexedExt {

    public static <E> Indexed<E> getIndexed(@This Set<E> thiz, E t) {
        return new Indexed<>(thiz.indexOf(t), t);
    }

    public static <E> Stream<Indexed<E>> streamIndexed(@This Set<E> thiz) {
        return thiz.mapIt(elem -> thiz.getIndexed(elem));
    }
}
