package manga.mangaapp.manymanga.sites.implementations.españolenglishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class CaféConIRST extends FoOlSlide implements Site {

    public CaféConIRST() {
        super("Café con IRST", "http://cafeconirst.com/", Arrays.asList(
                "Español", "English"), false, "http://reader.cafeconirst.com/",
                "/directory/");
    }
}
