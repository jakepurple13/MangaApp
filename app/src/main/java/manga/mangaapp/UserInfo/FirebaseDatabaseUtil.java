package manga.mangaapp.UserInfo;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cubestack.android.lib.storm.criteria.Restriction;
import in.cubestack.android.lib.storm.criteria.Restrictions;
import in.cubestack.android.lib.storm.criteria.StormRestrictions;
import in.cubestack.android.lib.storm.service.BaseService;
import in.cubestack.android.lib.storm.service.StormService;
import manga.mangaapp.Help;
import manga.mangaapp.MangaSide.MangaInfoActivity;

/**
 * Created by Jacob on 9/19/17.
 */

public class FirebaseDatabaseUtil {


    public static void writeNewUser() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserInfo user = new UserInfo(userId);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("users").child(userId).setValue(user);
    }

    public static void writeNewPost(String mangaID, String chapterID, String title) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String key = mDatabase.getReference().child("posts").push().getKey();
        UserInfo post = new UserInfo(userId, mangaID, chapterID, title);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + /*key + "/" +*/ mangaID, postValues);

        mDatabase.getReference().updateChildren(childUpdates);
    }

    public static void getListOfData(ValueEventListener valueEventListener) {

        String myUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference().child("user-posts").child(myUserId);

        // My top posts by number of stars
        //myTopPostsQuery.addValueEventListener(valueEventListener);

        myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    public static void removeData(String mangaid) {
        FirebaseDatabase.getInstance().getReference("/user-posts/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + mangaid).removeValue();
    }

    public static void saveLocal(Context context, String mangaID, String title, String chapterID) {

        StormService service = new BaseService(context, MangaDatabase.class);
        MangaTable entity = new MangaTable(mangaID, title, chapterID);

        // Set all values
        try {
            service.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertTrue(entity.getId() > 0 );

        printDatabase(service);
    }

    public static void deleteLocal(Context context, String mangaID, String title) {
        StormService service = new BaseService(context, MangaDatabase.class);
        MangaTable entity = new MangaTable(mangaID, title, "");

        // Set all values
        try {
            Restrictions name = StormRestrictions.restrictionsFor(MangaTable.class);
            service.delete(MangaTable.class, name.equals("mangaid", mangaID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        printDatabase(service);
    }

    public static MangaTable findLocal(Context context, String mangaID) {
        StormService service = new BaseService(context, MangaDatabase.class);
        MangaTable mt;
        // Set all values
        try {
            Restrictions name = StormRestrictions.restrictionsFor(MangaTable.class);
            mt = service.findOne(MangaTable.class, name.equals("mangaid", mangaID));
        } catch (Exception e) {
            e.printStackTrace();
            mt = null;
        }
        printDatabase(service);
        return mt;
    }

    public static MangaTable updateChapterLocal(Context context, String mangaID, String chapterID) throws Exception {
        StormService service = new BaseService(context, MangaDatabase.class);

        // Set all values
        try {
            MangaTable mt = findLocal(context, mangaID);
            Help.d(mt.toString());
            mt.setChapterID(chapterID);
            service.update(mt);
            printDatabase(service);
            return mt;
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        }
    }

    private static void printDatabase(StormService service) {
        try {
            List<MangaTable> savedEntities  = service.findAll(MangaTable.class);

            for(MangaTable mt : savedEntities) {
                Help.d(mt.getId() + "", mt.getMangaName() + ": " + mt.getMangaid() + ": " + mt.getChapterID());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
