package manga.mangaapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import manga.mangaapp.MangaSide.MangaInfoActivity;
import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Image;
import manga.mangaapp.manymanga.data.Manga;
import manga.mangaapp.manymanga.sites.Site;
import me.anshulagarwal.expandablemenuoption.ExpandableMenuView;


/**
 * Created by Jacob on 8/17/17.
 */

public class Manga2Adapter extends RecyclerView.Adapter<Manga2Adapter.ViewHolder>{
    //The list of contacts
    private ArrayList<Manga> mDataset;
    //Contact activity
    MainActivity in;
    int pageNumber = 0;
    int chapterNumber = 0;
    Chapter[] chaptersList;
    Site sites;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            imageView = v.findViewById(R.id.imageButton);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public Manga2Adapter(ArrayList<Manga> myDataset, MainActivity in, Site siteList) {
        mDataset = myDataset;
        this.in = in;
        sites = siteList;
    }

    public Manga2Adapter(ArrayList<Manga> myDataset, MainActivity in) {
        mDataset = myDataset;
        this.in = in;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Manga2Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manga_detail_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        TextView tv = holder.mTextView;
        final ImageView ib = holder.imageView;
        tv.setText(mDataset.get(position).getTitle());

        try {


            new RetrieveInfo(new AsyncTasks() {
                @Override
                public void onPreExecute() {

                }

                @Override
                public boolean doInBackground() {

                    List<Chapter> c;
                    List<Image> images = null;

                    try {
                        c = sites.getChapterList(mDataset.get(position));
                        images = sites.getChapterImageLinks(c.get(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {

                        final String imageUrl = images.get(0).getLink();

                        Handler uiHandler = new Handler(Looper.getMainLooper());
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(in)
                                        .load(imageUrl)
                                        .placeholder(android.R.mipmap.sym_def_app_icon)
                                        .into(ib);
                            }
                        });
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                @Override
                public void onPostExecute(Boolean success) {

                }
            }).execute();

        } catch(NullPointerException e) {
            e.printStackTrace();
        }

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(in, MangaInfoActivity.class);
                i.putExtra("manga_id", mDataset.get(0).getLink());
                in.startActivity(i);

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
