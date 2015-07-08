//
//  OptionTableViewCell.swift
//  KittenSavior
//
//  Created by Nguyen Manh Toan on 7/8/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit

class OptionTableViewCell: UITableViewCell {

    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var desc: UILabel!
    @IBOutlet weak var participeButton: UIButton!
    
    @IBOutlet weak var loadingIndicator: UIActivityIndicatorView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    func configure (time: Time) {
        self.title.text = time.getTitle()
    }

    
}

