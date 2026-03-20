package com.reactnativecommunity.slider;

import static com.facebook.drawee.drawable.RoundedCornersDrawable.Type.CLIPPING;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import com.facebook.drawee.drawable.RoundedCornersDrawable;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.Map;

import javax.annotation.Nullable;

public class ReactSliderManagerImpl {

    public static final String REACT_CLASS = "RNCSlider";

    public static ReactSlider createLegacySlider(ThemedReactContext context) {
        ReactSlider slider = new ReactSlider(context, null);

        /**
         * The "splitTrack" parameter should have "false" value,
         * otherwise the SeekBar progress line doesn't appear when it is rotated.
         */
        slider.setSplitTrack(false);

        return slider;
    }

    public static ReactSliderView createViewInstance(ThemedReactContext context) {
        return new ReactSliderView(context);
    }

    public static void setValue(ReactSliderView view, double value) {
        view.setValue(value);
    }

    public static void setMinimumValue(ReactSliderView view, double value) {
        view.setMinimumValue(value);
    }

    public static void setMaximumValue(ReactSliderView view, double value) {
        view.setMaximumValue(value);
    }

    public static void setLowerLimit(ReactSliderView view, double value) {
        view.setLowerLimit(value);
    }

    public static void setUpperLimit(ReactSliderView view, double value) {
        view.setUpperLimit(value);
    }

    public static void setStep(ReactSliderView view, double value) {
        view.setStep(value);
    }

    public static void setDisabled(ReactSliderView view, boolean disabled) {
        view.setDisabled(disabled);
    }

    public static void setThumbTintColor(ReactSliderView view, Integer color) {
        view.setThumbTintColor(color);
    }

    static void applyThumbTintColor(ReactSlider view, Integer color) {
        if (view.getThumb() != null) {
            if (color == null) {
                view.getThumb().clearColorFilter();
            } else {
                view.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        }
    }

    public static void setMinimumTrackTintColor(ReactSliderView view, Integer color) {
        view.setMinimumTrackTintColor(color);
    }

    static void applyMinimumTrackTintColor(ReactSlider view, Integer color) {
        LayerDrawable drawable = (LayerDrawable) view.getProgressDrawable().getCurrent();
        Drawable progress = drawable.findDrawableByLayerId(android.R.id.progress);
        if (color == null) {
            progress.clearColorFilter();
        } else {
            progress.setColorFilter(new PorterDuffColorFilter((int)color, PorterDuff.Mode.SRC_IN));
        }
    }

    public static void setThumbImage(ReactSliderView view, @Nullable ReadableMap source) {
        view.setThumbImage(source);
    }

    public static void setMaximumTrackTintColor(ReactSliderView view, Integer color) {
        view.setMaximumTrackTintColor(color);
    }

    static void applyMaximumTrackTintColor(ReactSlider view, Integer color) {
        LayerDrawable drawable = (LayerDrawable) view.getProgressDrawable().getCurrent();
        Drawable background = drawable.findDrawableByLayerId(android.R.id.background);
        if (color == null) {
            background.clearColorFilter();
        } else {
            background.setColorFilter(new PorterDuffColorFilter((int)color, PorterDuff.Mode.SRC_IN));
        }
    }

    public static void setInverted(ReactSliderView view, boolean inverted) {
        view.setInverted(inverted);
    }

    public static void setAccessibilityUnits(ReactSliderView view, String accessibilityUnits) {
        view.setAccessibilityUnits(accessibilityUnits);
    }

    public static void setAccessibilityIncrements(ReactSliderView view, ReadableArray accessibilityIncrements) {
        view.setAccessibilityIncrements(accessibilityIncrements);
    }

    public static Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.of(
                ReactSliderEvent.EVENT_NAME, MapBuilder.of("registrationName", ReactSliderEvent.EVENT_NAME)
        );
    }

    public static Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
                ReactSlidingStartEvent.EVENT_NAME, MapBuilder.of("registrationName", ReactSlidingStartEvent.EVENT_NAME),
                ReactSlidingCompleteEvent.EVENT_NAME, MapBuilder.of("registrationName", ReactSlidingCompleteEvent.EVENT_NAME)
        );
    }

    public static void setSliderThickness(ReactSliderView view, double value) {
        view.setSliderThickness(value);
    }

    static void applySliderThickness(ReactSlider view, double value) {
        LayerDrawable drawable = (LayerDrawable) view.getProgressDrawable().getCurrent();
        for (int i = 0; i < drawable.getNumberOfLayers(); i ++ ) {
            // 0 is max/background progress; 1 is ???; 2 is min/current progress
            drawable.setLayerHeight(i, (int) value);
        }
    }

    public static void setSliderCornerRoundness(ReactSliderView view, double value) {
        view.setSliderCornerRoundness(value);
    }

    static void applySliderCornerRoundness(ReactSlider view, double value) {
        LayerDrawable drawable = (LayerDrawable) view.getProgressDrawable().getCurrent();
        for (int i = 0; i < drawable.getNumberOfLayers(); i ++ ) {
            RoundedCornersDrawable newDrawable = new RoundedCornersDrawable(drawable.getDrawable(i));
            newDrawable.setRadius((float) value);
            newDrawable.setType(CLIPPING);
            drawable.setDrawable(i, newDrawable);
        }
    }
    public static void setThumbSize(ReactSliderView view, double w, double h) {
        view.setThumbSize(w, h);
    }

    static void applyThumbSize(ReactSlider view, double w, double h) {
        view.thumbDrawable.setDimension(w, h);
    }

}
