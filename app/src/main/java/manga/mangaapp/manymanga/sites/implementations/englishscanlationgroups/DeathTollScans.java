package manga.mangaapp.manymanga.sites.implementations.englishscanlationgroups;

import com.google.gson.Gson;
import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Image;
import manga.mangaapp.manymanga.helpers.JsoupHelper;
import manga.mangaapp.manymanga.sites.Site;
import manga.mangaapp.manymanga.sites.extend.FoOlSlide;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.jsoup.nodes.Document;

/**
 *
 * @author hurik
 */
public class DeathTollScans extends FoOlSlide implements Site {

    public DeathTollScans() {
        super("Death Toll Scans", "http://deathtollscans.net/", Arrays
                .asList("English"), false,
                "http://www.deathtollscans.net/reader", "/directory/");
    }

    @Override
    public List<Image> getChapterImageLinks(Chapter chapter) throws Exception {
        List<Image> images = new LinkedList<>();

        try {
            images = super.getChapterImageLinks(chapter);
        } catch (Exception ex) {
            String referrer = chapter.getLink();
            Document doc = JsoupHelper.getHTMLPageWithPost(referrer, post);

            String line = null;

            try (Scanner scanner = new Scanner(doc.toString())) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    if (line.contains("var pages = ")) {
                        break;
                    }
                }
            }

            if (line == null) {
                throw new Exception();
            }

            String data = line.replace("var pages = ", "").replace(";", "")
                    .trim();

            Wrapper[] dataArray = new Gson().fromJson(data, Wrapper[].class);

            for (Wrapper dataElement : dataArray) {
                String extension = dataElement.url.substring(
                        dataElement.url.length() - 3, dataElement.url.length());
                images.add(new Image(dataElement.url, referrer, extension));
            }
        }

        return images;
    }

    class Wrapper {

        public String url;
    }
}
