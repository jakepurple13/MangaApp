package manga.mangaapp.UserInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import manga.mangaapp.AsyncTasks;
import manga.mangaapp.Help;
import manga.mangaapp.Layouts;
import manga.mangaapp.MainActivity;
import manga.mangaapp.MangaAdapter;
import manga.mangaapp.R;
import manga.mangaapp.RetrieveInfo;
import manga.mangaapp.mangaedenclient.Manga;
import manga.mangaapp.mangaedenclient.MangaDetails;
import manga.mangaapp.mangaedenclient.MangaEdenClient;

public class Favorites extends Fragment {

    Map<String, UserInfo> map;

    FirebaseUser user;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    MangaEdenClient client;

    ArrayList<Manga> mangaList;

    Activity a;

    public Favorites() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public Favorites(Activity a) {
        // Required empty public constructor
        this.a = a;
    }


    // TODO: Rename and change types and number of parameters
    public static Favorites newInstance(Activity activity) {
        Favorites fragment = new Favorites(activity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_favorites);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = getView().findViewById(R.id.favorite_list);
        mLayoutManager = new LinearLayoutManager(a);
        mLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mangaList = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null) {

            getFavorites();

        } else {
            //Local
            Help.v("Hello there");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_favorites, container, false);
    }


    public void getFavorites() {

        map = new HashMap<>();

        FirebaseDatabaseUtil.getListOfData(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserInfo post = dataSnapshot.getValue(UserInfo.class);

                Help.i(post.toString());

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Help.i(postSnapshot.toString());

                    UserInfo post1 = postSnapshot.getValue(UserInfo.class);

                    Help.d(post1.toString());

                    String title = "";
                    String uid = "";
                    String mangaid = "";
                    String chapterid = "";

                    for (DataSnapshot postSnapshot1: postSnapshot.getChildren()) {

                        Help.i(postSnapshot1.toString());

                        //UserInfo post2 = postSnapshot1.getValue(UserInfo.class);

                        //Help.v(post2.toString());

                        switch (postSnapshot1.getKey()) {
                            case "mangatitle":
                                title = postSnapshot1.getValue(String.class);
                                break;
                            case "mangaid":
                                mangaid = postSnapshot1.getValue(String.class);
                                break;
                            case "chapterid":
                                chapterid = postSnapshot1.getValue(String.class);
                                break;
                            case "uid":
                                uid = postSnapshot1.getValue(String.class);
                                break;
                            default:
                                break;

                        }

                    }

                    UserInfo userInfo = new UserInfo(uid, mangaid, chapterid, title);

                    map.put(postSnapshot.getKey(), userInfo);
                }

                String list = "";

                for(UserInfo s : map.values()) {
                    list+=s.toString()+"\n\n";
                }

                for(String s : map.keySet()) {
                    list+=s+"\n\n";
                }

                getMangaEden();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Help.w(map.values().toString());
    }

    public void getMangaEden() {
        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPostExecute(Boolean success) {
                //Collections.sort(mangaArrayList, mangaSort(R.id.sort_date));

                //set the adapter for real time searching
                mAdapter = new MangaAdapter(mangaList, a, client, Layouts.DETAILS);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public boolean doInBackground() {
                try {

                    client = new MangaEdenClient();

                    for(String s : map.keySet()) {

                        // Get list of manga
                        MangaDetails details = client.getMangaDetails(s);

                        Manga m = new Manga(details.getImage(), details.getTitle(), s, details.getAlias(), details.getStatus(), details.getCreated(), details.getHits());

                        mangaList.add(m);

                        //mangaList.addAll(mangaLists);

                        //Collections.sort(mangaArrayList, mangaSort(R.id.sort_title));

                        /*for(int i=0;i<mangaArrayList.size();i++) {
                            Log.w("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), mangaArrayList.get(i).getTitle());
                            mangaDetailsArrayList.add(client.getMangaDetails(mangaArrayList.get(i).getId()));
                        }*/

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

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }

            @Override
            public void onPreExecute() {

            }
        }).execute();
    }

}
