
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNRTNVolumeListenerSpec.h"
#import <React/RCTEventEmitter.h>

@interface RTNVolumeListener : RCTEventEmitter <NativeRTNVolumeListenerSpec>
#else
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RTNVolumeListener : RCTEventEmitter <RCTBridgeModule>
#endif

@end
