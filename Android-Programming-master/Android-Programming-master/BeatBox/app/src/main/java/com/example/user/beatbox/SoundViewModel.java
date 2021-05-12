package com.example.user.beatbox;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by user on 2019/9/17.
 */

public class SoundViewModel extends BaseObservable {

    private Sound mSound;
    private BeatBox mBeatBox;

    public SoundViewModel(BeatBox beatBox) {
        mBeatBox = beatBox;
    }

    public Sound getSound() {
        return mSound;
    }

    public void setSound(Sound sound) {
        mSound = sound;
        notifyChange();
    }

    @Bindable
    public String getTitle() {
        return mSound.getName();
    }

    public void onButtonClicked() {
        mBeatBox.play(mSound);
    }
}
