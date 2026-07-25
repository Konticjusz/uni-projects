import Drzewo.DrzewoBST;
import Drzewo.WInteger;
import Drzewo.WString;


public class Main {
    public static void main(String[] args) {
        DrzewoBST<WInteger> drzewo = new DrzewoBST<>(WInteger.class);
        drzewo.wstaw(WInteger.of(5));
        drzewo.wstaw(WInteger.of(7));
        drzewo.wstaw(WInteger.of(1));
        drzewo.drukujZawartość();
        drzewo.drukujDrzewo(1);
        System.out.println(drzewo.obliczSumęElementów().toString());
        System.out.println(drzewo.obliczRozmiar());
        System.out.println(drzewo.obliczWysokość());
        System.out.println(drzewo.znajdź(WInteger.of(4)).toString());

        DrzewoBST<WString> drzewo2 = new DrzewoBST<>(WString.class);
        drzewo2.wstaw(WString.of("Ala"));
        drzewo2.wstaw(WString.of("Ma"));
        drzewo2.wstaw(WString.of("Kota"));
        drzewo2.drukujZawartość();
        drzewo2.drukujDrzewo(2);
        System.out.println(drzewo2.obliczSumęElementów().toString());


    }

}