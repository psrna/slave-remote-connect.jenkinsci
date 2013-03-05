package org.jenkinsci.plugins.slaveremoteconnect;

import hudson.model.Action;

public class ConnectRDPAction implements Action {

	private String connectUrl;
	
	public ConnectRDPAction(String connectUrl) {
		this.connectUrl = connectUrl;
	}
	
	public String getIconFileName() {
		return "/plugin/slave-remote-connect.jenkinsci/images/rdp.png";
	}

	public String getDisplayName() {
		return "RDP Connect to slave";
	}

	public String getUrlName() {
		return connectUrl;
	}

}
