package mnsx;

import javax.swing.*;

/**
 * @Description 关于功能弹出窗口
 * @Author Mnsx_x
 * @Email xx1527030652@gmail.com
 * @Version 1.8.1
 * @Date 2021--08--30   10:23
 */
public class About extends JDialog {
    private JTextArea jta = null;

    public static void main(String[] args){
        About about = new About();
    }

    About(){
        jta = new JTextArea();
        this.setTitle("关于: ");
        this.setSize(400, 150);
        this.setLocation(500, 300);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.add(jta);
        jta.setText("Author: Mnsx_x\nEmail: xx1527030652@gmail.com\nVersion: 1.8.1\nThanks for using!");
        jta.setEditable(false);
        jta.setFont(new java.awt.Font("黑体",1,20));
    }
}
