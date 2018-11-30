package cfca.trustsign.demo.test;

import java.io.IOException;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SignContractByKeywordVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.cs.SignKeywordVO;
import cfca.trustsign.common.vo.request.tx3.Tx3207ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3207 {
    public static void main(String[] args) throws PKIException, IOException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3207ReqVO tx3207ReqVO = new Tx3207ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        SignContractByKeywordVO signContractByKeyword = new SignContractByKeywordVO();
        signContractByKeyword.setContractNo("ZL20180321000000304");

        SignKeywordVO signKeyword = new SignKeywordVO();
        signKeyword.setKeyword("房屋租赁合同");
        signKeyword.setOffsetCoordX("0");
        signKeyword.setOffsetCoordY("0");
        signKeyword.setImageWidth("150");
        signKeyword.setImageHeight("50");
        signContractByKeyword.setSignKeyword(signKeyword);

        SignInfoVO signInfo = new SignInfoVO();
        signInfo.setUserId("67BBC2B9929C6DAAE055000000000001");
//        signInfo.setUserId("9C9E731AEE444B498F7B5DCFBA0CD0E8");
        signInfo.setLocation("211.94.108.226");
        signInfo.setAuthorizationTime("20160801095509");
        signInfo.setProjectCode("003");
        signInfo.setIsCheckProjectCode(1);
        signInfo.setCertType(1);

        // 传图片或传sealId的方式任选其一，传图片优先级高
        // signInfo.setImageData(Base64.toBase64String(Files.readAllBytes(Paths.get("./image/赵六.png"))));
        // signInfo.setSealId("8C5E69F26A7E45F4977687301E120F83");
        signContractByKeyword.setSignInfo(signInfo);

        tx3207ReqVO.setHead(head);
        tx3207ReqVO.setSignContractByKeyword(signContractByKeyword);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3207ReqVO);
        System.out.println("req:" + req);

        String txCode = "3207";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
