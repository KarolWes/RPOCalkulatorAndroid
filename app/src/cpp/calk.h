//
// Created by Karol on 06.04.2022.
//

#ifndef CALK_CALK_H
#define CALK_CALK_H
#include <string>
#include <stack>
#include <cmath>
using namespace std;

class Calk {
private:
    bool dot;
    bool negative;
    int decimal_places;
    int tmp;
    float input;
    stack <float> s;


public:
    Calk();
    void AddNum(char num);
    void Execute();
    string Operation(char o);
    bool Swp();
    void AC();
    bool Drp();
    void Del();
    bool Dot();
    void Rev();
    string Convert(float val);
    string GetTop();
};


#endif //CALK_CALK_H
