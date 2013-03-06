package org.jenkinsci.plugins.slaveremoteconnect;

import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jenkins.model.Jenkins;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class SlaveRemoteConnect extends BuildWrapper  {
	
	
	
	@DataBoundConstructor
	public SlaveRemoteConnect() {
	}
	
	private String getVNCLine(List<String> log){
		
		String vnc_line = null;
		for(String s : log){
    		if(s.contains("/usr/bin/vncserver"))
    			vnc_line = s;
    	}
		return vnc_line;
	}
	
	private int getVNCDisplay(String vnc_line){
		
		Pattern p = Pattern.compile("(.*:)(\\d+)(.*)");
		Matcher m = p.matcher(vnc_line);
		
		if(m.matches())
			return Integer.valueOf(m.group(2));
		else
			return 0;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Environment setUp(AbstractBuild build, Launcher launcher,
			BuildListener listener) throws IOException, InterruptedException {

		String hostname = build.getEnvironment(listener).get("HOSTNAME");
		String username = build.getEnvironment(listener).get("USERNAME");
		String password = build.getEnvironment(listener).get("PASSWORD");
		String vnc_line = getVNCLine(build.getLog(100));
		String serverUrl = getDescriptor().getServerAddress(); 
		
		Action action = null;
		if(launcher.isUnix()){
			if(vnc_line == null){
				listener.getLogger().println("slave-remote-connect: cannot see vncserver started!");
				return new Environment(){};
			}
			//isUnix and VNC started
			int display = getVNCDisplay(vnc_line);
			String connectUrl = serverUrl + 
					          "?hostname=" + hostname + 
					          "&port=" + (5900 + display) + 
					          "&password=" + password + 
					          "&protocol=vnc"; 
			
			action = new ConnectVNCAction(build, connectUrl);
		
		}else{
			String connectUrl = serverUrl + 
					           "?hostname=" + hostname +
					           "&port=3389" + 
					           "&username=" + username + 
					           "&password=" + password + 
					           "&protocol=rdp";
			
			action = new ConnectRDPAction(connectUrl);
		}
		
		if(action != null){
			build.addAction(action);
			listener.getLogger().println("slave-remote-connect: enabled!");
		}
		
		return new Environment(){};
	}

	public DescriptorImpl getDescriptor(){
		return (DescriptorImpl) Jenkins.getInstance().getDescriptor(getClass());
	}
	
	@Extension(ordinal = -1)
	public static final class DescriptorImpl extends BuildWrapperDescriptor {

		private String serverAddress;

		public DescriptorImpl() {
			load();
		}
		
		public String getServerAddress(){
			return serverAddress;
		}
		
		@Override
		public boolean isApplicable(AbstractProject<?, ?> item) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Enable VNC/RDP Connect to Slave";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject json)
				throws hudson.model.Descriptor.FormException {
			
			serverAddress = json.getString("serverAddress");
			save();
			return true;
		}
		
	}
}
