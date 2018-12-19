package com.reidwmulkey.vim;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) {
    // System.out.println(new Vim("hello world").execute("ia").toString());
    // System.out.println(new Vim("hello world").execute("lll^ia").toString());
    // System.out.println(new Vim("hello world").execute("lia"));
    // System.out.println(new Vim("hello world").execute("llllllllllllllllllllia"));
    // System.out.println(new Vim("hello world").execute("$ia"));
    // System.out.println(new Vim("hello world").execute("$aa"));
    System.out.println(new Vim(" ").execute("qaihello world`q@a"));
  }
}
