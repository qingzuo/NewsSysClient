package edu.jsu.newssysclient.ui.main;

import java.net.URL;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import edu.jsu.newssysclient.R;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 播放视频界面
 * 
 * @author zuo
 * 
 */
public class VideoPlayActivity extends MyBaseActivity implements
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
		OnVideoSizeChangedListener, SurfaceHolder.Callback {

	private static final String TAG = "MediaPlayerDemo";
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private String path = "http://flv2.bn.netease.com/videolib3/1408/16/UGmZj7894/SD/UGmZj7894-mobile.mp4";;
	private Bundle extras;
	private static final String MEDIA = "media";
	public static final String VIDEOURL = "edu.jsu.newssysclient.ui.main.VideoPlayActivity.MYVIDEOURL";
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	ImageView stopLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55333333")));
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.activity_videoplay);
		
		// 获取控件
		mPreview = (SurfaceView) findViewById(R.id.surface);
		stopLogo = (ImageView) findViewById(R.id.stoplogo);
		mPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPlaying()) {
					stopVideoPlayback();
				} else {
					startVideoPlayback();
				}
			}
		});
		// 抽取控件
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888);
		extras = getIntent().getExtras();
		showLoadingProgressDialog();
	}

	/**
	 * 播放视频方法
	 * @param Media
	 */
	private void playVideo(Integer Media) {
		doCleanUp();
		String t = null;
		if (extras != null) {
			t = extras.getString(VIDEOURL);
		}
		if (t != null && t.length() != 0) {
			path = t;
		}
		try {
			switch (Media) {
			case LOCAL_VIDEO:
				/*
				 * TODO: Set the path variable to a local media file path.
				 */
				if (path == "") {
					// Tell the user to provide a media file URL.
					Toast.makeText(
							VideoPlayActivity.this,
							"Please edit MediaPlayerDemo_Video Activity, "
									+ "and set the path variable to your media file path."
									+ " Your media file must be stored on sdcard.",
							Toast.LENGTH_LONG).show();
					return;
				}
				break;
			case STREAM_VIDEO:
				/*
				 * TODO: Set path variable to progressive streamable mp4 or 3gpp
				 * format URL. Http protocol should be used. Mediaplayer can
				 * only play "progressive streamable contents" which basically
				 * means: 1. the movie atom has to precede all the media data
				 * atoms. 2. The clip has to be reasonably interleaved.
				 */
				if (path == "") {
					// Tell the user to provide a media file URL.
					Toast.makeText(
							VideoPlayActivity.this,
							"Please edit MediaPlayerDemo_Video Activity,"
									+ " and set the path variable to your media file URL.",
							Toast.LENGTH_LONG).show();
					return;
				}

				break;

			}

			// Create a new media player and set the listeners
			mMediaPlayer = new MediaPlayer(this);
			mMediaPlayer.setDataSource(path);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepareAsync();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			setVolumeControlStream(AudioManager.STREAM_MUSIC);

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
		}
	}

	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
		// Log.d(TAG, "onBufferingUpdate percent:" + percent);

	}

	public void onCompletion(MediaPlayer arg0) {
		Log.d(TAG, "onCompletion called");
	}

	/**
	 * 视频宽高改变时调用
	 */
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height
					+ ")");
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			dismissProgressDialog();
			startVideoPlayback();
		}
	}

	/**
	 * 准备视频
	 */
	public void onPrepared(MediaPlayer mediaplayer) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			dismissProgressDialog();
			startVideoPlayback();
		}
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
		Log.d(TAG, "surfaceChanged called");

	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		Log.d(TAG, "surfaceDestroyed called");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated called");
		// playVideo(extras.getInt(MEDIA));
		playVideo(STREAM_VIDEO);

	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
	}

	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	/**
	 * 清除界面
	 */
	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	/**
	 * 开始播放
	 */
	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
//		holder.setFixedSize(mVideoWidth, mVideoHeight);
		holder.setFixedSize(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
		mMediaPlayer.start();
		stopLogo.setVisibility(View.INVISIBLE);
		getActionBar().hide();
	}

	/**
	 * 停止播放
	 */
	private void stopVideoPlayback() {
		Log.v(TAG, "stopVideoPlayback");
		mMediaPlayer.stop();
		getActionBar().show();
		stopLogo.setVisibility(View.VISIBLE);
	}

	/**
	 * 判断是否处于播放状态
	 * @return true, 在播放；false，暂停状态
	 */
	private boolean isPlaying() {
		return mMediaPlayer.isPlaying();
	}
}
