package ee.promobox.promoboxandroid.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.util.List;

import ee.promobox.promoboxandroid.R;
import ee.promobox.promoboxandroid.widgets.MarqueeTextView;


public class FragmentRSS extends Fragment {

    public static final String TAG = "FragmentRSS";

    private static final String ARG_URL = "rss_url";
    public static String RSS_URL = "http://feeds.reuters.com/reuters/topNews";
//    public static String RSS_URL = "";

    private StringBuilder stringBuilder;
    private TextView tvRSSfeed;
    private View rssFeedContainer;

    private boolean rssShowing = false;

    public static FragmentRSS newInstance(String rssURL) {

        Bundle args = new Bundle();
        args.putString(ARG_URL, rssURL);

        FragmentRSS fragment = new FragmentRSS();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_rss_feed, container, false);
        getBundleArgs();
        getView(view);
        return view;
    }

    /**
     * Inits views
     *
     * @param view - Base View
     */
    private void getView(View view) {
        stringBuilder = new StringBuilder();
        tvRSSfeed = (MarqueeTextView) view.findViewById(R.id.tv_rss_feed);
        rssFeedContainer = getActivity().findViewById(R.id.content_rss);
        downloadAndParseRSS(RSS_URL);
    }

    /**
     * Gets args from Bundle
     */
    private void getBundleArgs() {
        if (getArguments() != null) {
            if (!TextUtils.isEmpty(getArguments().getString(ARG_URL))) {
                RSS_URL = getArguments().getString(ARG_URL);
            }
        }
    }

    /**
     * Downloads and parses RSS url to objects and creates a rss string
     * via StringBuilder. Call this method when need to append new data to string
     * builder.
     *
     * @param url - RSS url
     */
    public void downloadAndParseRSS(String url) {

        if (!TextUtils.isEmpty(url)) {
            PkRSS.with(getActivity()).load(url).callback(new Callback() {
                @Override
                public void onPreload() {
                    Log.d(TAG, "getRSSFeed :: onPreload");
                }

                @Override
                public void onLoaded(List<Article> newArticles) {
                    if (newArticles != null) {
                        for (Article a : newArticles) {
                            stringBuilder.append(a.getTitle()).append(" : ").append(a.getDescription()).append(" ");
                        }
                        displayRssString(stringBuilder.toString());
                        Log.d(TAG, "getRSSFeed :: onLoaded");
                    }
                }

                @Override
                public void onLoadFailed() {
                    Log.d(TAG, "getRSSFeed :: onLoadFailed");
                }
            }).async();
        } else {
            Log.e(TAG, "invalid RSS url");
        }

    }

    /**
     * Displays rss String
     *
     * @param rssString - rss String
     */
    private void displayRssString(final String rssString) {
        if (!rssShowing) {
            if (!TextUtils.isEmpty(rssString)) {
                rssShowing = true;
                rssFeedContainer.setVisibility(View.VISIBLE);
                tvRSSfeed.setText(rssString);
                tvRSSfeed.setPressed(true);
                tvRSSfeed.setSelected(true);
            }
        } else {
            if (stringBuilder != null) {
                if (TextUtils.isEmpty(stringBuilder.toString())) {
                    rssFeedContainer.setVisibility(View.GONE);
                }
            }
        }
    }

}
