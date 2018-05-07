package android.bkav.com.fragment_music;


import java.io.Serializable;

/**
 * Created by Hau Dau on 4/11/2018.
 */

public class Song implements Serializable {
    private int mStt;
    private String mName;
    private String mDuration;
    private String mSingle;
    private String mArtist;
    private String mAlbum;
    private String mUriThumabnail;

    public Song(int mStt, String mName, String mDuration, String mSingle, String mArtist, String mAlbum, String mUriThumabnail) {
        this.mStt = mStt;
        this.mName = mName;
        this.mDuration = mDuration;
        this.mSingle = mSingle;
        this.mArtist = mArtist;
        this.mAlbum = mAlbum;
        this.mUriThumabnail = mUriThumabnail;
    }

    public String getmAnblum() {
        return mAlbum;
    }

    public void setmAnblum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public int getmStt() {
        return mStt;
    }

    public void setmStt(int mStt) {
        this.mStt = mStt;
    }

    public String getmUriThumabnail() {
        return mUriThumabnail;
    }

    public void setmUriThumabnail(String mUriThumabnail) {
        this.mUriThumabnail = mUriThumabnail;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmSingle() {
        return mSingle;
    }

    public void setmSingle(String mSingle) {
        this.mSingle = mSingle;
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String setCorrectDuration(String songs_duration) {

        if (Integer.valueOf(songs_duration) != null) {
            int time = Integer.valueOf(songs_duration);

            int seconds = time / 1000;
            int minutes = seconds / 60;
            seconds = seconds % 60;

            if (seconds < 10) {
                songs_duration = String.valueOf(minutes) + ":0" + String.valueOf(seconds);
            } else {

                songs_duration = String.valueOf(minutes) + ":" + String.valueOf(seconds);
            }
            return songs_duration;
        }
        return null;

    }

}
