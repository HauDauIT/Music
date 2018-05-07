package android.bkav.com.fragment_music;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaPlaybackService extends Service implements IAudioPlayer{

    private Intent intent;
    private Bundle bundle;
    private MediaPlayer mMediaPlayer;
    private ArrayList<String > arrList;
    private List<Song> songItems = new ArrayList<>();
    private final IBinder mBinder = new MyBinder();

    private SongOfflineManager songOfflineManager;
    private MediaManager mMediaManager;
    private int mCurrentPosition;
    private int mRepeatState;
    private int mShuffleState;


    @Override
    public void onCreate() {
        super.onCreate();
        songOfflineManager = new SongOfflineManager(this);
        mMediaManager = new MediaManager(this);
        songItems = songOfflineManager.getSongItems();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void play(Song song) {
        int position = 0;
        createNotification();
        try {
            mMediaManager.createMediaPlayer();
            mMediaManager.setDataSource(songItems.get(position).getmStt(), position);
            mMediaManager.PrepareAsyn();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNotification() {
        intent = new Intent();
        intent.setClass(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.iconmusic));
        notification.setContentTitle("Media");
        notification.setContentText("this media");
        notification.setContentIntent(pendingIntent);
        notification.setSmallIcon(R.drawable.iconmusic);
        startForeground(1, notification.build());
    }



    @Override
    public void start(int position) {
        mCurrentPosition = position;
    }

    @Override
    public void pause(int position) {
        mCurrentPosition = position;
    }

    @Override
    public void complete(int position) {

    }


    public class MyBinder extends Binder {
        public MediaPlaybackService getService() {
        return MediaPlaybackService.this;
        }
    }
}
