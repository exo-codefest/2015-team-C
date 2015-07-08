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
    var meetingArray:Array<Meeting> = Array()
    
    override func viewDidLoad() {
        self.navigationItem.title = self.user.usename + "'s meetings"
        self.loadMeetingArray()
        
    }

    func loadMeetingArray() {
        var objectManager = RKObjectManager.sharedManager()
        RKObjectManager.setSharedManager(objectManager)
        //TODO re-do the mapping for Meeting class 
        var mapping = RKObjectMapping(forClass: ServerInfo.self)
        var dictMapping = ["platformVersion":"platformVersion","platformRevision":"platformRevision", "currentRepoName":"currentRepoName","defaultWorkSpaceName":"defaultWorkSpaceName","userHomeNodePath":"userHomeNodePath"];
        mapping.addAttributeMappingsFromDictionary(dictMapping)
        
        var responseDescriptor = RKResponseDescriptor(mapping: mapping, method: RKRequestMethod.GET, pathPattern: "private/platform/info", keyPath: nil, statusCodes: NSIndexSet(index: 200))
        
        objectManager.addResponseDescriptor(responseDescriptor)
        
        objectManager.getObjectsAtPath("private/platform/info", parameters: nil, success: { (operation, mappingResult) -> Void in
            var objects:Array = mappingResult.array()
            if (objects.count>0){
                //TODO replace this.
                var m1 = Meeting();
                m1.name = "Kitten Savoir"
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
            } else {
                self.failure();
            }
            }, failure:{ (operation, error) -> Void in
                self.failure();
        })

    }
    func failure() {
        var alert = UIAlertView(title: "Unable to load the meetings", message: "", delegate: nil, cancelButtonTitle: "OK")
        alert.show()
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
            var meeting = meetingArray[indexPath.row]
            cell.configure(meeting)
            return cell
    }
    // MARK: - Navigation

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if (segue.identifier == "showMeetingDetail"){
            var meetingDetailVC = segue.destinationViewController as! MeetingDetailViewController
            var indexPath = self.tableView.indexPathForSelectedRow()
            var meeting = meetingArray[indexPath!.row]
            meetingDetailVC.meeting = meeting
        }
        self.tableView.reloadData()
    }

}
