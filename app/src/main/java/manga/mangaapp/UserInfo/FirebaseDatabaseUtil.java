package manga.mangaapp.UserInfo;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import manga.mangaapp.Help;

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
        myTopPostsQuery.addValueEventListener(valueEventListener);

    }

    public static void removeData(String mangaid) {
        FirebaseDatabase.getInstance().getReference("/user-posts/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + mangaid).removeValue();
    }

}
