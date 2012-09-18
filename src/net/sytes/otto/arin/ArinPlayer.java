package net.sytes.otto.arin;

import java.util.Random;

import android.content.Context;
import android.media.MediaPlayer;

public class ArinPlayer {
	private MediaPlayer mp[];	// 音声再生

	public ArinPlayer(Context context){
		// メディアプレイヤーを作成
        mp = new MediaPlayer[6];
        mp[0]=MediaPlayer.create(context,R.raw.sample0);
        mp[1]=MediaPlayer.create(context,R.raw.sample1);
        mp[2]=MediaPlayer.create(context,R.raw.sample2);
        mp[3]=MediaPlayer.create(context,R.raw.sample3);
        mp[4]=MediaPlayer.create(context,R.raw.sample4);
        mp[5]=MediaPlayer.create(context,R.raw.sample5);

	}

	// 音声を再生中かどうか
	public boolean isPlaying(){
		for(int i=0;i<mp.length;i++){
			//if(mp[i].isPlaying()){
			if(mp[i] != null && mp[i].isPlaying()){
				return true;
			}
		}
		return false;
	}

    // ランダムに音声を再生する
    public void startRandom(){
    	int x = new Random().nextInt(mp.length);
    	startIndex(x);
    }

    // 添字にある音声を再生する
    public void startIndex(int i){
    	if(mp[i]!=null){
    		mp[i].seekTo(0);
    		mp[i].start();
    	}
    }
}
