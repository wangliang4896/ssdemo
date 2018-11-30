package cfca.trustsign.demo.test;

import java.io.IOException;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.OpenCertSM2VO;
import cfca.trustsign.common.vo.request.tx3.Tx3321ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3321 {
    public static void main(String[] args) throws PKIException, IOException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3321ReqVO tx3321ReqVO = new Tx3321ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        tx3321ReqVO.setHead(head);
        OpenCertSM2VO openCertSM2 = new OpenCertSM2VO();
        openCertSM2.setUserId("9C9E731AEE444B498F7B5DCFBA0CD0E8");
        tx3321ReqVO.setOpenCertSM2(openCertSM2);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3321ReqVO);
        System.out.println("req:" + req);

        String txCode = "3321";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
