package com.baeldung.annotation.processor;

import com.github.os72.protocjar.Protoc;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes("com.baeldung.annotation.processor.ConvertToProto")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class JavaToProtoBuilderProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            System.err.println("annotations:");
            System.err.println(annotation.getQualifiedName());

            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWithAny(annotation);
            System.err.println("annotatedElementsCount:" + annotatedElements.size());
            Map<String, ProtoClass> protoClassMap = new HashMap<>();
            annotatedElements.forEach(annotatedElement -> {
                if (annotatedElement instanceof TypeElement) {
                    try {
                        TypeElement typeElement = (TypeElement) annotatedElement;
                        ProtoClass protoClass = ProtoClass.fromClass(processingEnv, typeElement, protoClassMap);
                        System.err.println("proto class for class=[" + typeElement.getSimpleName() + "] is " + protoClass);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            });
            protoClassMap.forEach((className, protoClass) -> {
                try {
                    FileObject file = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", protoClass.getFileName());
                    try (Writer writer = file.openWriter()) {
                        writer.write(protoClass.toProto());
                    }
                    String classOutputPath = new File(file.toUri()).getParentFile().getAbsolutePath();
                    String[] args = new String[]{
                            "--java_out="+classOutputPath,
                            "--proto_path="+classOutputPath,
                            protoClass.getFileName().toString()
                    };
                    int exitCode = Protoc.runProtoc(args);

                    if (exitCode == 0) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Protobuf compilation successful!");
                    } else {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Protobuf compilation failed!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        }
        return true;
    }
}
