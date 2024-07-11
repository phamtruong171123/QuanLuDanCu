package giaodien;
import connect.ConnectDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class ICreateEventDAO {
    private Date date;
    private String eventChoosed;

    public ICreateEventDAO() {
        // Empty constructor
    }

    public ICreateEventDAO(Date date, String eventChoosed) {
        this.date = date;
        this.eventChoosed = eventChoosed;
    }

    public void insertEvent() {
        try (ConnectDatabase connectDatabase = new ConnectDatabase();
             Connection connection = connectDatabase.getConnectDatabase()) {
        
            // Kiểm tra xem sự kiện đã tồn tại trong CSDL hay không
            if (isEventExist(connection, this.eventChoosed, this.date)) {
                System.out.println("Event already exists");
                JOptionPane.showMessageDialog(null, "Sự kiện đã tồn tại trong CSDL.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return; // Không thêm sự kiện nếu đã tồn tại
            }

            // Nếu không tồn tại, thực hiện thêm sự kiện
            String query = "INSERT INTO dip_le VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, this.eventChoosed);
                preparedStatement.setDate(2, this.date);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Đã thêm thành công sự kiện : "+eventChoosed+" ngày: "+ date, "Thông báo", JOptionPane.WARNING_MESSAGE);
                System.out.println("Event inserted successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error inserting event: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isEventExist(Connection connection, String eventName, Date eventDate) throws SQLException {
        String query = "SELECT COUNT(*) FROM dip_le WHERE ten_dip_le = ? AND ngay = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, eventName);
            preparedStatement.setDate(2, eventDate);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Trả về true nếu sự kiện đã tồn tại
            }

            return false;
        }
    }

    public DefaultTableModel getThangTichHocTapData(int year) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Tên học sinh");
        model.addColumn("Thành tích");
        model.addColumn("Mã nhân khẩu");
        model.addColumn("Năm học");

        try (ConnectDatabase connectDatabase = new ConnectDatabase();
             Connection connection = connectDatabase.getConnectDatabase()) {
            String query = "SELECT * FROM thanh_tich_hoc_tap WHERE nam_hoc = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, year);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String ten = resultSet.getString("ten_hoc_sinh");
                    String thanhtich = resultSet.getString("thanh_tich");
                    int namHoc = resultSet.getInt("nam_hoc");
                    int maNhanKhau = resultSet.getInt("ma_nhan_khau");
                    model.addRow(new Object[]{ten, thanhtich, maNhanKhau, namHoc});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error getting table data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return model;
    }

    public void updateThangTichHocTap(int maNhanKhau, int namHoc, String thanhTich) {
        try (ConnectDatabase connectDatabase = new ConnectDatabase();
             Connection connection = connectDatabase.getConnectDatabase()) {
            String query = "UPDATE thanh_tich_hoc_tap SET thanh_tich = ? WHERE ma_nhan_khau = ? AND nam_hoc = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, thanhTich);
                preparedStatement.setInt(2, maNhanKhau);
                preparedStatement.setInt(3, namHoc);
                preparedStatement.executeUpdate();
                System.out.println(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
