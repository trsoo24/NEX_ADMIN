package com.example.admin.common.service.gdcb.util;

public class GDCBConstants {
    /** 과금 요청 파일의 prefix */
    public static final String Prefix_RequstFile = "request";
    /** 환불 미처리 파일의 prefix */
    public static final String Prefix_OutstandingFile = "outstanding";


    /** 원격 서버의 프롬프트 메세지 (검수기) */
    public static final String HostPrompt_Test = "new_dl380g7_test07";
    public static final String HostPrompt_Real = "GDCBAPP";


    //Google 빌링관련

    public static final String SCH_AUTH_BILLING_AGREEMENT = "LGU_DCB";
    public static final String MESSGE_INVALID_CORRELATION_ID		= "INVALID_CORRELATION_ID";
    public static final String MESSGE_INVALID_BILLING_AGREEMENT_ID	= "INVALID_BILLING_AGREEMENT_ID";

    public static final String SCH_AUTH_TRANSACTION_CHARGE 	= "B";
    public static final String SCH_AUTH_TRANSACTION_CANCEL 	= "C";
    public static final String SCH_AUTH_TRANSACTION_REFUND		= "R";

    public static final String SCH_EAI_ACCOUNT_CHARGE 		    = "02";
    public static final String SCH_EAI_ACCOUNT_REFUND 			= "03";
    public static final String SCH_EAI_ACCOUNT_CANCEL 			= "04";
    public static final String SCH_EAI_SMLS_STLM_DV_CD 		= "96";
    public static final String SCH_EAI_SMLS_STLM_CMPNY_CD 		= "0196";
    public static final String SCH_EAI_REQUEST_OK 		  		= "0";
    public static final String SCH_EAI_REQUEST_NO 				= "1";

    // ADJ_INFO(REFUND S:당월 A: 조정)
    public static final String SCH_AUTH_ADJ_INFO_S = "S";
    public static final String SCH_AUTH_ADJ_INFO_A = "A";

    public static final String SUCCESS_RESULT 	= "SUCCESS";
}
