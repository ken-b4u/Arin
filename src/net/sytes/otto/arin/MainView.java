package net.sytes.otto.arin;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements
SurfaceHolder.Callback, Runnable {

	private SurfaceHolder holder;
	private Thread thread;
	private long interval = 100;
	private Runnable runnable;
	private Handler handler = new Handler();

	private Paint paint = null;		// 描画用
	private Bitmap button,button2;	// ボタン
	private Bitmap back;			// 背景
	private Bitmap zImage;			// Zイメージ
	public Counter counter;			//ボタンのクリック回数
	private ArinPlayer arinPlayer;	// あーりんプレイヤー
	private ArrayList<MovingImage> images;	// Zイメージたち
	public Selector selector;				// セレクター
	private Context context;
	public MainView(Context context) {
		super(context);


		this.context = context;

		//クリック回数
		counter = new Counter(context);

		// 描画用の準備
		paint = new Paint();
		paint.setColor(Color.WHITE);

		// リソースからビットマップを取り出す
		Resources r = getResources();
		button = BitmapFactory.decodeResource(r, R.drawable.button);
		button2 = BitmapFactory.decodeResource(r, R.drawable.button2);
		back = BitmapFactory.decodeResource(r, R.drawable.back);
		zImage = BitmapFactory.decodeResource(r, R.drawable.zimage);


		// イメージたち
		images = new ArrayList<MovingImage>();
		for(int i=0;i<3;i++)
			images.add(new MovingImage(zImage));

		// あーりんプレイヤー作成
		arinPlayer = new ArinPlayer(context);


		// getHolder()メソッドでSurfaceHolderを取得。さらにコールバックを登録
		getHolder().addCallback(this);
		// タイマー処理を開始
		runnable = new Runnable() {
			public void run() {
				TimerEvent();
				handler.postDelayed(this, interval);
			}
		};
		handler.postDelayed(runnable, interval);
	}

	private void TimerEvent() {

	}

	// SurfaceView生成時に呼び出される
	public void surfaceCreated(SurfaceHolder holder) {
		// セレクター
		selector = new Selector(context,this.getWidth());
		// 選択肢を更新
		selector.setMax( counter.n /10 );

		this.holder = holder;
		thread = new Thread(this);

	}

	// SurfaceView変更時に呼び出される
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		// スレッドスタート
		if (thread != null) {
			thread.start();
		}
	}

	// SurfaceView破棄時に呼び出される
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread = null;
	}

	// スレッドによるSurfaceView更新処理
	public void run() {
		while (thread != null) {

			// オブジェクト移動
			for (int i = 0; i < this.images.size(); i++) {
				images.get(i).move(getWidth(),getHeight());
			}
			// 描画処理
			Canvas canvas = holder.lockCanvas();
			this.draw(canvas);
			holder.unlockCanvasAndPost(canvas);

		}
	}

	// 描画処理
	@Override
	public void draw(Canvas canvas) {
		if(canvas==null){
			return;
		}
		// 背景色を設定
		canvas.drawColor(Color.MAGENTA);

		// グリッドを描画
		Paint pa = new Paint();
		pa.setColor(Color.argb(75, 255, 255, 255));
		pa.setStrokeWidth(1);
		for (int y = 0; y < 800; y = y + 10) {
			canvas.drawLine(0, y, 479, y, pa);
		}
		for (int x = 0; x < 480; x = x + 10) {
			canvas.drawLine(x, 0, x, 799, pa);
		}

		// 背景を描画
		Rect src = new Rect(0,0,back.getWidth(),back.getHeight());
		Rect dst = new Rect(0,0,getWidth(),getHeight());
		canvas.drawBitmap(back,src,dst,paint);

		// セレクターを描画
		selector.Draw(canvas);

		// ボタンを描画
		int bx = getWidth()*4/5, by = getHeight()/4;
		src = new Rect(0,0,button.getWidth(),button.getHeight());
		dst = new Rect(
				getWidth()/2 - bx/2,
				getHeight()*3/4 - by/2,
				getWidth()/2 + bx/2,
				getHeight()*3/4+by/2
		);
		if(arinPlayer.isPlaying()){
			canvas.drawBitmap(button2, src, dst, paint);
		}else{
			canvas.drawBitmap(button, src, dst, paint);
		}

		// 押された回数を描画する
		Paint p = new Paint();
		p.setColor(Color.MAGENTA);
		p.setTextSize(50);
		String s = String.format("%1$04dあーりんだよぉ", counter.read());
		canvas.drawText(s, 0, getHeight()-40, p);

		// オブジェクトを描画
		for (int i = 0; i < this.images.size(); i++) {
			images.get(i).draw(canvas);
		}

	}

	// タッチイベント
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			return true;
		}
		// ボタンのクリック
		int bx = getWidth() * 4 / 5, by = getHeight() / 4;
		Rect dst = new Rect(getWidth() / 2 - bx / 2, getHeight() * 3 / 4 - by
				/ 2, getWidth() / 2 + bx / 2, getHeight() * 3 / 4 + by / 2);
		if (dst.contains((int) event.getX(), (int) event.getY())) {
			// 音楽が再生中でなければ再生する
			if (!arinPlayer.isPlaying()) {
				//arinPlayer.startRandom();
				arinPlayer.startIndex( selector.selectedIndex());
				counter.add();
			}
		}
		// 選択肢を更新
		selector.setMax( counter.n /10 );
		// 選択
		selector.touch((int)event.getX(),(int) event.getY());
		/*
		if( event.getX() < 100){
			selector.select( (int)event.getY() / 100 );
		}
		*/
		return true;
	}
}