package cfca.trustsign.demo.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.DataSignVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3431ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3431 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3431ReqVO tx3431ReqVO = new Tx3431ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        DataSignVO dataSign = new DataSignVO();
        dataSign.setStubNo("007");

        tx3431ReqVO.setHead(head);
        tx3431ReqVO.setDataSign(dataSign);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3431ReqVO);
        System.out.println("req:" + req);

        String txCode = "3431";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
