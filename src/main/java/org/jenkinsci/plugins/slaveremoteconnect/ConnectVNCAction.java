package org.jenkinsci.plugins.slaveremoteconnect;

import hudson.model.Action;
import hudson.model.AbstractBuild;

public class ConnectVNCAction implements Action {

	@SuppressWarnings("rawtypes")
	private AbstractBuild build;
	private String connectUrl;
	
	@SuppressWarnings("rawtypes")
	public ConnectVNCAction(AbstractBuild build, String connectUrl) {
		this.build = build;
		this.connectUrl = connectUrl;
	}
	
	public String getIconFileName() {
		if(build.isBuilding())
			return "/plugin/slave-remote-connect.jenkinsci/images/rdp.png";
		else
			return null;
	}

	public String getDisplayName() {
		if(build.isBuilding()) //can connect only when build is runnig, otherwise vncserver is dead
			return "Connect to Slave via VNC";
		else
			return null;
	}

	public String getUrlName() {
		
		return connectUrl;
	}

}
