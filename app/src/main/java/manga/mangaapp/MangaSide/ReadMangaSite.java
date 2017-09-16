package manga.mangaapp.MangaSide;

/**
 * Created by Jacob on 9/14/17.
 */

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import jp.s64.android.animatedtoolbar.AnimatedToolbar;
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
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.gigamole.infinitecycleviewpager.OnInfiniteCyclePageTransformListener;
import com.squareup.picasso.Picasso;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

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

    Manga m;

    boolean isMenuShowing = false;

    DiscreteSeekBar seekBar;

    TextView pageCount;

    Handler h = new Handler();

    Runnable hideMenu = new Runnable() {
        @Override
        public void run() {
            revealLayout.revealMainView();
        }
    };

    final int DELAY_AMOUNT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);
        Toolbar mToolbar = (AnimatedToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

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

        m = new Manga(mangaLink, mangaTitle);

        layout = findViewById(R.id.reading_layout);

        revealLayout = findViewById(R.id.fab_reveal_layout);

        revealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {
                revealLayout.findViewById(R.id.show_and_hide).animate().alpha(0.5f);
                //revealLayout.findViewById(R.id.show_and_hide).animate().alpha(0f);
                isMenuShowing = false;
                getSupportActionBar().hide();
                h.removeCallbacks(hideMenu); // cancel the running action (the hiding process)
            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabRevealLayout, View secondaryView) {
                isMenuShowing = true;
                getSupportActionBar().show();
                h.postDelayed(hideMenu, DELAY_AMOUNT); // start a new hiding process that will trigger after 5 seconds
            }
        });

        getSupportActionBar().hide();

        pageCount = findViewById(R.id.page_count);
        seekBar = findViewById(R.id.page_chooser);
        seekBar.setMin(0);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                viewPager.setCurrentItem(value);
                pageCount.setText((value+1)+"/"+pages.size());
                h.removeCallbacks(hideMenu); // cancel the running action (the hiding process)
                h.postDelayed(hideMenu, DELAY_AMOUNT); // start a new hiding process that will trigger after 5 seconds
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        getPages(m, pageNumber, new GetPagePost() {
            @Override
            public void postExecute() {
                setTitle("Page " + 1 + "/" + pages.size());
                seekBar.setMax(pages.size());
                pageCount.setText(1+"/"+pages.size());
            }
        });

        viewPager = findViewById(R.id.viewpages);

        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

                h.removeCallbacks(hideMenu); // cancel the running action (the hiding process)
                h.postDelayed(hideMenu, DELAY_AMOUNT); // start a new hiding process that will trigger after 5 seconds

                if(position>=pages.size()) {
                    getPages(m, pageNumber++, new GetPagePost() {
                        @Override
                        public void postExecute() {
                            setTitle("Page " + (position+1) + "/" + pages.size());
                            seekBar.setProgress(0);
                            viewPager.setCurrentItem(0, true);
                        }
                    });
                } else {

                    setTitle("Page " + (position + 1) + "/" + pages.size());
                    seekBar.setProgress(position);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setPageMarginDrawable(android.R.drawable.dark_header);

        revealLayout.findViewById(R.id.show_and_hide).animate().alpha(0.5f);

    }

    public void getPages(final Manga manga, final int pageNumber, final GetPagePost pagePost) {
        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPreExecute() {

                pages = new ArrayList<>();

            }

            @Override
            public boolean doInBackground() {

                try {

                    List<Chapter> page = site.getChapterList(manga);
                    pages = site.getChapterImageLinks(page.get(pageNumber));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public void onPostExecute(Boolean success) {

                pagePost.postExecute();

                PagerAdapter adapter = new MangaPageSite(pages, ReadMangaSite.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isMenuShowing) {
                            revealLayout.revealMainView();
                        } else {
                            revealLayout.revealSecondaryView();
                        }
                    }
                });
                seekBar.setMax(pages.size());
                seekBar.setProgress(0);
                viewPager.setAdapter(adapter);
            }
        }).execute();
    }

    @Override
    public void onBackPressed() {

        if(isMenuShowing) {
            revealLayout.revealMainView();
        } else {
            super.onBackPressed();
        }
    }

}

interface GetPagePost {

    void postExecute();

}
