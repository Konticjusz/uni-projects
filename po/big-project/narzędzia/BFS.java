package narzędzia;

import graf.Węzeł;
import graf.krawedzie.Krawędź;
import graf.krawedzie.Trasa;

import java.util.*;

public class BFS {

    private Węzeł węzełStartowy;
    private Map<Węzeł, Integer> odleglosci;
    private Map<Węzeł, Krawędź> poprzednia;
    private List<Trasa> trasy;

    public BFS(Węzeł w) {
        węzełStartowy = w;
        odleglosci = new HashMap<>();
        poprzednia = new HashMap<>();
        trasy = new ArrayList<>();
    }

    public List<Trasa> dajTrasy() {
        return trasy;
    }

    public int odległość(Węzeł węzeł) {
        return odleglosci.get(węzeł);
    }

    public Deque<Krawędź> dajŚcieżkęDo(Węzeł w) {
        Deque<Krawędź> ścieżka = new ArrayDeque<>();
        while (poprzednia.containsKey(w)) {
            Krawędź k = poprzednia.get(w);
            ścieżka.addFirst(k);
            w = k.dajWęzełPoczątkowy();
        }
        return ścieżka;
    }

    public void oblicz() {
        odleglosci.put(węzełStartowy, 0);
        Deque<Węzeł> kolejka = new ArrayDeque<>();
        kolejka.add(węzełStartowy);
        while (!kolejka.isEmpty()) {
            Węzeł v = kolejka.removeFirst();
            trasy.addAll(List.of(v.dajTrasy()));
            for (Krawędź krawędź : v.dajKrawędzieWychodzące()) {
                if (!odleglosci.containsKey(krawędź.dajWęzełKońcowy())) {
                    odleglosci.put(krawędź.dajWęzełKońcowy(), odleglosci.get(v) + 1);
                    poprzednia.put(krawędź.dajWęzełKońcowy(), krawędź);
                    kolejka.add(krawędź.dajWęzełKońcowy());
                }
            }
        }
    }


}
