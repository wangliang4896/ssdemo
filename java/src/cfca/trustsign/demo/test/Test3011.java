package cfca.trustsign.demo.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.util.Base64;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SealAddVO;
import cfca.trustsign.common.vo.cs.SealVO;
import cfca.trustsign.common.vo.request.tx3.Tx3011ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3011 {
    public static void main(String[] args) throws PKIException, IOException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3011ReqVO tx3011ReqVO = new Tx3011ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        SealAddVO sealAddVO = new SealAddVO();
        sealAddVO.setUserId("F95ED2BF390B4DCF8AE175A97112EDDD");

        SealVO sealVO = new SealVO();

        sealVO.setImageData(Base64.toBase64String(Files.readAllBytes(Paths.get("./image/王五.png"))));
        sealAddVO.setSeal(sealVO);

        tx3011ReqVO.setHead(head);
        tx3011ReqVO.setSealAdd(sealAddVO);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3011ReqVO);
        System.out.println("req:" + req);

        String txCode = "3011";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
