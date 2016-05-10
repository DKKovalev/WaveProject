package com.example.dkovalev.waveproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.dkovalev.waveproject.Assets.Sector;
import com.example.dkovalev.waveproject.Assets.WaterContainerDataModel;
import com.example.dkovalev.waveproject.Assets.WaterCurtain;
import com.example.dkovalev.waveproject.Assets.WaveHelper;
import com.gelitenight.waveview.library.WaveView;
import com.squareup.picasso.Picasso;

//TODO Динамическая подстройка под различные изображения
//TODO Сделать масштабирование исходя из ширины и высоты экрана

public class MainActivity extends AppCompatActivity {
    private WaterCurtain waterCurtain;
    private float incr;
    private WaveHelper waveHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WaveView waveView = (WaveView) findViewById(R.id.waveview);

        ImageView imageFill = (ImageView) findViewById(R.id.image_fill);
        Picasso.with(this).load(R.drawable.water_drop_fill).into(imageFill);

        ImageView imageFront = (ImageView) findViewById(R.id.image_front);
        Picasso.with(this).load(R.drawable.water_drop).into(imageFront);


        waterCurtain = (WaterCurtain) findViewById(R.id.image_curtain);
        waterCurtain.setWaterContainerIcons(WaterContainerDataModel.WaterContainerIcons.BOTTLE);

        final Button increaseButton = (Button) findViewById(R.id.btn_increase);
        assert increaseButton != null;

        final Button decreaseButton = (Button) findViewById(R.id.btn_decrease);
        assert decreaseButton != null;

        decreaseButton.setEnabled(false);

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incr += 0.1;
                waterCurtain.setSector(new Sector(Color.WHITE, getRounded(incr)));
                waveView.setWaterLevelRatio(getRounded(incr)*0.9f);

                if (getRounded(incr) > 0) {
                    decreaseButton.setEnabled(true);
                }

                if (getRounded(incr) == 1f) {
                    increaseButton.setEnabled(false);
                }
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incr -= 0.1f;
                waterCurtain.setSector(new Sector(Color.WHITE, getRounded(incr)));
                waveView.setWaterLevelRatio(getRounded(incr)*0.9f);

                if (getRounded(incr) < 1f) {
                    increaseButton.setEnabled(true);
                }
                if (getRounded(incr) == 0) {
                    decreaseButton.setEnabled(false);
                }
            }
        });

        waveView.setBorder(1, Color.BLACK);
        waveView.setWaterLevelRatio(0);
        waveView.setAmplitudeRatio(0.02f);
        waveHelper = new WaveHelper(waveView);
        waveView.setShapeType(WaveView.ShapeType.DROP);
    }

    private float getRounded(float f) {
        float round = Math.round(f * 100f) / 100f;
        return round;
    }

    @Override
    protected void onResume() {
        super.onResume();
        waveHelper.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        waveHelper.cancel();
    }


}
