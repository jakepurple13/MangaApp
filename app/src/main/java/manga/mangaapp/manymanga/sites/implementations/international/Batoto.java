package manga.mangaapp.manymanga.sites.implementations.international;

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
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Batoto implements Site {

    private final String name = "Batoto";
    private final String url = "https://bato.to";
    private final List<String> language = Arrays.asList("International");
    private final Boolean watermarks = false;

    private final int loadCount = 1000;

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url
                + "/comic/_/comics/?per_page=" + loadCount + "&st=0");

        int max = Integer.parseInt(doc.select("li[class=last]").first()
                .select("a").first().attr("href").split("st=")[1]);

        for (int i = 0; i <= max; i += loadCount) {
            if (i != 0) {
                doc = JsoupHelper
                        .getHTMLPage(url + "/comic/_/comics/?per_page="
                                + loadCount + "&st=" + i);
            }

            Elements rows = doc
                    .select("table[class=ipb_table topic_list hover_rows]")
                    .first().select("tr");

            for (Element row : rows) {
                Elements cols = row.select("td");

                if (cols.size() != 7) {
                    continue;
                }

                mangas.add(new Manga(cols.get(1).select("a").first()
                        .attr("href"), cols.get(1).text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("table[class=ipb_table chapters_list]")
                .first().select("tr");

        for (Element row : rows) {
            Elements cols = row.select("td");
            if (cols.size() != 5) {
                continue;
            }

            Element link = cols.get(0).select("a").first();

            String title = cols.get(0).text() + " ["
                    + cols.get(1).select("div").first().attr("title") + "]"
                    + " [" + cols.get(2).text() + "]";

            chapters.add(new Chapter(manga, link.attr("href"), title));
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink() + "?supress_webtoon=t";
        Document doc = JsoupHelper.getHTMLPage(referrer);

        // Get pages linkes
        Elements pages = doc.select("div[class=moderation_bar rounded clear]")
                .first().select("ul").first().select("li").get(3)
                .select("option");

        for (int i = 0; i < pages.size(); i++) {
            if (i != 0) {
                referrer = pages.get(i).attr("value") + "?supress_webtoon=t";
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=comic_page]").first().attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }

    @Override
    public String getChapterCoverLink(Chapter chapter) throws Exception {
        return null;
    }

    @Override
    public String getMangaSummary(Manga manga) throws Exception {
        return null;
    }

    @Override
    public String coverURL(Manga manga) throws Exception {
        return null;
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
