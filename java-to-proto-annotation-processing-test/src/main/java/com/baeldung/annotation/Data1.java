package com.baeldung.annotation;


import java.util.Map;

public class Data1 {
    private Integer a;
    private String b;
    private Map<String, String> c;
    private Map<String, OtherClass1> d;

    //    public Data1Proto toProto() {
    //        Data1Proto.Builder builder = Data1Proto.newBuilder();
    //        builder.setA(a);
    //        builder.setB(b);
    //        c.forEach((key, value) -> builder.putC(key, value));
    //        d.forEach((key, value) -> builder.putD(key, value.toProto()));
    //        return builder.build();
    //    }
}
