package cfca.trustsign.demo.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.ContactWayVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3003ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3003 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3003ReqVO tx3003ReqVO = new Tx3003ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        tx3003ReqVO.setHead(head);
        ContactWayVO modifyContactWay = new ContactWayVO();
        modifyContactWay.setUserId("65E45204084D4B9D8530CCFDD7BB17AE");
        modifyContactWay.setMobilePhone("93520071111");
        modifyContactWay.setEmail("235200751@cfca.com.cn");
        tx3003ReqVO.setModifyContactWay(modifyContactWay);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3003ReqVO);
        System.out.println("req:" + req);

        String txCode = "3003";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
