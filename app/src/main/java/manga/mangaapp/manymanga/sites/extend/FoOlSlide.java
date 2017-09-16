package manga.mangaapp.manymanga.sites.extend;

import manga.mangaapp.Help;
import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Image;
import manga.mangaapp.manymanga.data.Manga;
import manga.mangaapp.manymanga.helpers.JsoupHelper;
import manga.mangaapp.manymanga.sites.Site;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class FoOlSlide implements Site {

    protected final String name;
    protected final String url;
    protected final List<String> language;
    protected final Boolean watermarks;

    protected final String baseUrl;
    protected final String listUrl;
    protected final Map<String, String> post = new HashMap<>();

    public FoOlSlide(String name, String url, List<String> language,
            Boolean watermarks, String baseUrl, String listUrl) {
        this.name = name;
        this.url = url;
        this.language = language;
        this.watermarks = watermarks;

        this.baseUrl = baseUrl;
        this.listUrl = listUrl;

        post.put("adult", "true"); // VortexScans adult check
        post.put("category", "1"); // SilentSkyScans Webton view
    }

    @Override
    public List<Manga> getMangaList() throws Exception {
        List<Manga> mangas = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPage(baseUrl + listUrl);

        int pages = 1;

        Element nav = doc.select("div[class=prevnext]").first();

        if (nav != null) {
            Element button = nav.select("a[class=gbutton fright]").first();
            if (button != null) {
                String[] pagesData = button.attr("href").split("/");
                pages = Integer.parseInt(pagesData[pagesData.length - 1]);
            }
        }

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                doc = JsoupHelper.getHTMLPage(baseUrl + listUrl + i + "/");
            }

            Elements rows = doc.select("div[class=group]");

            for (Element row : rows) {
                Element link = row.select("div[class^=title]").first()
                        .select("a").first();
                mangas.add(new Manga(link.attr("abs:href"), link.text()));
            }
        }

        return mangas;
    }

    @Override
    public List<Chapter> getChapterList(Manga manga) throws Exception {
        List<Chapter> chapters = new LinkedList<>();

        Document doc = JsoupHelper.getHTMLPageWithPost(manga.getLink(), post);

        Elements groups = doc.select("div[class=group]");

        if (groups.size() == 0) {
            Elements rows = doc.select("div[class=element]");

            for (Element row : rows) {
                Element link = row.select("div[class=title]").first()
                        .select("a").first();
                chapters.add(new Chapter(manga, link.attr("abs:href"), link.text()));
            }
        } else {
            for (Element group : groups) {
                String volume = group.select("div[class=title]").first().text();

                Elements rows = group.select("div[class=element]");

                for (Element row : rows) {
                    Element link = row.select("div[class=title]").first()
                            .select("a").first();
                    chapters.add(new Chapter(manga, link.attr("abs:href"), volume
                            + " - " + link.text()));
                }
            }
        }

        return chapters;
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPageWithPost(referrer, post);

        int pages = Integer.parseInt(doc
                .select("div[class=tbtitle dropdown_parent dropdown_right]")
                .first().select("div[class=text]").first().text()
                .replaceFirst("\\D*(\\d*).*", "$1"));

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                referrer = chapter.getLink() + "page/" + i;
                doc = JsoupHelper.getHTMLPageWithPost(referrer, post);
            }

            String link = doc.select("img[class=open]").first().attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension));
        }

        return images;
    }

    @Override
    public String getChapterCoverLink(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        String referrer = chapter.getLink();
        Document doc = JsoupHelper.getHTMLPageWithPost(referrer, post);

        int pages = Integer.parseInt(doc
                .select("div[class=tbtitle dropdown_parent dropdown_right]")
                .first().select("div[class=text]").first().text()
                .replaceFirst("\\D*(\\d*).*", "$1"));

        for (int i = 1; i <= pages; i++) {
            if (i != 1) {
                referrer = chapter.getLink() + "page/" + i;
                doc = JsoupHelper.getHTMLPageWithPost(referrer, post);
            }

            String link = doc.select("img[class=open]").first().attr("src");
            String extension = link.substring(link.length() - 3, link.length());

            images.add(new Image(link, referrer, extension));
            return link;
        }

        return "";
    }

    @Override
    public String getMangaSummary(Manga manga) throws Exception {
        String summary = "";

        Document doc = JsoupHelper.getHTMLPage(url + manga.getLink());
        Help.v(doc.html());
        Elements rows = doc.select("div[id=readmangasum]").first().select("p");
        Help.v(rows.text());
        summary = rows.text();

        return summary;
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
