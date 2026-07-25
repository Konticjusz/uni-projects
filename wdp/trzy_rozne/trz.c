#include <stdio.h>
#include <stddef.h>
#include <stdlib.h>
#include <limits.h>
#include <stdbool.h>
#include <assert.h>


int minn(int a, int b){
    if (a > b) return b;
    return a;
}

int maxx(int a, int b){
    if (a < b) return b;
    return a;
}

void swap(int* a, int* b){
    int tmp = *a;
    *a = *b;
    *b = tmp;
}


int znajdz_najdalsza(int* siec, int* odl, int n){
    int lewe[3][2]; // 3 skrajnie lewe rozne hotele
    int prawe[3][2]; // 3 skrajnie prawe rozne hotele
    int wyn = 0;
    for (int i = 0; i < 3; i++){
        lewe[i][0] = -1;
        prawe[i][0] = -1;
    }
    int ost = 0;
    int ind = 0;
    bool byla; // czy siec hotelu juz jest w trojce
    while (ind < n && ost < 3){
        byla = false;
        for (int pop = 0; pop < ost; pop++){
            if (lewe[pop][0] == siec[ind]){
                byla = true;
            }
        }
        if (!byla){
            lewe[ost][0] = siec[ind];
            lewe[ost][1] = odl[ind];
            ost++;
        }
        ind++;
    }
    ost = 0;
    ind = n-1;
    while (ind >= 0 && ost < 3){
        byla = false;
        for (int pop = 0; pop < ost; pop++){
            if (prawe[pop][0] == siec[ind]){
                byla = true;
            }
        }
        if (!byla){
            prawe[ost][0] = siec[ind];
            prawe[ost][1] = odl[ind];
            ost++;
        }
        ind--;
    }
    for (int i = 0; i < n; i++){ //ide po kazdym srodkowym hotelu
        for (int l = 0; l < 3; l++){ // kazdy mozliwy skrajnie lewy
            for (int p = 0; p < 3; p++){ // kazdy mozliwy skrajnie prawy
                if (lewe[l][0] != -1 && prawe[p][0] != -1 && siec[i] != lewe[l][0] && siec[i] != prawe[p][0] && lewe[l][0] != prawe[p][0]){
                    wyn = maxx(wyn, minn(odl[i] - lewe[l][1], prawe[p][1] - odl[i])); // sprawdzam czy 3 daje lepszy wynik
                }
            }
        }
    }
    return wyn;
}

int znajdz_najblizsza(int* siec, int* odl, int n){
     bool czy_cos_jest = false;
     int wyn = INT_MAX;
     int id1 = -1;
     int id2 = -1;
     int id3 = -1;
     for (int i = 0; i < n; i++){ // przesuwam sie w prawo az nie bede mial 3 roznych sieci hoteli
        if (id1 != -1 && siec[i]==siec[id1]) id1 = i; // jesli juz mam hotel tej sieci to go zamieniam z nim
        else if (id2 != -1 && siec[i]==siec[id2]) id2 = i;
        else if (id3 != -1 && siec[i]==siec[id3]) id3 = i;
        else if (id1 == -1) id1 = i;
        else if (id2 == -1) id2 = i;
        else if (id3 == -1) id3 = i;
        if (id1 != -1 && id2 != -1 && id3 != -1){ // mam 3 rozne hotele
            if (id1 > id2) swap(&id1, &id2);
            if (id2 > id3) swap(&id2, &id3);
            if (id1 > id2) swap(&id1,&id2); // sortuje sieci hoteli
            for (int k = id1+1; k < id3; k++){
                if (siec[id1] != siec[k] && siec[k] != siec[id3]){
                    czy_cos_jest = true;
                    wyn = minn(wyn, maxx(odl[k] - odl[id1], odl[id3] - odl[k])); 
                }
            }
            id1 = -1;   // pozbywam sie skrajnie lewego hotelu z trojki
        }

    }
    if (!czy_cos_jest) return 0;
    return wyn;
}


int main(){
    int n;
    assert(scanf("%d", &n)==1);
    int* siec = (int*)malloc(sizeof(int)*(unsigned)n);
    int* odl = (int*)malloc(sizeof(int)*(unsigned)n);
    assert(siec != NULL);
    assert(odl != NULL);
    for (int i = 0; i < n; i++){
        assert(scanf("%d %d", &siec[i], &odl[i])==2);
    
    }
    int najblizsza = znajdz_najblizsza(siec, odl, n);
    int najdalsza = znajdz_najdalsza(siec, odl, n);
    printf("%d %d\n", najblizsza, najdalsza);
    free(siec);
    free(odl);
    return 0;
}