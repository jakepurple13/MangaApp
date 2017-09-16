package manga.mangaapp.MangaSide;

/**
 * Created by Jacob on 9/14/17.
 */

import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import manga.mangaapp.AsyncTasks;
import manga.mangaapp.Help;
import manga.mangaapp.R;
import manga.mangaapp.RetrieveInfo;
import manga.mangaapp.StoryWorld;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Image;
import manga.mangaapp.manymanga.data.Manga;
import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.SiteHelper;
import manga.mangaapp.manymanga.sites.implementations.english.MangaFox;
import manga.mangaapp.manymanga.sites.implementations.english.MangaHereEnglish;
import manga.mangaapp.manymanga.sites.implementations.english.MangaReader;

public class MangaSiteInfoActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ImageView imageView;

    TextView title;
    TextView description;
    TextView link;

    SiteChapterListAdapter chapterListAdapter;

    List<Chapter> chapterList;
    Chapter[] chapters;

    Site s;

    String summary = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_info);

        imageView = findViewById(R.id.manga_cover);

        title = findViewById(R.id.manga_title);
        description = findViewById(R.id.manga_description);
        link = findViewById(R.id.manga_link);

        mRecyclerView = findViewById(R.id.chapter_list);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL ));

        final String mangaLink = getIntent().getStringExtra("manga_link");
        final String mangaTitle = getIntent().getStringExtra("manga_title");

        final Manga m = new Manga(mangaLink, mangaTitle);

        String source = "";
        GenreTags.Sources tags = null;
        try {

            source = getIntent().getStringExtra("manga_source");
            Help.e(source);
            tags = GenreTags.Sources.fromString(source.replaceAll(" ", ""));
            Help.e(tags.source);
            Help.e(SiteHelper.getEnglishSiteFromString(tags).getName());

        } catch(Exception e) {
            e.printStackTrace();
        }


        final GenreTags.Sources finalTags = tags;
        final String finalSource = source;
        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPreExecute() {
                title.setText("Please wait");
                s = StoryWorld.getSite();
                s = SiteHelper.getEnglishSiteFromString(finalTags);
            }

            @Override
            public boolean doInBackground() {

                try {
                    String url = s.coverURL(m);
                    link.setText(s.getUrl()+mangaLink);
                    Help.w(url);
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    final String finalUrl1 = url;
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                Picasso.with(MangaSiteInfoActivity.this)
                                        .load(finalUrl1)
                                        .resize(600 / 2, 800 / 2)
                                        .placeholder(android.R.mipmap.sym_def_app_icon)
                                        .into(imageView);

                            } catch(Exception e1) {
                                e1.printStackTrace();
                            }

                        }
                    });

                } catch(Exception e1) {
                    e1.printStackTrace();
                }

                try {

                    Help.e(s.getClass().getSimpleName());

                    chapterList = new ArrayList<>();
                    chapterList = s.getChapterList(m);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    summary = s.getMangaSummary(m);
                    Help.i(summary);
                } catch(Exception e) {
                    e.printStackTrace();
                    summary = "N/A";
                }

                return true;
            }

            @Override
            public void onPostExecute(Boolean success) {

                Collections.sort(chapterList, new Comparator<Chapter>() {
                    @Override
                    public int compare(Chapter t, Chapter t1) {

                        try {

                            int num1 = Integer.parseInt(t.getTitle().substring(0, t.getTitle().indexOf(":")).trim());
                            int num2 = Integer.parseInt(t1.getTitle().substring(0, t1.getTitle().indexOf(":")).trim());

                            return num2 - num1;

                        } catch(StringIndexOutOfBoundsException e) {
                            Help.w(e.getLocalizedMessage());
                            return t.getTitle().compareTo(t1.getTitle());
                        }
                    }
                });

                chapterListAdapter = new SiteChapterListAdapter(chapterList, MangaSiteInfoActivity.this, m, finalSource);
                mRecyclerView.setAdapter(chapterListAdapter);
                title.setText(m.getTitle());
                description.setText(summary);
            }
        }).execute();



    }


}
