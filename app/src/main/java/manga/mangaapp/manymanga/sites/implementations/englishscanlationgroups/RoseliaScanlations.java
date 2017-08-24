package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class RoseliaScanlations extends FoOlSlide implements Site {

    public RoseliaScanlations() {
        super("Roselia Scanlations", "http://www.roseliascans.com", Arrays
                .asList("English"), false, "http://reader.roseliascans.com",
                "/directory/");
    }
}
