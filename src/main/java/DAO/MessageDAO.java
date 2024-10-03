package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.*;

public class MessageDAO {

    public Message createMessage(Message message) {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";

        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if (pkeyResultSet.next()) {
                    int generatedMessageId = pkeyResultSet.getInt(1);
                    return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(),
                            message.getTime_posted_epoch());
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message;";

        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message getMessageByID(int messageId) {
        String sql = "SELECT * FROM message WHERE message_id = ?;";

        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int messageid = rs.getInt("message_id");
                int postedby = rs.getInt("posted_by");
                String messagetext = rs.getString("message_text");
                long timepostedepoch = rs.getLong("time_posted_epoch");

                return new Message(messageid, postedby, messagetext, timepostedepoch);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getMessageByAccountId(int accountId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?;";

        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int messageid = rs.getInt("message_id");
                int postedby = rs.getInt("posted_by");
                String messagetext = rs.getString("message_text");
                long timepostedepoch = rs.getLong("time_posted_epoch");

                Message message = new Message(messageid, postedby, messagetext, timepostedepoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public void deleteMessage(int messageId) {
        String sql = "SELECT * FROM message WHERE message_id = ?;";
        String sqlDelete = "DELETE FROM message WHERE message_id = ?;";
    
        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
    
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete);
                deleteStatement.setInt(1, messageId);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Message updateMessage(int messageId, String newMessageString) {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        String sqlUpdate = "UPDATE message SET message_text = ? WHERE message_id = ?;";

        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("message_id");
                int postedby = rs.getInt("posted_by");
                long timepostedepoch = rs.getLong("time_posted_epoch");

                PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate);
                updateStatement.setString(1, newMessageString);
                updateStatement.setInt(2, messageId);

                updateStatement.executeUpdate();
                return new Message(id, postedby, newMessageString, timepostedepoch);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

}
