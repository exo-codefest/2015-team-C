//
//  MeetingTableViewCell.swift
//  KittenSavor
//
//  Created by Nguyen Manh Toan on 7/6/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit

class MeetingTableViewCell: UITableViewCell {

    @IBOutlet weak var meetingTitle: UILabel!
    
    @IBOutlet weak var meetingDesc: UILabel!
    
    @IBOutlet weak var meetingSchedules: UILabel!
    
    @IBOutlet weak var meettingStatus: UILabel!
    
    @IBOutlet weak var meetingStatusIcon: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    func configure (meeting: Meeting) {
        self.meetingTitle.text = meeting.name
        self.meetingDesc.text = meeting.desc
        self.meetingSchedules.text = meeting.stringSchedule()
        self.meettingStatus.text = meeting.status
        if (meeting.status == "opened"){
            self.meetingStatusIcon.image = UIImage(named: "opened.png")
        } else {
            self.meetingStatusIcon.image = UIImage(named: "closeed.png")            
        }
    }
}
