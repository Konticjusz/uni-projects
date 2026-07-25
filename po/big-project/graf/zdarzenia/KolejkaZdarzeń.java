package graf.zdarzenia;

public interface KolejkaZdarzeń {
    void wstaw(Zdarzenie z);

    Zdarzenie zdejmij();

    boolean czyPusta();
}
