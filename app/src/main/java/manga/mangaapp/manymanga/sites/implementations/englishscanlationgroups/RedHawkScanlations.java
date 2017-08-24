package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class RedHawkScanlations extends FoOlSlide implements Site {

    public RedHawkScanlations() {
        super("Red Hawk Scanlations", "http://www.redhawkscans.com", Arrays
                .asList("English"), false, "http://manga.redhawkscans.com",
                "/reader/list/");
    }
}
