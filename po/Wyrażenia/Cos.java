

public class Cos extends Funkcja{




    static public Cos twórz(Wyrażenie arg){
        return new Cos(arg);
    }


    protected Cos(Wyrażenie arg){
        super(arg);
    }

    @Override
    public String toString() {
        return "cos(" + arg.toString() + ")";
    }

    @Override
    public double wartość(double x) {
        return Math.cos(arg.wartość(x));
    }

    @Override
    public Wyrażenie pochodna() {
        return Sin.twórz(arg).pomnóż(arg.pochodna()).pomnóż(Stała.twórz(-1));
    }

    @Override
    protected int dajPriorytet() {
        return Integer.MAX_VALUE;
    }
}
