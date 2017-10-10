package manga.mangaapp.MangaSide;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tapadoo.alerter.Alerter;

import junit.framework.Assert;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cubestack.android.lib.storm.service.BaseService;
import in.cubestack.android.lib.storm.service.StormService;
import manga.mangaapp.AppUtil;
import manga.mangaapp.AsyncTasks;
import manga.mangaapp.Help;
import manga.mangaapp.R;
import manga.mangaapp.RetrieveInfo;
import manga.mangaapp.UserInfo.FirebaseDatabaseUtil;
import manga.mangaapp.UserInfo.MangaDatabase;
import manga.mangaapp.UserInfo.MangaTable;
import manga.mangaapp.UserInfo.UserInfo;
import manga.mangaapp.mangaedenclient.Chapter;
import manga.mangaapp.mangaedenclient.ChapterDetails;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.Manga;
import manga.mangaapp.mangaedenclient.MangaDetails;
import manga.mangaapp.mangaedenclient.MangaEden;
import manga.mangaapp.mangaedenclient.MangaEdenClient;

public class MangaInfoActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ImageView imageView;

    TextView title;
    TextView description;
    TextView link;

    FloatingActionButton favButton;

    ChapterListAdapter chapterListAdapter;

    MangaEdenClient client;

    MangaDetails mangaDetails;

    List<Chapter> chapterList;
    Chapter[] chapters;

    boolean favorited = false;

    String chapterID;
    String mangaID;

    String mangaTitle;

    Palette p = null;

    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_info);

        handleIntent(getIntent());

        imageView = findViewById(R.id.manga_cover);

        rl = findViewById(R.id.manga_info_layout);

        favButton = findViewById(R.id.fav_button);

        title = findViewById(R.id.manga_title);
        description = findViewById(R.id.manga_description);
        link = findViewById(R.id.manga_link);

        mRecyclerView = findViewById(R.id.chapter_list);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        chapterID = "";

        if (user != null) {

            FirebaseDatabase.getInstance().getReference("/user-posts/" + user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        if (data.getKey().equals(mangaID)) {
                            //do ur stuff
                            //fav.setText("UnFavorite");
                            favorited = true;
                            favButton.setImageResource(android.R.drawable.star_big_on);
                            for (DataSnapshot postSnapshot1 : data.getChildren()) {

                                Help.i(postSnapshot1.toString());

                                switch (postSnapshot1.getKey()) {
                                    case "chapterid":
                                        chapterID = postSnapshot1.getValue(String.class);
                                        break;
                                    default:
                                        break;

                                }

                            }
                            break;
                        } else {
                            //do something
                            //fav.setText("Favorite");
                            favorited = false;
                            favButton.setImageResource(android.R.drawable.star_big_off);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            MangaTable mt = FirebaseDatabaseUtil.findLocal(this, mangaID);
            if (mt != null) {
                favorited = true;
                favButton.setImageResource(android.R.drawable.star_big_on);
                chapterID = mt.getChapterID();
            } else {
                favorited = false;
                favButton.setImageResource(android.R.drawable.star_big_off);
            }
        }

        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPreExecute() {
                title.setText("Please wait");
            }

            @Override
            public boolean doInBackground() {

                client = new MangaEdenClient();

                try {
                    mangaDetails = client.getMangaDetails(mangaID);

                    chapters = mangaDetails.getChapters();

                    mangaTitle = mangaDetails.getTitle();

                    //ChapterDetails chapterDetails = client.getChapterDetails(chapters[0].getId());
                    //chapterPages = chapterDetails.getPages();

                    Help.e(mangaDetails.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    final URI imageUrl = MangaEden.manga2ImageURI(mangaDetails.getImage());
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(MangaInfoActivity.this)
                                    .load(String.valueOf(imageUrl))
                                    .resize(300, 400)
                                    .placeholder(android.R.mipmap.sym_def_app_icon)
                                    .into(imageView);
                        }
                    });

                    try {
                        URL url_value = new URL(String.valueOf(imageUrl));
                        Bitmap mIcon1 = BitmapFactory.decodeStream(url_value.openConnection().getInputStream());
                        p = Palette.from(mIcon1).generate();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Help.w("Didn't get it!");
                        p = null;
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    p = null;
                }

                chapterList = Arrays.asList(mangaDetails.getChapters());

                Collections.sort(chapterList, new ChapterCompare());

                //Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), chapterList.toString());
                Log.e("Line_" + new Throwable().getStackTrace()[0].getLineNumber(), chapters.toString());
                Log.e("Line_" + new Throwable().getStackTrace()[0].getLineNumber(), Arrays.deepToString(chapters));


                return true;
            }

            @Override
            public void onPostExecute(Boolean success) {

                Arrays.sort(chapters, new Comparator<Chapter>() {
                    @Override
                    public int compare(Chapter t, Chapter t1) {
                        //return Long.valueOf(t.getDate()).compareTo(t1.getDate());
                        //return Long.valueOf(t.getDate()).compareTo(t1.getDate());
                        return Integer.valueOf(t1.getNumber()).compareTo(t.getNumber());
                    }
                });

                //if(p!=null) {
                if(mangaDetails.getImage()!=null && SharedPreferencesManager.getInstance().getValue("manga_color", Boolean.class, true)) {

                    int vibrantColor = p.getLightVibrantColor(getColor(R.color.white));
                    int darkVibrant = p.getDarkVibrantColor(getColor(R.color.md_black_1000));

                    //MangaInfoActivity.this.getActionBar().setBackgroundDrawable(new ColorDrawable(vibrantColor));

                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(vibrantColor));

                    String hex = "#"+Integer.toHexString(darkVibrant).substring(2);
                    String hex1 = "#"+Integer.toHexString(vibrantColor).substring(2);

                    getSupportActionBar().setTitle(Html.fromHtml("<font color='"+hex+"'>"+mangaDetails.getTitle()+"</font>"));

                    rl.setBackgroundColor(vibrantColor);
                    mRecyclerView.setBackgroundColor(vibrantColor);
                    //imageView.setBackgroundColor(vibrantColor);
                    title.setBackgroundColor(vibrantColor);
                    description.setBackgroundColor(vibrantColor);

                    Help.d(darkVibrant + " color and " + hex);

                    Help.d(vibrantColor + " color and " + hex1);

                    link.setBackgroundColor(vibrantColor);
                    title.setTextColor(darkVibrant);
                    description.setTextColor(darkVibrant);
                    link.setLinkTextColor(darkVibrant);

                    Window window = getWindow();
                    window.setStatusBarColor(darkVibrant);

                    favButton.setBackgroundTintList(ColorStateList.valueOf(darkVibrant));

                    chapterListAdapter = new ChapterListAdapter(chapters, MangaInfoActivity.this, mangaID, chapterID, mangaDetails.getTitle(), client, darkVibrant);

                } else {
                    chapterListAdapter = new ChapterListAdapter(chapters, MangaInfoActivity.this, mangaID, chapterID, mangaDetails.getTitle(), client);
                    setTitle(mangaDetails.getTitle());
                }

                mRecyclerView.setAdapter(chapterListAdapter);
                String titleText = mangaDetails.getTitle() + "\nTags: ";

                String[] cat = mangaDetails.getCategories();

                for (String s : cat) {
                    titleText += s + "\t";
                }

                title.setText(titleText);
                description.setText(mangaDetails.getDescription());
                link.setText(mangaDetails.getURI().toString());
                Linkify.addLinks(link, Linkify.WEB_URLS);
                link.setLinksClickable(true);

                favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //if state to fav and unfav

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (favorited) {
                            unFavorite(mangaID, user);
                        } else {
                            favorite(mangaID, mangaDetails.getTitle(), user);
                        }

                    }
                });

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
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");

        String link = "www.mangaworld.com/me/" + mangaID;

        String text = "<a href=\"" + link + "\">Check out " + mangaTitle + "</a>";

        String text2 = "Visit <a href=\"http://www.google.com\">google</a> for more info.";

        //intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out " + mangaTitle);
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(text + "<br>") + "\n\n" + text + "\n\n" + text2 + "\n\n" + Html.fromHtml(text2));

        //startActivity(Intent.createChooser(intent, "Share " + mangaTitle));

        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/html")
                .setHtmlText(text + "<br><br>" + link)
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

            Help.d("action: " + action + " | data: " + data.toString() + " | id: " + id);

            mangaID = id;

        } else {
            mangaID = getIntent().getStringExtra("manga_id");
        }

    }

    public void favorite(String mangaID, String title, FirebaseUser user) {
        FirebaseDatabaseUtil.saveLocal(MangaInfoActivity.this, mangaID, title, "");

        showAlert("Favorited", "Cool! I'm sure you'll enjoy it", android.R.drawable.star_big_on);

        if(user!=null) {

            FirebaseDatabaseUtil.writeNewUser();

            try {

                FirebaseDatabaseUtil.writeNewPost(mangaID, "", mangaDetails.getTitle());

            } catch (ArrayIndexOutOfBoundsException e) {

                FirebaseDatabaseUtil.writeNewPost(mangaID, "", mangaDetails.getTitle());

            }

        }
        favorited = true;
        favButton.setImageResource(android.R.drawable.star_big_on);
    }

    public void unFavorite(String mangaID, FirebaseUser user) {

        FirebaseDatabaseUtil.deleteLocal(MangaInfoActivity.this, mangaID, mangaDetails.getTitle());

        if(user!=null) {
            FirebaseDatabaseUtil.removeData(mangaID);
        }

        favorited = false;
        favButton.setImageResource(android.R.drawable.star_big_off);
        showAlert("UnFavorited", "I'm sorry you didn't enjoy it", android.R.drawable.star_big_off);

    }

    public void showAlert(String title, String text, int resid) {

        int color = getColor(R.color.colorPrimary);

        if(mangaDetails.getImage()!=null) {
            color = p.getDarkVibrantColor(getColor(R.color.md_black_1000));
        }

        String hex = "#"+Integer.toHexString(color).substring(2);

        color = Color.parseColor(hex);

        Help.v(color + " is the color and hex is " + hex);

        Alerter.create(this)
                .setTitle(title)
                .setText(text)
                .enableSwipeToDismiss()
                .setIcon(resid)
                .setBackgroundColorInt(color)
                .enableIconPulse(false)
                .showIcon(true)
                .show();
    }

    public class ChapterCompare implements Comparator<Chapter> {
        public int compare(Chapter e1, Chapter e2) {
            return e1.getNumber()-e2.getNumber();
        }
    }
}
