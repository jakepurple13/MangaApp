package manga.mangaapp.MangaSide;

/**
 * Created by Jacob on 9/14/17.
 */

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
import java.util.List;

import manga.mangaapp.R;
import manga.mangaapp.mangaedenclient.ChapterPage;
import manga.mangaapp.mangaedenclient.MangaEden;
import manga.mangaapp.manymanga.data.Chapter;
import manga.mangaapp.manymanga.data.Image;

/**
 * Created by Jacob on 8/24/17.
 */

public class MangaPageSite extends PagerAdapter {

    List<Image> pages;
    Activity activity;

    public MangaPageSite(List<Image> pages, Activity activity) {
        this.pages = pages;
        this.activity = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = LayoutInflater.from(activity).inflate(R.layout.manga_page_layout, container, false);

        final ImageView iv = view.findViewById(R.id.manga_page);

        final String imageUrl = pages.get(position).getLink();
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
        return pages.size();
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
