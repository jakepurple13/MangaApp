package manga.mangaapp.MangaSide;

/**
 * Created by Jacob on 9/14/17.
 */

import android.content.Intent;
import android.icu.math.BigDecimal;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    String mangaTitle;
    String mangaLink;

    Manga m;

    String source;
    GenreTags.Sources finalTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_info);

        handleIntent(getIntent());

        imageView = findViewById(R.id.manga_cover);

        title = findViewById(R.id.manga_title);
        description = findViewById(R.id.manga_description);
        link = findViewById(R.id.manga_link);

        mRecyclerView = findViewById(R.id.chapter_list);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL ));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manga_info_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share_manga) {
            shareEmail();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareEmail() {

        String links = mangaLink;//link.getText().toString();

        String text = "<a href=\"" + links + "\">Check out " + m.getTitle() + "</a>";

        String thisApp = "Or check it out in MangaWorld at http://www.mangaworld.com/" + finalTags.shortcut + "/" + m.getTitle().replaceAll(" ", "-");

        //startActivity(Intent.createChooser(intent, "Share " + mangaTitle));

        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/html")
                .setHtmlText(text + "<br><br>" + links + "<br><br>" + thisApp)
                .setSubject("Definitely read " + mangaTitle)
                .setChooserTitle("Share " + mangaTitle)
                .getIntent();

        startActivity(shareIntent);

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {

        String action = intent.getAction();
        Uri data = intent.getData();

        if(Intent.ACTION_VIEW.equals(action) && data!=null) {

            String id = data.getLastPathSegment();

            String sourceId = data.getPathSegments().get(0);
            String titleId = data.getPathSegments().get(1);

            for(String s : data.getPathSegments()) {
                Help.e(s);
            }

            GenreTags.Sources tags = null;
            source = sourceId;

            tags = GenreTags.Sources.fromString(source);

            finalTags = tags;

            s = SiteHelper.getEnglishSiteFromString(finalTags);

            mangaLink = "/" + titleId.replaceAll("_", "-").toLowerCase();
            mangaTitle = titleId.replaceAll("-", " ");

            if(tags.equals(GenreTags.Sources.READMANGATODAY)) {
                mangaLink = s.getUrl() + mangaLink;
            }

            m = new Manga(mangaLink, mangaTitle);

            Help.d("action: " + action + " | data: " + data.toString() + " | id: " + id);

            Help.i(mangaLink);

            Help.i(s.getUrl() + m.getLink());

        } else {
            //mangaID = getIntent().getStringExtra("manga_id");
            mangaLink = getIntent().getStringExtra("manga_link");
            mangaTitle = getIntent().getStringExtra("manga_title");
            m = new Manga(mangaLink, mangaTitle);

            GenreTags.Sources tags = null;
            source = "";
            try {

                source = getIntent().getStringExtra("manga_source");
                Help.e(source);
                tags = GenreTags.Sources.fromString(source.replaceAll(" ", ""));
                Help.e(tags.source);
                Help.e(SiteHelper.getEnglishSiteFromString(tags).getName());

            } catch(Exception e) {
                e.printStackTrace();
            }


            finalTags = tags;

        }

    }


}
