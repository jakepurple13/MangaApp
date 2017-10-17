package manga.mangaapp.UserInfo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import manga.mangaapp.AppUtil;
import manga.mangaapp.Layouts;
import manga.mangaapp.MainActivity;
import manga.mangaapp.MangaAdapter;
import manga.mangaapp.R;

/**
 * Created by Jacob on 10/17/17.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    HashMap<String, AppUtil.MangaListFile> mangaFiles;

    ArrayList<String> keys;

    Activity in;

    public DownloadAdapter(Activity in, ArrayList<String> keys, HashMap<String, AppUtil.MangaListFile> mangaFiles) {
        this.mangaFiles = mangaFiles;
        this.keys = keys;
        this.in = in;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView chapNum;
        public RelativeLayout layout;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.downloaded_title);
            chapNum = (TextView) v.findViewById(R.id.downloaded_chapter_num);
            layout = v.findViewById(R.id.download_list_layout);
        }
    }

    @Override
    public DownloadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.downloaded_chapter_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView title = holder.title;
        title.setText(keys.get(position));

        TextView chapList = holder.chapNum;

        String list = mangaFiles.get(keys.get(position)).getFiles().toString();

        chapList.setText(list);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(in, MainActivity.class);
                //in.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

}
