package manga.mangaapp.manymanga.sites.implementations.english;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.MangaPandaAndReader;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaReader extends MangaPandaAndReader implements Site {

    public MangaReader() {
        super("Manga Reader", "http://www.mangareader.net", Arrays
                .asList("English"), true);
    }
}
