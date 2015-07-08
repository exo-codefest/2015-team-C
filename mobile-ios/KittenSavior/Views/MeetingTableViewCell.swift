//
//  MeetingTableViewCell.swift
//  KittenSavor
//
//  Created by Nguyen Manh Toan on 7/6/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit

class MeetingTableViewCell: UITableViewCell {


    @IBOutlet weak var bgView: UIView!
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
        self.bgView.layer.cornerRadius = 5.0;
        self.bgView.layer.borderColor = UIColor(white: 0.0, alpha: 1.0).CGColor
        self.bgView.layer.borderWidth = 0.5;
        
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
