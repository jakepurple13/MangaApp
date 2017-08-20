package manga.mangaapp.mangaedenclient;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
public class MangaEdenClient {

  private static final Gson GSON = new Gson();

  private final OkHttpClient httpClient;
  private final URI baseURI;

  public MangaEdenClient(URI baseURI) {
    this.baseURI = baseURI;
    this.httpClient = new OkHttpClient();
  }

  public MangaEdenClient() {
    this(MangaEden.BASE_URI);
  }

  public List<Manga> getMangaList() throws IOException {
    return getMangaList(null);
  }

  public List<Manga> getMangaList(int page) throws IOException {
    return getMangaList(new Integer(page));
  }

  private List<Manga> getMangaList(Integer page) throws IOException {
    String suffix = page != null ? "?p=" + page : "";
    URI uri = baseURI.resolve("/api/list/0/" + suffix);
    Request request = new Request.Builder().get().url(uri.toURL()).build();

    Response response = httpClient.newCall(request).execute();
    String responseBody = response.body().string();
    MangaListResponse mangaListResponse = GSON.fromJson(responseBody, MangaListResponse.class);
    return mangaListResponse.getManga();
  }

  public MangaDetails getMangaDetails(String mangaId) throws IOException {
    URI uri = baseURI.resolve("/api/manga/" + mangaId + "/");
    Request request = new Request.Builder().get().url(uri.toURL()).build();

    Response response = httpClient.newCall(request).execute();
    String responseBody = response.body().string();
    return GSON.fromJson(responseBody, MangaDetails.class);
  }

  public ChapterDetails getChapterDetails(String chapterId) throws IOException {
    URI uri = baseURI.resolve("/api/chapter/" + chapterId + "/");
    Request request = new Request.Builder().get().url(uri.toURL()).build();

    Response response = httpClient.newCall(request).execute();
    String responseBody = response.body().string();
    return GSON.fromJson(responseBody, ChapterDetails.class);
  }

}
