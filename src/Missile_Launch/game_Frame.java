package Missile_Launch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.image.*;


class game_Frame extends JFrame implements KeyListener, Runnable {



/*

* 변수선언부분

*/


int f_width; //프레임넓이값을받는변수

int f_height; // 프레임높이값을받는변수


int x, y; //플레이어캐릭터의현재좌표값을받을변수


int[] cx = { 0, 0, 0 }; // 배경스크롤속도제어용변수

int bx = 0; // 전체배경스크롤용변수


boolean KeyUp = false; //키보드키값을받을변수

boolean KeyDown = false;

boolean KeyLeft = false;

boolean KeyRight = false;

boolean KeySpace = false;


int cnt; //무한루프횟수를카운트할변수


int player_Speed;// 유저의캐릭터가움직이는속도를조절할변수

int missile_Speed; // 미사일이날라가는속도조절할변수

int fire_Speed; // 미사일연사속도조절변수

int enemy_speed; // 적이동속도설정

int player_Status = 0;

// 유저캐릭터상태체크변수0 : 평상시, 1: 미사일발사, 2: 충돌

int game_Score; // 게임점수계산

int player_Hitpoint; // 플레이어캐릭터의체력


// int e_w, e_h; //소스변경으로인해해당변수가필요없어졌습니다

// int m_w, m_h;


Thread th; //스레드생성


Image[] Player_img;

// 플레이어애니메이션표현을위해이미지를배열로받음

Image BackGround_img; // 배경화면이미지



Image[] Explo_img; // 폭발이펙트용이미지배열


Image Missile_img; //플레이어미사일이미지생성

Image Enemy_img; // 적이미지생성

Image Missile2_img;// 적미사일이미지추가생성


ArrayList Missile_List = new ArrayList();

//다수의미사일을관리하기위한배열

ArrayList Enemy_List = new ArrayList();

//다수의적을관리하기위한배열

ArrayList Explosion_List = new ArrayList();

// 다수의폭발이펙트를처리하기위한배열


Image buffImage; //더블버퍼링을위한버퍼

Graphics buffg; // 더블버퍼링을위한버퍼


Missile ms; //미사일클래스접근키

Enemy en; // 에너미클래스접근키


Explosion ex; // 폭발이펙트용클래스접근키


/*

* 프레임생성부분

*/


game_Frame() {//화면에보여질프레임생성메소드

init();

start();


setTitle("Ezreal Adventure");//프레임타이틀설정

setSize(f_width, f_height);//프레임크기설정


Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

//모니터화면해상도크기값받아들이기


int f_xpos = (int) (screen.getWidth() / 2 - f_width / 2);

int f_ypos = (int) (screen.getHeight() / 2 - f_height / 2);

//프레임을모니터화면정중앙에배치하기위한좌표값계산


setLocation(f_xpos, f_ypos); //프레임을해당위치에배치

setResizable(false); //프레임크기를임의로변경하는것방지

setVisible(true);//프레임을화면에보이게만든다

}


/*

* 기본설정부분

*/


public void init() { //기본적인게임설정을관리할메소드
	Sound("dododo.wav", true);
x = 100; //최초플레이어시작x좌표

y = 100; //최초플레이어시작y좌표

f_width = 1200; //프레임넓이값설정

f_height = 600; //프레임높이값설정


Missile_img = new ImageIcon("ccccc.png").getImage();

//플레이어미사일이미지파일받아들이기

Missile2_img = new ImageIcon("mush2.png").getImage();

//적미사일이미지파일받아들이기

Enemy_img = new ImageIcon("timo3.png").getImage();

//적이미지파일받아들이기


Player_img = new Image[5];

for (int i = 0; i < Player_img.length ; ++i) {

Player_img[i] = new ImageIcon("me_4.png").getImage();

}

// 플레이어애니메이션표현을위해파일이름을

// 넘버마다나눠배열로담는다.


BackGround_img = new ImageIcon("background_1.png").getImage();

// 전체배경화면이미지를받습니다.



// 구름을3개동시에그리는데편의상배열로3개를동시에받는다.


Explo_img = new Image[3];

for (int i = 0; i < Explo_img.length ; ++i) {

Explo_img[i] = new ImageIcon("boom_" + i + ".png").getImage();

}

// 폭발애니메이션표현을위해

// 파일이름을넘버마다나눠배열로담는다.

// 모든이미지는Swing의 ImageIcon으로받아이미지넓이,높이// 값을바로얻을수있게한다.


game_Score = 0;// 게임스코어초기화

player_Hitpoint = 3;// 최초플레이어체력


player_Speed = 30; // 유저캐릭터움직이는속도설정

missile_Speed = 8; // 미사일움직임속도설정

fire_Speed = 10; // 미사일연사속도설정

enemy_speed = 3;// 적이날라오는속도설정


}


public void start() { //기본적으로실행하는메소드

setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//프레임오른쪽위X 버튼을클릭시프로그램종료

addKeyListener(this);

//키보드입력처리받아들이기


th = new Thread(this);

//새로운스레드를생성

th.start();

//스레드시작


}


/*

* 스레드
 
*/


public void run() {//스레드실행부분

try {//스레드로인한에러방지용예외처리

while (true) { // 무한루프돌리기


KeyProcess();

EnemyProcess();

MissileProcess();

ExplosionProcess();

//각종처리메소드실행


repaint();

//그래픽처음부터다시그리기


Thread.sleep(20);

//20milli sec 동안스레드슬립


cnt++;

//무한루프돌때마다cnt +1 카운트

if(player_Hitpoint == 0) {
	
	JOptionPane.showMessageDialog(this, "Game Over");
	System.out.println("게임 종료");
	System.exit(0);
	
	
}else if(game_Score == 100) {
	JOptionPane.showMessageDialog(this, "Winner");
	System.exit(0);
	
}else if(Enemy_List.size() > 30) {
	JOptionPane.showMessageDialog(this, "적을 처치 하지 못했습니다.");
	System.exit(0);
}
}

} catch (Exception e) {

}

}


/*

* 메인처리메소드

*/


public void MissileProcess() {//미사일관련처리메소드

if (KeySpace) { //키보드스페이스키입력여부확인

player_Status = 1; //플레이어상태를1로변경


if ((cnt % fire_Speed) == 0) {

//루프카운터의값에서fire_Speed에서설정한값만큼

//나눈값의나머지가0인지여부를확인


ms = new Missile(x + 100, y + 0      , 0, missile_Speed, 0); 

// 기본적인오른쪽직행미사일입니다.

// 각도값은0이기본값입니다.

//왼쪽부터미사일x좌표, y좌표, 미사일진행방향, 미사일속도, 미사일종류

//미사일종류0 : 플레이어가발사하는미사일, 1 : 적이발사하는미사일

Missile_List.add(ms);

//미사일배열에해당위치의미사일추가
Sound("izQ.wav", false);



}

}


for (int i = 0; i < Missile_List.size(); ++i) {

//미사일배열에추가된미사일이얼마나있는지확인

ms = (Missile) Missile_List.get(i);

//배열에존재하는미사일의객체를받아온다.

ms.move();

//해당미사일을움직이게만든다

if (ms.x > f_width - 20 || ms.x < 0 || ms.y < 0 || ms.y > f_height ) {

// 해당미사일이화면밖으로나갔는가여부를확인

Missile_List.remove(i);

//화면끝까지도달한미사일삭제

}


if ( Crash(x, y, ms.x, ms.y, Player_img[0], Missile_img) && ms.who == 1 ) {

//적이발사한미사일이플레이어와충돌하는지여부를확인

player_Hitpoint --;

//플레이어체력포인트를1삭감

ex = new Explosion(x, y, 1);

//플레이어자리에충돌용폭발이펙트객체생성

Explosion_List.add(ex);

//생성한객체를배열로저장
Sound("TmoR.wav", false);

Missile_List.remove(i);

//해당되는적미사일삭제

}


for (int j = 0; j < Enemy_List.size(); ++j) {

//에너미배열에추가된적이얼마나있는지확인

en = (Enemy) Enemy_List.get(j);

//배열에존재하는적의객체를받아온다

if (Crash(ms.x, ms.y, en.x, en.y, Missile_img, Enemy_img) && ms.who == 0) {

//플레이어미사일과적충돌판정

// 미사일의좌표및이미지파일,

// 적의좌표및이미지파일을받아

// 충돌판정메소드로넘기고true,false값을

// 리턴받아true면 아래를실행합니다.


Missile_List.remove(i);

//충돌한미사일삭제
Sound("izQ3.wav", false);
Enemy_List.remove(j);

//충돌한적삭제


game_Score += 1; // 게임점수를+10점.


ex = new Explosion(en.x + Enemy_img.getWidth(null) / 2,

en.y + Enemy_img.getHeight(null) / 2, 0);

// 적이위치해있는곳의중심좌표x,y 값과

// 폭발설정을받은값( 0 또는1 )을받습니다.

// 폭발설정값- 0 : 폭발, 1 : 단순피격


Explosion_List.add(ex);

// 충돌판정으로사라진적의위치에

// 이펙트를추가한다.


}

}

}

}


public void EnemyProcess() {//적의행동관련처리메소드


for (int i = 0; i < Enemy_List.size(); ++i) {

//에너미배열에추가된것이있는지확인

en = (Enemy) (Enemy_List.get(i));

//해당배열의적객체를받아온다.

en.move();

//해당적움직이기

if (en.x < -200) { // 적이x 좌표왼쪽화면끝까지도달여부확인
Enemy_List.remove(i);

//화면끝까지도달한적삭제

}


if ( cnt % 100 == 0){

ms = new Missile (en.x, en.y + 25, 180, missile_Speed, 1);

//확인된해당적의위치에미사일생성

//왼쪽부터미사일x좌표, y좌표, 미사일진행방향, 미사일속도, 미사일종류

//미사일종류0 : 플레이어가발사하는미사일, 1 : 적이발사하는미사일

Missile_List.add(ms);

//생성된미사일을객체로배열에추가

}



if (Crash(x, y, en.x, en.y, Player_img[0], Enemy_img)) {

// 플레이어와적의충돌을판정하여

// boolean값을 리턴받아true면 아래를실행합니다.


player_Hitpoint--; // 플레이어체력을1깍습니다.

Enemy_List.remove(i); // 적을제거합니다.

game_Score += 10;

// 제거된적으로게임스코어를10 증가시킵니다.


ex = new Explosion(en.x + Enemy_img.getWidth(null) / 2, en.y

+ Enemy_img.getHeight(null) / 2, 0);

// 적이위치해있는곳의중심좌표x,y 값과

// 폭발설정을받은값( 0 또는1 )을받습니다.

// 폭발설정값- 0 : 폭발, 1 : 단순피격


Explosion_List.add(ex);

// 제거된적위치에폭발이펙트를추가합니다.

Sound("TmoR.wav", false);

ex = new Explosion(x, y, 1);

// 적이위치해있는곳의중심좌표x,y 값과

// 폭발설정을받은값( 0 또는1 )을받습니다.

// 폭발설정값- 0 : 폭발, 1 : 단순피격


Explosion_List.add(ex);

// 충돌시플레이어의위치에충돌용이펙트를추가.


}

}

if (game_Score<10 ) { //속도
if(cnt % 400 == 0) {
//루프카운트를이용한적등장타이밍조절

if(game_Score%2 ==0) {
en = new Enemy(f_width + 100, 80, enemy_speed);

Enemy_List.add(en);

en = new Enemy(f_width + 100, 490, enemy_speed);

Enemy_List.add(en);
}


else {
// 적움직임속도를추가로받아적을생성한다.
en = new Enemy(f_width + 100, 130, enemy_speed);

Enemy_List.add(en);

en = new Enemy(f_width + 100, 440, enemy_speed);

Enemy_List.add(en);

en = new Enemy(f_width + 100, 550, enemy_speed);

Enemy_List.add(en);

}
}

}

else if (game_Score>=10 && game_Score <30 ) { //속도

	//루프카운트를이용한적등장타이밍조절
if(cnt%100 ==0){
	if(game_Score%2 ==0) {
		en = new Enemy(f_width + 100, 80, enemy_speed);

		Enemy_List.add(en);

		en = new Enemy(f_width + 100, 220, enemy_speed);

		Enemy_List.add(en);
		}


		else {
		// 적움직임속도를추가로받아적을생성한다.
		en = new Enemy(f_width + 100, 300, enemy_speed);

		Enemy_List.add(en);

		en = new Enemy(f_width + 100, 100, enemy_speed);

		Enemy_List.add(en);

		en = new Enemy(f_width + 100, 550, enemy_speed);

		Enemy_List.add(en);
	}


	}
}



else if (game_Score>=30 && game_Score <50 ) { //속도

	//루프카운트를이용한적등장타이밍조절
if(cnt%50 ==0){
	if(game_Score%2 ==0) {
		en = new Enemy(f_width + 100, 80, enemy_speed);

		Enemy_List.add(en);

		en = new Enemy(f_width + 100, 220, enemy_speed);

		Enemy_List.add(en);
		}


		else {
		// 적움직임속도를추가로받아적을생성한다.
		en = new Enemy(f_width + 100, 200, enemy_speed);

		Enemy_List.add(en);

		en = new Enemy(f_width + 100, 240, enemy_speed);

		Enemy_List.add(en);

		en = new Enemy(f_width + 100, 550, enemy_speed);

		Enemy_List.add(en);
	}


	}
}

else if (game_Score>=50 && game_Score <100 ) { //속도

	//루프카운트를이용한적등장타이밍조절
if(cnt%10 ==0){
	if(game_Score%2 ==0) {
		en = new Enemy(f_width + 100, 80, enemy_speed);

		Enemy_List.add(en);

		en = new Enemy(f_width + 100, 540, enemy_speed);

		Enemy_List.add(en);
		}


		else {
		// 적움직임속도를추가로받아적을생성한다.
		en = new Enemy(f_width + 100, 330, enemy_speed);

		Enemy_List.add(en);

		en = new Enemy(f_width + 100, 440, enemy_speed);

		Enemy_List.add(en);

		en = new Enemy(f_width + 100, 550, enemy_speed);

		Enemy_List.add(en);

	}


	}
}
}
	


public void Sound(String file, boolean Loop){

	//사운드재생용메소드

	//메인 클래스에 추가로 메소드를 하나 더 만들었습니다.

	//사운드파일을받아들여해당사운드를재생시킨다.


	Clip clip;


	try {

	AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));

