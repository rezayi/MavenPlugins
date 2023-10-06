package morez.test.plugin.javatoproto;

import morez.plugin.javatoproto.ProtoClass;

import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) throws NoSuchFieldException {
        Map<String, ProtoClass> protoClassMap = new HashMap<>();
        System.out.println("\n====================================================\n");
        System.out.println(ProtoClass.fromClass(Data1.class, protoClassMap).toProto());
        System.out.println("\n====================================================\n");
        System.out.println(ProtoClass.fromClass(Data2.class, protoClassMap).toProto());
        System.out.println("\n====================================================\n");
    }
}
