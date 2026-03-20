package com.reactnativecommunity.slider

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import kotlin.math.roundToInt

class ReactMaterialSliderView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {
  interface EventListener {
    fun onValueChange(nextValue: Double, fromUser: Boolean)
    fun onSlidingStart(nextValue: Double)
    fun onSlidingComplete(nextValue: Double)
  }

  private var minimumValueState by mutableStateOf(0.0)
  private var maximumValueState by mutableStateOf(1.0)
  private var stepState by mutableStateOf(0.0)
  private var lowerLimitState by mutableStateOf(Double.NEGATIVE_INFINITY)
  private var upperLimitState by mutableStateOf(Double.POSITIVE_INFINITY)
  private var sliderValueState by mutableStateOf(0.0)
  private var disabledState by mutableStateOf(false)
  private var thumbTintColorArgb by mutableIntStateOf(Int.MIN_VALUE)
  private var minimumTrackTintColorArgb by mutableIntStateOf(Int.MIN_VALUE)
  private var maximumTrackTintColorArgb by mutableIntStateOf(Int.MIN_VALUE)
  private var accessibilityUnitsState by mutableStateOf<String?>(null)
  private val accessibilityIncrementsState = mutableStateListOf<String>()
  private var eventListener: EventListener? = null
  private var dragging = false
  private val composeView = ComposeView(context)
  private var contentInitialized = false

  init {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    composeView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
    composeView.addOnAttachStateChangeListener(
      object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
          ensureContent()
          composeView.removeOnAttachStateChangeListener(this)
        }

        override fun onViewDetachedFromWindow(v: View) = Unit
      },
    )
    addView(composeView)
    if (composeView.isAttachedToWindow) {
      ensureContent()
    }
  }

  fun setEventListener(listener: EventListener?) {
    eventListener = listener
  }

  fun setValue(value: Double) {
    if (!dragging) {
      sliderValueState = normalizeValue(value)
    }
  }

  fun setMinimumValue(value: Double) {
    minimumValueState = value
    sliderValueState = normalizeValue(sliderValueState)
  }

  fun setMaximumValue(value: Double) {
    maximumValueState = value
    sliderValueState = normalizeValue(sliderValueState)
  }

  fun setStep(value: Double) {
    stepState = value
    sliderValueState = normalizeValue(sliderValueState)
  }

  fun setLowerLimit(value: Double) {
    lowerLimitState = value
    sliderValueState = normalizeValue(sliderValueState)
  }

  fun setUpperLimit(value: Double) {
    upperLimitState = value
    sliderValueState = normalizeValue(sliderValueState)
  }

  fun setDisabled(value: Boolean) {
    disabledState = value
  }

  fun setThumbTintColor(color: Int?) {
    thumbTintColorArgb = color ?: Int.MIN_VALUE
  }

  fun setMinimumTrackTintColor(color: Int?) {
    minimumTrackTintColorArgb = color ?: Int.MIN_VALUE
  }

  fun setMaximumTrackTintColor(color: Int?) {
    maximumTrackTintColorArgb = color ?: Int.MIN_VALUE
  }

  fun setAccessibilityUnits(units: String?) {
    accessibilityUnitsState = units
  }

  fun setAccessibilityIncrements(increments: List<String>?) {
    accessibilityIncrementsState.clear()
    if (increments != null) {
      accessibilityIncrementsState.addAll(increments)
    }
  }

  fun setThumbSize(@Suppress("UNUSED_PARAMETER") size: Double) {}

  fun setThumbImageUri(@Suppress("UNUSED_PARAMETER") uri: String?) {}

  private fun ensureContent() {
    if (contentInitialized) {
      return
    }

    contentInitialized = true
    composeView.setContent {
      MaterialTheme {
        val rangeStart = minimumValueState.toFloat()
        val rangeEnd = maxOf(maximumValueState, minimumValueState).toFloat()
        val sliderColors =
          if (
            thumbTintColorArgb != Int.MIN_VALUE ||
              minimumTrackTintColorArgb != Int.MIN_VALUE ||
              maximumTrackTintColorArgb != Int.MIN_VALUE
          ) {
            SliderDefaults.colors(
              thumbColor = thumbTintColorArgb.toComposeColor(),
              activeTrackColor = minimumTrackTintColorArgb.toComposeColor(),
              inactiveTrackColor = maximumTrackTintColorArgb.toComposeColor(),
            )
          } else {
            SliderDefaults.colors()
          }

        Slider(
          value = normalizeValue(sliderValueState).toFloat(),
          onValueChange = { next ->
            val adjusted = normalizeValue(next.toDouble())
            if (!dragging) {
              dragging = true
              eventListener?.onSlidingStart(adjusted)
            }
            if (adjusted != sliderValueState) {
              sliderValueState = adjusted
              eventListener?.onValueChange(adjusted, true)
            }
          },
          valueRange = rangeStart..rangeEnd,
          enabled = !disabledState,
          steps = materialSteps(),
          onValueChangeFinished = {
            if (dragging) {
              dragging = false
              eventListener?.onSlidingComplete(sliderValueState)
            }
          },
          modifier =
            Modifier
              .fillMaxWidth()
              .semantics {
                accessibilityDescription()?.let {
                  stateDescription = it
                }
              },
          colors = sliderColors,
        )
      }
    }
  }

  private fun normalizeValue(value: Double): Double {
    val actualMinimum = maxOf(minimumValueState, lowerLimitState)
    val actualMaximum = minOf(maximumValueState, upperLimitState)
    if (actualMaximum <= actualMinimum) {
      return actualMinimum
    }

    val clamped = value.coerceIn(actualMinimum, actualMaximum)
    if (stepState <= 0.0) {
      return clamped
    }

    val stepsFromMin = ((clamped - minimumValueState) / stepState).roundToInt()
    val snapped = minimumValueState + stepsFromMin * stepState
    return snapped.coerceIn(actualMinimum, actualMaximum)
  }

  private fun materialSteps(): Int {
    if (stepState <= 0.0) {
      return 0
    }

    val totalIntervals = ((maximumValueState - minimumValueState) / stepState).roundToInt()
    return maxOf(totalIntervals - 1, 0)
  }

  private fun accessibilityDescription(): String? {
    val units = accessibilityUnitsState ?: return null
    val increments = accessibilityIncrementsState
    val index = sliderValueState.roundToInt()
    if (increments.size != maximumValueState.roundToInt() + 1 || index !in increments.indices) {
      return null
    }

    val label = increments[index]
    val spokenUnits =
      if (label == "1" && units.length > 1) units.dropLast(1) else units
    return "$label $spokenUnits"
  }

  private fun Int.toComposeColor(): Color =
    if (this == Int.MIN_VALUE) Color.Unspecified else Color(this)
}
