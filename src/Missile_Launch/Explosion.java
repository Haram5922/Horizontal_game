package Missile_Launch;

public class Explosion {


int x; // 이미지를그릴x 좌표

int y; // 이미지를그릴y 좌표

int ex_cnt; // 이미지를순차적으로그리기위한카운터

int damage; // 이미지종류를구분하기위한변수값


Explosion(int x, int y, int damage) {

this.x = x;

this.y = y;

this.damage = damage;

ex_cnt = 0;

}


public void effect() {

ex_cnt++; // 해당메소드호출시카운터를+1 시킨다.

}
}
