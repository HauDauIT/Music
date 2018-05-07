package android.bkav.com.fragment_music;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by Hau Dau on 4/17/2018.
 */


public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int FIRST_LETTER = 0;
    public static final int NAME_SONG = 1;

    private Context context;
    private List<Object> mSongItems;
    private MediaPlayer mMediaPlayer;

    private int mCount = 0;

    public SongAdapter(Context context, List<Object> itemList) {
        this.context = context;
        this.mSongItems = itemList;
    }

    public interface OnItemClickedListener {
        void onItemClick(String tenBaiHat, Integer i);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }


    @Override
    public int getItemViewType(int position) {
        if (mSongItems.get(position) instanceof CharSequence) {
            return FIRST_LETTER;
        } else if (mSongItems.get(position) instanceof Song) {
            return NAME_SONG;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case FIRST_LETTER: {
                mCount++;
                View itemAlphabel = inflater.inflate(R.layout.row_title, parent, false);
                return new FirstNameViewHolder(itemAlphabel);
            }
            case NAME_SONG: {
                View itemSong = inflater.inflate(R.layout.row_song, parent, false);
                return new NameSongViewHolder(itemSong);
            }
            default:
                break;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)) {
            case FIRST_LETTER: {

                final FirstNameViewHolder alphabetAdapter = (FirstNameViewHolder) holder;
                alphabetAdapter.mFirstName.setText(mSongItems.get(position).toString());
                break;
            }
            case NAME_SONG: {
                final NameSongViewHolder nameSongViewHolder = (NameSongViewHolder) holder;
                songItem(nameSongViewHolder, position);
                break;
            }
        }

    }

    private void songItem(final NameSongViewHolder nameSongViewHolder, final int position) {
        final Song songItem = (Song) mSongItems.get(position);

        nameSongViewHolder.mStt.setText(Integer.toString(position + 1 - mCount));
        
        nameSongViewHolder.mTenBaiHat.setText(songItem.getmName());
        String duration = songItem.getmDuration();
        nameSongViewHolder.mThoiGian.setText(duration == null ? "0" : songItem.setCorrectDuration(songItem.getmDuration()
        ));

        nameSongViewHolder.mPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, nameSongViewHolder.mPopupMenu);
                popup.getMenuInflater().inflate(R.menu.poupop_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(context, "Bạn chọn : " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();
            }
        });

        nameSongViewHolder.mLinearLayoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onItemClick(nameSongViewHolder.mTenBaiHat.getText().toString(),
                            Integer.valueOf(nameSongViewHolder.mStt.getText().toString()));
                }

                nameSongViewHolder.mBaiDangChon.setVisibility(View.VISIBLE);
                nameSongViewHolder.mStt.setVisibility(View.INVISIBLE);

            }
        });
    }


    @Override
    public int getItemCount() {
        return mSongItems.size();
    }

    public class NameSongViewHolder extends RecyclerView.ViewHolder {

        private TextView mStt;
        private TextView mTenBaiHat;
        private TextView mThoiGian;
        private ImageView mBaiDangChon;
        private ImageView mPopupMenu;
        private LinearLayout mLinearLayoutRow;

        public NameSongViewHolder(View itemView) {
            super(itemView);
            mStt = itemView.findViewById(R.id.stt);
            mTenBaiHat = itemView.findViewById(R.id.ten_bai_bat);
            mThoiGian = itemView.findViewById(R.id.time_song);
            mPopupMenu = itemView.findViewById(R.id.img_popup_menu);
            mBaiDangChon = itemView.findViewById(R.id.bai_dang_chon);
            mLinearLayoutRow = itemView.findViewById(R.id.learRow);
        }
    }

    public class FirstNameViewHolder extends RecyclerView.ViewHolder {

        private TextView mFirstName;

        public FirstNameViewHolder(View itemView) {
            super(itemView);
            mFirstName = itemView.findViewById(R.id.firstname);
        }
    }

}