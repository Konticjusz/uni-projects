

public class Zmienna extends Wyrażenie{


    
    static public Zmienna twórz (){
        return new Zmienna();
    }

    @Override
    protected int dajPriorytet() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "x";
    }

    @Override
    public Wyrażenie pochodna() {
        return Stała.twórz(1);
    }




    @Override
    public double wartość(double x) {
        return x;
    }
}
