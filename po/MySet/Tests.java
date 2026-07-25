import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class Tests {

    @Test
    public void shouldCreateEmptySet() {
        MySet<Integer> setA = new MySet<Integer>();
        assertNotNull(setA);
        assertTrue(setA.isEmpty());
    }

    @Test
    public void shouldCreateSetFromCollection() {
        MySet<String> setA = new MySet<String>(List.of(new String[]{"ABA", "CDA", "DBA", "ABA"}));
        assertNotNull(setA);
        assertEquals(3, setA.size());

    }

    @Test
    public void shouldCreateSetFromArray() {
        MySet<Float> setA = new MySet<Float>(new Float[]{1.5f, 3.4f, 2f});
        assertNotNull(setA);
        assertEquals(3, setA.size());
    }

    @Test
    public void basicSetArithmeticOperations() {

        MySet<Integer> setA = new MySet<Integer>(new Integer[]{1, 5, 7, 3});
        MySet<Integer> setB = new MySet<Integer>(new Integer[]{1, 3, 5, 10});

        assertEquals(new MySet<Integer>(new Integer[]{1, 3, 5}), setA.intersection(setB));

        setA = new MySet<Integer>(new Integer[]{1, 5, 7, 3});

        assertEquals(new MySet<Integer>(new Integer[]{1, 3, 5, 7, 10}), setA.union(setB));

    }

    @Test
    public void differenceWithItself() {
        MySet<Integer> setA = new MySet<Integer>(new Integer[]{1, 3, 4});
        assertTrue(setA.difference(setA).isEmpty());
    }

    @Test

    public void shouldHaveEqualHashes() {

        MySet<String> setA = new MySet<String>("Ala", "Ma", "Kota");
        MySet<String> setB = new MySet<String>(new String[]{"Ma", "Ala", "Kota"});

        assertEquals(setA.hashCode(), setB.hashCode());
        assertEquals(setA, setB);

    }


}
