package com.baeldung.annotation.processor;

class ProtoSubType {
    private ProtoField.Type type;
    private String objectClassName;

    public String getType() {
        switch (type) {
            case LIST:
            case MAP:
                throw new RuntimeException("subtype not supported");
            case OBJECT:
                return objectClassName+"Proto";
            default:
                return type.getProtoType();
        }
    }

    public static ProtoSubType fromProtoType(ProtoField.Type type) {
        ProtoSubType protoSubType = new ProtoSubType();
        protoSubType.type = type;
        protoSubType.objectClassName = type.getProtoType();
        return protoSubType;
    }

    public static ProtoSubType fromProtoType(ProtoField.Type type, String objectClassName) {
        ProtoSubType protoSubType = new ProtoSubType();
        protoSubType.type = type;
        protoSubType.objectClassName = objectClassName;
        return protoSubType;
    }
}
