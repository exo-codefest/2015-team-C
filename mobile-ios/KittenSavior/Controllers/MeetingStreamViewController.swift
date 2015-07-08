//
//  MeetingStreamViewController.swift
//  KittenSavor
//
//  Created by Nguyen Manh Toan on 7/6/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit

class MeetingStreamViewController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    var user:User!
    var serverInfo:ServerInfo!
    var meetingArray:Array<AnyObject> = Array()
    
    override func viewDidLoad() {
        self.navigationItem.title = self.user.username + "'s meetings"
        self.updateUserInfo()
        self.loadMeetingArray()
        
    }

    func loadMeetingArray() {
        var objectManager = RKObjectManager.sharedManager()
        if (objectManager==nil){
            var baseURLString = serverBaseURL
            var baseURL = NSURL(string: baseURLString)
            var client = AFHTTPClient(baseURL:baseURL)
            // initialize RestKit
            objectManager = RKObjectManager(HTTPClient: client)
        }
        RKObjectManager.setSharedManager(objectManager)
        objectManager.HTTPClient.setAuthorizationHeaderWithUsername(user.username, password: user.password);

        //TODO re-do the mapping for Meeting class 
        var mapping = RKObjectMapping(forClass: Meeting.self)
        var dictMapping = ["id":"id", "name":"name", "description":"desc", "creator":"creator", "status":"status", "participants":"participants"];
        mapping.addAttributeMappingsFromDictionary(dictMapping)
        
        var optionsMapping = RKObjectMapping(forClass: Time.self)

        optionsMapping.addAttributeMappingsFromDictionary([
            "id":"time_id",
            "start_timestamp":"start_time",
            "end_timestamp":"end_time"])
        
        mapping.addPropertyMapping(RKRelationshipMapping(fromKeyPath: "options", toKeyPath: "options", withMapping: optionsMapping))
        
        var responseDescriptor = RKResponseDescriptor(mapping: mapping, method: RKRequestMethod.GET, pathPattern: "kittenSavior/meetings", keyPath: "meetings", statusCodes: NSIndexSet(index: 200))
        
        objectManager.addResponseDescriptor(responseDescriptor)
        
        objectManager.getObjectsAtPath("kittenSavior/meetings", parameters: ["user":user.username], success: { (operation, mappingResult) -> Void in
            var objects:Array = mappingResult.array()
            if (objects.count>0){
                self.meetingArray = objects;
                self.tableView.reloadData()
            } else {
                self.failure();
            }
            }, failure:{ (operation, error) -> Void in
                self.failure();
        })

    }
    func failure() {
//        var alert = UIAlertView(title: "Unable to load the meetings", message: "", delegate: nil, cancelButtonTitle: "OK")
//        alert.show()
        // mini test
        var m1 = Meeting();
        m1.name = "Bia hơi"
        m1.desc = "Kitten Savoir ale hop"
        m1.status = "opened"
        
        var now = NSDate()
        var timestamp = now.timeIntervalSince1970 as Double
        var time = Time()
        time.start_time = timestamp - 3600
        time.end_time = timestamp - 600
        m1.options = Array(arrayLiteral: time)
        
        self.meetingArray = Array(arrayLiteral: m1)
        self.tableView.reloadData()

    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func logoutAction(sender: AnyObject) {
        self.dismissViewControllerAnimated(true, completion: nil)
    }
    
    // MARK: - Table View Delegate
    func numberOfSectionsInTableView(tableView: UITableView) -> Int{
        return 1;
    }
    func tableView(tableView: UITableView,
        numberOfRowsInSection section: Int) -> Int {
        return meetingArray.count;
    }
    func tableView(tableView: UITableView,
        cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
            let cell = tableView.dequeueReusableCellWithIdentifier("meettingInfoCell", forIndexPath: indexPath) as! MeetingTableViewCell
            var meeting = meetingArray[indexPath.row] as! Meeting
            cell.configure(meeting)
            return cell
    }
    // MARK: - Navigation

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if (segue.identifier == "showMeetingDetail"){
            var meetingDetailVC = segue.destinationViewController as! MeetingDetailViewController
            var indexPath = self.tableView.indexPathForSelectedRow()
            var meeting = meetingArray[indexPath!.row] as! Meeting
            meetingDetailVC.meeting = meeting
        }
        self.tableView.reloadData()
    }

    
    func updateUserInfo () {
        var objectManager = RKObjectManager.sharedManager()
        RKObjectManager.setSharedManager(objectManager)
        //TODO re-do the mapping for Meeting class
        var userMapping = RKObjectMapping(forClass: User.self)
        var dictMapping = ["usename":"usename", "timezone":"timezone"];
        userMapping.addAttributeMappingsFromDictionary(dictMapping)

        var responseDescriptor = RKResponseDescriptor(mapping: userMapping, method: RKRequestMethod.GET, pathPattern: "/rest/kittenSavior/users/\(user.username)", keyPath:nil, statusCodes: NSIndexSet(index: 200))
        
        objectManager.addResponseDescriptor(responseDescriptor)
        
        
        objectManager.getObjectsAtPath("/rest/kittenSavior/users/\(user.username)", parameters: nil, success: { (operation, mappingResult) -> Void in
            var objects:Array = mappingResult.array()
            if (objects.count>0){
                var newUser = objects[0] as! User
                self.user.timezone = newUser.timezone
//                self.user.firstname = newUser.firstname
//                self.user.lastname = newUser.lastname
            } else {
//                self.failure();
            }
            }, failure:{ (operation, error) -> Void in
//                self.failure();
        })

    }
}
