package giaodien.phatthuong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connect.*;

public class IFund extends GiaoDienPhatThuong {
    private JTable phanThuongTable;
    private DefaultTableModel phanThuongTableModel;

    public IFund(String userName) {
        super(userName);
        createFundPanel();
    }

    private void createFundPanel() {
        JPanel fundPanel = new JPanel(new BorderLayout());
        fundPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        phanThuongTableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 4 || column == 5) {
                    return JButton.class;
                }
                return Object.class;
            }
        };

        phanThuongTable = new JTable(phanThuongTableModel);

        phanThuongTableModel.addColumn("Mã Phần Thưởng");
        phanThuongTableModel.addColumn("Loại Phần Thưởng");
        phanThuongTableModel.addColumn("Giá Trị");
        phanThuongTableModel.addColumn("Số Lượng");
        phanThuongTableModel.addColumn("Thêm");
        phanThuongTableModel.addColumn("Giảm");

        loadPhanThuongData();

        JScrollPane phanThuongScrollPane = new JScrollPane(phanThuongTable);
        fundPanel.add(phanThuongScrollPane, BorderLayout.CENTER);

        JButton addButton = createButton("plusicon.png");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleButtonAction(true);
            }
        });

        JButton subtractButton = createButton("minusicon.png");
        subtractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleButtonAction(false);
            }
        });

        phanThuongTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(addButton));
        phanThuongTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(addButton));

        phanThuongTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer(subtractButton));
        phanThuongTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(subtractButton));

        rightPanel.add(fundPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String iconName) {
        JButton button = new JButton();
        button.setIcon(new ImageIcon(new ImageIcon("QuanLyDanCu/src/icon/" + iconName).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        button.setBackground(Color.WHITE);
        return button;
    }

    private void handleButtonAction(boolean isAdd) {
        int selectedRow = phanThuongTable.getSelectedRow();
        if (selectedRow != -1) {
            int maPhanThuong = (int) phanThuongTableModel.getValueAt(selectedRow, 0);
            String input = JOptionPane.showInputDialog(null, "Nhập số lượng mới:", isAdd ? "Thêm vào Số Lượng" : "Giảm Số Lượng", JOptionPane.QUESTION_MESSAGE);
            if (input != null) {
                try {
                    int deltaSoLuong = Integer.parseInt(input);
                    updateSoLuongInDatabase(maPhanThuong, deltaSoLuong * (isAdd ? 1 : -1));
                    int currentSoLuong = (int) phanThuongTableModel.getValueAt(selectedRow, 3);
                    phanThuongTableModel.setValueAt(currentSoLuong + deltaSoLuong * (isAdd ? 1 : -1), selectedRow, 3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng để thay đổi số lượng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPhanThuongData() {
        try (ConnectDatabase connectDatabase = new ConnectDatabase();
             Connection connection = connectDatabase.getConnectDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM phan_thuong");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            clearTableData(phanThuongTableModel);

            while (resultSet.next()) {
                int maPhanThuong = resultSet.getInt("ma_phan_thuong");
                String loaiPhanThuong = resultSet.getString("loai_phan_thuong");
                String giaTri = resultSet.getString("gia_tri");
                int soLuong = resultSet.getInt("so_luong");

                phanThuongTableModel.addRow(new Object[]{maPhanThuong, loaiPhanThuong, giaTri, soLuong, null, null});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data from database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSoLuongInDatabase(int maPhanThuong, int deltaSoLuong) {
        try (ConnectDatabase connectDatabase = new ConnectDatabase();
             Connection connection = connectDatabase.getConnectDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE phan_thuong SET so_luong = so_luong + ? WHERE ma_phan_thuong = ?")) {

            preparedStatement.setInt(1, deltaSoLuong);
            preparedStatement.setInt(2, maPhanThuong);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data updated successfully.");
            } else {
                System.out.println("No rows were updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating data in database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(JButton button) {
            setOpaque(true);
            setIcon(button.getIcon());
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("");
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        public ButtonEditor(JButton button) {
            super(new JCheckBox());
            this.button = button;
            this.button.setOpaque(true);
            this.button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        public Object getCellEditorValue() {
            return button;
        }
    }

    private void clearTableData(DefaultTableModel model) {
        model.setRowCount(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new IFund("1");
        });
    }
}
