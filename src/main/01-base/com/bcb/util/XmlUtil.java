/**
 * 
 */
package com.bcb.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *  xml文件转换为map
 *  @author G/2016-1-4
 *
 */
public class XmlUtil {
    public static Map<String,String> xmlToMap(String protocolXML) {
    	Map<String,String> params = new HashMap<>();
    	try{   
    		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
    		DocumentBuilder builder = factory.newDocumentBuilder();   
            Document doc = builder.parse(new InputSource(new StringReader(protocolXML)));   
            Element root = doc.getDocumentElement();   
            NodeList books = root.getChildNodes();   
            if(books != null){   
                for (int i = 0; i < books.getLength(); i++) {   
                	Node book = books.item(i);
                	if(book.getFirstChild()!=null && book.getFirstChild().getNodeValue()!=null)
                		params.put(book.getNodeName(),book.getFirstChild().getNodeValue());
//                    System.out.println("节点=" + book.getNodeName() + "\ttext=" + book.getFirstChild().getNodeValue());   
                 }   
            }   
        }catch(Exception e){   
             e.printStackTrace();   
        }
    	return params;
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     */
    public static String mapToXml(Map<String, String> data){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("xml");
            document.appendChild(root);
            for (String key: data.keySet()) {
                String value = data.get(key);
                if (value == null) {
                    value = "";
                }
                value = value.trim();
                Element filed = document.createElement(key);
                filed.appendChild(document.createTextNode(value));
                root.appendChild(filed);
            }
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
            writer.close();

            System.out.println("xml="+output);
            return output;
        }catch (Exception ex) {
        }
        return null;

    }

}
