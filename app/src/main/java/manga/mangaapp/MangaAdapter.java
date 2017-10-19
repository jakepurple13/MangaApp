package manga.mangaapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;
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
import programmer.box.utilityhelper.UtilImage;
import programmer.box.utilityhelper.UtilNotification;

/**
 * Created by Jacob on 8/17/17.
 */

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.ViewHolder>{
    //The list of contacts
    private ArrayList<Manga> mDataset;
    //Contact activity
    Activity in;
    MangaEdenClient client;
    int pageNumber = 0;
    int chapterNumber = 0;
    ChapterPage[] chaptersList;
    Layouts layoutType;

    boolean isDownload = false;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView chapterCount;
        public ImageView imageView;
        public TextView summary;
        public RelativeLayout layout;

        public ViewHolder(View v, Layouts layouts) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            imageView = v.findViewById(R.id.imageButton);
            layout = v.findViewById(R.id.manga_layout);
            if(layouts.equals(Layouts.DETAILS)) {
                summary = v.findViewById(R.id.details);
                chapterCount = v.findViewById(R.id.chapter_count);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    public MangaAdapter(ArrayList<Manga> myDataset, Activity in, MangaEdenClient client, Layouts layoutType) {
        mDataset = myDataset;
        this.in = in;
        this.client = client;
        this.layoutType = layoutType;
    }

    public MangaAdapter(ArrayList<Manga> myDataset, Activity in, MangaEdenClient client, Layouts layoutType, boolean isDownload) {
        mDataset = myDataset;
        this.in = in;
        this.client = client;
        this.layoutType = layoutType;
        this.isDownload = isDownload;
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

        final TextView tv = holder.mTextView;
        final ImageView ib = holder.imageView;
        tv.setText(mDataset.get(position).getTitle());
        tv.setTypeface(null, Typeface.BOLD);

        final Manga manga = mDataset.get(position);

        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public boolean doInBackground() {
                try {
                    // Get manga details
                    final MangaDetails mangaDetails = client.getMangaDetails(manga.getId());
                    final Chapter[] chapters = mangaDetails.getChapters();
                    try {

                        // Get chapter details
                        /*ChapterDetails chapterDetails = client.getChapterDetails(chapters[0].getId());
                        ChapterPage[] pages = chapterDetails.getPages();
                        chaptersList = pages;*/
                        // Get chapter page image URLs
                        //final URI imageUrl = pages[0].getImageURI();
                        final URI imageUrl = MangaEden.manga2ImageURI(mangaDetails.getImage());
                        Palette p;
                        try {
                            p = UtilImage.getPalatteFromUrl(String.valueOf(imageUrl));
                        } catch(IllegalArgumentException e) {
                            p = null;
                        }
                        //final String detail = "Chapter Count: " + chapters.length+"\n\n"+mangaDetails.getDescription();
                        final String detail = mangaDetails.getDescription();
                        Handler uiHandler = new Handler(Looper.getMainLooper());
                        final Palette finalP = p;
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                int width = (int) in.getResources().getDimension(R.dimen.default_image_width);
                                int height = (int) in.getResources().getDimension(R.dimen.default_image_height);

                                if(layoutType.equals(Layouts.DETAILS)) {
                                    TextView details = holder.summary;
                                    details.setText(detail);
                                    TextView chapterCount = holder.chapterCount;
                                    chapterCount.setText("Chapters: " + chapters.length);
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

                                if(mangaDetails.getImage()!=null && SharedPreferencesManager.getInstance().getValue("manga_color", Boolean.class, true)) {

                                    try {

                                        int light = finalP.getLightVibrantColor(in.getColor(R.color.white));
                                        int dark = finalP.getDarkVibrantColor(in.getColor(R.color.md_black_1000));

                                        String hex = "#" + Integer.toHexString(light).substring(2);
                                        if (hex.trim().toLowerCase().matches("#(f[5-9|a-f]){3}")) {
                                            tv.setTextColor(light);
                                            holder.chapterCount.setTextColor(light);
                                            light = dark;
                                        }

                                        //tv.setTextColor(dark);
                                        tv.setBackgroundColor(light);

                                        if (layoutType.equals(Layouts.DETAILS)) {
                                            //holder.summary.setTextColor(dark);
                                            //holder.chapterCount.setBackgroundColor(p.getLightMutedColor(in.getColor(R.color.md_black_1000)));
                                            //holder.chapterCount.setBackgroundColor(p.getDarkMutedColor(in.getColor(R.color.md_black_1000)));
                                            //holder.chapterCount.setBackgroundColor(p.getVibrantColor(in.getColor(R.color.white)));
                                            holder.chapterCount.setBackgroundColor(AppUtil.lighter(light, 0.5f));
                                            holder.summary.setBackgroundColor(in.getColor(R.color.white));
                                        }

                                        int[] colors = {in.getColor(R.color.white), light, in.getColor(R.color.white)};

                                        //create a new gradient color
                                        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

                                        //ib.setBackgroundColor(light);


                                        //gd.setCornerRadius(0f);
                                        //apply the button background to newly created drawable gradient
                                        //holder.layout.setBackground(gd);

                                        holder.layout.setBackgroundColor(light);

                                    } catch(NullPointerException e) {
                                        Help.e(e.getLocalizedMessage());
                                    }
                                }

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
                i.putExtra("manga_downloaded", isDownload);
                in.startActivity(i);

            }
        };

        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                UtilNotification.showMenu(v.getContext(), v, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getTitle().equals("Read Manga")) {
                            v.callOnClick();
                            return true;
                        }

                        return false;
                    }
                }, "Read Manga");

                return true;
            }
        };

        ib.setOnClickListener(onClickListener);
        tv.setOnClickListener(onClickListener);
        ib.setOnLongClickListener(onLongClickListener);
        tv.setOnLongClickListener(onLongClickListener);
        if(layoutType.equals(Layouts.DETAILS)) {
            TextView details = holder.summary;
            TextView chaperCount = holder.chapterCount;
            details.setOnClickListener(onClickListener);
            chaperCount.setOnClickListener(onClickListener);
            details.setOnLongClickListener(onLongClickListener);
            chaperCount.setOnLongClickListener(onLongClickListener);
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
