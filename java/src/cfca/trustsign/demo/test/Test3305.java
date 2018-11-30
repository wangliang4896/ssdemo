package cfca.trustsign.demo.test;

import java.io.IOException;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CertVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3305ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3305 {
    public static void main(String[] args) throws PKIException, IOException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3305ReqVO tx3305ReqVO = new Tx3305ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        tx3305ReqVO.setHead(head);
        CertVO certRevoke = new CertVO();
        certRevoke.setUserId("F55CB8E25552482E94BFC18995417B70");
        certRevoke.setSerialNo("2027020930");
        tx3305ReqVO.setCertRevoke(certRevoke);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3305ReqVO);
        System.out.println("req:" + req);

        String txCode = "3305";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
