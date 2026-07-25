package graf.zdarzenia;

import graf.Węzeł;
import graf.sportowcy.Sportowiec;
import narzędzia.KonwersjaCzasu;

// Zdarzenie odpowiadające przybyciu sportowca do ośrodka.
public class PrzybycieSportowca extends Zdarzenie {
    private Sportowiec sportowiec;
    private Węzeł węzeł;

    public PrzybycieSportowca(Sportowiec s, Węzeł A, int t) {
        this.czas = t;
        this.sportowiec = s;
        this.węzeł = A;
    }

    @Override
    public void obsłuż(KolejkaZdarzeń q) {
        if (sportowiec.dajCzyŚledzony()) {
            System.out.printf("%s Sportowiec nr %d pojawił się w węźle nr %d.\n", KonwersjaCzasu.wypisz(czas), sportowiec.dajNumer(), węzeł.dajNumer());
        }
        if (czas < KonwersjaCzasu.doSekund("15:00:00")) {
            sportowiec.symuluj(węzeł, q, czas);
        }
    }
}
