package manga.mangaapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.marvelapiclient.model.ComicDto;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import co.hkm.soltag.TagContainerLayout;
import co.hkm.soltag.TagView;
import manga.mangaapp.MangaSide.GenreTags;
import manga.mangaapp.UserInfo.Favorites;
import manga.mangaapp.mangaedenclient.Manga;
import manga.mangaapp.mangaedenclient.MangaDetails;
import manga.mangaapp.mangaedenclient.MangaEdenClient;
import manga.mangaapp.manymanga.sites.Site;
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
import pl.aprilapps.easyphotopicker.EasyImage;
import rebus.bottomdialog.BottomDialog;


public class MainFragment extends Fragment {

    Activity a;

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

    TextView siteLink;

    private FirebaseAuth mAuth;

    public MainFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MainFragment(Activity a) {
        // Required empty public constructor
        this.a = a;
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(Activity a) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        Help.e("Hello", "World");

        mAuth = FirebaseAuth.getInstance();

        currentLayout = Layouts.DETAILS;
        currentSource = GenreTags.Sources.fromString(SharedPreferencesManager.getInstance().getValue("manga_source", String.class, "MangaEden"));

        String picture = SharedPreferencesManager.getInstance().getValue("profile_image", String.class);

        profileDrawerItem = new ProfileDrawerItem().withName("You");

        if (picture == null) {
            profileDrawerItem.withIcon(MaterialDesignIconic.Icon.gmi_account);
        } else {
            profileDrawerItem.withIcon(Drawable.createFromPath(picture));
        }

        //setUpDrawer();

        mangaArrayList = new ArrayList<>();
        mangaSearch = new ArrayList<>();
        tagSearch = new ArrayList<>();
        mangaDetailsArrayList = new ArrayList<>();

        mangaSearchList = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.manga_list);

        siteLink = view.findViewById(R.id.site_link);

        fab = view.findViewById(R.id.switch_source);

        searchBars = view.findViewById(R.id.searchBar);

        searchBars.setNavButtonEnabled(true);
        searchBars.setSpeechMode(false);
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
                    mAdapter = new MangaAdapter(mangaSearch, a, client, currentLayout);
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
                    mAdapter = new Manga2Adapter(mangaSearchList, a, currentSite, currentLayout);
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
                Help.v("Button code: " + buttonCode);
                switch (buttonCode){
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        result.openDrawer();
                        Help.v("Button Nav");
                        break;
                    case MaterialSearchBar.BUTTON_SPEECH:
                        Help.v("Button Speech");
                        break;
                    default:
                        Help.v("Default");
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(a);
        mLayoutManager.setItemPrefetchEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        newSource(currentSource);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(chosen) {
                    if(comicDone) {
                        Collections.sort(comicsDtos, new ComicCompare());
                        //set the adapter for real time searching
                        mAdapter = new ComicAdapter(comicsDtos, a);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    if(mangaDone) {
                        Collections.sort(mangaArrayList, new MangaCompare());
                        //set the adapter for real time searching
                        mAdapter = new MangaAdapter(mangaArrayList, a, client);
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

        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            /*
            @Override
            public Drawable placeholder(Context ctx) {
                return super.placeholder(ctx);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                return super.placeholder(ctx, tag);
            }
            */
        });

    }

    public void getMangaEden() {
        new RetrieveInfo(new AsyncTasks() {
            @Override
            public void onPostExecute(Boolean success) {
                //Collections.sort(mangaArrayList, mangaSort(R.id.sort_date));

                //set the adapter for real time searching
                mAdapter = new MangaAdapter(mangaArrayList, a, client, currentLayout);
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
        final LovelyCustomDialog customDialog = new LovelyCustomDialog(a)
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

                //if (sourceOrGenre || true) {
                search.setVisibility(View.GONE);
                //}

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

                mAdapter = new MangaAdapter(mangaArrayList, a, client, currentLayout);

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
                    result.addItemAtPosition(new PrimaryDrawerItem().withIdentifier(2).withSelectable(false).withName("Genre Search").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                            //genreSearch();

                            return false;
                        }
                    }), 1);
                    getMangaEden();
                } else {
                    result.removeItem(2);
                    //List<Site> siteList = SiteHelper.getSites();
                    try {
                        Log.e("Line_" + new Throwable().getStackTrace()[0].getLineNumber(), currentSite.getMangaList().size()+" manga titles");
                        mangaList = new ArrayList<>();
                        mangaList.addAll(currentSite.getMangaList());

                        mAdapter = new Manga2Adapter(mangaList, a, currentSite, currentLayout);

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
                        mAdapter = new Manga2Adapter(mangaList, a, currentSite, currentLayout);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    siteLink.setText(currentSite.getUrl());
                    SharedPreferencesManager.getInstance().putValue("manga_source", currentSource.source);
                }
            }
        }).execute();
    }

    public void iosDialog(String source, final View.OnClickListener positive, final View.OnClickListener negative) {
        final iOSDialog dialog = new iOSDialog(a);
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
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
