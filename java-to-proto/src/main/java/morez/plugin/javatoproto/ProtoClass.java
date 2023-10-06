package morez.plugin.javatoproto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProtoClass {
    private String className;
    private String packageName;
    private final List<ProtoField> fields = new ArrayList<>();

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public List<ProtoField> getFields() {
        return fields;
    }

    private void addField(Field field, Map<String, ProtoClass> protoClassMap) {
        this.fields.add(ProtoField.fromField(field, protoClassMap));
    }

    public String toProto() {
        StringBuilder builder = new StringBuilder();
        builder.append("message ").append(className).append(" {\n");
        for (int i = 0; i < fields.size(); i++) {
            var field = fields.get(i);
            builder.append("\t").append(field.getType()).append(" ").append(field.getName()).append(" = ").append(i + 1).append(";\n");
        }
        builder.append("}");
        return builder.toString();
    }


    public static ProtoClass fromClass(Class<?> clazz, Map<String, ProtoClass> protoClassMap) {
        ProtoClass protoClass = new ProtoClass();
        protoClass.className = clazz.getSimpleName();
        protoClass.packageName = clazz.getPackageName();
        protoClassMap.put(clazz.getName(), protoClass);
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> protoClass.addField(field, protoClassMap));
        return protoClass;
    }
}
