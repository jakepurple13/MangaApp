package manga.mangaapp.MangaSide;

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

import com.karumi.marvelapiclient.model.ComicDto;
import com.karumi.marvelapiclient.model.MarvelImage;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import manga.mangaapp.MainActivity;
import manga.mangaapp.R;
import manga.mangaapp.mangaedenclient.Chapter;
import manga.mangaapp.mangaedenclient.ChapterPage;

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
    int currentChapter;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            imageView = v.findViewById(R.id.imageButton);
            relativeLayout = v.findViewById(R.id.manga_layout);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    /*public ChapterListAdapter(List<Chapter> myDataset, Activity in) {
        mDataset = myDataset;
        this.in = in;
    }*/
    public ChapterListAdapter(Chapter[] myDataset, Activity in, String mangaID, int currentChapter) {
        mDataset = myDataset;
        this.in = in;
        this.mangaID = mangaID;
        this.currentChapter = currentChapter;
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
        ImageView ib = holder.imageView;
        ib.setVisibility(View.GONE);
        String title = mDataset[position].getTitle()!=null ? mDataset[position].getTitle() : "";
        String text = mDataset[position].getNumber() + ". " + title;
        if(currentChapter==(mDataset.length-position)) {
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
                in.startActivity(intent);
            }
        };

        RelativeLayout layout = holder.relativeLayout;

        tv.setOnClickListener(onClickListener);

        layout.setOnClickListener(onClickListener);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
