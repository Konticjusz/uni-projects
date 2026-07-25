#include <iostream>
#include <vector>
#include <queue>

using namespace std;

using ld = long double;

bool czy_mniejszy(const pair<int, int>& a, const pair<int, int>& b, const vector<pair<int,int>>& punkty){
     ld val1 = ld(punkty[a.second].first - punkty[a.first].first) * ld(punkty[a.second].first - punkty[a.first].first) / ld(a.second - a.first + 1);
     ld val2 = ld(punkty[b.second].first - punkty[b.first].first) * ld(punkty[b.second].first - punkty[b.first].first) / ld(b.second - b.first + 1);
     return val1 < val2;

}

void wstaw(int val, int ind, deque<pair<int,int>>& kol, int mnoznik){
    while (kol.size() && kol.back().first * mnoznik < val * mnoznik) 
        kol.pop_back();
    kol.push_back({val, ind});
}

void dodaj_wynik(deque<pair<int, int>>& przedzialy, const pair<int, int>& nowy, const vector<pair<int, int>>& punkty){
    while (przedzialy.size() && czy_mniejszy(przedzialy.back(), nowy, punkty)){
        przedzialy.pop_back();
    }
    przedzialy.push_back(nowy);
}


int main(){
    int n, U;
    cin >> n >> U;
    vector<pair<int, int>> punkty(n+1);
    for (int i = 1; i <= n; i++)
        cin >> punkty[i].first >> punkty[i].second;
    deque<pair<int, int>> max_y;
    deque<pair<int, int>> min_y;
    deque<pair<int, int>> przedzialy;
    int kon = 0;
    for (int i = 1; i <= n; i++){
        bool czy_rozszerzony = false;
        if (kon < i){
            kon = i;
            wstaw(punkty[i].second, i, max_y,1);
            wstaw(punkty[i].second, i, min_y,-1);
            czy_rozszerzony = true;
        }
        while (max_y.size() && max_y.front().second < i) 
            max_y.pop_front();
        while (min_y.size() && min_y.front().second < i) 
            min_y.pop_front();
        while (kon + 1 <= n && max(abs(punkty[kon+1].second - max_y.front().first), abs(punkty[kon+1].second - min_y.front().first)) <= U){
            kon++;
            wstaw(punkty[kon].second, kon, max_y, 1);
            wstaw(punkty[kon].second, kon, min_y, -1);
            czy_rozszerzony = true;
        }
        if (czy_rozszerzony)
            dodaj_wynik(przedzialy, {i, kon}, punkty);
        while (przedzialy.front().second < i) 
            przedzialy.pop_front();
        cout << przedzialy.front().first << " " << przedzialy.front().second << "\n";
    }
    return 0;
}   