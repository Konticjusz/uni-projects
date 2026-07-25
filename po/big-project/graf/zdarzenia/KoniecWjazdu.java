package graf.zdarzenia;

import graf.Węzeł;
import graf.krawedzie.Wyciąg;
import graf.sportowcy.Sportowiec;
import narzędzia.KonwersjaCzasu;

// Zdarzenie odpowiadające końcu wjazdu wyciągiem dla sportowca.
public class KoniecWjazdu extends Zdarzenie {
    private Sportowiec sportowiec;
    private Węzeł węzeł;
    private Wyciąg wyciąg;

    public KoniecWjazdu(Sportowiec s, Węzeł A, Wyciąg wyciąg, int t) {
        this.czas = t;
        this.sportowiec = s;
        this.węzeł = A;
        this.wyciąg = wyciąg;
    }

    @Override
    public void obsłuż(KolejkaZdarzeń q) {
        if (sportowiec.dajCzyŚledzony()) {
            System.out.printf("%s Sportowiec nr %d zakończył wjazd wyciągiem nr %d i pojawił się w węźle nr %d.\n",
                    KonwersjaCzasu.wypisz(czas), sportowiec.dajNumer(), wyciąg.dajNumer(), węzeł.dajNumer());
        }
        if (czas < KonwersjaCzasu.doSekund("15:00:00")) {
            sportowiec.symuluj(węzeł, q, czas);
        }
    }
}
