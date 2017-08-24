package manga.mangaapp.manymanga.sites;

import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Image;
import manga.mangaapp.manymanga.data.Manga;
import java.util.List;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public interface Site {

    public List<Manga> getMangaList() throws Exception;

    public List<Chapter> getChapterList(Manga manga) throws Exception;

    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception;

    public String getName();

    public String getUrl();

    public List<String> getLanguage();

    public Boolean hasWatermarks();
}