	clip = AudioSystem.getClip();

	clip.open(ais);

	clip.start();


	if ( Loop) clip.loop(-1);

	//Loop 값이true면 사운드재생을무한반복시킵니다.

	//false면 한번만재생시킵니다.


	} catch (Exception e) {

	e.printStackTrace();

	}

	}


public void ExplosionProcess() {

// 폭발이펙트처리용메소드


for (int i = 0; i < Explosion_List.size(); ++i) {

ex = (Explosion) Explosion_List.get(i);

ex.effect();

// 이펙트애니메이션을나타내기위해

// 이펙트처리추가가발생하면해당메소드를호출.


}

}


/*

* 내부계산메소드

*/


public boolean Crash(int x1, int y1, int x2, int y2, Image img1, Image img2) {

// 기존충돌판정소스를변경합니다.

// 이제이미지변수를바로받아해당이미지의넓이, 높이값을

// 바로계산합니다.


boolean check = false;


if (Math.abs((x1 + img1.getWidth(null) / 2)

- (x2 + img2.getWidth(null) / 2)) < (img2.getWidth(null) / 2 + img1

.getWidth(null) / 2)

&& Math.abs((y1 + img1.getHeight(null) / 2)

- (y2 + img2.getHeight(null) / 2)) < (img2

.getHeight(null) / 2 + img1.getHeight(null) / 2)) {

// 이미지넓이, 높이값을바로받아계산합니다.


check = true;// 위값이true면 check에 true를 전달합니다.

} else {

check = false;

}


return check; // check의 값을메소드에리턴시킵니다.


}


