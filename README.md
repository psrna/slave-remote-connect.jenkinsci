Slave Remote Connect jenkins plugin
===================================

This plug-in provides utilities to access jenkins slave via VNC/RDP protocol directly from your browser.


Prerequisities
--------------
* Install apache tomcat web server
* Build slave-remote-connect war and deploy to the running tomcat instance
   * git clone git@github.com:psrna/slave-remote-connect.server.git
   * Follow instructions in README.md on how to build and deploy slave-remote-connect war 


How to Configure
-----------------
1. In main jenkins configuration setup the address of the previously installed war
2. Enable VNC/RDP connect in job configuration


Security Concerns
-----------------
This version of plugin send vnc credentials over http GET. This is a security concern. Use it with caution only on a secured isolated 
network on your own risk.





