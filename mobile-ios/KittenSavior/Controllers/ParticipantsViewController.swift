//
//  ParticipantsViewController.swift
//  KittenSavior
//
//  Created by Nguyen Manh Toan on 7/8/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit

class ParticipantsViewController: UITableViewController {
    
    var meeting:Meeting!
    var user:User!
    var participants:Array<User>!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.loadParticipants()
    }

    func loadParticipants () {
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 0
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 0
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("UserCell", forIndexPath: indexPath) as! UITableViewCell

        return cell
    }

}
