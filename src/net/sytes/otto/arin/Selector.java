package net.sytes.otto.arin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Selector {
	private int n=0;			// 最大
	private int index = 0;		// 今選択されている場所
	private Bitmap images[];
	private Bitmap select;

	public Selector(Context context){
		images = new Bitmap[10];
		Resources r = context.getResources();
		images[0] = BitmapFactory.decodeResource(r, R.drawable.n0);
		images[1] = BitmapFactory.decodeResource(r, R.drawable.n1);
		images[2] = BitmapFactory.decodeResource(r, R.drawable.n2);
		images[3] = BitmapFactory.decodeResource(r, R.drawable.n3);
		images[4] = BitmapFactory.decodeResource(r, R.drawable.n4);
		images[5] = BitmapFactory.decodeResource(r, R.drawable.n5);
		images[6] = BitmapFactory.decodeResource(r, R.drawable.n6);
		images[7] = BitmapFactory.decodeResource(r, R.drawable.n7);
		images[8] = BitmapFactory.decodeResource(r, R.drawable.n8);
		images[9] = BitmapFactory.decodeResource(r, R.drawable.n9);
		select = BitmapFactory.decodeResource(r, R.drawable.select);
	}

	public void Draw(Canvas canvas){
		Paint paint = new Paint();
		for(int i=0;i<=n;i++){
			if(i==selectedIndex()){
				canvas.drawBitmap(select, 0, i*100, paint);
			}
			canvas.drawBitmap(images[i], 0, i*100, paint);
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
}
