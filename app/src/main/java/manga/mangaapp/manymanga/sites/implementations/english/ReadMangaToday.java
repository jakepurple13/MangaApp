package manga.mangaapp.manymanga.sites.implementations.english;

import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Image;
import manga.mangaapp.manymanga.data.Manga;
import manga.mangaapp.manymanga.helpers.JsoupHelper;
import manga.mangaapp.manymanga.sites.Site;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author hurik
 */
public class ReadMangaToday implements Site {

    private final String name = "ReadManga Today";
    private final String url = "http://www.readmanga.today";
    private final List<String> language = Arrays.asList("English");
    private final Boolean watermarks = false;

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + "/manga-list/");

        Elements letters = doc.select("div[class=manga-letters]").first()
                .select("a");

        for (Element letter : letters) {
            if (letter != letters.first()) {
                doc = JsoupHelper.getHTMLPage(letter.attr("href"));
            }

            Elements rows = doc.select("span[class=manga-item]");

            for (Element row : rows) {
                Element link = row.select("a").first();
                mangas.add(new Manga(link.attr("href"), link.text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("ul[class=chp_lst]").first().select("li");

        for (Element row : rows) {
            if (row.select("a").first() == null) {
                continue;
            }

            chapters.add(new Chapter(manga, row.select("a").first().attr("href"), row
                    .select("span[class=val]").first().text()));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements pages = doc.select("ul[class=list-switcher-2]").first()
                .select("select[class=form-control input-sm jump-menu]")
                .first().select("option");

        for (Element page : pages) {
            if (page != pages.first()) {
                referrer = page.attr("value");
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("div[class=col-left col-md-12]").first()
                    .select("div[class=content-list col-md-12 page_chapter]")
                    .get(1).select("img").attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public List<String> getLanguage() {
        return language;
    }

    @Override
    public Boolean hasWatermarks() {
        return watermarks;
    }

}
