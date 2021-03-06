package greenDao;

import java.util.List;
import greenDao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import com.example.leonardis.greendaoexample.application.GreenDaoExampleApplication;

import java.util.ArrayList;
import java.util.List;
// KEEP INCLUDES END
/**
 * Entity mapped to table CONVERSATION.
 */
public class Conversation {

    private Long id;
    private String Identifier;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ConversationDao myDao;

    private List<Message> messages;
    private List<ConversationGroup> conversationGroupList;

    // KEEP FIELDS - put your custom fields here
    //Note - example of how to implement a 'MANY to MANY' relationship. Must be done manually.
    private List<Contact> groupMembers;
    // KEEP FIELDS END

    public Conversation() {
    }

    public Conversation(Long id) {
        this.id = id;
    }

    public Conversation(Long id, String Identifier) {
        this.id = id;
        this.Identifier = Identifier;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConversationDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public void setIdentifier(String Identifier) {
        this.Identifier = Identifier;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Message> getMessages() {
        if (messages == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            List<Message> messagesNew = targetDao._queryConversation_Messages(id);
            synchronized (this) {
                if(messages == null) {
                    messages = messagesNew;
                }
            }
        }
        return messages;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetMessages() {
        messages = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<ConversationGroup> getConversationGroupList() {
        if (conversationGroupList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ConversationGroupDao targetDao = daoSession.getConversationGroupDao();
            List<ConversationGroup> conversationGroupListNew = targetDao._queryConversation_ConversationGroupList(id);
            synchronized (this) {
                if(conversationGroupList == null) {
                    conversationGroupList = conversationGroupListNew;
                }
            }
        }
        return conversationGroupList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetConversationGroupList() {
        conversationGroupList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    public void setGroupMembers(List<Contact> groupMembers) {
        this.groupMembers = groupMembers;
    }

    /**
     * Get the list of group members. If null, perform a query.
     * @return
     */
    public List<Contact> getGroupMembers() {
         /*
            Note - shows how we do a second sql call to load group members since that is a many to many relationship. Or we can override all the query options in a dao.
         */
        if (groupMembers == null) {

            groupMembers = new ArrayList<>();

            //
            //Query for list of Contacts in this specific conversation
            //
            ContactDao contactDao = GreenDaoExampleApplication.getDatabase().getContactDao();
            try {
                List<ConversationGroup> conversationGroups = this.getConversationGroupList();
                for (ConversationGroup conversationGroup : conversationGroups) {
                    groupMembers.add(contactDao.load(conversationGroup.getContactId()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return groupMembers;
    }
    // KEEP METHODS END

}
