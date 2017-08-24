package manga.mangaapp.manymanga.sites.implementations.germanscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Daniel Biesecke <dbiesecke@gmail.com>
 */
public class TokidoTranslations extends FoOlSlide implements Site {

    public TokidoTranslations() {
        super("Tokido Translations", "http://tokido-scans.blogspot.de/", Arrays
                .asList("German"), false, "http://tokido.bplaced.net/Reader/",
                "/directory/");
    }
}
