#include "zbior_ary.h"
#include <stddef.h>
#include <stdlib.h>
#include <limits.h>
#include <stdbool.h>

static int qq = 0; // roznica nastepnych elementow ciagu arytmetycznego

zbior_ary ciag_arytmetyczny(int a, int q, int b){
    qq = q;
    zbior_ary A;
    A.wielkosc = 1;
    A.lewe = (int*)malloc(sizeof(int));
    A.prawe = (int*)malloc(sizeof(int));
    A.reszty = (int*)malloc(sizeof(int));
    A.lewe[0] = a;
    A.prawe[0] = b;
    A.reszty[0] = ((a%qq)+qq)%qq;
    return A;
}


zbior_ary singleton(int a){
    zbior_ary A;
    A.wielkosc = 1;
    A.lewe = (int*)malloc(sizeof(int));
    A.prawe = (int*)malloc(sizeof(int));
    A.reszty = (int*)malloc(sizeof(int));
    A.lewe[0] = a;
    A.prawe[0] = a;
    A.reszty[0] = ((a%qq)+qq)%qq;
    return A;
}

// zmniejsza zbior do jego pozadanego rozmiaru
void zmniejsz(zbior_ary *A){
    A->lewe = (int*)realloc(A->lewe, (A->wielkosc+1)*sizeof(int));
    A->prawe = (int*)realloc(A->prawe, (A->wielkosc+1)*sizeof(int));
    A->reszty = (int*)realloc(A->reszty, (A->wielkosc+1)*sizeof(int));
}

// zamiena 2 zbiory i wskazniki z nimi zwiazane miejscami
void zamien(unsigned *wskaznik1, unsigned *wskaznik2, zbior_ary *A, zbior_ary *X){
    unsigned tmp_int;
    tmp_int = *wskaznik1;
    *wskaznik1 = *wskaznik2;
    *wskaznik2 = tmp_int;
    zbior_ary tmp_zbior;
    tmp_zbior = *A;
    *A = *X;
    *X = tmp_zbior;

}

//przypisuje zbiorowi X odpowiedni element z zbioru A
void przypisz(bool *czy_lewy_wolny, unsigned *wskaznik1, unsigned *wskaznik2, zbior_ary *A, zbior_ary *X){
    if (*czy_lewy_wolny){
        X->lewe[*wskaznik2] = A->lewe[*wskaznik1];
    }
    X->prawe[*wskaznik2] = A->prawe[*wskaznik1];
    X->reszty[*wskaznik2] = A->reszty[*wskaznik1];
    (*wskaznik1)++;
    (*wskaznik2)++;
    *czy_lewy_wolny = true;
}


long long max(long long a, long long b){
    if (b > a) return b;
    return a;
}

long long min(long long a, long long b){
    if (b < a) return b;
    return a;
}


