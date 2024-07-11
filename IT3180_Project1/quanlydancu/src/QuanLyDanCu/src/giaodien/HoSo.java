package QuanLyDanCu.src.giaodien;

import javax.swing.*;
import java.awt.*;

public class HoSo extends GiaoDienChung {
    private JSplitPane editPane;
    private JPanel profilePane;
    private JPanel accountPane;
    private JButton btnChinhSua;
    private JButton btnHuy;
    private JLabel lblHoTen;
    private JLabel lblNgaySinh;
    private JLabel lblGioiTinh;
    private JLabel lblChucVu;
    private JLabel lblHoTen1;
    private JLabel lblNgaySinh1;
    private JLabel lblGioiTinh1;
    private JLabel lblChucVu1;
    private JLabel lblUsername;
    private JLabel lblMatKhau;
    private JLabel lblNhapLaiMatKhau;
    private JTextField txtHoTen;
    private JTextField txtNgaySinh;
    private JTextField txtGioiTinh;
    private JTextField txtChucVu;
    private JTextField txtUsername;
    private JPasswordField txtMatKhau;
    private JPasswordField txtNhapLaiMatKhau;

    public HoSo() {
        super();
        frame.setTitle("Hồ sơ - Quản lý dân cư");

        // Xóa mọi thứ ở horizontal bar
        horizontalBar.removeAll();
        horizontalBar.setBackground(Color.WHITE);
        horizontalBar.setLayout(new BoxLayout(horizontalBar, BoxLayout.X_AXIS));

        // Thêm Box.createHorizontalGlue() để đảm bảo label "Hồ sơ" và glue được căn giữa
        horizontalBar.add(Box.createHorizontalGlue());

        // Thêm label "Hồ sơ" và căn giữa
        JLabel lblHoSo = new JLabel("Hồ sơ");
        lblHoSo.setForeground(Color.BLUE);
        lblHoSo.setHorizontalAlignment(JLabel.CENTER);
        lblHoSo.setFont(lblHoSo.getFont().deriveFont(Font.BOLD, lblHoSo.getFont().getSize() * 2)); // Tăng cỡ chữ gấp đôi
        horizontalBar.add(lblHoSo);

        // Thêm Box.createHorizontalGlue() tiếp theo để đảm bảo căn giữa
        horizontalBar.add(Box.createHorizontalGlue());

        // Tạo profilePane
        profilePane = new JPanel();
        profilePane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        lblHoTen = createLabel("Họ và tên:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        profilePane.add(lblHoTen, gbc);

        txtHoTen = createTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2; // Đặt chiều rộng thành 2
        profilePane.add(txtHoTen, gbc);

        lblNgaySinh = createLabel("Ngày sinh:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Đặt lại chiều rộng về 1
        profilePane.add(lblNgaySinh, gbc);

        txtNgaySinh = createTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        profilePane.add(txtNgaySinh, gbc);

        lblGioiTinh = createLabel("Giới tính:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        profilePane.add(lblGioiTinh, gbc);

        txtGioiTinh = createTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        profilePane.add(txtGioiTinh, gbc);

        lblChucVu = createLabel("Chức vụ:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        profilePane.add(lblChucVu, gbc);

        txtChucVu = createTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        profilePane.add(txtChucVu, gbc);

        // Tạo accountPane
        accountPane = new JPanel();
        accountPane.setLayout(new GridBagLayout());
        GridBagConstraints gbcAccount = new GridBagConstraints();
        gbcAccount.anchor = GridBagConstraints.WEST;
        gbcAccount.insets = new Insets(5, 5, 5, 5);

        lblUsername = createLabel("Tên đăng nhập:");
        gbcAccount.gridx = 0;
        gbcAccount.gridy = 0;
        accountPane.add(lblUsername, gbcAccount);

        txtUsername = createTextField();
        gbcAccount.gridx = 1;
        gbcAccount.gridy = 0;
        gbcAccount.fill = GridBagConstraints.HORIZONTAL;
        gbcAccount.gridwidth = 2; // Đặt chiều rộng thành 2
        accountPane.add(txtUsername, gbcAccount);

        lblMatKhau = createLabel("Mật khẩu:");
        gbcAccount.gridx = 0;
        gbcAccount.gridy = 1;
        gbcAccount.gridwidth = 1; // Đặt lại chiều rộng về 1
        accountPane.add(lblMatKhau, gbcAccount);

        txtMatKhau = createPasswordField();
        gbcAccount.gridx = 1;
        gbcAccount.gridy = 1;
        accountPane.add(txtMatKhau, gbcAccount);

        lblNhapLaiMatKhau = createLabel("Nhập lại mật khẩu:");
        gbcAccount.gridx = 0;
        gbcAccount.gridy = 2;
        accountPane.add(lblNhapLaiMatKhau, gbcAccount);

        txtNhapLaiMatKhau = createPasswordField();
        gbcAccount.gridx = 1;
        gbcAccount.gridy = 2;
        accountPane.add(txtNhapLaiMatKhau, gbcAccount);

        // Tạo editPane và set chiều cao bằng 1/2 chiều cao của editPane
        editPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, accountPane, profilePane);
        editPane.setResizeWeight(0.5);
        editPane.setDividerSize(0);
        rightPanel.add(editPane, BorderLayout.CENTER);

        // Tạo pane ở south với nút chỉnh sửa và nút hủy
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnChinhSua = new JButton("Chỉnh sửa");
        btnHuy = new JButton("Hủy");

        southPanel.add(btnChinhSua);
        southPanel.add(btnHuy);

        rightPanel.add(southPanel, BorderLayout.SOUTH);

        lblHoTen1=new JLabel("họ và trên");
        lblHoTen1.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 0));
     lblNgaySinh1=new JLabel("ngay sinh");
     lblGioiTinh1=new JLabel("gioi tinh");
     lblChucVu1=new JLabel("chuc vu");
        navigatePanel.add(lblHoTen1);
        navigatePanel.add(lblNgaySinh1);
        navigatePanel.add(lblGioiTinh1);
        navigatePanel.add(lblChucVu1);
        lblNgaySinh1.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 0));
        lblGioiTinh1.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 0));
        lblChucVu1.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 0));
        lblHoTen1.setForeground(Color.white);
        lblNgaySinh1.setForeground(Color.white);
        lblGioiTinh1.setForeground(Color.white);
        lblChucVu1.setForeground(Color.white);
        frame.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, (float) (label.getFont().getSize() * 1.5))); // Tăng cỡ chữ lên 1.5 lần
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setColumns(10);
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setColumns(10);
        return passwordField;
    }

    public static void main(String[] args) {
        new HoSo();
    }
}
