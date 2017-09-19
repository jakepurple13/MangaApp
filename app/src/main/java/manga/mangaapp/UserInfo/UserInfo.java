package manga.mangaapp.UserInfo;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jacob on 9/19/17.
 */

@IgnoreExtraProperties
public class UserInfo {

    String uid;
    Map<String, String> mangaAndChapter = new HashMap<>();
    String mangaID;
    String chapterID;
    String mangaTitle;

    public UserInfo() {

    }

    public UserInfo(String uid) {
        this.uid = uid;
        this.mangaID = "";
        this.chapterID = "";
        this.mangaTitle = "";
    }

    public UserInfo(String uid, String mangaID, String chapterID, String mangaTitle) {
        this.uid = uid;
        this.mangaID = mangaID;
        this.chapterID = chapterID;
        this.mangaTitle = mangaTitle;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("mangaid", mangaID);
        result.put("chapterid", chapterID);
        result.put("mangatitle", mangaTitle);
        result.put("mangaAndChapter", mangaAndChapter);

        return result;
    }

    @Exclude
    @Override
    public String toString() {
        return "Title: " + mangaTitle + "\t\nManga ID: " + mangaID + "\t\nChapter ID: " + chapterID;
    }



}
