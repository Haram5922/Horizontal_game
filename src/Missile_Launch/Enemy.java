package Missile_Launch;

public class Enemy {

	// �����������̹������׸�������Ŭ�������߰��Ͽ���ü����

	int x;//������x ��ǥ�뺯��

	int y;//������y ��ǥ�뺯��

	int speed; // ���̵��ӵ��������߰�


	Enemy(int x, int y, int speed) {

	this.x = x;

	this.y = y;

	this.speed = speed;

	}


	public void move() {

	x -= speed;// ���̵��ӵ���ŭ�̵�

	}
}
