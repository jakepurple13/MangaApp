package manga.mangaapp;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.marvelapiclient.CharacterApiClient;
import com.karumi.marvelapiclient.ComicApiClient;
import com.karumi.marvelapiclient.MarvelApiConfig;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.model.CharactersDto;
import com.karumi.marvelapiclient.model.CharactersQuery;
import com.karumi.marvelapiclient.model.ComicsDto;
import com.karumi.marvelapiclient.model.ComicsQuery;
import com.karumi.marvelapiclient.model.MarvelImage;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.squareup.picasso.Picasso;

import net.alhazmy13.gota.Gota;
import net.alhazmy13.gota.GotaResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import manga.mangaapp.mangaedenclient.Chapter;
import manga.mangaapp.mangaedenclient.ChapterDetails;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.Manga;
import manga.mangaapp.mangaedenclient.MangaDetails;
import manga.mangaapp.mangaedenclient.MangaEdenClient;
import me.anshulagarwal.expandablemenuoption.ExpandableMenuView;

public class MainActivity extends AppCompatActivity implements Gota.OnRequestPermissionsBack {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Manga> mangaArrayList;
    MangaEdenClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mangaArrayList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.manga_list);

        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new RetrieveManga(this).execute();

    }

    @Override
    public void onRequestBack(int requestId, @NonNull GotaResponse gotaResponse) {
        if (gotaResponse.hasDeniedPermission()) {
            new Gota.Builder(this)
                    .withPermissions(gotaResponse.deniedPermissions())
                    .requestId(13)
                    .setListener(this)
                    .check();
        } else {
            if(requestId==13) {
                Toast.makeText(this, "adksjlfhakljsdfh", Toast.LENGTH_LONG).show();
            }
            //new RetrieveManga(this).execute();
        }
    }

    class RetrieveManga extends AsyncTask<String, Void, Boolean> {


        private MainActivity activity;

        public RetrieveManga(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            //activity.getInfo();
            try {

                client = new MangaEdenClient();

                // Get list of manga
                List<Manga> mangaList = client.getMangaList();

                mangaArrayList.addAll(mangaList);

                Collections.sort(mangaArrayList, new InfoCompare());
                /*
                Manga manga = mangaList.get(0);

                // Get manga details
                MangaDetails mangaDetails = client.getMangaDetails(manga.getId());
                Chapter[] chapters = mangaDetails.getChapters();

                // Get chapter details
                ChapterDetails chapterDetails = client.getChapterDetails(chapters[0].getId());
                ChapterPage[] pages = chapterDetails.getPages();

                // Get chapter page image URLs
                String imageUrl = pages[0].getImage();
                Log.w("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), imageUrl);
                Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), mangaList.toString());
                */
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            // TODO: check this.exception
            // TODO: do something with the feed

            Collections.sort(mangaArrayList, new InfoCompare());

            //set the adapter for real time searching
            mAdapter = new MangaAdapter(mangaArrayList, activity, client);
            mRecyclerView.setAdapter(mAdapter);

        }
    }

    public class InfoCompare implements Comparator<Manga> {
        public int compare(Manga e1, Manga e2) {
            return e1.getLastChapterDate()<=e2.getLastChapterDate() ? 1 : 0;
        }
    }

    class RetrieveComics extends AsyncTask<String, Void, Boolean> {


        private MainActivity activity;

        public RetrieveComics(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... urls) {



            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            // TODO: check this.exception
            // TODO: do something with the feed

            //set the adapter for real time searching
            mAdapter = new MangaAdapter(mangaArrayList, activity, client);
            mRecyclerView.setAdapter(mAdapter);

        }
    }
}
