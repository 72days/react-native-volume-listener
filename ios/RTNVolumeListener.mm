#import <MediaPlayer/MediaPlayer.h>

#import "RTNVolumeListener.h"

#import <React/RCTEventDispatcherProtocol.h>
#import <React/RCTLog.h>
#import <React/RCTUtils.h>

#import <FBReactNativeSpec/FBReactNativeSpec.h>

static NSString *const sessionVolumeKeyPath = @"outputVolume";
static void *sessionContext                 = &sessionContext;

@interface RTNVolumeListener () <NativeRTNVolumeListenerSpec>

@property (nonatomic, strong) AVAudioSession * audioSession;

@end

@implementation RTNVolumeListener
RCT_EXPORT_MODULE()

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED

- (void)addVolumeListener {
    NSError *error = nil;

    self.audioSession = [AVAudioSession sharedInstance];

    [self.audioSession setCategory:AVAudioSessionCategoryPlayback
                       withOptions:AVAudioSessionCategoryOptionMixWithOthers
                             error:&error];

    if (error) {
        NSLog(@"%@", error);
        return;
    }

    [self.audioSession setActive:YES error:&error];
    if (error) {
        NSLog(@"%@", error);
        return;
    }

    [self.audioSession addObserver:self
                forKeyPath:sessionVolumeKeyPath
                    options:(NSKeyValueObservingOptionOld | NSKeyValueObservingOptionNew | NSKeyValueObservingOptionInitial)
                    context:sessionContext];
}

- (void)removeVolumeListener {
    [self.audioSession removeObserver:self forKeyPath:sessionVolumeKeyPath];
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context {
    if (context == sessionContext) {
        if ([keyPath isEqualToString:sessionVolumeKeyPath]) {
            float newValue = [change[@"new"] floatValue];
            [self
                sendEventWithName:@"RTNVolumeListenerEventVolume"
                                body:@{@"volume" : [NSNumber numberWithFloat:newValue]}];
        }
    }
}

- (NSArray<NSString *> *)supportedEvents {
    return @[ @"RTNVolumeListenerEventVolume" ];
}

- (void)startObserving {
    [self addVolumeListener];
}

- (void)stopObserving {
    [self removeVolumeListener];
}

- (void)dealloc {
    [self removeVolumeListener];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeRTNVolumeListenerSpecJSI>(params);
}
#endif

@end
