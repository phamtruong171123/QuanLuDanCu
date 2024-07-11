package giaodien.phatthuong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ICreateEvent extends GiaoDienPhatThuong {
    private JTextField dateTextField;
    private JComboBox<String> eventOptions;
    private JTable tblThanhTich;
    private DefaultTableModel tblModel;
    private JScrollPane scrollPane;

    public ICreateEvent( ) {
        super();

        // Tạo panel mới để chứa label và option menu
        JPanel labelOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(rightPanel.getWidth() / 2, rightPanel.getHeight() / 2);
            }
        };

        // Sử dụng EmptyBorder để đặt lề
        labelOptionPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel labelTenDip = new JLabel("Tên dịp: ");
        labelTenDip.setForeground(Color.BLUE);
        labelTenDip.setFont(new Font("Arial", Font.PLAIN, 20));

        eventOptions = new JComboBox<>(new String[]{"Cuối Năm Học", "Tết Trung Thu", "Tết Thiếu Nhi", "Tết Nguyên Đán"});
        eventOptions.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));

        dateTextField = new JTextField(10);
        dateTextField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));

        JButton saveButton = new JButton("Thêm");
       // saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.setBackground(Color.GREEN);
        saveButton.addActionListener(new SaveEventListener());

        // Khai báo bảng
        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        tblModel.addColumn("Tên học sinh");
        tblModel.addColumn("Thành tích");
        tblModel.addColumn("Mã nhân khẩu");
        tblModel.addColumn("Năm học");

        tblThanhTich = new JTable(tblModel);

        // Thêm bảng vào JScrollPane để có thanh cuộn nếu cần
        scrollPane = new JScrollPane(tblThanhTich);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Đặt chiều cao lớn hơn

        labelOptionPanel.add(labelTenDip);
        labelOptionPanel.add(eventOptions);

        labelOptionPanel.add(dateTextField);
        labelOptionPanel.add(saveButton);

        // Thêm panel vào rightPanel
        rightPanel.add(labelOptionPanel, BorderLayout.CENTER);

        tblThanhTich.setRowSelectionAllowed(true);
        // Thêm sự kiện khi bảng được click
        tblThanhTich.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                tblThanhTichMouseClicked(e);
            }
        });
    }

    private void tblThanhTichMouseClicked(MouseEvent e) {
        int selectedRow = tblThanhTich.getSelectedRow();
    
        if (selectedRow >= 0) {
            String name = (String) tblThanhTich.getValueAt(selectedRow, 0);
            String thanhTich = (String) tblThanhTich.getValueAt(selectedRow, 1);
            int ma_nhan_khau = (int) tblThanhTich.getValueAt(selectedRow, 2);
            int nam_hoc = (int) tblThanhTich.getValueAt(selectedRow, 3);
    
            // Hiển thị JOptionPane với các lựa chọn "Giỏi", "Khá", "Trung Bình", "Hủy"
            String[] options = {"Giỏi", "Khá", "Trung Bình"};
            String selectedValue = (String) JOptionPane.showInputDialog(
                    null,
                    "Chọn giá trị mới cho 'Thành tích'",
                    "Cập nhật giá trị",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
    
            // Nếu người dùng chọn "Hủy" hoặc đóng cửa sổ, không làm gì cả
            if (selectedValue != null && !selectedValue.equals("Hủy")) {
                // Cập nhật giá trị thành tích trong CSDL
                ICreateEventDAO i = new ICreateEventDAO();
                i.updateThangTichHocTap(ma_nhan_khau, nam_hoc, selectedValue);
    
                // Cập nhật giá trị trên bảng
                tblThanhTich.setValueAt(selectedValue, selectedRow, 1);
            }
        }
    }
    private class SaveEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String dateValue = dateTextField.getText();
            String optionValue = eventOptions.getSelectedItem().toString();

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(dateValue);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(utilDate);
                int year = calendar.get(Calendar.YEAR);

                ICreateEventDAO eventDAO = new ICreateEventDAO(sqlDate, optionValue);
                eventDAO.insertEvent();

                // Clear the table content if the selected event is not "Cuối Năm Học"
                if (!optionValue.equals("Cuối Năm Học")) {
                    tblModel.setRowCount(0); // Clear the rows in the table
                } else {
                    rightPanel.add(scrollPane, BorderLayout.SOUTH);
                    rightPanel.revalidate();
                    rightPanel.repaint();
                }

                // Lấy dữ liệu mới từ CSDL và cập nhật bảng
                refreshTableData(year);

            } catch (ParseException | NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }
    

    private void refreshTableData(int year) {
        // Lấy dữ liệu mới từ CSDL và cập nhật bảng
        ICreateEventDAO eventDAO = new ICreateEventDAO();
        DefaultTableModel model = eventDAO.getThangTichHocTapData(year);
        tblThanhTich.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ICreateEvent();
        });
    }
}
