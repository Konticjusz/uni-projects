

public class Jeden extends Stała{
    protected Jeden(){
        super(1);
    }
    static public Jeden twórz(){
        return new Jeden();
    }



    @Override
    public Wyrażenie pomnóż(Wyrażenie arg) {
        return arg;
    }

    @Override
    protected Wyrażenie pomnóżOdwrotnie(Wyrażenie arg) {
        return arg;
    }

    @Override
    protected Wyrażenie pomnóżPrzezStałą(Stała arg) {
        return arg;
    }
}
