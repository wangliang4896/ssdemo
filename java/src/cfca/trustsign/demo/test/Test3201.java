package cfca.trustsign.demo.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.util.Base64;
import cfca.trustsign.common.constant.SealColor;
import cfca.trustsign.common.vo.cs.AttachmentVO;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.cs.WatermarkVO;
import cfca.trustsign.common.vo.request.tx3.Tx3201ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3201 {
    public static void main(String[] args) throws PKIException, IOException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3201ReqVO tx3201ReqVO = new Tx3201ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        Map<String, String> fieldMap = new HashMap<String, String>();
        CreateContractVO createContract = new CreateContractVO();

        createContract.setIsSign(2);
        // createContract.setSealId("16CD3E1B621E402ABC179A646A717BA7");
        createContract.setTemplateId("JK_513");
        createContract.setContractName("123");
        createContract.setSealColor(SealColor.RED);
        // createContract.setSignLocation("Signature1");
        fieldMap.put("Text1", "Text1");
        fieldMap.put("Text2", "Text2");
        fieldMap.put("Text3", "Text3");
        fieldMap.put("Text4", "孙一");
        fieldMap.put("Text5", "222321199112050001");
        fieldMap.put("Text6", "1");
        fieldMap.put("Text7", "1");
        fieldMap.put("Text8", "1");
        fieldMap.put("Text9", "10000");
        fieldMap.put("Text10", "1");
        fieldMap.put("Text11", "10000");
        fieldMap.put("Text12", "1");
        fieldMap.put("Text13", "10000");
        fieldMap.put("Text14", "1");
        fieldMap.put("Text15", "30000");
        fieldMap.put("Text16", "10000");
        fieldMap.put("Text17", "10000");
        fieldMap.put("Text18", "10000");
        fieldMap.put("Text19", "1");
        fieldMap.put("Text20", "1");

        // setInvestmentInfo方法废弃，使用setTextValueInfo代替
        // createContract.setInvestmentInfo(fieldMap);
        createContract.setTextValueInfo(fieldMap);
        createContract.setIsSaveTextValue(1);

        SignInfoVO[] signInfos = new SignInfoVO[2];
        SignInfoVO signInfoVO0 = new SignInfoVO();
        signInfoVO0.setUserId("67BBC2B9929C6DAAE055000000000001");
//        signInfoVO0.setIsProxySign(1);
        signInfoVO0.setLocation("211.94.108.226");
        signInfoVO0.setProjectCode("003");
        // signInfoVO0.setIsCheckProjectCode(1);
        signInfoVO0.setSignLocation("Signature1");
        signInfoVO0.setAuthorizationTime("20160801095509");
        // signInfoVO0.setSealId("6ACEC0B88FC44FD2AB2166D1320ADCEF");
        signInfos[0] = signInfoVO0;

        SignInfoVO signInfoVO1 = new SignInfoVO();
        signInfoVO1.setUserId("96428FD7E441461DA91C797EC0B02CED");
        signInfoVO1.setIsProxySign(0);
        signInfos[1] = signInfoVO1;

        createContract.setSignInfos(signInfos);

        // 使用水印时用到
        // WatermarkVO watermark = new WatermarkVO();
        // watermark.setWatermarkOnPage("1");
        // watermark.setWatermarkLBX("100");
        // watermark.setWatermarkLBY("100");
        // watermark.setWatermarkWidth("100");
        // watermark.setWatermarkHeight("100");
        // watermark.setWatermarkData(Base64.toBase64String(Files.readAllBytes(Paths.get("./image/王五.png"))));
        // createContract.setWatermark(watermark);

        // 使用附件时用到
        AttachmentVO attachment = new AttachmentVO();
        attachment.setAttachmentName("123.txt");
        attachment.setAttachmentContent("11111王五");
        createContract.setAttachment(attachment);
        // createContract.setIsFillInContractNo(1);

        tx3201ReqVO.setHead(head);
        tx3201ReqVO.setCreateContract(createContract);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3201ReqVO);
        System.out.println("req:" + req);

        String txCode = "3201";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
