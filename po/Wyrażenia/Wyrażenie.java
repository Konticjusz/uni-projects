

public abstract class Wyrażenie {

    @Override
    public abstract String toString();
    public abstract double wartość(double x);
    public abstract Wyrażenie pochodna();

    protected abstract int dajPriorytet();

    public double całka(double a, double b, int n) {
        double dx = (b - a) / n; 
        double wynik = 0;

        for (int i = 0; i < n; i++) {
            wynik += (wartość(a + i * dx) + wartość(a + (i + 1) * dx)) / 2.0 * dx;
        }

        return wynik;
    }

    public Wyrażenie dodaj(Wyrażenie arg) {
        return arg.dodajOdwrotnie(this);
    }

    protected Wyrażenie dodajOdwrotnie(Wyrażenie arg){
        return new Dodawanie(arg, this);
    }

    protected Wyrażenie dodajStałą(Stała stała) {
        return new Dodawanie(stała, this);
    }

    protected Wyrażenie pomnóżOdwrotnie(Wyrażenie arg){
        return new Mnożenie(arg, this);
    }

    public Wyrażenie pomnóż(Wyrażenie arg) {
        return arg.pomnóżOdwrotnie(this);
    }

    protected Wyrażenie pomnóżPrzezStałą(Stała arg){
        return new Mnożenie(arg, this);
    }

    public Wyrażenie podziel(Wyrażenie arg){
        // To zaślepka
        return new Dzielenie(this, arg);
    }

    public Wyrażenie odejmij(Wyrażenie arg){
        // To zaślepka
        return new Odejmowanie(this, arg);
    }

}





