package org.xidea.app.dao;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Config;
import android.widget.MediaController;
import android.widget.SeekBar;

public class MediaHelper {
	private MediaPlayer mediaMusic;
	private SeekBar seekBar;
	private int startTime;
	private Updater updater;
	private int chapterIndex = -1;
	final static Handler handler = new Handler();

	public void setChapterIndex(int chapterIndex) {
		if (chapterIndex != this.chapterIndex) {
			this.chapterIndex = chapterIndex;

			this.resetMedia(Main.instance, chapterIndex);
		} else {
			if (ConfigController.isReading()) {
				if (mediaMusic != null && !mediaMusic.isPlaying()) {
					mediaMusic.start();
				}
			} else {
				if (mediaMusic != null && mediaMusic.isPlaying()) {
					mediaMusic.stop();
				}
			}
		}
	};

	private void resetMedia(final Main activity, int chapterIndex) {
		try {
			if (ConfigController.isReading()) {
				this.startTime = getStartTime(chapterIndex);
				if (mediaMusic != null) {
					try {
						mediaMusic.stop();
						mediaMusic.release();
					} catch (Exception e) {
					}
				}
				this.mediaMusic = MediaPlayer.create(
						activity,
						(Integer) R.raw.class.getField(
								"cp" + (chapterIndex + 1)).get(null));
				// mediaMusic.prepare();
				mediaMusic.start();
				mediaMusic.pause();
				new SeekAndStart().startAt(startTime);
				seekBar = (SeekBar) activity.findViewById(R.id.speekSeekBar);
				seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

						if (ConfigController.isReading()) {
							int dest = seekBar.getProgress();
							int mMax = mediaMusic.getDuration();
							int sMax = seekBar.getMax();
							mediaMusic.seekTo(startTime + (mMax - startTime)
									* dest / sMax);
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						if (fromUser) {
							new SeekAndStart().startAt(startTime);
						}
					}
				});
				mediaMusic
						.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {

								if (ConfigController.isAutoFlip()) {
									activity.onDrawerItemSelected(ConfigController
											.getSection() + 1);
								}
							}
						});

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getStartTime(int chapterIndex) {
		if (chapterIndex == 0) {
			return 6000;
		} else if (chapterIndex < 10 || (chapterIndex + 1) % 10 == 0) {
			return 2200;
		} else {
			return 3000;
		}
	}

	public void pause() {
		try {
			this.mediaMusic.pause();
			if (updater != null) {
				updater.stop();
				updater = null;
			}
		} catch (Exception e) {
		}
	}

	public void resume() {
		try {
			if (ConfigController.isReading()) {
				this.mediaMusic.start();
				if (updater != null) {
					updater.stop();
				}
				updater = new Updater();
				updater.start();
			}
		} catch (Exception e) {
		}
	}

	public void destroy() {
		try {
			this.mediaMusic.stop();
			this.mediaMusic.release();
		} catch (Exception e) {
		}

	}

	class Updater implements Runnable {
		private boolean stoped;

		@Override
		public void run() {
			if (stoped) {
				return;
			}
			// TODO Auto-generated method stub
			int position = mediaMusic.getCurrentPosition();
			int mMax = mediaMusic.getDuration();
			int sMax = seekBar.getMax();
			seekBar.setProgress(position * sMax / mMax);
			// curtimeAndTotaltime.setText("当前播放时间： "+position/1000+"秒"+"\n歌曲总时间: "+mMax/1000+"秒");
			handler.postDelayed(this, 400);
		}

		public void stop() {
			this.stoped = true;
		}

		public void start() {
			handler.postDelayed(this, 400);
		}
	};

	class SeekAndStart implements MediaPlayer.OnSeekCompleteListener {
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			// System.out.println("seek:" + mp.getCurrentPosition());
			mp.start();
		}

		void startAt(int seek) {
			mediaMusic.setOnSeekCompleteListener(this);
			mediaMusic.seekTo(seek);
		}
	}

}
