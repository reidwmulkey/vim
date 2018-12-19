package com.reidwmulkey.vim;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class Vim {

  @Getter
  private Mode mode;
  private int cursorRow;
  private int cursorColumn;

  private boolean willExecuteMacro;
  private Character currentMacroCharacter;
  private Character currentCharacter;
  private String currentMacroCommandString;
  private Map<Character, String> macroCharacterToCommandStringMap = new HashMap<>();

  private Map<Integer, String> map = new HashMap<>();

  // constructors

  public Vim(String body) {

    mode = Mode.NORMAL;
    cursorRow = 0;
    cursorColumn = 0;
    willExecuteMacro = false;

    String[] lines = body.split("\n");
    for (int i = 0; i < lines.length; i++) {
      map.put(i, lines[i]);
    }
  }

  // main method

  public Vim execute(String commandString) {

    for (char c : commandString.toCharArray()) {

      this.currentCharacter = c;
      if (willExecuteMacro) {
        String macro = macroCharacterToCommandStringMap.get(c);
        if (macro == null) {
          throw new RuntimeException("Tried to execute macro (" + c + ") but that macro has not been recorded.");
        }
        execute(macro);
      }

      switch (mode) {
        case NORMAL:
          switch (c) {
            // cursor methods
            case '^':
              moveCursorToBeginningOfLine();
              break;
            case '$':
              moveCursorToEndOfLine();
              break;
            case 'l':
              moveCursorRight();
              break;
            // mode change methods
            case 'i':
              enterInsertMode();
              break;
            case 'a':
              enterInsertModeWithAppend();
              break;
            // record methods
            case 'q':
              switchRecording();
              continue;
            case '@':
              prepareToExecuteMacro();
              break;
            // default
            default:
              break;
          }
          break;
        case INSERT:
          switch (c) {
            case '`':
              enterNormalMode();
              break;
            default:
              insert(c);
              break;
          }
        default:
          break;
      }

      if (isRecording() && !startedRecording()) {
        recordCharacter(c);
      }
    }

    return this;
  }

  // business methods

  // mode changes

  private void enterInsertMode() {
    mode = Mode.INSERT;
  }

  private void enterInsertModeWithAppend() {
    cursorColumn++;
    mode = Mode.INSERT;
  }

  private void enterNormalMode() {
    mode = Mode.NORMAL;
  }

  // cursor methods

  private void moveCursorToBeginningOfLine() {
    cursorColumn = 0;
  }

  private void moveCursorToEndOfLine() {
    cursorColumn = getCurrentLine().length() - 1;
  }

  private void moveCursorRight() {
    if (cursorColumn + 2 <= getCurrentLine().length()) {
      cursorColumn++;
    }
  }

  // insert methods

  private void insert(char c) {
    String line = getCurrentLine();
    String firstHalfOfLine = line.substring(0, cursorColumn);
    String secondHalfOfLine = line.substring(cursorColumn);
    line = firstHalfOfLine + c + secondHalfOfLine;
    cursorColumn++;
    setCurrentLine(line);
  }

  // recording methods

  private boolean isRecording() {
    return currentMacroCommandString != null;
  }

  private boolean startedRecording() {
    return isRecording() && currentCharacter == 'q';
  }

  private void startRecording() {
    currentMacroCommandString = "";
  }

  private void stopRecording() {
    macroCharacterToCommandStringMap.put(currentMacroCharacter, currentMacroCommandString);
    currentMacroCommandString = null;
    currentMacroCharacter = null;
  }

  private void switchRecording() {
    if (isRecording()) {
      stopRecording();
    } else {
      startRecording();
    }
  }

  private void recordCharacter(Character c) {
    if (currentMacroCommandString.isEmpty()) {
      currentMacroCharacter = c;
    } else {
      currentMacroCommandString += c;
    }
  }

  private void prepareToExecuteMacro() {
    willExecuteMacro = true;
  }

  // private utility methods

  private String getCurrentLine() {
    return map.get(cursorRow);
  }

  private void setCurrentLine(String line) {
    map.put(cursorRow, line);
  }

  // to string to return result

  public String toString() {
    String result = "";

    for (String line : map.values()) {
      result += line + "\n";
    }

    return result;
  }

}
