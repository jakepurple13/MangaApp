package manga.mangaapp;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.cleveroad.fanlayoutmanager.FanLayoutManager;
import com.cleveroad.fanlayoutmanager.FanLayoutManagerSettings;
import com.forcelain.awesomelayoutmanager.AwesomeLayoutManager;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.greenfrvr.hashtagview.HashtagView;
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
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import net.alhazmy13.gota.Gota;
import net.alhazmy13.gota.GotaResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.net.ssl.HttpsURLConnection;

import co.hkm.soltag.LayouMode;
import co.hkm.soltag.TagContainerLayout;
import co.hkm.soltag.TagView;
import info.guardianproject.netcipher.NetCipher;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import manga.mangaapp.MangaSide.GenreTags;
import manga.mangaapp.mangaedenclient.Manga;
import manga.mangaapp.mangaedenclient.MangaDetails;
import manga.mangaapp.mangaedenclient.MangaEdenClient;
import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.SiteHelper;
import manga.mangaapp.manymanga.sites.extend.MangaEden;
import manga.mangaapp.manymanga.sites.implementations.english.KissManga;
import manga.mangaapp.manymanga.sites.implementations.english.LINEWebtoon;
import manga.mangaapp.manymanga.sites.implementations.english.MangaEdenEnglish;
import manga.mangaapp.manymanga.sites.implementations.english.MangaFox;
import manga.mangaapp.manymanga.sites.implementations.english.MangaHereEnglish;
import manga.mangaapp.manymanga.sites.implementations.english.MangaPanda;
import manga.mangaapp.manymanga.sites.implementations.english.MangaReader;
import manga.mangaapp.manymanga.sites.implementations.english.Mangajoy;
import manga.mangaapp.manymanga.sites.implementations.english.ReadMangaToday;
import manga.mangaapp.manymanga.sites.implementations.english.Tapastic;
import me.gujun.android.taggroup.TagGroup;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import rebus.bottomdialog.BottomDialog;
import rebus.bottomdialog.Item;

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

    ArrayList<Manga> tagSearch;
    ArrayList<MangaDetails> mangaDetailsArrayList;

    ArrayList<manga.mangaapp.manymanga.data.Manga> mangaSearchList;
    ArrayList<manga.mangaapp.manymanga.data.Manga> mangaList;
    Site currentSite;
    Layouts currentLayout;
    GenreTags.Sources currentSource;

    ProfileDrawerItem profileDrawerItem;
    AccountHeader headerResult;
    Drawer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Help.e("Hello", "World");

        currentLayout = Layouts.DETAILS;
        currentSource = GenreTags.Sources.MANGAEDEN;

        String picture = SharedPreferencesManager.getInstance().getValue("profile_image", String.class);

        profileDrawerItem = new ProfileDrawerItem().withName("You");

        if (picture == null) {
            profileDrawerItem.withIcon(MaterialDesignIconic.Icon.gmi_account);
        } else {
            profileDrawerItem.withIcon(Drawable.createFromPath(picture));
        }

        setUpDrawer();

        mangaArrayList = new ArrayList<>();
        mangaSearch = new ArrayList<>();
        tagSearch = new ArrayList<>();
        mangaDetailsArrayList = new ArrayList<>();

        mangaSearchList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.manga_list);

        fab = findViewById(R.id.switch_source);

        searchBars = findViewById(R.id.searchBar);

        searchBars.setHint("Search Here");
        searchBars.setPlaceHolder("Search Here");

        searchBars.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i3, int i1, int i2) {

                Help.v(currentSource.source);

                if(currentSource.equals(GenreTags.Sources.MANGAEDEN)) {

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
                    mAdapter = new MangaAdapter(mangaSearch, MainActivity.this, client, currentLayout);
                    mRecyclerView.setAdapter(mAdapter);

                    //this is for the suggestion list
                    List<String> suggestions = new ArrayList<>();
                    //As long as the search size isn't 0
                    if (mangaSearch.size() != 0) {
                        //Go through and show only 5 suggestions if applicable
                        for (int i = 0; i < 5 && i < mangaSearch.size(); i++) {
                            suggestions.add(mangaSearch.get(i).getTitle());
                        }
                        //From here, update suggest list
                        searchBars.updateLastSuggestions(suggestions);
                        //if the search text is "" then just hide the list
                        if (s.length() == 0) {
                            searchBars.hideSuggestionsList();
                        }
                        //otherwise just hide the list
                    } else {
                        searchBars.hideSuggestionsList();
                    }

                } else {
                    //All this is to search
                    mangaSearchList.clear();
                    searchKey = s.toString();
                    System.out.println(searchKey);
                    //We search both number and name
                    for (int i = 0; i < mangaList.size(); i++) {
                        if (mangaList.get(i).getTitle().toUpperCase().contains(searchKey.toUpperCase())) {
                            mangaSearchList.add(mangaList.get(i));
                        }
                    }

                    //set the adapter for real time searching
                    mAdapter = new Manga2Adapter(mangaSearchList, MainActivity.this, currentSite, currentLayout);
                    mRecyclerView.setAdapter(mAdapter);

                    //this is for the suggestion list
                    List<String> suggestions = new ArrayList<>();
                    //As long as the search size isn't 0
                    if (mangaSearchList.size() != 0) {
                        //Go through and show only 5 suggestions if applicable
                        for (int i = 0; i < 5 && i < mangaSearchList.size(); i++) {
                            suggestions.add(mangaSearchList.get(i).getTitle());
                        }
                        //From here, update suggest list
                        searchBars.updateLastSuggestions(suggestions);
                        //if the search text is "" then just hide the list
                        if (s.length() == 0) {
                            searchBars.hideSuggestionsList();
                        }
                        //otherwise just hide the list
                    } else {
                        searchBars.hideSuggestionsList();
                    }
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
        mLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getMangaEden();

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

                /*showChipDialog("Choose a Genre", "Pick as Many as you Want", GenreTags.getAllTags(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/

            }
        });

        fab.setVisibility(View.GONE);

    }

    public void getMangaEden() {
        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPostExecute(Boolean success) {
                //Collections.sort(mangaArrayList, mangaSort(R.id.sort_date));

                //set the adapter for real time searching
                mAdapter = new MangaAdapter(mangaArrayList, MainActivity.this, client, currentLayout);
                mRecyclerView.setAdapter(mAdapter);

                tagSearch = mangaArrayList;

                mangaDone = true;
            }

            @Override
            public boolean doInBackground() {
                try {

                    client = new MangaEdenClient();

                    // Get list of manga
                    List<Manga> mangaList = client.getMangaList();

                    mangaArrayList.addAll(mangaList);

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

    public void showChipDialog(int iconId, String title, String message, final String[] chipsToUse, final View.OnClickListener yesClick, final boolean sourceOrGenre, final TagView.OnTagClickListener listener) {
        final LovelyCustomDialog customDialog = new LovelyCustomDialog(MainActivity.this)
                .setView(R.layout.genre_choose_layout)
                .setTopColorRes(R.color.md_blue_A400)
                .setTitle(title)
                .setMessage(message)
                .setTitleGravity(Gravity.CENTER_HORIZONTAL)
                .setMessageGravity(Gravity.CENTER_HORIZONTAL)
                .setIcon(iconId);

        customDialog.configureView(new LovelyCustomDialog.ViewConfigurator() {
            @Override
            public void configureView(View v) {

                /*TagGroup tagGroup = v.findViewById(R.id.tag_group);

                tagGroup.setTags(chipsToUse);

                tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                    @Override
                    public void onTagClick(String tag) {
                        tagListener.onTagClick(tag);
                        customDialog.dismiss();
                    }
                });*/

                final TagContainerLayout mTagContainerLayout = (TagContainerLayout) v.findViewById(R.id.tag_group);

                mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
                    @Override
                    public void onTagClick(int position, String text) {
                        Help.w(text);
                        listener.onTagClick(position, text);
                        customDialog.dismiss();
                    }

                    @Override
                    public void onTagLongClick(int position, String text) {
                        Help.w(text);
                        listener.onTagLongClick(position, text);
                        customDialog.dismiss();
                    }
                });

                mTagContainerLayout.setTags(chipsToUse);

                Button search = v.findViewById(R.id.yes_button);

                if (sourceOrGenre || true) {
                    search.setVisibility(View.GONE);
                }

                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //do genre search here
                        customDialog.dismiss();
                    }
                });

            }
        });
        if (yesClick != null) {
            customDialog.setListener(R.id.yes_button, true, yesClick);
        }

        customDialog.show();
    }

    public void newSource(final GenreTags.Sources tag) {
        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPreExecute() {

                mangaArrayList.clear();

                mAdapter = new MangaAdapter(mangaArrayList, MainActivity.this, client, currentLayout);

                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public boolean doInBackground() {

                currentSite = new MangaFox();

                currentSource = tag;

                switch (tag) {
                    case MANGAEDEN:
                        currentSite = new MangaEdenEnglish();
                        break;
                    case TAPASTIC:
                        currentSite = new Tapastic();
                        break;
                    case READMANGATODAY:
                        currentSite = new ReadMangaToday();
                        break;
                    case MANGAREADER:
                        currentSite = new MangaReader();
                        break;
                    case MANGAPANDA:
                        currentSite = new MangaPanda();
                        break;
                    case MANGAJOY:
                        currentSite = new Mangajoy();
                        break;
                    case MANGAHERE:
                        currentSite = new MangaHereEnglish();
                        break;
                    case MANGAFOX:
                        currentSite = new MangaFox();
                        break;
                    case LINEWEBTOON:
                        currentSite = new LINEWebtoon();
                        break;
                    case KISSMANGA:
                        currentSite = new KissManga();
                        break;
                    default:
                        currentSite = new MangaReader();
                        break;
                }

                if (tag.equals(GenreTags.Sources.MANGAEDEN)) {
                    getMangaEden();
                } else {

                    //List<Site> siteList = SiteHelper.getSites();
                    try {
                        Log.e("Line_" + new Throwable().getStackTrace()[0].getLineNumber(), currentSite.getMangaList().size()+" manga titles");
                        mangaList = new ArrayList<>();
                        mangaList.addAll(currentSite.getMangaList());

                        mAdapter = new Manga2Adapter(mangaList, MainActivity.this, currentSite, currentLayout);

                        Log.e("Line_" + new Throwable().getStackTrace()[0].getLineNumber(), currentSite.getChapterList(mangaList.get(0)).get(0).getLink());
                        Log.e("Line_" + new Throwable().getStackTrace()[0].getLineNumber(), mangaList.toString());

                        chosen = false;

                        StoryWorld.setSite(currentSite);

                    } catch (Exception e) {
                        e.printStackTrace();
                        getMangaEden();
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void onPostExecute(Boolean success) {
                if (success) {
                    if (!tag.equals(GenreTags.Sources.MANGAEDEN)) {
                        Log.e("Line_" + new Throwable().getStackTrace()[0].getLineNumber(), tag.source);
                        mAdapter = new Manga2Adapter(mangaList, MainActivity.this, currentSite, currentLayout);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
            }
        }).execute();
    }

    ArrayList<Manga> alm = new ArrayList<>();

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
        PrimaryDrawerItem sourceChange = new PrimaryDrawerItem().withIdentifier(1).withSelectable(false).withName("Choose Source").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                //SharedPreferencesManager.getInstance().clear();

                String currentSource = currentSite == null ? "MangaEden" : currentSite.getName();

                showChipDialog(android.R.drawable.ic_menu_search, "Current Source is " + currentSource, "Pick a new Source", GenreTags.getAllSources(), null, true, new TagView.OnTagClickListener() {
                    @Override
                    public void onTagClick(int position, String text) {

                        Help.w(text);

                        Log.e("Line_" + new Throwable().getStackTrace()[0].getLineNumber(), text);

                        final String tags = text;

                        iosDialog(text, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newSource(GenreTags.Sources.fromString(tags));
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        //newSource(GenreTags.Sources.fromString(tag));
                    }

                    @Override
                    public void onTagLongClick(int position, String text) {
                        Help.w(text);
                    }
                });

                return false;
            }
        });

        PrimaryDrawerItem genreChange = new PrimaryDrawerItem().withIdentifier(2).withSelectable(false).withName("Genre Search").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                //SharedPreferencesManager.getInstance().clear();

                /*showChipDialog(android.R.drawable.ic_menu_search, "Choose a Genre", "Pick as Many as you Want", GenreTags.getAllTags(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }, false, new TagGroup.OnTagClickListener() {
                    @Override
                    public void onTagClick(final String tag) {

                        new RetrieveInfo(new AsyncTasks() {
                            @Override
                            public void onPreExecute() {
                                alm.clear();
                                Help.w("TAG", "asdf");
                                mAdapter = new MangaAdapter(alm, MainActivity.this, client, currentLayout);
                                mRecyclerView.setAdapter(mAdapter);
                            }

                            @Override
                            public boolean doInBackground() {

                                try {

                                    for (int i = 0; i < mangaArrayList.size(); i++) {

                                        MangaDetails mangaDetails = client.getMangaDetails(mangaArrayList.get(i).getId());
                                        Help.w(mangaDetails.getTitle(), Arrays.toString(mangaDetails.getCategories()));
                                        if (Arrays.toString(mangaDetails.getCategories()).contains(tag)) {
                                            alm.add(mangaArrayList.get(i));
                                            Handler uiHandler = new Handler(Looper.getMainLooper());
                                            uiHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ((MangaAdapter) mAdapter).notifyData(alm);
                                                }
                                            });

                                        }

                                        *//*for(String s : mangaDetails.getCategories()) {
                                            Help.w(mangaDetails.getTitle(), s);
                                            if(tag.equals(s)) {
                                                alm.add(mangaArrayList.get(i));
                                                break;
                                            }
                                        }*//*

                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                return false;
                            }

                            @Override
                            public void onPostExecute(Boolean success) {
                                //mAdapter = new MangaAdapter(alm, MainActivity.this, client, currentLayout);
                                //mRecyclerView.setAdapter(mAdapter);
                            }
                        }).execute();

                    }
                });*/

                genreSearch();

                return false;
            }
        });

        PrimaryDrawerItem sortBy = new PrimaryDrawerItem().withIdentifier(9).withSelectable(false).withName("Sort By").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                if (mAdapter instanceof MangaAdapter) {

                    final BottomDialog dialog = new BottomDialog(MainActivity.this);
                    dialog.title("Sort By:");
                    dialog.canceledOnTouchOutside(true);
                    dialog.cancelable(true);
                    dialog.inflateMenu(R.menu.sort_by_menu);
                    dialog.setOnItemSelectedListener(new BottomDialog.OnItemSelectedListener() {
                        @Override
                        public boolean onItemSelected(int id) {
                            switch (id) {
                                case R.id.sort_title:
                                    sortManga(id);
                                    dialog.dismiss();
                                    return true;
                                case R.id.sort_date:
                                    sortManga(id);
                                    dialog.dismiss();
                                    return true;
                                case R.id.sort_cancel:
                                    dialog.dismiss();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    dialog.show();

                } else if (mAdapter != null) {

                    Collections.sort(mangaList, new Comparator<manga.mangaapp.manymanga.data.Manga>() {
                        @Override
                        public int compare(manga.mangaapp.manymanga.data.Manga manga, manga.mangaapp.manymanga.data.Manga t1) {
                            return manga.getTitle().compareTo(t1.getTitle());
                        }
                    });

                    mAdapter = new Manga2Adapter(mangaList, MainActivity.this, currentSite, currentLayout);
                    mRecyclerView.setAdapter(mAdapter);

                }

                return false;
            }
        });

        ToggleDrawerItem layoutChange = new ToggleDrawerItem().withIdentifier(3).withSelectable(false).withName("Layout Change").withOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    mLayoutManager = new LinearLayoutManager(MainActivity.this);
                    mLayoutManager.setItemPrefetchEnabled(true);
                } else {
                    //mLayoutManager = new GridLayoutManager(MainActivity.this, 3);
                    mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                    //mLayoutManager = new FanLayoutManager(MainActivity.this);

                }

                mLayoutManager.setItemPrefetchEnabled(true);
                mRecyclerView.setLayoutManager(mLayoutManager);

            }
        });

        PrimaryDrawerItem layoutChangeChoice = new PrimaryDrawerItem().withIdentifier(8).withSelectable(false).withName("Choose Layout").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                String[] cat = new String[]{
                        Layouts.STAGGERED_THUMBNAILS.source,
                        Layouts.THUMBNAILS.source,
                        Layouts.DETAILS.source
                };

                showChipDialog(android.R.drawable.ic_menu_search, "Choose a Layout", "Choose One", cat,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }, true,
                        new TagView.OnTagClickListener() {
                            @Override
                            public void onTagClick(int position, String text) {

                                Help.w(text);

                                currentLayout = Layouts.fromString(text);

                                switch (Layouts.fromString(text)) {

                                    case STAGGERED_THUMBNAILS:
                                        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                                        break;

                                    case THUMBNAILS:
                                        mLayoutManager = new GridLayoutManager(MainActivity.this, 3);
                                        break;

                                    case DETAILS:
                                        mLayoutManager = new LinearLayoutManager(MainActivity.this);
                                        break;

                                    default:
                                        mLayoutManager = new LinearLayoutManager(MainActivity.this);
                                        break;

                                }

                                mLayoutManager.setItemPrefetchEnabled(true);
                                mRecyclerView.setLayoutManager(mLayoutManager);

                                mAdapter = new MangaAdapter(mangaArrayList, MainActivity.this, client, currentLayout);
                                mRecyclerView.setAdapter(mAdapter);
                            }

                            @Override
                            public void onTagLongClick(int position, String text) {
                                Help.w(text);
                            }
                        });

                return false;
            }
        });


        SecondaryDrawerItem settings = new SecondaryDrawerItem().withIdentifier(4).withSelectable(false).withName("Settings").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
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
                        sourceChange,
                        genreChange,
                        new DividerDrawerItem(),
                        //layoutChange,
                        layoutChangeChoice,
                        sortBy,
                        new DividerDrawerItem(),
                        settings
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

    TagContainerLayout mTagContainerLayout;

    public void genreSearch() {
        final LovelyCustomDialog customDialog = new LovelyCustomDialog(MainActivity.this)
                .setView(R.layout.genre_choose_layout)
                .setTopColorRes(R.color.md_blue_A400)
                .setTitle("Pick some Genre's")
                .setMessage("Pick")
                .setTitleGravity(Gravity.CENTER_HORIZONTAL)
                .setMessageGravity(Gravity.CENTER_HORIZONTAL)
                .setIcon(android.R.drawable.ic_menu_search);

        final ArrayList<String> tagsChosen = new ArrayList<>();

        customDialog.configureView(new LovelyCustomDialog.ViewConfigurator() {
            @Override
            public void configureView(View v) {

                /*final TagGroup tagGroup = v.findViewById(R.id.tag_group);

                tagGroup.setTags(GenreTags.getAllTags());

                tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                    @Override
                    public void onTagClick(String tag) {

                        if(tagsChosen.contains(tag)) {
                            tagsChosen.remove(tag);
                        } else {
                            tagsChosen.add(tag);
                        }

                    }
                });*/

                mTagContainerLayout = (TagContainerLayout) v.findViewById(R.id.tag_group);

                mTagContainerLayout.setMode(LayouMode.MULTIPLE_CHOICE);

                mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
                    @Override
                    public void onTagClick(int position, String text) {
                        Help.w(text);
                        if(tagsChosen.contains(text)) {
                            tagsChosen.remove(text);
                        } else {
                            tagsChosen.add(text);
                        }

                        //Help.d(Arrays.toString(tagsChosen.toArray()));

                    }

                    @Override
                    public void onTagLongClick(int position, String text) {
                        Help.w(text);
                    }
                });

                mTagContainerLayout.setTags(GenreTags.getAllTags());

            }
        });

        customDialog.setListener(R.id.yes_button, true, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Help.d(Arrays.toString(mTagContainerLayout.getSelectedItems().toArray()));

                mRecyclerView.setHasTransientState(true);

                new RetrieveInfo(new AsyncTasks() {
                    @Override
                    public void onPreExecute() {

                        alm.clear();
                        //Help.w("TAG", mTagContainerLayout.getSelectedItems().toString());
                        mAdapter = new MangaAdapter(alm, MainActivity.this, client, currentLayout);
                        mRecyclerView.setAdapter(mAdapter);

                    }

                    @Override
                    public boolean doInBackground() {

                        try {

                            for (int i = 0; i < mangaArrayList.size(); i++) {

                                MangaDetails mangaDetails = client.getMangaDetails(mangaArrayList.get(i).getId());
                                //Help.w(mangaDetails.getTitle(), Arrays.toString(mangaDetails.getCategories()));
                                boolean add = true;
                                for(String s : mTagContainerLayout.getSelectedItems()) {

                                    if(!Arrays.toString(mangaDetails.getCategories()).contains(s)) {

                                        /*Handler uiHandler = new Handler(Looper.getMainLooper());
                                        uiHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                searchBars.setPlaceHolder("Search here through " + alm.size() + " items");
                                            }
                                        });*/

                                        add = false;
                                    }
                                }

                                if(add) {
                                    Help.w(mangaDetails.getTitle(), Arrays.toString(mangaDetails.getCategories()));
                                    alm.add(mangaArrayList.get(i));
                                    Handler handler = MainActivity.this.getWindow().getDecorView().getHandler();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((MangaAdapter) mAdapter).notifyData(alm);
                                        }
                                    });

                                }

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        return false;
                    }

                    @Override
                    public void onPostExecute(Boolean success) {
                        //mAdapter = new MangaAdapter(alm, MainActivity.this, client, currentLayout);
                        //mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setHasTransientState(false);
                    }
                }).execute();
            }
        });

        customDialog.show();
    }

    public void sortManga(int id) {
        Comparator<Manga> sorter = mangaSort(id);
        Collections.sort(mangaArrayList, sorter);

        mAdapter = new MangaAdapter(mangaArrayList, MainActivity.this, client, currentLayout);
        mRecyclerView.setAdapter(mAdapter);
    }

    public Comparator<Manga> mangaSort(int id) {

        Comparator<Manga> sorted;

        switch (id) {

            case R.id.sort_title:
                sorted = new Comparator<Manga>() {
                    @Override
                    public int compare(Manga manga, Manga t1) {
                        return manga.getTitle().compareTo(t1.getTitle());
                    }
                };
                break;

            case R.id.sort_date:
                sorted = new Comparator<Manga>() {
                    @Override
                    public int compare(Manga manga, Manga t1) {

                        //Log.e("Line_" + new Throwable().getStackTrace()[0].getLineNumber(), manga.toString());

                        Date d1 = new Date(manga.getLastChapterDate());
                        Date d2 = new Date(t1.getLastChapterDate());

                        return d1.compareTo(d2);
                    }
                };
                break;

            default:

                sorted = new Comparator<Manga>() {
                    @Override
                    public int compare(Manga manga, Manga t1) {
                        return manga.getTitle().compareTo(t1.getTitle());
                    }
                };

                break;

        }

        return sorted;
    }

    public void iosDialog(String source, final View.OnClickListener positive, final View.OnClickListener negative) {
        final iOSDialog dialog = new iOSDialog(MainActivity.this);
        dialog.setTitle("Change source to " + source);
        dialog.setSubtitle("Are you sure you want to change source?");
        dialog.setNegativeLabel("Never Mind");
        dialog.setPositiveLabel("Yes Please");
        //dialog.setBoldPositiveLabel(true);
        dialog.setNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                negative.onClick(view);
                dialog.dismiss();
            }
        });
        dialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positive.onClick(view);
                dialog.dismiss();
            }
        });
        dialog.show();
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
            if (requestId == 13) {
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