/*

* 이미지그리는부분

*/


public void paint(Graphics g) {

//기본적으로페인트에서는 업데이트에그려진그림을가져온다.

buffImage = createImage(f_width, f_height);

buffg = buffImage.getGraphics();


update(g);

}


public void update(Graphics g) {


Draw_Background();

Draw_Player();

Draw_Enemy();

Draw_Missile();

Draw_Explosion();

Draw_StatusText();

//실질적인그림은메소드를통해서버퍼에그린후

//각종이미지를그린메소드를여기에서실행


g.drawImage(buffImage, 0, 0, this);

//버퍼에그려진이미지를가져와실제화면에보이게그린다.

}


public void Draw_Background() {

// 배경이미지를그리는부분입니다.


buffg.clearRect(0, 0, f_width, f_height);

// 화면지우기명령은이제여기서실행합니다.


if (bx > -3500) {

// 기본값이0인bx가 -3500 보다크면실행


buffg.drawImage(BackGround_img, bx, 0, this);

bx -= 1;

// bx를 0에서-1만큼계속줄이므로배경이미지의x좌표는

// 계속좌측으로이동한다. 그러므로전체배경은천천히

// 좌측으로움직이게된다.


} else {

bx = 0;

}



}


public void Draw_Player() {

//플레이어캐릭터를그리는부분


switch (player_Status) {//플레이어의상태를체크한다


case 0: // 평상시

if ((cnt / 5 % 2) == 0) {

buffg.drawImage(Player_img[1], x, y, this);


} else {

buffg.drawImage(Player_img[2], x, y, this);

}

// 깜박거림 해결


break;


case 1: // 미사일발사

if ((cnt / 5 % 2) == 0) {

buffg.drawImage(Player_img[3], x, y, this);

} else {

buffg.drawImage(Player_img[4], x, y, this);

}

// 미사일을쏘는듯한이미지를번갈아그려준다.


player_Status = 0;

// 미사일쏘기가끝나면플레이어상태를0으로돌린다.


break;

case 2: // 충돌

break;


}


}


