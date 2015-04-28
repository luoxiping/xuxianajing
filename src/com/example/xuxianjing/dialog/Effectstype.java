package com.example.xuxianjing.dialog;

import com.example.xuxianjing.effect.BaseEffects;
import com.example.xuxianjing.effect.FadeIn;
import com.example.xuxianjing.effect.FlipH;
import com.example.xuxianjing.effect.FlipV;
import com.example.xuxianjing.effect.NewsPaper;
import com.example.xuxianjing.effect.SideFall;
import com.example.xuxianjing.effect.SlideLeft;
import com.example.xuxianjing.effect.SlideRight;
import com.example.xuxianjing.effect.SlideTop;
import com.example.xuxianjing.effect.SlideBottom;
import com.example.xuxianjing.effect.Fall;
import com.example.xuxianjing.effect.RotateBottom;
import com.example.xuxianjing.effect.RotateLeft;
import com.example.xuxianjing.effect.Slit;
import com.example.xuxianjing.effect.Shake;

/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class effectsClazz;

    private Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
