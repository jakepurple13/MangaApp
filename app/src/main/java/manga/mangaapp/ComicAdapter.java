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

import com.karumi.marvelapiclient.CharacterApiClient;
import com.karumi.marvelapiclient.ComicApiClient;
import com.karumi.marvelapiclient.MarvelApiConfig;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.model.CharactersDto;
import com.karumi.marvelapiclient.model.CharactersQuery;
import com.karumi.marvelapiclient.model.ComicDto;
import com.karumi.marvelapiclient.model.ComicsDto;
import com.karumi.marvelapiclient.model.ComicsQuery;
import com.karumi.marvelapiclient.model.MarvelImage;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jacob on 8/17/17.
 */

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder>{
    //The list of contacts
    private List<ComicDto> mDataset;
    //Contact activity
    MainActivity in;

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

    public ComicAdapter(List<ComicDto> myDataset, MainActivity in) {
        mDataset = myDataset;
        this.in = in;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ComicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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

        new RetrieveComic(mDataset.get(position), ib).execute();

    }

    class RetrieveComic extends AsyncTask<String, Void, Boolean> {

        ImageView ib;
        ComicDto comic;

        public RetrieveComic(ComicDto comic, ImageView ImageView) {
            ib = ImageView;
            this.comic = comic;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... urls) {

                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        MarvelImage marvelImage = comic.getThumbnail();

                        Picasso.with(in)
                                .load(marvelImage.getImageUrl(MarvelImage.Size.LANDSCAPE_AMAZING))
                                .placeholder(android.R.mipmap.sym_def_app_icon)
                                .into(ib);

                    }
                });
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
