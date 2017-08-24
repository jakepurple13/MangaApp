package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class DemonicScanlations extends FoOlSlide implements Site {

    public DemonicScanlations() {
        super("Demonic Scanlations", "http://www.demonicscans.com", Arrays
                .asList("English"), false,
                "http://www.demonicscans.com/FoOlSlide", "/directory/");
    }

}
