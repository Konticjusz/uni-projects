package graf.sportowcy;

import graf.Węzeł;
import graf.krawedzie.Krawędź;
import graf.krawedzie.Trasa;
import graf.zdarzenia.KolejkaZdarzeń;
import narzędzia.BFS;

import java.util.*;

public class Kolekcjoner extends Sportowiec {

    private Deque<Krawędź> ścieżka;
    private Map<Trasa, Integer> ileRazyZjeżdzał = new HashMap<>();

    public Kolekcjoner(int numer, int poziomZaawansowania, double współczynnikSpontaniczności, double wspołczynnikZnudzenia, boolean czyŚledzony,
                       double wagaDopasowania, double wagaWyrównania, double wagaZnudzenia, int czasPrzybycia, Węzeł węzełStartowy) {
        super(numer, poziomZaawansowania, współczynnikSpontaniczności, wspołczynnikZnudzenia, czyŚledzony, wagaDopasowania, wagaWyrównania, wagaZnudzenia, czasPrzybycia, węzełStartowy);
        this.ścieżka = new ArrayDeque<>();
    }

    @Override
    public void zjazd(Trasa t) {
        ileRazyZjeżdzał.put(t, ileRazyZjeżdzał.getOrDefault(t, 0) + 1);
        super.zjazd(t);
    }

    @Override
    public void symuluj(Węzeł węzeł, KolejkaZdarzeń q, int czas) {

        // Kontynuuje swój plan.
        if (!ścieżka.isEmpty()) {
            Krawędź następna = ścieżka.pop();
            następna.użyj(q, this, czas);
            return;
        }

        // Szansa na spontaniczną decyzję jeśli nie ma planu.
        double losowyDouble = generator.nextDouble(0, 1);
        if (losowyDouble < współczynnikSpontaniczności) {   // Wybiera losowo krawędź.
            podejmijSpontanicznąDecyzję(węzeł, q, czas);
            return;
        }

        // Wybieramy najlepszą trasę spośród wszysatkich w ośrodku i odzyskujemy do niej ściężkę z BFSa.

        BFS bfs = new BFS(węzeł);
        bfs.oblicz();
        List<Trasa> wszystkieTrasy = bfs.dajTrasy();

        Optional<Trasa> najlepsza = wszystkieTrasy.stream().min(Comparator.comparing((Trasa a) -> ileRazyZjeżdzał.getOrDefault(a, 0))
                .thenComparingInt(t -> bfs.odległość(t.dajWęzełPoczątkowy()))
                .thenComparing(Comparator.comparingDouble((Trasa trasa) -> trasa.obliczAtrakcyjność(this)).reversed()));
        if (najlepsza.isEmpty()) {
            throw new AssertionError("Brak najlepszych tras. Błąd w programie lub w danych wejściowych.");
        }
        ścieżka = bfs.dajŚcieżkęDo(najlepsza.get().dajWęzełPoczątkowy());
        ścieżka.add(najlepsza.get());
        ścieżka.pop().użyj(q, this, czas);
        return;
    }

}
