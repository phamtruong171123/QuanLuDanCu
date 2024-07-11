package giaodien.phatthuong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import connect.ConnectDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IViewEvent extends GiaoDienPhatThuong {
    private JTextField yearTextField;
    private JComboBox<String> eventOptions;
    private JTable tableView;
    private DefaultTableModel tableModel;

    public IViewEvent(String userName) {
        super(userName);
        createViewPanel();
    }

    private void createViewPanel() {
        // Tạo panel mới để chứa các thành phần
        JPanel viewPanel = new JPanel(new BorderLayout());
        viewPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Tạo panel mới để chứa label, option, năm và nút Xem
        JPanel labelOptionYearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelDip = new JLabel("Dịp:");
        labelDip.setForeground(Color.BLUE);
        labelDip.setFont(new Font("Arial", Font.PLAIN, 20));

        // Khởi tạo eventOptions
        eventOptions = new JComboBox<>(new String[]{"Cuối Năm Học", "Tết Trung Thu", "Tết Thiếu Nhi", "Tết Nguyên Đán"});
        eventOptions.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));

        // Thêm các thành phần vào labelOptionYearPanel
        labelOptionYearPanel.add(labelDip);
        labelOptionYearPanel.add(eventOptions);

        // Thêm năm và nút Xem vào labelOptionYearPanel
        yearTextField = new JTextField(5);
        JButton viewButton = new JButton("Xem");

        // Đặt màu xanh cho nút Xem
        viewButton.setBackground(Color.GREEN);

        // Thêm sự kiện cho nút Xem
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xử lý sự kiện khi nút Xem được nhấn
                onViewButtonClick();
            }
        });

        // Thêm năm và nút Xem vào labelOptionYearPanel
        labelOptionYearPanel.add(new JLabel("Năm:"));
        labelOptionYearPanel.add(yearTextField);
        labelOptionYearPanel.add(viewButton);

        // Thêm labelOptionYearPanel vào viewPanel
        viewPanel.add(labelOptionYearPanel, BorderLayout.NORTH);

        // Tạo bảng tên học sinh, loại quà, số lượng, đã nhận và mã phần thưởng
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã phần thưởng");
        tableModel.addColumn("Tên học sinh");
        tableModel.addColumn("Loại phần thưởng");
        tableModel.addColumn("Số lượng");
        tableModel.addColumn("Đã nhận");

        tableView = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableView);
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        // Thêm sự kiện click chuột để cập nhật giá trị "Đã nhận"
        tableView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onTableMouseClicked(e);
            }
        });

        // Thêm viewPanel vào rightPanel
        rightPanel.add(viewPanel, BorderLayout.CENTER);
    }

    private void onViewButtonClick() {
        // Xử lý sự kiện khi nút Xem được nhấn
        // Lấy dữ liệu từ cơ sở dữ liệu và cập nhật bảng
        int selectedYear;
        try {
            selectedYear = Integer.parseInt(yearTextField.getText());
            // Gọi hàm để lấy và cập nhật dữ liệu từ cơ sở dữ liệu dựa trên năm đã chọn
            updateTableData(selectedYear);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Vui lòng nhập một năm hợp lệ.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableData(int year) {
        // Gọi hàm để lấy dữ liệu từ cơ sở dữ liệu và cập nhật bảng
        // Sử dụng câu truy vấn SQL đã chỉnh sửa cho trường hợp này
        String query = "SELECT phat_thuong.ma_phat_thuong, ho_va_ten, phan_thuong.loai_phan_thuong, phat_thuong.so_luong, phat_thuong.da_xac_nhan " +
                "FROM nhan_khau " +
                "JOIN phat_thuong ON nhan_khau.ma_nhan_khau = phat_thuong.ma_nhan_khau " +
                "JOIN phan_thuong ON phan_thuong.ma_phan_thuong = phat_thuong.ma_phan_thuong " +
                "JOIN dip_le ON dip_le.ten_dip_le = phat_thuong.dip_le " +
                "WHERE dip_le.ten_dip_le = ? AND EXTRACT(YEAR FROM dip_le.ngay) = ?";

        try (ConnectDatabase connectDatabase = new ConnectDatabase();
             Connection connection = connectDatabase.getConnectDatabase()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Lấy tên dịp từ JComboBox
                String selectedEvent = eventOptions.getSelectedItem().toString();
                preparedStatement.setString(1, selectedEvent);
                preparedStatement.setInt(2, year);

                // Thực hiện truy vấn
                ResultSet resultSet = preparedStatement.executeQuery();

                // Xóa dữ liệu cũ trong bảng
                clearTableData();

                // Đọc dữ liệu từ ResultSet và thêm vào bảng
                while (resultSet.next()) {
                    // Lấy giá trị của các cột
                    int maPhanThuong = resultSet.getInt("ma_phat_thuong");
                    String hoVaTen = resultSet.getString("ho_va_ten");
                    String loaiPhanThuong = resultSet.getString("loai_phan_thuong");
                    int soLuong = resultSet.getInt("so_luong");
                    boolean daXacNhan = resultSet.getBoolean("da_xac_nhan");

                    // Chuyển giá trị boolean thành chuỗi "Có" hoặc "Không"
                    String daXacNhanString = daXacNhan ? "Có" : "Không";

                    // Thêm vào bảng
                    tableModel.addRow(new Object[]{maPhanThuong, hoVaTen, loaiPhanThuong, soLuong, daXacNhanString});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật bảng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearTableData() {
        // Xóa dữ liệu trong bảng
        tableModel.setRowCount(0);
    }

    private void onTableMouseClicked(MouseEvent e) {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow >= 0) {
            int maPhanThuong = (int) tableView.getValueAt(selectedRow, 0);
            String currentValue = (String) tableView.getValueAt(selectedRow, 4);

            // Hiển thị hộp thoại với các lựa chọn "Có", "Không", "Hủy"
            Object[] options = {"Có", "Không", "Hủy"};
            int result = JOptionPane.showOptionDialog(null,
                    "Chọn giá trị mới cho 'Đã nhận'",
                    "Cập nhật giá trị",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]);

            // Xử lý dựa trên lựa chọn của người dùng
            switch (result) {
                case JOptionPane.YES_OPTION:
                    updateDaXacNhanValue(maPhanThuong, "Có");
                    break;
                case JOptionPane.NO_OPTION:
                    updateDaXacNhanValue(maPhanThuong, "Không");
                    break;
                case JOptionPane.CANCEL_OPTION:
                case JOptionPane.CLOSED_OPTION:
                    // Không làm gì nếu người dùng chọn "Hủy" hoặc đóng cửa sổ
                    break;
            }
        }
    }

    private void updateDaXacNhanValue(int maPhatThuong, String newValue) {
        boolean check;

        if (newValue.equals("Có")) {
            check = true;
        } else check = false;
        String updateQuery = "UPDATE phat_thuong SET da_xac_nhan = ? WHERE ma_phat_thuong = ?";

        try (ConnectDatabase connectDatabase = new ConnectDatabase();
             Connection connection = connectDatabase.getConnectDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Thiết lập giá trị cho PreparedStatement
            preparedStatement.setBoolean(1, check);
            preparedStatement.setInt(2, maPhatThuong);

            // Thực hiện cập nhật
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Cập nhật giá trị thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Không có dòng nào được cập nhật.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật giá trị: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new IViewEvent("1");
        });
    }
}
