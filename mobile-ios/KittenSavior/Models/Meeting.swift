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
    
    func loadChoice() {
        
        var objectManager = RKObjectManager.sharedManager()
        RKObjectManager.setSharedManager(objectManager)
        
        //TODO: change the setting
        var mapping = RKObjectMapping(forClass: Choice.self)
        mapping.addAttributeMappingsFromDictionary([
            "id":"time_id",
            "username": "username",
            "choice": "choice"])
        
        var path = "/rest/kittenSavior/meetings/\(id)/choices"
        
        var responseDescriptor = RKResponseDescriptor(mapping: mapping, method: RKRequestMethod.GET, pathPattern: path, keyPath: nil, statusCodes: NSIndexSet(index: 200))
        
        objectManager.addResponseDescriptor(responseDescriptor)
        
        objectManager.getObjectsAtPath(path, parameters: nil, success: { (operation, mappingResult) -> Void in
            var objects:Array = mappingResult.array()
            if (objects.count>0){
                self.choices = objects
            } else {
            }
            }, failure:{ (operation, error) -> Void in
        })
        
    }
    
    func getParticipantsList (time:Time) -> Array<String> {
        var a = Array<String>()
        for c in self.choices as! Array<Choice>  {
            if (c.choice == true) && (c.time_id == time.time_id){
                a.append(c.username!)
            }
        }
        return a
    }

}
