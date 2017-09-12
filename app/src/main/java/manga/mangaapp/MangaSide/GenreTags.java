package manga.mangaapp.MangaSide;

/**
 * Created by Jacob on 8/24/17.
 */

public class GenreTags {

    public enum Sources {

        MANGAEDEN("MangaEden"),
        TAPASTIC("Tapastic"),
        READMANGATODAY("ReadMangaToday"),
        MANGAREADER("MangaReader"),
        MANGAPANDA("MangaPanda"),
        MANGAJOY("MangaJoy"),
        MANGAHERE("MangaHere"),
        MANGAFOX("MangaFox"),
        LINEWEBTOON("LINEWebtoon"),
        KISSMANGA("KissManga");

        public String source;

        Sources(String source) {
            this.source = source;
        }

        @Override
        public String toString() {
            return source;
        }

        public boolean equals(Sources other) {
            return source.equals(other.source);
        }

        public static Sources fromString(String source) {
            for (Sources b : Sources.values()) {
                if (b.source.equalsIgnoreCase(source)) {
                    return b;
                }
            }
            return null;
        }

    }

    public enum Tags {

        ACTION("Action"),
        ADULT("Adult"),
        ADVENTURE("Adventure"),
        COMEDY("Comedy"),
        DOUJINSHI("Doujinshi"),
        DRAMA("Drama"),
        ECCHI("Ecchi"),
        FANTASY("Fantasy"),
        GENDER_BENDER("Gender Bender"),
        HAREM("Harem"),
        HISTORICAL("Historical"),
        HORROR("Horro"),
        JOSEI("Josei"),
        MARTIAL_ARTS("Martial Arts"),
        MATURE("Mature"),
        MECHA("Mecha"),
        MYSTERY("Mystery"),
        ONE_SHOT("One Shot"),
        PSYCHOLOGICAL("Psychological"),
        ROMANCE("Romance"),
        SCHOOL_LIFE("School Life"),
        SCI_FI("Sci-fi"),
        SEINEN("Seinen"),
        SHOUJO("Shoujo"),
        SHOUNEN("Shounen"),
        SLICE_OF_LIFE("Slife of Life"),
        SMUT("Smut"),
        SPORTS("Sports"),
        SUPERNATURAL("Supernatural"),
        TRAGEDY("Tragedy"),
        WEBTOONS("Webtoons"),
        YAOI("Yaoi"),
        YURI("Yuri");

        public String tag;

        Tags(String tag) {
            this.tag = tag;
        }

        @Override
        public String toString() {
            return tag;
        }

        public static Tags fromString(String tags) {
            for (Tags b : Tags.values()) {
                if (b.tag.equalsIgnoreCase(tags)) {
                    return b;
                }
            }
            return null;
        }
    }


    Tags tags;

    public GenreTags(Tags tags) {
        this.tags = tags;
    }

    public Tags getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return tags.toString();
    }


    public static String[] getAllTags() {
        return new String[]{
                Tags.ACTION.tag,
                Tags.ADULT.tag,
                Tags.ADVENTURE.tag,
                Tags.COMEDY.tag,
                Tags.DOUJINSHI.tag,
                Tags.DRAMA.tag,
                Tags.ECCHI.tag,
                Tags.FANTASY.tag,
                Tags.GENDER_BENDER.tag,
                Tags.HAREM.tag,
                Tags.HISTORICAL.tag,
                Tags.HORROR.tag,
                Tags.JOSEI.tag,
                Tags.MARTIAL_ARTS.tag,
                Tags.MATURE.tag,
                Tags.MECHA.tag,
                Tags.MYSTERY.tag,
                Tags.ONE_SHOT.tag,
                Tags.PSYCHOLOGICAL.tag,
                Tags.ROMANCE.tag,
                Tags.SCHOOL_LIFE.tag,
                Tags.SCI_FI.tag,
                Tags.SEINEN.tag,
                Tags.SHOUJO.tag,
                Tags.SHOUNEN.tag,
                Tags.SLICE_OF_LIFE.tag,
                Tags.SMUT.tag,
                Tags.SPORTS.tag,
                Tags.SUPERNATURAL.tag,
                Tags.TRAGEDY.tag,
                Tags.WEBTOONS.tag,
                Tags.YAOI.tag,
                Tags.YURI.tag
        };
    }

    public static String[] getAllSources() {
        return new String[]{
                Sources.KISSMANGA.source,
                Sources.LINEWEBTOON.source,
                Sources.MANGAEDEN.source,
                Sources.MANGAFOX.source,
                Sources.MANGAHERE.source,
                Sources.MANGAJOY.source,
                Sources.MANGAPANDA.source,
                Sources.MANGAREADER.source,
                Sources.READMANGATODAY.source,
                Sources.TAPASTIC.source
        };
    }

}
