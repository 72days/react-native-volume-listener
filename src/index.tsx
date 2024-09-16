const RTNVolumeListener = require('./NativeRTNVolumeListener').default;

export function multiply(a: number, b: number): number {
  return RTNVolumeListener.multiply(a, b);
}
