package graf.sportowcy;

import graf.Węzeł;
import graf.krawedzie.Krawędź;
import graf.zdarzenia.KolejkaZdarzeń;

// Wersja sportowca z 1 części zadania.
public class Lokalny extends Sportowiec {

    public Lokalny(int numer, int poziomZaawansowania, double współczynnikSpontaniczności, double wspołczynnikZnudzenia, boolean czyŚledzony,
                   double wagaDopasowania, double wagaWyrównania, double wagaZnudzenia, int czasPrzybycia, Węzeł węzełStartowy) {
        super(numer, poziomZaawansowania, współczynnikSpontaniczności, wspołczynnikZnudzenia, czyŚledzony, wagaDopasowania, wagaWyrównania, wagaZnudzenia, czasPrzybycia, węzełStartowy);
    }


    private Krawędź wybierzNajatrakcyjniejszą(Węzeł węzeł) {
        Krawędź[] krawędzieWychodzące = węzeł.dajKrawędzieWychodzące();
        Krawędź wybrana = null;
        double wartość = -1;
        for (int i = 0; i < krawędzieWychodzące.length; i++) {
            Krawędź k = krawędzieWychodzące[i];
            if (k.obliczAtrakcyjność(this) > wartość) {
                wartość = k.obliczAtrakcyjność(this);
                wybrana = k;
            }
        }
        return wybrana;
    }


    @Override
    public void symuluj(Węzeł węzeł, KolejkaZdarzeń q, int czas) {
        double losowyDouble = generator.nextDouble(0, 1);
        if (losowyDouble < współczynnikSpontaniczności) {
            podejmijSpontanicznąDecyzję(węzeł, q, czas);
            return;
        }
        Krawędź następna = wybierzNajatrakcyjniejszą(węzeł);
        następna.użyj(q, this, czas);
    }


}
