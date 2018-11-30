package cfca.trustsign.demo.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.util.Base64;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SignContractSignatureAttrVO;
import cfca.trustsign.common.vo.request.tx3.Tx3205ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3205 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3205ReqVO tx3205ReqVO = new Tx3205ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        SignContractSignatureAttrVO signContractSignatureAttr = new SignContractSignatureAttrVO();
        signContractSignatureAttr.setContractNo("ZL20180404000000104");

        String signatureAttr = "2Idu/yqJ/FIvRCc0lcrYkPV4BsTo/3YXJz+qYS4v/Ew=";
        // String signatureValue = SecurityUtil.p1SignMessage("./jks/test.pfx", "11111111", Base64.decode(signatureAttr));
        // String signatureValue = SecurityUtil.p1SignMessage("./jks/1.pfx", "1", Base64.decode(signatureAttr));
        // String signatureValue = SecurityUtil.p1SignMessage("./jks/2.pfx", "1", Base64.decode(signatureAttr));
        String signatureValue = SecurityUtil.p7SignByHash("./jks/test.pfx", "11111111", Base64.decode(signatureAttr));

        signContractSignatureAttr.setSignatureOfAttr(signatureValue);

        tx3205ReqVO.setHead(head);
        tx3205ReqVO.setSignContractSignatureAttr(signContractSignatureAttr);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3205ReqVO);
        System.out.println("req:" + req);

        String txCode = "3205";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
