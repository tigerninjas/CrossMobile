// (c) 2020 under LGPL by CrossMobile plugin tools

// crossmobile_ios_foundation_NSURLConnectionDelegate definition

#import "xmlvm.h"
#import <CoreGraphics/CoreGraphics.h>
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@class crossmobile_ios_foundation_NSError;
@class crossmobile_ios_foundation_NSURLConnection;

CM_EXPORT_CLASS
@protocol crossmobile_ios_foundation_NSURLConnectionDelegate
- (void) didFailWithError___crossmobile_ios_foundation_NSURLConnection_crossmobile_ios_foundation_NSError:(NSURLConnection*) connection :(NSError*) error ;
@end
