# UTP8.3 - Binary Files to H2 Database

This project demonstrates how to transfer previously encoded graphical circle data from a binary file into an H2 database using Java.

## Project Structure

```
UTP8.3/
├── build.gradle.kts           # Gradle build configuration with H2 dependency
├── settings.gradle.kts         # Gradle settings
├── src/main/java/
│   ├── DatabaseFeeder.java     # Main class that loads and feeds data to DB
│   ├── PositionAndColor.java   # Utility for encoding/decoding circle data
│   ├── DatabaseInitializer.java # Helper to initialize the database
│   ├── VerifyDatabase.java     # Helper to verify database contents
│   └── CircleDataGenerator.java # Helper to generate sample circle data
├── dbRes/                      # Directory for H2 database files
├── circles.bin                 # Binary file containing encoded circle data
└── init-db.sql                 # SQL script to create the circles table
```

## Features

- **Gradle Build System**: Configured with H2 database dependency (version 2.2.224)
- **Binary Data Decoding**: Reads encoded circle data from a binary file
- **Data Transformation**: 
  - Decodes position coordinates (x, y) from packed integers
  - Expands compressed 3-3-2 color format to full RGB values
- **Batch Database Insert**: Efficiently inserts all circles using JDBC batch operations
- **Resource Management**: Uses try-with-resources for safe database connection handling

## Database Schema

The `circles` table has the following structure:

```sql
CREATE TABLE circles (
    x INT,    -- X coordinate
    y INT,    -- Y coordinate
    r INT,    -- Red component (0-255)
    g INT,    -- Green component (0-255)
    b INT     -- Blue component (0-255)
);
```

## Building the Project

```bash
# Build the project
./gradlew build
```

## Running the Application

### Step 1: Generate Sample Data (Optional)

If you don't have a `circles.bin` file, generate one:

```bash
java -cp "build/classes/java/main:~/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.2.224/*/h2-2.2.224.jar" CircleDataGenerator
```

### Step 2: Initialize the Database

```bash
java -cp "build/classes/java/main:~/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.2.224/*/h2-2.2.224.jar" DatabaseInitializer
```

### Step 3: Feed Data to Database

```bash
java -cp "build/classes/java/main:~/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.2.224/*/h2-2.2.224.jar" DatabaseFeeder
```

### Step 4: Verify the Results

```bash
java -cp "build/classes/java/main:~/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.2.224/*/h2-2.2.224.jar" VerifyDatabase
```

## How It Works

### Data Encoding

Circle data is encoded using `PositionAndColor.encode()`:
- Lower 12 bits: X coordinate (0-4095)
- Next 12 bits: Y coordinate (0-4095)
- Upper 8 bits: Color in 3-3-2 format (3 bits red, 3 bits green, 2 bits blue)

### Data Decoding

The `DatabaseFeeder.feedDB()` method:
1. Opens a connection to the H2 database
2. Prepares an INSERT statement with 5 parameters (x, y, r, g, b)
3. For each encoded integer:
   - Calls `PositionAndColor.decode()` to extract x, y, and color byte
   - Calls `PositionAndColor.byteToColor()` to expand the color to full RGB
   - Sets the PreparedStatement parameters
   - Adds to batch
4. Executes the batch insert
5. Handles any SQL exceptions

## Assignment Requirements

This project fulfills the following requirements:

✅ Gradle project with H2 dependency (2.2.224)  
✅ Database directory `dbRes` for H2 database files  
✅ Table `circles` with columns: x, y, r, g, b  
✅ `DatabaseFeeder.java` with implemented `feedDB()` method  
✅ Binary file reading using FileChannel and ByteBuffer  
✅ Data decoding using `PositionAndColor.decode()`  
✅ Color conversion using `PositionAndColor.byteToColor()`  
✅ PreparedStatement with batch operations  
✅ Try-with-resources for connection management  
✅ SQLException handling with stack trace printing  

## Technologies Used

- Java 11
- Gradle 8.5
- H2 Database 2.2.224
- JDBC for database connectivity
- NIO FileChannel for binary file I/O

## Example Output

```
Successfully inserted 5 circles into the database.

Circles in database:
--------------------
  X  |  Y  |  R  |  G  |  B  
-----------------------------
 100 |  150 | 255 |   0 |   0
 200 |  250 |   0 | 255 |   0
 300 |  350 |   0 |   0 | 255
 400 |  450 | 255 | 255 |   0
 500 |  550 | 255 |   0 | 255
-----------------------------
Total circles: 5
```
