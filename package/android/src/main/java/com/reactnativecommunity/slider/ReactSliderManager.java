package com.reactnativecommunity.slider;

import android.content.Context;
import android.view.View;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.annotations.ReactProp;
import java.util.Map;
import com.facebook.react.viewmanagers.RNCSliderManagerInterface;
import com.facebook.react.viewmanagers.RNCSliderManagerDelegate;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.yoga.YogaMeasureMode;
import com.facebook.yoga.YogaMeasureOutput;

/**
 * Manages instances of {@code ReactSlider}.
 */
@ReactModule(name = ReactSliderManagerImpl.REACT_CLASS)
public class ReactSliderManager extends SimpleViewManager<ReactSliderView> implements RNCSliderManagerInterface<ReactSliderView> {

  private final ViewManagerDelegate<ReactSliderView> mDelegate;

  public ReactSliderManager() {
    mDelegate = new RNCSliderManagerDelegate<>(this);
  }

  @Nullable
  @Override
  protected ViewManagerDelegate<ReactSliderView> getDelegate() {
    return mDelegate;
  }

  @Override
  public String getName() {
    return ReactSliderManagerImpl.REACT_CLASS;
  }

  @Override
  protected ReactSliderView createViewInstance(ThemedReactContext context) {
    return ReactSliderManagerImpl.createViewInstance(context);
  }

  @Override
  @ReactProp(name = "disabled", defaultBoolean = false)
  public void setDisabled(ReactSliderView view, boolean disabled) {
    ReactSliderManagerImpl.setDisabled(view, disabled);
  }

  @Override
  @ReactProp(name = "value", defaultFloat = 0f)
  public void setValue(ReactSliderView view, float value) {
    ReactSliderManagerImpl.setValue(view, value);
  }

  @Override
  @ReactProp(name = "minimumValue", defaultFloat = 0f)
  public void setMinimumValue(ReactSliderView view, double value) {
    ReactSliderManagerImpl.setMinimumValue(view, value);
  }

  @Override
  @ReactProp(name = "maximumValue", defaultFloat = 0f)
  public void setMaximumValue(ReactSliderView view, double value) {
    ReactSliderManagerImpl.setMaximumValue(view, value);
  }

  @Override
  @ReactProp(name = "step", defaultFloat = 0f)
  public void setStep(ReactSliderView view, double value) {
    ReactSliderManagerImpl.setStep(view, value);
  }

  @Override
  @ReactProp(name = "thumbTintColor", customType = "Color")
  public void setThumbTintColor(ReactSliderView view, Integer color) {
    ReactSliderManagerImpl.setThumbTintColor(view, color);
  }

  @Override
  @ReactProp(name = "minimumTrackTintColor", customType = "Color")
  public void setMinimumTrackTintColor(ReactSliderView view, Integer color) {
    ReactSliderManagerImpl.setMinimumTrackTintColor(view, color);
  }

  @Override
  @ReactProp(name = "maximumTrackTintColor", customType = "Color")
  public void setMaximumTrackTintColor(ReactSliderView view, Integer color) {
    ReactSliderManagerImpl.setMaximumTrackTintColor(view, color);
  }

  @Override
  @ReactProp(name = "inverted", defaultBoolean = false)
  public void setInverted(ReactSliderView view, boolean inverted) {
    ReactSliderManagerImpl.setInverted(view, inverted);
  }

  @ReactProp(name = "useMaterial3", defaultBoolean = false)
  public void setUseMaterial3(ReactSliderView view, boolean useMaterial3) {
    view.setUseMaterial3(useMaterial3);
  }

  @Override
  @ReactProp(name = "accessibilityUnits")
  public void setAccessibilityUnits(ReactSliderView view, String accessibilityUnits) {
    ReactSliderManagerImpl.setAccessibilityUnits(view, accessibilityUnits);
  }

  @Override
  @ReactProp(name = "accessibilityIncrements")
  public void setAccessibilityIncrements(ReactSliderView view, ReadableArray accessibilityIncrements) {
    ReactSliderManagerImpl.setAccessibilityIncrements(view, accessibilityIncrements);
  }

  @ReactProp(name = "lowerLimit")
  public void setLowerLimit(ReactSliderView view, float value) {
    ReactSliderManagerImpl.setLowerLimit(view, value);
  }

  @ReactProp(name = "upperLimit")
  public void setUpperLimit(ReactSliderView view, float value) {
    ReactSliderManagerImpl.setUpperLimit(view, value);
  }

  @Override
  public void setSliderThickness(ReactSliderView view, double value) {
    ReactSliderManagerImpl.setSliderThickness(view, value);
  }

  @Override
  public void setSliderCornerRoundness(ReactSliderView view, double value) {
    ReactSliderManagerImpl.setSliderCornerRoundness(view, value);
  }

  @Override
  public void setThumbSize(ReactSliderView view, double value) {
    ReactSliderManagerImpl.setThumbSize(view, value, value);
  }

  @Override
  @ReactProp(name = "thumbImage")
  public void setThumbImage(ReactSliderView view, @androidx.annotation.Nullable ReadableMap source) {
    ReactSliderManagerImpl.setThumbImage(view, source);
  }

  @Override
  public void setTestID(ReactSliderView view, @Nullable String value) {
    super.setTestId(view, value);
  }

  @Override
  protected void addEventEmitters(final ThemedReactContext reactContext, final ReactSliderView view) {
    view.setEventDispatcher(com.facebook.react.uimanager.UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.getId()));
  }

  // these props are not available on Android, however we must override their setters
  @Override
  public void setMinimumTrackImage(ReactSliderView view, @Nullable ReadableMap readableMap) {}

  @Override
  public void setMaximumTrackImage(ReactSliderView view, @Nullable ReadableMap readableMap) {}

  @Override
  public void setTrackImage(ReactSliderView view, @Nullable ReadableMap value) {}

  @Override
  public void setTapToSeek(ReactSliderView view, boolean value) {}

  @Override
  public void setVertical(ReactSliderView view, boolean value) {}

  @Override
  public long measure(
          Context context,
          ReadableMap localData,
          ReadableMap props,
          ReadableMap state,
          float width,
          YogaMeasureMode widthMode,
          float height,
          YogaMeasureMode heightMode,
          @Nullable float[] attachmentsPositions) {
    ReactSlider view = new ReactSlider(context, null);
    view.setSplitTrack(false);
    int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    view.measure(measureSpec, measureSpec);
    return YogaMeasureOutput.make(
            PixelUtil.toDIPFromPixel(view.getMeasuredWidth()),
            PixelUtil.toDIPFromPixel(view.getMeasuredHeight()));
  }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        return ReactSliderManagerImpl.getExportedCustomBubblingEventTypeConstants();
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return ReactSliderManagerImpl.getExportedCustomDirectEventTypeConstants();
    }
}
