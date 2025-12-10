#!/bin/bash
# Integration test script for H2 Database Circle Feeder

set -e  # Exit on error

echo "=========================================="
echo "H2 Database Integration Test"
echo "=========================================="
echo ""

# Find the H2 jar
H2_JAR=$(find ~/.gradle/caches -name "h2-2.2.224.jar" 2>/dev/null | head -1)
if [ -z "$H2_JAR" ]; then
    echo "ERROR: H2 jar not found. Please run './gradlew build' first."
    exit 1
fi

CLASSPATH="build/classes/java/main:$H2_JAR"

echo "Step 1: Clean previous database..."
rm -rf dbRes/*.db 2>/dev/null || true
echo "✓ Database cleaned"
echo ""

echo "Step 2: Initialize database and create table..."
java -cp "$CLASSPATH" DatabaseInitializer
echo "✓ Database initialized"
echo ""

echo "Step 3: Generate sample circle data..."
java -cp "$CLASSPATH" CircleDataGenerator
echo "✓ Sample data generated"
echo ""

echo "Step 4: Feed data to database..."
java -cp "$CLASSPATH" DatabaseFeeder
echo "✓ Data fed to database"
echo ""

echo "Step 5: Verify data in database..."
java -cp "$CLASSPATH" VerifyDatabase
echo ""

echo "=========================================="
echo "Integration test completed successfully!"
echo "=========================================="
