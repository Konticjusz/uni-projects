package graf.zdarzenia;

import graf.krawedzie.Wyciąg;

// Zdarzenie przejazdu wyciągiem (odbywa się cyklicznie co jakiś odstęp czasu).
public class PrzejazdWyciągu extends Zdarzenie {
    private Wyciąg w;

    public PrzejazdWyciągu(int czas, Wyciąg w) {
        this.czas = czas;
        this.w = w;
    }

    @Override
    public void obsłuż(KolejkaZdarzeń q) {
        w.przejazd(q, czas);
    }
}
