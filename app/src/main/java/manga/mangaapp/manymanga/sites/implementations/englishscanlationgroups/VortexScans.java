package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class VortexScans extends FoOlSlide implements Site {

    public VortexScans() {
        super("Vortex-Scans", "http://vortex-scans.com", Arrays
                .asList("English"), false, "http://reader.vortex-scans.com",
                "/directory/");
    }
}
