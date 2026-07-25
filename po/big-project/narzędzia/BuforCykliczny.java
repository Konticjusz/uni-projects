package narzędzia;

import graf.sportowcy.Sportowiec;

public class BuforCykliczny implements Kolejka {
    private Sportowiec[] sportowcy;
    private int pierwszy;
    private int ostatni;
    private int wielkosc;

    public BuforCykliczny() {
        sportowcy = new Sportowiec[5];
        pierwszy = 0;
        ostatni = 0;
        wielkosc = 0;
    }

    @Override
    public void wstaw(Sportowiec sportowiec) {
        if (sportowcy.length == wielkosc) {
            powieksz();
        }
        sportowcy[ostatni] = sportowiec;
        ostatni = (ostatni + 1) % sportowcy.length;
        wielkosc++;
    }

    @Override
    public Sportowiec zdejmij() {
        if (wielkosc == 0) {
            throw new AssertionError("Pusta kolejka");
        }
        Sportowiec wyn = sportowcy[pierwszy];
        sportowcy[pierwszy] = null;
        pierwszy = (pierwszy + 1) % sportowcy.length;
        wielkosc--;
        return wyn;
    }

    @Override
    public boolean czyPusta() {
        return wielkosc == 0;
    }

    private void powieksz() {
        Sportowiec[] tmp = new Sportowiec[sportowcy.length * 2];
        for (int i = 0; i < wielkosc; i++) {
            tmp[i] = sportowcy[(i + pierwszy) % sportowcy.length];
        }
        pierwszy = 0;
        ostatni = wielkosc;
        sportowcy = tmp;
    }

    @Override
    public int rozmiar() {
        return wielkosc;
    }
}
