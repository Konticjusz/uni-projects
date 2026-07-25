

public class Stała extends Wyrażenie{
    private double wartość;





    @Override
    public String toString(){
        return Double.toString(wartość);
    }

    @Override
    public double wartość(double x) {
        return wartość;
    }

    @Override
    protected int dajPriorytet() {
        return Integer.MAX_VALUE;
    }

    protected Stała (double wartość){
        this.wartość = wartość;
    }

    static public Stała twórz (double wartość){
        if (wartość == 0) return new Zero();
        if (wartość == 1) return new Jeden();
        return new Stała(wartość);
    }

    @Override
    public Wyrażenie pochodna() {
        return Stała.twórz(0);
    }

    @Override
    public Wyrażenie dodaj(Wyrażenie arg) {
        return arg.dodajStałą(this);
    }

    @Override
    protected Wyrażenie dodajStałą(Stała stała) {
        return new Stała(stała.wartość + wartość);
    }

    @Override
    protected Wyrażenie dodajOdwrotnie(Wyrażenie arg) {
        return arg.dodajStałą(this);
    }

    @Override
    public Wyrażenie pomnóż(Wyrażenie arg) {
        return arg.pomnóżPrzezStałą(this);
    }

    @Override
    protected Wyrażenie pomnóżOdwrotnie(Wyrażenie arg) {
        return arg.pomnóżPrzezStałą(this);
    }

    @Override
    protected Wyrażenie pomnóżPrzezStałą(Stała arg) {
        return Stała.twórz(wartość*arg.wartość);
    }
}
