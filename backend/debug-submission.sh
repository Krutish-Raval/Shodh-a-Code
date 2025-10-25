#!/bin/bash

# Debug Script for Contest API
# This script helps debug submission issues

BASE_URL="http://localhost:8080/api"

echo "=== Contest API Debug Script ==="
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

echo "1. Checking test cases for problem ID: 68fd1377ad086d9b118e8d66"
api_call "GET" "/debug/problem/68fd1377ad086d9b118e8d66/testcases"
echo ""

echo "2. The issue is likely one of these:"
echo "   a) Your code outputs 'Hello World' but test case expects different output"
echo "   b) Docker execution environment issue"
echo "   c) Compilation error in the container"
echo ""

echo "3. Expected outputs for different problems:"
echo "   - Two Sum: Should output indices like '0 1'"
echo "   - Palindrome Number: Should output 'true' or 'false'"
echo "   - Binary Tree: Should output traversal like '1 3 2'"
echo ""

echo "4. To fix the issue, your code should:"
echo "   - Read input from stdin"
echo "   - Process the input according to problem requirements"
echo "   - Output the expected result"
echo ""

echo "5. Example correct solution for Two Sum problem:"
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

echo ""
echo "6. Test with correct solution:"
echo "   Replace your 'Hello World' code with the above solution"
echo "   and submit again to see ACCEPTED result"
