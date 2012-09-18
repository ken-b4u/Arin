package net.sytes.otto.arin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Selector {
	private int n=0;			// 選択できる最大
	private int index = 0;		// 今選択されている場所
	private Bitmap images[];
	private Bitmap selected,unselected;
	private Rect src;			// 元画像
	private Rect dst[];			// 表示する場所
	private Paint paint;
	private int border[] =		// いつ新しいのを開放するか
			{0,20,40,80,160,320};

	public Selector(Context context,int width){
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(30);
		images = new Bitmap[6];
		Resources r = context.getResources();
		images[0] = BitmapFactory.decodeResource(r, R.drawable.n0);
		images[1] = BitmapFactory.decodeResource(r, R.drawable.n1);
		images[2] = BitmapFactory.decodeResource(r, R.drawable.n2);
		images[3] = BitmapFactory.decodeResource(r, R.drawable.n3);
		images[4] = BitmapFactory.decodeResource(r, R.drawable.n4);
		images[5] = BitmapFactory.decodeResource(r, R.drawable.n5);
		selected = BitmapFactory.decodeResource(r, R.drawable.selected);
		unselected = BitmapFactory.decodeResource(r, R.drawable.unselected);
		src = new Rect(0,0,200,200);
		int x = width/13;
		dst = new Rect[6];
		for(int i=0;i<6;i++){
			dst[i] = new Rect(
					x*( 4*(i%3)+1),
					x* (4 *(i/3) +1),
					0,
					0);
			dst[i].right = dst[i].left + 3*x;
			dst[i].bottom = dst[i].top + 3*x;
		}
	}

	public void Draw(Canvas canvas){
		for(int i=0;i<images.length;i++){
			String s = String.valueOf(border[i]);
			canvas.drawText(s, dst[i].left,dst[i].top, paint);
			if(i==selectedIndex()){
				canvas.drawBitmap(selected, src,dst[i],paint);
				canvas.drawBitmap(images[i], src,dst[i],paint);
			}else if (i<=n){
				canvas.drawBitmap(unselected, src,dst[i],paint);
				canvas.drawBitmap(images[i], src,dst[i],paint);
			}
			else{
				canvas.drawBitmap(unselected, src,dst[i],paint);
			}
		}
	}

	public void update(int count){
		for(int i=5;i>=0;i--){
			if( count >= border[i]){
				setMax(i);
				return;
			}
		}
	}

	public boolean select(int i){
		if(i<=n){
			index = i;
			return true;
		}
		return false;
	}

	public int selectedIndex(){
		return index;
	}

	public void setMax(int max){
		n = max;
		if(n>5){
			n = 5;
		}
	}

	public void reset(){
		select(0);
		n = 0;
	}

	public void touch(int x,int y){
		for(int i=0;i<6; i++){
			if( dst[i].contains(x,y)){
				select(i);
			}
		}
	}
}
