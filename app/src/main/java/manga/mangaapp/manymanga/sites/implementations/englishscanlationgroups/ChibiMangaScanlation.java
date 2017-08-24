package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChibiMangaScanlation extends FoOlSlide implements Site {

    public ChibiMangaScanlation() {
        super("Chibi Manga Scanlation", "http://www.chibimanga.info", Arrays
                .asList("English"), false, "http://www.cmreader.info",
                "/directory/");
    }
}
