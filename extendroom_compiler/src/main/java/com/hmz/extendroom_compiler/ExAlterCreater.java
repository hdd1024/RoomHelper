package com.hmz.extendroom_compiler;


import androidx.room.migration.Migration;

import com.hmz.extendroom_annotation.ExAlter;
import com.squareup.javapoet.FieldSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;

/***********************************************************
 * 创建时间:2020-07-02
 * 作   者: [hanmingze]
 * 功能描述: <用于处理数据库的增删改标签的功能>
 * 备注信息: {}
 * @see
 **********************************************************/
public class ExAlterCreater {
    private MigrateElementBean migrateElementBean;
    private Elements elements;
    private ExecSQLBuildr execSQLBuildr;
    private StringBuffer migrateBuffer;

    public ExAlterCreater(Elements elements, MigrateElementBean bean) {
        this.elements = elements;
        this.migrateElementBean = bean;
        execSQLBuildr = new ExecSQLBuildr();
        migrateBuffer = new StringBuffer();
    }


    public FieldSpec alterMigration() {
        migrateBuffer.setLength(0);

        // static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        //        @Override
        //        public void migrate(SupportSQLiteDatabase database) {
        //            database.execSQL("CREATE TABLE `tb_version_manual_info` (`id` INTEGER  NOT NULL, "
        //                    + "`oldVersion` TEXT,`code` TEXT,`environment` TEXT,`terminalid` TEXT," +
        //                    "`versionNo` TEXT,PRIMARY KEY(`id`))");
        //        }
        //    };

        int startVersion = migrateElementBean.getStartVersion();
        int endVersion = migrateElementBean.getEndVersion();
        String database = "database";
        String fieldName = "MIGRATION_" + startVersion + "_" + endVersion;
        FieldSpec.Builder builder = FieldSpec.builder(Migration.class, fieldName);
//        builder.addModifiers(Modifier.FINAL, Modifier.STATIC);
        for (Element migrate : migrateElementBean.getMigrates()) {
            String m = execSQLBuildr.getAlterSqlChange(database, migrate);
            migrateBuffer.append(m);
            migrateBuffer.append("\n");
        }
        System.out.println("\n生成的》》》》》》》" + migrateBuffer.toString());

//        System.out.println();
        builder.initializer("new $T($L,$L){ @Override\n  public void migrate(androidx.sqlite.db.SupportSQLiteDatabase database){$L}}",
                Migration.class, startVersion, endVersion, migrateBuffer.toString());

        return builder.build();
    }


    public String getAlterClassName(Element element) {
        return element.getSimpleName().toString();
    }

    public String getAlterClassPackageName(Element element) {
        PackageElement packageOf = elements.getPackageOf(element);
        return packageOf.getQualifiedName().toString();
    }

    public String getAlterClassFullName(Element element) {
        PackageElement packageOf = elements.getPackageOf(element);
        String classPackageName = packageOf.getQualifiedName().toString();
        String className = element.getSimpleName().toString();
        return classPackageName + "." + className;
    }

}
