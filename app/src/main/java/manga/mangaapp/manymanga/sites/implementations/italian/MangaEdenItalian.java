package manga.mangaapp.manymanga.sites.implementations.italian;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.MangaEden;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaEdenItalian extends MangaEden implements Site {

    public MangaEdenItalian() {
        super(
                "Manga Eden (Italian)",
                "http://www.mangaeden.com/it/",
                Arrays.asList("Italian"),
                true,
                "it-directory/");
    }
}
