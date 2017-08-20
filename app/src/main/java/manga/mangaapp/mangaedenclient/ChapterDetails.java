package manga.mangaapp.mangaedenclient;

import java.util.Arrays;
import java.util.Comparator;

import manga.mangaapp.mangaedenclient.internal.Cast;


/**
 *
 */
public class ChapterDetails {

  private final Object[][] images;

  public ChapterDetails(Object[][] images) {
    this.images = images;
  }

  public ChapterPage[] getPages() {
    ChapterPage[] result = new ChapterPage[images.length];
    for (int i = 0; i < images.length; i++) {
      Object[] image = images[i];
      result[i] = new ChapterPage(Cast.double2int(image[0]), (String) image[1],
        Cast.double2int(image[2]), Cast.double2int(image[3]));
    }

    Arrays.sort(result, new Comparator<ChapterPage>() {
      @Override
      public int compare(ChapterPage o1, ChapterPage o2) {
        return o1.getNumber() - o2.getNumber();
      }
    });

    return result;
  }
}
