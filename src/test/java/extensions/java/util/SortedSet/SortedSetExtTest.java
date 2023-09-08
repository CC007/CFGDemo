package extensions.java.util.SortedSet;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SortedSetExtTest {

    @RepeatedTest(10)
    void get() {
        // prepare
        SortedSet<String> sortedSet = new TreeSet<>();
        sortedSet.add("a");
        sortedSet.add("c");
        sortedSet.add("b");

        // execute
        String actual = sortedSet[1];

        // verify
        assertThat(actual, is("b"));
    }

    @Test
    void get_IndexOutOfBounds() {
        // prepare
        SortedSet<String> sortedSet = new TreeSet<>();
        sortedSet.add("a");
        sortedSet.add("c");
        sortedSet.add("b");

        // execute
        IndexOutOfBoundsException actualException = assertThrows(IndexOutOfBoundsException.class, () -> {
            String ignored = sortedSet[3];
        });

        // verify
        assertThat(actualException.getMessage(), is("Index: 3, Size: 3"));
    }
}
