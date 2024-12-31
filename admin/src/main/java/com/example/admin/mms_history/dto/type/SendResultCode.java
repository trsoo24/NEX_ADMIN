package com.example.admin.mms_history.dto.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SendResultCode {
    Success("1000", "성공"),
    PartialSuccess("1100", "부분 성공"),
    ClientError("2000", "Client 가 잘못된 응답을 보냄"),
    OperationRestricted("2001", "허용되지 않은 command 실행에 의해 메시지가 거부됨"),
    AddressError("2002", "메시지에 있는 주소가 잘못된 형식이거나 유효하지 않음. 메시지 수신자가 다수일 경우 적어도 한 개의 주소가 잘못되어도 응답을 줌"),
    AddressNotFound("2003", "메시지에 있는 주소를 MMS Relay/Server 가 찾을 수 없음. 이 코드는 메시지가 전송될 주소를 찾을 수 없을 때 리턴 됨"),
    MultimediaContentRefused("2004", "SOAP 메시지에 포함된 MIME content의 요소나 크기, 타입이 불분명함"),
    MessageIdNotFound("2005" ,"MMS Relay/Server가 이전에 전송된 메시지에 대한 message ID를 찾을 수 없거나 VASP로 부터 받은 응답에서 message ID를 찾을 수 없음"),
    LinkedIdNotFound("2006", "MMS Relay/Server가 메시지에 있는 LinkedID를 찾을 수 없음"),
    MessageFormatCorrupt("2007", "메시지가 규격에 맞지 않거나 부적당함"),
    ServerError("3000", "서버에서 올바른 요청에 대한 처리를 실패함"),
    NotPossible("3001", "메시지 처리가 불가능함. 이 코드는 메시지가 더 이상 유효하지 않거나 취소된 것에 대한 결과임. 메시지가 이미 처리 되었거나 더 이상 유효하지 않아서 MMS Relay/Serve가 처리할 수 없음"),
    MessageRejected("3002", "서버에서 메시지를 받아들일 수 없음"),
    MultipleAddressesNotSupported("3003", "MMS Relay/Server가 multiple recipients를 지원하지 않음"),
    GeneralServiceError("4000", "요구된 서비스가 실행될 수 없음"),
    ImproperIdentification("4001", "메시지의 Identification header가 client를 확인할 수 없음 (VASP나 MMS Relay/Server)"),
    UnsupportedVersion("4002", "메시지에 있는 MM7 version 이 지원되지 않는 version임"),
    UnsupportedOperation("4003", "메시지 헤더에 있는 Message Type이 서버에서 지원되지 않음"),
    ValidationError("4004", "필수적인 field가 빠졌거나 message-format 이 맞지 않아 XML로 된 SOAP 메시지를 parsing할 수 없음"),
    ServiceError("4005", "서버(MMS Relay/Server 나 VASP)에서 메시지 처리에 실패하여 재전송 할 수 없음"),
    ServiceUnavailable("4006", "서비스가 일시적으로 되지 않음. 서버에서 응답이 없음"),
    ServiceDenied("4007", "Client에게 요청된 작업에 대한 허가가 나있지 않음"),
    SubsReject("6014", "수신자가 착신거절 신청자임"),
    InvalidAuthPassword("6024", "CP 보안 인증 실패"),
    ExpiredPassword("6025", "CP 인증 Password 유효 기간 만료"),
    MmsDisableSubs("6072", "MMS 비가용 단말"),
    TrafficIsOver("7103", "1:1 메시지 전송 시 허용된 트래픽을 초과 하여 전송 하는 경우"),
    UnknownError("9999", "서버에서 지정되지 않은 기타 에러입니다.");

    private final String code;
    private final String message;
}
