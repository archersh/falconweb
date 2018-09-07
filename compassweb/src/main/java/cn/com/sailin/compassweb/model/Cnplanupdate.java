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

public class Cnplanupdate {

	private String gcpguid;

	private String rvn;

	private String cycode;

	private String bcycode;

	private String shipping;

	private String bn;

	private String eir;

	private String vsl;

	private String voy;
	
	private String forwarder;

	private String deadline;

	private String nonce_str;

	private String sign;

	public void applyJson(String applyData) {
		JSONObject obj = JSON.parseObject(applyData);

		gcpguid = obj.getString("gcpguid");
		rvn = obj.getString("rvn");
		cycode = obj.getString("cycode");
		bcycode = obj.getString("bcycode");
		shipping = obj.getString("shipping");
		bn = obj.getString("bn");
		eir = obj.getString("eir");
		vsl = obj.getString("vsl");
		voy = obj.getString("voy");
		forwarder = obj.getString("forwarder");
		deadline=obj.getString("deadline");
		nonce_str = UUID.randomUUID().toString();

		List<String> l = new ArrayList<String>();
		l.add("gcpguid=" + gcpguid);
		l.add("rvn=" + rvn);
		l.add("cycode=" + cycode);
		l.add("bcycode=" + bcycode);
		l.add("shipping=" + shipping);
		l.add("bn=" + bn);
		l.add("eir=" + eir);
		l.add("vsl=" + vsl);
		l.add("voy=" + voy);
		l.add("forwarder=" + forwarder);
		l.add("deadline=" + deadline);
		l.add("nonce_str=" + nonce_str);

		sign = Code.getSign(l);
	}

	public String toXml() {

		try {
			// 生成xml
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			doc.setXmlStandalone(true);
			Element root = doc.createElement("xml");

			Element rngcpguid = doc.createElement("gcpguid");
			CDATASection rngcpguidval = doc.createCDATASection(gcpguid);
			rngcpguid.appendChild(rngcpguidval);
			root.appendChild(rngcpguid);

			Element rnrvn = doc.createElement("rvn");
			CDATASection rnrvnval = doc.createCDATASection(rvn);
			rnrvn.appendChild(rnrvnval);
			root.appendChild(rnrvn);

			Element rncycode = doc.createElement("cycode");
			CDATASection rncycodeval = doc.createCDATASection(cycode);
			rncycode.appendChild(rncycodeval);
			root.appendChild(rncycode);

			Element rnbcycode = doc.createElement("bcycode");
			CDATASection rnbcycodeval = doc.createCDATASection(bcycode);
			rnbcycode.appendChild(rnbcycodeval);
			root.appendChild(rnbcycode);

			Element rnshipping = doc.createElement("shipping");
			CDATASection rnshippingval = doc.createCDATASection(shipping);
			rnshipping.appendChild(rnshippingval);
			root.appendChild(rnshipping);

			Element rnbn = doc.createElement("bn");
			CDATASection rnbnval = doc.createCDATASection(bn);
			rnbn.appendChild(rnbnval);
			root.appendChild(rnbn);

			Element rneir = doc.createElement("eir");
			CDATASection rneirval = doc.createCDATASection(eir);
			rneir.appendChild(rneirval);
			root.appendChild(rneir);

			Element rnvsl = doc.createElement("vsl");
			CDATASection rnvslval = doc.createCDATASection(vsl);
			rnvsl.appendChild(rnvslval);
			root.appendChild(rnvsl);

			Element rnvoy = doc.createElement("voy");
			CDATASection rnvoyval = doc.createCDATASection(voy);
			rnvoy.appendChild(rnvoyval);
			root.appendChild(rnvoy);

			Element rnforwarder = doc.createElement("forwarder");
			CDATASection rnforwarderval = doc.createCDATASection(forwarder);
			rnforwarder.appendChild(rnforwarderval);
			root.appendChild(rnforwarder);

			Element rndeadline=doc.createElement("deadline");
			CDATASection rndeadlineval=doc.createCDATASection(deadline);
			rndeadline.appendChild(rndeadlineval);
			root.appendChild(rndeadline);

			Element rnnonce_str = doc.createElement("nonce_str");
			CDATASection rnnonce_strval = doc.createCDATASection(nonce_str);
			rnnonce_str.appendChild(rnnonce_strval);
			root.appendChild(rnnonce_str);

			Element rnsign = doc.createElement("sign");
			CDATASection rnsignval = doc.createCDATASection(sign);
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
