#include "worki.h"
#include <list>

worek* korzen = new worek;

std::list<worek*> smieci;
std::list<przedmiot*> smieci_prze;

int ile_workow = 0;

przedmiot *nowy_przedmiot(){
    przedmiot* nowy = new przedmiot;
    nowy->ode_mnie = korzen->do_mnie;
    korzen->wielkosc++;
    smieci_prze.push_back(nowy);
    return nowy;
}

worek *nowy_worek(){
    worek* nowy = new worek;
    nowy->id = ile_workow++;
    nowy->ode_mnie = korzen->do_mnie;
    smieci.push_back(nowy);
    return nowy;
}

void wloz(przedmiot *co, worek *gdzie){
    co->ode_mnie = gdzie->do_mnie;
    gdzie->wielkosc++;
}

void wloz(worek *co, worek *gdzie){
    co->ode_mnie = gdzie->do_mnie;
    gdzie->wielkosc += co->wielkosc;
}

void wyjmij(przedmiot *p){
    p->ode_mnie->wlasciciel->wielkosc--;
    p->ode_mnie = korzen->do_mnie;
}

void wyjmij(worek *w){
    w->ode_mnie->wlasciciel->wielkosc -= w->wielkosc;
    w->ode_mnie = korzen->do_mnie;
}

int w_ktorym_worku(przedmiot *p){
    return p->ode_mnie->wlasciciel->id;
}

int w_ktorym_worku(worek *w){
     return w->ode_mnie->wlasciciel->id;
}

int ile_przedmiotow(worek *w){
    return w->wielkosc;
}

void na_odwrot(worek *w){
     korzen->do_mnie->wlasciciel = w; // wszystko co prowadzilo do korzenia ma prowadzic do w
     w->wielkosc = korzen->wielkosc - w->wielkosc;
     posrednik* tmp = korzen-> do_mnie;
     korzen->do_mnie = w->do_mnie; // to co prowadzilo do w prowadzi do korzenia
     w->do_mnie->wlasciciel = korzen;
     w->do_mnie = tmp;
     w->ode_mnie = korzen->do_mnie; // w prowadzi do korzenia
}

// Kończy i zwalnia pamięć
void gotowe(){
    while (smieci.size()){
        delete smieci.back();
        smieci.pop_back();
    }
    while (smieci_prze.size()){
        delete smieci_prze.back();
        smieci_prze.pop_back();
    }
    delete korzen;
}