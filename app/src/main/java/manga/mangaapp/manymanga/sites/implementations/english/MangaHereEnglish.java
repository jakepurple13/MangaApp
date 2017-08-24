package manga.mangaapp.manymanga.sites.implementations.english;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.MangaHere;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaHereEnglish extends MangaHere implements Site {

    public MangaHereEnglish() {
        super("Manga Here", "http://www.mangahere.co",
                Arrays.asList("English"), true);
    }
}
