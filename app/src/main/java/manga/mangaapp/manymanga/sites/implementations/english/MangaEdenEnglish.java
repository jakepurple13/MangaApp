package manga.mangaapp.manymanga.sites.implementations.english;

import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.MangaEden;
import java.util.Arrays;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaEdenEnglish extends MangaEden implements Site {

    public MangaEdenEnglish() {
        super(
                "Manga Eden",
                "http://www.mangaeden.com/en/",
                Arrays.asList("English"),
                false,
                "en-directory/");
    }
}
