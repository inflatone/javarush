package com.javarush.task.task33.task3309;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/*
Комментарий внутри xml
*/
public class Solution {
    public static String toXmlWithComment(Object obj, String tagName, String comment) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());




        return null;
    }

    public static void main(String[] args) {



    }

    public static class MyMarshallerListener extends Marshaller.Listener {
        private XMLStreamWriter xsw;
        private String comment;
        private String tagName;

        public  MyMarshallerListener(XMLStreamWriter xsw, String comment, String tagName) {
            this.xsw = xsw;
            this.comment = comment;
            this.tagName = tagName;
        }
        @Override
        public void beforeMarshal(Object source) {


            try {
                if (source.toString().equals(tagName)) {
                    xsw.writeComment(comment);
                }



            } catch (XMLStreamException xmlse) {
                xmlse.printStackTrace();
            }
        }
    }
}
