package org.acme.generator;

import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.Definition;

public final class MyGeneratorStrategy extends DefaultGeneratorStrategy {


    @Override
    public String getJavaClassExtends(Definition definition, Mode mode) {
        String defaultJavaClassExtends = super.getJavaClassExtends(definition, mode);
        if (mode == Mode.POJO) {
            return "AbstractDTO";
        }
        return defaultJavaClassExtends;
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        String defaultPackageName = super.getJavaPackageName(definition, mode);
        if (mode == Mode.POJO) {
            return defaultPackageName.replace("pojo", "dto");
        }
        return defaultPackageName;
    }

    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        return "set" + definition.getOutputName().substring(0, 1).toUpperCase() + definition.getOutputName().substring(1);
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        return "get" + definition.getOutputName().substring(0, 1).toUpperCase() + definition.getOutputName().substring(1);
    }


    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        return definition.getOutputName();
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {
        return definition.getOutputName();
    }
}
