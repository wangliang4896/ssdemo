package cfca.trustsign.demo.test;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.BankCardInfoVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.cs.PersonVO;
import cfca.trustsign.common.vo.request.tx3.Tx3001ReqVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3001 {
    public static void main(String[] args) throws PKIException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3001ReqVO tx3001ReqVO = new Tx3001ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        PersonVO person = new PersonVO();
        person.setPersonName("刘**");
        person.setIdentTypeCode("0");
        person.setIdentNo("110102200001071118");
        person.setMobilePhone("93520071111");
        // person.setEmail("235200751@cfca.com.cn");
        // person.setAddress("北京");
        person.setAuthenticationMode("公安部");

        tx3001ReqVO.setHead(head);
        tx3001ReqVO.setPerson(person);

        // 身份认证时需要
        tx3001ReqVO.setIsVerifyBankCard(0);
        BankCardInfoVO bankCardInfo = new BankCardInfoVO();
        bankCardInfo.setPanNo("6225365271562822");
        bankCardInfo.setMobilePhone("13542314551");
        tx3001ReqVO.setBankCardInfo(bankCardInfo);

        // tx3001ReqVO.setSealColor(1);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3001ReqVO);
        System.out.println("req:" + req);

        String txCode = "3001";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);
    }
}
