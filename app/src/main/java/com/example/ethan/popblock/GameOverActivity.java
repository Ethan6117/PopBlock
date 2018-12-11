package com.example.ethan.popstar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends Activity implements OnClickListener {
	private long lastClickTime=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		findViewById(R.id.button_replay).setOnClickListener(this);
		TextView textView_level = (TextView) findViewById(R.id.textView_level);
		TextView textView_score = (TextView) findViewById(R.id.textView_score);
		Intent intent = this.getIntent();
		textView_level.setText("通过第" + intent.getLongExtra("level", 0) + "关" );
		textView_score.setText("得了" + intent.getLongExtra("score", 0)+"分");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_replay:
				Intent intent = new Intent(this, GameActivity.class);
				intent.putExtra("newGame", true);
				this.startActivity(intent);
				this.finish();
				break;
		}
	}

	public void onBackPressed(){
		if(lastClickTime<=0){
			Toast.makeText(this, "再按一次后退键退出程序！", Toast.LENGTH_SHORT).show();
			lastClickTime=System.currentTimeMillis();
		}
		else {
			long currentClickTime=System.currentTimeMillis();
			if(currentClickTime-lastClickTime<1000){
				finish();
				GameActivity.mediaPlayer.stop();
			}
			else {
				Toast.makeText(this,"再按一次后退键退出程序！",Toast.LENGTH_SHORT).show();
				lastClickTime=currentClickTime;
			}
		}

	}
}
