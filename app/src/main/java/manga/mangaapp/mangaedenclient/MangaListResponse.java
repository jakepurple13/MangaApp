package manga.mangaapp.mangaedenclient;

import java.util.List;

/**
 *
 */
public class MangaListResponse {

  private final List<Manga> manga;

  public MangaListResponse(List<Manga> manga) {
    this.manga = manga;
  }

  public List<Manga> getManga() {
    return manga;
  }
}
