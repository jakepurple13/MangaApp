package manga.mangaapp.manymanga.sites.implementations.germanscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Daniel Biesecke <dbiesecke@gmail.com>
 */
public class ThrillerBarkCafé extends FoOlSlide implements Site {

    public ThrillerBarkCafé() {
        super("Thriller Bark Café", "http://www.thrillerbarkcafe.de/", Arrays
                .asList("German"), false, "http://reader.thrillerbarkcafe.de/",
                "/directory/");
    }
}
