package manga.mangaapp.MangaSide;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.gigamole.infinitecycleviewpager.OnInfiniteCyclePageTransformListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;
import com.squareup.picasso.Picasso;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import jp.s64.android.animatedtoolbar.AnimatedToolbar;
import manga.mangaapp.AsyncTasks;
import manga.mangaapp.Help;
import manga.mangaapp.R;
import manga.mangaapp.RetrieveInfo;
import manga.mangaapp.UserInfo.FirebaseDatabaseUtil;
import manga.mangaapp.UserInfo.MangaTable;
import manga.mangaapp.mangaedenclient.Chapter;
import manga.mangaapp.mangaedenclient.ChapterDetails;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.MangaDetails;
import manga.mangaapp.mangaedenclient.MangaEden;
import manga.mangaapp.mangaedenclient.MangaEdenClient;
import programmer.box.utilityhelper.UtilNotification;

public class ReadManga extends AppCompatActivity {

    int pageNumber = 0;

    ChapterPage[] pages;

    ViewPager viewPager;

    FABRevealLayout revealLayout;

    RelativeLayout layout;

    boolean isMenuShowing = false;

    String chapID;

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

    int chapterNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);
        Toolbar mToolbar = (AnimatedToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.cardview_dark_background));

        final String chapterID = getIntent().getStringExtra("chapter_id");
        final String mangaID = getIntent().getStringExtra("manga_id");
        final String mangaTitle = getIntent().getStringExtra("manga_title");
        chapterNumber = getIntent().getIntExtra("chapter_number", 0);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        try {
            FirebaseDatabaseUtil.updateChapterLocal(ReadManga.this, mangaID, chapterID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(user!=null) {
            FirebaseDatabaseUtil.writeNewPost(mangaID, chapterID, mangaTitle);
        }


        layout = findViewById(R.id.reading_layout);

        revealLayout = findViewById(R.id.fab_reveal_layout);

        revealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {
                revealLayout.findViewById(R.id.show_and_hide).animate().alpha(0.5f);
                //revealLayout.findViewById(R.id.show_and_hide).animate().alpha(0f);
                hide();
            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabRevealLayout, View secondaryView) {
                show();
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
                pageCount.setText((value+1)+"/"+pages.length);
                //h.removeCallbacks(hideMenu); // cancel the running action (the hiding process)
                //h.postDelayed(hideMenu, DELAY_AMOUNT); // start a new hiding process that will trigger after 5 seconds
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                h.removeCallbacks(hideMenu); // cancel the running action (the hiding process)
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                h.postDelayed(hideMenu, DELAY_AMOUNT);
            }
        });

        getPages(chapterID, new GetPagePost() {
            @Override
            public void postExecute() {
                setTitle("Page " + 1 + "/" + pages.length);
                seekBar.setMax(pages.length);
                pageCount.setText(1+"/"+pages.length);
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

                Help.v(position + "/" + pages.length);

                if(position>=pages.length) {

                    new RetrieveInfo(new AsyncTasks() {
                        @Override
                        public void onPreExecute() {
                            Help.e("adsk;fjas;kldfj;laskdjf;aks");
                        }

                        @Override
                        public boolean doInBackground() {
                            MangaEdenClient client = new MangaEdenClient();

                            try {
                                MangaDetails mangaDetails = client.getMangaDetails(mangaID);

                                Chapter[] c = mangaDetails.getChapters();

                                chapterNumber++;

                                chapID = c[chapterNumber].getId();

                                SharedPreferencesManager.getInstance().putValue(mangaID, c[chapterNumber].getNumber());

                                Help.e(Arrays.toString(pages));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                                UtilNotification.showSnackbar(pageCount, "You're all caught up!", UtilNotification.Lengths.LONG, "Awesome!", new UtilNotification.SnackBarAction() {
                                    @Override
                                    public void snackClick(Snackbar snackbar) {
                                        snackbar.dismiss();
                                    }
                                });
                                ReadManga.this.finish();
                            }
                            return true;
                        }

                        @Override
                        public void onPostExecute(Boolean success) {

                            getPages(chapID, new GetPagePost() {
                                @Override
                                public void postExecute() {
                                    setTitle("Page " + (position+1) + "/" + pages.length);
                                    seekBar.setMax(pages.length);
                                    seekBar.setProgress(0);
                                    viewPager.setCurrentItem(0, true);

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    try {
                                        FirebaseDatabaseUtil.updateChapterLocal(ReadManga.this, mangaID, chapID);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if(user!=null) {
                                        FirebaseDatabaseUtil.writeNewPost(mangaID, chapID, mangaTitle);
                                    }

                                }
                            });

                        }
                    }).execute();

                } else {

                    setTitle("Page " + (position+1) + "/" + pages.length);
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

    public void getPages(final String chapterID, final GetPagePost pagePost) {
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
                    Help.e(Arrays.toString(pages));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onPostExecute(Boolean success) {

                pagePost.postExecute();

                PagerAdapter adapter = new MangaPage(pages, viewPager, ReadManga.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPages(chapterID, pagePost);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isMenuShowing) {
                            revealLayout.revealMainView();
                        } else {
                            revealLayout.revealSecondaryView();
                        }
                    }
                });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hide() {
        isMenuShowing = false;
        getSupportActionBar().hide();
        h.removeCallbacks(hideMenu); // cancel the running action (the hiding process)
    }

    public void show() {
        isMenuShowing = true;
        getSupportActionBar().show();
        h.postDelayed(hideMenu, DELAY_AMOUNT); // start a new hiding process that will trigger after 5 seconds
    }

}