import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseFeeder {

    public static void main(String[] args){
        // Path to the circles.bin file
        int[] data = DatabaseFeeder.loadFromFile("circles.bin");
        DatabaseFeeder.feedDB(data);
    }
    private static void feedDB(int[] data) {
        // SQL statement for inserting records into the circles table
        String sql = "INSERT INTO circles (x, y, r, g, b) VALUES (?, ?, ?, ?, ?)";
        
        // Database URL - adjust path to your dbRes folder
        String dbUrl = "jdbc:h2:./dbRes/circles";
        
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Process each encoded value from the data array
            for (int val : data) {
                // Decode the value to get coordinates and color byte
                int[] res = PositionAndColor.decode(val);
                
                // Convert color byte to Color object
                Color c = PositionAndColor.byteToColor(res[2]);
                
                // Set the parameters in the PreparedStatement
                ps.setInt(1, res[0]);          // x coordinate
                ps.setInt(2, res[1]);          // y coordinate
                ps.setInt(3, c.getRed());      // red component
                ps.setInt(4, c.getGreen());    // green component
                ps.setInt(5, c.getBlue());     // blue component
                
                // Add the statement to the batch
                ps.addBatch();
            }
            
            // Execute all statements in the batch
            ps.executeBatch();
            System.out.println("Successfully inserted " + data.length + " circles into the database.");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static int[] loadFromFile(String absolutePath) {
        int[] circles = null;
        try (
            FileChannel channel = FileChannel.open(
                Paths.get(absolutePath),
                StandardOpenOption.READ
            )
        ) {
            long fileSize = channel.size();
            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);

            channel.read(buffer);
            buffer.flip();

            int circlesCount = buffer.getInt();
            circles = new int[circlesCount];

            for (int i = 0; i < circlesCount; i++)
                circles[i] = buffer.getInt();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return circles;
    }

}
