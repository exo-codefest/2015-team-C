//
//  MeetingDetailViewController.swift
//  KittenSavor
//
//  Created by Nguyen Manh Toan on 7/6/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit

class MeetingDetailViewController: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    var meeting:Meeting!
    var user:User!
    var loaded:Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.title = meeting.name;
        self.loadChoice()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    

    
    // MARK: - Table view data source
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return meeting.options.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("OptionTableViewCell", forIndexPath: indexPath) as! OptionTableViewCell
        var time = meeting.options[indexPath.row]
        cell.configure(time)
        cell.desc.attributedText = self.attributedDesc(time)
        cell.participeButton.addTarget(self, action: "checkForOptionAtCell:", forControlEvents: UIControlEvents.TouchUpInside)
        if loaded {
            cell.participeButton.hidden = false
            cell.desc.hidden = false
            cell.loadingIndicator.hidden = true
            
            if time.selectedByThisUser {
                cell.participeButton.setImage(UIImage(named: "check.png"), forState: UIControlState.Normal)
            } else {
                cell.participeButton.setImage(UIImage(named: "uncheck.png"), forState: UIControlState.Normal)
            }
        } else {
            cell.participeButton.hidden = true
            cell.desc.hidden = true
            cell.loadingIndicator.hidden = false
        }
        return cell
    }
    
    func attributedDesc(time:Time) ->NSAttributedString{
        var attributedString = NSAttributedString(string: "2 participants")
        return attributedString
    }
    
    func checkForOptionAtCell(sender: AnyObject?) {
    
        var cell = sender!.superview!!.superview as! OptionTableViewCell
        var indexPath = self.tableView.indexPathForCell(cell)
        var time = meeting.options[indexPath!.row]
        time.selectedByThisUser = !time.selectedByThisUser
        if time.selectedByThisUser {
            cell.participeButton.setImage(UIImage(named: "check.png"), forState: UIControlState.Normal)
        } else {
            cell.participeButton.setImage(UIImage(named: "uncheck.png"), forState: UIControlState.Normal)
        }
        self.sendRequetToSelectTime(time)
        
    }
    
    
    func sendRequetToSelectTime(time:Time) {
        var request = NSMutableURLRequest(URL: NSURL(string: (serverBaseURL+"kittenSavior/meetings/\(meeting.id)/choices/"))!)
        var session = NSURLSession.sharedSession()
        request.HTTPMethod = "POST"
         var params = ["time_id":"\(time.time_id)", "user":user.username, "choice":"\(time.selectedByThisUser)"] as Dictionary<String, String>
         var err: NSError?
        request.HTTPBody = NSJSONSerialization.dataWithJSONObject(params, options: nil, error: &err)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.addValue("application/json", forHTTPHeaderField: "Accept")
        var task = session.dataTaskWithRequest(request, completionHandler:nil)
        task.resume()
        
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    func loadChoice() {
        
        var objectManager = RKObjectManager.sharedManager()
        RKObjectManager.setSharedManager(objectManager)
        
        //TODO: change the setting
        var mapping = RKObjectMapping(forClass: Choice.self)
        mapping.addAttributeMappingsFromDictionary([
            "id":"time_id",
            "username": "username",
            "choice": "choice"])
        
        var path = "kittenSavior/meetings/\(meeting.id)/choices"
        
        var responseDescriptor = RKResponseDescriptor(mapping: mapping, method: RKRequestMethod.GET, pathPattern: path, keyPath: "choices", statusCodes: NSIndexSet(index: 200))
        
        objectManager.addResponseDescriptor(responseDescriptor)
        
        objectManager.getObjectsAtPath(path, parameters: nil, success: { (operation, mappingResult) -> Void in
            var objects:Array = mappingResult.array()
            if (objects.count>0){
                self.meeting.choices = objects
                self.loaded = true;
                self.tableView.reloadData()
            } else {
            }
            }, failure:{ (operation, error) -> Void in
        })
        
    }


}
