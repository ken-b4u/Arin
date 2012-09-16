package net.sytes.otto.arin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Selector {
	private int n=0;			// 選択できる最大
	private int index = 0;		// 今選択されている場所
	private Bitmap images[];
	private Bitmap selected,unselected;
	private Rect src;
	private Rect dst[];

	public Selector(Context context,int width){
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
		Paint paint = new Paint();

		for(int i=0;i<images.length;i++){

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

	public void add(){
		setMax(n+1);
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
