package morez.test.plugin.javatoproto;

import morez.plugin.javatoproto.ConvertToProto;

import java.util.Map;

@ConvertToProto
public class Data1 {
    private Integer a;
    private String b;
    private Map<String, String> c;
}
