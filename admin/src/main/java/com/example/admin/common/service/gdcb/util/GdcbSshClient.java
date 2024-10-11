package com.example.admin.common.service.gdcb.util;


import com.sshtools.j2ssh.ScpClient;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.connection.ChannelInputStream;
import com.sshtools.j2ssh.connection.ChannelOutputStream;
import com.sshtools.j2ssh.connection.ChannelState;
import com.sshtools.j2ssh.io.IOStreamConnector;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.sftp.SftpFile;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GdcbSshClient {
    private Logger log = LoggerFactory.getLogger(getClass());

    private SshClient ssh;
    private SessionChannelClient client;
    private SftpClient sftpClient;
    private ScpClient scpClient;


    @Value("${LGUDCB.BILLING.IP}")
    private String ip;
    @Value("${LGUDCB.BILLING.PORT}")
    private int port;
    @Value("${LGUDCB.BILLING.ID}")
    private String id;
    @Value("${LGUDCB.BILLING.PWD}")
    private String pwd;

    public boolean connect() throws IOException {

        if (ssh == null) {
            ssh = new SshClient();
        } else if (ssh.isConnected()) {
            log.warn("Already Server Connected!({})", ssh.getServerId());
            return false;
        }

        if (ip == null || port <= 0) {
            log.error("Connecting Server Info is Null({}, {}", ip, port);

            throw new IllegalArgumentException("Required Server IP or Port is NULL");
        }

        if (id == null || pwd == null) {
            log.error("Connecting Account Info is Null({}, {}", id, pwd);

            throw new IllegalArgumentException("Required ID or PWD is NULL");
        }

        try {

            ssh.connect(ip, port, new AlwaysAllowingConsoleKnownHostsKeyVerification());

            PasswordAuthenticationClient pwdAuthClient = new PasswordAuthenticationClient();

            pwdAuthClient.setUsername(id);
            pwdAuthClient.setPassword(pwd);

            int authenticate = ssh.authenticate(pwdAuthClient);

            if (authenticate == AuthenticationProtocolState.COMPLETE) {
                log.warn("Login ok! ({})", id, pwd);

                this.scpClient = ssh.openScpClient();
                this.sftpClient = ssh.openSftpClient();

                return true;
            } else {
                log.error("Login fail! ({}, {})", id, pwd);
            }

            return false;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            return false;
        }
    }

    public boolean isConnected() {
        if (ssh != null && ssh.isConnected()) {
            return true;
        }

        return false;
    }

    public void disConnect() {

        try {
            if (client != null && client.isOpen()) {
                client.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        try {
            if (sftpClient != null) {
                sftpClient.quit();
                sftpClient = null;
            }
            if (scpClient != null) {
                scpClient = null;
            }
        } catch (IOException e) {
            try {
                if (sftpClient != null) {
                    sftpClient.quit();
                    sftpClient = null;
                }
                if (scpClient != null) {
                    scpClient = null;
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        if (ssh != null && ssh.isConnected()) {
            ssh.disconnect();
        }
    }

    /**
     * 여러 command 를 반복해서 호출하면 에러가 발생할 수 있음
     */
    public List<String> executeCommand(String command) {
        BufferedReader reader = null;

        List<String> result = null;

        try {

            if (command == null || "".equals(command.trim())) {
                return null;
            }

            if (client == null || client.isClosed()) {
                client = ssh.openSessionChannel();
            }

            if (client.executeCommand(command)) {
                log.debug("Command exceute OK");

                ChannelInputStream is = client.getInputStream();

                reader = new BufferedReader(new InputStreamReader(is));

                result = new ArrayList<String>();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    log.debug("Result: {}", line);

                    result.add(line);
                }
            } else {
                log.debug("Command execute fail!");
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return result;
    }

    public List<String> executeCommand(List<String> commands) {
        List<String> result = null;

        try {

            if (commands == null || commands.isEmpty()) {
                return null;
            }

            if (client == null || client.isClosed()) {
                client = ssh.openSessionChannel();
                client.requestPseudoTerminal("Linux", 160, 25, 0, 0 , "");

            }

            if(client.startShell()) {
                result = new ArrayList<>();

                ChannelInputStream in = client.getInputStream();
                ChannelOutputStream out = client.getOutputStream();

                result = new ArrayList<>();

                byte[] buffer = new byte[1024];
                int read = 0;

                // 로그인 메세지
                while (true) {
                    read = in.read(buffer);
                    String msg1 = new String(buffer, 0, read);

                    log.debug("Login Message: {}", msg1);

                    // 호스명 설정을 상용으로 변경
                    if(msg1.indexOf(GDCBConstants.HostPrompt_Real) > 0) {
                        log.debug("---------------------------------------");
                        break;
                    }
                }

                // shell 명령 수행
                for(String cmd : commands) {
                    log.info("Starting Command : {}", cmd);


                    out.write(cmd.getBytes("UTF-8"));
                    out.flush();

                    boolean promptReturned = false;

                    while (promptReturned == false && (read = in.read(buffer)) > 0) {
                        String msg = new String(buffer, 0, read);
                        // 호스명 설정을 상용으로 변경
                        if (msg != null && msg.trim().length() > 1 && msg.indexOf(GDCBConstants.HostPrompt_Real) < 0) {
                            log.debug("결과 메세지: {}", msg);

                            if(msg.startsWith("/")) {
                                result.add(msg);
                            } else {
                                log.debug("기타 메세지 제외: {}", msg);
                            }

							/*
							if(msg.indexOf("grep") >= 0) {
								log.debug("Command 메세지 제외: {}", msg);
							} else if(msg.indexOf("파일") >= 0 || msg.indexOf("디렉토리") >= 0) {
								log.debug("에러 메세지 제외: {}", msg);

								// 에러 따위는 돌리지 않음
//								return result;
							} else {
								// 처음 발견하고 다음은 수행하지 않으려면 리턴
								result.add(msg);
							}
							*/
                            // 호스명 설정을 상용으로 변경
                        } else if(!StringUtil.isEmpty(msg) && msg.indexOf(GDCBConstants.HostPrompt_Real) >= 0){
                            promptReturned = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return result;
    }

    public String executeCmd(String commnad) {
        String result = null;

        try {

            if (commnad == null || "".equals(commnad.trim())) {
                return null;
            }

            if (client == null || client.isClosed()) {
                client = ssh.openSessionChannel();
            }

            if (client.executeCommand(commnad)) {
                IOStreamConnector output = new IOStreamConnector();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                output.connect(client.getInputStream(), bos);
                client.getState().waitForState(ChannelState.CHANNEL_CLOSED);

                result = bos.toString();
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return result;
    }

    public List<String> getDir(String path) throws Exception {
        try {
            sftpClient.cd(path);

            @SuppressWarnings("unchecked") List<SftpFile> list = sftpClient.ls();

            if (list != null && !list.isEmpty()) {
                log.debug("Dir file count: {}", list.size());

                List<String> result = new ArrayList<String>();

                for (SftpFile file : list) {
                    log.debug("{} [{}]", file.getFilename(), file.getAttributes().getSize());

                    if (file.getFilename().startsWith("request")) {
                        result.add(path + "/" + file.getFilename());
                    }
                }

                return result;

            } else {
                log.warn("{} 디렉토리에 파일이 없음", path);
            }

        } catch (FileNotFoundException ex) {
            log.debug(ex.getMessage(), ex);

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

        }

        return null;
    }

    public void fileUpload(String localFile, String upLoadFileName) throws Exception {

        try {
            log.debug("◀FTP file upload 요청: {}, {}", localFile, upLoadFileName);

            sftpClient.put(localFile, upLoadFileName);

            log.info("◀FTP file upload OK: {}", upLoadFileName);
        } catch (IOException ioEx) {
            log.error(ioEx.getMessage(), ioEx);
        }
    }

    public void fileDownload(String remotePath, String localPath, String downFileName) throws Exception {
        String strRemote = null;
        String strLocal = null;

        try {
            strRemote = remotePath + "/" + downFileName;
            strLocal = localPath + "/" + downFileName;

            File localGooglePathDir = new File(localPath);

            if (!(localGooglePathDir.exists() && localGooglePathDir.isDirectory())) {
                localGooglePathDir.mkdirs();

                log.info("DOWN FOLDER 디렉토리가 존재하지 않아 생성: {}", localGooglePathDir);
            }

            sftpClient.get(strRemote, strLocal);

            log.info("▶FTP file download: {}", strRemote);

        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);

            // 파일 다운로드 재시도
            scpClient.get(strLocal, strRemote, false);
        }
    }
}
