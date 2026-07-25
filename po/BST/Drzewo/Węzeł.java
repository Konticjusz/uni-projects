package Drzewo;

/**
 * Klasa abstrakcyjna opisująca węzły w drzewie.
 *
 * @param <T> typ wartości
 */
public abstract class Węzeł<T extends Comparable<T> & Dodawalny<T>> {
    /**
     * Wstawia element w drzewie
     *
     * @param wartość element do wstawienia
     * @return węzeł z wstawionym elementem
     */
    protected abstract Węzeł<T> wstaw(T wartość);

    /**
     * Znajduje element w drzewie o danej wartości.
     *
     * @param wartość szukanego elementu
     * @return węzeł z szukanym elementem, w przeciwnym wypadku wartownik
     */
    protected abstract Węzeł<T> znajdź(T wartość);

    /**
     *
     * @return rozmiar drzewa
     */
    protected abstract int obliczRozmiarPoddrzewa();

    /**
     *
     * @return wysokość drzewa
     */
    protected abstract int obliczWysokość();

    /**
     * Wypisuje poddrzewo aktualnego węzła.
     */
    protected abstract void wypiszPoddrzewo();

    /**
     * Wypisuje poddrzewo horyzontalnie.
     *
     * @param głębokość głębkość tego węzła
     * @param wcięcie   liczba spacji w pojedynczym wcięciu
     */
    protected abstract void wypiszPoddrzewoHoryzontalnie(int głębokość, int wcięcie);

    /**
     * Oblicza sumę elementów w poddrzewie.
     *
     * @param typ
     * @return
     */
    protected abstract T suma(Class<T> typ);


}
