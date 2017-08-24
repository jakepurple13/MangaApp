package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Japanzai extends FoOlSlide implements Site {

    public Japanzai() {
        super("Japanzai", "http://japanzai.com", Arrays.asList("English"),
                false, "http://reader.japanzai.com", "/directory/");
    }
}
