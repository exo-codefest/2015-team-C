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
    
        var cell = sender!.superview!!.superview as! OptionTableViewCell
        
        
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
