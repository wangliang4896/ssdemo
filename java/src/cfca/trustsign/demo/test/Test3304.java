package cfca.trustsign.demo.test;

import java.io.FileOutputStream;
import java.io.IOException;

import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.util.P10Request;
import cfca.sadk.util.P12FileKit;
import cfca.sadk.x509.certificate.X509Cert;
import cfca.trustsign.common.vo.cs.CertVO;
import cfca.trustsign.common.vo.cs.HeadVO;
import cfca.trustsign.common.vo.request.tx3.Tx3304ReqVO;
import cfca.trustsign.common.vo.response.tx3.Tx3304ResVO;
import cfca.trustsign.demo.connector.HttpConnector;
import cfca.trustsign.demo.constant.Request;
import cfca.trustsign.demo.converter.JsonObjectMapper;
import cfca.trustsign.demo.util.SecurityUtil;
import cfca.trustsign.demo.util.TimeUtil;

public class Test3304 {
    public static void main(String[] args) throws PKIException, IOException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        Tx3304ReqVO tx3304ReqVO = new Tx3304ReqVO();
        HeadVO head = new HeadVO();
        head.setTxTime(TimeUtil.getCurrentTime(TimeUtil.FORMAT_14));

        tx3304ReqVO.setHead(head);
        CertVO certUpdate = new CertVO();
        certUpdate.setUserId("F55CB8E25552482E94BFC18995417B70");
        certUpdate.setSerialNo("2027020930");
        P10Request p10Request = SecurityUtil.getP10Request();
        String p10 = SecurityUtil.getP10(p10Request, 2048);
        certUpdate.setP10(p10);
        tx3304ReqVO.setCertUpdate(certUpdate);

        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        String req = jsonObjectMapper.writeValueAsString(tx3304ReqVO);
        System.out.println("req:" + req);

        String txCode = "3304";
        String signature = SecurityUtil.p7SignMessageDetach(HttpConnector.JKS_PATH, HttpConnector.JKS_PWD, HttpConnector.ALIAS, req);
        String res = httpConnector.post("platId/" + Request.PLAT_ID + "/txCode/" + txCode + "/transaction", req, signature);
        System.out.println("res:" + res);

        Tx3304ResVO tx3305ResVO = jsonObjectMapper.readValue(res, Tx3304ResVO.class);
        X509Cert x509Cert = new X509Cert(tx3305ResVO.getCertUpdate().getSignCert().getBytes("UTF-8"));
        byte[] p12Data = P12FileKit.RSACombineP12Data(x509Cert, p10Request.getPrivateKey(), "111111");
        FileOutputStream fos = new FileOutputStream("./file/test1.pfx");
        fos.write(p12Data);
        fos.close();
    }
}
