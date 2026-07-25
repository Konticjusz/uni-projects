

public class Sin extends Funkcja{


    protected Sin(Wyrażenie arg){
        super(arg);
    }

    @Override
    protected int dajPriorytet() {
        return Integer.MAX_VALUE;
    }

    static public Sin twórz(Wyrażenie arg){
        return new Sin(arg);
    }

    @Override
    public String toString() {
        return "sin(" + arg.toString() + ")";
    }

    @Override
    public double wartość(double x) {
        return Math.sin(arg.wartość(x));
    }

    @Override
    public Wyrażenie pochodna() {
        return Cos.twórz(arg).pomnóż(arg.pochodna());
    }
}
