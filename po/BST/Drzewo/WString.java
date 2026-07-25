package Drzewo;

/**
 * Klasa implementująca String z metodą dodaj.
 */
public class WString implements Comparable<WString>, Dodawalny<WString> {
    private String wartość;

    /**
     * Konstruktor pustego WStringa.
     */
    protected WString() {
        wartość = "";
    }

    /**
     * Konstruktor WStringa z podanym napisem.
     *
     * @param string
     */
    private WString(String string) {
        this.wartość = string;
    }

    /**
     * Tworzy WString z podanym napisem.
     *
     * @param string napis
     * @return nowy WString
     */
    public static WString of(String string) {
        return new WString(string);
    }

    /**
     * Zwraca napis.
     *
     * @return
     */
    @Override
    public String toString() {
        return wartość;
    }

    /**
     * Porównuje dwa WStringi.
     *
     * @param o WString z którym ma być porównany
     * @return wynik taki jak dla standardowego porównania Stringów
     */
    @Override
    public int compareTo(WString o) {
        return wartość.compareTo(o.wartość);
    }

    /**
     * Dodaje dwa WStringi.
     *
     * @param inny drugi WString
     * @return połączenie dwóch WStringów
     */
    @Override
    public WString dodaj(WString inny) {
        StringBuilder sb = new StringBuilder(wartość);
        sb.append(inny.wartość);
        return new WString(sb.toString());
    }


}
