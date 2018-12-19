package com.youki.shoot;

import java.awt.image.BufferedImage;
//飞行物类
public abstract class FlyObj {
	protected BufferedImage img;//图
	protected int width;  //宽
	protected int height;//高
	protected int x; //横坐标
	protected int y;
	//飞行物走一步
	public abstract void step();
	
	public abstract boolean outOfbounds();
	
	public boolean shootBy(Bullet b) {
		int x1 = this.x;
		int x2 = this.x+this.width;
		int y1 = this.y;
		int y2 = this.y+this.height;
		int x = b.x;
		int y = b.y;
		return x > x1 && x < x2 && y > y1 && y < y2;
	}
}
