//
//  MeetingDetailViewController.swift
//  KittenSavor
//
//  Created by Nguyen Manh Toan on 7/6/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit

class MeetingDetailViewController: UIViewController {
    
    var meeting:Meeting!
    var choiceArray:Array<Choice>!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.title = meeting.name;
        self.loadChoice()
    }

    func loadChoice() {    
        
        var objectManager = RKObjectManager.sharedManager()
        RKObjectManager.setSharedManager(objectManager)
        
        //TODO: change the setting
        var mapping = RKObjectMapping(forClass: ServerInfo.self)
        var dictMapping = ["platformVersion":"platformVersion","platformRevision":"platformRevision", "currentRepoName":"currentRepoName","defaultWorkSpaceName":"defaultWorkSpaceName","userHomeNodePath":"userHomeNodePath"];
        mapping.addAttributeMappingsFromDictionary(dictMapping)
        
        var responseDescriptor = RKResponseDescriptor(mapping: mapping, method: RKRequestMethod.GET, pathPattern: "private/platform/info", keyPath: nil, statusCodes: NSIndexSet(index: 200))
        
        objectManager.addResponseDescriptor(responseDescriptor)
        
        objectManager.getObjectsAtPath("private/platform/info", parameters: nil, success: { (operation, mappingResult) -> Void in
            var objects:Array = mappingResult.array()
            if (objects.count>0){
            } else {
                self.failure();
            }
            }, failure:{ (operation, error) -> Void in
                self.failure();
        })
        
    }
    
    func failure() {
        var alert = UIAlertView(title: "Login error", message: "", delegate: nil, cancelButtonTitle: "OK")
        alert.show()
        
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
    
        return cell
    }
    
    func checkForOptionAtCell(sender: AnyObject?) {
    
        var cell = sender!.superview as! OptionTableViewCell
        
        
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
