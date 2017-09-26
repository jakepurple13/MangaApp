package manga.mangaapp.MangaSide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
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
    View.OnClickListener onClickListener;
    View.OnClickListener menuShow;

    public MangaPage(ChapterPage[] pages, Activity activity, View.OnClickListener endClick, View.OnClickListener menuShow) {
        this.pages = pages;
        this.activity = activity;
        this.onClickListener = endClick;
        this.menuShow = menuShow;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final View view;

        if(position>=pages.length) {
            view = LayoutInflater.from(activity).inflate(R.layout.manga_page_start_chapter, container, false);

            TextView tv = view.findViewById(R.id.placeholder_page);

            Button startNext = view.findViewById(R.id.start_next);

            startNext.setOnClickListener(onClickListener);

        } else {

            view = LayoutInflater.from(activity).inflate(R.layout.manga_page_layout, container, false);

            final ImageView iv = view.findViewById(R.id.manga_page);
            iv.setOnTouchListener(new ImageMatrixTouchHandler(view.getContext()));

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

            //iv.setOnClickListener(menuShow);

        }

        container.addView(view);

        return view;
        //return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return pages.length+1;
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
