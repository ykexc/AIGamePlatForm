import ace from 'ace-builds';

import modeJsonUrl from 'ace-builds/src-noconflict/mode-json?url';

ace.config.setModuleUrl('ace/mode/json', modeJsonUrl);
import snippetsJsonUrl from 'ace-builds/src-noconflict/snippets/json?url';

ace.config.setModuleUrl('ace/snippets/json', snippetsJsonUrl);


import modeHtmlUrl from 'ace-builds/src-noconflict/mode-html?url';

ace.config.setModuleUrl('ace/mode/html', modeHtmlUrl);

import modeYamlUrl from 'ace-builds/src-noconflict/mode-yaml?url';

ace.config.setModuleUrl('ace/mode/yaml', modeYamlUrl);

import themeGithubUrl from 'ace-builds/src-noconflict/theme-github?url';

ace.config.setModuleUrl('ace/theme/github', themeGithubUrl);

import themeChromeUrl from 'ace-builds/src-noconflict/theme-chrome?url';

ace.config.setModuleUrl('ace/theme/chrome', themeChromeUrl);

import themeMonokaiUrl from 'ace-builds/src-noconflict/theme-monokai?url';

ace.config.setModuleUrl('ace/theme/monokai', themeMonokaiUrl);

import workerBaseUrl from 'ace-builds/src-noconflict/worker-base?url';

ace.config.setModuleUrl('ace/mode/base', workerBaseUrl);

import workerJsonUrl from 'ace-builds/src-noconflict/worker-json?url';

ace.config.setModuleUrl('ace/mode/json_worker', workerJsonUrl);

import workerJavascriptUrl from 'ace-builds/src-noconflict/worker-javascript?url';

ace.config.setModuleUrl('ace/mode/javascript_worker', workerJavascriptUrl);

import workerHtmlUrl from 'ace-builds/src-noconflict/worker-html?url';

ace.config.setModuleUrl('ace/mode/html_worker', workerHtmlUrl);

import workerYamlUrl from 'ace-builds/src-noconflict/worker-yaml?url';

ace.config.setModuleUrl('ace/mode/yaml_worker', workerYamlUrl);
import snippetsHtmlUrl from 'ace-builds/src-noconflict/snippets/html?url';

ace.config.setModuleUrl('ace/snippets/html', snippetsHtmlUrl);
//JavaScript配置
import snippetsJsUrl from 'ace-builds/src-noconflict/snippets/javascript?url';

ace.config.setModuleUrl('ace/snippets/javascript', snippetsJsUrl);
import modeJavascriptUrl from 'ace-builds/src-noconflict/mode-javascript?url';

ace.config.setModuleUrl('ace/mode/javascript', modeJavascriptUrl);
import snippetsYamlUrl from 'ace-builds/src-noconflict/snippets/yaml?url';

ace.config.setModuleUrl('ace/snippets/javascript', snippetsYamlUrl);


//Java配置
import modeJavaUrl from 'ace-builds/src-noconflict/mode-java?url'

ace.config.setModuleUrl('ace/mode/java', modeJavaUrl);
import snippetsJavaUrl from 'ace-builds/src-noconflict/snippets/java?url';

ace.config.setModuleUrl('ace/snippets/java', snippetsJavaUrl);
//C++配置
import modeCCppUrl from 'ace-builds/src-noconflict/mode-c_cpp?url'

ace.config.setModuleUrl('ace/mode/c_cpp', modeCCppUrl)
import snippetsCCppUrl from 'ace-builds/src-noconflict/snippets/c_cpp?url'

ace.config.setModuleUrl('ace/snippets/c_cpp', snippetsCCppUrl)
//编辑器配置
import keybindingEmacsUrl from 'ace-builds/src-noconflict/keybinding-emacs?url'
ace.config.setModuleUrl('ace/mode/emacs', keybindingEmacsUrl)
import keybindingSublimeUrl from 'ace-builds/src-noconflict/keybinding-sublime?url'
ace.config.setModuleUrl('ace/mode/sublime', keybindingSublimeUrl)
import keybindingVimUrl from 'ace-builds/src-noconflict/keybinding-vim?url'
ace.config.setModuleUrl('vim', keybindingVimUrl)
import keybindingVscode from 'ace-builds/src-noconflict/keybinding-vscode?url'
ace.config.setModuleUrl('vscode', keybindingVscode)

import 'ace-builds/src-noconflict/ext-language_tools';
ace.require("ace/ext/language_tools");
