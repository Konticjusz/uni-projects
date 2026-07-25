package graf.zdarzenia;

public abstract class Zdarzenie {
    protected int czas;

    public abstract void obsłuż(KolejkaZdarzeń q);

    public int dajCzas() {
        return czas;
    }

}
