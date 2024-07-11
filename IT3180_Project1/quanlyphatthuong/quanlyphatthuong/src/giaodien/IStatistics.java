package giaodien.phatthuong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connect.*;

public class IStatistics extends GiaoDienPhatThuong {
    private JTextField yearTextField;
    private JComboBox<String> eventOptions;
    private JRadioButton chungRadioButton, hoGiaDinhRadioButton;

    private JTable chungTableView, hoGiaDinhTableView;
    private DefaultTableModel chungTableModel, hoGiaDinhTableModel;
    private JPanel statisticsPanel;
    JScrollPane hoGiaDinhScrollPane,chungScrollPane;
    public IStatistics(String userName) {
        super(userName);
        createStatisticsPanel();
    }

    private void createStatisticsPanel() {
        statisticsPanel = new JPanel(new BorderLayout());
        statisticsPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel labelOptionYearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel labelDip = new JLabel("Dịp:");
        labelDip.setForeground(Color.BLUE);
        labelDip.setFont(new Font("Arial", Font.PLAIN, 20));

        eventOptions = new JComboBox<>(new String[]{"Cuối Năm Học", "Tết Trung Thu", "Tết Thiếu Nhi", "Tết Nguyên Đán"});
        eventOptions.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));

        labelOptionYearPanel.add(labelDip);
        labelOptionYearPanel.add(eventOptions);

        yearTextField = new JTextField(5);

        JButton viewButton = new JButton("Xem");
        viewButton.setBackground(Color.GREEN);
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onViewButtonClick();
            }
        });

        labelOptionYearPanel.add(new JLabel("Năm:"));
        labelOptionYearPanel.add(yearTextField);
        

        chungRadioButton = new JRadioButton("Chung");
        hoGiaDinhRadioButton = new JRadioButton("Hộ Gia Đình");

        ButtonGroup group = new ButtonGroup();
        group.add(chungRadioButton);
        group.add(hoGiaDinhRadioButton);

        // Thêm các thành phần vào panel
        labelOptionYearPanel.add(chungRadioButton);
        labelOptionYearPanel.add(hoGiaDinhRadioButton);

        labelOptionYearPanel.add(viewButton);
        statisticsPanel.add(labelOptionYearPanel, BorderLayout.NORTH);

        // Tạo bảng cho "Chung"
        chungTableModel = new DefaultTableModel();
        chungTableView = new JTable(chungTableModel);
        
        chungTableModel.addColumn("Loại phần thưởng");
        chungTableModel.addColumn("Số lượng");
        chungTableModel.addColumn("Giá Trị");
         chungScrollPane = new JScrollPane(chungTableView);
        statisticsPanel.add(chungScrollPane, BorderLayout.CENTER);

        // Tạo bảng cho "Hộ Gia Đình"
        hoGiaDinhTableModel = new DefaultTableModel();
        hoGiaDinhTableView = new JTable(hoGiaDinhTableModel);
        hoGiaDinhTableModel.addColumn("Mã hô khẩu");
        hoGiaDinhTableModel.addColumn("Loại Phần Thưởng");
        hoGiaDinhTableModel.addColumn("Số lượng");
        hoGiaDinhScrollPane = new JScrollPane(hoGiaDinhTableView);

       
        

        rightPanel.add(statisticsPanel, BorderLayout.CENTER);
    }

    private void onViewButtonClick() {
        int selectedYear;
        try {
            selectedYear = Integer.parseInt(yearTextField.getText());
            updateTableData(selectedYear);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Vui lòng nhập một năm hợp lệ.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableData(int year) {
        String selectedEvent = eventOptions.getSelectedItem().toString();
        boolean isChungSelected = chungRadioButton.isSelected();
    
        clearTableData(chungTableModel);
        clearTableData(hoGiaDinhTableModel);
    
        if (isChungSelected) {
            executeChungQuery(selectedEvent, year);
            chungTableView.setVisible(true);
            hoGiaDinhTableView.setVisible(false);
            statisticsPanel.add(chungScrollPane, BorderLayout.CENTER);
            statisticsPanel.remove(hoGiaDinhScrollPane);
        } else {
            executeHoGiaDinhQuery(selectedEvent, year);
            chungTableView.setVisible(false);
            hoGiaDinhTableView.setVisible(true);
            statisticsPanel.add(hoGiaDinhScrollPane, BorderLayout.CENTER);
            statisticsPanel.remove(chungScrollPane);
        }
    
        // Repaint the panel to reflect changes
        statisticsPanel.revalidate();
        statisticsPanel.repaint();
    }
    

    private void executeChungQuery(String selectedEvent, int year) {
        // Execute the query for "Chung"
        String query = "SELECT " +
                "phan_thuong.loai_phan_thuong, " +
                "SUM(phat_thuong.so_luong) AS so_luong, " +
                "phan_thuong.gia_tri * SUM(phat_thuong.so_luong) || ' VND' AS gia_tri " +
                "FROM phat_thuong " +
                "JOIN phan_thuong ON phat_thuong.ma_phan_thuong = phan_thuong.ma_phan_thuong " +
                "WHERE EXTRACT(YEAR FROM phat_thuong.ngay) = ? AND phat_thuong.dip_le = ? " +
                "GROUP BY phan_thuong.loai_phan_thuong, phan_thuong.gia_tri";
    
        executeQuery(query, chungTableModel, year, selectedEvent);
    }
    

    private void executeHoGiaDinhQuery(String selectedEvent, int year) {
        // Execute the query for "Hộ Gia Đình"
        String query = "SELECT " +
                "Ho_khau.Ma_ho_khau, " +
                "Phan_thuong.loai_phan_thuong, " +
                "SUM(Phat_thuong.so_luong) AS so_luong " +
                "FROM Phat_thuong " +
                "JOIN Nhan_khau ON Phat_thuong.Ma_nhan_khau = Nhan_khau.Ma_nhan_khau " +
                "JOIN Ho_khau ON Nhan_khau.Ma_ho_khau = Ho_khau.Ma_ho_khau " +
                "JOIN Phan_thuong ON Phat_thuong.ma_phan_thuong = Phan_thuong.Ma_phan_thuong " +
                "WHERE Phat_thuong.Dip_le = ? AND EXTRACT(YEAR FROM Phat_thuong.ngay) = ? " +
                "GROUP BY Ho_khau.Ma_ho_khau, Phan_thuong.loai_phan_thuong";

        executeQuery(query, hoGiaDinhTableModel, selectedEvent, year);
    }

    private void executeQuery(String query, DefaultTableModel model, Object... params) {
        try (ConnectDatabase connectDatabase = new ConnectDatabase();
             Connection connection = connectDatabase.getConnectDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            // Set parameters based on the query
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Clear old data
            clearTableData(model);
    
            // Display the results in the table
            while (resultSet.next()) {
                String loaiPhanThuong = resultSet.getString("loai_phan_thuong");
                int soLuong = resultSet.getInt("so_luong");
    
                // If it's "Chung" query
                if (query.contains("SUM(phat_thuong.so_luong)")) {
                    String giaTri = resultSet.getString("gia_tri");
                    model.addRow(new Object[]{loaiPhanThuong, soLuong, giaTri});
                } else { // If it's "Hộ Gia Đình" query
                    int maHoKhau = resultSet.getInt("Ma_ho_khau");
                    model.addRow(new Object[]{maHoKhau, loaiPhanThuong, soLuong});
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error executing query: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearTableData(DefaultTableModel model) {
        model.setRowCount(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new IStatistics("1");
        });
    }
}