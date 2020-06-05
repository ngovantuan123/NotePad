package com.tuangh.notepad;


import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XuLyFileXML {
    public static ArrayList<NoteItem> readByDOM(String file) {
        ArrayList<NoteItem> items = new ArrayList<NoteItem>();
        NoteItem item = null;
        String title = "", content = "", createdDate = "";


        try {
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = fac.newDocumentBuilder();
            String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
            String xmlfile = sdcard + "/"+file;
            FileInputStream fIn = new FileInputStream(xmlfile);
            Document doc=builder.parse(fIn);
            Element root= doc.getDocumentElement();
            NodeList list= root.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node instanceof Element) {
                    Element element = (Element) node;
                    title = element.getAttribute("title");
                    NodeList listchild = element.getElementsByTagName("content");
                    NodeList listchild1 = element.getElementsByTagName("createddate");
                    content = listchild.item(0).getTextContent();
                    createdDate = listchild1.item(0).getTextContent();
                    item = new NoteItem();
                    item.setTitle(title);
                    item.setContent(content);
                    item.setCreatedDate(createdDate);
                    items.add(item);

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            return items;
        }
    }
    public static void saveData(String fileName, ArrayList<NoteItem> noteItems){
        File file;
        FileOutputStream outputStream;
        StringBuilder data=new StringBuilder();
        //convert to string
        data.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        data.append("<notes>\n");
        for(NoteItem item :noteItems)
        {

            data.append("<note title=\"" +item.getTitle()+"\">\n");
            data.append("<content>"+item.getContent()+"</content>\n");
            data.append("<createddate>"+item.getCreatedDate()+"</createddate>\n");
            data.append("</note>\n");
        }
        data.append("</notes>");



        String content=data.toString();

        try {
            file = new File(Environment.getExternalStorageDirectory(), fileName);
            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

