package dangnhap;

import connect.ConnectDatabase;

import javax.swing.*;
import javax.swing.border.Border;

import QuanLyDanCu.src.giaodien.GiaoDienChung;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DangNhap extends ConnectDatabase {
    private static JPanel topPanel;
    private static JPanel mainPanel;
    private static JFrame frame;
    private static JLabel taiKhoanLabel;
    private static JTextField taiKhoanField;
    private static JLabel matKhauLabel;
    private static JTextField matKhauField;
    private static JPanel dangKyPanel;
    private static JButton quenMatKhauButton;
    private static JButton dangNhapButton;
    public DangNhap() {
        frame = new JFrame("Đăng nhập");
        frame.setSize(1000, 578);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.getContentPane().setLayout(new BorderLayout());

        topPanel = new JPanel();
        //topPanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() / 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove any border/margin
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JButton backButton = new JButton();
        ImageIcon icon = new ImageIcon("src/icon/goBackIcon.png");
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(35, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        backButton.setIcon(scaledIcon);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel dangNhapLabel = new JLabel("Đăng nhập");
        dangNhapLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        dangNhapLabel.setHorizontalAlignment(SwingConstants.CENTER); // Align label to the center
        dangNhapLabel.setForeground(Color.decode("#38B6FF"));
        topPanel.add(dangNhapLabel, BorderLayout.CENTER);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        //mainPanel.setBackground(Color.decode("#FBFCFC"));

        Font newFont = new Font("Arial", Font.PLAIN, 18);
        taiKhoanLabel = new JLabel("Tài khoản:");
        taiKhoanField = new JTextField(20);
        matKhauLabel = new JLabel("Mật khẩu:");
        matKhauField = new JTextField(20);
        quenMatKhauButton = new JButton("Quên mật khẩu ?");
        dangNhapButton = new JButton("Đăng nhập");

        taiKhoanLabel.setFont(newFont);
        matKhauLabel.setFont(newFont);
        quenMatKhauButton.setFont(new Font("Arial", Font.PLAIN, 15));
        dangNhapButton.setFont(new Font("Arial", Font.BOLD, 20));
        dangNhapButton.setBackground(Color.decode("#004AAD"));
        dangNhapButton.setOpaque(true);
        quenMatKhauButton.setBorder(BorderFactory.createEmptyBorder());


        dangKyPanel = new JPanel();
        dangKyPanel.setLayout(new BoxLayout(dangKyPanel, BoxLayout.X_AXIS));
        dangKyPanel.setBorder(BorderFactory.createEmptyBorder());
        dangKyPanel.setOpaque(false);
        JLabel askLabel = new JLabel("Chưa có tài khoản ? ");
        askLabel.setFont(newFont);
        JButton dangKyButton = new JButton("Đăng ký");
        dangKyButton.setFont(newFont);
        dangKyButton.setForeground(Color.decode("#38B6FF"));
        dangKyButton.setBorder(BorderFactory.createEmptyBorder());
        dangKyButton.setBorderPainted(false);

        dangKyPanel.add(askLabel);
        dangKyPanel.add(dangKyButton);

        mainPanel.add(taiKhoanLabel);
        mainPanel.add(taiKhoanField);
        mainPanel.add(matKhauLabel);
        mainPanel.add(matKhauField);
        mainPanel.add(quenMatKhauButton);
        mainPanel.add(dangNhapButton);
        mainPanel.add(dangKyPanel);

        taiKhoanLabel.setBounds(350, 30, 300, 50);
        taiKhoanField.setBounds(350, 80, 300, 50);
        matKhauLabel.setBounds(350, 130, 300, 50);
        matKhauField.setBounds(350, 180, 300, 50);
        quenMatKhauButton.setBounds(260, 230, 300, 20);
        dangNhapButton.setBounds(350, 280, 300, 50);
        dangKyPanel.setBounds(350, 330, 300, 50);

        dangNhapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(taiKhoanField.getText().equals("") || matKhauField.getText().equals("")) {
                    JOptionPane.showMessageDialog(mainPanel, "Đăng nhập không thành công");
                } else if(validateSignIn(taiKhoanField.getText(), matKhauField.getText()) == null) {
                    JOptionPane.showMessageDialog(mainPanel, "Tài khoản hoặc mật khẩu chưa chính xác");
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Đăng nhập thành công");
                    new GiaoDienChung();
                }
            }
        });
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    public String validateSignIn(String username, String password) {
        String role = null;
        try {
            Connection connection = getConnectDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM dangnhap WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                role = resultSet.getString("vaitro");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    public static String getTenNguoiDung() {
        return taiKhoanField.getText(); 
    }
    public static void main(String[] args) {
        new DangNhap();
    }
}
