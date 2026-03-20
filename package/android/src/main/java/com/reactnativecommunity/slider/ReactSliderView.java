package com.reactnativecommunity.slider;

import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.EventDispatcher;

import java.util.ArrayList;
import java.util.List;

public class ReactSliderView extends FrameLayout {
  private final ThemedReactContext reactContext;
  private final ReactSlider legacySlider;
  @Nullable private ReactMaterialSliderView materialSlider;
  @Nullable private EventDispatcher eventDispatcher;

  private boolean useMaterial3 = false;
  private boolean disabled = false;
  private boolean inverted = false;
  private double value = 0d;
  private double minimumValue = 0d;
  private double maximumValue = 0d;
  private double step = 0d;
  private double lowerLimit = Long.MIN_VALUE;
  private double upperLimit = Long.MAX_VALUE;
  @Nullable private Integer thumbTintColor;
  @Nullable private Integer minimumTrackTintColor;
  @Nullable private Integer maximumTrackTintColor;
  @Nullable private String thumbImageUri;
  @Nullable private String accessibilityUnits;
  @Nullable private List<String> accessibilityIncrements;
  @Nullable private Double sliderThickness;
  @Nullable private Double sliderCornerRoundness;
  @Nullable private Double thumbSize;

  private final SeekBar.OnSeekBarChangeListener legacyChangeListener =
      new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
          ReactSlider slider = (ReactSlider) seekbar;

          progress = slider.getValidProgressValue(progress);
          seekbar.setProgress(progress);

