package com.volumelistener

import android.util.Log
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule

class RTNVolumeListenerModule internal constructor(reactContext: ReactApplicationContext) :
  RTNVolumeListenerSpec(reactContext), LifecycleEventListener {

  private val reactContext: ReactApplicationContext = reactContext
  private val volumeBR = VolumeBroadcastReceiver()
  private val audioManager: AudioManager = reactContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
  private val TAG = "RTNVolumeListenerModule"

  private var listenerCount = 0
  private var isVolumeBRRegistered = false

  init {
    reactContext.addLifecycleEventListener(this)
  }

  override fun getName(): String {
    return NAME
  }

  private fun registerVolumeReceiver() {
    if (!isVolumeBRRegistered) {
      val filter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
      reactContext.registerReceiver(volumeBR, filter)
      sendVolumeEvent()
    }
  }

  private fun unregisterVolumeReceiver() {
    if (isVolumeBRRegistered) {
      try {
        reactContext.unregisterReceiver(volumeBR)
        isVolumeBRRegistered = false
      } catch (e: IllegalArgumentException) {
        Log.e(TAG, "Error unregistering volume receiver", e)
      }
    }
  }

  private fun sendVolumeEvent() {
    Log.d(TAG, "sendVolumeEvent")
    val params = Arguments.createMap()
    params.putDouble("volume", getNormalizationVolume(AudioManager.STREAM_MUSIC).toDouble())
    try {
      reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
        .emit("RTNVolumeListenerEventVolume", params)
    } catch (e: RuntimeException) {
      // JS not ready to receive event, ignore
      Log.e(TAG, "Error sending volume event", e)
    }
  }

  private fun getNormalizationVolume(type: Int): Float {
    return audioManager.getStreamVolume(type) * 1.0f / audioManager.getStreamMaxVolume(type)
  }

  override fun onHostResume() {
    if (listenerCount > 0) {
      registerVolumeReceiver()
    }
  }

  override fun onHostPause() {
    unregisterVolumeReceiver()
  }

  override fun onHostDestroy() {
    unregisterVolumeReceiver()
  }

  @ReactMethod
  override fun addListener(eventName: String) {
    if (listenerCount == 0) {
      registerVolumeReceiver()
    }
    listenerCount += 1
  }
  
  @ReactMethod
  override fun removeListeners(count: Double) {
    var countInt = count.toInt()
    listenerCount -= countInt
    if (listenerCount == 0) {
      unregisterVolumeReceiver()
    }
  }

  private inner class VolumeBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      if (intent.action == "android.media.VOLUME_CHANGED_ACTION") {
        sendVolumeEvent()
      }
    }
  }

  companion object {
    const val NAME = "RTNVolumeListener"
  }
}



// class RTNVolumeListenerModule internal constructor(reactContext: ReactApplicationContext) :
//   RTNVolumeListenerSpec(reactContext), LifecycleEventListener {

//   @ReactMethod
//   override fun addListener(eventName: String) {
//     // 
//   }

//   fun removeListeners(count: Int) {
//     // 
//   }

//   @ReactMethod
//   override fun removeListeners(count: Double) {
//     removeListeners(count.toInt())
//   }
// }