public void Draw_Missile() {

//미사일이미지를그리는부분

for (int i = 0; i < Missile_List.size(); ++i) {

//미사일배열에값이존재하면

ms = (Missile) (Missile_List.get(i));

//객체화시켜배열에저장된미사일을하나씩가져온다

if ( ms.who == 0 )buffg.drawImage(Missile_img, ms.x, ms.y, this);

//플레이어가발사한이미지를그린다.

if ( ms.who == 1 )buffg.drawImage(Missile2_img, ms.x, ms.y, this);

//적이발사한이미지를그린다.

}

}


public void Draw_Enemy() {

//적이미지를그리는부분

for (int i = 0; i < Enemy_List.size(); ++i) {

en = (Enemy) (Enemy_List.get(i));

buffg.drawImage(Enemy_img, en.x, en.y, this);

}

}


public void Draw_Explosion() {

// 폭발이펙트를그리는부분입니다.


for (int i = 0; i < Explosion_List.size(); ++i) {

ex = (Explosion) Explosion_List.get(i);

// 폭발이펙트의존재유무를체크하여리스트를받음.


if (ex.damage == 0) {

// 설정값이0 이면폭발용이미지그리기


if (ex.ex_cnt < 7) {

buffg.drawImage(Explo_img[0],

ex.x - Explo_img[0].getWidth(null) / 2, ex.y

- Explo_img[0].getHeight(null) / 2, this);

} else if (ex.ex_cnt < 14) {

buffg.drawImage(Explo_img[1],

ex.x - Explo_img[1].getWidth(null) / 2, ex.y

- Explo_img[1].getHeight(null) / 2, this);

} else if (ex.ex_cnt < 21) {

buffg.drawImage(Explo_img[2],

ex.x - Explo_img[2].getWidth(null) / 2, ex.y

- Explo_img[2].getHeight(null) / 2, this);

} else if (ex.ex_cnt > 21) {

Explosion_List.remove(i);

ex.ex_cnt = 0;

// 폭발은따로카운터를계산하여

// 이미지를순차적으로그림.

}

} else { // 설정값이1이면단순피격용이미지그리기

if (ex.ex_cnt < 7) {

buffg.drawImage(Explo_img[0], ex.x + 120, ex.y + 15, this);

} else if (ex.ex_cnt < 14) {

buffg.drawImage(Explo_img[1], ex.x + 60, ex.y + 5, this);

} else if (ex.ex_cnt < 21) {

buffg.drawImage(Explo_img[0], ex.x + 5, ex.y + 10, this);

} else if (ex.ex_cnt > 21) {

Explosion_List.remove(i);

ex.ex_cnt = 0;

// 단순피격또한순차적으로이미지를그리지만

// 구분을위해약간다른방식으로그립니다.


}

}

}

}


