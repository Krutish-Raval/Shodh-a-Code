#!/bin/bash

# Test Script for Local Execution Fix
# This script tests the fixed local execution service

BASE_URL="http://localhost:8080/api"

echo "=== Testing Local Execution Fix ==="
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

echo "1. Testing with Hello World code (should show WRONG_ANSWER now, not RUNTIME_ERROR)"
echo ""

# Test with Hello World code
echo "Submitting Hello World code..."
SUBMISSION_RESPONSE=$(api_call "POST" "/submissions" '{
  "userId": "68fd1377ad086d9b118e8d62",
  "problemId": "68fd1377ad086d9b118e8d66",
  "contestId": "68fd1377ad086d9b118e8d65",
  "code": "public class Solution {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World\");\n    }\n}",
  "language": "java"
}')

echo "Submission Response: $SUBMISSION_RESPONSE"
SUBMISSION_ID=$(echo $SUBMISSION_RESPONSE | jq -r '.submissionId')

if [ "$SUBMISSION_ID" != "null" ] && [ "$SUBMISSION_ID" != "" ]; then
    echo ""
    echo "2. Waiting 3 seconds for processing..."
    sleep 3
    
    echo ""
    echo "3. Checking submission status..."
    api_call "GET" "/submissions/$SUBMISSION_ID"
else
    echo "Failed to get submission ID"
fi

echo ""
echo "4. Expected Results:"
echo "   ✅ Compilation should succeed (no more 'invalid flag: &&' error)"
echo "   ✅ Execution should succeed"
echo "   ⚠️  Result should be WRONG_ANSWER (not RUNTIME_ERROR)"
echo "   📝 Output: 'Hello World' vs Expected: '0 1'"
echo ""

echo "5. To get ACCEPTED result, use the correct Two Sum solution:"
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
