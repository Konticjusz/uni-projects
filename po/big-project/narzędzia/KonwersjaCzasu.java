package narzędzia;

public class KonwersjaCzasu {

    // Zamienia godzinę w formacie HH:MM:SS na liczbę sekund od 00:00:00.
    public static int doSekund(String godzina) {
        String[] części = godzina.split(":");

        int godziny = Integer.parseInt(części[0]);
        int minuty = Integer.parseInt(części[1]);
        int sekundy = Integer.parseInt(części[2]);

        return godziny * 3600 + minuty * 60 + sekundy;

    }

    // Zamienia liczbę sekund od 00:00:00 na godzinę w formacie HH:MM:SS.
    public static String wypisz(int łącznieSekundy) {
        int godziny = łącznieSekundy / 3600;
        int minuty = (łącznieSekundy % 3600) / 60;
        int sekundy = łącznieSekundy % 60;

        return String.format("%02d:%02d:%02d", godziny, minuty, sekundy);
    }

}
