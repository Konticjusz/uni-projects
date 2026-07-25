package Drzewo;

/**
 * Klasa implementująca wierzchołek drzewa BST.
 *
 * @param <T> typ wartości
 */
public class Wierzchołek<T extends Comparable<T> & Dodawalny<T>> extends Węzeł<T> {
    private T wartość;
    private Węzeł<T> lewy, prawy;


    Wierzchołek(T wartosć, Wartownik<T> wartownik) {
        this.wartość = wartosć;
        this.lewy = wartownik;
        this.prawy = wartownik;
    }

    /**
     * Buduje reprezentację wierzchołka jako jego wartość i połączenie reprezentacji jego lewego i prawego poddrzewa.
     *
     * @return String z reprezentacją wierzchołka
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(wartość.toString());
        sb.append(" (L: ");
        sb.append(lewy.toString());
        sb.append(", P: ");
        sb.append(prawy.toString());
        sb.append(")");
        return sb.toString();
    }

    /**
     * Sprawdza czy wartość jest niewiększa od wartości w aktualnym węźle i rekurencyjnie wstawia element w odpowiednim miejscu drzewa.
     *
     * @param inna element do wstawienia
     * @return węzeł z wstawionym elementem
     */
    @Override
    protected Węzeł<T> wstaw(T inna) {
        if (wartość.compareTo(inna) <= 0) {
            this.prawy = prawy.wstaw(inna);
        } else {
            this.lewy = lewy.wstaw(inna);
        }
        return this;
    }

    /**
     * Sprawdza czy wartość jest równa aktualnej, jeśli tak to zwraca aktualny wierzchołek, w.p.p wywołuje się rekurencyjnie.
     *
     * @param inna wartość szukanego elementu
     * @return węzeł z szukanym elementem, ew. wartownik
     */
    @Override
    protected Węzeł<T> znajdź(T inna) {
        if (wartość.compareTo(inna) == 0) {
            return this;
        } else if (wartość.compareTo(inna) < 0) {
            return prawy.znajdź(inna);
        } else {
            return lewy.znajdź(inna);
        }
    }

    /**
     *
     * @return rozmiar poddrzewa aktualnego wierzchołka
     */
    @Override
    protected int obliczRozmiarPoddrzewa() {
        return 1 + lewy.obliczRozmiarPoddrzewa() + prawy.obliczRozmiarPoddrzewa();
    }

    /**
     *
     * @return wysokość poddrzewa aktualnego wierzchołka
     */
    @Override
    protected int obliczWysokość() {
        return 1 + Math.max(lewy.obliczWysokość(), prawy.obliczWysokość());
    }

    /**
     * Wypisuje rekurencyjnie poddrzewo aktualnego wiezchołka na standardowe wyjście.
     */
    @Override
    protected void wypiszPoddrzewo() {
        lewy.wypiszPoddrzewo();
        System.out.printf("%s ", wartość.toString());
        prawy.wypiszPoddrzewo();
    }

    /**
     * Wypisuje horyzontalnie poddrzewo w sposób rekurencyjny na standardowe wyjście.
     *
     * @param głębokość głębkość tego węzła
     * @param wcięcie   liczba spacji w pojedynczym wcięciu
     */
    @Override
    protected void wypiszPoddrzewoHoryzontalnie(int głębokość, int wcięcie) {
        lewy.wypiszPoddrzewoHoryzontalnie(głębokość + 1, wcięcie);
        System.out.print(" ".repeat(głębokość * wcięcie));
        System.out.println(wartość.toString());
        prawy.wypiszPoddrzewoHoryzontalnie(głębokość + 1, wcięcie);
    }

    /**
     *
     * @param typ typ elementu
     * @return suma elementów w poddrzewie
     */
    @Override
    protected T suma(Class<T> typ) {
        return wartość.dodaj(lewy.suma(typ)).dodaj(prawy.suma(typ));
    }
}
