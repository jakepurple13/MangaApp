package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class YuriProject extends FoOlSlide implements Site {

    public YuriProject() {
        super("Yuri Project", "http://yuriproject.net", Arrays
                .asList("English"), false, "http://reader.yuriproject.net",
                "/directory/");
    }
}
