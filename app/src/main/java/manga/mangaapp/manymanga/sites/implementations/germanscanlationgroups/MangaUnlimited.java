package manga.mangaapp.manymanga.sites.implementations.germanscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Daniel Biesecke <dbiesecke@gmail.com>
 */
public class MangaUnlimited extends FoOlSlide implements Site {

    public MangaUnlimited() {
        super("MangaUnlimited", "http://mangaunlimited.com/", Arrays
                .asList("German"), false, "http://reader.mangaunlimited.com",
                "/directory/");
    }
}
