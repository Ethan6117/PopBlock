package com.example.ethan.popstar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private int sideLengthOfBlock;
	private int topSidePosition;
	private Paint paint;
	private Level level;
	private GamePad gamePad;
	public static final int SOUND_EXPLOSION = 1;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	boolean bang=false;
	private Bitmap bitmap;
	private static int mScreenWidth;
	private static int mScreenHeight;

	private void initSounds() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 10);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(SOUND_EXPLOSION, soundPool.load(getContext(),R.raw.mus, 1));
	}

	public void playSound(int sound) {
		AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
		int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume,1,0, 1f);
	}

	public GameSurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
		paint = new Paint();
		level = new Level();
		gamePad = new GamePad();
	}

	public void newLevel() {
		level.next();
		gamePad = new GamePad();
		redraw();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Rect surfaceFrame = holder.getSurfaceFrame();
		sideLengthOfBlock = surfaceFrame.width() / GamePad.BLOCKS_PER_ROW;
		topSidePosition = surfaceFrame.height() - sideLengthOfBlock * GamePad.BLOCKS_PER_COLUMN;
		redraw();
	}

	private void redraw() {
		Canvas canvas = getHolder().lockCanvas();
		redraw(canvas);
		getHolder().unlockCanvasAndPost(canvas);
	}

	//画布显示分数、绘制格子
	public void redraw(Canvas canvas) {
		//画布大小
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		Bitmap bmp = ((BitmapDrawable)getResources().getDrawable(R.drawable.star)).getBitmap();
		bitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
		//设置画布的背景颜色
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(bitmap, 0, 0,null);
		//绘制格子
		for (int column = 0; column < GamePad.BLOCKS_PER_ROW; column++) {
			for (int row = 0; row < GamePad.BLOCKS_PER_COLUMN; row++) {
				if (gamePad.blockMatrix[row][column] == null) {
					continue;
				}
				//给画布中矩阵的每个位置绘制颜色
				drawBlock(canvas, gamePad.blockMatrix[row][column]);
			}
		}
		paint.setColor(Color.WHITE);
		paint.setTextSize(60);
		canvas.drawText("     " + level.getLevel() + " ", 50, 170, paint);
		canvas.drawText("                       " + level.getRequiredScore(), 500, 170, paint);
		canvas.drawText("                       " + level.getScore(), 500, 330, paint);
		paint.reset();
	}

	//绘制每个方块的颜色
	private void drawBlock(Canvas canvas, Block block) {
		paint.setColor(block.getColor());
		// 取方块左边缘和上边缘坐标，确定矩形对象。
		int leftCoordinate = block.getColumn() * sideLengthOfBlock;
		int topCoordinate = topSidePosition + block.getRow() * sideLengthOfBlock;
		Rect r = new Rect(leftCoordinate, topCoordinate,leftCoordinate + sideLengthOfBlock,topCoordinate + sideLengthOfBlock);
		canvas.drawRect(r, paint);
		paint.setColor(Color.BLACK); //设置框框边缘的颜色
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);
		canvas.drawRect(r, paint);
		paint.reset();
	}

	private void drawSelectedMarks(ArrayList<Block> selectedBlocks) {
		Canvas canvas = getHolder().lockCanvas();
		redraw(canvas);
		for (Block block: selectedBlocks) {
			drawSelectedMark(canvas, block);
		}
		getHolder().unlockCanvasAndPost(canvas);
	}

	public void drawSelectedMark(Canvas canvas, Block block) {
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);
		// 取方块左边缘和上边缘坐标，确定矩形对象。
		int leftCoordinate = block.getColumn() * sideLengthOfBlock;
		int topCoordinate = topSidePosition + block.getRow() * sideLengthOfBlock;
		Rect r = new Rect(leftCoordinate,topCoordinate,leftCoordinate + sideLengthOfBlock,topCoordinate + sideLengthOfBlock);
		canvas.drawRect(r, paint);
		paint.reset();
	}

	//方块点击事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(bang){
			initSounds();
			bang=true;
		}
		onBlockTouched(getTouchedBlock(event.getX(), event.getY()));
		playSound(1);
		bang=true;
		return super.onTouchEvent(event);
	}

	//点击事件执行的操作，获得同色方块，消除
	private void onBlockTouched(Block touchedBlock) {
        initSounds();
        if (touchedBlock == null) {
            playSound(1);
            return;
        }
        ArrayList<Block> blocksSelected = gamePad.selectBlockInSameColor(touchedBlock);
        drawSelectedMarks(blocksSelected);
        int blocksDestroyed = gamePad.destroySelectedBlocks();
        level.gainScore(Algorithm.calcDestroyScore(blocksDestroyed));
        gamePad.sortBlocks();
        redraw();
        if (gamePad.isDead()) {
            int remainedBlocks = gamePad.countRemainedBlocks();
            level.gainScore(Algorithm.calcRemainedScore(remainedBlocks));
            if (!level.hasEnoughScore()) {
                gameOver();
            } else {
                newLevel();
                Toast.makeText(this.getContext(), "剩余方块" + remainedBlocks + "个，得分" + level.getScore() + "，进入第" + level.getLevel() + "关", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //根据点击位置获取方块
	private Block getTouchedBlock(float posX, float posY) {
		if (posY < topSidePosition) {
            return null;
        }
		int i = ((int) posY - topSidePosition) / sideLengthOfBlock;
		int j = (int) posX / sideLengthOfBlock;
		return gamePad.blockMatrix[i][j];
	}

	private void gameOver() {
		Activity parent = (Activity) this.getContext();
		Intent intent  = new Intent(parent, GameOverActivity.class);
		intent.putExtra("score", level.getScore());
		intent.putExtra("level", level.getLevel());
		parent.startActivity(intent);
		parent.finish();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}
