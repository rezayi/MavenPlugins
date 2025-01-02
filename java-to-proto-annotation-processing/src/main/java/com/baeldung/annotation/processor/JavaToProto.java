package com.baeldung.annotation.processor;
public class JavaToProto {}
//
//import org.apache.maven.plugin.AbstractMojo;
//import org.apache.maven.plugins.annotations.LifecyclePhase;
//import org.apache.maven.plugins.annotations.Mojo;
//import org.apache.maven.plugins.annotations.Parameter;
//import org.apache.maven.project.MavenProject;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.nio.file.*;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.util.HashMap;
//import java.util.Map;
//
//@Mojo(name = "java-to-proto", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
//public class JavaToProto extends AbstractMojo {
//    @Parameter(defaultValue = "${project}", required = true, readonly = true)
//    private MavenProject project;
//    private String outputDirectory;
//    private Path startDir;
//
//    @Override
//    public void execute() {
//        outputDirectory = project.getBuild().getOutputDirectory();
//        startDir = Paths.get(outputDirectory);
//
//        try {
//            Files.walkFileTree(startDir, new SimpleFileVisitor<>() {
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
//                    if (file.toString().endsWith(".class")) {
//                        processClass(file);
//                    }
//                    return FileVisitResult.CONTINUE;
//                }
//            });
//        } catch (IOException e) {
//            getLog().error("Error while scanning files", e);
//        }
//    }
//
//    private void processClass(Path file) {
//        try {
//            String className = startDir.relativize(file).toString()
//                    .replace(File.separator, ".")
//                    .replace(".class", "");
//            getLog().info("class name: " + className);
//            try (URLClassLoader classLoader = new URLClassLoader(new URL[]{startDir.toUri().toURL()}, ConvertToProto.class.getClassLoader())) {
//                Class<?> clazz = classLoader.loadClass(className);
//                if (clazz.isAnnotationPresent(ConvertToProto.class)) {
//                    getLog().info("Found annotated");
//                    processAnnotatedClass(clazz);
//                }
//            }
//        } catch (Exception e) {
//            getLog().error("Error while loading class " + file.toString(), e);
//        }
//    }
//
//    private void processAnnotatedClass(Class<?> clazz) {
//        Map<String, ProtoClass> protoClassMap = new HashMap<>();
//        ProtoClass protoClass = ProtoClass.fromClass(clazz, protoClassMap);
//        getLog().info("proto class for class=[" + clazz.getName() + "] is " + protoClass);
//    }
//}
