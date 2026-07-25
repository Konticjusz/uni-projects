

public abstract class Operator extends Wyrażenie {
    protected Wyrażenie arg1;
    protected Wyrażenie arg2;

    protected Operator(Wyrażenie arg1, Wyrażenie arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
    
    protected abstract String dajSymbol();

    @Override
    public String toString() {
        String lewy = arg1.toString();
        String prawy = arg2.toString();
        if (arg1.dajPriorytet() < this.dajPriorytet()) {
            lewy = "(" + lewy + ")";
        }
        if (arg2.dajPriorytet() < this.dajPriorytet()) {
            prawy = "(" + prawy + ")";
        }
        return lewy + " " + dajSymbol() + " " + prawy;
    }
}
