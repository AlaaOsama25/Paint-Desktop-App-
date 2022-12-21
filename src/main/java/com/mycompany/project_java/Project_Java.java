/*
MADE BY
MAGDA OSAMA
ALAA OSAMA
*/
package com.mycompany.project_java;

import MyPanel.MyPanel;
import javax.swing.JFrame;

public class Project_Java {

    public static void main(String[] args) {
        JFrame myframe = new JFrame();
        myframe.setTitle("Paint");
        MyPanel mp = new MyPanel();
        myframe.setContentPane(mp);
        myframe.setSize(1500, 1000);
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myframe.setVisible(true);
    }
}
