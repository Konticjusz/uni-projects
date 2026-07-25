package narzędzia;

import graf.sportowcy.Sportowiec;

public interface Kolejka {
    void wstaw(Sportowiec sportowiec);

    Sportowiec zdejmij();

    int rozmiar();

    boolean czyPusta();
}
