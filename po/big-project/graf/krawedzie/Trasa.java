package graf.krawedzie;

import graf.Węzeł;
import graf.sportowcy.Sportowiec;
import graf.zdarzenia.KolejkaZdarzeń;
import graf.zdarzenia.PoczątekZjazdu;
import graf.zdarzenia.Zdarzenie;

import java.util.List;

public class Trasa extends Krawędź {
    private int trudność;
    private int czasPrzejazdu;
    private double bazowaAtrakcyjność;
    private double odporność;
    private int numer;


    public Trasa(int numer, Węzeł węzełPoczątkowy, Węzeł węzełKońcowy, int trudność, int czasPrzejazdu, double bazowaAtrakcyjność, double odporność) {
        super(węzełPoczątkowy, węzełKońcowy);
        this.trudność = trudność;
        this.czasPrzejazdu = czasPrzejazdu;
        this.bazowaAtrakcyjność = bazowaAtrakcyjność;
        this.odporność = odporność;
        węzełPoczątkowy.dodajTrasę(this);
        this.numer = numer;

    }

    @Override
    public double obliczAtrakcyjność(Sportowiec sportowiec) {
        return sportowiec.obliczAtrakcyjnosćTrasy(this);
    }


    public int dajTrudność() {
        return trudność;
    }

    public double dajOdporność() {
        return odporność;
    }

    public double dajBazowąAtrakcyjność() {
        return bazowaAtrakcyjność;
    }

    public int dajNumer() {
        return numer;
    }

    public int dajCzasPrzejazdu() {
        return czasPrzejazdu;
    }

    @Override
    public void użyj(KolejkaZdarzeń q, Sportowiec sportowiec, int czas) {
        liczbaUżyć++;
        Zdarzenie z = new PoczątekZjazdu(sportowiec, this, czas);
        q.wstaw(z);
    }

    // Napisy dla 1 mapki
    public List<String> dajNapisA() {
        return List.of(String.format("t%d: poziom: %d, czas: %ds", numer, trudność, czasPrzejazdu),
                String.format("odporność: %.2f, %.5f", bazowaAtrakcyjność, odporność));
    }

    // Napisy dla 2 mapki
    public List<String> dajNapisB() {
        return List.of(String.format("t%d: śnieg: %.2f", numer, bazowaAtrakcyjność + (1 - bazowaAtrakcyjność) * (Math.pow(odporność, liczbaUżyć))),
                String.format("zjazdy: %d", liczbaUżyć));
    }

    //Napis dla mapki danego sportowca
    public String dajNapisC(Sportowiec s) {
        List<Integer> przejazdy = s.dajListęPrzejazdów(this);
        return String.format("t%d(%d): %s", numer, przejazdy.size(), przejazdy.stream().map(String::valueOf).reduce((a, b) -> a + ", " + b).orElse(""));
    }
}
