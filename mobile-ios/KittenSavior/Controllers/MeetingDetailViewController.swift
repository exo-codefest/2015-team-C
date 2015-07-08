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
        cell.participeButton.addTarget(self, action: "checkForOptionAtCell:", forControlEvents: UIControlEvents.TouchUpInside)
        if meeting.status == "closed" {
            cell.participeButton.enabled = false
        }
        if loaded {
            cell.desc.attributedText = self.attributedDesc(time)
            cell.participeButton.hidden = false
            cell.desc.hidden = false
            cell.loadingIndicator.hidden = true
            
            if time.selectedByUser(user) {
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
        var desc = ""
        if (time.participants.count == 0) {
            desc = "be the first participant of this schedule"
        } else {
            desc = "\(time.participants.count) participants"
            for username in time.participants {
                desc += ", "+username
            }
        }
        
        var attributedString = NSMutableAttributedString(string: desc)
        return attributedString
    }
    
    func checkForOptionAtCell(sender: AnyObject?) {
    
        var cell = sender!.superview!!.superview as! OptionTableViewCell
        var indexPath = self.tableView.indexPathForCell(cell)
        var time = meeting.options[indexPath!.row]
        if  time.selectedByUser(user) {
            time.participants.removeAtIndex(find(time.participants, user.username)!)
        } else {
            time.participants.append(user.username)
        }
        self.tableView.reloadData()
        self.sendRequetToSelectTime(time)
    }
    
    
    func sendRequetToSelectTime(time:Time) {
        var urlString = serverBaseURL+"kittenSavior/meetings/\(meeting.id)/options/\(time.time_id)/choices/"
        var request = NSMutableURLRequest(URL: NSURL(string: urlString)!)
        var configsession = NSURLSessionConfiguration.defaultSessionConfiguration()
        var session = NSURLSession(configuration: configsession)
        request.HTTPMethod = "POST"
         var params = ["participant":user.username, "choice":"\(time.selectedByUser(user))"] as Dictionary<String, String>
         var err: NSError?
        request.HTTPBody = NSJSONSerialization.dataWithJSONObject(params, options: nil, error: &err)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.addValue("application/json", forHTTPHeaderField: "Accept")
        var error:NSError?
        var task = session.dataTaskWithRequest(request, completionHandler: {data, response, error -> Void in
            println("Response: \(response)")
            var strData = NSString(data: data, encoding: NSUTF8StringEncoding)
            println("Body: \(strData)")
            var err: NSError?
            var json = NSJSONSerialization.JSONObjectWithData(data, options: .MutableLeaves, error: &err) as? NSDictionary
            
            // Did the JSONObjectWithData constructor return an error? If so, log the error to the console
            if(err != nil) {
                println(err!.localizedDescription)
                let jsonStr = NSString(data: data, encoding: NSUTF8StringEncoding)
                println("Error could not parse JSON: '\(jsonStr)'")
            }
            else {
                // The JSONObjectWithData constructor didn't return an error. But, we should still
                // check and make sure that json has a value using optional binding.
                if let parseJSON = json {
                    // Okay, the parsedJSON is here, let's get the value for 'success' out of it
                    var success = parseJSON["success"] as? Int
                    println("Succes: \(success)")
                }
                else {
                    // Woa, okay the json object was nil, something went worng. Maybe the server isn't running?
                    let jsonStr = NSString(data: data, encoding: NSUTF8StringEncoding)
                    println("Error could not parse JSON: \(jsonStr)")
                }
            }
        })
        
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
            "time_id":"time_id",
            "user": "username",
            "choice": "choice"])
        
        var path = "kittenSavior/meetings/\(meeting.id)/choices"
        
        var responseDescriptor = RKResponseDescriptor(mapping: mapping, method: RKRequestMethod.GET, pathPattern: path, keyPath: "choices", statusCodes: NSIndexSet(index: 200))
        
        objectManager.addResponseDescriptor(responseDescriptor)
        
        objectManager.getObjectsAtPath(path, parameters: nil, success: { (operation, mappingResult) -> Void in
            var objects:Array = mappingResult.array()
            if (objects.count>0){
                self.meeting.choices = objects
                self.meeting.initParticipantsForAllOptions()
                self.loaded = true;
                self.tableView.reloadData()
            } else {
            }
            }, failure:{ (operation, error) -> Void in
        })
        
    }


}
