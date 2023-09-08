package extensions.java.util.SortedSet;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.SortedSet;

import static manifold.collections.api.range.RangeFun.to_;

@Extension
public class SortedSetExt {
    public static <E> E get(@This SortedSet<E> thiz, int index) {
        if (!isElementIndex(thiz, index)) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(thiz, index));
        }
        return thiz.stream()
            .skip(index)
            .findFirst()
            .orElse(null);
    }

    public static <E> int indexOf(@This SortedSet<E> thiz, E elem) {
        int size = thiz.size();
        for (int i : 0 to_ size) {
            if (elem.equals(thiz[i])) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isElementIndex(SortedSet<?> thiz, int index) {
        return index >= 0 && index < thiz.size();
    }

    private static String outOfBoundsMsg(SortedSet<?> thiz, int index) {
        return "Index: " + index + ", Size: " + thiz.size();
    }
}
