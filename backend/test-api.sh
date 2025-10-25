#!/bin/bash

# Contest API Test Script
# This script demonstrates how to use the Contest API

BASE_URL="http://localhost:8080/api"

echo "=== Contest API Test Script ==="
echo "Make sure the application is running on http://localhost:8080"
echo ""

# Function to make API calls
api_call() {
    local method=$1
    local endpoint=$2
    local data=$3
    
    if [ "$method" = "GET" ]; then
        curl -s "$BASE_URL$endpoint" | jq .
    elif [ "$method" = "POST" ]; then
        curl -s -X POST "$BASE_URL$endpoint" \
             -H "Content-Type: application/json" \
             -d "$data" | jq .
    fi
}

echo "1. Getting all contests (if any)..."
# Note: You'll need to get the actual contest ID from the database or create one
echo "   (This would require knowing the contest ID first)"
echo ""

echo "2. Sample submission request:"
echo "   POST /api/submissions"
echo "   {"
echo "     \"userId\": \"user_id_here\","
echo "     \"problemId\": \"problem_id_here\","
echo "     \"contestId\": \"contest_id_here\","
echo "     \"code\": \"public class Solution { public static void main(String[] args) { System.out.println(\\\"Hello World\\\"); } }\","
echo "     \"language\": \"java\""
echo "   }"
echo ""

echo "3. Sample submission status check:"
echo "   GET /api/submissions/{submissionId}"
echo ""

echo "4. Sample leaderboard request:"
echo "   GET /api/contests/{contestId}/leaderboard"
echo ""

echo "=== Manual Testing Instructions ==="
echo "1. Start the application: mvnw spring-boot:run"
echo "2. Build Docker image: docker build -t code-judge ."
echo "3. Use the sample data created on startup"
echo "4. Test with curl commands or Postman"
echo ""

echo "=== Sample Data Created ==="
echo "- Users: john_doe, jane_smith, alice_wonder"
echo "- Contest: Spring Boot Coding Contest 2024"
echo "- Problems: Two Sum, Palindrome Number, Binary Tree Inorder Traversal"
echo ""

echo "=== Example Java Solution for Two Sum ==="
cat << 'EOF'
import java.util.*;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] numsStr = scanner.nextLine().split(" ");
        int[] nums = new int[numsStr.length];
        for (int i = 0; i < numsStr.length; i++) {
            nums[i] = Integer.parseInt(numsStr[i]);
        }
        int target = scanner.nextInt();
        
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                System.out.println(map.get(complement) + " " + i);
                return;
            }
            map.put(nums[i], i);
        }
    }
}
EOF
