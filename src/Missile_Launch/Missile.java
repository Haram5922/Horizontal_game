package Missile_Launch;

public class Missile {

	// �������ǹ̻����̹������׸�������Ŭ�������߰��Ͽ���ü����

	int x ;//�̻�������x ��ǥ�뺯��

	int y; //�̻�������y ��ǥ�뺯��

	int angle; // �̻����̳��󰡴¹����Ǻ������Ѻ���

	int speed; //�̻��Ͽ����Ӽӵ�����

	int who; //�̻����̹߻��Ѱ��̴����������ϴº���


	Missile(int x, int y, int angle, int speed, int who) {

	this.x = x;

	this.y = y;

	this.who = who;

	//�߰��Ⱥ������޾ƿɴϴ�.

	this.angle = angle;

	this.speed = speed;


	}


	public void move() {

	x += Math.cos(Math.toRadians(angle)) * speed;

	// �ش�������ι̻��Ϲ߻�.

	y += Math.sin(Math.toRadians(angle)) * speed;

	// �ش�������ι̻��Ϲ߻�.

	}
}
