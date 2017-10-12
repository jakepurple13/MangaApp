package manga.mangaapp.MangaSide;

import manga.mangaapp.R;
import manga.mangaapp.StoryWorld;

/**
 * Created by Jacob on 8/24/17.
 */

public class GenreTags {

    public enum Sources {

        MANGAEDEN("MangaEden", "me"),
        TAPASTIC("Tapastic", "ts"),
        READMANGATODAY("ReadMangaToday", "rmt"),
        MANGAREADER("MangaReader", "mr"),
        MANGAPANDA("MangaPanda", "mp"),
        MANGAJOY("MangaJoy", "mj"),
        MANGAHERE("MangaHere", "mh"),
        MANGAFOX("MangaFox", "mf"),
        LINEWEBTOON("LINEWebtoon", "lw"),
        KISSMANGA("KissManga", "km");

        public String source;
        public String shortcut;

        Sources(String source) {
            this.source = source;
            this.shortcut = "";
        }

        Sources(String source, String shortcut) {
            this.source = source;
            this.shortcut = shortcut;
        }

        @Override
        public String toString() {
            return source;
        }

        public boolean equals(Sources other) {
            return source.equals(other.source);// && shortcut.equals(other.shortcut);
        }

        public static Sources fromString(String source) {
            for (Sources b : Sources.values()) {
                if (b.source.equalsIgnoreCase(source)) {
                    return b;
                } else if(b.shortcut.equalsIgnoreCase(source)) {
                    return b;
                }
            }
            return null;
        }

    }

    public enum Tags {

        ACTION(StoryWorld.getResource(R.string.action)),
        ADULT(StoryWorld.getResource(R.string.adult)),
        ADVENTURE(StoryWorld.getResource(R.string.adventure)),
        COMEDY(StoryWorld.getResource(R.string.comedy)),
        DOUJINSHI(StoryWorld.getResource(R.string.doujinshi)),
        DRAMA(StoryWorld.getResource(R.string.drama)),
        ECCHI(StoryWorld.getResource(R.string.ecchi)),
        FANTASY(StoryWorld.getResource(R.string.fantasy)),
        GENDER_BENDER(StoryWorld.getResource(R.string.gender_bender)),
        HAREM(StoryWorld.getResource(R.string.harem)),
        HISTORICAL(StoryWorld.getResource(R.string.historical)),
        HORROR(StoryWorld.getResource(R.string.horror)),
        JOSEI(StoryWorld.getResource(R.string.josei)),
        MARTIAL_ARTS(StoryWorld.getResource(R.string.martial_arts)),
        MATURE(StoryWorld.getResource(R.string.mature)),
        MECHA(StoryWorld.getResource(R.string.mecha)),
        MYSTERY(StoryWorld.getResource(R.string.mystery)),
        ONE_SHOT(StoryWorld.getResource(R.string.one_shot)),
        PSYCHOLOGICAL(StoryWorld.getResource(R.string.psychological)),
        ROMANCE(StoryWorld.getResource(R.string.romance)),
        SCHOOL_LIFE(StoryWorld.getResource(R.string.school_life)),
        SCI_FI(StoryWorld.getResource(R.string.sci_fi)),
        SEINEN(StoryWorld.getResource(R.string.seinen)),
        SHOUJO(StoryWorld.getResource(R.string.shoujo)),
        SHOUNEN(StoryWorld.getResource(R.string.shounen)),
        SLICE_OF_LIFE(StoryWorld.getResource(R.string.slice_of_life)),
        SMUT(StoryWorld.getResource(R.string.smut)),
        SPORTS(StoryWorld.getResource(R.string.sports)),
        SUPERNATURAL(StoryWorld.getResource(R.string.supernatural)),
        TRAGEDY(StoryWorld.getResource(R.string.tragedy)),
        WEBTOONS(StoryWorld.getResource(R.string.webtoons)),
        YAOI(StoryWorld.getResource(R.string.yaoi)),
        YURI(StoryWorld.getResource(R.string.yuri));

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
                //Sources.KISSMANGA.source,
                Sources.LINEWEBTOON.source,
                Sources.MANGAEDEN.source,
                //Sources.MANGAFOX.source,
                Sources.MANGAHERE.source,
                //Sources.MANGAJOY.source,
                Sources.MANGAPANDA.source,
                Sources.MANGAREADER.source,
                Sources.READMANGATODAY.source,
                //Sources.TAPASTIC.source
        };
    }

}
