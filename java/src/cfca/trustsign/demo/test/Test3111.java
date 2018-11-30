package cfca.trustsign.demo.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.ProxySignVO;
import cfca.trustsign.common.vo.request.tx3.Tx3111ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3111 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3111ReqVO tx3111ReqVO = new Tx3111ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        ProxySignVO proxySignVO = new ProxySignVO();
        proxySignVO.setUserId("67BBC2B9929C6DAAE055000000000001");
        proxySignVO.setProjectCode("003");

        tx3111ReqVO.setHead(head);
        tx3111ReqVO.setProxySign(proxySignVO);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3111ReqVO);
        System.out.println("req:" + req);

        String txCode = "3111";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
