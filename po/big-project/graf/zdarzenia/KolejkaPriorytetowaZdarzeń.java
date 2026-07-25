package graf.zdarzenia;

import narzędzia.Para;

import java.util.Comparator;
import java.util.PriorityQueue;

public class KolejkaPriorytetowaZdarzeń implements KolejkaZdarzeń {

    PriorityQueue<Para<Zdarzenie, Integer>> kolejka;

    Integer ostatniNumerPorządkowy;

    public KolejkaPriorytetowaZdarzeń() {
        kolejka = new PriorityQueue<>(Comparator.comparing((Para<Zdarzenie, Integer> a) -> a.pierwszy().dajCzas()).thenComparing(Para::drugi));
        ostatniNumerPorządkowy = -1;
    }


    @Override
    public void wstaw(Zdarzenie z) {
        ostatniNumerPorządkowy++;
        kolejka.add(new Para<>(z, ostatniNumerPorządkowy));
    }

    @Override
    public Zdarzenie zdejmij() {
        return kolejka.remove().pierwszy();
    }

    @Override
    public boolean czyPusta() {
        return kolejka.isEmpty();
    }


}
