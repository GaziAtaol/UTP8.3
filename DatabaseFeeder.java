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

public
    class DatabaseFeeder {

    public static void main(String[] args){
//TODO: place absolute path to file from classes 8 and 9 (circles.bin)
        int[] data = DatabaseFeeder.loadFromFile("...");
        DatabaseFeeder.feedDB(data);
    }
//TODO: implement feedDB based on tutorial instructions
    private static void feedDB(int[] data) {
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
