

public class Mnożenie extends Operator{

    protected Mnożenie(Wyrażenie arg1, Wyrażenie arg2){
        super(arg1, arg2);

    }


    @Override
    protected String dajSymbol() {
        return "*";
    }

    @Override
    protected int dajPriorytet() {
        return 2;
    }

    @Override
    public double wartość(double x){
        return arg1.wartość(x) * arg2.wartość(x);
    }

    @Override
    public Wyrażenie pochodna() {
        return arg1.pochodna().pomnóż(arg2).dodaj(arg2.pochodna().pomnóż(arg1));
    }
}
