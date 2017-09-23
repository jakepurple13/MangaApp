package manga.mangaapp.UserInfo;

import in.cubestack.android.lib.storm.FieldType;
import in.cubestack.android.lib.storm.annotation.Column;
import in.cubestack.android.lib.storm.annotation.PrimaryKey;
import in.cubestack.android.lib.storm.annotation.Table;

/**
 * Created by Jacob on 9/23/17.
 */
@Table(name = "MANGA")
public class MangaTable {

    @PrimaryKey
    @Column(name="ID", type = FieldType.INTEGER)
    private int id;

    @Column(name="MANGA_ID", type = FieldType.TEXT)
    private String mangaid;

    @Column(name="MANGA_NAME", type = FieldType.TEXT)
    private String mangaName;

    @Column(name="CHAPTER_ID", type = FieldType.TEXT)
    private String chapterID;

    public MangaTable(String mangaid) {
        this.mangaid = mangaid;
    }

    public MangaTable(String mangaid, String mangaName) {
        this.mangaid = mangaid;
        this.mangaName = mangaName;
    }

    public MangaTable(String mangaid, String mangaName, String chapterID) {
        this.mangaid = mangaid;
        this.mangaName = mangaName;
        this.chapterID = chapterID;
    }

    public MangaTable() {

    }

    public int getId() {
        return id;
    }

    public String getMangaid() {
        return mangaid;
    }

    public String getMangaName() {
        return mangaName;
    }

    public String getChapterID() {
        return chapterID;
    }

    public void setChapterID(String chapterID) {
        this.chapterID = chapterID;
    }

    @Override
    public String toString() {
        return id + ": " + mangaName + ": " + mangaid + ": " + chapterID;
    }
}
