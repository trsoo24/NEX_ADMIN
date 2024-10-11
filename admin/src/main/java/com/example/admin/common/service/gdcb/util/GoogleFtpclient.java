package com.example.admin.common.service.gdcb.util;

import com.sshtools.j2ssh.ScpClient;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PublicKeyAuthenticationClient;
import com.sshtools.j2ssh.configuration.ConfigurationLoader;
import com.sshtools.j2ssh.sftp.SftpFile;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKey;
import com.sshtools.j2ssh.transport.publickey.SshPrivateKeyFile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleFtpclient {
    private Logger log = LoggerFactory.getLogger(getClass());
    // SSH 클라이언트
    private SshClient sshClient;
    private SftpClient sftpClient;
    private ScpClient scpClient;

    @Value("${FTP.Google.IP}")
    private String ip;
    @Value("${FTP.Google.Port}")
    private int port;
    @Value("${FTP.Google.Account}")
    private String account;
    @Value("${FTP.Google.keyFile}")
    private String keyFilePath;


    public boolean connect() throws Exception {
        try {

            if (sshClient == null) {
                log.info("◀ FTP 연결 초기화: {}, {}", ip, port);
                log.info("◀ FTP 사용자 계정: {}, {}", account, null);

                // 다운로드 할때 sftp client 객체 생성
                ConfigurationLoader.initialize(false);
                sshClient = new SshClient(); // known host 를 체크하지 않음.
                sshClient.setSocketTimeout(60000);

                // known host 등록하는 로직일 경우 다음을 참고
                // http://sacrosanctblood.blogspot.com/2008/07/j2ssh-on-authentication-removing-user.html
                sshClient.connect(ip, port, new SftpHostsKeyVerification());

                PublicKeyAuthenticationClient pk = new PublicKeyAuthenticationClient();
                pk.setUsername(account);

                SshPrivateKeyFile keyFile = SshPrivateKeyFile.parse(new File(keyFilePath));

                // 패스워드 설정
                SshPrivateKey key = keyFile.toPrivateKey(null);
                pk.setKey(key);

                int result = sshClient.authenticate(pk);

                if (result != AuthenticationProtocolState.COMPLETE) {
//					ValidationUtil.validationCheck(SchedulerConstants.INTERNAL_SFTP_ERROR_CD);
                }

                sftpClient = sshClient.openSftpClient();
                scpClient = sshClient.openScpClient();

                log.info("◀ FTP Connecting OK");

            } else {
                log.warn("◇ FTP 연결이 존재 합니다.");

                // 업로드할때 기존객체 사용
                sftpClient = sshClient.openSftpClient();
                scpClient = sshClient.openScpClient();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);

            // FTP 연결에 문제가 있는 경우 종료한다.
            if (sshClient != null) {
                this.disconnection();
            }

            throw ex;
//			ValidationUtil.validationCheck(SchedulerConstants.INTERNAL_SFTP_ERROR_CD);
        }

        return true;
    }

    /**
     * 구글FTP 서버의 과금파일 목록을 조회한다.
     * @param path
     * @return List<과금파일명>
     * @throws Exception
     */
    public List<String> getRequestFile(String path) throws Exception {
        try {
            return ftpDir(path, GDCBConstants.Prefix_RequstFile);
        } catch (FileNotFoundException ex) {
            log.warn("Google FTP Dir: {}", ex.getMessage());
            log.debug(ex.getMessage(), ex);

            throw ex;
//			ValidationUtil.validationCheck(SchedulerConstants.DOWN_FOLDER_NOT_FOUND_CD);
        } catch (Exception ex) {
            log.error("Google FTP Dir: {}", path);
            log.error(ex.getMessage(), ex);

            throw ex;
//			ValidationUtil.validationCheck(SchedulerConstants.DOWN_FOLDER_NOT_FOUND_CD);
        }

    }

    /**
     * 가장 최근의 outStanding 파일을 조회한다.
     *
     * @return [path, fileName] 가장 최근의 outStanding 파일명
     * @throws Exception
     */
    public String getLastOutStading() throws Exception {

        Calendar toDay = Calendar.getInstance();

        StringBuilder sb = new StringBuilder();

        int tDay = toDay.get(Calendar.DAY_OF_MONTH);

        sb.append("/");
        sb.append(toDay.get(Calendar.YEAR));
        sb.append("/");
        sb.append(toDay.get(Calendar.MONTH) + 1);
        sb.append("/");

        if (tDay < 10) {
            sb.append(0);
            sb.append(tDay);
        } else {
            sb.append(toDay.get(Calendar.DAY_OF_MONTH));
        }

        try {

            List<String> result = this.ftpDir(sb.toString(), GDCBConstants.Prefix_OutstandingFile);

            if(result == null) {
                // 이전 날짜로 다시 조회
                toDay.add(Calendar.DATE, -1);

                int yDay = toDay.get(Calendar.DAY_OF_MONTH);

                sb = new StringBuilder();
                sb.append("/");
                sb.append(toDay.get(Calendar.YEAR));
                sb.append("/");
                sb.append(toDay.get(Calendar.MONTH) + 1);
                sb.append("/");

                if (yDay < 10) {
                    sb.append(0);
                    sb.append(yDay);
                } else {
                    sb.append(toDay.get(Calendar.DAY_OF_MONTH));
                }

                result = this.ftpDir(sb.toString(), GDCBConstants.Prefix_OutstandingFile);
            }

            Collections.sort(result);;

            return result.get(result.size() - 1);

        } catch (IOException ex) {
            // 이전 날짜로 다시 조회
            toDay.add(Calendar.DATE, -1);

            sb = new StringBuilder();
            sb.append("/");
            sb.append(toDay.get(Calendar.YEAR));
            sb.append("/");
            sb.append(toDay.get(Calendar.MONTH) + 1);
            sb.append("/");
            sb.append(toDay.get(Calendar.DAY_OF_MONTH));

            List<String> result = this.ftpDir(sb.toString(), GDCBConstants.Prefix_OutstandingFile);

            if (result != null && !result.isEmpty()) {

                Collections.sort(result);;

                return result.get(result.size() - 1);
            }
        }

        return null;
    }

    /**
     * 하나의 파일을 업로드 한다( * 파일경로는 전체 경로를 사용한다.)
     * @param localFile (로컬 파일)
     * @param upLoadFileName (업로드 파일)
     * @throws Exception
     */
    public void upload(String localFile, String upLoadFileName) throws Exception {

        // [김팔수] 업로드가 실패인 모든경우에 SftpException 발생하는 구조로 나와야한다.
        try {
            log.debug("◀FTP file upload 요청: {}, {}", localFile, upLoadFileName);

//			sftpClient.put(localFile, upLoadFileName);
            scpClient.put(localFile, upLoadFileName, false);

            log.info("◀FTP file upload OK: {}", upLoadFileName);
        } catch (IOException ioEx) {
            log.error(ioEx.getMessage(), ioEx);

            if ("Directory does not exist.".equals(ioEx.getMessage())) {
//				ValidationUtil.validationCheck(SchedulerConstants.UP_FOLDER_NOT_FOUND_CD);
            } else if ("Unable to create the file/directory".equals(ioEx.getMessage())) {
                log.error("◀FTP Upload file write fail: {}", upLoadFileName);

//				ValidationUtil.validationCheck(SchedulerConstants.UP_FOLDER_NOT_FOUND_CD);
            } else {
//				ValidationUtil.validationCheck(SchedulerConstants.UP_FILE_NOT_FOUND_CD);
            }

            throw ioEx;
        }
    }


    /**
     * 하나의 파일을 다운로드 한다.
     */
    public void download(String remotePath, String localPath, String downFileName) throws Exception {

        try {
            String strRemote = remotePath + "/" + downFileName;
            String strLocal = localPath + "/" + downFileName;

            File localGooglePathDir = new File(localPath);

            if (!(localGooglePathDir.exists() && localGooglePathDir.isDirectory())) {
                localGooglePathDir.mkdirs();

                log.info("DOWN FOLDER 디렉토리가 존재하지 않아 생성: {}", localGooglePathDir);
            }
            // 파일 다운로드는 SCP Client를 사용하도록한다.
            scpClient.get(strLocal, strRemote, false);

            log.info("▶FTP file download: {}", strRemote);

        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);

            throw ex;
        }
    }

    /**
     * 통신종료
     */
    public void disconnection() {
        try {
            if (sftpClient != null) {
                sftpClient.quit();
                sftpClient = null;
            }
            if (scpClient != null) {
                scpClient = null;
            }
            if (sshClient != null) {
                sshClient.disconnect();
                sshClient = null;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);

            try {
                if (sftpClient != null) {
                    sftpClient.quit();
                    sftpClient = null;
                }
                if (scpClient != null) {
                    scpClient = null;
                }
                if (sshClient != null) {
                    sshClient.disconnect();
                    sshClient = null;
                }

            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    public boolean isConnected() {
        if (sshClient != null && scpClient != null && sshClient.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * FTP ls & dir
     * @param path (경로)
     * @param orderPrefix (필터링할 prefix)
     * @return
     * @throws IOException
     */
    private List<String> ftpDir(String path, String orderPrefix) throws IOException, FileNotFoundException {

        log.info("Google 디렉토리 조회[{}] : {}", orderPrefix, path);

        @SuppressWarnings("unchecked")
        List<SftpFile> list = sftpClient.ls(path);

        if (list != null && !list.isEmpty()) {
            log.debug("Dir file count: {}", list.size());

            List<String> result = new ArrayList<String>();

            for (SftpFile file : list) {
                log.debug("{} [{}]", file.getFilename(), file.getAttributes().getSize());

                if (file.getFilename().startsWith(orderPrefix)) {
                    result.add(path + "/" + file.getFilename());
                }
            }

            return result;

        } else {
            log.warn("{} 디렉토리에 파일이 없음", path);

            return null;
        }
    }
}
