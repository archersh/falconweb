package cn.com.sailin.compassweb.model;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.sailin.compassweb.publiccode.Code;

public class Outsuccess {

	private String cycode;

	private String gcpguid;

	private String rvn;

	private String cn;

	private String sn;

	private String ccrq;

	private String nonce_str;

	private String sign;

	public void applyJson(String applyData) {
		JSONObject obj = JSON.parseObject(applyData);
		cycode = obj.getString("cycode");
		gcpguid = obj.getString("gcpguid");
		rvn = obj.getString("rvn");
		cn = obj.getString("cn");
		sn = obj.getString("sn");
		ccrq = obj.getString("ccrq");
		nonce_str = UUID.randomUUID().toString();

		List<String> l = new ArrayList<String>();
		l.add("cycode=" + cycode);
		l.add("gcpguid=" + gcpguid);
		l.add("rvn=" + rvn);
		l.add("cn" + cn);
		l.add("sn=" + sn);
		l.add("ccrq" + ccrq);
		l.add("nonce_str=" + nonce_str);

		sign = Code.getSign(l);
	}

	public String toXml() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			doc.setXmlStandalone(true);
			Element root = doc.createElement("xml");
			
			Element rncycode=doc.createElement("cycode");
			CDATASection rncycodeval=doc.createCDATASection(cycode);
			rncycode.appendChild(rncycodeval);
			root.appendChild(rncycode);
			
			Element rngcpguid=doc.createElement("gcpguid");
			CDATASection rngcpguidval=doc.createCDATASection(gcpguid);
			rngcpguid.appendChild(rngcpguidval);
			root.appendChild(rngcpguid);
			
			Element rnrvn=doc.createElement("rvn");
			CDATASection rnrvnval=doc.createCDATASection(rvn);
			rnrvn.appendChild(rnrvnval);
			root.appendChild(rnrvn);
			
			Element rncn=doc.createElement("cn");
			CDATASection rncnval=doc.createCDATASection(cn);
			rncn.appendChild(rncnval);
			root.appendChild(rncn);
			
			Element rnsn=doc.createElement("sn");
			CDATASection rnsnval=doc.createCDATASection(sn);
			rnsn.appendChild(rnsnval);
			root.appendChild(rnsn);
			
			Element rnccrq=doc.createElement("ccrq");
			CDATASection rnccrqval=doc.createCDATASection(ccrq);
			rnccrq.appendChild(rnccrqval);
			root.appendChild(rnccrq);
			
			Element rnnonce_str=doc.createElement("nonce_str");
			CDATASection rnnonce_strval=doc.createCDATASection(nonce_str);
			rnnonce_str.appendChild(rnnonce_strval);
			root.appendChild(rnnonce_str);
			
			Element rnsign=doc.createElement("sign");
			CDATASection rnsignval=doc.createCDATASection(sign);
			rnsign.appendChild(rnsignval);
			root.appendChild(rnsign);

			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			tf.transform(new DOMSource(doc), new StreamResult(os));

			return os.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
