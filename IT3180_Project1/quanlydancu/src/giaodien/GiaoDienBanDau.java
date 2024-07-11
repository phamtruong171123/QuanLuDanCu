package giaodien;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class GiaoDienBanDau extends QuanLyDanCu.src.giaodien.GiaoDienChung {

    public GiaoDienBanDau() {
        super();

        JButton dangNhap = new JButton("ĐĂNG NHẬP") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(horizontalBar.getWidth() / 6, horizontalBar.getHeight() / 2);
            }
        };
        dangNhap.setBackground(Color.decode("#38B6FF"));
        dangNhap.setForeground(Color.WHITE);
        dangNhap.setFont(new Font("Arial", Font.PLAIN, 20));
        dangNhap.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Thiết lập border
        dangNhap.setFocusPainted(false); // Loại bỏ viền focus

//        dangNhap.setBorder(BorderFactory.createLineBorder(Color.decode("#004AAD"), 1, true));
        JButton dangKy = new JButton("ĐĂNG KÝ") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(horizontalBar.getWidth() / 6, horizontalBar.getHeight() / 2);
            }
        };

        dangKy.setBackground(Color.decode("#38B6FF"));
        dangKy.setForeground(Color.WHITE);
        dangKy.setFont(new Font("Arial", Font.PLAIN, 20));
        dangKy.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Thiết lập border
        dangKy.setFocusPainted(false); // Loại bỏ viền focus
        horizontalBar.add(Box.createHorizontalGlue()); // Thêm glue đưa các thành phần về bên phải
        horizontalBar.add(dangKy);
        horizontalBar.add(dangNhap);
        horizontalBar.add(Box.createRigidArea(new Dimension(20, 0))); // Để tạo khoảng cách giữa nút

        JPanel mainPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(rightPanel.getWidth(), rightPanel.getHeight() / 2);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ DÂN CƯ") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(mainPanel.getWidth(), mainPanel.getHeight() / 4);
            }
            @Override
            public Font getFont() {
                return new Font("Arial", Font.PLAIN, mainPanel.getHeight() / 16);
            }
        };
        title.setFont(new Font("Arial", Font.PLAIN, 20));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setForeground(Color.decode("#0097B2"));

        mainPanel.add(title, BorderLayout.NORTH);

        rightPanel.add(mainPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new GiaoDienBanDau();
    }
}