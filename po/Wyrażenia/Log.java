

public class Log extends Funkcja{

    private double podstawa;

    static public Log twórz(Wyrażenie arg, double podstawa){
        return new Log(arg, podstawa);
    }
    

    @Override
    protected int dajPriorytet() {
        return Integer.MAX_VALUE;
    }


    protected Log(Wyrażenie arg, double podstawa){
        super(arg);
        this.podstawa = podstawa;
    }

    @Override
    public String toString() {
        return "log(" + arg.toString() + ")";
    }

    @Override
    public double wartość(double x) {
        return Math.log(arg.wartość(x)) / Math.log(podstawa);
    }

    @Override
    public Wyrażenie pochodna() {
        return new Dzielenie(Stała.twórz(1),arg).pomnóż(arg.pochodna());
    }
}
