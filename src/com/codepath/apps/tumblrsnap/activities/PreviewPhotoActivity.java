
package com.codepath.apps.tumblrsnap.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.codepath.apps.tumblrsnap.ImageFilterProcessor;
import com.codepath.apps.tumblrsnap.R;
import com.codepath.apps.tumblrsnap.TumblrClient;
import com.codepath.apps.tumblrsnap.models.User;
import com.codepath.libraries.androidviewhelpers.SimpleProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PreviewPhotoActivity extends FragmentActivity {
    private Bitmap photoBitmap;
    private Bitmap processedBitmap;
    private SimpleProgressDialog dialog;
    private ImageView ivPreview;
    private ImageFilterProcessor filterProcessor;

    private ImageView ivOriginal;
    private ImageView ivBlur;
    private ImageView ivGrayscale;
    private ImageView ivCrystallize;
    private ImageView ivSolarize;
    private ImageView ivGlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);
        ivPreview = (ImageView) findViewById(R.id.ivPreview);
        photoBitmap = getIntent().getParcelableExtra("photo_bitmap");
        filterProcessor = new ImageFilterProcessor(photoBitmap);
        redisplayPreview(ImageFilterProcessor.NONE);

        setupFilterImages();
    }

    private void redisplayPreview(int effectId) {
        processedBitmap = filterProcessor.applyFilter(effectId);
        ivPreview.setImageBitmap(processedBitmap);
    }

    private void setupFilterImages() {
        ivOriginal = (ImageView) findViewById(R.id.ivOriginal);
        ivBlur = (ImageView) findViewById(R.id.ivBlur);
        ivGrayscale = (ImageView) findViewById(R.id.ivGrayscale);
        ivCrystallize = (ImageView) findViewById(R.id.ivCrystallize);
        ivSolarize = (ImageView) findViewById(R.id.ivSolarize);
        ivGlow = (ImageView) findViewById(R.id.ivGlow);

        ivOriginal.setImageBitmap(filterProcessor.applyFilter(ImageFilterProcessor.NONE));
        ivBlur.setImageBitmap(filterProcessor.applyFilter(ImageFilterProcessor.BLUR));
        ivGrayscale.setImageBitmap(filterProcessor.applyFilter(ImageFilterProcessor.GRAYSCALE));
        ivCrystallize.setImageBitmap(filterProcessor.applyFilter(ImageFilterProcessor.CRYSTALLIZE));
        ivSolarize.setImageBitmap(filterProcessor.applyFilter(ImageFilterProcessor.SOLARIZE));
        ivGlow.setImageBitmap(filterProcessor.applyFilter(ImageFilterProcessor.GLOW));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preview_photo, menu);
        return true;
    }

    public void onSaveButton(MenuItem menuItem) {
        dialog = SimpleProgressDialog.build(this);
        dialog.show();

        TumblrClient client = ((TumblrClient) TumblrClient.getInstance(TumblrClient.class, this));
        client.createPhotoPost(User.currentUser().getBlogHostname(), processedBitmap,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, String arg1) {
                        dialog.dismiss();
                        PreviewPhotoActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Throwable arg0, String arg1) {
                        dialog.dismiss();
                    }
                });
    }

    public void onChangeFilter(View v) {
        int effectId = 0;

        switch (v.getId()) {
            case R.id.ivOriginal:
                effectId = ImageFilterProcessor.NONE;
                break;
            case R.id.ivBlur:
                effectId = ImageFilterProcessor.BLUR;
                break;
            case R.id.ivGrayscale:
                effectId = ImageFilterProcessor.GRAYSCALE;
                break;
            case R.id.ivCrystallize:
                effectId = ImageFilterProcessor.CRYSTALLIZE;
                break;
            case R.id.ivSolarize:
                effectId = ImageFilterProcessor.SOLARIZE;
                break;
            case R.id.ivGlow:
                effectId = ImageFilterProcessor.GLOW;
                break;
            default:
                effectId = ImageFilterProcessor.NONE;
                break;
        }

        redisplayPreview(effectId);
    }
}
