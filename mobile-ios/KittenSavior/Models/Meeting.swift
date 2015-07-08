//
//  Meeting.swift
//  KittenSavor
//
//  Created by Nguyen Manh Toan on 7/6/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit

class Meeting: NSObject {
    var meeting_id:Int!
    var name:String!
    var desc:String!
    var creator:String!
    var status:String!
    var participants:Array<String>!
    var options:Array<Time>!
    
    func stringSchedule()->String {
        var schelduleDesc = "" as String
        for time in self.options {
            if schelduleDesc.isEmpty {
                schelduleDesc = time.getTitle()
            } else {
              schelduleDesc += " // " + time.getTitle()
            }
        }
        return schelduleDesc
    }
}
