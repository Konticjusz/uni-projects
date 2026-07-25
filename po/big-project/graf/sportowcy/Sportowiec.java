package graf.sportowcy;

import graf.Węzeł;
import graf.krawedzie.Krawędź;
import graf.krawedzie.Trasa;
import graf.zdarzenia.KolejkaZdarzeń;
import narzędzia.Para;

import java.util.*;

public abstract class Sportowiec {
    static protected Random generator = new Random(1337);
    protected int poziomZaawansowania;
    protected double współczynnikSpontaniczności;
    protected double wspólczynnikZnudzenia;
    protected boolean czyŚledzony;
    protected double wagaDopasowania;
    protected double wagaWyrównania;
    protected double wagaZnudzenia;
    protected int numer;
    protected int czasPrzybycia;
    protected int liczbaZjazdów;
    protected Węzeł węzełStartowy;
    protected Map<Trasa, Para<Integer, Double>> znudzenieTrasą;
    protected int liczbaPrzejazdów;
    protected Map<Krawędź, List<Integer>> przejazdy;

    public Sportowiec(int numer, int poziomZaawansowania, double współczynnikSpontaniczności, double wspólczynnikZnudzenia, boolean czyŚledzony,
                      double wagaDopasowania, double wagaWyrównania, double wagaZnudzenia, int czasPrzybycia, Węzeł węzełStartowy) {
        this.poziomZaawansowania = poziomZaawansowania;
        this.współczynnikSpontaniczności = współczynnikSpontaniczności;
        this.wspólczynnikZnudzenia = wspólczynnikZnudzenia;
        this.czyŚledzony = czyŚledzony;
        this.wagaDopasowania = wagaDopasowania;
        this.wagaWyrównania = wagaWyrównania;
        this.wagaZnudzenia = wagaZnudzenia;
        this.numer = numer;
        this.czasPrzybycia = czasPrzybycia;
        this.węzełStartowy = węzełStartowy;
        this.znudzenieTrasą = new HashMap<>();
        this.liczbaZjazdów = 0;
        this.liczbaPrzejazdów = 0;
        this.przejazdy = new HashMap<>();
    }


    public int dajCzasPrzybycia() {
        return czasPrzybycia;
    }

    public Węzeł dajWęzełStartowy() {
        return węzełStartowy;
    }

    public boolean dajCzyŚledzony() {
        return czyŚledzony;
    }

    public double obliczAtrakcyjnosćTrasy(Trasa t) {

        double dopasowanie;

        if (t.dajTrudność() >= (poziomZaawansowania + 5)) {
            dopasowanie = 0;
        } else if ((poziomZaawansowania + 5) > t.dajTrudność() && t.dajTrudność() >= poziomZaawansowania) {
            dopasowanie = 1 - ((double) (t.dajTrudność() - poziomZaawansowania) / 5);

        } else {
            dopasowanie = Math.max(0.2, 1 - ((double) (poziomZaawansowania - t.dajTrudność()) / 7));
        }

        double wyrównanie;

        wyrównanie = t.dajBazowąAtrakcyjność() + (1 - t.dajBazowąAtrakcyjność()) * (Math.pow(t.dajOdporność(), t.dajLiczbęUżyc()));

        double znudzenie = 0;
        if (znudzenieTrasą.containsKey(t)) {
            znudzenie = znudzenieTrasą.get(t).drugi() * Math.pow((1 - wspólczynnikZnudzenia), liczbaZjazdów - znudzenieTrasą.get(t).pierwszy());
        }

        return (wagaDopasowania * dopasowanie) + (wagaWyrównania * wyrównanie) + (wagaZnudzenia * (1 - znudzenie));
    }


    public void zjazd(Trasa t) {
        double znudzenie = 0;
        if (znudzenieTrasą.containsKey(t)) {
            znudzenie = znudzenieTrasą.get(t).drugi() * Math.pow((1 - wspólczynnikZnudzenia), liczbaZjazdów - znudzenieTrasą.get(t).pierwszy());
        }
        znudzenie = wspólczynnikZnudzenia + (1 - wspólczynnikZnudzenia) * znudzenie;
        liczbaZjazdów++;
        znudzenieTrasą.put(t, new Para<>(liczbaZjazdów, znudzenie));
        zanotujPrzejazd(t);
    }


    public void podejmijSpontanicznąDecyzję(Węzeł węzeł, KolejkaZdarzeń q, int czas) {
        Krawędź[] krawędzieWychodzące = węzeł.dajKrawędzieWychodzące();
        int ktory = generator.nextInt(0, krawędzieWychodzące.length);
        krawędzieWychodzące[ktory].użyj(q, this, czas);
        return;
    }

    // Symuluje zachowanie s
    public abstract void symuluj(Węzeł węzeł, KolejkaZdarzeń q, int czas);


    public int dajNumer() {
        return numer;
    }


    public void zanotujPrzejazd(Krawędź krawędź) {
        liczbaPrzejazdów++;
        przejazdy.putIfAbsent(krawędź, new ArrayList<>());
        przejazdy.get(krawędź).add(liczbaPrzejazdów);
    }

    public List<Integer> dajListęPrzejazdów(Krawędź kra) {
        return przejazdy.getOrDefault(kra, List.of());
    }

}
