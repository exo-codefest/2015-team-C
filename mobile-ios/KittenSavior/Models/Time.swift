//
//  Time.swift
//  KittenSavor
//
//  Created by Nguyen Manh Toan on 7/6/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit

class Time: NSObject {
    var time_id:Int = 0
    var start_time:Double = 0
    var end_time:Double = 0
    
    func getTitle() -> String {
        var start_date = NSDate(timeIntervalSince1970:self.start_time )
        var end_date = NSDate(timeIntervalSince1970: self.end_time)
        var dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat =  "MMM dd"
        var hourFormatter = NSDateFormatter()
        hourFormatter.dateFormat = "hh:mm"
        var zoneFormatter = NSDateFormatter()
        zoneFormatter.dateFormat = "zzz"
        var s = zoneFormatter.stringFromDate(start_date)
        
        var title = ""

        if dateFormatter.stringFromDate(start_date) == dateFormatter.stringFromDate(end_date) {
            title = dateFormatter.stringFromDate(start_date) + ": " + hourFormatter.stringFromDate(start_date) + " - " + hourFormatter.stringFromDate(end_date)
        } else {
            title = dateFormatter.stringFromDate(start_date) + " " + hourFormatter.stringFromDate(start_date) + " - "  +  dateFormatter.stringFromDate(end_date) + " " + hourFormatter.stringFromDate(end_date)
        }
        return title
    }
    
}
