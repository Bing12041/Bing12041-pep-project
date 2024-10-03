package Service;

import java.util.List;
import Service.AccountService;
import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private AccountService accountService;
    private AccountDAO accountDAO;
    private MessageDAO messageDAO;

    public MessageService(){
        accountService = new AccountService();
        accountDAO = new AccountDAO();
        messageDAO = new MessageDAO();
    }

    public MessageService(AccountDAO accountDAO, MessageDAO messageDAO){
        this.accountDAO = accountDAO;
        this.messageDAO = messageDAO;
    }

    public Message postMessage(Message message){
        if(message.getMessage_text().isEmpty()){
            return null;
        }
        if(accountService.getAccountByID(message.getPosted_by()) == null){
            return null;
        }
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId){
        return messageDAO.getMessageByID(messageId);
    }

    public Message deleteMessage(int messageId){
        Message messageToDelete = messageDAO.getMessageByID(messageId);
        if(messageToDelete == null){
            return null;
        }

        messageDAO.deleteMessage(messageId);
        return messageToDelete;
    }

    public Message updatMessage(int messageid, String newMessageText){
        Message existingMessage = messageDAO.getMessageByID(messageid);
        if(newMessageText == null || newMessageText.length() > 255 || existingMessage == null || newMessageText.isEmpty()){
            return null;
        }
        return messageDAO.updateMessage(messageid, newMessageText);
    }

    public List<Message> getMessageByUser(int accountid){
        return messageDAO.getMessageByAccountId(accountid);
    }

}
