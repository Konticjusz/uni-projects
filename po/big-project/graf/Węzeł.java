package graf;

import graf.krawedzie.Krawędź;
import graf.krawedzie.Trasa;
import graf.krawedzie.Wyciąg;
import graf.sportowcy.Sportowiec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Węzeł {
    private int wysokość;
    private boolean czySkomunikowany;
    private int x;
    private int y;
    private List<Trasa> trasy;
    private List<Wyciąg> wyciągi;
    private int liczbaWychodzącychTras;
    private int liczbaWychodzącychWyciągów;
    private int numer;

    public Węzeł(int numer, int wysokość, int x, int y, boolean czySkomunikowany) {
        this.wysokość = wysokość;
        this.x = x;
        this.y = y;
        this.czySkomunikowany = czySkomunikowany;

        trasy = new ArrayList<>();
        wyciągi = new ArrayList<>();
        liczbaWychodzącychTras = 0;
        liczbaWychodzącychWyciągów = 0;
        this.numer = numer;

    }

    // Zwraca wartość najatrakcyjniejszej trasy zaczynającej się w tym węźle dla przekazanego sportowca.
    public Optional<Trasa> najatrakcyjniejszaTrasa(Sportowiec sportowiec) {
        return trasy.stream().max(Comparator.comparing(a -> a.obliczAtrakcyjność(sportowiec)));
    }


    public void dodajWyciąg(Wyciąg w) {
        wyciągi.add(w);
        liczbaWychodzącychWyciągów++;
    }

    public void dodajTrasę(Trasa t) {
        trasy.add(t);
        liczbaWychodzącychTras++;
    }

    public int dajNumer() {
        return numer;
    }

    public Krawędź[] dajKrawędzieWychodzące() {
        return Stream.concat(trasy.stream(), wyciągi.stream()).toArray(Krawędź[]::new);
    }

    public Trasa[] dajTrasy() {
        return trasy.toArray(Trasa[]::new);
    }

    public Wyciąg[] dajWyciągi() {
        return wyciągi.toArray(Wyciąg[]::new);
    }

    public int dajX() {
        return x;
    }

    public int dajY() {
        return y;
    }

    public boolean czySkomunikowany() {
        return czySkomunikowany;
    }


}
