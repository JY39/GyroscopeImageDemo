package com.example.jiangye.demo.gyroscope;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 创建时间: 2018/01/12 12:13 <br>
 * 作者: jiangye <br>
 * 描述: 可随陀螺仪滑动的自定义 ImageView 控件
 */

public class GyroscopeImageView extends ImageView {

  private double mScaleX;
  private double mScaleY;
  private float mLenX;
  private float mLenY;
  protected double mAngleX;
  protected double mAngleY;
  private float mOffsetX;
  private float mOffsetY;

  public GyroscopeImageView(Context context) {
    super(context);
    init();
  }

  public GyroscopeImageView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public GyroscopeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setScaleType(ScaleType.CENTER);
  }

  @Override public void setScaleType(ScaleType scaleType) {
    super.setScaleType(ScaleType.CENTER);
  }

  public float getOffsetX() {
    return mOffsetX;
  }

  public float getOffsetY() {
    return mOffsetY;
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    GyroscopeManager gyroscopeManager = GyroscopeManager.getInstance();
    if (gyroscopeManager != null) {
      gyroscopeManager.addView(this);
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    GyroscopeManager gyroscopeManager = GyroscopeManager.getInstance();
    if (gyroscopeManager != null) {
      gyroscopeManager.removeView(this);
    }
  }

  public void update(double scaleX, double scaleY) {
    mScaleX = scaleX;
    mScaleY = scaleY;
    invalidate();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
    int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
    if (getDrawable() != null) {
      int drawableWidth = getDrawable().getIntrinsicWidth();
      int drawableHeight = getDrawable().getIntrinsicHeight();
      mLenX = Math.abs((drawableWidth - width) * 0.5f);
      mLenY = Math.abs((drawableHeight - height) * 0.5f);
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    if (getDrawable() == null || isInEditMode()) {
      super.onDraw(canvas);
      return;
    }
    mOffsetX = (float) (mLenX * mScaleX);
    mOffsetY = (float) (mLenY * mScaleY);
    canvas.save();
    canvas.translate(mOffsetX, mOffsetY);
    super.onDraw(canvas);
    canvas.restore();
  }

  @Override public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override public int hashCode() {
    return super.hashCode();
  }
}
