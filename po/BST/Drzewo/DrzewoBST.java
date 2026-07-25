package Drzewo;

/**
 * Klasa implementująca drzewo BST
 *
 * @param <T> typ wartości
 */
public class DrzewoBST<T extends Comparable<T> & Dodawalny<T>> {
    private Węzeł<T> korzeń;
    private Wartownik<T> wartownik;
    private Class<T> typ;

    public DrzewoBST(Class<T> typ) {
        wartownik = new Wartownik<T>();
        korzeń = wartownik;
        this.typ = typ;
    }


    /**
     * * Wstawia element o danej wartości w odpowiednie miejsce w drzewie.
     *
     * @param wartość element do wstawienia
     */
    public void wstaw(T wartość) {
        this.korzeń = korzeń.wstaw(wartość);
    }

    /**
     * Zwraca węzeł o danej wartości jeśli istnieje, w przeciwnym wypadku zwraca wartownika.
     *
     * @param wartość wartość szukanego elementu
     * @return
     */
    public Węzeł<T> znajdź(T wartość) {
        return korzeń.znajdź(wartość);
    }

    /**
     * @return rozmiar drzewa
     */
    public int obliczRozmiar() {
        return korzeń.obliczRozmiarPoddrzewa();
    }

    /**
     * @return wysokość drzewa
     */
    public int obliczWysokość() {
        return korzeń.obliczWysokość();
    }


    /**
     * @return suma elementów w drzewie
     */
    public T obliczSumęElementów() {
        return korzeń.suma(typ);
    }

    /**
     * Wypisuje zawartość drzewa w kolejności niemalejącej.
     */
    public void drukujZawartość() {
        korzeń.wypiszPoddrzewo();
        System.out.println();
    }

    /**
     * Wypisuje drzewo horyzontalnie.
     *
     * @param wcięcie liczba spacji dla pojedynczego wcięcia
     */
    public void drukujDrzewo(int wcięcie) {
        korzeń.wypiszPoddrzewoHoryzontalnie(0, wcięcie);
    }


}
