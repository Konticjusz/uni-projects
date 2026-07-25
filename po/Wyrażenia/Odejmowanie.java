


// To zaślepka

public class Odejmowanie extends Operator{

    protected Odejmowanie(Wyrażenie arg1, Wyrażenie arg2) {
        super(arg1, arg2);
    }


    @Override
    protected String dajSymbol() {
        return "-";
    }

    @Override
    protected int dajPriorytet() {
        return 1;
    }

    @Override
    public double wartość(double x) {
        return 0;
    }

    @Override
    public Wyrażenie pochodna() {
        return Stała.twórz(0);
    }
}
