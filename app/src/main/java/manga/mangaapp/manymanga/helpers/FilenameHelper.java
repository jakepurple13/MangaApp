package manga.mangaapp.manymanga.helpers;

import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Manga;
import manga.mangaapp.manymanga.options.Options;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public final class FilenameHelper {

    private final static String windows = "[<>:\"/\\|?*]";
    private final static String os = System.getProperty("os.name")
            .toLowerCase();

    private FilenameHelper() {
    }

    public static String checkForIllegalCharacters(String string) {
        if (os.contains("win")) {
            return string.replaceAll(windows, "_");
        } else {
            return string;
        }
    }

    private static String checkForValidDirectoryName(String string) {
        if (os.contains("win")) {
            while (string.endsWith(".") || string.endsWith(" ")) {
                string = string.substring(0, string.length() - 1);
            }
            return checkForIllegalCharacters(string);
        } else {
            return string;
        }
    }

    public static Path buildMangaPath(Manga manga) {
        return Paths.get(Options.INSTANCE.getMangaDir()).resolve(
                checkForValidDirectoryName(manga.getTitle()));
    }

    public static Path buildChapterPath(Manga manga, Chapter chapter) {
        return buildMangaPath(manga).resolve(
                checkForIllegalCharacters(chapter.getTitle()) + ".cbz");
    }
}
