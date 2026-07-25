

public class Dodawanie extends Operator{

    protected Dodawanie(Wyrażenie arg1, Wyrażenie arg2){
        super(arg1, arg2);

    }



    @Override
    protected String dajSymbol() {
        return "+";
    }

    @Override
    public double wartość(double x){
        return arg1.wartość(x) + arg2.wartość(x);
    }

    @Override
    public Wyrażenie pochodna() {
        return arg1.pochodna().dodaj(arg2.pochodna());
    }

    @Override
    protected int dajPriorytet() {
        return 1;
    }
}
