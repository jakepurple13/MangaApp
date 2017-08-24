package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class OneTimeScans extends FoOlSlide implements Site {

    public OneTimeScans() {
        super("One Time Scans",
                "http://onetimescans.com/",
                Arrays.asList("English"),
                false,
                "http://onetimescans.com/foolslide/",
                "directory/");
    }
}
