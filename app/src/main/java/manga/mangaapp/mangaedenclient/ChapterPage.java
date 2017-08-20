package manga.mangaapp.mangaedenclient;

import java.net.URI;

/**
 *
 */
public class ChapterPage {

  private final int number;
  private final String image;
  private final int width;
  private final int height;

  public ChapterPage(int number, String image, int width, int height) {
    this.number = number;
    this.image = image;
    this.width = width;
    this.height = height;
  }

  public int getNumber() {
    return number;
  }

  public String getImage() {
    return image;
  }

  public URI getImageURI() {
    return MangaEden.chapterPage2ImageURI(this);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ChapterPage{");
    sb.append("number=").append(number);
    sb.append(", image='").append(image).append('\'');
    sb.append(", width=").append(width);
    sb.append(", height=").append(height);
    sb.append('}');
    return sb.toString();
  }
}
