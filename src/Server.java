import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.border.EmptyBorder;
import java.net.*;
import java.io.*;

public class Server extends JFrame implements ActionListener {
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;
    static JPanel p2;
    JTextField msg;
    static JScrollPane scrollPane;

    public Server() {
        setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBounds(0, 0, 300, 40);
        p1.setBackground(new Color(7, 94, 84));
        p1.setLayout(null);
        add(p1);

        ImageIcon b1 = new ImageIcon(ClassLoader.getSystemResource("Icons/back.png"));
        Image b2 = b1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon b3 = new ImageIcon(b2);
        JLabel b4 = new JLabel(b3);
        b4.setBounds(5, 13, 20, 20);
        p1.add(b4);

        b4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                System.exit(0);
            }
        });

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Icons/Picture1.png"));
        Image i2 = i1.getImage().getScaledInstance(40, 30, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel i4 = new JLabel(i3);
        i4.setBounds(30, 5, 40, 30);
        p1.add(i4);

        JLabel name = new JLabel("Dhana");
        name.setBounds(80, 7, 100, 20);
        name.setFont(new Font("System", Font.BOLD, 15));
        name.setForeground(Color.WHITE);
        p1.add(name);

        JLabel dname = new JLabel("Online");
        dname.setBounds(80, 25, 100, 10);
        dname.setFont(new Font("System", Font.BOLD, 10));
        dname.setForeground(Color.WHITE);
        p1.add(dname);

        ImageIcon v1 = new ImageIcon(ClassLoader.getSystemResource("Icons/video.png"));
        Image v2 = v1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon v3 = new ImageIcon(v2);
        JLabel v4 = new JLabel(v3);
        v4.setBounds(210, 10, 20, 20);
        p1.add(v4);

        ImageIcon c1 = new ImageIcon(ClassLoader.getSystemResource("Icons/phone.png"));
        Image c2 = c1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon c3 = new ImageIcon(c2);
        JLabel c4 = new JLabel(c3);
        c4.setBounds(240, 10, 20, 20);
        p1.add(c4);

        ImageIcon r1 = new ImageIcon(ClassLoader.getSystemResource("Icons/3icon.png"));
        Image r2 = r1.getImage().getScaledInstance(7, 20, Image.SCALE_DEFAULT);
        ImageIcon r3 = new ImageIcon(r2);
        JLabel r4 = new JLabel(r3);
        r4.setBounds(270, 10, 7, 20);
        p1.add(r4);

        p2 = new JPanel();
        //p2.setBounds(0, 40, 285, 380);
        p2.setLayout(new BorderLayout());
        add(p2);

        scrollPane = new JScrollPane(p2);
        scrollPane.setBounds(0, 40, 285, 380);
        add(scrollPane);

        msg = new JTextField();
        msg.setBounds(0, 420, 247, 40);
        msg.setFont(new Font("System", Font.BOLD, 15));
        add(msg);

        ImageIcon s1 = new ImageIcon(ClassLoader.getSystemResource("Icons/send.jpeg"));
        Image s2 = s1.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        ImageIcon s3 = new ImageIcon(s2);
        JLabel s4 = new JLabel(s3);
        s4.setBounds(246, 420, 40, 40);
        add(s4);

        s4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {

                String txt = msg.getText();
                JPanel label = formatLabel(txt);

                JPanel out = new JPanel(new BorderLayout());
                out.add(label, BorderLayout.LINE_END);

                vertical.add(out);
                vertical.add(Box.createVerticalStrut(15));

                p2.add(vertical, BorderLayout.PAGE_START);

                try {
                    dout.writeUTF(txt);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(txt);
                msg.setText("");

                p2.revalidate();
                p2.repaint();

                scrollToBottom();
            }
        });

        setSize(300, 500);
        setLocation(250, 100);
        setVisible(true);
    }

    public static JPanel formatLabel(String txt) {
            JPanel m1 = new JPanel();
            m1.setLayout(new BoxLayout(m1, BoxLayout.Y_AXIS));

            JLabel text = new JLabel("<html><p style = 'width: 100px'>" + txt + "</p></html>");
            text.setFont(new Font("System", Font.PLAIN, 15));

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            JLabel time = new JLabel();
            time.setText(sdf.format(cal.getTime()));
            time.setBorder(new EmptyBorder(0, 0, 3, 3));
            time.setFont(new Font("System", Font.PLAIN, 11));
            m1.add(text);
            m1.add(time);
            m1.setBackground(Color.GRAY);

            return m1;
    }

    public void actionPerformed(ActionEvent ae) {

    }

    public static void main(String[] args) {

        new Server();
        JFrame f = new JFrame();
        try {
            ServerSocket skt = new ServerSocket(6001);
            while (true) {
                Socket s = skt.accept();
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());

                while (true) {
                    String msg = din.readUTF();
                    JPanel pan = formatLabel(msg);

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(pan, BorderLayout.LINE_START);

                    vertical.add(left);
                    vertical.add(Box.createVerticalStrut(15));

                    p2.revalidate();
                    p2.repaint();

                    scrollToBottom();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void scrollToBottom() {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }
}