public void Draw_StatusText() { // 상태체크용텍스트를그립니다.


buffg.setFont(new Font("Defualt", Font.BOLD, 20));

// 폰트설정을합니다. 기본폰트, 굵게, 사이즈20


buffg.drawString("SCORE : " + game_Score, 500, 550);

// 좌표x : 1000, y : 70에스코어를표시합니다.


buffg.drawString("남은 목숨 : " + player_Hitpoint, 500, 570);

// 좌표x : 1000, y : 90에플레이어체력을표시합니다.


//buffg.drawString("Missile Count : " + Missile_List.size(), 1000, 110);

// 좌표x : 1000, y : 110에나타난미사일수를표시합니다.


//buffg.drawString("Enemy Count : " + Enemy_List.size(), 1000, 130);

// 좌표x : 1000, y : 130에나타난적의수를표시합니다.


}


/*

* 키보드입력처리부분

*/


public void KeyProcess() {

if (KeyUp == true) {

if (y > 20)

y -= 5;

// 캐릭터가보여지는화면위로못넘어가게합니다.


player_Status = 0;

// 이동키가눌려지면플레이어상태를0으로돌립니다.
//유저캐릭터상태체크변수0 : 평상시, 1: 미사일발사, 2: 충돌

}


if (KeyDown == true) {

if (y + Player_img[0].getHeight(null) < f_height)

y += 5;

// 캐릭터가보여지는화면아래로못넘어가게합니다.


player_Status = 0;

// 이동키가눌려지면플레이어상태를0으로돌립니다.

}


if (KeyLeft == true) {

if (x > 0)

x -= 5;

// 캐릭터가보여지는화면왼쪽으로못넘어가게합니다.


player_Status = 0;

// 이동키가눌려지면플레이어상태를0으로돌립니다.

}


if (KeyRight == true) {

if (x + Player_img[0].getWidth(null) < f_width)

x += 5;

// 캐릭터가보여지는화면오른쪽으로못넘어가게합니다.


player_Status = 0;

// 이동키가눌려지면플레이어상태를0으로돌립니다.

}

}


public void keyPressed(KeyEvent e) {


switch (e.getKeyCode()) {

case KeyEvent.VK_UP:

KeyUp = true;

break;

case KeyEvent.VK_DOWN:

KeyDown = true;

break;

case KeyEvent.VK_LEFT:

KeyLeft = true;

break;

case KeyEvent.VK_RIGHT:

KeyRight = true;

break;


case KeyEvent.VK_SPACE:

KeySpace = true;

break;

}

}


public void keyReleased(KeyEvent e) {


switch (e.getKeyCode()) {

case KeyEvent.VK_UP:

KeyUp = false;

break;

case KeyEvent.VK_DOWN:

KeyDown = false;

break;

case KeyEvent.VK_LEFT:

KeyLeft = false;

break;

case KeyEvent.VK_RIGHT:

KeyRight = false;

break;


case KeyEvent.VK_SPACE:

KeySpace = false;

break;


}

}


public void keyTyped(KeyEvent e) {}

//본게임에서는사용하지않는메소드이나

//키리스너를상속받았기때문에기본적으로생성해둬야

//에러가발생하지않는다.


}


/*

* 객체화를위한클래스관리부분

*/










