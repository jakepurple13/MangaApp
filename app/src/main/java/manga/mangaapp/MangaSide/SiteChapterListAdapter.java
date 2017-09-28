package manga.mangaapp.MangaSide;

/**
 * Created by Jacob on 9/14/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import manga.mangaapp.AppUtil;
import manga.mangaapp.AsyncTasks;
import manga.mangaapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.IOException;
import java.util.List;

import manga.mangaapp.MainActivity;
import manga.mangaapp.R;
import manga.mangaapp.RetrieveInfo;
import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Manga;
import manga.mangaapp.manymanga.sites.Site;

/**
 * Created by Jacob on 8/23/17.
 */

public class SiteChapterListAdapter extends RecyclerView.Adapter<SiteChapterListAdapter.ViewHolder>{
    //The list of contacts
    private List<Chapter> mDataset;
    //private Chapter[] mDataset;
    //Contact activity
    Activity in;
    Manga m;
    String source;
    Site s;

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
    public SiteChapterListAdapter(List<Chapter> myDataset, Activity in, Manga manga, String source, Site s) {
        mDataset = myDataset;
        this.in = in;
        m = manga;
        this.source = source;
        this.s = s;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SiteChapterListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_list_detail_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        SiteChapterListAdapter.ViewHolder vh = new SiteChapterListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(SiteChapterListAdapter.ViewHolder holder, final int position) {

        TextView tv = holder.mTextView;
        ImageView ib = holder.imageView;
        ib.setVisibility(View.GONE);
        //String title = mDataset.get(position).getTitle()!=null ? mDataset.get(position).getTitle() : "";
        String text = mDataset.get(position).getTitle();// + ". " + title;
        tv.setText(text);
        tv.setTextSize(15f);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(in, ReadMangaSite.class);
                intent.putExtra("manga_link", m.getLink());
                intent.putExtra("manga_title", m.getTitle());
                intent.putExtra("chapter", position);
                intent.putExtra("manga_source", source);
                in.startActivity(intent);
            }
        };

        RelativeLayout layout = holder.relativeLayout;

        tv.setOnClickListener(onClickListener);

        layout.setOnClickListener(onClickListener);

        ImageButton downloadChapter = holder.downloadChapter;
        downloadChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LovelyProgressDialog lpd = new LovelyProgressDialog(in);
                lpd.setMessage("Getting Manga");
                lpd.setMessageGravity(Gravity.CENTER);
                lpd.setTitle("Please Wait...");
                lpd.setTitleGravity(Gravity.CENTER);

                new RetrieveInfo(new AsyncTasks() {
                    @Override
                    public void onPreExecute() {

                    }

                    @Override
                    public boolean doInBackground() {

                        try {
                            AppUtil.downloadChapter(in, s.getChapterImageLinks(mDataset.get(position)), m.getTitle(), mDataset.size()-position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return false;
                    }

                    @Override
                    public void onPostExecute(Boolean success) {

                    }
                }, lpd).execute();

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

