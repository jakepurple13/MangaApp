package manga.mangaapp.MangaSide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karumi.marvelapiclient.model.ComicDto;
import com.karumi.marvelapiclient.model.MarvelImage;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.IOException;
import java.util.List;

import manga.mangaapp.AppUtil;
import manga.mangaapp.AsyncTasks;
import manga.mangaapp.MainActivity;
import manga.mangaapp.R;
import manga.mangaapp.RetrieveInfo;
import manga.mangaapp.mangaedenclient.Chapter;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.MangaEdenClient;
import manga.mangaapp.manymanga.data.Image;
import programmer.box.utilityhelper.UtilNotification;

/**
 * Created by Jacob on 8/23/17.
 */

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder>{
    //The list of contacts
    //private List<Chapter> mDataset;
    private Chapter[] mDataset;
    //Contact activity
    Activity in;
    String mangaID;
    String currentChapter;
    String mangaTitle;
    MangaEdenClient client;

    ChapterPage[] images;

    int textColor;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        public ImageButton downloadChapter;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            imageView = v.findViewById(R.id.imageButton);
            relativeLayout = v.findViewById(R.id.manga_layout);
            downloadChapter = v.findViewById(R.id.download_chapter);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    /*public ChapterListAdapter(List<Chapter> myDataset, Activity in) {
        mDataset = myDataset;
        this.in = in;
    }*/
    public ChapterListAdapter(Chapter[] myDataset, Activity in, String mangaID, String currentChapter, String title, MangaEdenClient client) {
        mDataset = myDataset;
        this.in = in;
        this.mangaID = mangaID;
        this.currentChapter = currentChapter;
        this.mangaTitle = title;
        this.client = client;
        this.textColor = in.getResources().getColor(R.color.md_black_1000);
    }

    public ChapterListAdapter(Chapter[] myDataset, Activity in, String mangaID, String currentChapter, String title, MangaEdenClient client, int textColor) {
        mDataset = myDataset;
        this.in = in;
        this.mangaID = mangaID;
        this.currentChapter = currentChapter;
        this.mangaTitle = title;
        this.client = client;
        this.textColor = textColor;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChapterListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_list_detail_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ChapterListAdapter.ViewHolder vh = new ChapterListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ChapterListAdapter.ViewHolder holder, final int position) {

        TextView tv = holder.mTextView;
        tv.setTextColor(textColor);
        ImageView ib = holder.imageView;
        ib.setVisibility(View.GONE);
        String title = mDataset[position].getTitle()!=null ? mDataset[position].getTitle() : "";
        String text = mDataset[position].getNumber() + ". " + title;
        if(currentChapter.equals(mDataset[position].getId())) {
            text+="\t<--";
        }
        tv.setText(text);
        tv.setTextSize(15f);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferencesManager.getInstance().putValue(mangaID, mDataset[position].getNumber());

                Intent intent = new Intent(in, ReadManga.class);
                intent.putExtra("chapter_id", mDataset[position].getId());
                intent.putExtra("chapter_number", mDataset[position].getNumber());
                intent.putExtra("manga_id", mangaID);
                intent.putExtra("manga_title", mangaTitle);
                in.startActivity(intent);
            }
        };

        RelativeLayout layout = holder.relativeLayout;

        tv.setOnClickListener(onClickListener);

        layout.setOnClickListener(onClickListener);

        final ImageButton downloadChapter = holder.downloadChapter;
        downloadChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LovelyProgressDialog lpd = new LovelyProgressDialog(in);
                lpd.setMessage(in.getString(R.string.getting_manga));
                lpd.setMessageGravity(Gravity.CENTER);
                lpd.setTitle(in.getString(R.string.please_wait));
                lpd.setTitleGravity(Gravity.CENTER);

                new RetrieveInfo(new AsyncTasks() {

                    String coverURL = "";

                    @Override
                    public void onPreExecute() {

                    }

                    @Override
                    public boolean doInBackground() {

                        try {
                            images = client.getChapterDetails(mDataset[position].getId()).getPages();
                            coverURL = client.getMangaDetails(mangaID).getImage();
                            //AppUtil.downloadChapter(in, , mangaTitle, mDataset[position].getNumber());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }

                    @Override
                    public void onPostExecute(Boolean success) {

                        AppUtil.downloadChapter(in, coverURL, images, mangaTitle, mangaID, mDataset[position].getNumber(), true);

                    }
                }).execute();

            }
        });
        
        final String downloadChapterString = in.getResources().getString(R.string.download_chapter);

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                UtilNotification.showMenu(v.getContext(), v, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals(downloadChapterString)) {
                            v.callOnClick();
                            return true;
                        }
                        return false;
                    }
                }, downloadChapterString);
                return true;
            }
        };

        downloadChapter.setOnLongClickListener(longClickListener);

        tv.setOnLongClickListener(longClickListener);

        ib.setOnLongClickListener(longClickListener);

        layout.setOnLongClickListener(longClickListener);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
