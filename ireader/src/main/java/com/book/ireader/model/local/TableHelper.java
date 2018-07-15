package com.book.ireader.model.local;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.book.ireader.model.annotation.Check;
import com.book.ireader.model.annotation.Column;
import com.book.ireader.model.annotation.Id;
import com.book.ireader.model.annotation.NotNull;
import com.book.ireader.model.annotation.Relations;
import com.book.ireader.model.annotation.Table;
import com.book.ireader.model.annotation.Unique;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableHelper {

    /**
     * 日志标记.
     */
    private static final String TAG = "TableHelper";

    /**
     * 根据映射的对象创建表.
     *
     * @param <T>    the generic type
     * @param db     数据库对象
     * @param clazzs 对象映射
     */
    public static <T> void createTablesByClasses(SQLiteDatabase db, Class<?>[] clazzs) {
        for (Class<?> clazz : clazzs) {
            createTable(db, clazz);
        }
    }

    /**
     * 根据映射的对象删除表.
     *
     * @param <T>    the generic type
     * @param db     数据库对象
     * @param clazzs 对象映射
     */
    public static <T> void dropTablesByClasses(SQLiteDatabase db, Class<?>[] clazzs) {
        for (Class<?> clazz : clazzs) {
            dropTable(db, clazz);
        }
    }

    /**
     * 创建表.
     *
     * @param <T>   the generic type
     * @param db    根据映射的对象创建表.
     * @param clazz 对象映射
     */
    public static <T> void createTable(SQLiteDatabase db, Class<T> clazz) {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        createTable(db, clazz, tableName);
    }

    public static <T> void createTable(SQLiteDatabase db, Class<T> clazz, String tableName) {
        if (TextUtils.isEmpty(tableName)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        List<Field> allFields = TableHelper.joinFieldsOnlyColumn(clazz);
        for (Field field : allFields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = (Column) field.getAnnotation(Column.class);
            String columnType = "";
            if (column.type().equals(""))
                columnType = getColumnType(field.getType());
            else {
                columnType = column.type();
            }
            sb.append(column.name() + " " + columnType);
            if (column.length() != 0) {
                sb.append("(" + column.length() + ")");
            }
            if ((field.isAnnotationPresent(Id.class))) {
                sb.append(" primary key");
                if (field.getType() == Integer.TYPE || field.getType() == Integer.class) {
                    sb.append(" autoincrement");
                }
            } else {
                //约束操作处理
                if (field.isAnnotationPresent(Unique.class)) {
                    sb.append(" UNIQUE");
                }
                if (field.isAnnotationPresent(NotNull.class)) {
                    sb.append(" NOT NULL");
                }
                Check check = field.getAnnotation(Check.class);
                if (check != null) {
                    String checkStr = check.value();
                    if (checkStr != null) {
                        sb.append(" CHECK(").append(check).append(")");
                    }
                }
            }
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append(")");
        String sql = sb.toString();
        db.execSQL(sql);

    }

    /**
     * 删除表.
     *
     * @param <T>   the generic type
     * @param db    根据映射的对象创建表.
     * @param clazz 对象映射
     */
    public static <T> void dropTable(SQLiteDatabase db, Class<T> clazz) {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        String sql = "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(sql);
    }

    /**
     * 在表中增加一列（INTEGER 默认值0）.
     *
     * @param <T>        the generic type
     * @param db         根据映射的对象创建表.
     * @param clazz      对象映射
     * @param ColumnName 需要增加的列名
     */
    public static <T> void addColumn(SQLiteDatabase db, Class<T> clazz, String ColumnName) {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        String sql = "ALTER TABLE " + tableName + " ADD " + ColumnName + " INTEGER DEFAULT 0";
        db.execSQL(sql);
    }

    public static <T> void addColumnInteger(SQLiteDatabase db, Class<T> clazz, String ColumnName) {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        String sql = "ALTER TABLE " + tableName + " ADD " + ColumnName + " INTEGER DEFAULT -1";
        db.execSQL(sql);
    }

    public static <T> void addColumnString(SQLiteDatabase db, Class<T> clazz, String ColumnName) {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        String sql = "ALTER TABLE " + tableName + " ADD " + ColumnName;
        db.execSQL(sql);
    }

    /**
     * 获取列类型.
     *
     * @param fieldType the field type
     * @return 列类型
     */
    private static String getColumnType(Class<?> fieldType) {
        if (String.class == fieldType) {
            return "TEXT";
        }
        if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
            return "INTEGER";
        }
        if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
            return "BIGINT";
        }
        if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
            return "FLOAT";
        }
        if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
            return "INT";
        }
        if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
            return "DOUBLE";
        }
        if (Blob.class == fieldType) {
            return "BLOB";
        }

        return "TEXT";
    }

    /**
     * 合并Field数组并去重,并实现过滤掉非Column字段,和实现Id放在首字段位置功能.
     *
     * @param fields1 属性数组1
     * @param fields2 属性数组2
     * @return 属性的列表
     */
    public static List<Field> joinFieldsOnlyColumn(Field[] fields1, Field[] fields2) {
        Map<String, Field> map = new LinkedHashMap<String, Field>();
        for (Field field : fields1) {
            // 过滤掉非Column定义的字段
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = (Column) field.getAnnotation(Column.class);
            map.put(column.name(), field);
        }
        for (Field field : fields2) {
            // 过滤掉非Column定义的字段
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = (Column) field.getAnnotation(Column.class);
            if (!map.containsKey(column.name())) {
                map.put(column.name(), field);
            }
        }
        List<Field> list = new ArrayList<Field>();
        for (String key : map.keySet()) {
            Field tempField = map.get(key);
            // 如果是Id则放在首位置.
            if (tempField.isAnnotationPresent(Id.class)) {
                list.add(0, tempField);
            } else {
                list.add(tempField);
            }
        }
        return list;
    }

    public static List<Field> joinFieldsOnlyColumn(Class<?> clz) {
        Map<String, Field> map = new LinkedHashMap<String, Field>();
        while (clz != null && clz != Object.class) {
            for (Field field : clz.getDeclaredFields()) {
                // 过滤掉非Column定义的字段
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }
                Column column = (Column) field.getAnnotation(Column.class);
                String key = column.name();
                if (!map.containsKey(key))
                    map.put(key, field);
            }
            clz = clz.getSuperclass();
        }
        List<Field> list = new ArrayList<Field>();
        for (String key : map.keySet()) {
            Field tempField = map.get(key);
            // 如果是Id则放在首位置.
            if (tempField.isAnnotationPresent(Id.class)) {
                list.add(0, tempField);
            } else {
                list.add(tempField);
            }
        }
        return list;
    }

    /**
     * 合并Field数组并去重.
     *
     * @param fields1 属性数组1
     * @param fields2 属性数组2
     * @return 属性的列表
     */
    public static List<Field> joinFields(Field[] fields1, Field[] fields2) {
        Map<String, Field> map = new LinkedHashMap<String, Field>();
        for (Field field : fields1) {
            // 过滤掉非Column和Relations定义的字段
            if (field.isAnnotationPresent(Column.class)) {
                Column column = (Column) field.getAnnotation(Column.class);
                map.put(column.name(), field);
            } else if (field.isAnnotationPresent(Relations.class)) {
                Relations relations = (Relations) field.getAnnotation(Relations.class);
                map.put(relations.name(), field);
            }

        }
        for (Field field : fields2) {
            // 过滤掉非Column和Relations定义的字段
            if (field.isAnnotationPresent(Column.class)) {
                Column column = (Column) field.getAnnotation(Column.class);
                if (!map.containsKey(column.name())) {
                    map.put(column.name(), field);
                }
            } else if (field.isAnnotationPresent(Relations.class)) {
                Relations relations = (Relations) field.getAnnotation(Relations.class);
                if (!map.containsKey(relations.name())) {
                    map.put(relations.name(), field);
                }
            }
        }
        List<Field> list = new ArrayList<Field>();
        for (String key : map.keySet()) {
            Field tempField = map.get(key);
            // 如果是Id则放在首位置.
            if (tempField.isAnnotationPresent(Id.class)) {
                list.add(0, tempField);
            } else {
                list.add(tempField);
            }
        }
        return list;
    }
}
