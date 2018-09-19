package com.example.administrator.waveview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable mDisposables;
    private float progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WaveProgressView mWaveView = findViewById(R.id.waveview);
        mWaveView.setMax(98);

        mDisposables = new CompositeDisposable();
        DisposableObserver<Long> mObserver = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long value) {
                progress = progress + 5;
                if (progress <= 100) {
                    mWaveView.setProgress(progress);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i("接收数据", "onComplete");
            }
        };
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
        mDisposables.add(mObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.clear();
    }
}