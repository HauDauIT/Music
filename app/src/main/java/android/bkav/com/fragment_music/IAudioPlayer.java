package android.bkav.com.fragment_music;


public interface IAudioPlayer {
    void start(int position);

    void pause(int position);

    void complete(int position);
}
