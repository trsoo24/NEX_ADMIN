package com.example.admin.common.service.gdcb.util;

import com.sshtools.j2ssh.transport.ConsoleKnownHostsKeyVerification;
import com.sshtools.j2ssh.transport.InvalidHostFileException;
import com.sshtools.j2ssh.transport.publickey.SshPublicKey;

public class AlwaysAllowingConsoleKnownHostsKeyVerification extends ConsoleKnownHostsKeyVerification {

    public AlwaysAllowingConsoleKnownHostsKeyVerification() throws InvalidHostFileException {
        super();
    }

    @Override
    public void onUnknownHost(String host, SshPublicKey key) {
        try {
            allowHost(host, key, false);
        } catch (InvalidHostFileException e) {
            e.printStackTrace();
        }
    }
}
