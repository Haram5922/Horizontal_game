package Missile_Launch;

public class Missile {

	// 여러개의미사일이미지를그리기위해클래스를추가하여객체관리

	int x ;//미사일현재x 좌표용변수

	int y; //미사일현재y 좌표용변수

	int angle; // 미사일이날라가는방향판별을위한변수

	int speed; //미사일움직임속도변수

	int who; //미사일이발사한것이누군지구분하는변수


	Missile(int x, int y, int angle, int speed, int who) {

	this.x = x;

	this.y = y;

	this.who = who;

	//추가된변수를받아옵니다.

	this.angle = angle;

	this.speed = speed;


	}


	public void move() {

	x += Math.cos(Math.toRadians(angle)) * speed;

	// 해당방향으로미사일발사.

	y += Math.sin(Math.toRadians(angle)) * speed;

	// 해당방향으로미사일발사.

	}
}
