package org.example;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CircleDataGenerator {
    public static void main(String[] args) {
        // Create sample circle data
        int[] circles = new int[5];
        
        // Encode some sample circles
        circles[0] = PositionAndColor.encode(100, 150, new Color(255, 0, 0));    // Red circle
        circles[1] = PositionAndColor.encode(200, 250, new Color(0, 255, 0));    // Green circle
        circles[2] = PositionAndColor.encode(300, 350, new Color(0, 0, 255));    // Blue circle
        circles[3] = PositionAndColor.encode(400, 450, new Color(255, 255, 0));  // Yellow circle
        circles[4] = PositionAndColor.encode(500, 550, new Color(255, 0, 255));  // Magenta circle
        
        // Save to file
        String filePath = "circles.bin";
        try (FileChannel channel = FileChannel.open(
                Paths.get(filePath),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            ByteBuffer buffer = ByteBuffer.allocate(4 + circles.length * 4);
            buffer.putInt(circles.length);
            
            for (int circle : circles) {
                buffer.putInt(circle);
            }
            
            buffer.flip();
            channel.write(buffer);
            System.out.println("Created " + filePath + " with " + circles.length + " circles");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
