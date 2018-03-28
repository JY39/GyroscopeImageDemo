package com.example.jiangye.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.AutoTransition;
import android.transition.ChangeImageTransform;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.jiangye.demo.R;
import com.example.jiangye.demo.gyroscope.GyroscopeImageView;
import com.example.jiangye.demo.gyroscope.GyroscopeTransFormation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class CoverActivity extends Activity {

  private static final String COVER_URL = "cover_url";
  private static final String OFFSET_X = "OFFSET_X";
  private static final String OFFSET_Y = "OFFSET_Y";
  private static final long TRANSITIONS_DURATION = 1000L;   //做动画的时长

  /**
   * 屏幕宽度和高度
   */
  private int mScreenWidth;
  private int mScreenHeight;

  /**
   * 上一个页面 GyroscopeImageView 的宽度和高度
   */
  private int mOriginWidth;
  private int mOriginHeight;

  /**
   * Loading 覆盖图
   */
  private ImageView mIvCover;
  private String mCoverUrl;

  /**
   * 用于做 Transitions 动画的 ViewGroup
   */
  private ViewGroup mTransitionsContainer;

  /**
   * @param cover_url Loading 图 url
   * @param gyroscopeImageView 想要做动画的 GyroscopeImageView
   */
  public static void startActivityWithAnimation(final Activity activity, String cover_url,
      GyroscopeImageView gyroscopeImageView) {
    final Intent intent = new Intent(activity, CoverActivity.class);
    intent.putExtra(COVER_URL, cover_url);
    // 获取控件位置信息
    final Rect rect = new Rect();
    gyroscopeImageView.getGlobalVisibleRect(rect);
    intent.setSourceBounds(rect);
    intent.putExtra(OFFSET_X, gyroscopeImageView.getOffsetX());
    intent.putExtra(OFFSET_Y, gyroscopeImageView.getOffsetY());
    activity.startActivity(intent);
    activity.overridePendingTransition(0, 0);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cover);
    mCoverUrl = getIntent().getStringExtra(COVER_URL);
    mIvCover = findViewById(R.id.iv_cover);
    mTransitionsContainer = findViewById(R.id.container_layout);
    initial();
  }

  /**
   * 设置动画最初场景
   */
  private void initial() {
    // 获取上一个界面传入的控件位置信息
    Rect rect = getIntent().getSourceBounds();

    if (rect != null) {
      // 获取上一个界面中，控件的宽度和高度
      mOriginWidth = rect.right - rect.left;
      mOriginHeight = rect.bottom - rect.top;

      // 设置 ImageView 的位置，使其和上一个界面中图片的位置重合
      FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mOriginWidth, mOriginHeight);
      params.setMargins(rect.left, rect.top, rect.right, rect.bottom);
      mIvCover.setLayoutParams(params);
      mIvCover.setScaleType(ImageView.ScaleType.MATRIX);

      //拿到上个界面图片的偏移量
      final float offsetX = getIntent().getFloatExtra(OFFSET_X, 0);
      final float offsetY = getIntent().getFloatExtra(OFFSET_Y, 0);

      Picasso.get()
          .load(mCoverUrl)
          .transform(new GyroscopeTransFormation(mOriginWidth, mOriginHeight))
          .into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
              mIvCover.setImageBitmap(bitmap);
              Matrix matrix = mIvCover.getImageMatrix();
              int width = bitmap.getWidth();
              int height = bitmap.getHeight();
              //保持上个界面图片的偏移量
              matrix.setTranslate((int) ((mOriginWidth - width) * 0.5f + 0.5f) + offsetX,
                  (int) ((mOriginHeight - height) * 0.5f + 0.5f) + offsetY);
              mIvCover.setImageMatrix(matrix);
            }

            @Override public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
          });

      //开始入场动画
      runEnterAnim();
    } else {
      //没有动画效果
      FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mScreenWidth, mScreenHeight);
      mIvCover.setLayoutParams(params);
      mIvCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
      Picasso.get().load(mCoverUrl).into(mIvCover);
    }
  }

  /**
   * 获取屏幕尺寸
   */
  private void getScreenSize() {
    mScreenWidth = getResources().getDisplayMetrics().widthPixels;
    mScreenHeight = getResources().getDisplayMetrics().heightPixels;
  }

  /**
   * 模拟入场动画
   */
  private void runEnterAnim() {
    getScreenSize();
    mIvCover.post(new Runnable() {
      @Override public void run() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mScreenWidth, mScreenHeight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          ChangeImageTransform changeImageTransform = new ChangeImageTransform();
          AutoTransition autoTransition = new AutoTransition();
          changeImageTransform.setDuration(TRANSITIONS_DURATION);
          autoTransition.setDuration(TRANSITIONS_DURATION);
          TransitionSet transitionSet = new TransitionSet();
          transitionSet.addTransition(autoTransition);
          transitionSet.addTransition(changeImageTransform);
          TransitionManager.beginDelayedTransition(mTransitionsContainer, transitionSet);
          mIvCover.setLayoutParams(params);
          mIvCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
          mIvCover.setLayoutParams(params);
          mIvCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
      }
    });
  }
}
