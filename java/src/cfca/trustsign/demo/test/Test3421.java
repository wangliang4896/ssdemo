package cfca.trustsign.demo.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.DataSignVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3421ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.CommonUtil;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3421 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3421ReqVO tx3421ReqVO = new Tx3421ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        DataSignVO dataSign = new DataSignVO();
        dataSign.setStubNo("009");
        dataSign.setUserId("7DD861A118C746529EC5F6EFC56C2196");
        dataSign.setProjectCode("003");
        dataSign.setIsAddTimestamp(1);
        dataSign.setSourceData("1234");

        tx3421ReqVO.setHead(head);
        tx3421ReqVO.setDataSign(dataSign);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3421ReqVO);
        System.out.println("req:" + req);

        if (CommonUtil.isEmpty(req)) {
            System.out.println("req is null");
            return;
        }

        String txCode = "3421";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
