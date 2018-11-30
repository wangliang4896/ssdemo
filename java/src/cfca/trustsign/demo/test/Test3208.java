package cfca.trustsign.demo.test;

import java.io.IOException;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SignContractByCoordinateVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.cs.SignLocationVO;
import cfca.trustsign.common.vo.request.tx3.Tx3208ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3208 {
    public static void main(String[] args) throws PKIException, IOException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3208ReqVO tx3208ReqVO = new Tx3208ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        SignContractByCoordinateVO signContractByCoordinate = new SignContractByCoordinateVO();
        signContractByCoordinate.setContractNo("JK20180518000000001");

        SignLocationVO[] signLocations0 = new SignLocationVO[1];
        SignLocationVO signLocation0 = new SignLocationVO();
        signLocation0.setSignOnPage("1");
        signLocation0.setSignLocationLBX("85");
        signLocation0.setSignLocationLBY("550");
        signLocation0.setSignLocationRUX("240");
        signLocation0.setSignLocationRUY("675");
        signLocations0[0] = signLocation0;
        signContractByCoordinate.setSignLocations(signLocations0);

        SignInfoVO signInfo = new SignInfoVO();
        signInfo.setUserId("9C9E731AEE444B498F7B5DCFBA0CD0E8");
        signInfo.setLocation("211.94.108.226");
        signInfo.setAuthorizationTime("20160801095509");
        signInfo.setProjectCode("003");
        signInfo.setIsCheckProjectCode(1);
        signInfo.setCertType(1);

        // 传图片或传sealId的方式任选其一，传图片优先级高
        // signInfo.setImageData(Base64.toBase64String(Files.readAllBytes(Paths.get("./image/赵六.png"))));
        // signInfo.setSealId("8C5E69F26A7E45F4977687301E120F83");
        signContractByCoordinate.setSignInfo(signInfo);

        tx3208ReqVO.setHead(head);
        tx3208ReqVO.setSignContractByCoordinate(signContractByCoordinate);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3208ReqVO);
        System.out.println("req:" + req);

        String txCode = "3208";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
