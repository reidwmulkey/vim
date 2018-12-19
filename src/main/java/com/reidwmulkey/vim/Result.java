package com.reidwmulkey.vim;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class Result {

  @Getter
  private Mode mode;
  private int cursorRow;
  private int cursorColumn;

  private boolean willExecuteMacro = false;
  private Character currentMacroCharacter;
  private String currentMacroCommandString;
  private Map<Character, String> macroCharacterToCommandStringMap = new HashMap<>();

  private Map<Integer, String> map = new HashMap<>();

  // constructors

  public Result(String body) {

    mode = Mode.NORMAL;
    cursorRow = 0;
    cursorColumn = 0;

    String[] lines = body.split("\n");
    for (int i = 0; i < lines.length; i++) {
      map.put(i, lines[i]);
    }
  }

  // business methods

  // mode changes

  public void enterInsertMode() {
    mode = Mode.INSERT;
  }

  public void enterInsertModeWithAppend() {
    cursorColumn++;
    mode = Mode.INSERT;
  }

  public void enterNormalMode() {
    mode = Mode.NORMAL;
  }

  // cursor methods

  public void moveCursorToBeginningOfLine() {
    cursorColumn = 0;
  }

  public void moveCursorToEndOfLine() {
    cursorColumn = getCurrentLine().length() - 1;
  }

  public void moveCursorRight() {
    if (cursorColumn + 2 <= getCurrentLine().length()) {
      cursorColumn++;
    }
  }

  // insert methods

  public void insert(char c) {
    String line = getCurrentLine();
    String firstHalfOfLine = line.substring(0, cursorColumn);
    String secondHalfOfLine = line.substring(cursorColumn);
    line = firstHalfOfLine + c + secondHalfOfLine;
    cursorColumn++;
    setCurrentLine(line);
  }

  // recording methods

  public boolean isRecording() {
    return currentMacroCommandString != null;
  }

  private void startRecording() {
    currentMacroCommandString = "";
  }

  private void stopRecording() {
    macroCharacterToCommandStringMap.put(currentMacroCharacter, currentMacroCommandString);
    currentMacroCommandString = null;
    currentMacroCharacter = null;
  }

  public void switchRecording() {
    if (isRecording()) {
      stopRecording();
    } else {
      startRecording();
    }
  }

  public void recordCharacter(Character c) {
    if (currentMacroCommandString.isEmpty()) {
      currentMacroCharacter = c;
    } else {
      currentMacroCommandString += c;
    }
  }

  public void prepareToExecuteMacro() {
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
