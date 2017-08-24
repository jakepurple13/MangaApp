package manga.mangaapp.manymanga.sites.implementations.english;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.MangaPandaAndReader;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaPanda extends MangaPandaAndReader implements Site {

    public MangaPanda() {
        super("Manga Panda", "http://www.mangapanda.com", Arrays
                .asList("English"), true);
    }
}
