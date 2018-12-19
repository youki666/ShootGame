package com.youki.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Hero extends FlyObj{
       private int doubleFire;
       
       private int Life;
       private BufferedImage [] imgs;
       private int index;
   	public Hero() {
		img = ShootGame.hero0;
		width = img.getWidth();
		height = img.getHeight();
		Random rand = new Random();
		 x = 150;
		 y = 400;
		 Life = 3;
		 doubleFire = 0;
		 imgs = new BufferedImage[]{ShootGame.hero0,ShootGame.hero1};
		 index = 0;  //切换图片
		 
	}
	@Override
	public void step() { //10毫秒走一次
		img = imgs[index++/10%imgs.length];
		/*index++;
		int a = index/10;
		int b = a%2;
		img = imgs[b];*/
	}
	      //发射子弹
	public Bullet [] shoot() {
		int xStep = this.width/4;
		int yStep = 20; 
		if(doubleFire>0) {//双倍火力
			Bullet [] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+xStep,this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep,this.y-yStep);
			doubleFire -=2;//发射一次双倍火力-2;
			return bs;
		}else {//单倍火力
			Bullet [] bs = new Bullet[1];
			bs[0] = new Bullet(this.x+2*xStep,this.y-yStep);
			return bs;
		}
		
	}
	public void movedTo(int x,int y) {
		this.x = x-this.width/2;
		this.y = y-this.height/2;
	}
	@Override
	public boolean outOfbounds() {
		
		return false;
	}
	public  void lifeAdd() {
		Life++;//飞机加命
	}
	public int getLife() {
		return Life;
	}
	public  void doubleFireAdd() {
		doubleFire+=40;//飞机加火力
	}
	public void lifeSub() {
		Life--;
	}
	public  void doubleFireClear() {
		doubleFire = 0;//飞机火力0;
	}
	
	public boolean hit(FlyObj f) {
		int x1 = f.x-this.width/2;
		int x2 = f.x+f.width+this.width/2;
		int y1 = f.y-this.height/2;
		int y2 = f.y+f.width+this.height/2;
		int x = this.x+this.width/2;
		int y = this.y+this.height/2;
		return x>x1 && x<x2 && y>y1 && y<y2;
	}
       
       
}
