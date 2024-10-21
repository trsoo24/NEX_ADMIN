package com.example.admin.common.service.gdcb.util;

import com.example.admin.common.service.gdcb.vo.LogServiceInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Map;

@Component
public class LogUtil{

    protected static final Logger log = LoggerFactory.getLogger(LogUtil.class);
    protected static final Logger log_info = LoggerFactory.getLogger("LOGGER");
    protected static final Logger log_error = LoggerFactory.getLogger("ERROR_LOGGER");

    public static final String LOG_ATTRIBUTE_NAME = "SERVICE_LOG_INFO";

    /**
     * <PRE>
     * Comment : Unique Make Key 정보를 가져온다.
     * </PRE>
     *   @return String
     *   @param request
     *   @return
     */
    public String getUmk(HttpServletRequest request) {
        if(request != null){
            log_info.info("request is not null");
            LogServiceInfo serviceLog = (LogServiceInfo) request.getAttribute(LOG_ATTRIBUTE_NAME);

            if(serviceLog == null) {
                log_info.info("serviceLog is null");
                return "";
            } else {
                log_info.info("serviceLog is not null");
                return serviceLog.getUmk();
            }

        } else {
            return "";
        }
    }

    /**
     * <PRE>
     * Comment : 웹 요청시 로그 정보
     * </PRE>
     *   @return String
     *   @param request	= HttpServletRequest 객체
     *   @return		= Unique 키값
     */
    public String startServiceLog(HttpServletRequest request, String method, String url, String remoteAddr, String adminId){
        StringBuffer sb = new StringBuffer();
        SecureRandom RANDOM = new SecureRandom();

        String umk = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date())+RANDOM.nextInt(4);// Unique Make Key

        sb.append("[");
        sb.append("UMK=").append(umk);
        //sb.append("|").append("SESSION_ID").append("=").append(sessionId);
        sb.append("|").append("METHOD").append("=").append(method);
        sb.append("|").append("URL").append("=").append(url);
        sb.append("|").append("REMOTE_ADDR").append("=").append(remoteAddr);
        sb.append("|").append("LOGIN_ID").append("=").append(adminId);
        sb.append("]");

        log_info.info(sb.toString());

        return umk;
    }

    /**
     * <PRE>
     * Comment : Unique Make Key 정보 + INFO 로그메시지
     * </PRE>
     *   @return void
     *   @param request	= HttpServletRequest 객체
     *   @param logMsg	= INFO 로그 메시지
     */
    public void umkInfoLogging(HttpServletRequest request, String logMsg, String umk){

        HttpSession session = request.getSession(false);
        Map user =  (Map) session.getAttribute("userLoginInfo");
        String adminId = "";

        StringBuffer url = request.getRequestURL();
        String remoteAddr = request.getRemoteAddr() == null ? "" : request.getRemoteAddr();

        //log_info.info("umk="+umk);

        if (!"".equals(umk) ) {
            adminId = request.getParameter("adminId");
        } else {

            //if (session != null && session.getAttribute("UMK") != null && session.getAttribute("ADMIN_ID") != null) {
            //if (logMsg != "[fail]") {
            umk = user.get("UMK").toString();
            adminId = user.get("ADMIN_ID").toString();
            //}
        }

        //log_info.info("umk="+umk);

        String sb = "[" +
                "UMK=" + umk +
                "|" + "URL" + "=" + url +
                "|" + "REMOTE_ADDR" + "=" + remoteAddr +
                "|" + "LOGIN_ID" + "=" + adminId +
                "] ";

        logMsg = sb +logMsg;

        log_info.info(logMsg);
    }

}