package cfca.trustsign.demo.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CertBindingVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3301ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3301 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3301ReqVO tx3301ReqVO = new Tx3301ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        tx3301ReqVO.setHead(head);
        CertBindingVO certBindingVO = new CertBindingVO();
        String userId = "67BBC2B9929C6DAAE055000000000001";
        certBindingVO.setUserId(userId);
        certBindingVO.setSignCert(SecurityUtil.getSignCert("./jks/test.pfx", "11111111"));
        // certBindingVO.setSignCert(SecurityUtil.getSignCert("./jks/1.pfx", "1"));
        // certBindingVO.setSignCert(SecurityUtil.getSignCert("./jks/2.pfx", "1"));

        tx3301ReqVO.setCertBinding(certBindingVO);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3301ReqVO);
        System.out.println("req:" + req);

        String txCode = "3301";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
