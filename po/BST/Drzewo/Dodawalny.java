package Drzewo;

/**
 * Interfejs dla typów dodawalnych.
 *
 * @param <T> typ wartości, które są dodawane
 */
public interface Dodawalny<T> {
    /**
     * Zwraca wartość sumy tych elementów (kolejność aktualny element + inny element).
     *
     * @param inny
     * @return suma
     */
    T dodaj(T inny);
}
