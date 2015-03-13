package greenDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.leonardis.greendaoexample.application.GreenDaoExampleApplication;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greenDao.Conversation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CONVERSATION.
*/
public class ConversationDao extends AbstractDao<Conversation, Long> {

    public static final String TABLENAME = "CONVERSATION";

    /**
     * Properties of entity Conversation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Identifier = new Property(1, String.class, "Identifier", false, "IDENTIFIER");
    };

    private DaoSession daoSession;


    public ConversationDao(DaoConfig config) {
        super(config);
    }
    
    public ConversationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CONVERSATION' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'IDENTIFIER' TEXT UNIQUE );"); // 1: Identifier
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CONVERSATION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Conversation entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String Identifier = entity.getIdentifier();
        if (Identifier != null) {
            stmt.bindString(2, Identifier);
        }
    }

    @Override
    protected void attachEntity(Conversation entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Conversation readEntity(Cursor cursor, int offset) {
        Conversation entity = new Conversation( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // Identifier
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Conversation entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIdentifier(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Conversation entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Conversation entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }

    // KEEP METHODS - put your custom methods here
    //Note - this does not actually stay. If you regenerate your code you'll lose this. BAD
    @Override
    public long insert(Conversation conversation) {
        /*
         * Note - In a many to many relationship, you must enforce it using a 3rd table. Therefore, someone at some point needs to fill in that table. You can do it manually, or you can override the
          * insert and other methods for a conversation. Overriding methods allows you to enforce it behind the scenes. Here is that example of how every conversation has a group of contacts that we can update in the
          * ConversationGroup table.
         */

        //first insert the conversation into sql
        long id = super.insert(conversation);

        //For every contact in the group, create the link to contact table.
        ConversationGroupDao conversationGroupDao = GreenDaoExampleApplication.getDatabase().getConversationGroupDao();
        for (Contact contact : conversation.getGroupMembers()) {
            ConversationGroup conversationGroup = new ConversationGroup();
            conversationGroup.setContactId(contact.getId());
            conversationGroup.setConversationId(conversation.getId());
            conversationGroupDao.insert(conversationGroup);
        }

        return id;
    }
    // KEEP METHODS END
}
