package com.youki.shoot;

import java.util.Random;

public class Airplane extends FlyObj implements Enemy {
     
	private int speed = 2; //敌机速度
	@Override
	public int getScore() {
		
		return 5;//打掉一个得五分
	}
	public Airplane() {
		//super();
		img = ShootGame.airplane;
		width = img.getWidth();
		height = img.getHeight();
		Random rand = new Random();
		 x = rand.nextInt(ShootGame.Width-this.width);
		 y = -this.height;
	}
	@Override
	public void step() {
		y += speed;
	}
	@Override
	public boolean outOfbounds() {
	
		return  this.y >= ShootGame.Height;
	}
	

}
