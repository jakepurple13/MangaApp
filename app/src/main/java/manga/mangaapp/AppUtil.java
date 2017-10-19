package manga.mangaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import manga.mangaapp.mangaedenclient.Chapter;
import manga.mangaapp.mangaedenclient.ChapterDetails;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.Manga;
import manga.mangaapp.mangaedenclient.MangaDetails;
import manga.mangaapp.mangaedenclient.MangaEden;
import manga.mangaapp.mangaedenclient.MangaEdenClient;
import manga.mangaapp.manymanga.data.Image;
import programmer.box.utilityhelper.UtilNotification;

/**
 * Created by Jacob on 9/28/17.
 */

public class AppUtil {

    public static void downloadChapter(final Context context, final ChapterPage[] chapters, final String title, final int chapterNum) {

        Help.d(chapters.length + " pages");

        for (int i = 0; i < chapters.length; i++) {

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

    public static void downloadChapter(final Context context, final String coverURL, final ChapterPage[] chapters, final String title, final String mangaID, final int chapterNum, boolean b) {

        final LovelyProgressDialog lpd = new LovelyProgressDialog(context);
        lpd.setMessage(context.getString(R.string.getting_manga));
        lpd.setMessageGravity(Gravity.CENTER);
        lpd.setTitle(context.getString(R.string.please_wait_));
        lpd.setTitleGravity(Gravity.CENTER);
        lpd.setIcon(R.drawable.ic_menu_black_24dp);

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Integer, Boolean> task = new AsyncTask<Void, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lpd.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {

                Help.d(chapters.length + " pages");

                try {
                    URL url1 = new URL(String.valueOf(MangaEden.manga2ImageURI(coverURL)));//new URL(coverURL);
                    Bitmap image1 = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                    SaveImage(image1, "MangaWorld/" + title + "/" + chapterNum, "cover.jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < chapters.length; i++) {

                    URL url = null;
                    try {
                        url = new URL(String.valueOf(chapters[i].getImageURI()));
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        //SaveImage(image, "MangaWorld/" + mangaID + "/" + chapterNum, chapters[i].getNumber() + ".jpg");
                        SaveImage(image, "MangaWorld/" + title + "/" + chapterNum, chapters[i].getNumber() + ".jpg");
                        //Toast.makeText(context, "Downloaded Page: " + chapters[i].getNumber(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    publishProgress(i, chapters.length);

                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                lpd.dismiss();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    UtilNotification.createNotificationChannel(context, "manga", "downloaded", "manga_is_downloaded");
                    UtilNotification.createNotificationGroup(context, "group_manga_id", "group_manga");
                }

                String notifyUser = context.getString(R.string.manga_is_downloaded, title);

                UtilNotification.sendNotification(context, android.R.mipmap.sym_def_app_icon, context.getString(R.string.downloaded), notifyUser, "manga_is_downloaded", MainActivity.class, 1);

            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                int per = (int) ((Double.valueOf(values[0])/Double.valueOf(values[1]))*100);
                lpd.setMessage(per + "%");
            }

        }.execute();


    }

    public static void downloadChapter(final Context context, final List<Image> chapters, final String title, final int chapterNum, boolean b) {

        final LovelyProgressDialog lpd = new LovelyProgressDialog(context);
        lpd.setMessage("Getting Manga");
        lpd.setMessageGravity(Gravity.CENTER);
        lpd.setTitle("Please Wait...");
        lpd.setTitleGravity(Gravity.CENTER);
        lpd.setIcon(R.drawable.ic_menu_black_24dp);

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Integer, Boolean> task = new AsyncTask<Void, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lpd.show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {

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
                    publishProgress(i, chapters.size());
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                lpd.dismiss();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                int per = (int) ((Double.valueOf(values[0])/Double.valueOf(values[1]))*100);
                lpd.setMessage(per + "%");
            }

        }.execute();

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

    public static void getDownloadedChapters() {

        String root = Environment.getExternalStorageDirectory().toString();

        List<File> files = getListFiles(new File(root + "/MangaWorld"));
        for(File f : files) {
            Help.v(f.getName());
        }

    }

    public static MangaListFile getMangaFromDownloads(String key) {
        return getDownloadedChapters(true).get(key);
    }

    public static File[] getMangaChapters() {
        String root = Environment.getExternalStorageDirectory().toString();
        List<File> imageFile = getImageFiles(new File(root + "/MangaWorld"));

        for(File f : imageFile) {
            //int num = getLocation(mangas, f);
            Help.v(f.getName());
        }
        return imageFile.get(0).listFiles();
    }

    public static HashMap<String, MangaListFile> getDownloadedChapters(boolean choose/*ArrayList<Manga> mangas*/) {

        String root = Environment.getExternalStorageDirectory().toString();

        //List<File> files = getListFiles(new File(root + "/MangaWorld"));
        List<File> files = getImageFiles(new File(root + "/MangaWorld"));

        for(File f : files) {
            //int num = getLocation(mangas, f);
            Help.v(f.getName());
        }
        /*
        File[] filed = imageFile.get(0).listFiles();*/
        //ArrayList<MangaFile> mangaFileArrayList = new ArrayList<>();

        HashMap<String, MangaListFile> map = new HashMap<>();

        for(int i=0;i<files.size()-1;i++) {
            if(files.get(i+1).getParentFile().equals(files.get(i))) {
                Help.v(files.get(i).getName() + " is the parent directory of " + files.get(i+1).getName());

                String name = files.get(i).getName();
                String path = files.get(i+1).getPath();
                MangaFile file = new MangaFile(name, path);

                if(!map.containsKey(name)) {
                    //if its not here
                    map.put(name, new MangaListFile(name));
                    Help.e(name + " is now here");
                } else {
                    //if it is here
                    map.get(name).addFile(file);
                    Help.e(name + " is still here");
                }

                //mangaFileArrayList.add(file);
            }
        }

        return map;

    }

    public static class MangaListFile {
        String name;
        String cover;
        ArrayList<MangaFile> files;

        public MangaListFile(String name) {
            this.name = name;
            files = new ArrayList<>();
        }

        public MangaListFile(String name, String cover) {
            this.name = name;
            this.cover = cover;
            files = new ArrayList<>();
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getCover() {
            return cover;
        }

        public void addFile(MangaFile file) {
            files.add(file);
        }

        public ArrayList<MangaFile> getFiles() {
            return files;
        }

        public String getName() {
            return name;
        }
    }

    public static class MangaFile {

        String name;
        String directory;

        public MangaFile(String name, String directory) {
            this.name = name;
            this.directory = directory;
        }

        public String getName() {
            return name;
        }

        public String getDirectory() {
            return directory;
        }

        @Override
        public String toString() {
            return name + ": " + directory;
        }
    }

    public static int getLocation(ArrayList<Manga> mangas, File f) {
        int num = -1;

        for(int i=0;i<mangas.size();i++) {
            if(mangas.get(i).getTitle().equals(f.getName())) {
                num = i;
                break;
            }
        }

        return num;
    }

    private static List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.add(file);
                inFiles.addAll(getListFiles(file));
            } else {
                //if(file.getName().endsWith("")){
                    //inFiles.add(file);
                //}
            }
        }
        return inFiles;
    }

    private static List<File> getImageFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".jpg")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    /**
     * Lightens a color by a given factor.
     *
     * @param color
     *            The color to lighten
     * @param factor
     *            The factor to lighten the color. 0 will make the color unchanged. 1 will make the
     *            color white.
     * @return lighter version of the specified color.
     */
    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

}
