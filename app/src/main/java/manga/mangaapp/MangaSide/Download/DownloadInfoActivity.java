package manga.mangaapp.MangaSide.Download;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import manga.mangaapp.AppUtil;
import manga.mangaapp.Help;
import manga.mangaapp.R;

public class DownloadInfoActivity extends AppCompatActivity {

    AppUtil.MangaListFile mangaListFile;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_info);

        String title = getIntent().getStringExtra("download_manga_title");

        mangaListFile = AppUtil.getMangaFromDownloads(title);



        titleView = findViewById(R.id.download_title);
        titleView.setText(title);

        mRecyclerView = findViewById(R.id.download_chapter_list);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAdapter = new DownloadListAdapter(this, mangaListFile);

        for(AppUtil.MangaFile f : mangaListFile.getFiles()) {
            Help.w(f.getName());
        }

        mRecyclerView.setAdapter(mAdapter);

    }
}