zbior_ary suma(zbior_ary A, zbior_ary B){
    zbior_ary WYN;
    // zaalokujemy A.wielkosc + B.wielkosc pamieci zeby zmiescic sume. Potem nadmiarowa pamiec zwolnimy.
    WYN.lewe = (int*)malloc(sizeof(int)*(A.wielkosc+B.wielkosc+1));
    WYN.prawe = (int*)malloc(sizeof(int)*(A.wielkosc+B.wielkosc+1));
    WYN.reszty = (int*)malloc(sizeof(int)*(A.wielkosc+B.wielkosc+1));
    // Chcemy zachowywac strukture zbioru tak zeby ciagi byly uporzadkowane najpierw po resztach % q, potem po pierwszym elemencie, a potem ostatnim
    unsigned wskaznik_A = 0;
    unsigned wskaznik_B = 0;
    unsigned wskaznik_WYN = 0;
    bool czy_lewy_wolny = true; // czy juz nie ustawilismy lewego konca obecnego przedzialu
    while (wskaznik_A < A.wielkosc && wskaznik_B < B.wielkosc){
        if (A.reszty[wskaznik_A] < B.reszty[wskaznik_B]){ 
            przypisz(&czy_lewy_wolny, &wskaznik_A, &wskaznik_WYN, &A, &WYN);
        }
        else if (B.reszty[wskaznik_B] < A.reszty[wskaznik_A]){ // analogicznie jak w poprzednim przypadku
            przypisz(&czy_lewy_wolny, &wskaznik_B, &wskaznik_WYN, &B, &WYN);
        }
        else{
             // wiemy ze ciagi arytmetyczne maja ta sama reszte mod q
            WYN.reszty[wskaznik_WYN] = A.reszty[wskaznik_A];
            if (A.lewe[wskaznik_A] <= B.lewe[wskaznik_B]){ // Lewy początek jest wcześniej więc opłaca się nam go wziąć
                if (czy_lewy_wolny){
                    WYN.lewe[wskaznik_WYN] = A.lewe[wskaznik_A]; // rezerwujemy lewy początek z A jako najwczesniejszy
                    czy_lewy_wolny = false;
                }
                if (B.lewe[wskaznik_B] <= (int) min((long long) A.prawe[wskaznik_A]+qq, INT_MAX)){ // sprawdzamy czy ciag przecina sie z nami badz zaczyna sie od razu po nas
                    if (B.prawe[wskaznik_B] <= A.prawe[wskaznik_A])  // ciag z B jest " w srodku" tego z A wiec nie przedluzy obecnie naszego ciagu
                        wskaznik_B++;
                    else{ // Ciąg z B przedłuza obecny ciąg, więc możemy już przesunąć wskaźnik A
                        wskaznik_A++;
                    }
                }
                else{ // Ciag B jest wiecej niz element za nami wiec nie przedluzy ciagu
                    przypisz(&czy_lewy_wolny, &wskaznik_A, &wskaznik_WYN, &A, &WYN);
                }
            }
            else{ // symetryczny przypadek do poprzedniego
                zamien(&wskaznik_A, &wskaznik_B, &A, &B);
            }
        }
    }
    if (wskaznik_B < B.wielkosc)
        zamien(&wskaznik_A, &wskaznik_B, &A, &B);
    while (wskaznik_A < A.wielkosc){ // jesli czesc ktoregos z zbioru nie zostala jeszcze dodana to teraz to robimy
        przypisz(&czy_lewy_wolny, &wskaznik_A, &wskaznik_WYN, &A, &WYN);
    }
    WYN.wielkosc = wskaznik_WYN;
    // zmniejszamy nasze tablice tak zeby nie zuzywac zbednej pamieci
    zmniejsz(&WYN);
    return WYN;
}




