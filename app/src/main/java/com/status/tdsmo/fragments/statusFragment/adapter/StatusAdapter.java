package com.status.tdsmo.fragments.statusFragment.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.status.tdsmo.R;
import com.status.tdsmo.ViewStatusActivity;
import com.status.tdsmo.adapters.StatusListAdapter;
import com.status.tdsmo.common.Common;
import com.status.tdsmo.databinding.AdUnifiedBinding;
import com.status.tdsmo.databinding.ItemAdmobNativeAdsBinding;
import com.status.tdsmo.databinding.StatusListBinding;
import com.status.tdsmo.models.Image;

import java.util.ArrayList;
import java.util.List;

public class StatusAdapter extends PagedListAdapter<Image, RecyclerView.ViewHolder> {
    private Context context;
    private InterstitialAd mInterstitialAd;
    private List<Image>getlist=new ArrayList<>();

    public StatusAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
        if (Common.interstitial != null) {
            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(Common.interstitial);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                StatusListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.status_list, parent, false);
                return new StatusViewHolder(binding);
            }
            case 6: {
                ItemAdmobNativeAdsBinding admobNativeAdsBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_admob_native_ads, parent, false);
                return new AdmobNativeHolder(admobNativeAdsBinding);
            }
            default:{
                StatusListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.status_list, parent, false);
                return new StatusViewHolder(binding);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:{
                ((StatusViewHolder) holder).OnBindView(getItem(position),position);
                break;
            }
            case 6:{
                final AdmobNativeHolder holder_admob = (AdmobNativeHolder) holder;
                holder_admob.adLoader.loadAd(new AdRequest.Builder().build());
                break;
            }
        }

    }
    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (getItem(position).getViewType()==6) {
            type = 6;
        }
        return type;
    }


    public class StatusViewHolder extends RecyclerView.ViewHolder {
        private StatusListBinding binding;

        public StatusViewHolder(StatusListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void OnBindView(final Image item, final int position) {
            binding.status.setText(item.getStatus());
            binding.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewStatusActivity.class);
                    Common.statusItem = item;
                    Common.position = position;
                    //context.startActivity(intent);
                }
            });


            if (position % 2 == 0) {
                binding.root.setCardBackgroundColor(context.getResources().getColor(R.color.colorOne));
            } else {
                binding.root.setCardBackgroundColor(context.getResources().getColor(R.color.colorThree));
            }

            binding.fabShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, binding.status.getText().toString());
                    shareIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(shareIntent, "Share app via"));
                }
            });

            binding.fabWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, binding.status.getText().toString());
                    shareIntent.setType("text/plain");
                    shareIntent.setPackage("com.whatsapp");
                    context.startActivity(Intent.createChooser(shareIntent, "Share app via"));
                }
            });

            binding.fabInstagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, binding.status.getText().toString());
                    shareIntent.setType("text/plain");
                    shareIntent.setPackage("com.instagram.android");
                    context.startActivity(Intent.createChooser(shareIntent, "Share app via"));
                }
            });


            binding.fabCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (Common.interstitial != null) {
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                        }
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("text", binding.status.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    public class AdmobNativeHolder extends RecyclerView.ViewHolder {
        public final AdLoader adLoader;
        private UnifiedNativeAd nativeAd;
        private ItemAdmobNativeAdsBinding admobNativeAdsBinding;

        public AdmobNativeHolder(@NonNull final ItemAdmobNativeAdsBinding admobNativeAdsBinding) {
            super(admobNativeAdsBinding.getRoot());
            this.admobNativeAdsBinding=admobNativeAdsBinding;
            AdLoader.Builder builder = new AdLoader.Builder(context, "ca-app-pub-3940256099942544/1044960115");
            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                // OnUnifiedNativeAdLoadedListener implementation.
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity)context).getLayoutInflater().inflate(R.layout.ad_unified, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    //admobNativeAdsBinding.flAdplaceholder.removeAllViews();
                    admobNativeAdsBinding.flAdplaceholder.addView(adView);
                }
            });
            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
            builder.withNativeAdOptions(adOptions);
            this.adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                }
            }).build();

        }
    }

    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(R.id.ad_media);

        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
        VideoController vc = nativeAd.getVideoController();
        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        } else {

        }
    }

    private static DiffUtil.ItemCallback<Image> DIFF_CALLBACK = new DiffUtil.ItemCallback<Image>() {

        @Override
        public boolean areItemsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
            return true;
        }
    };
}