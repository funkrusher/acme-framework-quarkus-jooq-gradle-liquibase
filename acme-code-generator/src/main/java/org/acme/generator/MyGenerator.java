package org.acme.generator;

import jakarta.validation.Valid;
import org.acme.generated.AbstractDTO;
import org.jooq.codegen.GeneratorStrategy;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.Definition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.TypedElementDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MyGenerator extends JavaGenerator {

    private static final Pattern SQUARE_BRACKETS = Pattern.compile("\\[\\]$");


    @Override
    public void setGenerateValidationAnnotations(boolean generateValidationAnnotations) {
        super.setGenerateValidationAnnotations(generateValidationAnnotations);
    }


    @Override
    protected void printClassAnnotations(JavaWriter out, Definition definition, GeneratorStrategy.Mode mode) {
        super.printClassAnnotations(out, definition, mode);

        out.println("@%s", out.ref(Valid.class));
    }

    @Override
    protected void generatePojo(TableDefinition table) {
        super.generatePojo(table);
    }

    @Override
    protected String getJavaType(DataTypeDefinition type, JavaWriter out) {
        return super.getJavaType(type, out);
    }

    @Override
    protected void generatePojoSetter(TypedElementDefinition<?> column, int index, JavaWriter out) {
        final String className = getStrategy().getJavaClassName(column.getContainer(), GeneratorStrategy.Mode.POJO);
        final String columnTypeFull = getJavaType(column.getType(resolver(out, GeneratorStrategy.Mode.POJO)), out, GeneratorStrategy.Mode.POJO);
        final String columnType = out.ref(columnTypeFull);
        final String columnSetter = getStrategy().getJavaSetterName(column, GeneratorStrategy.Mode.POJO);
        final String columnMember = getStrategy().getJavaMemberName(column, GeneratorStrategy.Mode.POJO);
        final String name = column.getQualifiedOutputName();

        if (!printDeprecationIfUnknownType(out, columnTypeFull))
            out.javadoc("Setter for <code>%s</code>.", name);

        final String nullableAnnotation = nullableOrNonnullAnnotation(out, column);

        out.overrideIf(generateInterfaces() && !generateImmutableInterfaces());
        out.println("%s %s %s([[before=@][after= ][%s]]%s %s) {", "public", "void", columnSetter, list(nullableAnnotation), varargsIfArray(columnType), columnMember);
        out.println("this.%s = %s;", columnMember, columnMember);
        out.println("this.setAt(\"%s\", %s);", columnMember, columnMember);
        if (generateFluentSetters())
            out.println("return this;");

        out.println("}");
    }

/*
    @Override
    protected void generatePojoClassFooter(TableDefinition table, JavaWriter out) {
        super.generatePojoClassFooter(table, out);

        out.println();
        out.tab(0).println("private Map<String, Object> bookKeepingMap = new HashMap<>();");
        out.tab(0).println("");
        out.tab(0).println("@JsonIgnore");
        out.tab(0).println("@XmlTransient");
        out.tab(0).println("protected void setAt(String key, Object value) {");
        out.tab(1).println("// inspired by: https://blog.jooq.org/orms-should-update-changed-values-not-just-modified-ones/");
        out.tab(1).println("this.bookKeepingMap.put(key, value);");
        out.tab(0).println("}");
        out.tab(0).println("");
        out.tab(0).println("@JsonIgnore");
        out.tab(0).println("@XmlTransient");
        out.tab(0).println("public void clearBookKeepingMap() {");
        out.tab(1).println("this.bookKeepingMap.clear();;");
        out.tab(0).println("}");
        out.tab(0).println("");
        out.tab(0).println("@JsonIgnore");
        out.tab(0).println("@XmlTransient");
        out.tab(0).println("public Map<String, Object> getBookKeepingMap() {");
        out.tab(1).println("return this.bookKeepingMap;");
        out.tab(0).println("}");

        out.ref(Map.class);
        out.ref(HashMap.class);
        out.ref(JsonIgnore.class);
        out.ref(XmlTransient.class);
    }
*/

    @Override
    protected void generatePojoClassFooter(TableDefinition table, JavaWriter out) {
        super.generatePojoClassFooter(table, out);
        out.ref(AbstractDTO.class);
    }


    private boolean printDeprecationIfUnknownType(JavaWriter out, String type) {
        return printDeprecationIfUnknownType(out, type, "");
    }

    private boolean printDeprecationIfUnknownType(JavaWriter out, String type, String precision) {
        if (generateDeprecationOnUnknownTypes() && (Object.class.getName().equals(type))) {
            out.javadoc("@deprecated "
                    + "Unknown data type. " + precision
                    + "If this is a qualified, user-defined type, it may have been excluded from code generation. "
                    + "If this is a built-in type, you can define an explicit {@link org.jooq.Binding} to specify how this type should be handled. "
                    + "Deprecation can be turned off using {@literal <deprecationOnUnknownTypes/>} in your code generator configuration."
            );
            out.println("@%s", out.ref(Deprecated.class));
            return true;
        } else {
            return false;
        }
    }

    private String nullableOrNonnullAnnotation(JavaWriter out, Definition column) {
        return (column instanceof TypedElementDefinition && ((TypedElementDefinition<?>) column).getType().isNullable())
                ? nullableAnnotation(out)
                : nonnullAnnotation(out);
    }

    private String nullableAnnotation(JavaWriter out) {
        return generateNullableAnnotation() ? out.ref(generatedNullableAnnotationType()) : null;
    }

    private String nonnullAnnotation(JavaWriter out) {
        return generateNonnullAnnotation() ? out.ref(generatedNonnullAnnotationType()) : null;
    }

    private void printNullableOrNonnullAnnotation(JavaWriter out, Definition column) {
        if (column instanceof TypedElementDefinition && ((TypedElementDefinition<?>) column).getType().isNullable())
            printNullableAnnotation(out);
        else
            printNonnullAnnotation(out);
    }

    protected void printNullableAnnotation(JavaWriter out) {
        if (generateNullableAnnotation())
            out.println("@%s", out.ref(generatedNullableAnnotationType()));
    }

    protected void printNonnullAnnotation(JavaWriter out) {
        if (generateNonnullAnnotation())
            out.println("@%s", out.ref(generatedNonnullAnnotationType()));
    }

    private static final <T> List<T> list(T... objects) {
        List<T> result = new ArrayList<>();

        if (objects != null)
            for (T object : objects)
                if (object != null && !"".equals(object))
                    result.add(object);

        return result;
    }

    private String varargsIfArray(String type) {
        if (!generateVarargsSetters())
            return type;
        else
            return SQUARE_BRACKETS.matcher(type).replaceFirst("...");
    }


}
