// (c) 2020 under LGPL by CrossMobile plugin tools

// crossmobile_ios_mapkit_MKReverseGeocoderDelegate definition

#import "xmlvm.h"
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>
@class crossmobile_ios_foundation_NSError;
@class crossmobile_ios_mapkit_MKPlacemark;
@class crossmobile_ios_mapkit_MKReverseGeocoder;

CM_EXPORT_CLASS
@protocol crossmobile_ios_mapkit_MKReverseGeocoderDelegate
- (void) didFailWithError___crossmobile_ios_mapkit_MKReverseGeocoder_crossmobile_ios_foundation_NSError:(MKReverseGeocoder*) geocoder :(NSError*) error ;
- (void) didFindPlacemark___crossmobile_ios_mapkit_MKReverseGeocoder_crossmobile_ios_mapkit_MKPlacemark:(MKReverseGeocoder*) geocoder :(MKPlacemark*) placemark ;
@end
