package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class KireiCake extends FoOlSlide implements Site {

    public KireiCake() {
        super("Kirei Cake", "http://kireicake.com", Arrays.asList("English"),
                false, "http://reader.kireicake.com", "/reader/list/");
    }
}
