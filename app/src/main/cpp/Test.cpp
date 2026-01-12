//
// Created by cjx on 2022/9/9.
//

#include "Test.h"
#include <string>

using namespace std;

struct student {
    //成员列表
    string name;  //姓名
    int age = 0;      //年龄
    int score = 0;    //分数
};


int main() {
    //结构体数组
    struct student a;
    a.name = "jjsjs";
    return 0;
}
