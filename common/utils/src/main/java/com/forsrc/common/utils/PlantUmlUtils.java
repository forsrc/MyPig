package com.forsrc.common.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlantUmlUtils {

    public static void search(final File path, final FileHandler handler) throws Exception {
        if (path.isFile()) {
            handler.handle(path);
            return;
        }
        File[] files = path.listFiles();
        for (File file : files) {
            // System.out.println(file);
            if (file.isDirectory()) {
                search(file, handler);
                continue;
            }
            handler.handle(file);
        }
    }

    public static String plantuml(final File path) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        search(path, new FileHandler() {

            @Override
            public void handle(File file) throws Exception {
                // System.out.println(file);
                String filename = file.getName();
                if (filename.endsWith(".java")) {
                    sb.append(handleJavaCode(file));
                }
            }
        });
        sb.append("\n");
        sb.append("@endtuml");
        return sb.toString();
    }

    public static String plantuml(Config config) throws Exception {

        PlantumlClassDiagram plantumlClassDiagram = new PlantumlClassDiagram();
        plantumlClassDiagram.setPackageName(config.getPackageName());
        plantumlClassDiagram.getConfigs().add("top to bottom direction");

        search(config.getPath(), new FileHandler() {

            @Override
            public void handle(File file) throws Exception {
                if (file.getName().endsWith(".class")) {
                    Class<?> cls = toClass(file, config.getPackageName());
                    PlantumlClass plantumlClass = new PlantumlClass(cls, config.getPackageName());
                    plantumlClassDiagram.getPlantumlClasses().add(plantumlClass);
                }
            }
        });

        return plantumlClassDiagram.toUml();
    }

    public static String handleJavaCode(File javaCode) {
        StringBuilder sb = new StringBuilder();
        // TODO
        return sb.toString();
    }


    public static Class<?> toClass(final File classFile, final String packageName) throws ClassNotFoundException {
        if (classFile.isDirectory()) {
            return null;
        }
        String fileName = classFile.getName();
        // @formatter:off
        String path = classFile.getAbsolutePath()
                .replace("\\", "/")
                .replace(fileName, "")
                .replaceAll(String.format(".+/*%s/*(\\w*.*\\w*)/*", packageName), "$1")
                ;
        String className = String.format("%s.%s.%s", packageName, path, fileName.replace(".class", ""))
                .replace("/", ".")
                .replace("..", ".");
        // @formatter:on
        Class<?> cls = Class.forName(className);
        return cls;
    }

    public static String getPackagePath(String packageName) {
        String name = packageName.replace(".", "/");
        return PlantUmlUtils.class.getClassLoader().getResource(name).getFile();
    }

    public static interface FileHandler {
        public void handle(final File file) throws Exception;
    }

    public static class Config {
        private String packageName;
        private File path;

        public Config() {
        }

        public Config(String packageName) {
            this.packageName = packageName;
            String path = PlantUmlUtils.getPackagePath(packageName);
            this.path = new File(path);
        }

        public Config(String packageName, File path) {
            this.packageName = packageName;
            this.path = path;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public File getPath() {
            return path;
        }

        public void setPath(File path) {
            this.path = path;
        }
    }

    public enum VisibilityType {
        // @formatter:off
        SYMBOL(" "),

        STATIC("{static}"),
        ABSTRACT("{abstract}"),
        
        PRIVATE("-"),
        PROTECTED("#"),
        PUBLIC("+"),
        PACKAGE("~");
        // @formatter:on

        private String symbol;

        private VisibilityType(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return this.symbol;
        }

        public static String getSymbol(Field f) {
            return getSymbol(f.toGenericString());
        }

        public static String getSymbol(Method m) {
            return getSymbol(m.toGenericString());
        }

        private static String getSymbol(String code) {

            StringBuilder sb = new StringBuilder();
            // @formatter:off
                if (code.indexOf("private ") >= 0) {
                    sb.append(PRIVATE.symbol);
                } else if (code.indexOf("protected ") >= 0) {
                    sb.append(PROTECTED.symbol);
                } else if (code.indexOf("public ") >= 0) {
                    sb.append(PUBLIC.symbol);
                } else {
                    sb.append(PACKAGE.symbol);
                }
                if (code.indexOf("static ") >= 0) {
                    sb.append(" ");
                    sb.append(STATIC.symbol);
                }
                if (code.indexOf("abstract ") >= 0) {
                    sb.append(" ");
                    sb.append(ABSTRACT.symbol);
                }
                // @formatter:on
            return sb.toString();
        }
    }

    public static enum RelationType {

        SYMBOL(" "),

        DEPENDENCY("...>"),

        ASSOCIATION("---"),

        AGGREGATION("o---"),

        COMPOSITION("*---"),

        GENERALIZATION("-up--|>"),

        REALIZATION(".up..|>");

        private String symbol;

        private RelationType(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol(String from, String to) {
            return String.format("%s %s %s", from, this.symbol, to);
        }

    }

    public static interface Handler<T, R> {
        public R handle(T t, String packageName);
    }

    public static class PlantumlClassDiagram {
        private String packageName;
        private List<String> configs = new ArrayList<>();
        private List<PlantumlClass> plantumlClasses = new ArrayList<>();

        public List<String> getConfigs() {
            return configs;
        }

        public void setConfigs(List<String> configs) {
            this.configs = configs;
        }

        public List<PlantumlClass> getPlantumlClasses() {
            return plantumlClasses;
        }

        public void setPlantumlClasses(List<PlantumlClass> plantumlClasses) {
            this.plantumlClasses = plantumlClasses;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String toUml() {

            StringBuilder sb = new StringBuilder();
            // @formatter:off
            sb.append("@startuml\n");
            sb.append("\n");
            for (String config : configs) {
                sb.append("    ");
                sb.append(config);
                sb.append("\n");
            }
            sb.append("\n");
            sb.append("\n");
            for (PlantumlClass plantumlClass : plantumlClasses) {
                sb.append(plantumlClass.toUml());
                sb.append("\n");
            }
            sb.append("@enduml");
            // @formatter:on
            return sb.toString();
        }

        @Override
        public String toString() {
            return toUml();
        }
    }

    public static class PlantumlClass {
        private String classType;
        private String className;
        private String classGeneralType;
        private List<String> fields = new ArrayList<>();
        private List<String> methods = new ArrayList<>();
        private List<String> relations = new ArrayList<>();

        private Class<?> plantumlClass;
        private String packageName;

        public PlantumlClass(Class<?> plantumlClass, String packageName) {
            this.plantumlClass = plantumlClass;
            this.packageName = packageName;
            init();
        }

        public void init() {
            this.className = new Handler<Class<?>, String>() {
                @Override
                public String handle(Class<?> cls, String packageName) {
                    String genericString = cls.toGenericString();
                    int genericIndexOf = genericString.indexOf("<");
                    genericString = genericIndexOf > 0 ? genericString.substring(genericIndexOf) : "";
                    return String.format("%s%s", cls.getName(), genericString);
                }
            }.handle(this.plantumlClass, this.packageName);

            this.classType = new Handler<Class<?>, String>() {
                @Override
                public String handle(Class<?> cls, String packageName) {
                    if (cls.isEnum()) {
                        return "enum";
                    }
                    if (!cls.isInterface() && cls.toString().indexOf("abstract") > 0) {
                        return "abstract";
                    }
                    if (cls.isInterface()) {
                        return "interface";
                    }
                    return "class";
                }
            }.handle(this.plantumlClass, this.packageName);

            this.classGeneralType = new Handler<Class<?>, String>() {
                @Override
                public String handle(Class<?> cls, String packageName) {
                    if (plantumlClass.isAnnotation()) {
                        return String.format("%s << (@,#FF7700) Annotation >>", cls.toString());
                    }
                    Annotation[] annotations = cls.getAnnotations();
                    if (annotations != null && annotations.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(" << ");
                        for (Annotation annotation : annotations) {
                            sb.append(annotation.annotationType().getSimpleName()).append(" ");
                        }
                        sb.append(" >> ");
                        return sb.toString();
                    }
                    return "";
                }
            }.handle(this.plantumlClass, this.packageName);

            this.fields.addAll(new Handler<Class<?>, List<String>>() {
                @Override
                public List<String> handle(Class<?> cls, String packageName) {

                    Field[] fields = cls.getDeclaredFields();
                    List<String> list = new ArrayList<>(fields.length);

                    for (Field field : fields) {
                        String definingVisibility = cls.isInterface() ? VisibilityType.PUBLIC.getSymbol()
                                : VisibilityType.getSymbol(field);
                        // @formatter:off
                        String fieldName = field.toGenericString()
                                            .replace(cls.getName() + ".", "")
                                            .replace(cls.getPackage().getName() + ".", "")
                                            .replaceAll("java.lang.", "");
                        // @formatter:on

                        if (cls.isEnum()) {
                            if (field.getName().endsWith("$VALUES")) {
                                continue;
                            }
                            if (field.getType().isEnum()) {
                                fieldName = field.getName();
                            }
                        }
                        list.add(String.format("%s %s", definingVisibility, fieldName));
                    }
                    return list;
                }
            }.handle(this.plantumlClass, this.packageName));

            this.methods.addAll(new Handler<Class<?>, List<String>>() {
                @Override
                public List<String> handle(Class<?> cls, String packageName) {
                    Method[] methods = cls.getDeclaredMethods();
                    List<String> list = new ArrayList<>(methods.length);
                    if (cls.isEnum()) {
                        return Collections.emptyList();
                    }

                    for (Method method : methods) {

                        String definingVisibility = cls.isInterface() ? VisibilityType.PUBLIC.getSymbol()
                                : VisibilityType.getSymbol(method);

                     // @formatter:off
                        String methodName = method.toGenericString()
                                            .replace(cls.getName() + ".", "")
                                            .replace(cls.getPackage().getName() + ".", "")
                                            .replaceAll("java.lang.", "");
                        // @formatter:on
                        list.add(String.format("%s %s", definingVisibility, methodName));
                    }
                    return list;
                }
            }.handle(this.plantumlClass, this.packageName));

            this.relations.addAll(new Handler<Class<?>, List<String>>() {
                @Override
                public List<String> handle(Class<?> cls, String packageName) {

                    List<String> list = new ArrayList<>();

                    final Set<String> relations = new HashSet<>();
                    Field[] fields = cls.getDeclaredFields();
                    for (Field field : fields) {
                        boolean isAggregation = false;
                        String genericString = field.toGenericString();
                        System.out.println(genericString);
                        Class<?> type = field.getType();
                        if (packageName != null && type.getName().indexOf(packageName) < 0) {
                            continue;
                        }
                        try {
                            type.asSubclass(Collection.class);
                            isAggregation = true;
                        } catch (Exception e) {
                        }
                        if (type.isAssignableFrom(Collection.class) || type.isArray()) {
                            isAggregation = true;
                        }
                        // System.out.println(genericString);
                        String[] codes = genericString.split(" ");
                        for (String code : codes) {

                            // String relation = String.format("%s ---> %s", cls.getName(),
                            // code.replace("[]", ""));
                            String relation = RelationType.DEPENDENCY.getSymbol(cls.getName(), code.replace("[]", ""));

                            if (relations.contains(relation)) {
                                continue;
                            }
                            if (isAggregation) {
                                // relation = String.format("%s o--- %s", cls.getName(),
                                // code.replace("[]", "").replaceAll(".*<(.+)>", "$1"));
                                relation = RelationType.AGGREGATION.getSymbol(cls.getName(),
                                        code.replace("[]", "").replaceAll(".*<(.+)>", "$1"));

                            }
                            if ((!isAggregation && code.startsWith("java.")) || code.indexOf(".") < 0) {
                                continue;
                            }
                            if (cls.isEnum() && code.indexOf(cls.getName()) >= 0) {
                                continue;
                            }
                            if (code.matches(String.format("%s\\.[a-z]+.*", cls.getName()))) {
                                continue;
                            }
                            if (cls.getName().matches(".+\\$\\d+.*")) {
                                continue;
                            }
                            relations.add(relation);
                            list.add(relation);
                            break;
                        }
                    }

                    Method[] methods = cls.getDeclaredMethods();

                    for (Method method : methods) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        for (Class<?> parameterType : parameterTypes) {
                            // System.out.println(code);
                            if (packageName != null && parameterType.getName().indexOf(packageName) < 0) {
                                continue;
                            }
                            // String relation = String.format("%s ...> %s", cls.getName(),
                            // parameterType.getName().replace("[]", ""));
                            String relation = RelationType.DEPENDENCY.getSymbol(cls.getName(),
                                    parameterType.getName().replace("[]", ""));

                            if (parameterType.getName().indexOf("java.") >= 0
                                    || parameterType.getName().indexOf(".") < 0) {
                                continue;
                            }
                            if (relations.contains(relation)) {
                                continue;
                            }
                            relations.add(relation);
                            list.add(relation);
                        }

                    }
                    Constructor<?>[] constructors = cls.getDeclaredConstructors();
                    for (Constructor<?> constructor : constructors) {
                        // System.out.println(constructor.toGenericString());
                        Class<?>[] parameterTypes = constructor.getParameterTypes();
                        for (Class<?> parameterType : parameterTypes) {
                            // System.out.println(parameterType);
                            if (packageName != null && parameterType.getName().indexOf(packageName) < 0) {
                                continue;
                            }
                            if (parameterType.getName().indexOf("java.") >= 0
                                    || parameterType.getName().indexOf(".") < 0) {
                                continue;
                            }
                            // String relation = String.format("%s *--- %s", cls.getName(),
                            // parameterType.getName().replace("[]", ""));
                            String relation = RelationType.COMPOSITION.getSymbol(cls.getName(),
                                    parameterType.getName().replace("[]", ""));

                            if (relations.contains(relation)) {
                                continue;
                            }
                            relations.add(relation);
                            list.add(relation);
                        }
                    }

                    Class<?>[] clss = cls.getInterfaces();
                    for (Class<?> c : clss) {
                        if (packageName != null && c.getName().indexOf(packageName) < 0) {
                            continue;
                        }
                        if (c.getName().indexOf("java.") < 0) {

                            // list.add(String.format("%s .up..|> %s", cls.getName(), c.getName()));
                            list.add(RelationType.GENERALIZATION.getSymbol(cls.getName(), c.getName()));
                        }
                    }
                    Class<?> superclass = cls.getSuperclass();
                    if (superclass != null && packageName != null && superclass.getName().indexOf(packageName) < 0) {
                        return list;
                    }
                    if (superclass != null && superclass.getName().indexOf("java.") < 0) {
                        // list.add(String.format("%s -up--|> %s", cls.getName(),
                        // superclass.getName()));
                        list.add(RelationType.GENERALIZATION.getSymbol(cls.getName(), superclass.getName()));
                    }
                    return list;
                }
            }.handle(this.plantumlClass, this.packageName));

        }

        public String toUml() {
            StringBuilder sb = new StringBuilder();

            sb.append("    ");
            sb.append(classType);
            sb.append(" ");
            sb.append(className);
            sb.append(" ");
            sb.append(classGeneralType);
            sb.append(" {\n");
            for (String str : fields) {
                sb.append("        ");
                sb.append(str);
                sb.append("\n");
            }
            sb.append("\n");
            for (String str : methods) {
                sb.append("        ");
                sb.append(str);
                sb.append("\n");
            }
            sb.append("    }\n");

            for (String str : relations) {
                sb.append("        ");
                sb.append(str);
                sb.append("\n");
            }

            return sb.toString();
        }

        public String getClassType() {
            return classType;
        }

        public void setClassType(String classType) {
            this.classType = classType;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getClassGeneralType() {
            return classGeneralType;
        }

        public void setClassGeneralType(String classGeneralType) {
            this.classGeneralType = classGeneralType;
        }

        public List<String> getFields() {
            return fields;
        }

        public void setFields(List<String> fields) {
            this.fields = fields;
        }

        public List<String> getMethods() {
            return methods;
        }

        public void setMethods(List<String> methods) {
            this.methods = methods;
        }

        public List<String> getRelations() {
            return relations;
        }

        public void setRelations(List<String> relations) {
            this.relations = relations;
        }

        public Class<?> getPlantumlClass() {
            return plantumlClass;
        }

        public void setPlantumlClass(Class<?> plantumlClass) {
            this.plantumlClass = plantumlClass;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

    }

    public static void main(String[] args) throws Exception {
        String path = PlantUmlUtils.getPackagePath("com.forsrc");
        Config config = new Config("com.forsrc", new File(path));
        String text = PlantUmlUtils.plantuml(config);
        System.out.println(text);
    }

}