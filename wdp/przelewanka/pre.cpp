#include <bits/stdc++.h>


using namespace std;
using ull = unsigned long long;

ull how_many_states(const vector<int>& capacities){
    ull prod = 1;
    for (int c: capacities){
        if (prod > (ULLONG_MAX-(ull)1)/(c+1)){
            return 0;
        }
        prod *= (c+1);
    }
    return prod;
}

ull encode(const vector<int>& cap, const vector<int>& val, int n){
    ull state = 0;
    ull pow = 1;
    for (int i = 0; i < n; i++){
        state += pow * val[i];
        pow *= (cap[i]+1);
    }
    return state;
}


void decode(ull state, const vector<int>& cap, vector<int>& v, int n){
    for (int i = 0; i < n; i++){
        v[i] = (int)(state % (ull)(cap[i]+1));
        state /= (ull)(cap[i]+1);
    }
}

int bfs_small(const vector<int>& cap, const vector<int>& tar, int n, ull num_states){
    unordered_map<ull, int> distance;
    ull MAXKEY = min(num_states+1, (ull) 1e8);
    vector<int> tiny_distance(MAXKEY, -1);
    ull start = 0;
    tiny_distance[start] = 0;
    queue<ull> queue;
    queue.push(start);
    ull target = encode(cap, tar, n);
    if (start == target) return 0;
    vector<ull> mult(n);
    mult[0] = 1;
    for (int i = 1; i < n; i++){
        mult[i] = mult[i-1] * (ull) (cap[i-1]+1);
    }
    vector<int> tmp(n);
    ull v;
    int dist;
    ull u;
    while (queue.size()){
        v = queue.front();
        dist = v < MAXKEY ? tiny_distance[v] : distance[v];
        queue.pop();
        decode(v, cap, tmp, n);
        for (int i = 0; i < n; i++){
            if (tmp[i] == 0) continue;
            u = v - (ull)tmp[i] * mult[i];
            if (u == target) return dist+1;
            if (u < MAXKEY && tiny_distance[u] == -1){
                tiny_distance[u] = dist+1;
                queue.push(u);
            }
            else if (u >= MAXKEY && distance.find(u) == distance.end()){
                distance[u] = dist+1;
                queue.push(u);
            }
        }
        for (int i = 0; i < n; i++){
            if (tmp[i] == cap[i]) continue;
            u = v + (ull)(cap[i] - tmp[i]) * mult[i];
            if (u == target) return dist+1;
            if (u < MAXKEY && tiny_distance[u] == -1){
                tiny_distance[u] = dist+1;
                queue.push(u);
            }
            else if (u >= MAXKEY && distance.find(u) == distance.end()){
                distance[u] = dist+1;
                queue.push(u);
            }
        }
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                if (i == j) continue;
                int transfer = min(tmp[i], cap[j] - tmp[j]);
                if (!transfer) continue;
                u = v - (ull)transfer * mult[i] + (ull)transfer * mult[j];
                if (u == target) return dist+1;
                if (u < MAXKEY && tiny_distance[u] == -1){
                    tiny_distance[u] = dist+1;
                    queue.push(u);
                }
                else if (u >= MAXKEY && distance.find(u) == distance.end()){
                    distance[u] = dist+1;
                    queue.push(u);
                }
            }
        }
    }
    return -1;
}

bool is_possible(vector<int>& capacities, vector<int>& target, int n){
    bool full_or_empty = false; // after each operation there should be a full or an empty cup
    int gcd = 0; // after each operation water in cups changes by some combination of capacities
    for (int i = 0; i < n; i++){
        full_or_empty |= ((capacities[i] == target[i]) | !target[i]);
        gcd = __gcd(gcd, capacities[i]);
    }
    if (!full_or_empty) return 0;
    if (!gcd) return 1;
    for (int tar: target){
        if (tar%gcd) return 0;
    }
    return 1;
    }


int bfs_big(const vector<int>& capacity, const vector<int>& target, int n){
    map<vector<int>, int> distances;
    vector<int> start(n);
    distances[start] = 0;
    queue<vector<int>> queue;
    queue.push(start);
    vector<int> v;
    int tmp1;
    int dist;
    while (queue.size()){
        v = queue.front();
        queue.pop();
        dist = distances[v];
        if (v == target) return dist;
        for (int i = 0; i < n; i++){
            if (v[i] != 0){
                tmp1 = v[i];
                v[i] = 0;
                if (distances.find(v) == distances.end()){
                    distances[v] = dist+1;
                    queue.push(v);
                }
                v[i] = tmp1;
            }
            if (v[i] != capacity[i]){
                tmp1 = v[i];
                v[i] = capacity[i];
                if (distances.find(v) == distances.end()){
                    distances[v] = dist+1;
                    queue.push(v);
                }
                v[i] = tmp1;
            }
            for (int j = 0; j < n; j++){
                if (i != j){
                    int transfer = min(v[i], capacity[j] - v[j]);
                    if (transfer){
                        v[i] -= transfer;
                        v[j] += transfer;
                        if (distances.find(v) == distances.end()){
                            distances[v] = dist+1;
                            queue.push(v);
                        }
                        v[i] += transfer;
                        v[j] -= transfer;

                    }
                }
            }

        }
    }
    return -1;


}



int main(){
    int n;
    cin >> n;
    if (n == 0){
        cout << 0 << "\n";
        return 0;
    }
    vector<int> capacities(n);
    vector<int> target(n);
    for (int i = 0; i < n; i++){
        cin >> capacities[i] >> target[i];
    }
    if (!is_possible(capacities, target, n)){
        cout << -1 << "\n";
        return 0;
    }
    ull num_states = how_many_states(capacities);
    if (num_states){
        cout << bfs_small(capacities, target, n, num_states) << "\n";
        return 0;
    }
    cout << bfs_big(capacities, target, n) << "\n";
    return 0;
}