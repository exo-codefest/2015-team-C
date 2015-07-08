//
//  Meeting.swift
//  KittenSavor
//
//  Created by Nguyen Manh Toan on 7/6/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit

class Meeting: NSObject {

    var name:String!
    var desc:String!
    var creator:String!
    var status:String!
    var participants:Array<String>!
    var options:Array<Time> = Array()
    var choices:Array<AnyObject>!
    var id:Int = 0
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
    
    func initParticipantsForAllOptions() {
        for time in self.options {
            time.participants = self.getParticipantsList(time)
        }
    }
    
    func getParticipantsList (time:Time) -> Array<String> {
        var a = Array<String>()
        if self.choices != nil {
            for c in self.choices as! Array<Choice>  {
                if (c.choice == true) && (c.time_id == time.time_id){
                    a.append(c.username!)
                }
            }
            
        }
        return a
    }

}
