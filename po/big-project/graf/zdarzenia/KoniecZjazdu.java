package graf.zdarzenia;

import graf.Węzeł;
import graf.krawedzie.Trasa;
import graf.sportowcy.Sportowiec;
import narzędzia.KonwersjaCzasu;

// Zdarzeniec odpowiadające końcu zjazdu trasą dla sportowca.
public class KoniecZjazdu extends Zdarzenie {
    private Sportowiec sportowiec;
    private Węzeł węzeł;
    private Trasa trasa;

    public KoniecZjazdu(Sportowiec s, Węzeł A, Trasa trasa, int t) {
        this.czas = t;
        this.sportowiec = s;
        this.trasa = trasa;
        this.węzeł = A;
    }

    @Override
    public void obsłuż(KolejkaZdarzeń q) {
        if (sportowiec.dajCzyŚledzony()) {
            System.out.printf("%s Sportowiec nr %d zakończył zjazd trasą nr %d i pojawił się w węźle nr %d.\n",
                    KonwersjaCzasu.wypisz(czas), sportowiec.dajNumer(), trasa.dajNumer(), węzeł.dajNumer());
        }
        if (czas < KonwersjaCzasu.doSekund("15:00:00")) {
            sportowiec.symuluj(węzeł, q, czas);
        }
    }
}
