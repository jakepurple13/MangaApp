package manga.mangaapp.MangaSide;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URI;

import manga.mangaapp.R;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.MangaEden;

/**
 * Created by Jacob on 8/24/17.
 */

public class MangaPage extends PagerAdapter {

    ChapterPage[] pages;
    Activity activity;

    public MangaPage(ChapterPage[] pages, Activity activity) {
        this.pages = pages;
        this.activity = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = LayoutInflater.from(activity).inflate(R.layout.manga_page_layout, container, false);

        final ImageView iv = view.findViewById(R.id.manga_page);

        final URI imageUrl = pages[position].getImageURI();
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(activity)
                        .load(String.valueOf(imageUrl))
                        .placeholder(android.R.mipmap.sym_def_app_icon)
                        .into(iv);
            }
        });

        container.addView(view);

        return view;
        //return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return pages.length;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }

}
