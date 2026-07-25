package Drzewo;

/**
 * Klasa implementująca Integer z metodą dodaj.
 */
public class WInteger implements Comparable<WInteger>, Dodawalny<WInteger> {
    private Integer wartość;


    /**
     * Konstruktor WIntegera o danej wartości.
     *
     * @param wartość
     */
    private WInteger(int wartość) {
        this.wartość = wartość;
    }

    /**
     * Konstruktor pustego WIntegera (wartość 0).
     */
    protected WInteger() {
        this.wartość = 0;
    }


    /**
     * Zwraca WInteger o danej wartości.
     *
     * @param wartość
     * @return WInteger
     */
    static public WInteger of(int wartość) {
        return new WInteger(wartość);
    }

    /**
     * Porównuje dwa WIntegery tak jak dwa Integery.
     *
     * @param other WInteger, z którym ma być porównany
     * @return wynik jak dla standardowego porównania Integerów
     */
    @Override
    public int compareTo(WInteger other) {
        return wartość.compareTo(other.wartość);
    }

    /**
     * Tworzy WInteger o sumie tych Wintegerów
     *
     * @param inny drugi Winteger
     * @return WInteger
     */
    @Override
    public WInteger dodaj(WInteger inny) {
        return new WInteger(this.wartość + inny.wartość);
    }

    /**
     * Zamienia WInteger na String.
     *
     * @return String
     */
    @Override
    public String toString() {
        return wartość.toString();
    }

    /**
     * Zamienia WInteger na Integer.
     *
     * @return Integer
     */
    public int toInt() {
        return this.wartość;
    }

}
