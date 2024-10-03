package Controller;

import static org.mockito.Mockito.lenient;

import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("register", this::userRegister);
        app.post("login", this::userLogin);
        app.post("messages", this::createMessage);
        app.get("messages", this::retrieveAllMessage);
        app.get("/messages/{message_id}", this::retrieveMessageByID);
        app.delete("/messages/{message_id}", this::deleteMessageByID);
        app.patch("/messages/{message_id}", this::updateMessageByID);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByID);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */

    // ## 1: Our API should be able to process new User registrations.
    private void userRegister(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account createdAccount = accountService.register(account);
        if (createdAccount != null) {
            context.json(createdAccount);
        } else {
            context.status(400);
        }
    }

    // ## 2: Our API should be able to process User logins.
    private void userLogin(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account loggedAccount = accountService.login(account.getUsername(), account.getPassword());
        if (loggedAccount != null) {
            context.json(loggedAccount);
        } else {
            context.status(401);
        }
    }

    // ## 3: Our API should be able to process the creation of new messages.
    private void createMessage(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Message createdMessage = messageService.postMessage(message);
        if(createdMessage != null){
            context.json(createdMessage);
        } else{
            context.status(400);
        }
    }

    // ## 4: Our API should be able to retrieve all messages.
    private void retrieveAllMessage(Context context) {
        List<Message> retrievedMessages = messageService.getAllMessages();

        context.json(retrievedMessages);
    }

    // ## 5: Our API should be able to retrieve a message by its ID.
    private void retrieveMessageByID(Context context) {
        int messageid = Integer.parseInt(context.pathParam("message_id"));
        Message retrievedMessage = messageService.getMessageById(messageid);

        if (retrievedMessage == null) {
            context.status(200).result("");
        } else {
            context.json(retrievedMessage);
        }
    }

    // ## 6: Our API should be able to delete a message identified by a message ID.
    private void deleteMessageByID(Context context) {
        int messageid = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageid);

        if(deletedMessage != null){
            context.json(deletedMessage);
        } else{
            context.status(200).json("");
        }
    }

    // ## 7: Our API should be able to update a message text identified by a message ID.
    private void updateMessageByID(Context context) {
        int messageid = Integer.parseInt(context.pathParam("message_id"));
        Message message = context.bodyAsClass(Message.class);
        Message updatedMessage = messageService.updatMessage(messageid, message.getMessage_text());

        if (updatedMessage != null) {
            context.json(updatedMessage);
        } else {
            context.status(400);
        }

    }

    // ## 8: Our API should be able to retrieve all messages written by a particular
    // user.
    private void getAllMessagesByID(Context context) {
        int userid = Integer.parseInt(context.pathParam("account_id"));
        List<Message> retrivedMessage = messageService.getMessageByUser(userid);

        context.json(retrivedMessage);

    }
}