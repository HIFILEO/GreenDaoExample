# GreenDaoExample

This project is to demonstrate how to use GreenDao in order to create a list of conversations and messages within those conversations. Although the visual components are simple, the database being built by GreenDao is what the real project demonstrates.

Great tutorial on setting up the auto generator. Easy to drop into Android SDK.
http://blog.surecase.eu/using-greendao-with-android-studio-ide/

You can add conversations and messages. Delete a conversation and it will cascade the deletions of all messages within that conversation (Had to code manually). The following tables are created:
1.) Conversation table - Represents a single conversation. Has multiple messages and multiple contacts
2.) Message table - represents individual messages. Linked to conversation table
3.) Contact table - represents an individual contact. Used to demonstrate many to many relationship with the Conversation table.
4.) ConversationGroup table - represents the linkage between the conversation table and contact table. Required for Many to Many relationships.

The menu option on the conversation activity allows you to export the database that was created using ormlite. This way you can see how the relationships are set up and how data is stored in the database. Use ADB monitor to remove the database file once export. Files are exported
into a folder with the same name as the application.

GreenDao has the following benefits (IMHO)
- Code Generation speeds up inserts
- Code generation easy to learn
- Entities can be customized with proper comments inserted (look at Conversation.java)
- Table layouts match the same layout as set out by the generated code. In other words, extract the database and the _id primary key will be in the first column
- Caches one to many relationships for faster loading

GreenDao has the following drawbacks (IMHO)
- Need to keep a module in your app just for generating DAOs
- Not much documentation. Especially on setting up the DatabaseHelper.
- Does not support cascade deletes. Not even an option. Was done as per design of their architect who prefers bottom up deletes.
- Unable to customize DAOs. Since there was little documentation on this, I had to stick custom code in the ConversationDao to support Many to Many. That code will always get removed
when generating DAO classes. Not a problem in OrmLite to customize DAOs.
