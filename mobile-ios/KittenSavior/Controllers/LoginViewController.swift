//
//  LoginViewController.swift
//  KittenSavor
//
//  Created by Nguyen Manh Toan on 7/6/15.
//  Copyright (c) 2015 eXo. All rights reserved.
//

import UIKit


class LoginViewController: UIViewController {


    @IBOutlet weak var loadingActivity: UIActivityIndicatorView!
    
    @IBOutlet weak var usernameTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    var user:User!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.user = User()
        if (NSUserDefaults.standardUserDefaults().objectForKey(DEFAULT_USERNAME) != nil)  {
            self.usernameTextField.text = NSUserDefaults.standardUserDefaults().objectForKey(DEFAULT_USERNAME) as! String
        } else {
            self.usernameTextField.placeholder = "username"
        }
        if (NSUserDefaults.standardUserDefaults().objectForKey(DEFAULT_PASSWORD) != nil)  {
            self.passwordTextField.text = NSUserDefaults.standardUserDefaults().objectForKey(DEFAULT_PASSWORD) as! String
        } else {
            self.passwordTextField.placeholder = "password"
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    

    @IBAction func loginAction(sender: AnyObject) {
        NSUserDefaults.standardUserDefaults().setValue(self.usernameTextField.text, forKey: DEFAULT_USERNAME)
        NSUserDefaults.standardUserDefaults().setValue(self.passwordTextField.text, forKey: DEFAULT_PASSWORD)
        self.resignFirstResponder()
        self.login(usernameTextField.text, password: passwordTextField.text)

    }
    
    func login(username:String, password:String){
        self.user.usename = username
        self.user.password = password
        var objectManager = RKObjectManager.sharedManager()
        if (objectManager==nil){
            var baseURLString = serverBaseURL
            var baseURL = NSURL(string: baseURLString)
            var client = AFHTTPClient(baseURL:baseURL)
            // initialize RestKit
            objectManager = RKObjectManager(HTTPClient: client)
        }
        RKObjectManager.setSharedManager(objectManager)
        objectManager.HTTPClient.setAuthorizationHeaderWithUsername(username, password: password);
        
        
        var mapping = RKObjectMapping(forClass: ServerInfo.self)
        var dictMapping = ["platformVersion":"platformVersion","platformRevision":"platformRevision", "currentRepoName":"currentRepoName","defaultWorkSpaceName":"defaultWorkSpaceName","userHomeNodePath":"userHomeNodePath"];
        mapping.addAttributeMappingsFromDictionary(dictMapping)
        
        var responseDescriptor = RKResponseDescriptor(mapping: mapping, method: RKRequestMethod.GET, pathPattern: "platform/info", keyPath: nil, statusCodes: NSIndexSet(index: 200))
        
        objectManager.addResponseDescriptor(responseDescriptor)
        
        self.loadingActivity.startAnimating()
        objectManager.getObjectsAtPath("platform/info", parameters: nil, success: { (operation, mappingResult) -> Void in
            self.performSegueWithIdentifier("finishedLogin", sender: nil)
            self.loadingActivity.stopAnimating()
            }, failure:{ (operation, mappingResult) -> Void in
                self.loadingActivity.stopAnimating()
                var alert = UIAlertView(title: "Login error", message: "", delegate: nil, cancelButtonTitle: "OK")
                alert.show()
        })
        
    }
    func configurationRestKit() {

    }
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if (segue.identifier == "finishedLogin"){
            var naviVC: UINavigationController = segue.destinationViewController as! UINavigationController;
            var meetingStreamVC = naviVC.viewControllers[0] as! MeetingStreamViewController
            meetingStreamVC.user = self.user            
        }
    }
    

}
