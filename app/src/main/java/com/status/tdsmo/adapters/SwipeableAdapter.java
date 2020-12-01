package com.status.tdsmo.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.status.tdsmo.BuildConfig;
import com.status.tdsmo.R;
import com.status.tdsmo.common.Common;
import com.status.tdsmo.models.Image;
import com.status.tdsmo.utils.BitmapUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.status.tdsmo.common.Common.isShare;
import static com.status.tdsmo.common.Common.isShareFb;
import static com.status.tdsmo.common.Common.isShareInstagram;
import static com.status.tdsmo.common.Common.isShareWhatsapp;

/**
 * This app is developed by Sagar Khawse
 * <p>
 * Contact this developer at gmail - sagar.khawse@gmail.com
 * Contact Number :- +917385663427
 * fiverr profile :- {@link "https://www.fiverr.com/s2/c1746e55d6"}
 * <p>
 * Date : - 6 march 2020
 */
public class SwipeableAdapter extends RecyclerView.Adapter<SwipeableAdapter.ImageViewholder> {
    Context mContext;
    List<Image> mList;
    private ProgressDialog progressDialog;
    private Dialog dialog;

    private InterstitialAd mInterstitialAd;
    private String dp;


    public SwipeableAdapter(Context context, List<Image> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ImageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dp_swipe_item, parent, false);
        progressDialog = new ProgressDialog(mContext);
        MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(mContext);

        mInterstitialAd.setAdUnitId(Common.interstitial);
//
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        return new ImageViewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewholder holder, int position) {
        final Image image = mList.get(position);

//
//if (position % 4 == 0){
//
//    mInterstitialAd.loadAd(new AdRequest.Builder().build());
//    if (mInterstitialAd.isLoaded()){
//        mInterstitialAd.show();
//    }
//}


        try {
            Glide.with(mContext).load(image.getImage())
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                    .into(holder.img);
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: " + e.getMessage());
        }


        holder.fab_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                downloadingImage(image.getImage());

            }
        });


        holder.fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Image Share");
                progressDialog.setMessage("please wait...");
                progressDialog.show();
                sharingImage(image.getImage());
                isShare = true;


            }
        });

        holder.fab_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Image sharing to facebook");
                progressDialog.setMessage("please wait...");
                progressDialog.show();
                isShareFb = true;
                isShare = true;

                sharingImage(image.getImage());

            }
        });

        holder.fab_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Image sharing to whatsapp");
                progressDialog.setMessage("please wait...");
                progressDialog.show();
                isShare = true;

                isShareWhatsapp = true;
                sharingImage(image.getImage());
            }
        });

        holder.fab_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Image sharing to instagram");
                progressDialog.setMessage("please wait...");
                progressDialog.show();
                isShare = true;
                isShareInstagram = true;
                sharingImage(image.getImage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class ImageViewholder extends RecyclerView.ViewHolder {
        ImageView fab_download, fab_share, fab_whatsapp, fab_fb, fab_instagram;
        ImageView img;

        public ImageViewholder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.imageView);
            fab_download = itemView.findViewById(R.id.fab_download);
            fab_share = itemView.findViewById(R.id.fab_share);
            fab_whatsapp = itemView.findViewById(R.id.fab_whatsapp);
            fab_fb = itemView.findViewById(R.id.fab_fb);
            fab_instagram = itemView.findViewById(R.id.fab_instagram);
        }
    }

    private void sharingImage(String img) {

        new DownloadTask().execute(img);
    }

    private void downloadingImage(String image) {
        new DownloadTask().execute(image);
    }


    @SuppressLint("StaticFieldLeak")
    public class DownloadTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        String image_path;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Download in progress...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String path = strings[0];
            int file_length = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();
                File new_folder = new File(Environment.getExternalStorageDirectory() + "/DpStatus");
                if (!new_folder.exists()) {
                    new_folder.mkdir();
                }
                dp = String.valueOf(System.currentTimeMillis());
                image_path = new_folder + "/" + "Dp_" + dp + ".jpg";

                File input_file = new File(image_path);
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;

                OutputStream outputStream = new FileOutputStream(input_file);
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    outputStream.write(data, 0, count);
                    int progress = (int) total * 100 / file_length;
                    publishProgress(progress);
                }
                inputStream.close();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download Completed";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();
            MediaScannerConnection.scanFile(mContext
                    ,
                    new String[]{Environment.getExternalStorageDirectory() + "/DpStatus/" + "Dp_" + dp + ".jpg"},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {

                        public void onScanCompleted(String path, Uri uri) {

                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
            if (isShare) {
                shareContent(image_path);
                Toast.makeText(mContext, "Sharing please wait...", Toast.LENGTH_SHORT).show();
            } else {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void shareContent(String path) {
        File file = new File(path);
        Uri imageUri = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing via Dp and Status app , Download link :- https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        progressDialog.dismiss();
        if (isShareWhatsapp) {

            shareIntent.setPackage("com.whatsapp");
            mContext.startActivity(Intent.createChooser(shareIntent, "send"));
            flaseAllValue();

        } else if (isShareFb) {
            shareIntent.setPackage("com.facebook.katana");
            mContext.startActivity(Intent.createChooser(shareIntent, "send"));
            flaseAllValue();
        } else if (isShareInstagram) {
            shareIntent.setPackage("com.instagram.android");
            mContext.startActivity(Intent.createChooser(shareIntent, "send"));
            flaseAllValue();
        } else {
            mContext.startActivity(Intent.createChooser(shareIntent, "send"));
            flaseAllValue();
        }

    }

    private void flaseAllValue() {
        isShareInstagram = false;
        isShare = false;
        isShareWhatsapp = false;
        isShareFb = false;
    }


}
