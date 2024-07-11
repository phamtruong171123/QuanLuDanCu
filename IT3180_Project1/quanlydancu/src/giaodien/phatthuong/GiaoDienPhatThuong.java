package giaodien.phatthuong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
import QuanLyDanCu.src.giaodien.*;
import giaodien.phatthuong.*;

public class GiaoDienPhatThuong extends GiaoDienChung {
    protected JButton btnTaoDip, btnXemDip;
    protected JButton btnQuyPhanThuong, btnThongKe;
  
    public GiaoDienPhatThuong() {
        super();

        btnTaoDip = new JButton("Tạo dịp ");
        customizeButton(btnTaoDip);
        btnXemDip = new JButton("Xem dịp");
        customizeButton(btnXemDip);
        btnThongKe = createCustomButton("Thống kê");
        btnQuyPhanThuong = createCustomButton(" Quỹ");

        navigatePanel.setLayout(new BoxLayout(navigatePanel, BoxLayout.Y_AXIS));

         navigatePanel.add(btnTaoDip);
         navigatePanel.add(Box.createVerticalGlue());
         navigatePanel.add(btnXemDip);
         navigatePanel.add(Box.createVerticalGlue());
        navigatePanel.add(btnThongKe);
        navigatePanel.add(Box.createVerticalGlue());
        navigatePanel.add(btnQuyPhanThuong);

        navigatePanel.add(Box.createVerticalGlue());

        

       

        

     

        btnTaoDip.addActionListener(new CreateEventButtonListener());
        btnXemDip.addActionListener(new ViewEventButtonListener());
        btnThongKe.addActionListener(new StatisticsButtonListener());
        btnQuyPhanThuong.addActionListener(new FundPoolButtonListener());
    }

    // Listener for "Create Awarding Event" button
    protected class CreateEventButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Gọi hàm hoặc thực hiện các tác vụ liên quan khi nhấn nút "Create Awarding Event"
            new ICreateEvent();
        }
    }

    // Listener for "View Awarding Event" button
    public class ViewEventButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Gọi hàm hoặc thực hiện các tác vụ liên quan khi nhấn nút "View Awarding Event"
            new IViewEvent();
           
        }
    }

    // Listener for "Statistics" button
    public class StatisticsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Gọi hàm hoặc thực hiện các tác vụ liên quan khi nhấn nút "Statistics"
            new IStatistics();
        }
    }

    // Listener for "Fund Pool" button
    public class FundPoolButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Gọi hàm hoặc thực hiện các tác vụ liên quan khi nhấn nút "Fund Pool"
            new IFund();
        }
    }

    public void customizeButton(AbstractButton button) {
        button.setBackground(Color.decode("#004AAD"));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        customizeButton(button);
        return button;
    }

    public static void main(String[] args) {
        new GiaoDienPhatThuong();
    }
}