          if (fromUser) {
            dispatchValueChange(slider.toRealProgress(progress), true);
          }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekbar) {
          ReactSlider slider = (ReactSlider) seekbar;
          slider.isSliding(true);
          dispatchSlidingStart(slider.toRealProgress(seekbar.getProgress()));
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekbar) {
          ReactSlider slider = (ReactSlider) seekbar;
          slider.isSliding(false);
          dispatchSlidingComplete(slider.toRealProgress(seekbar.getProgress()));
        }
      };

  private final ReactMaterialSliderView.EventListener materialEventListener =
      new ReactMaterialSliderView.EventListener() {
        @Override
        public void onValueChange(double nextValue, boolean fromUser) {
          value = nextValue;
          dispatchValueChange(nextValue, fromUser);
        }

        @Override
        public void onSlidingStart(double nextValue) {
          value = nextValue;
          dispatchSlidingStart(nextValue);
        }

        @Override
        public void onSlidingComplete(double nextValue) {
          value = nextValue;
          dispatchSlidingComplete(nextValue);
        }
      };

  public ReactSliderView(ThemedReactContext context) {
    super(context);
    reactContext = context;
    legacySlider = ReactSliderManagerImpl.createLegacySlider(context);
    legacySlider.setLayoutParams(defaultLayoutParams());
    legacySlider.setOnSeekBarChangeListener(legacyChangeListener);
    addView(legacySlider);
    applyAllProps();
  }

  public void setEventDispatcher(@Nullable EventDispatcher dispatcher) {
    eventDispatcher = dispatcher;
  }

  public void setUseMaterial3(boolean enabled) {
    if (useMaterial3 == enabled) {
      return;
    }

    useMaterial3 = enabled;
    swapImplementation();
  }

  public void setValue(double nextValue) {
    value = nextValue;
    if (!useMaterial3) {
      if (!legacySlider.isSliding()) {
        legacySlider.setValue(nextValue);
        if (legacySlider.isAccessibilityFocused() && Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
          legacySlider.setupAccessibility((int) nextValue);
        }
      }
      return;
    }

    if (materialSlider != null) {
      materialSlider.setValue(nextValue);
    }
  }

  public void setMinimumValue(double nextValue) {
    minimumValue = nextValue;
    if (useMaterial3) {
      if (materialSlider != null) {
        materialSlider.setMinimumValue(nextValue);
      }
    } else {
      legacySlider.setMinValue(nextValue);
    }
  }

  public void setMaximumValue(double nextValue) {
    maximumValue = nextValue;
    if (useMaterial3) {
      if (materialSlider != null) {
        materialSlider.setMaximumValue(nextValue);
      }
    } else {
      legacySlider.setMaxValue(nextValue);
    }
  }

  public void setLowerLimit(double nextValue) {
    lowerLimit = nextValue;
    if (useMaterial3) {
      if (materialSlider != null) {
        materialSlider.setLowerLimit(nextValue);
      }
    } else {
      legacySlider.setLowerLimit(nextValue);
    }
  }

  public void setUpperLimit(double nextValue) {
    upperLimit = nextValue;
    if (useMaterial3) {
      if (materialSlider != null) {
        materialSlider.setUpperLimit(nextValue);
      }
    } else {
      legacySlider.setUpperLimit(nextValue);
    }
  }

  public void setStep(double nextValue) {
    step = nextValue;
    if (useMaterial3) {
      if (materialSlider != null) {
        materialSlider.setStep(nextValue);
      }
    } else {
      legacySlider.setStep(nextValue);
    }
  }

  public void setDisabled(boolean nextValue) {
    disabled = nextValue;
    currentView().setEnabled(!nextValue);
    if (materialSlider != null) {
      materialSlider.setDisabled(nextValue);
    }
  }

  public void setThumbTintColor(@Nullable Integer color) {
    thumbTintColor = color;
    if (useMaterial3) {
      if (materialSlider != null) {
        materialSlider.setThumbTintColor(color);
      }
    } else {
      ReactSliderManagerImpl.applyThumbTintColor(legacySlider, color);
    }
  }

  public void setMinimumTrackTintColor(@Nullable Integer color) {
    minimumTrackTintColor = color;
    if (useMaterial3) {
      if (materialSlider != null) {
        materialSlider.setMinimumTrackTintColor(color);
      }
    } else {
      ReactSliderManagerImpl.applyMinimumTrackTintColor(legacySlider, color);
    }
  }

  public void setMaximumTrackTintColor(@Nullable Integer color) {
    maximumTrackTintColor = color;
    if (useMaterial3) {
      if (materialSlider != null) {
        materialSlider.setMaximumTrackTintColor(color);
      }
    } else {
      ReactSliderManagerImpl.applyMaximumTrackTintColor(legacySlider, color);
    }
  }

  public void setThumbImage(@Nullable ReadableMap source) {
    setThumbImageUri(source != null ? source.getString("uri") : null);
  }

  public void setThumbImageUri(@Nullable String uri) {
    thumbImageUri = uri;
    if (useMaterial3) {
      if (materialSlider != null) {
        materialSlider.setThumbImageUri(thumbImageUri);
      }
    } else {
      legacySlider.setThumbImage(thumbImageUri);
    }
  }

  public void setInverted(boolean nextValue) {
    inverted = nextValue;
    float scale = nextValue ? -1f : 1f;
    legacySlider.setScaleX(scale);
    if (materialSlider != null) {
      materialSlider.setScaleX(scale);
    }
  }

  public void setAccessibilityUnits(@Nullable String nextValue) {
    accessibilityUnits = nextValue;
    legacySlider.setAccessibilityUnits(nextValue);
    if (materialSlider != null) {
      materialSlider.setAccessibilityUnits(nextValue);
    }
  }

  public void setAccessibilityIncrements(@Nullable ReadableArray increments) {
    accessibilityIncrements = null;
    if (increments != null) {
      List<Object> objectList = increments.toArrayList();
      List<String> stringList = new ArrayList<>();
      for (Object item : objectList) {
        stringList.add((String) item);
      }
      accessibilityIncrements = stringList;
    }

    legacySlider.setAccessibilityIncrements(accessibilityIncrements);
    if (materialSlider != null) {
      materialSlider.setAccessibilityIncrements(accessibilityIncrements);
    }
  }

  public void setSliderThickness(double nextValue) {
    sliderThickness = nextValue;
    if (!useMaterial3) {
      ReactSliderManagerImpl.applySliderThickness(legacySlider, nextValue);
    }
  }

  public void setSliderCornerRoundness(double nextValue) {
    sliderCornerRoundness = nextValue;
    if (!useMaterial3) {
      ReactSliderManagerImpl.applySliderCornerRoundness(legacySlider, nextValue);
    }
  }

  public void setThumbSize(double width, double height) {
    thumbSize = width;
    if (useMaterial3) {
      if (materialSlider != null) {
        materialSlider.setThumbSize(width);
      }
    } else {
      ReactSliderManagerImpl.applyThumbSize(legacySlider, width, height);
    }
  }

  private View currentView() {
    return useMaterial3 && materialSlider != null ? materialSlider : legacySlider;
  }

  private LayoutParams defaultLayoutParams() {
    return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
  }

  private void swapImplementation() {
    removeAllViews();
    if (useMaterial3) {
      if (materialSlider == null) {
        materialSlider = new ReactMaterialSliderView(reactContext);
        materialSlider.setLayoutParams(defaultLayoutParams());
        materialSlider.setEventListener(materialEventListener);
      }
      addView(materialSlider);
    } else {
      addView(legacySlider);
    }

    applyAllProps();
  }

  private void applyAllProps() {
    setDisabled(disabled);
    setMinimumValue(minimumValue);
    setMaximumValue(maximumValue);
    setStep(step);
    setLowerLimit(lowerLimit);
    setUpperLimit(upperLimit);
    setAccessibilityUnits(accessibilityUnits);
    legacySlider.setAccessibilityIncrements(accessibilityIncrements);
    if (materialSlider != null) {
      materialSlider.setAccessibilityIncrements(accessibilityIncrements);
    }
    setThumbTintColor(thumbTintColor);
    setMinimumTrackTintColor(minimumTrackTintColor);
    setMaximumTrackTintColor(maximumTrackTintColor);
    setThumbImageUri(thumbImageUri);
    setInverted(inverted);
    if (sliderThickness != null) {
      setSliderThickness(sliderThickness);
    }
    if (sliderCornerRoundness != null) {
      setSliderCornerRoundness(sliderCornerRoundness);
    }
    if (thumbSize != null) {
      setThumbSize(thumbSize, thumbSize);
    }
    setValue(value);
  }

  private void dispatchValueChange(double nextValue, boolean fromUser) {
    if (eventDispatcher == null) {
      return;
    }
    eventDispatcher.dispatchEvent(new ReactSliderEvent(getId(), nextValue, fromUser));
  }

  private void dispatchSlidingStart(double nextValue) {
    if (eventDispatcher == null) {
      return;
    }
    eventDispatcher.dispatchEvent(new ReactSlidingStartEvent(getId(), nextValue));
  }

  private void dispatchSlidingComplete(double nextValue) {
    if (eventDispatcher == null) {
      return;
    }
    eventDispatcher.dispatchEvent(new ReactSlidingCompleteEvent(getId(), nextValue));
  }
}
