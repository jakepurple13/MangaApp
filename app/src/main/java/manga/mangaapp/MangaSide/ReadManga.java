package manga.mangaapp.MangaSide;

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

import manga.mangaapp.AsyncTasks;
import manga.mangaapp.Help;
import manga.mangaapp.R;
import manga.mangaapp.RetrieveInfo;
import manga.mangaapp.mangaedenclient.ChapterDetails;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.MangaEden;
import manga.mangaapp.mangaedenclient.MangaEdenClient;

public class ReadManga extends AppCompatActivity {

    int pageNumber = 0;

    ChapterPage[] pages;

    ViewPager viewPager;

    FABRevealLayout revealLayout;

    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);

        final String chapterID = getIntent().getStringExtra("chapter_id");

        layout = findViewById(R.id.reading_layout);

        revealLayout = findViewById(R.id.fab_reveal_layout);

        revealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {

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

            }

            @Override
            public boolean doInBackground() {
                MangaEdenClient client = new MangaEdenClient();

                try {
                    ChapterDetails chapters = client.getChapterDetails(chapterID);
                    pages = chapters.getPages();
                    Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), Arrays.toString(pages));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onPostExecute(Boolean success) {
                PagerAdapter adapter = new MangaPage(pages, ReadManga.this);
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
