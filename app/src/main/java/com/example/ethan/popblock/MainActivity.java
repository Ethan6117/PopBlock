package com.example.ethan.popstar;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends Activity implements OnClickListener{
    public static int diff=0;
    public Button music;
	boolean isMusic;
	private Button button_rule;
	private Button button_diff;
	private Intent intent;
	private Intent i2 ;
	private MediaPlayer mediaPlayer;

	private void playLocalFile() {
		mediaPlayer = MediaPlayer.create(this,R.raw.bgmmusic);
		try {
			mediaPlayer.prepare();
		}   catch (IllegalStateException e) {
		}    catch (IOException e) {
		}
		mediaPlayer.start();
		isMusic=true;
		mediaPlayer.setLooping(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			moveTaskToBack(false);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_HOME ) {
			finish();
			GameActivity.mediaPlayer.stop();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		playLocalFile();
		findViewById(R.id.button_start).setOnClickListener(this);
		i2 = new Intent(this, ChooseDifficultActivity.class);
		button_diff= (Button) findViewById(R.id.button_diff);
		button_diff.setOnClickListener(new OnClickListener() {

			//跳转到难度界面
			@Override
			public void onClick(View v) {
				mediaPlayer.stop();
				startActivity(i2);
			}
		});
		intent = new Intent(this, RuleActivity.class);
		button_rule= (Button) findViewById(R.id.button_rule);
		button_rule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mediaPlayer.stop();
				startActivity(intent);
			}
		});
		music= (Button) findViewById(R.id.btn_music);
		music.setOnClickListener(new OnClickListener() {
			//实现背景音乐的播放与暂停
			@Override
			public void onClick(View v) {
				if(mediaPlayer.isPlaying()||isMusic==true) {
                    mediaPlayer.pause();
                    isMusic = false;
                }
				else if(!mediaPlayer.isPlaying()||isMusic==false) {
					mediaPlayer.start();
				}
			}
		});
	}


	@Override
	public void onClick(View v) {
		Intent i = new Intent(this, GameActivity.class);
		switch (v.getId()) {
			case R.id.button_start:
				i.putExtra("newGame", true);
				break;
		}
		mediaPlayer.stop();
		startActivity(i);
		finish();
	}
}
