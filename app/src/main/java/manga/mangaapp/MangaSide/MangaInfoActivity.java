package manga.mangaapp.MangaSide;

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

import manga.mangaapp.AsyncTasks;
import manga.mangaapp.R;
import manga.mangaapp.RetrieveInfo;
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

    ChapterListAdapter chapterListAdapter;

    MangaEdenClient client;

    MangaDetails mangaDetails;

    List<Chapter> chapterList;
    Chapter[] chapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_info);

        imageView = findViewById(R.id.manga_cover);

        title = findViewById(R.id.manga_title);
        description = findViewById(R.id.manga_description);

        mRecyclerView = findViewById(R.id.chapter_list);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL ));

        final String mangaID = getIntent().getStringExtra("manga_id");

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

                    //ChapterDetails chapterDetails = client.getChapterDetails(chapters[0].getId());
                    //chapterPages = chapterDetails.getPages();

                    Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), mangaDetails.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                final URI imageUrl = MangaEden.manga2ImageURI(mangaDetails.getImage());
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(MangaInfoActivity.this)
                                .load(String.valueOf(imageUrl))
                                .placeholder(android.R.mipmap.sym_def_app_icon)
                                .into(imageView);
                    }
                });

                chapterList = Arrays.asList(mangaDetails.getChapters());

                Collections.sort(chapterList, new ChapterCompare());

                //Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), chapterList.toString());
                Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), chapters.toString());
                Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), Arrays.deepToString(chapters));

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

                chapterListAdapter = new ChapterListAdapter(chapters, MangaInfoActivity.this);
                mRecyclerView.setAdapter(chapterListAdapter);
                String titleText = mangaDetails.getTitle() + "\nTags: ";

                String[] cat =  mangaDetails.getCategories();

                for(String s : cat) {
                    titleText+=s+"\t";
                }

                //Date date = new Date();
                //date.setTime(mangaDetails.getCreated());

                //titleText+="\nCreated:"+date;

                title.setText(titleText);
                description.setText(mangaDetails.getDescription());
            }
        }).execute();



    }

    public class ChapterCompare implements Comparator<Chapter> {
        public int compare(Chapter e1, Chapter e2) {
            return e1.getNumber()-e2.getNumber();
        }
    }
}
