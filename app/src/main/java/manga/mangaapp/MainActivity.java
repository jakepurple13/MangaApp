package manga.mangaapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.karumi.marvelapiclient.ComicApiClient;
import com.karumi.marvelapiclient.MarvelApiConfig;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.model.ComicDto;
import com.karumi.marvelapiclient.model.ComicsDto;
import com.karumi.marvelapiclient.model.ComicsQuery;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import net.alhazmy13.gota.Gota;
import net.alhazmy13.gota.GotaResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import manga.mangaapp.mangaedenclient.Manga;
import manga.mangaapp.mangaedenclient.MangaEdenClient;
import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.SiteHelper;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity implements Gota.OnRequestPermissionsBack {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Manga> mangaArrayList;
    List<ComicDto> comicsDtos;
    MangaEdenClient client;
    FloatingActionButton fab;
    boolean chosen = false;

    boolean mangaDone = false;
    boolean comicDone = false;

    MaterialSearchBar searchBars;

    ArrayList<Manga> mangaSearch;
    String searchKey;

    ArrayList<manga.mangaapp.manymanga.data.Manga> mangaList;

    ProfileDrawerItem profileDrawerItem;
    AccountHeader headerResult;
    Drawer result;

    RecyclerView.Adapter manga2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String picture = SharedPreferencesManager.getInstance().getValue("profile_image", String.class);

        profileDrawerItem = new ProfileDrawerItem().withName("You");

        if(picture==null) {
            profileDrawerItem.withIcon(MaterialDesignIconic.Icon.gmi_account);
        } else {
            profileDrawerItem.withIcon(Drawable.createFromPath(picture));
        }

        setUpDrawer();

        mangaArrayList = new ArrayList<>();
        mangaSearch = new ArrayList<>();

        mRecyclerView = findViewById(R.id.manga_list);

        fab = findViewById(R.id.switch_source);

        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public boolean doInBackground() {
                List<Site> siteList = SiteHelper.getSites();
                try {
                    Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), siteList.get(0).getMangaList().toString());
                    mangaList = new ArrayList<>();
                    for(int i=0;i<siteList.size();i++) {
                        mangaList.addAll(siteList.get(i).getMangaList());
                    }

                    manga2Adapter = new Manga2Adapter(mangaList, MainActivity.this, siteList.get(0));

                    Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), siteList.get(0).getChapterList(mangaList.get(0)).get(0).getLink());
                    Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), mangaList.toString());

                    chosen = false;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public void onPostExecute(Boolean success) {
                //manga2Adapter = new Manga2Adapter(mangaList, MainActivity.this);
                //mRecyclerView.setAdapter(manga2Adapter);
            }
        });//.execute();

        searchBars = findViewById(R.id.searchBar);

        searchBars.setHint("Search Here");
        searchBars.setPlaceHolder("Search Here");

        searchBars.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i3, int i1, int i2) {
                //All this is to search
                mangaSearch.clear();
                searchKey = s.toString();
                System.out.println(searchKey);
                //We search both number and name
                for (int i = 0; i < mangaArrayList.size(); i++) {
                    if (mangaArrayList.get(i).getTitle().toUpperCase().contains(searchKey.toUpperCase())) {
                        mangaSearch.add(mangaArrayList.get(i));
                    }

                }
                //set the adapter for real time searching
                mAdapter = new MangaAdapter(mangaSearch, MainActivity.this, client);
                mRecyclerView.setAdapter(mAdapter);

                //this is for the suggestion list
                List<String> suggestions = new ArrayList<>();
                //As long as the search size isn't 0
                if(mangaSearch.size()!=0) {
                    //Go through and show only 5 suggestions if applicable
                    for (int i = 0; i < 5 && i<mangaSearch.size(); i++) {
                        suggestions.add(mangaSearch.get(i).getTitle());
                    }
                    //From here, update suggest list
                    searchBars.updateLastSuggestions(suggestions);
                    //if the search text is "" then just hide the list
                    if(s.length()==0) {
                        searchBars.hideSuggestionsList();
                    }
                    //otherwise just hide the list
                } else {
                    searchBars.hideSuggestionsList();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //on the action of searching, just hide the suggestion list.
        searchBars.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                searchBars.hideSuggestionsList();
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                searchBars.hideSuggestionsList();
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                searchBars.hideSuggestionsList();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //new RetrieveManga(this).execute();
        //new RetrieveComics(this).execute();
        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPostExecute(Boolean success) {
                Collections.sort(mangaArrayList, new MangaCompare());

                //set the adapter for real time searching
                mAdapter = new MangaAdapter(mangaArrayList, MainActivity.this, client);
                mRecyclerView.setAdapter(mAdapter);

                mangaDone = true;
            }

            @Override
            public boolean doInBackground() {
                try {

                    client = new MangaEdenClient();

                    // Get list of manga
                    List<Manga> mangaList = client.getMangaList();

                    mangaArrayList.addAll(mangaList);

                    Collections.sort(mangaArrayList, new MangaCompare());

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
                    return false;
                }

                return true;
            }

            @Override
            public void onPreExecute() {

            }
        }).execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(chosen) {
                    if(comicDone) {
                        Collections.sort(comicsDtos, new ComicCompare());
                        //set the adapter for real time searching
                        mAdapter = new ComicAdapter(comicsDtos, MainActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    if(mangaDone) {
                        Collections.sort(mangaArrayList, new MangaCompare());
                        //set the adapter for real time searching
                        mAdapter = new MangaAdapter(mangaArrayList, MainActivity.this, client);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }*/
                chosen = !chosen;
            }
        });


    }

    public void setUpDrawer() {

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withOnlyMainProfileImageVisible(true)
                .withHeaderBackground(android.R.drawable.dark_header)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        EasyImage.openChooserWithGallery(MainActivity.this, "Choose a new Profile Picture", 1);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withAlternativeProfileHeaderSwitching(false)
                .withDividerBelowHeader(true)
                .addProfiles(profileDrawerItem)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();



        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Choose Source").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                //SharedPreferencesManager.getInstance().clear();
                return false;
            }
        });
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Settings").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                return false;
            }
        });

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withCloseOnClick(true)
                .withAccountHeader(headerResult, true)
                .withSelectedItemByPosition(-1)
                .withDisplayBelowStatusBar(true)
                .withItemAnimator(new DefaultItemAnimator())
                .withKeepStickyItemsVisible(true)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2
                        /*,
                        new ExpandableDrawerItem().withName("Collapsable").withIcon(GoogleMaterial.Icon.gmd_collections_bookmark).withIdentifier(19).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_add_alert).withIdentifier(2002),
                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_3d_rotation).withIdentifier(2003)
                        )*/
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        result.closeDrawer();
                        return true;
                    }
                })
                .build();

    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the images
                //onPhotosReturned(imagesFiles);
                SharedPreferencesManager.getInstance().putValue("profile_image", imageFile.getPath());
                profileDrawerItem.withIcon(Uri.fromFile(imageFile));
                headerResult.updateProfile(profileDrawerItem);
            }
        });
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

    public void saveItems() {
        //SharedPreferencesManager.getInstance().putValue("profile_image", profileDrawerItem.);
        SharedPreferencesManager.getInstance().printAllKeyValues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveItems();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveItems();
    }

    public class MangaCompare implements Comparator<Manga> {
        public int compare(Manga e1, Manga e2) {
            //return (int) (e1.getLastChapterDate()-e2.getLastChapterDate());
            if (e1.getLastChapterDate() < e2.getLastChapterDate())
                return -1;
            if (e1.getLastChapterDate() > e2.getLastChapterDate())
                return 1;
            return 0;
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

            String publicKey = "8f048136de65d2ca3a0660fa9d074ff0";
            String privateKey = "ac6e44d7284de6fe16d1a87f703c484f8f25fc08";
            try {

                MarvelApiConfig marvelApiConfig = new MarvelApiConfig.Builder(publicKey, privateKey).debug().build();

                ComicApiClient comicApiClient = new ComicApiClient(marvelApiConfig);
                ComicsQuery query = ComicsQuery.Builder.create().withOffset(0).build();
                MarvelResponse<ComicsDto> all1 = comicApiClient.getAll(query);

                comicsDtos = all1.getResponse().getComics();

            } catch (MarvelApiException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);
            // TODO: check this.exception
            // TODO: do something with the feed

            Collections.sort(comicsDtos, new ComicCompare());
            //set the adapter for real time searching
            mAdapter = new ComicAdapter(comicsDtos, activity);
            mRecyclerView.setAdapter(mAdapter);

            comicDone = true;

        }
    }

    public class ComicCompare implements Comparator<ComicDto> {
        public int compare(ComicDto e1, ComicDto e2) {
            return e1.getModified().compareTo(e2.getModified());
        }
    }
}
