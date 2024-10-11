package com.example.admin.common.service.gdcb.util;

import com.sshtools.j2ssh.transport.ConsoleKnownHostsKeyVerification;
import com.sshtools.j2ssh.transport.InvalidHostFileException;
import com.sshtools.j2ssh.transport.publickey.SshPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SftpHostsKeyVerification extends ConsoleKnownHostsKeyVerification { 
	protected static final Logger log = LoggerFactory.getLogger("LOGGER");
	public SftpHostsKeyVerification() 
	    throws InvalidHostFileException { 
	super(); 
	// Don't not do anything else 
	} 

	@Override 
	public void onHostKeyMismatch(String s, SshPublicKey sshpublickey, 
	    SshPublicKey sshpublickey1) { 
		try 
		{ 
		    allowHost(s, sshpublickey, false); 
		} 
		catch(Exception exception) 
		{ 
		    exception.printStackTrace(); 
		} 
	} 

	@Override 
	public void onUnknownHost(String s, SshPublicKey sshpublickey) {
		
		try 
		{ 
		    log.debug("The host " + s + " is currently unknown to the system"); 
		    log.debug("The host key fingerprint is: " + sshpublickey.getFingerprint()); 
		    log.debug("~~~Using Custom Key verification, allowing to pass through~~~"); 
		    allowHost(s, sshpublickey, false); 
		} 
		catch(Exception exception) 
		{ 
		    exception.printStackTrace(); 
		} 
	} 

}





