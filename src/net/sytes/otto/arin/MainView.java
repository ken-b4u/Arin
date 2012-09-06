package net.sytes.otto.arin;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class MainView extends SurfaceView 
implements SurfaceHolder.Callback, Runnable {
	// スレッドクラス
    Thread mainLoop = null;
    // 描画用
    Paint paint = null;
    
	// 円のX,Y座標
    private int circleX = 10;
    private int circleY = 10;
    // 円の移動量
    private int circleVx = 5;
    private int circleVy = 5;
    
    private MediaPlayer mp[];
    //private Context context;
    
    private Bitmap bmp;
    private Bitmap button;
    
    private int count = 0;
    
    public MainView(Context context) {
        super(context);
        // SurfaceView描画に用いるコールバックを登録する。
        getHolder().addCallback(this);
        // 描画用の準備
        paint = new Paint();
        paint.setColor(Color.WHITE);
        // スレッド開始
        mainLoop = new Thread(this);
        mainLoop.start();
        
     // リソースからビットマップを取り出す
        Resources r = getResources();
        bmp = BitmapFactory.decodeResource(r, R.drawable.image1);
        button = BitmapFactory.decodeResource(r, R.drawable.button);
        
        mp = new MediaPlayer[6];
        mp[0]=MediaPlayer.create(context,R.raw.sample0);
        mp[1]=MediaPlayer.create(context,R.raw.sample1);
        mp[2]=MediaPlayer.create(context,R.raw.sample2);
        mp[3]=MediaPlayer.create(context,R.raw.sample3);
        mp[4]=MediaPlayer.create(context,R.raw.sample4);
        mp[5]=MediaPlayer.create(context,R.raw.sample5);
        //this.context = context;
    }

    private boolean isPlaying(){
    	for(int i=0;i<6;i++){
    		if(mp[i].isPlaying()){
    			return true;
    		}
    	}
    	return false;
    }
    private void start(){
    	int x = new Random().nextInt(6);
    	mp[x].start();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        // TODO 今回は何もしない。
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // SurfaceView生成時に呼び出されるメソッド。
        // 今はとりあえず背景を白にするだけ。
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.MAGENTA);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO 今回は何もしない。
    	mainLoop = null;
    }
 // タッチイベントを処理するためOverrideする
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 移動する方向を反転させる
        // onTouchEventでは、以下の動きをとらえる事ができる
        //  [ACTION_DOWN] [ACTION_UP] [ACTION_MOVE] [ACTION_CANCEL]
        // 今回はACTION_DOWN(タッチパネルが押されたとき)に、ボールの動きを反転させます
        switch (event.getAction())
        {
        case MotionEvent.ACTION_DOWN:
            //circleVx *= -1;
            //circleVy *= -1;
            break;
        default:
            break;
        }
        //音楽再生
       /*
        if(!mp1.isPlaying()){ 
        	mp1.start();
        }
        */
        if(!this.isPlaying()){
        	this.start();
        	count++;
        }
        return true;
    }

    @Override
    public void run() {
        // Runnableインターフェースをimplementsしているので、runメソッドを実装する
        // これは、Threadクラスのコンストラクタに渡すために用いる。
        while (true) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null)
            {
                canvas.drawColor(Color.MAGENTA);
                // グリッドを描画する
                Paint pa = new Paint();
                pa.setColor(Color.argb(75, 255, 255, 255));
                pa.setStrokeWidth(1);
                for (int y = 0; y < 800; y = y + 10) {
                    canvas.drawLine(0, y, 479, y, pa);
                }
                for (int x = 0; x < 480; x = x + 10) {
                    canvas.drawLine(x, 0, x, 799, pa);
                }

                // 円を描画する
                //canvas.drawCircle(circleX, circleY, 10, paint);
                canvas.drawBitmap(bmp, circleX, circleY, paint);
                canvas.drawBitmap(button, getWidth()/2, getHeight()*2/3,paint);
                //テキストを描画する
                Paint p = new Paint();
                p.setColor(Color.BLACK);
                p.setTextSize(50);
                String s = String.format("%1$04dあーりんだよぉ", count);
                canvas.drawText(s, 0, 400, p);
                getHolder().unlockCanvasAndPost(canvas);
                // 円の座標を移動させる
                circleX += circleVx;
                //circleY += circleVy;
                // 画面の領域を超えた？
                if (circleX < 0 || getWidth() < circleX+bmp.getHeight())  circleVx *= -1;
                if (circleY < 0 || getHeight() < circleY+bmp.getWidth()) circleVy *= -1;
            }
        }
    }
}
