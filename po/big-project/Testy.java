import graf.Węzeł;
import graf.krawedzie.Krawędź;
import graf.krawedzie.Trasa;
import graf.krawedzie.Wyciąg;
import graf.sportowcy.Lokalny;
import graf.sportowcy.Sportowiec;
import graf.zdarzenia.KolejkaPriorytetowaZdarzeń;
import graf.zdarzenia.KolejkaZdarzeń;
import narzędzia.BFS;
import narzędzia.Para;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Testy {

    @Test
    void testWyciągu1() {
        Węzeł węzełA = new Węzeł(0, 0, 0, 0, false);
        Węzeł węzełB = new Węzeł(1, 0, 0, 0, false);
        Wyciąg wyciąg = new Wyciąg(0, węzełA, węzełB, 0, 3, 0);
        Sportowiec sportowiec1 = new Lokalny(0, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        Sportowiec sportowiec2 = new Lokalny(1, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        Sportowiec sportowiec3 = new Lokalny(2, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        Sportowiec sportowiec4 = new Lokalny(3, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        KolejkaZdarzeń q = new KolejkaPriorytetowaZdarzeń();
        wyciąg.użyj(q, sportowiec1, 0);
        wyciąg.użyj(q, sportowiec2, 0);
        wyciąg.użyj(q, sportowiec3, 0);
        wyciąg.użyj(q, sportowiec4, 0);
        wyciąg.przejazd(q, 0);
        assertEquals(3, wyciąg.dajLiczbęUżyc());
    }

    @Test
    void testyWyciągu2() {
        Węzeł węzełA = new Węzeł(0, 0, 0, 0, false);
        Węzeł węzełB = new Węzeł(1, 0, 0, 0, false);
        Wyciąg wyciąg = new Wyciąg(0, węzełA, węzełB, 0, 3, 0);
        Sportowiec sportowiec1 = new Lokalny(2, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        Sportowiec sportowiec2 = new Lokalny(3, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        KolejkaZdarzeń q = new KolejkaPriorytetowaZdarzeń();
        wyciąg.użyj(q, sportowiec1, 0);
        wyciąg.użyj(q, sportowiec2, 0);
        wyciąg.przejazd(q, 0);
        assertEquals(2, wyciąg.dajLiczbęUżyc());
    }

    @Test
    void testWyciągu4() {
        Węzeł węzełA = new Węzeł(0, 0, 0, 0, false);
        Węzeł węzełB = new Węzeł(1, 0, 0, 0, false);
        Wyciąg wyciąg = new Wyciąg(0, węzełA, węzełB, 0, 3, 0);
        Sportowiec sportowiec1 = new Lokalny(0, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        Sportowiec sportowiec2 = new Lokalny(1, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        Sportowiec sportowiec3 = new Lokalny(2, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        Sportowiec sportowiec4 = new Lokalny(3, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        KolejkaZdarzeń q = new KolejkaPriorytetowaZdarzeń();
        wyciąg.użyj(q, sportowiec1, 0);
        wyciąg.użyj(q, sportowiec2, 0);
        wyciąg.użyj(q, sportowiec3, 0);
        wyciąg.użyj(q, sportowiec4, 0);
        wyciąg.przejazd(q, 0);
        Sportowiec sportowiec5 = new Lokalny(4, 0, 0, 0, false, 0, 0, 0, 0, węzełA);
        wyciąg.użyj(q, sportowiec5, 0);
        assertEquals(4, wyciąg.dajMaxDługoścKolejki());
    }

    @Test
    void testBFS() {
        Węzeł[] węzły = new Węzeł[6];
        for (int i = 0; i < 6; i++) {
            węzły[i] = new Węzeł(i, 0, 0, 0, false);
        }
        List<Para<Integer, Integer>> gdzieTrasy = List.of(new Para<>(1, 0), new Para<>(1, 2), new Para<>(2, 0),
                new Para<>(3, 1), new Para<>(3, 4), new Para<>(5, 3), new Para<>(5, 3));
        List<Para<Integer, Integer>> gdzieWyciągi = List.of(new Para<>(0, 1), new Para<>(2, 3), new Para<>(2, 4), new Para<>(4, 5));

        Trasa[] trasy = new Trasa[gdzieTrasy.size()];
        Wyciąg[] wyciągi = new Wyciąg[gdzieWyciągi.size()];


        for (int i = 0; i < gdzieTrasy.size(); i++) {
            Para<Integer, Integer> trasa = gdzieTrasy.get(i);
            trasy[i] = new Trasa(i, węzły[trasa.pierwszy()], węzły[trasa.drugi()], 0, 0, 0, 0);
        }
        for (int i = 0; i < gdzieWyciągi.size(); i++) {
            Para<Integer, Integer> wyciąg = gdzieWyciągi.get(i);
            wyciągi[i] = new Wyciąg(i, węzły[wyciąg.pierwszy()], węzły[wyciąg.drugi()], 0, 0, 0);
        }

        BFS bfsOd0 = new BFS(węzły[0]);
        bfsOd0.oblicz();

        assertEquals(3, bfsOd0.odległość(węzły[4]));
        Deque<Krawędź> ścieżka = bfsOd0.dajŚcieżkęDo(węzły[4]);
        assertEquals(wyciągi[0], ścieżka.pop());
        assertEquals(trasy[1], ścieżka.pop());
        assertEquals(wyciągi[2], ścieżka.pop());

        BFS bfsOd3 = new BFS(węzły[3]);
        bfsOd3.oblicz();

        assertEquals(1, bfsOd3.odległość(węzły[1]));

        BFS bfsOd2 = new BFS(węzły[2]);
        bfsOd2.oblicz();

        assertEquals(0, bfsOd2.odległość(węzły[2]));

        BFS bfsOd4 = new BFS(węzły[4]);
        bfsOd4.oblicz();

        assertEquals(2, bfsOd4.odległość(węzły[3]));
        Deque<Krawędź> ścieżka2 = bfsOd4.dajŚcieżkęDo(węzły[3]);
        assertEquals(wyciągi[3], ścieżka2.pop());
        assertTrue(ścieżka2.getFirst().equals(trasy[5]) || ścieżka2.getFirst().equals(trasy[6]));


    }


}


