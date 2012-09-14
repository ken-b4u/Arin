package net.sytes.otto.arin;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

public class MovingImage {
	private PointF  point;		//オブジェクトの座標
	public PointF  speed;		//スピード
	private Bitmap bitmap;
	private Paint paint;
	Random rand = new Random();

	public MovingImage(Bitmap bitmap){

		this.point = new PointF(rand.nextInt(1000)-200,rand.nextInt(1000)-200);
		this.speed = new PointF(3,3);
		this.bitmap= bitmap;
		this.paint = new Paint();
	}

	public MovingImage(Bitmap bitmap,int x,int y,int speedX,int speedY){
		this.point = new PointF(x,y);
		this.speed = new PointF(speedX,speedY);
		this.bitmap= bitmap;
		this.paint = new Paint();
	}

	/* 動かす(引数1:Viewのサイズ)  */
	public void move(Point size){
		point.x += speed.x;
		point.y += speed.y;
		if(point.x > size.x-bitmap.getWidth()){
			speed.x = -speed.x;
			point.x = size.x-bitmap.getWidth();
		}
		else if(point.x<0){
			speed.x = -speed.x;
			point.x = 0;
		}
		if(point.y > size.y-bitmap.getHeight()){
			speed.y= -speed.y;
			point.y = size.y-bitmap.getHeight();
		}
		else if(point.y<0){
			speed.y = -speed.y;
			point.y = 0;
		}
	}

	public void move(int width, int height) {
		move( new Point(width,height));
	}
	//描画
	public void draw(Canvas canvas){
		canvas.drawBitmap(bitmap, point.x,point.y, paint);
	}


}