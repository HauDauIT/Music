package android.bkav.com.fragment_music;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Hau Dau on 4/25/2018.
 */

public class MediaManager implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private IAudioPlayer mIAudioPlayer;
    private MediaPlayer mMediaPlayer;
    private MediaManager mMediaManager;
    private int mPosition;

    public MediaManager(IAudioPlayer mIAudioPlayer) {
        this.mIAudioPlayer = mIAudioPlayer;
    }


    public void createMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
    }

    public void setDataSource(int dataSource, int position) throws IOException {
        mMediaPlayer.setDataSource(String.valueOf(dataSource));// mở nhạc từ bộ nhớ
        mPosition = position;
    }

    //prepare xong tu dong phat nhac
    public void PrepareAsyn() {
        mMediaPlayer.setOnCompletionListener(this);//hat xong thi vao phuong thuc nay
        mMediaPlayer.setOnPreparedListener(this);//khi prepare xong thì rơi vào phương thức này
        mMediaPlayer.prepareAsync();
    }


    public void start(){
        mMediaPlayer.start();
        mIAudioPlayer.start(mPosition);
    }

    public void pause(){
        mMediaPlayer.pause();
        mIAudioPlayer.pause(mPosition);
    }

    public void stop(){
        mMediaPlayer.stop();
        mIAudioPlayer.start(mPosition);
    }

    public void seek(int position) {
        if (position < mMediaPlayer.getDuration()) {
            mMediaPlayer.seekTo(position);
        }
    }

    public void loop(boolean repeat){
        mMediaPlayer.setLooping(repeat);
    }


    public int getCurrentPlayback() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getPlayback() {
        return mMediaPlayer.getDuration();
    }

    public int currentPosion(){
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mIAudioPlayer.complete(mPosition);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        
    }
}
