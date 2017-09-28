package manga.mangaapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import manga.mangaapp.mangaedenclient.Chapter;
import manga.mangaapp.mangaedenclient.ChapterDetails;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.MangaDetails;
import manga.mangaapp.mangaedenclient.MangaEdenClient;
import manga.mangaapp.manymanga.data.Image;

/**
 * Created by Jacob on 9/28/17.
 */

public class AppUtil {

    public static void downloadChapter(final Context context, final ChapterPage[] chapters, final String title, final int chapterNum) {

        Help.d(chapters.length + " pages");

        for(int i=0;i<chapters.length;i++) {

            URL url = null;
            try {
                url = new URL(String.valueOf(chapters[i].getImageURI()));
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                SaveImage(image, "MangaWorld/" + title + "/" + chapterNum, chapters[i].getNumber() + ".jpg");
                Toast.makeText(context, "Downloaded Page: " + chapters[i].getNumber(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static void downloadChapter(final Context context, final List<Image> chapters, final String title, final int chapterNum) {

        Help.d(chapters.size() + " pages");

        for(int i=0;i<chapters.size();i++) {

            URL url = null;
            try {
                url = new URL(String.valueOf(chapters.get(i).getLink()));
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                SaveImage(image, "MangaWorld/" + title + "/" + chapterNum, i + ".jpg");
                Toast.makeText(context, "Downloaded Page: " + i, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private static void SaveImage(Bitmap finalBitmap, String folderName, String name) {

        String root = Environment.getExternalStorageDirectory().toString();

        File myDir = new File(root + "/" + folderName);
        int i=0;
        boolean mkdirs = false;
        do {

            mkdirs = myDir.mkdirs();
            i++;

        } while((!mkdirs || !myDir.exists()) && i<20);

        String fname = name;

        File file = new File (myDir, fname);

        if (file.exists ()) file.delete ();

        try {

            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
