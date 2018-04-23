// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.samples.mycustommediaviewvideorenderer;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.MediaViewVideoRenderer;
import com.facebook.ads.NativeAd;
import com.facebook.ads.VideoStartReason;

public class CustomMediaViewVideoRendererFragment extends Fragment implements AdListener {


    private static final String TAG = CustomMediaViewVideoRendererFragment.class.getSimpleName();
    private static final int IMAGE_HEIGHT = 180;
    private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

    private ViewGroup mMediaViewContainer;
    private NativeAd mNativeAd;
    private MyVideoRenderer myVideoRenderer;
    private MediaView mMediaView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.fragment_native_ad_custom_video_renderer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.reload_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });

        mMediaViewContainer = view.findViewById(R.id.media_view_container);
        mMediaView = new MediaView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (int)(IMAGE_HEIGHT * DENSITY));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mMediaView.setLayoutParams(layoutParams);
        myVideoRenderer = new MyVideoRenderer(getContext());
        mMediaView.setVideoRenderer(myVideoRenderer);
        mMediaViewContainer.addView(mMediaView);

        loadAd();
    }

    private void loadAd() {
        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();

        mNativeAd = new NativeAd(getContext(),
                "YOUR_PLACEMENT_ID");

        // Set a listener to get notified when the ad was loaded.
        mNativeAd.setAdListener(this);

        // Initiate a request to load an ad.
        mNativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);

    }

    @Override
    public void onError(Ad ad, AdError error) {
        Toast.makeText(
                getContext(),
                "Ad failed to load: " + error.getErrorMessage(),
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        mMediaView.setNativeAd(mNativeAd);
        myVideoRenderer.play(VideoStartReason.USER_STARTED);
        mNativeAd.registerViewForInteraction(mMediaViewContainer);
    }

    @Override
    public void onAdClicked(Ad ad) {
        Toast.makeText(getContext(), "Ad Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        Log.d(TAG, "onLoggingImpression");
    }

    private class MyVideoRenderer extends MediaViewVideoRenderer {

        public MyVideoRenderer(Context context) {
            super(context);
        }

    }

}
