# GyroscopeImageDemo

在使用`GyroscopeImageView`控件时，需要告知我们控件的宽高，这样我们能合理地对图像尺寸做处理:
```java
//例如用 Picasso加载图像 可将控件的宽高传入自定义的 TransFormation 来处理图像大小
Picasso.get()
    .load(picUrl)
    .transform(new GyroscopeTransFormation(width, height))
    .into(gyroscopeImageView);
```

在相关`Activity`的`onResume`和`onPause`中对陀螺仪传感器进行注册和注销监听:
```java
  @Override protected void onResume() {
    super.onResume();
    //利用我们的 GyroscopeManager 来注册传感器监听
    GyroscopeManager.getInstance().register(this);
  }

  @Override protected void onPause() {
    super.onPause();
    //利用我们的 GyroscopeManager 来注销监听
    GyroscopeManager.getInstance().unregister(this);
  }
```

         
平滑转场:
```java
CoverActivity.startActivityWithAnimation(ImageMoveActivity.this, PIC_URL, gyroscopeImageView);
```
