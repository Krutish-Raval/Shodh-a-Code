import React from 'react';

interface CodeEditorProps {
  code: string;
  language: 'JAVA' | 'PYTHON' | 'CPP' | 'C';
  onChange: (code: string) => void;
  readOnly?: boolean;
}

const SimpleCodeEditor: React.FC<CodeEditorProps> = ({ 
  code, 
  language, 
  onChange, 
  readOnly = false 
}) => {
  const getDefaultCode = (lang: string) => {
    switch (lang) {
      case 'JAVA':
        return `public class Solution {
    public static void main(String[] args) {
        // Your code here
        System.out.println("Hello World!");
    }
}`;
      case 'PYTHON':
        return `# Your code here
def main():
    print("Hello World!")

if __name__ == "__main__":
    main()`;
      case 'CPP':
        return `#include <iostream>
using namespace std;

int main() {
    // Your code here
    cout << "Hello World!" << endl;
    return 0;
}`;
      case 'C':
        return `#include <stdio.h>

int main() {
    // Your code here
    printf("Hello World!\\n");
    return 0;
}`;
      default:
        return '';
    }
  };

  const getLanguageIcon = (lang: string) => {
    switch (lang) {
      case 'JAVA':
        return '☕';
      case 'PYTHON':
        return '🐍';
      case 'CPP':
        return '⚡';
      case 'C':
        return '🔧';
      default:
        return '📝';
    }
  };

  return (
    <div className="h-full border-2 border-gray-200 rounded-xl overflow-hidden bg-white shadow-inner">
      <div className="h-full flex flex-col">
        {/* Editor Header */}
        <div className="bg-gradient-to-r from-gray-50 to-gray-100 px-4 py-3 border-b border-gray-200 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <span className="text-lg">{getLanguageIcon(language)}</span>
            <span className="text-sm font-semibold text-gray-700">{language}</span>
            <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
          </div>
          <div className="flex items-center space-x-2 text-xs text-gray-500">
            <span>Lines: {(code || getDefaultCode(language)).split('\n').length}</span>
            <span>•</span>
            <span>Chars: {(code || getDefaultCode(language)).length}</span>
          </div>
        </div>
        
        {/* Editor Content */}
        <div className="flex-1 relative">
          <textarea
            value={code || getDefaultCode(language)}
            onChange={(e) => onChange(e.target.value)}
            readOnly={readOnly}
            className="w-full h-full p-4 font-mono text-sm resize-none focus:outline-none bg-white text-gray-800 leading-relaxed"
            style={{
              fontFamily: '"Fira Code", "Monaco", "Menlo", "Ubuntu Mono", monospace',
              lineHeight: '1.6',
              tabSize: 2,
              background: 'linear-gradient(to right, #f8fafc 0%, #f8fafc 100%)',
            }}
            placeholder={`Enter your ${language} code here...`}
            spellCheck={false}
          />
          
          {/* Line Numbers */}
          <div className="absolute left-0 top-0 bottom-0 w-12 bg-gray-50 border-r border-gray-200 flex flex-col text-xs text-gray-400 font-mono">
            {(code || getDefaultCode(language)).split('\n').map((_, index) => (
              <div key={index} className="h-6 flex items-center justify-center">
                {index + 1}
              </div>
            ))}
          </div>
        </div>
        
        {/* Editor Footer */}
        <div className="bg-gray-50 px-4 py-2 border-t border-gray-200 flex items-center justify-between text-xs text-gray-500">
          <div className="flex items-center space-x-4">
            <span>UTF-8</span>
            <span>•</span>
            <span>2 spaces</span>
            <span>•</span>
            <span>{language}</span>
          </div>
          <div className="flex items-center space-x-2">
            <div className="w-2 h-2 bg-green-500 rounded-full"></div>
            <span>Ready</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SimpleCodeEditor;
