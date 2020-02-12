package Missile_Launch;

public class Enemy {

	// 여러개의적이미지를그리기위해클래스를추가하여객체관리

	int x;//적현재x 좌표용변수

	int y;//적현재y 좌표용변수

	int speed; // 적이동속도변수를추가


	Enemy(int x, int y, int speed) {

	this.x = x;

	this.y = y;

	this.speed = speed;

	}


	public void move() {

	x -= speed;// 적이동속도만큼이동

	}
}
