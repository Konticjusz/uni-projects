public class Ułamek {
    // Niezmienniki klasy:
    // + mianownik > 0
    // + nwd(|licznik|, mianownik) = 1
    // + licznik > Integer.MIN_VALUE

    private int licznik;
    private int mianownik;

    public Ułamek(int licznik, int mianownik){
        assert mianownik != 0 : "Mianownik nie może być zerem";
        assert licznik > Integer.MIN_VALUE: "Licznik musi być większy od Integer.MIN_VALUE";
        
        if (mianownik < 0){
            assert mianownik != Integer.MIN_VALUE : "Mianownik nie może być Integer.MIN_VALUE";
            mianownik *= -1;
            licznik *= -1;
        }
        int d = nwd(licznik, mianownik);
        mianownik /= d;
        licznik /= d;

        this.licznik = licznik;
        this.mianownik = mianownik;
    }

    @Override
    public String toString(){
        return Integer.toString(licznik) + '/' + Integer.toString(mianownik);
    }

    public boolean czyRowne(Ułamek u){
          return licznik == u.licznik && mianownik == u.mianownik;
    }

    public boolean czyMniejszy(Ułamek u){
        if (licznik <= 0 && u.licznik > 0 ) return true;
        if (licznik > 0 && u.licznik <= 0) return false;
        int d = nwd(mianownik, u.mianownik);

        assert !czyIloczynPozaZakresem(licznik, u.mianownik/d);
        assert !czyIloczynPozaZakresem(u.licznik, mianownik/d);

        return (licznik * (u.mianownik/d)) < (u.licznik * (mianownik/d));
    }

    public void pomnóż(Ułamek u){
        // (a*b) / (c*d);
        int d1 = nwd(mianownik, u.licznik);
        int d2 = nwd(u.mianownik, licznik);

        assert !czyIloczynPozaZakresem(licznik/d2, u.licznik/d1);
        assert !czyIloczynPozaZakresem(mianownik/d1, u.mianownik/d2);

        mianownik = (mianownik/d1) * (u.mianownik/d2);
        licznik = (licznik/d2) * (u.licznik/d1);
        int d = nwd(mianownik, licznik);
        licznik /= d;
        mianownik /= d;
    }

    public Ułamek mnożenie(Ułamek u){
        int d1 = nwd(mianownik, u.licznik);
        int d2 = nwd(u.mianownik, licznik);

        assert !czyIloczynPozaZakresem(licznik/d2, u.licznik/d1);
        assert !czyIloczynPozaZakresem(mianownik/d1, u.mianownik/d2);

        return new Ułamek((licznik/d2) * (u.licznik/d1), (mianownik/d1) * (u.mianownik/d2));

    }

    public void odwróć(){
        assert licznik != 0 : "Zero nie ma odwrotności";

        if (licznik < 0){
            int tmp = mianownik;
            mianownik = -licznik;
            licznik = -tmp;
        }
        else{
            int tmp = mianownik;
            mianownik = licznik;
            licznik = tmp;
        }
    }

    public void dziel(Ułamek u){
        assert u.licznik != 0 : "Nie można dzielić przez zero";

        Ułamek odw = new Ułamek(u.mianownik, u.licznik);
        this.pomnóż(odw);
    }

    public void dodaj(Ułamek u){
        int d1 = nwd(mianownik, u.mianownik);

        assert !czyIloczynPozaZakresem(licznik, (u.mianownik/d1)) : "Iloczyn poza zakresem";
        assert !czyIloczynPozaZakresem(u.licznik, mianownik/d1) : "Iloczyn poza zakresem";
        assert !czyIloczynPozaZakresem(mianownik/d1, u.mianownik) : "Iloczyn poza zakresem";
        assert !czySumaPozaZakresem(licznik * (u.mianownik/d1), u.licznik * (mianownik/d1)) : "Iloczyn poza zakresem";

        licznik = licznik * (u.mianownik/d1) + u.licznik * (mianownik/d1);
        mianownik = mianownik/d1 * u.mianownik;
        int d = nwd(mianownik, licznik);
        licznik /= d;
        mianownik /= d;
    }

    public Ułamek dodawanie(Ułamek u){
        int d1 = nwd(mianownik, u.mianownik);

        assert !czyIloczynPozaZakresem(licznik, (u.mianownik/d1)) : "Iloczyn poza zakresem";
        assert !czyIloczynPozaZakresem(u.licznik, mianownik/d1) : "Iloczyn poza zakresem";
        assert !czyIloczynPozaZakresem(mianownik/d1, u.mianownik) : "Iloczyn poza zakresem";
        assert !czySumaPozaZakresem(licznik * (u.mianownik/d1), u.licznik * (mianownik/d1)) : "Suma poza zakresem";

        return new Ułamek(licznik * (u.mianownik/d1) + u.licznik * (mianownik/d1), mianownik/d1 * u.mianownik);
    }

    public void odejmij(Ułamek u){
        Ułamek zanegowane_u = new Ułamek(-u.licznik, u.mianownik);
        this.dodaj(zanegowane_u);
    }

    public Ułamek odejmowanie(Ułamek u){
        Ułamek zanegowane_u = new Ułamek(-u.licznik, u.mianownik);
        return this.dodawanie(zanegowane_u);
    }

    public int dajLicznik(){
        return licznik;
    }

    public int dajMianownik(){
        return mianownik;
    }

    public double toDouble(){
        return (double) licznik / mianownik;
    }

    private static boolean czyIloczynPozaZakresem(int a, int b){
        if (a == 0 || b == 0 ) return false;
        a = Math.abs(a);
        b = Math.abs(b);
        return a > Integer.MAX_VALUE / b;
    }

    private static boolean czySumaPozaZakresem(int a, int b){
        if (b>0 && a > Integer.MAX_VALUE - b) return true;
        if (b < 0 && a <= Integer.MIN_VALUE - b) return true;
        return false;
    }

    private static int nwd(int a, int b){
        assert a != Integer.MIN_VALUE: "Argument ma wartość Integer.MIN_VALUE";
        assert b != Integer.MIN_VALUE: "Argument ma wartość Integer.MIN_VALUE";
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int tmp = b;
            b = a % b;
            a = tmp;
        }
        return a;
    }
}