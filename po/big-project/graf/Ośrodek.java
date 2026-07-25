package graf;

import graf.krawedzie.Trasa;
import graf.krawedzie.Wyciąg;
import graf.sportowcy.Sportowiec;
import graf.zdarzenia.*;
import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import kadra.mapki.styl.GruboscKonturu;
import kadra.mapki.styl.StylKrawedzi;
import kadra.mapki.styl.StylLinii;
import kadra.mapki.styl.StylWezla;
import narzędzia.KonwersjaCzasu;

// Ta klasa odpowiada ośrodkowi z treści zadania.
public class Ośrodek {
    private KolejkaZdarzeń kolejka;
    private Węzeł[] węzły;
    private Wyciąg[] wyciągi;
    private Trasa[] trasy;
    private Sportowiec[] sportowcy;
    private int ostatniaOperacja = 0;

    public Ośrodek(Węzeł[] węzły, Wyciąg[] wyciągi, Trasa[] trasy, Sportowiec[] sportowcy) {
        this.węzły = węzły;
        this.wyciągi = wyciągi;
        this.trasy = trasy;
        this.sportowcy = sportowcy;
        kolejka = new KolejkaPriorytetowaZdarzeń();
    }

    public void symuluj() {
        for (Wyciąg w : wyciągi) {      // Wrzuca na kolejkę wszystkie wyciągi.
            kolejka.wstaw(new PrzejazdWyciągu(KonwersjaCzasu.doSekund("09:00:00"), w));
        }
        for (Sportowiec sportowiec : sportowcy) {   // Wrzuca na kolejkę wszystkich sportowców.
            kolejka.wstaw(new PrzybycieSportowca(sportowiec, sportowiec.dajWęzełStartowy(), sportowiec.dajCzasPrzybycia()));
        }

        while (!kolejka.czyPusta()) {   // Symuluje, póki kolejka nie jest pusta.
            Zdarzenie z = kolejka.zdejmij();
            z.obsłuż(kolejka);
        }


    }

    public void wygenerujStatystyki() {
        for (Wyciąg w : wyciągi) {
            System.out.printf("Statystki wyciągu nr %d\n", w.dajNumer());
            System.out.printf("-> Maksymalna długość kolejki: %d\n", w.dajMaxDługoścKolejki());
            System.out.printf("-> Średnia długość kolejki: %f\n", w.dajŚredniąDługośćKolejki());
            System.out.printf("-> Łączna liczba przewiezionych pasażerów: %d\n", w.dajLiczbęUżyc());
            double procent = w.dajMaxPrzepustowość() != 0 ? (double) w.dajLiczbęUżyc() / w.dajMaxPrzepustowość() : 0;
            System.out.printf("-> Procent zajętych miejsc: %f\n", procent);
        }

        for (Trasa t : trasy) {
            System.out.printf("Trasą %d przejechało łącznie %d sportowców i jej wyrównanie na koniec dnia wynosi %f.\n",
                    t.dajNumer(), t.dajLiczbęUżyc(), t.dajBazowąAtrakcyjność() + ((1 - t.dajBazowąAtrakcyjność()) * Math.pow(t.dajOdporność(), t.dajLiczbęUżyc())));
        }

    }


    public void wygenerujWizualizacje(String ścieżkaDoKatologuMapek) throws WyjatekSystemuPlikow {
        GeneratorMapek generatorMapek = new GeneratorMapek(ścieżkaDoKatologuMapek);
        // Mapka 1
        for (Węzeł w : węzły) {
            generatorMapek.dodajWezel(w.dajNumer(), w.dajX(), w.dajY(), new StylWezla(w.czySkomunikowany() ? GruboscKonturu.POGRUBIONY : GruboscKonturu.ZWYKLY));
        }

        for (Trasa t : trasy) {
            generatorMapek.dodajKrawedz(t.dajWęzełPoczątkowy().dajNumer(), t.dajWęzełKońcowy().dajNumer(), new StylKrawedzi(StylLinii.CIAGLA), t.dajNapisA());
        }

        for (Wyciąg w : wyciągi) {
            generatorMapek.dodajKrawedz(w.dajWęzełPoczątkowy().dajNumer(), w.dajWęzełKońcowy().dajNumer(), new StylKrawedzi(StylLinii.PRZERYWANA), w.dajNapisA());
        }
        generatorMapek.tworzMapke("mapa1.tex");

        //Mapka 2
        generatorMapek.zeruj();
        for (Węzeł w : węzły) {
            generatorMapek.dodajWezel(w.dajNumer(), w.dajX(), w.dajY(), new StylWezla(w.czySkomunikowany() ? GruboscKonturu.POGRUBIONY : GruboscKonturu.ZWYKLY));
        }

        for (Trasa t : trasy) {
            generatorMapek.dodajKrawedz(t.dajWęzełPoczątkowy().dajNumer(), t.dajWęzełKońcowy().dajNumer(), new StylKrawedzi(StylLinii.CIAGLA), t.dajNapisB());
        }

        for (Wyciąg w : wyciągi) {
            generatorMapek.dodajKrawedz(w.dajWęzełPoczątkowy().dajNumer(), w.dajWęzełKońcowy().dajNumer(), new StylKrawedzi(StylLinii.PRZERYWANA), w.dajNapisB());
        }
        generatorMapek.tworzMapke("mapa2.tex");

        // Mapki dla sportowców

        for (Sportowiec s : sportowcy) {
            if (!s.dajCzyŚledzony()) continue;
            generatorMapek.zeruj();

            for (Węzeł w : węzły) {
                generatorMapek.dodajWezel(w.dajNumer(), w.dajX(), w.dajY(), new StylWezla(w.czySkomunikowany() ? GruboscKonturu.POGRUBIONY : GruboscKonturu.ZWYKLY));
            }

            for (Trasa t : trasy) {
                generatorMapek.dodajKrawedz(t.dajWęzełPoczątkowy().dajNumer(), t.dajWęzełKońcowy().dajNumer(), new StylKrawedzi(StylLinii.CIAGLA), t.dajNapisC(s));
            }

            for (Wyciąg w : wyciągi) {
                generatorMapek.dodajKrawedz(w.dajWęzełPoczątkowy().dajNumer(), w.dajWęzełKońcowy().dajNumer(), new StylKrawedzi(StylLinii.PRZERYWANA), w.dajNapisC(s));
            }

            generatorMapek.tworzMapke(String.format("mapaSportowcaNr%d.tex", s.dajNumer()));


        }


    }
}
