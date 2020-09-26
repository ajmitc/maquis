package maquis.view;

import javax.swing.*;
import java.awt.*;

public class PopupUtil {
    public static void popupNotification(Component parent, String title, String message){
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
        //JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public static int popupQuestion(Component parent, String title, String message, String[] options){
        return JOptionPane.showOptionDialog(parent, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    }

    public static boolean popupConfirm(Component parent, String title, String message){
        int choice = popupQuestion(parent, title, message, new String[]{"No", "Yes"});
        return choice == 1;
    }

    private PopupUtil(){}
}
