package manga.mangaapp;

/**
 * Created by Jacob on 9/9/17.
 */

public enum Layouts {

    STAGGERED_THUMBNAILS("Staggered Thumbnails"),THUMBNAILS("Thumbnails"),DETAILS("Details");

    public String source;

    Layouts(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return source;
    }

    public boolean equals(Layouts other) {
        return source.equals(other.source);
    }

    public static Layouts fromString(String source) {
        for (Layouts b : Layouts.values()) {
            if (b.source.equalsIgnoreCase(source)) {
                return b;
            }
        }
        return null;
    }

}
