package manga.mangaapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import manga.mangaapp.MangaSide.MangaInfoActivity;
import manga.mangaapp.mangaedenclient.Chapter;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.Manga;
import manga.mangaapp.mangaedenclient.MangaDetails;
import manga.mangaapp.mangaedenclient.MangaEden;
import manga.mangaapp.mangaedenclient.MangaEdenClient;

/**
 * Created by Jacob on 8/17/17.
 */

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.ViewHolder>{
    //The list of contacts
    private ArrayList<Manga> mDataset;
    //Contact activity
    MainActivity in;
    MangaEdenClient client;
    int pageNumber = 0;
    int chapterNumber = 0;
    ChapterPage[] chaptersList;
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
            if(layouts.equals(Layouts.DETAILS))
                summary = v.findViewById(R.id.details);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public MangaAdapter(ArrayList<Manga> myDataset, MainActivity in, MangaEdenClient client, Layouts layoutType) {
        mDataset = myDataset;
        this.in = in;
        this.client = client;
        this.layoutType = layoutType;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MangaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
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

        ViewHolder vh = new ViewHolder(v, layoutType);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        TextView tv = holder.mTextView;
        final ImageView ib = holder.imageView;
        tv.setText(mDataset.get(position).getTitle());

        final Manga manga = mDataset.get(position);

        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public boolean doInBackground() {
                try {
                    // Get manga details
                    MangaDetails mangaDetails = client.getMangaDetails(manga.getId());
                    Chapter[] chapters = mangaDetails.getChapters();
                    try {

                        // Get chapter details
                    /*ChapterDetails chapterDetails = client.getChapterDetails(chapters[0].getId());
                    ChapterPage[] pages = chapterDetails.getPages();
                    chaptersList = pages;*/
                        // Get chapter page image URLs
                        //final URI imageUrl = pages[0].getImageURI();
                        final URI imageUrl = MangaEden.manga2ImageURI(mangaDetails.getImage());
                        final String detail = "\nChapter Count: " + chapters.length+"\n"+mangaDetails.getDescription();
                        Handler uiHandler = new Handler(Looper.getMainLooper());
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                int width = 600;
                                int height = 800;

                                if(layoutType.equals(Layouts.DETAILS)) {
                                    TextView details = holder.summary;
                                    details.setText(detail);
                                    width*=.5;
                                    height*=.5;
                                } else {
                                    width*=.6;
                                    height*=.6;
                                }

                                Picasso.with(in)
                                        .load(String.valueOf(imageUrl))
                                        .resize(width, height)
                                        .centerInside()
                                        .placeholder(android.R.mipmap.sym_def_app_icon)
                                        .into(ib);

                                Help.e("IB STUFF", ib.getDrawable().getBounds().flattenToString());
                            }
                        });
                    } catch (ArrayIndexOutOfBoundsException e1) {
                        e1.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onPostExecute(Boolean success) {

            }
        }).execute();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(in, MangaInfoActivity.class);
                i.putExtra("manga_id", manga.getId());
                in.startActivity(i);

            }
        };

        ib.setOnClickListener(onClickListener);
        tv.setOnClickListener(onClickListener);
        if(layoutType.equals(Layouts.DETAILS)) {
            TextView details = holder.summary;
            details.setOnClickListener(onClickListener);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void notifyData(ArrayList<Manga> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.mDataset = myList;
        //notifyItemInserted(mDataset.size()-1);
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(mDataset.size()-1);
            }
        });
    }

}
