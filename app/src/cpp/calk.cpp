//
// Created by Karol on 06.04.2022.
//

#include "Calk.h"

void Calk::AddNum(char num) {
    int n = num - '0';
    tmp *=10;
    tmp += n;
    if(dot){
        decimal_places++;
    }
}

void Calk::Execute() {
    if(negative){
        tmp *= -1;
    }
    input = tmp /(pow(10.0, decimal_places));
    s.push(input);
    negative = false;
    dot = false;
    decimal_places = 0;
}

string Calk::Operation(char o) {
    string ans ="";
    if(s.size()<2){
        return "err: not enough args";
    }
    float b = s.top();
    s.pop();
    float a = s.top();
    s.pop();
    float res = 0;
    switch(o){
        case '+': res = a+b; break;
        case '-': res = a-b; break;
        case '*': res = a*b; break;
        case '/': {if(b == 0) return "err: div by 0"; res = a/b; break;}
        case '^': {if(b == 0 && a == 0) return "err: pow 0^0"; res = pow(a,b); break;}
        case 'r': {if(a <= 0) return "err: root of neg";res = pow (a, 1./b); break;}
    }
    s.push(res);
    return to_string(res);
}

bool Calk::Swp() {
    if(s.size() > 2){
        float a = s.top();
        s.pop();
        float b = s.top();
        s.pop();
        s.push(a);
        s.push(b);
        return true;
    }
    return false;
}

void Calk::AC() {
    while(!s.empty()){
        s.pop();
    }
}

bool Calk::Drp() {
    if(!s.empty()){
        s.pop();
        return true;
    }
    return false;
}

void Calk::Del() {
    tmp/=10;
    if(dot){
        decimal_places--;
    }
}

bool Calk::Dot() {
    if(!dot){
        dot = true;
        return true;
    }
    else{
        return false;
    }
}

void Calk::Rev() {
    negative ^= 1;
}

Calk::Calk() {
    decimal_places = 0;
    dot = false;
    negative = false;
    tmp = 0;
    input = 0;
}

string Calk::Convert(float val) {
    string res = to_string(val);
    if(dot){
        res = res.substr(0,res.size()-decimal_places) + '.' + res.substr(res.size()-decimal_places, decimal_places);
    }
    return res;
}

string Calk::GetTop() {
    return Convert(s.top());
}

