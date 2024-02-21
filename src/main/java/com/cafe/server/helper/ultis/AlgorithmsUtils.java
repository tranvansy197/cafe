package com.cafe.server.helper.ultis;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.function.BiFunction;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
// Suppress sonarlint warn because source code comes from java lang.
@SuppressWarnings({"java:S3776", "java:S1117", "java:S1119", "java:S135", "java:S2589", "java:S6541", "unchecked", "fallthrough", "rawtypes"})
public class AlgorithmsUtils {

    /**
     * Folk of {@link java.util.Collections#binarySearch}, support manual custom.
     */
    public static <T, U> int binarySearch(List<T> list, U key, BiFunction<T, U, Integer> comparator) {
        if (list instanceof RandomAccess || list.size() < 5000)
            return indexedBinarySearch(list, key, comparator);
        else
            return iteratorBinarySearch(list, key, comparator);
    }

    public static <T, U> int indexedBinarySearch(List<T> list, U key, BiFunction<T, U, Integer> comparator) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = list.get(mid);
            int cmp = comparator.apply(midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -(low + 1);
    }

    public static <T, U> int iteratorBinarySearch(List<T> list, U key, BiFunction<T, U, Integer> comparator) {
        int low = 0;
        int high = list.size() - 1;
        ListIterator<T> i = list.listIterator();

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = get(i, mid);
            int cmp = comparator.apply(midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -(low + 1);
    }

    public static <T, U extends Comparable<U>> void sort(List<T> list, Function<T, U> keyExtractor, boolean ascending) {
        Comparator<T> comparator = getComparator(keyExtractor);

        if (!ascending) {
            comparator = comparator.reversed();
        }

        list.sort(comparator);
    }

    public static <T, U extends Comparable<U>> void sort(List<T> list, Function<T, U> keyExtractor) {
        list.sort(getComparator(keyExtractor));
    }

    private static <T, U extends Comparable<U>> Comparator<T> getComparator(Function<T, U> keyExtractor) {
        if (keyExtractor == null) {
            // If keyExtractor is null, use a default comparator that compares string representations
            return Comparator.comparing(Object::toString, Comparator.nullsFirst(Comparator.naturalOrder()));
        }
        return Comparator.comparing(keyExtractor, Comparator.nullsFirst(Comparator.naturalOrder()));
    }

    private static <T> T get(ListIterator<? extends T> i, int index) {
        T obj;
        int pos = i.nextIndex();
        if (pos <= index) {
            do {
                obj = i.next();
            } while (pos++ < index);
        } else {
            do {
                obj = i.previous();
            } while (--pos > index);
        }
        return obj;
    }

}
