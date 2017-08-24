package manga.mangaapp.manymanga.sites.implementations.español;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.MangaHere;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaHereEspañol extends MangaHere implements Site {

    public MangaHereEspañol() {
        super("Manga Here (Español)", "http://es.mangahere.co", Arrays
                .asList("Español"), false);
    }
}
