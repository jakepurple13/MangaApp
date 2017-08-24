package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SilentSky extends FoOlSlide implements Site {

    public SilentSky() {
        super("Silent Sky", "http://www.silentsky-scans.net", Arrays
                .asList("English"), false, "http://reader.silentsky-scans.net",
                "/directory/");
    }
}
