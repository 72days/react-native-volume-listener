import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export type VolumeResult = {
  volume: number;
};

export interface Spec extends TurboModule {
  // RCTEventEmitter
  addListener: (eventName: string) => void;
  removeListeners: (count: number) => void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RTNVolumeListener');
