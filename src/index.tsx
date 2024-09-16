import { NativeEventEmitter, type EmitterSubscription } from 'react-native';
import { type VolumeResult } from './NativeRTNVolumeListener';

const RTNVolumeListener = require('./NativeRTNVolumeListener').default;

const eventEmitter = new NativeEventEmitter(RTNVolumeListener);

export function addVolumeListener(
  callback: (result: VolumeResult) => void
): EmitterSubscription {
  return eventEmitter.addListener('RTNVolumeListenerEventVolume', callback);
}
