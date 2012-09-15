package net.sytes.otto.arin;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends Activity {
	private static final int MENU_ID_MENU1 = (Menu.FIRST + 1);
	private MainView mainView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mainView = new MainView(this);
        setContentView(mainView);
        Intent i = getIntent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// メニューアイテムを追加します
        menu.add(Menu.NONE, MENU_ID_MENU1, Menu.NONE, "count reset");
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
    		mainView.selector.select(0);
    		ret = true;
    		break;
        }
        return ret;
    }
}
