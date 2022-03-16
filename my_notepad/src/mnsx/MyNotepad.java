package mnsx;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static mnsx.Util.getNowTime;

/**
 * @Description 模仿windows系统实现的简单记事本
 * @Author Mnsx_x
 * @Email xx1527030652@gmail.com
 * @Version 1.8.1
 * @Date 2021--08--29   13:37
 */
public class MyNotepad extends JFrame implements ActionListener {
    //---------------------------------------------------------------------------------------------------定义所需要的组件
    //用作显示文本信息
    JTextArea jta = null;

    //用作提供滚动条
    JScrollPane jsp = null;

    //用作管理撤销
    UndoManager um = null;

    //用作提供菜单栏
    JMenuBar jmb = null;

    //用作底部状态框
    JToolBar jtb = null;

    //用作提供菜单栏的选项
    JMenu jm_file = null;
    JMenu jm_edit = null;
    JMenu jm_format = null;
    JMenu jm_help = null;

    //底部状态框图标
    Icon statusBar = null;

    //编码类型
    String codeStyle = "UTF-8";

    //右击弹出菜单
    JPopupMenu popupMenu = null;

    //用作提供菜单选项下的按键
    //文件下的选项
    JMenuItem jmi_open = null;
    JMenuItem jmi_saveAs = null;
    JMenuItem jmi_exit = null;
    JMenuItem jmi_save = null;
    JMenuItem jmi_newFile = null;

    //编辑下的选项
    JMenuItem jmi_cut = null;
    JMenuItem jmi_copy = null;
    JMenuItem jmi_paste = null;
    JMenuItem jmi_clear = null;
    JMenuItem jmi_undo = null;

    //格式下的选项
    JMenuItem jmi_color = null;
    JMenuItem jmi_font = null;

    //帮助下的选项
    JMenuItem jmi_about = null;

    //右击弹出菜单选项
    JMenuItem popCut = null;
    JMenuItem popCopy = null;
    JMenuItem popPaste = null;

    //用来获取当前文件的位置
    File curFile = null;

    //状态栏标签
    JLabel label = null;
    JLabel Time = null;
    JLabel CodeStyle = null;

    //----------------------------------------------------------------------------------------------------------main方法
    //执行main后自动启动记事本程序
    public static void main(String[] args){
        MyNotepad mnp = new MyNotepad();
    }
    
    //----------------------------------------------------------------------------------------------------------构造函数
    public MyNotepad() {
        //新建文本显示板块
        jta = new JTextArea();
        //对文本显示板块添加滚动条
        jsp = new JScrollPane(jta);
        //添加菜单栏
        jmb = new JMenuBar();
        //添加撤销管理的对象
        um = new UndoManager();

        //----------------------------------------------------------------------------------------------------菜单栏选项
        jm_file = new JMenu("文件(F)");
        jm_file.setMnemonic('F');
        jm_edit = new JMenu("编辑(E)");
        jm_edit.setMnemonic('E');
        jm_format = new JMenu("格式(M)");
        jm_format.setMnemonic('M');
        jm_help = new JMenu("帮助(H)");
        jm_help.setMnemonic('H');

        //--------------------------------------------------------------------------------------------------文件下拉选项
        //添加打开功能
        jmi_open = new JMenuItem("打开(O)");
        //注册监听（相当于添加按钮功能）
        jmi_open.addActionListener(this);
        jmi_open.setMnemonic('O');
        //设置功能的指令
        jmi_open.setActionCommand("open");
        jmi_open.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));

        //添加另存为功能
        jmi_saveAs = new JMenuItem("另保存");
        //注册监听（相当于添加按钮功能）
        jmi_saveAs.addActionListener(this);
        //设置功能的指令
        jmi_saveAs.setActionCommand("save as");
        jmi_saveAs.setAccelerator(KeyStroke.getKeyStroke("ctrl shift S"));

        //添加退出功能
        jmi_exit = new JMenuItem("退出(X)");
        //注册监听（相当于添加按钮功能）
        jmi_exit.addActionListener(this);
        //设置功能的指令
        jmi_exit.setActionCommand("exit");
        jmi_exit.setMnemonic('X');

        //添加保存功能
        jmi_save = new JMenuItem("保存(S)");
        jmi_save.addActionListener(this);
        jmi_save.setActionCommand("save");
        jmi_save.setMnemonic('S');
        jmi_save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));

        //添加新建功能
        jmi_newFile = new JMenuItem("新建(N)");
        jmi_newFile.addActionListener(this);
        jmi_newFile.setActionCommand("new file");
        jmi_newFile.setMnemonic('N');
        jmi_newFile.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));

        //--------------------------------------------------------------------------------------------------编辑下拉选项
        //添加剪切功能
        jmi_cut = new JMenuItem("剪切(T)");
        jmi_cut.setMnemonic('T');
        jmi_cut.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
        jmi_cut.setEnabled(false);

        //添加复制功能
        jmi_copy = new JMenuItem("复制(C)");
        jmi_copy.setMnemonic('C');
        jmi_copy.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        jmi_copy.setEnabled(false);

        //添加粘贴功能
        jmi_paste = new JMenuItem("粘贴(V)");
        jmi_paste.setMnemonic('V');
        jmi_paste.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));

        //添加清除功能
        jmi_clear = new JMenuItem("清除");
        jmi_clear.setEnabled(false);

        //添加撤回功能
        jmi_undo = new JMenuItem("撤回(Z)");
        jmi_undo.setMnemonic('Z');
        jmi_undo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));

        //--------------------------------------------------------------------------------------------------格式下拉选项
        //添加更改字体颜色功能
        jmi_color = new JMenuItem("字体颜色");
        //添加更改字体功能
        jmi_font = new JMenuItem("字体选择");
