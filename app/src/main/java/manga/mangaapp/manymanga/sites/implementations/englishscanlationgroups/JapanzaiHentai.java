package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class JapanzaiHentai extends FoOlSlide implements Site {

    public JapanzaiHentai() {
        super("Japanzai (Hentai)", "http://japanzai.com", Arrays
                .asList("English"), false, "http://h.japanzai.com",
                "/directory/");
    }
}
