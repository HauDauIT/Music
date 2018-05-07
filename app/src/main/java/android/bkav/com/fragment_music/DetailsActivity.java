package android.bkav.com.fragment_music;


import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by Hau Dau on 4/17/2018.
 */

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mMediaPlayer;
    private ImageButton mNext;
    private ImageButton mPrevious;
    private ImageButton mPlay;
    private ImageButton mPause;
    private int mViTriBaiHat;
    private ArrayList<String > arrList;
    private Uri uri;
    private TextView mThoiGianKetThuc;
    private TextView mThoiGianBatDau;
    private SeekBar mSeekBar;
    private TextView mTenBaiHat;
    private TextView mTenCaSi;
    private Intent intent;
    private Bundle bundle;
    private int thoiluong;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_music);

        intent = getIntent();


        initView();
        getData();

        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPrevious.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());
                if (mSeekBar.equals(simpleDateFormat.format(mMediaPlayer.getCurrentPosition())+"")){
                    mViTriBaiHat += 1; // Bkav QuangLH review 20180422: cho vào trong song item
                    playSongPosition();
                    mPlay.setImageResource(R.drawable.pause);
                    startTime();
                    finishTime();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK    ) {
            intent = new Intent(DetailsActivity.this,MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void updateSong() {
        MediaMetadataRetriever retriever = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(arrList.get(mViTriBaiHat));

            String tenAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String ten = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            mTenBaiHat.setText(ten);
            mTenCaSi.setText(tenAlbum);

        }
    }

    private void getData() {

        bundle = intent.getBundleExtra("dulieu");
        mViTriBaiHat = bundle.getInt("vitri");
        arrList = bundle.getStringArrayList("tenbai");
        thoiluong = bundle.getInt("CurrentPosition");
        Log.d("aa","timer"+thoiluong);

        playSongPosition();
        updateSong();
        startTime();
        finishTime();

    }

    private void playSongPosition(){
        uri = Uri.parse(arrList.get(mViTriBaiHat).toString());
        mMediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mMediaPlayer.seekTo(thoiluong);
        startTime();
        updateSong();
    }


    private void startTime(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mThoiGianBatDau.setText(simpleDateFormat.format(mMediaPlayer.getCurrentPosition()));
                mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                handler.postDelayed(this,1000);
            }
        }, 1000);
    }
    private void finishTime(){
        mThoiGianKetThuc.setText(simpleDateFormat.format(mMediaPlayer.getDuration()) +"");
        mSeekBar.setMax(mMediaPlayer.getDuration());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play: {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mPlay.setImageResource(R.drawable.play);
                } else {
                    mMediaPlayer.start();
                    mPlay.setImageResource(R.drawable.pause);
                }
                startTime();
                finishTime();
                break;
            }
            case R.id.btn_previous: {

                mViTriBaiHat -= 1;
                if (mViTriBaiHat < 0) {
                    mViTriBaiHat = arrList.size() - 1;
                }
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                playSongPosition();
                mPlay.setImageResource(R.drawable.pause);
                startTime();
                finishTime();
                break;
            }
            case R.id.btn_next: {

                mViTriBaiHat += 1;
                if (mViTriBaiHat > arrList.size() - 1) {
                    mViTriBaiHat = 0;
                }
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                playSongPosition();
                mPlay.setImageResource(R.drawable.pause);
                startTime();
                finishTime();
                break;
            }
        }
    }
    private void initView() {
        mPause = findViewById(R.id.btn_pause);
        mPlay = findViewById(R.id.btn_play);
        mThoiGianBatDau = findViewById(R.id.tv_currentPosition);
        mThoiGianKetThuc = findViewById(R.id.tv_Duration);
        mSeekBar = findViewById(R.id.seekbar_music);
        mTenBaiHat = findViewById(R.id.van_tat_bai_hat);
        mTenCaSi = findViewById(R.id.ten_ca_si);
        mNext = findViewById(R.id.btn_next);
        mPrevious = findViewById(R.id.btn_previous);
    }
}
