package Drzewo;

import java.lang.reflect.InvocationTargetException;

/**
 * Klasa implementująca wartownik drzewa BST.
 *
 * @param <T> typ wartości
 */
public class Wartownik<T extends Comparable<T> & Dodawalny<T>> extends Węzeł<T> {

    /**
     * Wartownik jest wypisywany jako "-".
     *
     * @return "-"
     */
    @Override
    public String toString() {
        return "-";
    }

    /**
     * Tworzy nowy wierzchołek z podaną wartością.
     *
     * @param inna wartość do wstawienia
     * @return nowy wierzchołek
     */
    @Override
    protected Węzeł<T> wstaw(T inna) {
        return new Wierzchołek<T>(inna, this);
    }

    /**
     * To znaczy, że szukana wartość nie występuje w drzewie.
     *
     * @param inna wartość szukanego elementu
     * @return wartownik
     */
    @Override
    protected Węzeł<T> znajdź(T inna) {
        return this;
    }

    /**
     * Rozmiar poddrzewa to 0.
     *
     * @return 0
     */
    @Override
    protected int obliczRozmiarPoddrzewa() {
        return 0;
    }

    /**
     * Wysokość poddrzewa to 0.
     *
     * @return 0
     */
    @Override
    protected int obliczWysokość() {
        return 0;
    }


    /**
     * Nic nie wypisuje.
     */
    @Override
    protected void wypiszPoddrzewo() {
    }

    /**
     * Nic nie wypisuje.
     *
     * @param głębokość głębkość tego węzła
     * @param wcięcie   liczba spacji w pojedynczym wcięciu
     */
    @Override
    protected void wypiszPoddrzewoHoryzontalnie(int głębokość, int wcięcie) {
    }

    /**
     * Zwraca element neutralny dodawania dla danego typu (konstruktor bezargumentowy) jeśli może. W przeciwnym przypadku rzuca błąd.
     *
     * @param typ
     * @return element neutralny dodawania
     */
    @Override
    protected T suma(Class<T> typ) {
        try {
            return typ.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }
}
