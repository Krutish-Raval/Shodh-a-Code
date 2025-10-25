#!/bin/bash

# MongoDB Atlas Connection Test Script
# This script tests the MongoDB Atlas connection

echo "=== MongoDB Atlas Connection Test ==="
echo ""

# Function to test MongoDB Atlas connection
test_mongo_connection() {
    echo "Testing MongoDB Atlas connection..."
    
    # Test using MongoDB Compass connection string format
    CONNECTION_STRING="mongodb+srv://Krutish:learning123@cluster0.s9ri8.mongodb.net/shodh_contest_db?retryWrites=true&w=majority"
    
    echo "Connection String: $CONNECTION_STRING"
    echo ""
    
    # Check if mongosh is available for testing
    if command -v mongosh &> /dev/null; then
        echo "Testing with mongosh..."
        mongosh "$CONNECTION_STRING" --eval "db.runCommand('ping')" --quiet
        if [ $? -eq 0 ]; then
            echo "✅ MongoDB Atlas connection successful!"
        else
            echo "❌ MongoDB Atlas connection failed!"
        fi
    else
        echo "⚠️  mongosh not found. Install MongoDB Shell to test connection."
        echo "   Download from: https://www.mongodb.com/try/download/shell"
    fi
}

# Function to check network connectivity
test_network_connectivity() {
    echo "Testing network connectivity to MongoDB Atlas..."
    
    # Test DNS resolution
    if nslookup cluster0.s9ri8.mongodb.net &> /dev/null; then
        echo "✅ DNS resolution successful"
    else
        echo "❌ DNS resolution failed"
    fi
    
    # Test port connectivity (MongoDB Atlas uses port 27017)
    if timeout 5 bash -c "</dev/tcp/cluster0.s9ri8.mongodb.net/27017" 2>/dev/null; then
        echo "✅ Port 27017 is accessible"
    else
        echo "⚠️  Port 27017 test failed (this might be normal due to Atlas security)"
    fi
}

# Function to check IP whitelist
check_ip_whitelist() {
    echo ""
    echo "=== IP Whitelist Check ==="
    echo "Your current public IP:"
    curl -s ifconfig.me || curl -s ipinfo.io/ip || echo "Unable to determine IP"
    echo ""
    echo "Please ensure this IP is whitelisted in MongoDB Atlas:"
    echo "1. Go to MongoDB Atlas Dashboard"
    echo "2. Navigate to Network Access"
    echo "3. Add your IP address to the whitelist"
    echo "4. Or add 0.0.0.0/0 for testing (NOT recommended for production)"
}

# Main execution
echo "Starting MongoDB Atlas connection tests..."
echo ""

test_network_connectivity
echo ""
test_mongo_connection
echo ""
check_ip_whitelist

echo ""
echo "=== Test Complete ==="
echo ""
echo "If connection tests fail:"
echo "1. Check your internet connection"
echo "2. Verify IP whitelist in MongoDB Atlas"
echo "3. Check username/password credentials"
echo "4. Ensure database user has proper permissions"
echo ""
echo "For more help, visit: https://docs.atlas.mongodb.com/"
