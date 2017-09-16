package manga.mangaapp.manymanga.sites;

import android.util.Log;

import manga.mangaapp.MangaSide.GenreTags;
import manga.mangaapp.manymanga.helpers.ClassesInPackageHelper;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class SiteHelper {

    private final static String implementationsPackage = "manga.mangaapp.manymanga.sites.implementations";

    private SiteHelper() {
    }

    public static List<Site> getSite(Site s) {
        List<Site> sites = new LinkedList<>();
        sites.add(s);
        return sites;
    }

    public static List<Site> getSites() {
        List<Site> sites = new LinkedList<>();

        try {
            ArrayList<Class<?>> clazzes = ClassesInPackageHelper
                    .getClassesForPackageWithInterface(implementationsPackage,
                            Site.class);

            Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), clazzes.toString());


            //sites.add(new MangaFox());
            //sites.add(new MangaHereEnglish());
            //sites.add(new ReadMangaToday());
            sites.add(new MangaReader());
            //sites.add(new KissManga());
            //sites.add(new LINEWebtoon());
            //sites.add(new Tapastic());
            //sites.add(new Mangajoy());
            //sites.add(new MangaPanda());

            for (Class<?> clazz : clazzes) {
                try {
                    sites.add((Site) clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        Collections.sort(sites, new Comparator<Site>() {
            @Override
            public int compare(Site site1, Site site2) {
                return site1.getName().compareTo(site2.getName());
            }
        });

        return sites;
    }

    public static Site getInstance(String source) {
        try {
            ArrayList<Class<?>> clazzes = ClassesInPackageHelper
                    .getClassesForPackageWithInterface(implementationsPackage,
                            Site.class);

            for (Class<?> clazz : clazzes) {
                if (clazz.getSimpleName().equals(source)) {
                    return (Site) clazz.newInstance();
                }
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
        }

        return null;
    }

    public static Site getSiter(String source) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class x = Site.class;
        Constructor[] constructors = x.getDeclaredConstructors();
        Field[] fields = x.getDeclaredFields();
        Method[] methods = x.getDeclaredMethods();
        for (Constructor constructor : constructors) {
            //constructors
            Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), constructor.getName());
        }
        for (Field field : fields) {
            //fields
            Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), field.getName());
        }
        for (Method method : methods) {
            //methods
            Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), method.getName());
        }

        for (Class<?> clazz : x.getClasses()) {
            if (clazz.getSimpleName().equals(source)) {
                Site s = (Site) clazz.getClassLoader().loadClass(source).newInstance();
                Log.e("Line_"+new Throwable().getStackTrace()[0].getLineNumber(), Arrays.toString(s.getClass().getMethods()));
                return s;
            }
        }
        return null;
    }

    public static Site getEnglishSiteFromString(GenreTags.Sources tag) {

        Site currentSite;

        switch (tag) {
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

        return currentSite;

    }

}
