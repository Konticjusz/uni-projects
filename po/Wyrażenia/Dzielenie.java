


// To zaślepka

public class Dzielenie extends Operator{

    protected Dzielenie(Wyrażenie arg1, Wyrażenie arg2){
        super(arg1, arg2);

    }


    @Override
    protected String dajSymbol() {
        return "/";
    }

    @Override
    protected int dajPriorytet() {
        return 2;
    }

    @Override
    public double wartość(double x){
        return 1;
    }

    @Override
    public Wyrażenie pochodna() {
        return Stała.twórz(1);
    }
}
