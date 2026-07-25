#include <stdio.h>
#include <stddef.h>
#include <stdlib.h>
#include <limits.h>
#include <stdbool.h>
#include <assert.h>
#include <math.h>

#define EPS 1e-6

typedef struct Point{
    double x;
    double y;
} Point;

Point create_point(double x, double y){
    Point p;
    p.x = x;
    p.y = y;
    return p;
}

double calc_squared_dist(Point p1, Point p2){
      return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
}

double calc_orientation(Point p1, Point p2, Point p3){
    return ((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x));
}

Point reflection(Point a, Point b, Point p){ // odbicie punktu p wzgledem prostej przechodzacej przez punkty a,b
     double A = a.y - b.y;
     double B = b.x - a.x;
     double C = a.x * b.y - a.y*b.x;
     double x = p.x - (2*A*(A*p.x + B*p.y + C))/(A*A + B*B);
     double y = p.y - (2*B*(A*p.x + B*p.y + C))/(A*A + B*B);
     return create_point(x, y);
}

typedef struct Rectangle{
    Point p1;
    Point p2;
} Rectangle;

Rectangle create_rectangle(double a, double b, double x, double y){
    Point p1 = create_point(a,b);
    Point p2 = create_point(x,y);
    Rectangle r;
    r.p1 = p1;
    r.p2 = p2;
    return r;
}

int is_inside_rect(Rectangle rect, Point p){
    return (p.x >= rect.p1.x - EPS && p.y >= rect.p1.y - EPS && p.x <= rect.p2.x + EPS && p.y <= rect.p2.y + EPS);
}

typedef struct Circle{
    Point s;
    double rad;
} Circle;

bool is_inside_circle(Circle c, Point p){
    double dist = calc_squared_dist(c.s, p);
    return (dist <= ((c.rad+EPS)*(c.rad+EPS)));
}

Circle create_circle(double x, double y, double rad){
    Point s = create_point(x,y);
    Circle c;
    c.s = s;
    c.rad = rad;
    return c;
}

typedef enum Type{
    TYPE_RECTANGLE,
    TYPE_CIRCLE,
    TYPE_FOLDED,
} Type;

typedef struct Page{
    Type tag;
    union{
        Rectangle rectangle;
        Circle circle;
        struct{
            Point p1;
            Point p2;
            struct Page *previous;
        } folded;
    } content;
} Page;

int count_intersections(const Page* a, Point p){
    int answer = 0;
    if (a->tag == TYPE_RECTANGLE){
        answer = is_inside_rect(a->content.rectangle, p);
    }
    else if (a->tag == TYPE_CIRCLE){
        answer = is_inside_circle(a->content.circle, p);
    }
    else{
        double orientation = calc_orientation(a->content.folded.p1, a->content.folded.p2, p); // czy jest po lewej czy prawej stronie prostej
        if (orientation < -EPS) answer = 0;
        else if (fabs(orientation) < EPS) answer = count_intersections(a->content.folded.previous, p); // jest na prostej
        else{
            answer = count_intersections(a->content.folded.previous, p);
            answer += count_intersections(a->content.folded.previous, reflection(a->content.folded.p1, a->content.folded.p2, p));
        }
    }
    return answer;
}

int main(){
    int n, q;
    assert(scanf("%d %d", &n, &q));
    Page* pages = (Page*)malloc((size_t)(n+1)*sizeof(Page));
    char type;
    for (int i = 1; i <= n; i++){
        assert(scanf(" %c", &type));
        if (type == 'P'){
            double a,b, x, y;
            assert(scanf("%lf %lf %lf %lf", &a, &b, &x, &y));
            pages[i].tag = TYPE_RECTANGLE;
            pages[i].content.rectangle = create_rectangle(a,b,x,y);
        }
        else if (type == 'K'){
            double x,y,r;
            assert(scanf("%lf %lf %lf", &x, &y, &r));
            pages[i].tag = TYPE_CIRCLE;
            pages[i].content.circle = create_circle(x,y,r);

        }
        else{
            int prv;
            double a,b, x, y;
            assert(scanf("%d %lf %lf %lf %lf", &prv, &a, &b, &x, &y));
            Point p1 = create_point(a,b);
            Point p2 = create_point(x,y);
            pages[i].tag = TYPE_FOLDED;
            pages[i].content.folded.p1 = p1;
            pages[i].content.folded.p2 = p2;
            pages[i].content.folded.previous = &pages[prv];
        }
    }
    for (int i = 1; i <= q; i++){
        int k;
        double x,y;
        assert(scanf("%d %lf %lf", &k, &x, &y));
        Point p = create_point(x,y);
        int answer = count_intersections(&pages[k], p);
        printf("%d\n", answer);
    }
    free(pages);
    return 0;
}