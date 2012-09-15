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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class MainView extends SurfaceView
implements SurfaceHolder.Callback, Runnable {

	private Paint paint = null;		// 描画用
    private Thread mainLoop = null;	// スレッド
    private Bitmap image;			// イラスト
    private Bitmap button,button2;	// ボタン
    private Bitmap back;			// 背景
    private Bitmap zImage;			// Zイメージ
    private Counter counter;		//ボタンのクリック回数
    private ArinPlayer arinPlayer;	// あーりんプレイヤー

    private MovingImage arinImage;			// あーりんのイメージ
    private ArrayList<MovingImage> images;	// Zイメージたち

    public MainView(Context context) {
        super(context);
        // SurfaceView描画に用いるコールバックを登録する。
        getHolder().addCallback(this);

        //クリック回数
        counter = new Counter(context);

        // 描画用の準備
        paint = new Paint();
        paint.setColor(Color.WHITE);

        // スレッド開始
        mainLoop = new Thread(this);
        mainLoop.start();

        // リソースからビットマップを取り出す
        Resources r = getResources();
        image = BitmapFactory.decodeResource(r, R.drawable.image1);
        button = BitmapFactory.decodeResource(r, R.drawable.button);
        button2 = BitmapFactory.decodeResource(r, R.drawable.button2);
        back = BitmapFactory.decodeResource(r, R.drawable.back);
        zImage = BitmapFactory.decodeResource(r, R.drawable.zimage);

        // あーりんのイメージ
        this.arinImage = new MovingImage(image,0,0,1,0);

        // イメージたち
        images = new ArrayList<MovingImage>();
        for(int i=0;i<3;i++)
        	images.add(new MovingImage(zImage));

        // あーりんプレイヤー作成
        arinPlayer = new ArinPlayer(context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 背景を変更
    	/*
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.MAGENTA);
        holder.unlockCanvasAndPost(canvas);
        */
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	mainLoop = null;
    }

    // タッチイベント
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 音楽が再生中でなければ再生する
    	if(!arinPlayer.isPlaying()){
    		arinPlayer.startRandom();
    		counter.add();
    	}
    	return true;
    }

    // 更新処理
    private void Update(){
        // イラストの移動
    	this.arinImage.move(getWidth(), getHeight());

     // オブジェクト移動
		for (int i = 0; i < this.images.size(); i++) {
			images.get(i).move(getWidth(),getHeight());
		}
    }

    // 描画処理
    private void Draw(Canvas canvas){
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

        // イラストを描画
        arinImage.draw(canvas);

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

    // 描画と更新を合わせたもの
    @Override
    public void run() {
        // Runnableインターフェースをimplementsしているので、runメソッドを実装する
        // これは、Threadクラスのコンストラクタに渡すために用いる。
        while (true) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null)
            {
            	Update();
            	Draw(canvas);
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
}
