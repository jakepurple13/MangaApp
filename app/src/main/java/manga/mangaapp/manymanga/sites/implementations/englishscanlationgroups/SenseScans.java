package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SenseScans extends FoOlSlide implements Site {

    public SenseScans() {
        super("Sense-Scans", "http://sensescans.com", Arrays.asList("English"),
                false, "http://reader.sensescans.com", "/reader/list/");
    }
}
