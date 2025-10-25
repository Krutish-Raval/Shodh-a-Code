import React from 'react';
import AceEditor from 'react-ace';

// Import Ace Editor modes
import 'ace-builds/src-noconflict/mode-c_cpp';
import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/mode-python';

// Import Ace Editor themes
import 'ace-builds/src-noconflict/theme-github';
import 'ace-builds/src-noconflict/theme-monokai';

// Import Ace Editor extensions
import 'ace-builds/src-noconflict/ext-beautify';
import 'ace-builds/src-noconflict/ext-error_marker';
import 'ace-builds/src-noconflict/ext-keybinding_menu';
import 'ace-builds/src-noconflict/ext-language_tools';
import 'ace-builds/src-noconflict/ext-modelist';
import 'ace-builds/src-noconflict/ext-options';
import 'ace-builds/src-noconflict/ext-prompt';
import 'ace-builds/src-noconflict/ext-rtl';
import 'ace-builds/src-noconflict/ext-searchbox';
import 'ace-builds/src-noconflict/ext-settings_menu';
import 'ace-builds/src-noconflict/ext-split';
import 'ace-builds/src-noconflict/ext-statusbar';
import 'ace-builds/src-noconflict/ext-textarea';
import 'ace-builds/src-noconflict/ext-themelist';
import 'ace-builds/src-noconflict/ext-whitespace';

interface CodeEditorProps {
  code: string;
  language: 'JAVA' | 'PYTHON' | 'CPP' | 'C';
  onChange: (code: string) => void;
  readOnly?: boolean;
}

const CodeEditor: React.FC<CodeEditorProps> = ({ 
  code, 
  language, 
  onChange, 
  readOnly = false 
}) => {
  const getMode = (lang: string) => {
    switch (lang) {
      case 'JAVA':
        return 'java';
      case 'PYTHON':
        return 'python';
      case 'CPP':
      case 'C':
        return 'c_cpp';
      default:
        return 'java';
    }
  };

  const getDefaultCode = (lang: string) => {
    switch (lang) {
      case 'JAVA':
        return `public class Solution {
    public static void main(String[] args) {
        // Your code here
    }
}`;
      case 'PYTHON':
        return `# Your code here
def main():
    pass

if __name__ == "__main__":
    main()`;
      case 'CPP':
        return `#include <iostream>
using namespace std;

int main() {
    // Your code here
    return 0;
}`;
      case 'C':
        return `#include <stdio.h>

int main() {
    // Your code here
    return 0;
}`;
      default:
        return '';
    }
  };

  return (
    <div className="h-full border border-gray-300 rounded-lg overflow-hidden">
      <AceEditor
        mode={getMode(language)}
        theme="github"
        value={code || getDefaultCode(language)}
        onChange={onChange}
        name="code-editor"
        width="100%"
        height="100%"
        fontSize={14}
        showPrintMargin={false}
        showGutter={true}
        highlightActiveLine={true}
        setOptions={{
          enableBasicAutocompletion: true,
          enableLiveAutocompletion: true,
          enableSnippets: true,
          showLineNumbers: true,
          tabSize: 2,
          readOnly: readOnly,
          useWorker: false, // Disable worker to avoid issues
        }}
      />
    </div>
  );
};

export default CodeEditor;
