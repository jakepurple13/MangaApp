package manga.mangaapp.manymanga.sites.extend;

import manga.mangaapp.Help;
import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Image;
import manga.mangaapp.manymanga.data.Manga;
import manga.mangaapp.manymanga.helpers.JsoupHelper;
import manga.mangaapp.manymanga.sites.Site;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaHere implements Site {

    private final String name;
    private final String url;
    private final List<String> language;
    private final Boolean watermarks;

    public MangaHere(String name, String url, List<String> language,
            Boolean watermarks) {
        this.name = name;
        this.url = url;
        this.language = language;
        this.watermarks = watermarks;
    }

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(url + "/mangalist/");

        Elements cols = doc.select("div[class=list_manga]");

        for (Element col : cols) {
            Elements rows = col.select("li");

            for (Element row : rows) {
                Element link = row.select("a").first();
                mangas.add(new Manga(link.attr("abs:href"), link.attr("rel")));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        // Check if comic not there (One Piece - Colored) ...
        Elements pageNotFound = doc.select("div[class=detail_list]");
        if (pageNotFound.isEmpty()) {
            return chapters;
        }

        // Check if comic was licensed (One Piece) ...
        Elements licensed = doc.select("div[class=detail_list]").first()
                .select("div[class=mt10 color_ff00 mb10]");
        if (!licensed.isEmpty()) {
            return chapters;
        }

        Elements rows = doc.select("div[class=detail_list]").first()
                .select("ul").first().select("li");

        for (Element row : rows) {
            if (row.select("a").first() == null) {
                continue;
            }

            chapters.add(new Chapter(manga, row.select("a").first().attr("abs:href"),
                    buildChapterName(row)));
        }

        return chapters;
    }

    private String buildChapterName(Element row) {
        String front = row.select("a").first().text();
        String middle = row.select("span[class=mr6]").first().text();
        String end = row.select("span").first().text()
                .replace(front + middle, "");

        if (!middle.isEmpty()) {
            front += " " + middle;
        }

        if (!end.isEmpty()) {
            front += " " + end;
        }

        return front;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements nav = doc.select("select[onchange=change_page(this)]").first()
                .select("option");

        for (Element page : nav) {
            if (page != nav.first()) {
                referrer = page.attr("value");
                if (!referrer.startsWith("http://")) {
                    // TODO: CHECK THIS!
                    referrer = url + referrer;
                }
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=image]").first().attr("src");
            String linkTemp = link.split("\\?v=")[0];
            String extension = linkTemp.substring(linkTemp.length() - 3,
                    linkTemp.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }

    @Override
    public String getChapterCoverLink(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPage(referrer);

        Elements nav = doc.select("select[onchange=change_page(this)]").first()
                .select("option");

        for (Element page : nav) {
            if (page != nav.first()) {
                referrer = page.attr("value");
                if (!referrer.startsWith("http://")) {
                    // TODO: CHECK THIS!
                    referrer = url + referrer;
                }
                doc = JsoupHelper.getHTMLPage(referrer);
            }

            String link = doc.select("img[id=image]").first().attr("src");
            String linkTemp = link.split("\\?v=")[0];
            String extension = linkTemp.substring(linkTemp.length() - 3,
                    linkTemp.length());

            images.add(new Image(link, referrer, extension));
            return link;
        }

        return "";
    }

    @Override
    public String coverURL(Manga manga) throws Exception {
        Document doc = JsoupHelper.getHTMLPage(manga.getLink());

        Elements rows = doc.select("img").addClass("img");//first().select("img");

        Help.e(rows.attr("src"));

        return rows.attr("src");
    }

    @Override
    public String getMangaSummary(Manga manga) throws Exception {
        String summary = "";

        Document doc = JsoupHelper.getHTMLPage(manga.getLink());
        //Help.v(doc.html());
        Elements rows = doc.select("p[id=hide]");//.first().select("p");
        Help.v(rows.text());
        summary = rows.text();

        return summary;
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
