package com.example.jiangye.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.jiangye.demo.R;
import com.example.jiangye.demo.adapter.GyroscopeImageAdapter;
import com.example.jiangye.demo.bean.ImageVo;
import com.example.jiangye.demo.gyroscope.GyroscopeImageView;
import com.example.jiangye.demo.gyroscope.GyroscopeManager;
import com.example.jiangye.demo.gyroscope.GyroscopeTransFormation;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ImageMoveActivity extends Activity {

  private static final String PIC1_URL =
      "http://wx2.sinaimg.cn/large/6e9ad2bdly1fnih8s6on4j21401401kz.jpg";
  private static final String PIC2_URL =
      "http://vrlab-public.ljcdn.com//release//vradmin//1000000020129136//images//FF41C450.png";
  private static final String PIC3_URL =
      "http://wx2.sinaimg.cn/large/6e9ad2bdly1fnih8uqgkuj2140140b2b.jpg";

  private GyroscopeImageView gyroscopeImageView;
  private List<ImageVo> mData = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_move);

    initView();
  }

  @Override protected void onResume() {
    super.onResume();
    GyroscopeManager.getInstance().register(this);
  }

  @Override protected void onPause() {
    super.onPause();
    GyroscopeManager.getInstance().unregister(this);
  }

  private void initView() {
    gyroscopeImageView = findViewById(R.id.head_image);

    gyroscopeImageView.post(new Runnable() {
      @Override public void run() {
        //获取控件大小，作为拉伸参数
        int width = gyroscopeImageView.getWidth();
        int height = gyroscopeImageView.getHeight();

        Picasso.get()
            .load(PIC3_URL)
            .transform(new GyroscopeTransFormation(width, height))
            .into(gyroscopeImageView);
      }
    });

    RecyclerView recyclerView = findViewById(R.id.rv_image_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    initData();
    final GyroscopeImageAdapter gyroscopeImageAdapter = new GyroscopeImageAdapter(this, mData);
    gyroscopeImageAdapter.setOnGvItemClickListener(
        new GyroscopeImageAdapter.OnGvItemClickListener() {
          @Override
          public void onGvItemClick(GyroscopeImageAdapter.ViewHolder holder, int position) {

            CoverActivity.startActivityWithAnimation(ImageMoveActivity.this,
                mData.get(position).mImageUrl, holder.gImg);
          }
        });
    recyclerView.setAdapter(gyroscopeImageAdapter);
    recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    gyroscopeImageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        CoverActivity.startActivityWithAnimation(ImageMoveActivity.this, PIC3_URL,
            gyroscopeImageView);
      }
    });
  }

  private void initData() {
    mData.add(new ImageVo(PIC1_URL, "1"));
    mData.add(new ImageVo(PIC2_URL, "2"));
    mData.add(new ImageVo(PIC1_URL, "3"));
    mData.add(new ImageVo(PIC2_URL, "4"));
    mData.add(new ImageVo(PIC1_URL, "5"));
    mData.add(new ImageVo(PIC2_URL, "6"));
    mData.add(new ImageVo(PIC1_URL, "7"));
    mData.add(new ImageVo(PIC2_URL, "8"));
    mData.add(new ImageVo(PIC1_URL, "9"));
  }
}
