package com.example.ethan.popstar;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;

import java.io.File;
import java.io.IOException;

public class GameActivity extends Activity {
	private GameSurfaceView surfaceView;
	boolean isMusic;
	public static MediaPlayer mediaPlayer;

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
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				//	mMediaPlayer.start();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		playLocalFile();
		surfaceView = new GameSurfaceView(this);
		setContentView(surfaceView);
		boolean newGame = this.getIntent().getBooleanExtra("newGame", true);
		if (!newGame) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			GameActivity.mediaPlayer.stop();
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onUserLeaveHint() {
		GameActivity.mediaPlayer.stop();
		System.exit(0);
		super.onUserLeaveHint();
	}
}
