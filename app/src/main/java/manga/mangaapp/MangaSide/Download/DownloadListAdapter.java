package manga.mangaapp.MangaSide.Download;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import manga.mangaapp.AppUtil;
import manga.mangaapp.R;

/**
 * Created by Jacob on 10/17/17.
 */

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.ViewHolder> {

    AppUtil.MangaListFile mangaFiles;

    Activity in;

    public DownloadListAdapter(Activity in, AppUtil.MangaListFile mangaFiles) {
        this.mangaFiles = mangaFiles;
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
    public DownloadListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.downloaded_chapter_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView title = holder.title;

        title.setText(mangaFiles.getName());

        TextView chapList = holder.chapNum;

        chapList.setText(mangaFiles.getFiles().get(position).getName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mangaFiles.getFiles().size();
    }

}