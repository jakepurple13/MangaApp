package manga.mangaapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import co.hkm.soltag.LayouMode;
import co.hkm.soltag.TagContainerLayout;
import co.hkm.soltag.TagView;
import manga.mangaapp.MangaSide.GenreTags;
import manga.mangaapp.UserInfo.Favorites;
import manga.mangaapp.mangaedenclient.Manga;
import manga.mangaapp.mangaedenclient.MangaDetails;
import pl.aprilapps.easyphotopicker.EasyImage;
import rebus.bottomdialog.BottomDialog;

public class MainHub extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hub);





    }
/*

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null) {
            updateUI(currentUser);
        }

        */
/*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }*//*


    }

    public void updateUI(FirebaseUser user) {
        profileDrawerItem.withIcon(user.getPhotoUrl());
        profileDrawerItem.withEmail(user.getEmail());
        profileDrawerItem.withName(user.getDisplayName());
        headerResult.updateProfile(profileDrawerItem);
    }

    public void signIn(String email, String password) {

        final LovelyProgressDialog lovelyProgressDialog = new LovelyProgressDialog(ManHub.this);
        lovelyProgressDialog.setMessage("Please Wait");
        lovelyProgressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Help.d("signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Help.w("signInWithEmail:failure", task.getException().toString());
                            Toast.makeText(ManHub.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        lovelyProgressDialog.dismiss();

                        // ...
                    }
                });
    }

    public void signUp(String email, String password) {

        final LovelyProgressDialog lovelyProgressDialog = new LovelyProgressDialog(ManHub.this);
        lovelyProgressDialog.setMessage("Please Wait");
        lovelyProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Help.d("createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            user.sendEmailVerification();

                        } else {
                            // If sign in fails, display a message to the user.
                            Help.w("createUserWithEmail:failure", task.getException().toString());
                            Toast.makeText(ManHub.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        lovelyProgressDialog.dismiss();

                        // ...
                    }
                });
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

                        if(mAuth.getCurrentUser()==null) {

                            final LovelyCustomDialog customDialog = new LovelyCustomDialog(ManHub.this)
                                    .setView(R.layout.account_choice)
                                    .setTopColorRes(R.color.md_blue_A400)
                                    .setTitle("Account")
                                    .setMessage("Sign in or Sign Up")
                                    .setTitleGravity(Gravity.CENTER_HORIZONTAL)
                                    .setMessageGravity(Gravity.CENTER_HORIZONTAL)
                                    .setIcon(R.drawable.ic_menu_black_24dp);

                            customDialog.configureView(new LovelyCustomDialog.ViewConfigurator() {
                                @Override
                                public void configureView(View v) {

                                    Button signInButton = v.findViewById(R.id.signin);

                                    Button signUpButton = v.findViewById(R.id.signup);

                                    signInButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            final LovelyCustomDialog signInDialog = new LovelyCustomDialog(ManHub.this)
                                                    .setView(R.layout.sign_in_or_up)
                                                    .setTopColorRes(R.color.md_blue_A400)
                                                    .setTitle("Sign In")
                                                    .setMessage("Sign in")
                                                    .setTitleGravity(Gravity.CENTER_HORIZONTAL)
                                                    .setMessageGravity(Gravity.CENTER_HORIZONTAL)
                                                    .setIcon(R.drawable.ic_menu_black_24dp);

                                            signInDialog.configureView(new LovelyCustomDialog.ViewConfigurator() {
                                                @Override
                                                public void configureView(View v) {

                                                    final EditText email = v.findViewById(R.id.email);
                                                    final EditText password = v.findViewById(R.id.password);
                                                    Button submit = v.findViewById(R.id.submit);

                                                    submit.setText("Sign In");

                                                    submit.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            signInDialog.dismiss();
                                                            signIn(email.getText().toString(), password.getText().toString());
                                                        }
                                                    });

                                                }
                                            });

                                            signInDialog.show();

                                        }
                                    });

                                    signUpButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            final LovelyCustomDialog signUpDialog = new LovelyCustomDialog(ManHub.this)
                                                    .setView(R.layout.sign_in_or_up)
                                                    .setTopColorRes(R.color.md_blue_A400)
                                                    .setTitle("Sign Up")
                                                    .setMessage("Sign Up")
                                                    .setTitleGravity(Gravity.CENTER_HORIZONTAL)
                                                    .setMessageGravity(Gravity.CENTER_HORIZONTAL)
                                                    .setIcon(R.drawable.ic_menu_black_24dp);

                                            signUpDialog.configureView(new LovelyCustomDialog.ViewConfigurator() {
                                                @Override
                                                public void configureView(View v) {

                                                    final EditText email = v.findViewById(R.id.email);
                                                    final EditText password = v.findViewById(R.id.password);
                                                    Button submit = v.findViewById(R.id.submit);

                                                    submit.setText("Sign Up");

                                                    submit.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            signUpDialog.dismiss();
                                                            signUp(email.getText().toString(), password.getText().toString());
                                                        }
                                                    });

                                                    signUpDialog.show();

                                                }
                                            });
                                        }
                                    });

                                }
                            });

                            customDialog.show();

                        } else {
                            EasyImage.openChooserWithGallery(ManHub.this, "Choose a new Profile Picture", 1);
                        }

                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        EasyImage.openChooserWithGallery(ManHub.this, "Choose a new Profile Picture", 1);
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

                genreSearch();

                return false;
            }
        });

        PrimaryDrawerItem gotoFavorites = new PrimaryDrawerItem().withIdentifier(897).withSelectable(false).withName("Favorites").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                //Intent i = new Intent(ManHub.this, Favorites.class);
                //startActivity(i);

                addFragment(Favorites.newInstance(ManHub.this));

                */
