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
import java.util.Arrays;
import java.util.List;

import manga.mangaapp.MangaSide.MangaInfoActivity;
import manga.mangaapp.MangaSide.MangaSiteInfoActivity;
import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Image;
import manga.mangaapp.manymanga.data.Manga;
import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.SiteHelper;
import manga.mangaapp.manymanga.sites.implementations.english.MangaReader;
import me.anshulagarwal.expandablemenuoption.ExpandableMenuView;


/**
 * Created by Jacob on 8/17/17.
 */

public class Manga2Adapter extends RecyclerView.Adapter<Manga2Adapter.ViewHolder> {
    //The list of contacts
    private ArrayList<Manga> mDataset;
    //Contact activity
    MainActivity in;
    int pageNumber = 0;
    int chapterNumber = 0;
    Chapter[] chaptersList;
    Site sites;
    Layouts layoutType;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView imageView;
        public TextView summary;

        public ViewHolder(View v, Layouts layouts) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            imageView = v.findViewById(R.id.imageButton);
            if (layouts.equals(Layouts.DETAILS))
                summary = v.findViewById(R.id.details);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public Manga2Adapter(ArrayList<Manga> myDataset, MainActivity in, Site siteList, Layouts layoutType) {
        mDataset = myDataset;
        this.in = in;
        sites = siteList;
        this.layoutType = layoutType;

    }

    public Manga2Adapter(ArrayList<Manga> myDataset, MainActivity in, Layouts layoutType) {
        mDataset = myDataset;
        this.in = in;
        this.layoutType = layoutType;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Manga2Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = null;

        switch (layoutType) {

            case STAGGERED_THUMBNAILS:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.manga_detail_layout, parent, false);
                break;

            case THUMBNAILS:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.manga_detail_layout, parent, false);
                break;

            case DETAILS:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.manga_detailed_layout, parent, false);
                break;

        }


        // set the view's size, margins, paddings and layout parameters

        Manga2Adapter.ViewHolder vh = new Manga2Adapter.ViewHolder(v, layoutType);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

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

                    String cover = null;
                    Help.i(Arrays.toString(mDataset.toArray()));
                    int chapterCount = 0;
                    try {
                        cover = sites.coverURL(mDataset.get(position));
                        chapterCount = sites.getChapterList(mDataset.get(position)).size();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String des = null;

                    try {
                        des = "\nChapter Count: " + chapterCount + "\n" + sites.getMangaSummary(mDataset.get(position));
                    } catch (Exception e) {
                        e.printStackTrace();
                        des = "N/A";
                    }

                    final String finalCover = cover;
                    final String finalDes = des;

                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            int width = 600;
                            int height = 800;

                            if (layoutType.equals(Layouts.DETAILS)) {
                                TextView details = holder.summary;
                                details.setText(finalDes);
                                width *= .5;
                                height *= .5;
                            } else {
                                width *= .6;
                                height *= .6;
                            }

                            try {

                                Help.v(finalCover);

                                Picasso.with(in)
                                        .load(finalCover)
                                        .resize(width, height)
                                        .centerInside()
                                        .placeholder(android.R.mipmap.sym_def_app_icon)
                                        .into(ib);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Help.e("IB STUFF", ib.getDrawable().getBounds().flattenToString());
                        }
                    });


                    return true;
                }

                @Override
                public void onPostExecute(Boolean success) {

                }
            }).execute();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(in, MangaSiteInfoActivity.class);
                StoryWorld.setSite(sites);
                i.putExtra("manga_link", mDataset.get(position).getLink());
                i.putExtra("manga_title", mDataset.get(position).getTitle());
                i.putExtra("manga_source", sites.getName());
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
