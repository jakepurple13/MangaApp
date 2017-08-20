package manga.mangaapp;

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

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import manga.mangaapp.mangaedenclient.Chapter;
import manga.mangaapp.mangaedenclient.ChapterDetails;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.Manga;
import manga.mangaapp.mangaedenclient.MangaDetails;
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

    public MangaAdapter(ArrayList<Manga> myDataset, MainActivity in, MangaEdenClient client) {
        mDataset = myDataset;
        this.in = in;
        this.client = client;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MangaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        ImageView ib = holder.imageView;
        tv.setText(mDataset.get(position).getTitle());

        Manga manga = mDataset.get(position);
        new RetrieveManga(manga, ib).execute();


    }

    class RetrieveManga extends AsyncTask<String, Void, Boolean> {

        ImageView ib;
        Manga manga;

        public RetrieveManga(Manga m, ImageView ImageView) {
            this.manga = m;
            ib = ImageView;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                // Get manga details
                MangaDetails mangaDetails = client.getMangaDetails(manga.getId());
                Chapter[] chapters = mangaDetails.getChapters();
                try {

                    // Get chapter details
                    ChapterDetails chapterDetails = client.getChapterDetails(chapters[0].getId());
                    ChapterPage[] pages = chapterDetails.getPages();


                    // Get chapter page image URLs
                    final URI imageUrl = pages[0].getImageURI();
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(in)
                                    .load(String.valueOf(imageUrl))
                                    .placeholder(android.R.mipmap.sym_def_app_icon)
                                    .fit()
                                    .into(ib);
                        }
                    });
                } catch (ArrayIndexOutOfBoundsException e1) {
                    e1.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            // TODO: check this.exception
            // TODO: do something with the feed

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
