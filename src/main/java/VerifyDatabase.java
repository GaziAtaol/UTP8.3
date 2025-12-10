import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class VerifyDatabase {
    public static void main(String[] args) {
        String dbUrl = "jdbc:h2:./dbRes/circles";
        
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            
            // Query all circles from the database
            String query = "SELECT * FROM circles";
            ResultSet rs = stmt.executeQuery(query);
            
            System.out.println("Circles in database:");
            System.out.println("--------------------");
            System.out.println("  X  |  Y  |  R  |  G  |  B  ");
            System.out.println("-----------------------------");
            
            int count = 0;
            while (rs.next()) {
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int r = rs.getInt("r");
                int g = rs.getInt("g");
                int b = rs.getInt("b");
                
                System.out.printf("%4d | %4d | %3d | %3d | %3d%n", x, y, r, g, b);
                count++;
            }
            
            System.out.println("-----------------------------");
            System.out.println("Total circles: " + count);
            
        } catch (SQLException ex) {
            System.err.println("Error querying database:");
            ex.printStackTrace();
        }
    }
}
