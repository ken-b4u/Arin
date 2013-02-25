package net.sytes.otto.arin;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	private static final int MENU_ID_MENU1 = (Menu.FIRST + 1);
	private static final int MENU_ID_MENU2 = (Menu.FIRST + 2);
	private MainView mainView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// タイトルバーを消す
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ステータスバーを消す
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mainView = new MainView(this);
		setContentView(mainView);
		Intent i = getIntent();
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		setVolumeControlStream(AudioManager.STREAM_MUSIC); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// メニューアイテムを追加します
		menu.add(Menu.NONE, MENU_ID_MENU1, Menu.NONE, "リセット");
		menu.add(Menu.NONE, MENU_ID_MENU2, Menu.NONE, "つぶやく");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret = true;
		switch (item.getItemId()) {
		default:
			ret = super.onOptionsItemSelected(item);
			break;
		case MENU_ID_MENU1:
			// カウンターのリセット
			mainView.counter.clear();
			mainView.selector.reset();
			ret = true;
			break;
		case MENU_ID_MENU2:
			// つぶやく
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
				intent.setType("text/plain");
				String content = mainView.counter.n + getString(R.string.share_strings_1)
						+ getString(R.string.my_store_url)
						+ getString(R.string.from_app_name);
				intent.putExtra(Intent.EXTRA_TEXT, content);
				startActivity(Intent.createChooser(intent, getString(R.string.share_dialog_title)));
			}
		return ret;
	}

	@Override
	protected void onPause() {
		super.onPause();
		mainView.isOnPause = true;
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		mainView.isOnPause = false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
		System.exit(0);
	}

}
