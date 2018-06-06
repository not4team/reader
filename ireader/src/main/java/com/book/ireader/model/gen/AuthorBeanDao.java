package com.book.ireader.model.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.book.ireader.model.bean.AuthorBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "AUTHOR_BEAN".
*/
public class AuthorBeanDao extends AbstractDao<AuthorBean, String> {

    public static final String TABLENAME = "AUTHOR_BEAN";

    /**
     * Properties of entity AuthorBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, String.class, "_id", true, "_ID");
        public final static Property Avatar = new Property(1, String.class, "avatar", false, "AVATAR");
        public final static Property Nickname = new Property(2, String.class, "nickname", false, "NICKNAME");
        public final static Property ActivityAvatar = new Property(3, String.class, "activityAvatar", false, "ACTIVITY_AVATAR");
        public final static Property Type = new Property(4, String.class, "type", false, "TYPE");
        public final static Property Lv = new Property(5, int.class, "lv", false, "LV");
        public final static Property Gender = new Property(6, String.class, "gender", false, "GENDER");
    }


    public AuthorBeanDao(DaoConfig config) {
        super(config);
    }
    
    public AuthorBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"AUTHOR_BEAN\" (" + //
                "\"_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: _id
                "\"AVATAR\" TEXT," + // 1: avatar
                "\"NICKNAME\" TEXT," + // 2: nickname
                "\"ACTIVITY_AVATAR\" TEXT," + // 3: activityAvatar
                "\"TYPE\" TEXT," + // 4: type
                "\"LV\" INTEGER NOT NULL ," + // 5: lv
                "\"GENDER\" TEXT);"); // 6: gender
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"AUTHOR_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AuthorBean entity) {
        stmt.clearBindings();
 
        String _id = entity.get_id();
        if (_id != null) {
            stmt.bindString(1, _id);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(2, avatar);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(3, nickname);
        }
 
        String activityAvatar = entity.getActivityAvatar();
        if (activityAvatar != null) {
            stmt.bindString(4, activityAvatar);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(5, type);
        }
        stmt.bindLong(6, entity.getLv());
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(7, gender);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AuthorBean entity) {
        stmt.clearBindings();
 
        String _id = entity.get_id();
        if (_id != null) {
            stmt.bindString(1, _id);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(2, avatar);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(3, nickname);
        }
 
        String activityAvatar = entity.getActivityAvatar();
        if (activityAvatar != null) {
            stmt.bindString(4, activityAvatar);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(5, type);
        }
        stmt.bindLong(6, entity.getLv());
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(7, gender);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public AuthorBean readEntity(Cursor cursor, int offset) {
        AuthorBean entity = new AuthorBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // avatar
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nickname
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // activityAvatar
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // type
            cursor.getInt(offset + 5), // lv
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // gender
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AuthorBean entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setAvatar(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNickname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setActivityAvatar(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLv(cursor.getInt(offset + 5));
        entity.setGender(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final String updateKeyAfterInsert(AuthorBean entity, long rowId) {
        return entity.get_id();
    }
    
    @Override
    public String getKey(AuthorBean entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AuthorBean entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
