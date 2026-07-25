package graf.zdarzenia;

import graf.krawedzie.Trasa;
import graf.sportowcy.Sportowiec;
import narzędzia.KonwersjaCzasu;

//Zdarzenie odpowiadajace początku zjazdu sportowca jakąś trasą.
public class PoczątekZjazdu extends Zdarzenie {
    private Sportowiec sportowiec;
    private Trasa trasa;

    public PoczątekZjazdu(Sportowiec sportowiec, Trasa trasa, int czas) {
        this.czas = czas;
        this.sportowiec = sportowiec;
        this.trasa = trasa;
    }

    @Override
    public void obsłuż(KolejkaZdarzeń q) {
        if (sportowiec.dajCzyŚledzony()) {
            System.out.printf("%s Sportowiec nr %d rozpoczął zjazd trasą nr %d.\n", KonwersjaCzasu.wypisz(czas), sportowiec.dajNumer(), trasa.dajNumer());
        }
        sportowiec.zjazd(trasa);
        Zdarzenie z = new KoniecZjazdu(sportowiec, trasa.dajWęzełKońcowy(), trasa, czas + trasa.dajCzasPrzejazdu());
        q.wstaw(z);
    }
}