zbior_ary roznica(zbior_ary A, zbior_ary B){
    zbior_ary WYN;
    WYN.lewe = (int*)malloc(sizeof(int)*(A.wielkosc + B.wielkosc+1));
    WYN.prawe = (int*)malloc(sizeof(int)*(A.wielkosc + B.wielkosc+1));
    WYN.reszty = (int*)malloc(sizeof(int)*(A.wielkosc + B.wielkosc+1));
    unsigned wskaznik_WYN = 0;
    unsigned wskaznik_A = 0;
    unsigned wskaznik_B = 0;
    bool czy_lewy_wolny = true;
    while (wskaznik_A < A.wielkosc){
        if (wskaznik_B >= B.wielkosc){ // Ciag B już nie wpływa na wynik
            przypisz(&czy_lewy_wolny,&wskaznik_A, &wskaznik_WYN, &A, &WYN);
        }
        else{
            if (B.reszty[wskaznik_B] < A.reszty[wskaznik_A]){ //Idziemy do większej reszty w B, ktora moglaby wplynac na wynik
                wskaznik_B++;
            }
            else if (B.reszty[wskaznik_B] > A.reszty[wskaznik_A]){ // Ciag z B jest za nami wiec nie wplywa na wynik
                przypisz(&czy_lewy_wolny,&wskaznik_A, &wskaznik_WYN, &A, &WYN);
            }
            else{
                  // Oba ciagi maja ta sama reszte
                if (B.prawe[wskaznik_B] < A.lewe[wskaznik_A]) //Przesuwamy wskaznik z B az dojdzie do przedzialu z A
                    wskaznik_B++;
                else if (B.lewe[wskaznik_B] > A.prawe[wskaznik_A]){ // Ciag z B jest za nami wiec nie wplywa na wynik
                    przypisz(&czy_lewy_wolny,&wskaznik_A, &wskaznik_WYN, &A, &WYN);
                }
                else{ // Ciagi sie przecinaja
                    if (czy_lewy_wolny && A.lewe[wskaznik_A] < B.lewe[wskaznik_B]){ // Powstanie przedzial od lewego konca ciagu A do lewego konca ciagu B
                        WYN.lewe[wskaznik_WYN] = A.lewe[wskaznik_A];
                       // std::cout << WYN.lewe[wskaznik_WYN] << std::endl;
                        WYN.prawe[wskaznik_WYN] = (int) max(INT_MIN, (long long) B.lewe[wskaznik_B] -qq);
                        WYN.reszty[wskaznik_WYN] = A.reszty[wskaznik_A];
                        wskaznik_WYN++;
                    }
                    else if (!czy_lewy_wolny && WYN.lewe[wskaznik_WYN] < B.lewe[wskaznik_B]){ // juz raz przesunelismy lewy koniec A
                        WYN.prawe[wskaznik_WYN] = (int) max(INT_MIN, ((long long) B.lewe[wskaznik_B] - qq));
                        WYN.reszty[wskaznik_WYN] = A.reszty[wskaznik_A];
                        wskaznik_WYN++;
                    }
                    if (A.prawe[wskaznik_A] > B.prawe[wskaznik_B]){ // konczymy sie za B 
                        WYN.lewe[wskaznik_WYN] = (int) min(((long long)B.prawe[wskaznik_B]+qq), INT_MAX); // rezerwujemy element od razu za koncem ciagu z B jako nowy poczatek
                        wskaznik_B++;
                        czy_lewy_wolny = false;
                    }
                    else{ // ciag z A konczy sie w ciagu B
                        wskaznik_A++;
                        czy_lewy_wolny = true;
                    }
                }
            }
        }
    }
    WYN.wielkosc = wskaznik_WYN;
    zmniejsz(&WYN);
    return WYN;
}

zbior_ary iloczyn(zbior_ary A, zbior_ary B){
    return roznica(A, roznica(A,B)); // A - (A - B) = A ∩ B
}

bool nalezy(zbior_ary A, int b){
    unsigned l = 0;
    if (A.wielkosc == 0){
        return 0;
    }
    unsigned r = A.wielkosc - 1;
    unsigned mid;
    while (l < r){
        mid = (l+r+1)/2;
        if (A.reszty[mid] > ((b%qq)+qq)%qq) // jesli reszta jest wieksza niz reszta b to ciag w ktorym jest b musi byc wczesniej
            r = mid-1;
        else if (A.reszty[mid] < ((b%qq)+qq)%qq) 
            l = mid;
        else{ // reszta sie zgadza
            if (A.lewe[mid] > b) r = mid-1; // jesli lewy poczatek jest za b to b musi byc wczesniej
            else l = mid;
        }
    }
    if ((l != r) ||  (l >= A.wielkosc)){
        return false;
    }
    return (A.lewe[l] <= b && b <= A.prawe[l]) && (A.reszty[l] == (((b%qq)+qq)%qq));
}

unsigned moc(zbior_ary A){
    unsigned wynik = 0;
    for (unsigned i = 0; i < A.wielkosc; i++){ 
        wynik += (unsigned)(((long long)A.prawe[i] - A.lewe[i])/(qq))+1;
    }
    return wynik;
}

unsigned ary(zbior_ary A){
    return A.wielkosc;
}