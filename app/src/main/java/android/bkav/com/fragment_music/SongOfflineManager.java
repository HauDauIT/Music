package android.bkav.com.fragment_music;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hau Dau on 4/24/2018.
 */

public class SongOfflineManager {
    private Context context;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    public SongOfflineManager(Context context) {
        this.context = context;
    }


    private boolean checkArtist(List<Song> artists, String nameArtist) {
        for (int i = 0; i < artists.size(); i++) {
            if (artists.get(i).getmArtist().equals(nameArtist)) {
                return true;
            }
        }
        return false;
    }

    public List<Song> getListArtist() {
        List<Song> songOfflines = getSongItems();
        List<Song> artists = new ArrayList<>();
        for (int i = 0; i < songOfflines.size(); i++) {
            if (!checkArtist(artists, songOfflines.get(i).getmArtist())) {
                artists.add(songOfflines.get(i));
            }
        }
        return artists;
    }

    private boolean checkAlbum(List<Song> albums, String nameAlbum) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getmAnblum().equals(nameAlbum)) {
                return true;
            }
        }
        return false;
    }

    public List<Song> getListAlbum() {
        List<Song> songOfflines = getSongItems();
        List<Song> albums = new ArrayList<>();
        for (int i = 0; i < songOfflines.size(); i++) {
            if (!checkAlbum(albums, songOfflines.get(i).getmAnblum())) {
                albums.add(songOfflines.get(i));
            }
        }
        return albums;
    }

    public List<Song> getSongItems() {
        Cursor cursor = null;
        List<Song> songItems = new ArrayList<>();

        String[] information = new String[]{
                MediaStore.Audio.Media.ALBUM_KEY,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,

        };


        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = context.getContentResolver().query(uri, information, null, null, null);
        }

        if (cursor == null) {
            return songItems;
        }


        int songID = cursor.getColumnIndex(information[0]);
        int songTittle = cursor.getColumnIndex(information[1]);
        int songTime = cursor.getColumnIndex(information[2]);
        int songSinger = cursor.getColumnIndex(information[3]);
        int songArtist = cursor.getColumnIndex(information[4]);
        int indexAlbum = cursor.getColumnIndex(information[5]);
        int indexAlbumId = cursor.getColumnIndex(information[6]);


        cursor.moveToFirst();

        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        int i=0;
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(songTittle);
            String duaration = cursor.getString(songTime);
            String singer = cursor.getString(songSinger);
            String artist = cursor.getString(songArtist);
            String album = cursor.getString(indexAlbum);
            long idAlbum = cursor.getLong(indexAlbumId);
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, idAlbum);


            songItems.add(new Song(i,name, duaration, singer, artist, album, albumArtUri.toString()));
            cursor.moveToNext();
            i++;
        }

        cursor.close();

        return songItems;
    }
}
