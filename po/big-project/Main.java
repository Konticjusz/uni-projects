import graf.Ośrodek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import narzędzia.Wczytywacz;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Brak ścieżki do katologu mapek w parametrze.");
            return;
        }
        try {
            Scanner scannerWejscia = new Scanner(System.in);
            scannerWejscia.useLocale(Locale.ENGLISH);
            Wczytywacz wczytywacz = new Wczytywacz(scannerWejscia);
            wczytywacz.wczytajDane();
            Ośrodek ośrodek = new Ośrodek(wczytywacz.dajWęzły(), wczytywacz.dajWyciągi(), wczytywacz.dajTrasy(), wczytywacz.dajSportowców());
            ośrodek.symuluj();
            ośrodek.wygenerujStatystyki();
            ośrodek.wygenerujWizualizacje(args[0]);
        } catch (WyjatekSystemuPlikow e) {
            System.err.println("Bład systemu plików!");
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        } catch (Exception e) {
            System.err.println("Wystąpił błąd programu. Zgłoś to programiście.");
            e.printStackTrace(System.err);
        }
    }

}

