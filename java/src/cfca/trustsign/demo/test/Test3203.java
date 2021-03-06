package cfca.trustsign.demo.test;

import java.io.File;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SignKeywordVO;
import cfca.trustsign.common.vo.cs.SignLocationVO;
import cfca.trustsign.common.vo.cs.UploadContractVO;
import cfca.trustsign.common.vo.cs.UploadSignInfoVO;
import cfca.trustsign.common.vo.request.tx3.Tx3203ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3203 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3203ReqVO tx3203ReqVO = new Tx3203ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        UploadContractVO uploadContract = new UploadContractVO();

        UploadSignInfoVO[] signInfos = new UploadSignInfoVO[1];
        UploadSignInfoVO signInfoVO0 = new UploadSignInfoVO();
        signInfoVO0.setUserId("F55CB8E25552482E94BFC18995417B70");
        signInfoVO0.setIsProxySign(1);
        signInfoVO0.setLocation("210.74.41.0");

        SignLocationVO[] signLocations0 = new SignLocationVO[1];
        SignLocationVO signLocation0 = new SignLocationVO();
        signLocation0.setSignOnPage("1");
        signLocation0.setSignLocationLBX("85");
        signLocation0.setSignLocationLBY("550");
        signLocation0.setSignLocationRUX("140");
        signLocation0.setSignLocationRUY("575");
        signLocations0[0] = signLocation0;
        signInfoVO0.setSignLocations(signLocations0);
        signInfoVO0.setProjectCode("001");
        signInfoVO0.setIsCheckProjectCode(1);
        signInfoVO0.setAuthorizationTime("20160214171200");
        signInfoVO0.setCertType(2);
        signInfos[0] = signInfoVO0;

        // UploadSignInfoVO signInfoVO1 = new UploadSignInfoVO();
        // signInfoVO1.setUserId("2ED850C06CFB3AADE050007F010003F0");
        // signInfoVO1.setIsProxySign(1);
        // signInfoVO1.setLocation("成都");
        // SignLocationVO[] signLocations1 = new SignLocationVO[1];
        // SignLocationVO signLocation1 = new SignLocationVO();
        // signLocation1.setSignOnPage("2");
        // signLocation1.setSignLocationLBX("151");
        // signLocation1.setSignLocationLBY("476");
        // signLocation1.setSignLocationRUX("240");
        // signLocation1.setSignLocationRUY("500");
        // signLocations1[0] = signLocation1;
        // signInfoVO1.setSignLocations(signLocations1);
        // signInfoVO1.setProjectCode("541840");
        // signInfoVO1.setAuthorizationTime("20160214171200");
        // signInfos[1] = signInfoVO1;
        //
        // UploadSignInfoVO signInfoVO2 = new UploadSignInfoVO();
        // signInfoVO2.setUserId("35FE921E7CF0F890E050007F01004E8E");
        // signInfoVO2.setIsProxySign(1);
        // signInfoVO2.setLocation("210.74.41.0");
        // SignLocationVO[] signLocations2 = new SignLocationVO[1];
        // SignLocationVO signLocation2 = new SignLocationVO();
        // signLocation2.setSignOnPage("2");
        // signLocation2.setSignLocationLBX("230");
        // signLocation2.setSignLocationLBY("105");
        // signLocation2.setSignLocationRUX("330");
        // signLocation2.setSignLocationRUY("130");
        // signLocations2[0] = signLocation2;
        // signInfoVO2.setSignLocations(signLocations2);
        // signInfoVO2.setProjectCode("003");
        // signInfoVO2.setAuthorizationTime("20160214171200");
        // signInfos[2] = signInfoVO2;

        uploadContract.setSignInfos(signInfos);
        uploadContract.setContractTypeCode("MM");
        uploadContract.setContractName("测试合同");

        uploadContract.setIsSign(1);
        // uploadContract.setSealId("16CD3E1B621E402ABC179A646A717BA7");
        SignKeywordVO signKeyword = new SignKeywordVO();
        signKeyword.setKeyword("示例");
        signKeyword.setOffsetCoordX("0");
        signKeyword.setOffsetCoordY("20");
        signKeyword.setImageWidth("150");
        signKeyword.setImageHeight("150");
         uploadContract.setSignKeyword(signKeyword);

        SignLocationVO[] signLocationsPlat = new SignLocationVO[1];
        SignLocationVO signLocationPlat = new SignLocationVO();
        signLocationPlat.setSignOnPage("1");
        signLocationPlat.setSignLocationLBX("10");
        signLocationPlat.setSignLocationLBY("10");
        signLocationPlat.setSignLocationRUX("140");
        signLocationPlat.setSignLocationRUY("130");
        signLocationsPlat[0] = signLocationPlat;
        uploadContract.setSignLocations(signLocationsPlat);

        tx3203ReqVO.setHead(head);
        tx3203ReqVO.setUploadContract(uploadContract);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3203ReqVO);
        System.out.println("req:" + req);

        File file = new File("./file/1.pdf");

        String txCode = "3203";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature, file);
        System.out.println("res:" + res);
    }
}
