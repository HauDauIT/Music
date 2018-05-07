package android.bkav.com.fragment_music;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Bkav QuangLH review 20180415:
// 1. Ra soat lai convention o tat ca cac file.
// 2. Gop y ve drawable giong Tuan, Phong.
// 3. Khong tach ham khong can thiet.

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SongAdapter.OnItemClickedListener {

    private MediaPlayer mMediaPlayer;
    private RecyclerView mRecyclerView;
    private ImageButton mPlay;
    private ImageButton mPause;
    private ImageView mAnhCaSi;
    private TextView mVanTatBaiHat;
    private TextView mTenCaSi;
    private LinearLayout mLinearLayout;
    private SearchView mSearchView;
    private Toolbar toolbar;
    private SongAdapter mSongAdapter;
    private SongOfflineManager mSongOfflineManager;
    private List<Song> mListSong = new ArrayList<>();
    private ArrayList<String> mPath;
    private Intent intent;
    private Bundle bundle = new Bundle();

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(toolbar);

        initView();

        mSongOfflineManager = new SongOfflineManager(this);
        mListSong = mSongOfflineManager.getSongItems();
        adapterRecyclerview(mListSong);
        getAllListMusic();
        xuLyChoiNhac();
        xuLyChuyenDoi();

        Intent intent = new Intent(this, MediaPlaybackService.class);

        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MediaPlaybackService.MyBinder binder = (MediaPlaybackService.MyBinder) service;
                MediaPlaybackService mpService = binder.getService();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    mpService.play(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }


    private void xuLyChuyenDoi() {
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, DetailsActivity.class);
                bundle.putInt("CurrentPosition", mMediaPlayer.getCurrentPosition());
                intent.putExtra("dulieu", bundle);
                startActivity(intent);
            }
        });
    }

    private void xuLyChoiNhac() {

        mPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPause.setVisibility(View.INVISIBLE);
                mPlay.setVisibility(View.VISIBLE);
                mMediaPlayer.pause();
            }
        });

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPause.setVisibility(View.VISIBLE);
                mPlay.setVisibility(View.INVISIBLE);
                if (mMediaPlayer == null) {
                    mMediaPlayer.start();
                } else {
                    mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition());
                    mMediaPlayer.start();
                }
            }
        });
    }


    private void adapterRecyclerview(List<Song> mListSong) {

        Collections.sort(mListSong, new Comparator<Song>() {
            @Override
            public int compare(Song songItem, Song t1) {

                return songItem.getmName().compareTo(t1.getmName());
            }
        });
        List<Object> objectList = new ArrayList<>();

        for (int i = 0; i < mListSong.size(); i++) {
            if (i == 0
                    || (mListSong.get(i).getmName().charAt(0) != mListSong.get(i - 1).getmName().charAt(0))) {

                CharSequence alphabetDisplay = mListSong.get(i).getmName()
                        .subSequence(0,
                                1);
                objectList.add(alphabetDisplay);
            }
            objectList.add(mListSong.get(i));
        }
        mSongAdapter = new SongAdapter(this, objectList);
        mSongAdapter.setOnItemClickedListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSongAdapter);
        mSongAdapter.notifyDataSetChanged();
    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recyclerview_song);
        mLinearLayout = findViewById(R.id.ln_van_tat_bai_hat);
        mTenCaSi = findViewById(R.id.ten_ca_si);
        mVanTatBaiHat = findViewById(R.id.van_tat_bai_hat);
        mAnhCaSi = findViewById(R.id.anh_ca_si);
        mPlay = findViewById(R.id.btn_play);
        mPause = findViewById(R.id.btn_pause);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem itemSearch = menu.findItem(R.id.search_view);
        mSearchView = (SearchView) itemSearch.getActionView();
        mSearchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Do you want to exit this application?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog dialog = alertDialog.create();
            dialog.setTitle("Thông báo!!!");
            dialog.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    private void getAllListMusic() {
        mPath = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            String s = files[i].getName();
            if (s.endsWith(".mp3")) {
                mPath.add(files[i].getAbsolutePath());
            }
        }
    }

    @Override
    public void onItemClick(String tenBaiHat, Integer i) {

        String local = mPath.get(i);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(local);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setVolume(100, 100);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MediaMetadataRetriever retriever = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mPath.get(i));

            String tenAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String ten = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            mVanTatBaiHat.setText(ten);
            mTenCaSi.setText(tenAlbum);
            mLinearLayout.setVisibility(View.VISIBLE);
        }
        bundle.putInt("vitri", i);
        bundle.putStringArrayList("tenbai", mPath);
    }
}