//        jmi_font.setEnabled(false);

        //--------------------------------------------------------------------------------------------------帮助下拉选项
        //添加关于功能
        jmi_about = new JMenuItem("关于");
//        jmi_about.setEnabled(false);

        //------------------------------------------------------------------------------------------------------右击菜单
        popupMenu = new JPopupMenu();
        popCut = new JMenuItem("剪切");
        popCopy = new JMenuItem("复制");
        popPaste = new JMenuItem("粘贴");
        popupMenu.add(popCut);
        popupMenu.add(popCopy);
        popupMenu.add(popPaste);

        //--------------------------------------------------------------------------------------------------------状态栏
        jtb = new JToolBar();
        jtb.setFloatable(false); //设置是否可以悬浮
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        label = new JLabel("当前字数:0",SwingConstants.CENTER);
        Time = new JLabel("日期: "+ sdf.format(new Date()));
        CodeStyle = new JLabel("编码: "+ codeStyle);
        jtb.add(label);
        jtb.addSeparator(new Dimension(180,5));
        jtb.add(Time);
        jtb.addSeparator(new Dimension(180,5));
        jtb.add(CodeStyle);

        //----------------------------------------------------------------------------------------------------------添加
        //将选项添加到菜单栏
        jmb.add(jm_file);
        jmb.add(jm_edit);
        jmb.add(jm_format);
        jmb.add(jm_help);
        jm_edit.addSeparator();

        //将功能添加到选项中
        jm_file.add(jmi_newFile);
        jm_file.add(jmi_open);
        jm_file.addSeparator();
        jm_file.add(jmi_save);
        jm_file.add(jmi_saveAs);
        jm_file.addSeparator();
        jm_file.add(jmi_exit);
        jm_edit.add(jmi_cut);
        jm_edit.add(jmi_copy);
        jm_edit.add(jmi_paste);
        jm_edit.addSeparator();
        jm_edit.add(jmi_clear);
        jm_edit.addSeparator();
        jm_edit.add(jmi_undo);
        jm_format.add(jmi_color);
        jm_format.add(jmi_font);
        jm_help.add(jmi_about);

        //将组件放到JFrame中
        this.add(jtb, BorderLayout.SOUTH);
        this.add(jsp);
        this.setJMenuBar(jmb);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700, 450);
        this.setLocation(300, 200);
        this.setVisible(true);
        this.setTitle("Power by Mnsx_x");
        registerListener();
    }

    public void registerListener(){
        //鼠标监控文本框
        jta.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                maybeShowPopup(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }
        });

        //文本框数据改变监控
        jta.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                isItemsAvalible();// 一旦文本有改变就设置各按钮的可用性
                changeTextLengthStatus();// 实时显示文本字数
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                isItemsAvalible();
                changeTextLengthStatus();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                isItemsAvalible();
                changeTextLengthStatus();
            }
        });
        //事件监控2.0（lambda表达式）
        jmi_cut.addActionListener(e -> jta.cut());
        jmi_copy.addActionListener(e -> jta.copy());
        jmi_paste.addActionListener(e -> jta.paste());
        jmi_clear.addActionListener(e -> clearAll());
        jmi_undo.addActionListener(e -> undo());
        jmi_font.addActionListener(e -> setTextFont());
        jmi_color.addActionListener(e -> setTextColor());
        jmi_about.addActionListener(e -> about());
        popCopy.addActionListener(e -> jta.copy());
        popCut.addActionListener(e -> jta.cut());
        popPaste.addActionListener(e -> jta.paste());
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        //事件监控1.0
        if(event.getActionCommand().equals("open")){
            //打开功能
            open();
            System.out.println(getNowTime() + ": " + curFile.getName() + " in " + curFile.getAbsolutePath() + " has been opened.");
        } else if(event.getActionCommand().equals("save as")) {
            //另存为功能
            saveAs();
            System.out.println(getNowTime() + ": " + "File has been saved in " + curFile.getAbsolutePath() + ".");
        } else if(event.getActionCommand().equals("exit")) {
            //退出功能
            System.out.println(getNowTime() + ": " + "Exit success.");
            if(curFile != null) {
                save();
            }
            this.dispose();
        } else if(event.getActionCommand().equals("save")) {
            //保存功能
            if(curFile != null) {
                save();
            }else{
                saveAs();
            }
            System.out.println(getNowTime() + ": " + curFile + " has been saved in " + curFile.getAbsolutePath() + ".");
        } else if(event.getActionCommand().equals("new file")){
            //新建功能
            newFile();
            System.out.println(getNowTime() + ": " + "New file successfully.");
        }
    }

    //打开功能
    public void open(){
        //添加文件选择组件
        JFileChooser jfc = new JFileChooser();
        //设置窗口名称
        jfc.setDialogTitle("请选择文件： ");
        //设置打开方式
        jfc.showOpenDialog(null);
        jfc.setVisible(true);

        //获取选择文件的路径
        String filepath = jfc.getSelectedFile().getAbsolutePath();
        curFile = new File(filepath);
        this.setTitle(filepath);
        //通过IO读取文件中的文字（这里选择使用字符流）
        FileReader fr = null;
        BufferedReader br = null;
        try{
            fr = new FileReader(filepath);
            br = new BufferedReader(fr);

            String str = "";
            String text = "";
            while((str = br.readLine()) != null){
                text += str + "\r\n";
            }
            //将文本数据放入组件中
            jta.setText(text);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //另存为功能
    public void saveAs(){
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("另存为: ");
        jfc.showSaveDialog(null);
        jfc.setVisible(true);

        String filepath = jfc.getSelectedFile().getAbsolutePath();

        //判断curFile是否为空
        if(curFile == null){
            curFile = new File(filepath);
            this.setTitle(filepath);
        }

        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            fw = new FileWriter(filepath);
            bw = new BufferedWriter(fw);
            bw.write(this.jta.getText());
            bw.flush();
            bw.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //保存功能
    public void save(){
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            fw = new FileWriter(curFile);
            bw = new BufferedWriter(fw);
            bw.write(this.jta.getText());
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //新建功能
    public void newFile(){
        if(!this.jta.getText().equals("")){
            int res = JOptionPane.showConfirmDialog(null, "当前信息是否需要保存: ", "Info", JOptionPane.YES_NO_OPTION);
            if(res == JOptionPane.YES_OPTION){
                if(curFile != null) {
                    save();
                }else{
                    saveAs();
                }
                this.jta.setText("");
                this.setTitle("untitled");
                curFile = null;
            }else{
                save();
                this.jta.setText("");
                this.setTitle("untitled");
                curFile = null;
            }
        } else {
            this.jta.setText("");
            this.setTitle("untitled");
            curFile = null;
        }
    }

    //清除功能
    public void clearAll(){
        jta.setText("");
    }

    //撤回功能
    public void undo(){
        if(um.canUndo()){
            um.undo();
        }
    }

    //颜色选择功能
    private void setTextColor(){
        Color color = JColorChooser.showDialog(null, "**请选择字体颜色**",
                Color.WHITE);
        jta.setForeground(color);
    }

    //文字选择功能——借鉴的他人作品
    private void setTextFont(){                               // 选择字体触发函数
        try{
            GraphicsEnvironment ge = GraphicsEnvironment
                    .getLocalGraphicsEnvironment();
            // 获取系统字体
            JList<String> fontNames = new JList<>(ge.getAvailableFontFamilyNames());
            int response = JOptionPane.showConfirmDialog(null,
                    new JScrollPane(fontNames), "请选择字体 ( 默认: Courier Prime )",
                    JOptionPane.OK_CANCEL_OPTION);
            Object selectedFont = fontNames.getSelectedValue();
            if (response == JOptionPane.OK_OPTION && selectedFont != null)
                jta.setFont(new Font(fontNames.getSelectedValue(), Font.PLAIN, 20));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //关于功能
    private void about(){
        new About();
    }

    // 监听鼠标
    private void maybeShowPopup(MouseEvent e){
        if(e.isPopupTrigger()){
            popupMenu.show(e.getComponent(),e.getX(),e.getY());
        }
    }

    //文本框监听
    private void changeTextLengthStatus(){                     // 文本监听
        String content = jta.getText().trim();
        label.setText("当前字数:"+content.length());
    }

    //更改功能可使用状态
    private void isItemsAvalible() {                                   // 监视文本区并设置各功能项是否可用
        String content = jta.getText();
        if (content.equals("")) {
            jmi_cut.setEnabled(false);
            jmi_clear.setEnabled(false);
            jmi_copy.setEnabled(false);
        } else {
            jmi_cut.setEnabled(true);
            jmi_clear.setEnabled(true);
            jmi_copy.setEnabled(true);
        }
    }
}
