

public class Zero extends Stała{
    protected Zero(){
        super(0);
    }
    static public Zero twórz(){
        return new Zero();
    }

    @Override
    public Wyrażenie dodaj(Wyrażenie arg) {
        return arg;
    }

    @Override
    protected Wyrażenie dodajStałą(Stała stała) {
        return stała;
    }

    @Override
    protected Wyrażenie dodajOdwrotnie(Wyrażenie arg) {
        return arg;
    }

    @Override
    public Wyrażenie pomnóż(Wyrażenie arg) {
        return this;
    }

    @Override
    protected Wyrażenie pomnóżOdwrotnie(Wyrażenie arg) {
        return this;
    }

    @Override
    protected Wyrażenie pomnóżPrzezStałą(Stała arg) {
        return this;
    }
}
