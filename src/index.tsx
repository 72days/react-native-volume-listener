import { NativeEventEmitter, type EmitterSubscription } from 'react-native';
import { type VolumeResult } from './NativeRTNVolumeListener';
import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'r' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const RTNVolumeListenerModule = isTurboModuleEnabled
  ? require('./NativeRTNVolumeListener').default
  : NativeModules.RTNVolumeListener;

const RTNVolumeListener = RTNVolumeListenerModule
  ? RTNVolumeListenerModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const eventEmitter = new NativeEventEmitter(RTNVolumeListener);

export function addVolumeListener(
  callback: (result: VolumeResult) => void
): EmitterSubscription {
  return eventEmitter.addListener('RTNVolumeListenerEventVolume', callback);
}
