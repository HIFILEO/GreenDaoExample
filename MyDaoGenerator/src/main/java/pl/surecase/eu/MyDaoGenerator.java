package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * This module was built using the open source project from here:
 * http://blog.surecase.eu/using-greendao-with-android-studio-ide/
 */
public class MyDaoGenerator {
    private static Schema schema;

    public static void main(String args[]) throws Exception {

        /*
        Note - Run this module to create all the sql tables and DAOs
        http://blog.surecase.eu/using-greendao-with-android-studio-ide/
         */
        /*
        Why greenDaos does not support delete cascade
        http://stackoverflow.com/questions/2973982/orm-on-android-sqlite-and-database-scheme
        Delete-cascade is a dangerous thing, and thus unsupported by greenDAO. The safer way is to delete the entities bottom-up inside a transaction.
         */

        //The package is the session along with the database schema number. The package location and the schema version number. The schema number is the database version.
        schema = new Schema(1, "greenDao");

        //allows for custom code in the the entities of generated classes. Not DAOs.
        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();

        //
        //Create the Contact Object/Table
        //
        Entity contact = schema.addEntity("Contact");

        //add _id column and set as primary key
        contact.addIdProperty();

        //add GUID and make unique
        contact.addStringProperty("GUID").unique();

        //add first name
        contact.addStringProperty("FirstName");

        //add last name
        contact.addStringProperty("LastName");

        //
        //Create the Contact Object/Table
        //
        Entity conversation = schema.addEntity("Conversation");

        //add _id column and set as primary key
        conversation.addIdProperty();

        //add Identifier - conversationId
        conversation.addStringProperty("Identifier").unique();

        //
        //Create the Message Object/Table
        //
        Entity message = schema.addEntity("Message");

        //add _id column and set as primary key
        message.addIdProperty();

        //add Identifier - conversationId
        message.addStringProperty("Identifier").unique();

        //create a ONE to ONE relationship. Each message should have one sender.
        Property contactIdProperty = message.addLongProperty("SenderId").getProperty();
        message.addToOne(contact, contactIdProperty);


        //
        //Add One To Many Relationships
        //
        //Create a ONE to MANY relationship. Each conversation consists of many messages.
        Property conversationIdProperty = message.addLongProperty("ConversationId").notNull().getProperty();
        ToMany conversationToMessagesToMany = conversation.addToMany(message, conversationIdProperty);
        conversationToMessagesToMany.setName("messages"); // Optional


        //
        //Add Many to Many relationship
        //
        Entity conversationGroup = schema.addEntity("ConversationGroup");
        conversationGroup.addIdProperty();

        Property conversationIdPropertyBlah = conversationGroup.addLongProperty("ConversationId").notNull().getProperty();
        ToMany conversationToMany = conversation.addToMany(conversationGroup, conversationIdPropertyBlah);




        Property contactIdPropertyBlah = conversationGroup.addLongProperty("ContactId").notNull().getProperty();
        ToMany contactToMany = contact.addToMany(conversationGroup, contactIdPropertyBlah);

        //
        //Generate all the tables
        //
        new DaoGenerator().generateAll(schema, args[0]);
    }

}
