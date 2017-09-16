package manga.mangaapp.MangaSide;

/**
 * Created by Jacob on 9/14/17.
 */

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import manga.mangaapp.AsyncTasks;
import manga.mangaapp.R;
import manga.mangaapp.RetrieveInfo;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.gigamole.infinitecycleviewpager.OnInfiniteCyclePageTransformListener;
import com.squareup.picasso.Picasso;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import manga.mangaapp.AsyncTasks;
import manga.mangaapp.Help;
import manga.mangaapp.R;
import manga.mangaapp.RetrieveInfo;
import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Image;
import manga.mangaapp.manymanga.data.Manga;
import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.SiteHelper;

public class ReadMangaSite extends AppCompatActivity {

    int pageNumber = 0;

    List<Image> pages;

    ViewPager viewPager;

    FABRevealLayout revealLayout;

    RelativeLayout layout;

    Site site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);

        //final String chapterID = getIntent().getStringExtra("chapter_id");

        String mangaTitle = getIntent().getStringExtra("manga_title");
        String mangaLink = getIntent().getStringExtra("manga_link");

        pageNumber = getIntent().getIntExtra("chapter", 0);

        String source;
        GenreTags.Sources tags = null;
        try {

            source = getIntent().getStringExtra("manga_source");
            Help.e(source);
            tags = GenreTags.Sources.fromString(source.replaceAll(" ", ""));
            Help.e(tags.source);
            site = SiteHelper.getEnglishSiteFromString(tags);

        } catch(Exception e) {
            e.printStackTrace();
        }

        final Manga m = new Manga(mangaLink, mangaTitle);

        layout = findViewById(R.id.reading_layout);

        revealLayout = findViewById(R.id.fab_reveal_layout);

        revealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {
                revealLayout.findViewById(R.id.show_and_hide).animate().alpha(0.5f);
            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabRevealLayout, View secondaryView) {

                Button left = secondaryView.findViewById(R.id.back_page);

                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                    }
                });

                Button right = secondaryView.findViewById(R.id.forward_page);

                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }
                });

                Button close = secondaryView.findViewById(R.id.close_view);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fabRevealLayout.revealMainView();
                    }
                });

            }
        });

        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPreExecute() {

                pages = new ArrayList<>();

            }

            @Override
            public boolean doInBackground() {

                try {

                    List<Chapter> page = site.getChapterList(m);
                    pages = site.getChapterImageLinks(page.get(pageNumber));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public void onPostExecute(Boolean success) {
                PagerAdapter adapter = new MangaPageSite(pages, ReadMangaSite.this);
                viewPager.setAdapter(adapter);
            }
        }).execute();

        viewPager = findViewById(R.id.viewpages);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle("Page " + (position+1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setPageMarginDrawable(android.R.drawable.dark_header);

        revealLayout.findViewById(R.id.show_and_hide).animate().alpha(0.5f);

    }
}
