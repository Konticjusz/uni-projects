package graf.krawedzie;

import graf.Węzeł;
import graf.sportowcy.Sportowiec;
import graf.zdarzenia.KolejkaZdarzeń;
import graf.zdarzenia.KoniecWjazdu;
import graf.zdarzenia.PrzejazdWyciągu;
import graf.zdarzenia.Zdarzenie;
import narzędzia.BuforCykliczny;
import narzędzia.Kolejka;
import narzędzia.KonwersjaCzasu;

import java.util.List;
import java.util.Optional;

public class Wyciąg extends Krawędź {
    private int odstępCzasowy;
    private int wielkośćGrupy;
    private int czasPrzejazdu;
    private Kolejka kolejka;
    private int numer = 0;
    private int maksymalnaDługośćKolejki = 0;
    private long sumaDlugosciKolejek = 0;
    private long maksymalnaPrzepustowość = 0;
    private int czasOstatniejOperacji = 0;


    public Wyciąg(int numer, Węzeł węzełPoczątkowy, Węzeł węzełKońcowy, int odstępCzasowy, int wielkośćGrupy, int czasPrzejazdu) {
        super(węzełPoczątkowy, węzełKońcowy);
        this.odstępCzasowy = odstępCzasowy;
        this.wielkośćGrupy = wielkośćGrupy;
        this.czasPrzejazdu = czasPrzejazdu;
        this.kolejka = new BuforCykliczny();
        węzełPoczątkowy.dodajWyciąg(this);
        this.numer = numer;
    }

    // Atrakcyjność wyciągu obliczamy jako max atrakcyjność tras wychodzących z jego końca.
    @Override
    public double obliczAtrakcyjność(Sportowiec sportowiec) {
        Optional<Trasa> najlepsza = węzełKońcowy.najatrakcyjniejszaTrasa(sportowiec);
        return najlepsza.map(trasa -> trasa.obliczAtrakcyjność(sportowiec)).orElse(-1.0);
    }

    @Override
    public void użyj(KolejkaZdarzeń q, Sportowiec sportowiec, int czas) {
        if (sportowiec.dajCzyŚledzony()) {
            System.out.printf("%s Sportowiec nr %d ustawił się w kolejce do wyciągu nr %d.\n", KonwersjaCzasu.wypisz(czas), sportowiec.dajNumer(), this.dajNumer());
        }
        sumaDlugosciKolejek += (long) (czas - czasOstatniejOperacji) * kolejka.rozmiar();
        kolejka.wstaw(sportowiec);
        maksymalnaDługośćKolejki = Math.max(maksymalnaDługośćKolejki, kolejka.rozmiar());
        czasOstatniejOperacji = czas;
    }

    public int dajNumer() {
        return numer;
    }

    public void przejazd(KolejkaZdarzeń q, int czas) {
        int miejsca = 0;
        Sportowiec[] pasażerowie = new Sportowiec[wielkośćGrupy];
        sumaDlugosciKolejek += (long) (czas - czasOstatniejOperacji) * kolejka.rozmiar();
        czasOstatniejOperacji = czas;
        maksymalnaPrzepustowość += wielkośćGrupy;
        while (!kolejka.czyPusta() && miejsca < wielkośćGrupy) {
            pasażerowie[miejsca] = kolejka.zdejmij();
            if (pasażerowie[miejsca].dajCzyŚledzony()) {
                System.out.printf("%s Sportowiec nr %d rozpoczął wjazd wyciągiem nr %d.\n", KonwersjaCzasu.wypisz(czas), pasażerowie[miejsca].dajNumer(), this.dajNumer());
            }
            pasażerowie[miejsca].zanotujPrzejazd(this);
            Zdarzenie z = new KoniecWjazdu(pasażerowie[miejsca], dajWęzełKońcowy(), this, czas + czasPrzejazdu);
            q.wstaw(z);
            miejsca++;
            liczbaUżyć++;
        }
        if ((czas + odstępCzasowy) < KonwersjaCzasu.doSekund("15:00:00")) {
            Zdarzenie z = new PrzejazdWyciągu(czas + odstępCzasowy, this);
            q.wstaw(z);
        }
    }

    public int dajMaxDługoścKolejki() {
        maksymalnaDługośćKolejki = Math.max(maksymalnaDługośćKolejki, kolejka.rozmiar());
        return maksymalnaDługośćKolejki;
    }

    public double dajŚredniąDługośćKolejki() {
        long suma = sumaDlugosciKolejek + (long) (KonwersjaCzasu.doSekund("15:00:00") - czasOstatniejOperacji) * kolejka.rozmiar();
        return (double) suma / (KonwersjaCzasu.doSekund("15:00:00") - KonwersjaCzasu.doSekund("09:00:00"));
    }

    public long dajMaxPrzepustowość() {
        return maksymalnaPrzepustowość;
    }

    // Napisy dla 1 mapki
    public List<String> dajNapisA() {
        return List.of(String.format("w%d: %d os. co %ds", numer, wielkośćGrupy, odstępCzasowy),
                String.format("czas: %ds", czasPrzejazdu));
    }

    // Napisy dla 2 mapki
    public List<String> dajNapisB() {
        double procent = maksymalnaPrzepustowość != 0 ? (double) (100 * liczbaUżyć) / maksymalnaPrzepustowość : 0;
        return List.of(String.format("w%d: kol: %.0f(śr), %d(maks)", numer, dajŚredniąDługośćKolejki(), dajMaxDługoścKolejki()),
                String.format("wjazdy: %d / %d (%.0f%%)", liczbaUżyć, maksymalnaPrzepustowość, procent));
    }


    //Napis dla mapki danego sportowca
    public String dajNapisC(Sportowiec s) {
        List<Integer> przejazdy = s.dajListęPrzejazdów(this);
        return String.format("w%d(%d): %s", numer, przejazdy.size(), przejazdy.stream().map(String::valueOf).reduce((a, b) -> a + ", " + b).orElse(""));

    }
}
