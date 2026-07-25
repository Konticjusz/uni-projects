package narzędzia;

import graf.Węzeł;
import graf.krawedzie.Trasa;
import graf.krawedzie.Wyciąg;
import graf.sportowcy.Kolekcjoner;
import graf.sportowcy.Lokalny;
import graf.sportowcy.Sportowiec;
import graf.sportowcy.Zachłanny;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Klasa wczytująca dane używając przekazany skaner.
public class Wczytywacz {

    private Scanner skaner;
    private Węzeł[] węzły;
    private Wyciąg[] wyciągi;
    private Trasa[] trasy;
    private Sportowiec[] sportowcy;

    public Wczytywacz(Scanner skaner) {
        this.skaner = skaner;
    }

    public void wczytajDane() {
        wczytajWęzły();
        wczytajWyciągi();
        wczytajTrasy();
        wczyytajSportowców();
    }

    public Węzeł[] dajWęzły() {
        return węzły;
    }

    public Wyciąg[] dajWyciągi() {
        return wyciągi;
    }

    public Trasa[] dajTrasy() {
        return trasy;
    }

    public Sportowiec[] dajSportowców() {
        return sportowcy;
    }

    private void wczytajWęzły() {
        int liczbaWęzłów = skaner.nextInt();
        węzły = new Węzeł[liczbaWęzłów];
        for (int i = 0; i < liczbaWęzłów; i++) {
            int wysokość = skaner.nextInt();
            int x = skaner.nextInt();
            int y = skaner.nextInt();
            boolean czySkomunikowany = skaner.findInLine("s") != null;
            Węzeł w = new Węzeł(i, wysokość, x, y, czySkomunikowany);
            węzły[i] = w;
        }
    }

    private void wczytajWyciągi() {
        int liczbaWyciągów = skaner.nextInt();
        wyciągi = new Wyciąg[liczbaWyciągów];
        for (int i = 0; i < liczbaWyciągów; i++) {
            int wezelA = skaner.nextInt();
            int wezelB = skaner.nextInt();
            int odstęp = skaner.nextInt();
            int wielkosć = skaner.nextInt();
            int czas = skaner.nextInt();
            Wyciąg w = new Wyciąg(i, węzły[wezelA], węzły[wezelB], odstęp, wielkosć, czas);
            wyciągi[i] = w;
        }
    }

    private void wczytajTrasy() {
        int liczbaTras = skaner.nextInt();
        trasy = new Trasa[liczbaTras];
        for (int i = 0; i < liczbaTras; i++) {
            int wezelA = skaner.nextInt();
            int wezelB = skaner.nextInt();
            int trudność = skaner.nextInt();
            int czas = skaner.nextInt();
            double bazowa = skaner.nextDouble();
            double odporność = skaner.nextDouble();
            Trasa t = new Trasa(i, węzły[wezelA], węzły[wezelB], trudność, czas, bazowa, odporność);
            trasy[i] = t;
        }
    }

    private void wczyytajSportowców() {
        List<Sportowiec> sportowcyDoDodania = new ArrayList<>();
        // Wczytywanie sportowców.
        int liczbaGrupSportowców = skaner.nextInt();

        for (int i = 0; i < liczbaGrupSportowców; i++) {
            int liczbaSportowcówWGrupie = skaner.nextInt();
            int poziomZaawansowania = skaner.nextInt();
            double współczynnikSpontaniczności = skaner.nextDouble();
            double wspólczynnikZnudzenia = skaner.nextDouble();
            char rodzajSportowca = skaner.next().charAt(0);
            boolean czyŚledzeni = skaner.findInLine("s") != null;
            double wagaDopasowania = skaner.nextDouble();
            double wagaWyrównania = skaner.nextDouble();
            double wagaZnudzenia = skaner.nextDouble();
            int węzełStart = skaner.nextInt();
            String godzinarozpoczecia = skaner.next();
            int t = KonwersjaCzasu.doSekund(godzinarozpoczecia);
            int odstep = 0;
            if (liczbaSportowcówWGrupie > 1) {
                odstep = skaner.nextInt();
            }
            for (int j = 0; j < liczbaSportowcówWGrupie; j++) {
                if (rodzajSportowca == 'L') {
                    sportowcyDoDodania.add(new Lokalny(sportowcyDoDodania.size(), poziomZaawansowania, współczynnikSpontaniczności, wspólczynnikZnudzenia, czyŚledzeni, wagaDopasowania, wagaWyrównania, wagaZnudzenia, t + j * odstep, węzły[węzełStart]));
                }
                if (rodzajSportowca == 'Z') {
                    sportowcyDoDodania.add(new Zachłanny(sportowcyDoDodania.size(), poziomZaawansowania, współczynnikSpontaniczności, wspólczynnikZnudzenia, czyŚledzeni, wagaDopasowania, wagaWyrównania, wagaZnudzenia, t + j * odstep, węzły[węzełStart]));
                }
                if (rodzajSportowca == 'K') {
                    sportowcyDoDodania.add(new Kolekcjoner(sportowcyDoDodania.size(), poziomZaawansowania, współczynnikSpontaniczności, wspólczynnikZnudzenia, czyŚledzeni, wagaDopasowania, wagaWyrównania, wagaZnudzenia, t + j * odstep, węzły[węzełStart]));
                }
            }
        }
        sportowcy = sportowcyDoDodania.toArray(Sportowiec[]::new);

    }

}


