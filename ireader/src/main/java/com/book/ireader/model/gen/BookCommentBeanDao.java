package com.book.ireader.model.gen;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.book.ireader.model.bean.AuthorBean;

import com.book.ireader.model.bean.BookCommentBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BOOK_COMMENT_BEAN".
*/
public class BookCommentBeanDao extends AbstractDao<BookCommentBean, String> {

    public static final String TABLENAME = "BOOK_COMMENT_BEAN";

    /**
     * Properties of entity BookCommentBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, String.class, "_id", true, "_ID");
        public final static Property AuthorId = new Property(1, String.class, "authorId", false, "AUTHOR_ID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Type = new Property(3, String.class, "type", false, "TYPE");
        public final static Property LikeCount = new Property(4, int.class, "likeCount", false, "LIKE_COUNT");
        public final static Property Block = new Property(5, String.class, "block", false, "BLOCK");
        public final static Property HaveImage = new Property(6, boolean.class, "haveImage", false, "HAVE_IMAGE");
        public final static Property State = new Property(7, String.class, "state", false, "STATE");
        public final static Property Updated = new Property(8, String.class, "updated", false, "UPDATED");
        public final static Property Created = new Property(9, String.class, "created", false, "CREATED");
        public final static Property CommentCount = new Property(10, int.class, "commentCount", false, "COMMENT_COUNT");
        public final static Property VoteCount = new Property(11, int.class, "voteCount", false, "VOTE_COUNT");
    }

    private DaoSession daoSession;


    public BookCommentBeanDao(DaoConfig config) {
        super(config);
    }
    
    public BookCommentBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOOK_COMMENT_BEAN\" (" + //
                "\"_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: _id
                "\"AUTHOR_ID\" TEXT," + // 1: authorId
                "\"TITLE\" TEXT," + // 2: title
                "\"TYPE\" TEXT," + // 3: type
                "\"LIKE_COUNT\" INTEGER NOT NULL ," + // 4: likeCount
                "\"BLOCK\" TEXT," + // 5: block
                "\"HAVE_IMAGE\" INTEGER NOT NULL ," + // 6: haveImage
                "\"STATE\" TEXT," + // 7: state
                "\"UPDATED\" TEXT," + // 8: updated
                "\"CREATED\" TEXT," + // 9: created
                "\"COMMENT_COUNT\" INTEGER NOT NULL ," + // 10: commentCount
                "\"VOTE_COUNT\" INTEGER NOT NULL );"); // 11: voteCount
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_BOOK_COMMENT_BEAN_BLOCK ON \"BOOK_COMMENT_BEAN\"" +
                " (\"BLOCK\" ASC);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_BOOK_COMMENT_BEAN_TYPE ON \"BOOK_COMMENT_BEAN\"" +
                " (\"TYPE\" ASC);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_BOOK_COMMENT_BEAN_STATE ON \"BOOK_COMMENT_BEAN\"" +
                " (\"STATE\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOOK_COMMENT_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BookCommentBean entity) {
        stmt.clearBindings();
 
        String _id = entity.get_id();
        if (_id != null) {
            stmt.bindString(1, _id);
        }
 
        String authorId = entity.getAuthorId();
        if (authorId != null) {
            stmt.bindString(2, authorId);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(4, type);
        }
        stmt.bindLong(5, entity.getLikeCount());
 
        String block = entity.getBlock();
        if (block != null) {
            stmt.bindString(6, block);
        }
        stmt.bindLong(7, entity.getHaveImage() ? 1L: 0L);
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(8, state);
        }
 
        String updated = entity.getUpdated();
        if (updated != null) {
            stmt.bindString(9, updated);
        }
 
        String created = entity.getCreated();
        if (created != null) {
            stmt.bindString(10, created);
        }
        stmt.bindLong(11, entity.getCommentCount());
        stmt.bindLong(12, entity.getVoteCount());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BookCommentBean entity) {
        stmt.clearBindings();
 
        String _id = entity.get_id();
        if (_id != null) {
            stmt.bindString(1, _id);
        }
 
        String authorId = entity.getAuthorId();
        if (authorId != null) {
            stmt.bindString(2, authorId);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(4, type);
        }
        stmt.bindLong(5, entity.getLikeCount());
 
        String block = entity.getBlock();
        if (block != null) {
            stmt.bindString(6, block);
        }
        stmt.bindLong(7, entity.getHaveImage() ? 1L: 0L);
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(8, state);
        }
 
        String updated = entity.getUpdated();
        if (updated != null) {
            stmt.bindString(9, updated);
        }
 
        String created = entity.getCreated();
        if (created != null) {
            stmt.bindString(10, created);
        }
        stmt.bindLong(11, entity.getCommentCount());
        stmt.bindLong(12, entity.getVoteCount());
    }

    @Override
    protected final void attachEntity(BookCommentBean entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public BookCommentBean readEntity(Cursor cursor, int offset) {
        BookCommentBean entity = new BookCommentBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // authorId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // type
            cursor.getInt(offset + 4), // likeCount
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // block
            cursor.getShort(offset + 6) != 0, // haveImage
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // state
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // updated
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // created
            cursor.getInt(offset + 10), // commentCount
            cursor.getInt(offset + 11) // voteCount
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BookCommentBean entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setAuthorId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLikeCount(cursor.getInt(offset + 4));
        entity.setBlock(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setHaveImage(cursor.getShort(offset + 6) != 0);
        entity.setState(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUpdated(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCreated(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCommentCount(cursor.getInt(offset + 10));
        entity.setVoteCount(cursor.getInt(offset + 11));
     }
    
    @Override
    protected final String updateKeyAfterInsert(BookCommentBean entity, long rowId) {
        return entity.get_id();
    }
    
    @Override
    public String getKey(BookCommentBean entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BookCommentBean entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getAuthorBeanDao().getAllColumns());
            builder.append(" FROM BOOK_COMMENT_BEAN T");
            builder.append(" LEFT JOIN AUTHOR_BEAN T0 ON T.\"AUTHOR_ID\"=T0.\"_ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected BookCommentBean loadCurrentDeep(Cursor cursor, boolean lock) {
        BookCommentBean entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        AuthorBean author = loadCurrentOther(daoSession.getAuthorBeanDao(), cursor, offset);
        entity.setAuthor(author);

        return entity;    
    }

    public BookCommentBean loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<BookCommentBean> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<BookCommentBean> list = new ArrayList<BookCommentBean>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<BookCommentBean> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<BookCommentBean> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
