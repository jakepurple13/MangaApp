package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class S2Scans extends FoOlSlide implements Site {

    public S2Scans() {
        super("S2Scans", "http://s2smanga.com", Arrays.asList("English"),
                false, "http://reader.s2smanga.com", "/directory/");
    }
}
