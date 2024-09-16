
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNRTNVolumeListenerSpec.h"

@interface RTNVolumeListener : NSObject <NativeRTNVolumeListenerSpec>
#else
#import <React/RCTBridgeModule.h>

@interface RTNVolumeListener : NSObject <RCTBridgeModule>
#endif

@end
