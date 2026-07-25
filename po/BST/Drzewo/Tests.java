package Drzewo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Tests {
    @Test
    public void dodajDwaWInteger() {
        WInteger a = WInteger.of(1);
        WInteger b = WInteger.of(2);
        WInteger c = a.dodaj(b);
        assertEquals(c.toString(), "3");
    }

    @Test
    public void czyJestDefaultConstructorWInteger() {
        WInteger a = new WInteger();
        assertEquals(a.toString(), "0");
    }

    @Test
    public void dodajDwaWString() {
        WString a = WString.of("a");
        WString b = WString.of("b");
        WString c = a.dodaj(b);
        assertEquals(c.toString(), "ab");
    }

    @Test
    public void czyJestDefaultConstructorWString() {
        WString a = new WString();
        assertEquals(a.toString(), "");
    }

    @Test
    public void czyTworzyDrzewoBST() {
        DrzewoBST<WInteger> drzewo = new DrzewoBST<>(WInteger.class);
        assertNotNull(drzewo);
    }

    @Test

    public void czyPoprawnaSuma() {
        DrzewoBST<WInteger> drzewo = new DrzewoBST<>(WInteger.class);
        drzewo.wstaw(WInteger.of(5));
        drzewo.wstaw(WInteger.of(3));
        drzewo.wstaw(WInteger.of(7));
        drzewo.wstaw(WInteger.of(1));
        assertEquals(drzewo.obliczSumęElementów().toInt(), 16);
    }


}
