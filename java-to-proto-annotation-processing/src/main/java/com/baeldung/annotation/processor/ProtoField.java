package com.baeldung.annotation.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Map;

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
    private ProtoSubType subType1;
    private ProtoSubType subType2;
    private String objectClassName;

    public String getName() {
        return name;
    }

    public String getType() {
        switch (type) {
            case MAP:
                return new StringBuilder().append("map<").append(subType1.getType()).append(",").append(subType2.getType()).append("> ").toString();
            case LIST:
                return "repeated " + subType1.getType();
            case OBJECT:
                return objectClassName + "Proto";
            default:
                return type.getProtoType();
        }
    }

    public static ProtoField fromField(
            ProcessingEnvironment processingEnv,
            VariableElement field,
            Map<String, ProtoClass> protoClassMap,
            List<ProtoClass> imports
    ) {
        TypeMirror typeMirror = field.asType();
        DeclaredType declaredType = (DeclaredType) typeMirror;

        ProtoField protoField = new ProtoField();
        protoField.name = field.getSimpleName().toString();
        protoField.type = getTypeByClass(declaredType.asElement().toString());
        if (protoField.type == Type.MAP) {
            List<? extends TypeMirror> parameterizedType = declaredType.getTypeArguments();
            protoField.subType1 = getProtoSubType(processingEnv, parameterizedType.get(0).toString(), protoClassMap, imports);
            protoField.subType2 = getProtoSubType(processingEnv, parameterizedType.get(1).toString(), protoClassMap, imports);
        }
        if (protoField.type == Type.LIST) {
            List<? extends TypeMirror> parameterizedType = declaredType.getTypeArguments();
            protoField.subType1 = getProtoSubType(processingEnv, parameterizedType.get(0).toString(), protoClassMap, imports);

        }
        if (protoField.type == Type.OBJECT) {
            protoField.objectClassName = field.asType().toString();
            if (!protoClassMap.containsKey(protoField.objectClassName)) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(protoField.objectClassName);
                ProtoClass.fromClass(processingEnv, typeElement, protoClassMap);
            }
            if (!imports.contains(protoClassMap.get(protoField.objectClassName)))
                imports.add(protoClassMap.get(protoField.objectClassName));
        }
        return protoField;
    }

    private static Type getTypeByClass(String className) {
        switch (className) {
            case "int":
            case "java.lang.Integer":
                return Type.INTEGER;
            case "long":
            case "java.lang.Long":
                return Type.LONG;
            case "float":
            case "java.lang.Float":
                return Type.FLOAT;
            case "double":
            case "java.lang.Double":
                return Type.DOUBLE;
            case "boolean":
            case "java.lang.Boolean":
                return Type.BOOLEAN;
            case "java.lang.String":
                return Type.STRING;
            case "java.util.Map":
                return Type.MAP;
            case "java.util.List":
            case "java.util.Set":
                return Type.LIST;
            default:
                return Type.OBJECT;
        }
    }

    private static ProtoSubType getProtoSubType(
            ProcessingEnvironment processingEnv,
            String type,
            Map<String, ProtoClass> protoClassMap,
            List<ProtoClass> imports
    ) {
        Type rawType = getTypeByClass(type);
        if (rawType == Type.OBJECT && !protoClassMap.containsKey(type)) {
            TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(type);
            ProtoClass.fromClass(processingEnv, typeElement, protoClassMap);
        }
        if (rawType == Type.OBJECT) {
            if (!imports.contains(protoClassMap.get(type)))
                imports.add(protoClassMap.get(type));
            return ProtoSubType.fromProtoType(rawType, type);
        } else if (rawType == Type.MAP || rawType == Type.LIST) {
            throw new RuntimeException("subtype not supported");
        } else {
            return ProtoSubType.fromProtoType(rawType);
        }
    }
}
