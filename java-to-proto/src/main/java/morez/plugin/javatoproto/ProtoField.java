package morez.plugin.javatoproto;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ProtoField {
    public enum Type {
        INTEGER("int32"),
        LONG("int64"),
        FLOAT("float"),
        DOUBLE("double"),
        STRING("string"),
        BOOLEAN("bool"),
        MAP("map"),
        LIST("list"),
        OBJECT("object");
        private final String protoType;

        Type(String protoType) {
            this.protoType = protoType;
        }

        public String getProtoType() {
            return protoType;
        }
    }

    private String name;
    private Type type;
    private Type subType1;
    private Type subType2;
    private String objectClassName;

    public String getName() {
        return name;
    }

    public String getType() {
        switch (type) {
            case MAP:
                return new StringBuilder().append("map<").append(subType1.getProtoType()).append(",").append(subType2.getProtoType()).append("> ").toString();
            case LIST:
                return "repeatd " + subType1.getProtoType();
            case OBJECT:
                return objectClassName;
            default:
                return type.getProtoType();
        }
    }

    public static ProtoField fromField(Field field, Map<String, ProtoClass> protoClassMap) {
        ProtoField protoField = new ProtoField();
        protoField.name = field.getName();
        protoField.type = getTypeByClass(field.getType());
        if (protoField.type == Type.MAP) {
            var parameterizedType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
            protoField.subType1 = getTypeByClass((Class<?>) parameterizedType[0]);
            protoField.subType2 = getTypeByClass((Class<?>) parameterizedType[1]);

        }
        if (protoField.type == Type.LIST) {
            var parameterizedType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
            protoField.subType1 = getTypeByClass((Class<?>) parameterizedType[0]);

        }
        if (protoField.type == Type.OBJECT) {
            protoField.objectClassName = field.getType().getName();
            if (!protoClassMap.containsKey(field.getType().getName())) {
                ProtoClass.fromClass(field.getType(), protoClassMap);
            }
        }
        return protoField;
    }

    private static Type getTypeByClass(Class<?> type) {
        if (type == Integer.TYPE || type == Integer.class) {
            return Type.INTEGER;
        } else if (type == Long.TYPE || type == Long.class) {
            return Type.LONG;
        } else if (type == Float.TYPE || type == Float.class) {
            return Type.FLOAT;
        } else if (type == Double.TYPE || type == Double.class) {
            return Type.DOUBLE;
        } else if (type == Boolean.TYPE) {
            return Type.BOOLEAN;
        } else if (type.isAssignableFrom(String.class)) {
            return Type.STRING;
        } else if (type.isAssignableFrom(Map.class)) {
            return Type.MAP;
        } else if (type.isAssignableFrom(List.class) || type.isAssignableFrom(Set.class) || type.isAssignableFrom(Array.class)) {
            return Type.LIST;
        } else {
            return Type.OBJECT;
        }
    }
}