/*<LinearLayout
                android:id="@+id/container_frags"
                android:layout_width="match_parent"
                android:layout_height="100dp"
                android:layout_above="@id/site_link"
                android:orientation="horizontal" />*//*


                return false;
            }
        });

        PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(898).withSelectable(false).withName("Home").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                //Intent i = new Intent(ManHub.this, Favorites.class);
                //startActivity(i);

                replaceFragment(null);

                return false;
            }
        });

        PrimaryDrawerItem logout = new PrimaryDrawerItem().withIdentifier(89).withSelectable(false).withName("Logout").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                mAuth.signOut();

                return false;
            }
        });

        PrimaryDrawerItem sortBy = new PrimaryDrawerItem().withIdentifier(9).withSelectable(false).withName("Sort By").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                if (mAdapter instanceof MangaAdapter) {

                    final BottomDialog dialog = new BottomDialog(ManHub.this);
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

                    mAdapter = new Manga2Adapter(mangaList, ManHub.this, currentSite, currentLayout);
                    mRecyclerView.setAdapter(mAdapter);

                }

                return false;
            }
        });

        ToggleDrawerItem layoutChange = new ToggleDrawerItem().withIdentifier(3).withSelectable(false).withName("Layout Change").withOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    mLayoutManager = new LinearLayoutManager(ManHub.this);
                    mLayoutManager.setItemPrefetchEnabled(true);
                } else {
                    //mLayoutManager = new GridLayoutManager(ManHub.this, 3);
                    mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                    //mLayoutManager = new FanLayoutManager(ManHub.this);

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
                                        mLayoutManager = new GridLayoutManager(ManHub.this, 3);
                                        break;

                                    case DETAILS:
                                        mLayoutManager = new LinearLayoutManager(ManHub.this);
                                        break;

                                    default:
                                        mLayoutManager = new LinearLayoutManager(ManHub.this);
                                        break;

                                }

                                mLayoutManager.setItemPrefetchEnabled(true);
                                mRecyclerView.setLayoutManager(mLayoutManager);

                                mAdapter = new MangaAdapter(mangaArrayList, ManHub.this, client, currentLayout);
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
                        //genreChange,
                        new DividerDrawerItem(),
                        //layoutChange,
                        layoutChangeChoice,
                        sortBy,
                        new DividerDrawerItem(),
                        home,
                        gotoFavorites,
                        settings,
                        logout
                        */
/*,
                        new ExpandableDrawerItem().withName("Collapsable").withIcon(GoogleMaterial.Icon.gmd_collections_bookmark).withIdentifier(19).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_add_alert).withIdentifier(2002),
                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_3d_rotation).withIdentifier(2003)
                        )*//*

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

    public void addFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container_frags,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container_frags,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    TagContainerLayout mTagContainerLayout;

    public void genreSearch() {
        final LovelyCustomDialog customDialog = new LovelyCustomDialog(ManHub.this)
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

                */
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
                });*//*


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
                        mAdapter = new MangaAdapter(alm, ManHub.this, client, currentLayout);
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

                                        */
/*Handler uiHandler = new Handler(Looper.getMainLooper());
                                        uiHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                searchBars.setPlaceHolder("Search here through " + alm.size() + " items");
                                            }
                                        });*//*


                                        add = false;
                                    }
                                }

                                if(add) {
                                    Help.w(mangaDetails.getTitle(), Arrays.toString(mangaDetails.getCategories()));
                                    alm.add(mangaArrayList.get(i));
                                    Handler handler = ManHub.this.getWindow().getDecorView().getHandler();
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
                        //mAdapter = new MangaAdapter(alm, ManHub.this, client, currentLayout);
                        //mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setHasTransientState(false);
                    }
                }).execute();
            }
        });

        customDialog.show();
    }
*/


}
