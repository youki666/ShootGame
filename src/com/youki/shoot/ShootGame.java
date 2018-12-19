package com.youki.shoot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShootGame extends JPanel {
     
	public static final  int Width = 400;//窗口宽
	public static final  int Height = 654;//窗口高
	public static BufferedImage bg;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage over;
	public static BufferedImage airplane;
	public static BufferedImage bullet;
	public static BufferedImage bee;
	public static BufferedImage hero1;
	public static BufferedImage hero0;
	
	static final int Start = 0;
	static final int Running = 1;
	static final int Pause = 2;
	static final int Over = 3;
	static int State = Start;
	
	
	private Hero hero = new Hero();//英雄机对象
	private FlyObj [] flyobj = {};//飞行物对象
	private Bullet [] bullets = {};//子弹数组
	
	
	public FlyObj nextOne() {
		
		Random rand = new Random();
		int num = rand.nextInt(20);
		if(num <= 4) {
			return new Bee();
		}else {
			return new Airplane();
		}
	}
	//敌机 bee 入场
	int flyIndex = 0;
	public void enterAction() {
		flyIndex++;
		if(flyIndex % 40 ==0) {
			FlyObj one = nextOne();
			flyobj = Arrays.copyOf(flyobj, flyobj.length+1) ;
			flyobj[flyobj.length-1] = one;
		}
	}
	public void stepAction() {
		hero.step();
		for(int i =0;i<flyobj.length;i++) {
			flyobj[i].step();
		}
		for(int i =0;i<bullets.length;i++) {
			bullets[i].step();
		}
		
	}
	
	int shootIndex = 0;
	public void shootAction() {
		 shootIndex++;
		 if(shootIndex %30 ==0) {
			 Bullet [] bs = hero.shoot();
			 bullets = Arrays.copyOf(bullets, bullets.length+bs.length);//扩充子弹数组长度
			 System.arraycopy(bs,0,bullets,bullets.length-bs.length ,bs.length );;
		 }
		
	}
	public void outOfboundAction() {//删除越界flyobj
		int index = 0;
		FlyObj [] flylives = new FlyObj[flyobj.length];
		for(int i=0;i<flylives.length;i++) {
			FlyObj f = flyobj[i];
			if(!f.outOfbounds()) {
				flylives[index]  = f;
				index++;
			}
		}
		flylives = Arrays.copyOf(flylives, index);
	}
	public void bangAction() {
		for(int i =0;i<bullets.length;i++) {
			Bullet b = bullets[i];
			bang(b);
		}
	}
	int score;//得分
    public void bang(Bullet b) {
    	int index = -1;
		for(int j = 0;j<flyobj.length;j++) {
			FlyObj f = flyobj[j];
			if(f.shootBy(b)) {
				index = j;//记录被撞飞行物的下标
				break;
			}
		}
		if(index != -1) {
			FlyObj one = flyobj[index];//获取被撞敌人
			if(one instanceof Enemy) {
				Enemy e = (Enemy)one;
				score +=e.getScore();
			}else if(one instanceof Award) {
				Award a = (Award)one;
				int type = a.getType();
				switch(type) {
				case Award.Double_Fire :
					hero.doubleFireAdd();
					break;
				case Award.Life:		
					hero.lifeAdd();
					break;
				}	
			}
		    FlyObj f = 	flyobj[index];
		    flyobj[index]= flyobj[flyobj.length-1];
		    flyobj[flyobj.length-1] =f;
		    flyobj = Arrays.copyOf(flyobj, flyobj.length-1);
		}
	}
    public void checkGameoverAction() {
    	if(isGameoverAction()) {//游戏结束
    		State = Over;
    	}
    }
    public boolean isGameoverAction() {
    	for(int i=0;i<flyobj.length;i++) {
    		FlyObj f = flyobj[i];
    		if(hero.hit(f)) {
    			hero.lifeSub();
    			hero.doubleFireClear();
    			//将被撞敌人与敌人数组最后一个交换
    			FlyObj fobj = 	flyobj[i];
    		    flyobj[i]= flyobj[flyobj.length-1];
    		    flyobj[flyobj.length-1] =f;
    		    //去掉飞行物最后一个即被撞敌人
    		    flyobj = Arrays.copyOf(flyobj, flyobj.length-1);	
    		}
    	}
    	return hero.getLife()<=0;
    }
	public void action() {
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(State == Running) {
					 int x = e.getX();
					 int y = e.getY();
					 hero.movedTo(x, y);
				}
			}
			public void mouseClicked(MouseEvent e) {
				switch(State) {
				case Start:
					State = Running;
					break;
				case Pause:
					State = Pause;
					break;
				case Over:
					score = 0;//数据归零
					hero = new Hero();
					flyobj = new FlyObj[0];
					bullets = new Bullet[0];
					State = Start;
					break;
				}
			}
			public void mouseExited(MouseEvent e) {
				 if(State == Running) {
					 State = Pause;
				 }
			}
			public void mouseEntered(MouseEvent e) {
				 if(State == Pause) {
					 State = Running;
				 }
			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		//定时器设置子弹敌机定时出现
		Timer tm = new Timer();
		int interval = 10;
		tm.schedule(new TimerTask(){

			@Override
			public void run() {
				if(State == Running) {
					enterAction();
					stepAction();
					shootAction();
					outOfboundAction();
					bangAction();
					checkGameoverAction();
				}
				repaint();
			}
			
		},interval,interval);
		
	}
	static {
		try {
			bg = ImageIO.read(ShootGame.class.getResource("bg.png"));
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			over = ImageIO.read(ShootGame.class.getResource("over.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
 	}//重写paint方法  画背景飞机蜜蜂等
    public void paint (Graphics g) {
    	 g.drawImage(bg, -10, -60, null);
    	 paintHero(g);
    	 paintFlyobj(g);
    	 paintBullets(g);
    	 paintScoreAddLife(g);
    	 paintState(g);
    }
    public void paintScoreAddLife (Graphics g) {
    	g.setColor(new Color(0xFF0000));
    	g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
      	 g.drawString("Score:"+score, 40,40);
      	g.drawString("Life:"+hero.getLife(), 40,80);
      }
    //画飞机
    public void paintHero (Graphics g) {
   	 g.drawImage(hero.img, hero.x, hero.y, null);
   }
  //画飞行物
    public void paintFlyobj (Graphics g) {
   	 for(int i=0;i<flyobj.length;i++) {
   		FlyObj f = flyobj[i];
   		g.drawImage(f.img,f.x,f.y,null);
   	 }
   }
  //画子弹
    public void paintBullets (Graphics g) {
    	for(int i=0;i<bullets.length;i++) {
       		FlyObj b = bullets[i];
       		g.drawImage(b.img,b.x,b.y,null);
       	 }
   }
  //画游戏状态
    public void paintState (Graphics g) {
    	switch(State) {
    	case Start:
    	  g.drawImage(start,180,280,null);
    		break;
    	case Pause:
     	   g.drawImage(pause,0,0,null);
     		break;
    	case Over:
     	   g.drawImage(over,0,0,null);
     		break;
    	}
   }
	public static void main(String[] args) {
      JFrame jf = new JFrame("Fly");
	  ShootGame sg = new ShootGame();
	  jf.add(sg);
	  jf.setSize(Width, Height);
	  jf.setAlwaysOnTop(true);
	  jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  jf.setLocationRelativeTo(null);
	  jf.setVisible(true);
	  sg.action();
	}

}
