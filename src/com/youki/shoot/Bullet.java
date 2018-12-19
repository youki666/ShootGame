package com.youki.shoot;

import java.util.Random;

//子弹类 继承飞行物
public class Bullet extends FlyObj {
       
	private int speed = 3;
   //xy代表子弹位置，根据hero得到 
	public Bullet(int x,int y) {
		img = ShootGame.bullet;
		width = img.getWidth();
		height = img.getHeight();
		//System.out.println(width);
		this.x = x;
		this.y = y;
		//创建子弹对象，加入子弹对象数组
	}
	@Override
	public void step() {
		y -= speed;
	}
	@Override
	public boolean outOfbounds() {
		
		return  this.y <= -this.height;
	}
}
