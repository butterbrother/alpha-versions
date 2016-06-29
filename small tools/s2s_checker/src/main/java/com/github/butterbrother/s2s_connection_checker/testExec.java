package com.github.butterbrother.s2s_connection_checker;

import org.xbill.DNS.*;

import java.io.IOException;

/**
 * Created by user on 29.06.2016.
 */
public class testExec {

    public static void main(String args[]) throws IOException {
        int type = Type.A;
        int dclass = DClass.IN;
        Name name = Name.fromString("ya.ru", Name.root);
        Name ggl = Name.fromString("google.com", Name.root);

        Record rec = Record.newRecord(name, type, dclass);
        Message query = Message.newQuery(rec);

        SimpleResolver resolver = new SimpleResolver("192.168.128.2");
        resolver.setPort(53);
        SimpleResolver gdns = new SimpleResolver("8.8.8.8");

        Message response = resolver.send(query);
        System.out.println(response);

        System.out.println("---");

        rec = Record.newRecord(ggl, type, dclass);
        query = Message.newQuery(rec);
        response = gdns.send(query);

        System.out.println(response);

        SimpleResolver def = new SimpleResolver();

        System.out.println("---");

        System.out.println("Def: " + def.getAddress());
        System.out.println("Named: " + resolver.getAddress());
        System.out.println("Google: " + gdns.getAddress());

        System.out.println("---");

        for (Record item : response.getSectionArray(Section.ANSWER)) {
            System.out.println(item.rdataToString());
        }
    }
}
