package com.youki.shoot;

import java.util.Random;

public class Bee extends FlyObj implements Award{
     
	private int xSpeed = 1;
	private int ySpeed = 1;
	private int awardType ;//奖励类型0、1
	@Override
	public int getType() {
		
		return awardType;
	}
	public Bee() {
		img = ShootGame.bee;
		width = img.getWidth();
		height = img.getHeight();
		Random rand = new Random();
		 x = rand.nextInt(ShootGame.Width-this.width);
		 y = -this.height;
		 awardType = rand.nextInt(2);
	}
	int n = 0;
	@Override
	
	public void step() {
		x += xSpeed;
		y += ySpeed;
		 n++;
		if(x > ShootGame.Width-this.width) {
			x = -1;
		}
		if(x < 0) {
			x = 1;
		}
	}
	@Override
	public boolean outOfbounds() {
		
		return  this.y >= ShootGame.Height;
	}

}
