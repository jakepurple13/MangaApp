package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class JapanzaiEcchi extends FoOlSlide implements Site {

    public JapanzaiEcchi() {
        super("Japanzai (Ecchi)", "http://japanzai.com", Arrays
                .asList("English"), false, "http://ecchi.japanzai.com",
                "/directory/");
    }
}
