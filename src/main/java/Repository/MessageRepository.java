package Repository;

import Model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository {

    private Connection connection;

    public MessageRepository(Connection connection) {
        this.connection = connection;
    }

    public Message createMessage(Message message) {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, message.getPosted_by());
            pstmt.setString(2, message.getMessage_text());
            pstmt.setLong(3, message.getTime_posted_epoch());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
              
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        message.setMessage_id(rs.getInt(1)); // Set the generated message_id
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }


    public List<Message> getMessagesByUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                    );
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }


    public Message updateMessage(int messageId, Message updatedMessage) {
        String sql = "UPDATE message SET message_text = ?, time_posted_epoch = ? WHERE message_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, updatedMessage.getMessage_text());
            pstmt.setLong(2, updatedMessage.getTime_posted_epoch());
            pstmt.setInt(3, messageId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return updatedMessage;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  
    }

   
    public boolean deleteMessage(int messageId) {
        String sql = "DELETE FROM message WHERE message_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, messageId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
