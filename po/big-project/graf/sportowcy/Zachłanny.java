package graf.sportowcy;

import graf.Węzeł;
import graf.krawedzie.Krawędź;
import graf.krawedzie.Trasa;
import graf.zdarzenia.KolejkaZdarzeń;
import narzędzia.BFS;

import java.util.*;

public class Zachłanny extends Sportowiec {

    private Deque<Krawędź> ścieżka;

    public Zachłanny(int numer, int poziomZaawansowania, double współczynnikSpontaniczności, double wspołczynnikZnudzenia, boolean czyŚledzony,
                     double wagaDopasowania, double wagaWyrównania, double wagaZnudzenia, int czasPrzybycia, Węzeł węzełStartowy) {
        super(numer, poziomZaawansowania, współczynnikSpontaniczności, wspołczynnikZnudzenia, czyŚledzony, wagaDopasowania, wagaWyrównania, wagaZnudzenia, czasPrzybycia, węzełStartowy);
        this.ścieżka = new ArrayDeque<>();
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
        Optional<Trasa> najlepsza = wszystkieTrasy.stream().max(Comparator.comparing(a -> a.obliczAtrakcyjność(this)));
        if (najlepsza.isEmpty()) {
            throw new AssertionError("Nie ma żadnej trasy, niepoprawne dane wejściowe.");
        }
        ścieżka = bfs.dajŚcieżkęDo(najlepsza.get().dajWęzełPoczątkowy());
        ścieżka.add(najlepsza.get());
        ścieżka.pop().użyj(q, this, czas);
        return;
    }

}
