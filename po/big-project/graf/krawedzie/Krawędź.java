package graf.krawedzie;

import graf.Węzeł;
import graf.sportowcy.Sportowiec;
import graf.zdarzenia.KolejkaZdarzeń;


public abstract class Krawędź {
    protected int liczbaUżyć;
    protected Węzeł węzełPoczątkowy;
    protected Węzeł węzełKońcowy;


    protected Krawędź(Węzeł węzełPoczątkowy, Węzeł węzełKońcowy) {
        liczbaUżyć = 0;
        this.węzełPoczątkowy = węzełPoczątkowy;
        this.węzełKońcowy = węzełKońcowy;
    }

    public Węzeł dajWęzełKońcowy() {
        return węzełKońcowy;
    }

    public Węzeł dajWęzełPoczątkowy() {
        return węzełPoczątkowy;
    }

    public int dajLiczbęUżyc() {
        return liczbaUżyć;
    }

    public abstract double obliczAtrakcyjność(Sportowiec sportowiec);

    public abstract void użyj(KolejkaZdarzeń q, Sportowiec sportowiec, int czas);

}
