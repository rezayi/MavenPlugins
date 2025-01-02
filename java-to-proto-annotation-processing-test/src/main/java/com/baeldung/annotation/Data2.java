package com.baeldung.annotation;

import com.baeldung.annotation.processor.ConvertToProto;

import java.util.List;
import java.util.Map;

@ConvertToProto
public class Data2 {
    private Integer a2;
    private String b2;
    private List<Integer> c2;
    private Data1 d2;
    private List<Data1> e2;
    private Map<String, Data1> f2;
    private Map<String, OtherClass2> f3;
}
