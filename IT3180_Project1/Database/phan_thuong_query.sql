--  thống kê tổng các phần quà và giá trị tương ứng đã phát trong mỗi lần

SELECT 
   
    phan_thuong.loai_phan_thuong, 
    SUM(phat_thuong.so_luong) AS so_luong, 
    CONCAT(phan_thuong.gia_tri * SUM(phat_thuong.so_luong), ' VND') AS gia_tri 
FROM 
    phat_thuong 
JOIN 
    phan_thuong ON phat_thuong.ma_phan_thuong = phan_thuong.ma_phan_thuong 
WHERE 
    EXTRACT(YEAR FROM phat_thuong.ngay) = 2023 AND phat_thuong.dip_le = 'Cuối Năm Học'
GROUP BY 
   
    phan_thuong.loai_phan_thuong, 
    phan_thuong.gia_tri;

-- xem thông tin phát thưởng trong các dịp:
SELECT ho_va_ten, loai_phan_thuong, phat_thuong.so_luong, phat_thuong.da_xac_nhan 
                FROM nhan_khau 
                JOIN phat_thuong ON nhan_khau.ma_nhan_khau = phat_thuong.ma_nhan_khau 
                JOIN phan_thuong ON phan_thuong.ma_phan_thuong = phat_thuong.ma_phan_thuong 
                JOIN dip_le ON dip_le.ten_dip_le = phat_thuong.dip_le 
                WHERE dip_le.ten_dip_le = 'Cuối Năm Học' AND EXTRACT(YEAR FROM dip_le.ngay) = 2023;


-- xem chi tiết mỗi hộ đã nhận những phần quà nào.
SELECT 
    Ho_khau.Ma_ho_khau, 
    Phan_thuong.loai_phan_thuong, 
    SUM(Phat_thuong.so_luong) AS so_luong
FROM 
    Phat_thuong
JOIN 
    Nhan_khau ON Phat_thuong.Ma_nhan_khau = Nhan_khau.Ma_nhan_khau
JOIN 
    Ho_khau ON Nhan_khau.Ma_ho_khau = Ho_khau.Ma_ho_khau
JOIN 
    Phan_thuong ON Phat_thuong.ma_phan_thuong = Phan_thuong.Ma_phan_thuong
WHERE 
    Phat_thuong.Dip_le = 'Tết Trung Thu'
    AND EXTRACT(YEAR FROM Phat_thuong.ngay) = 2023
GROUP BY 
    Ho_khau.Ma_ho_khau, Phan_thuong.loai_phan_thuong;




CREATE TRIGGER phat_thuong_from_thanh_tich_trigger
AFTER UPDATE ON thanh_tich_hoc_tap
FOR EACH ROW
WHEN (NEW.thanh_tich IS NOT NULL)
EXECUTE FUNCTION insert_phat_thuong_from_thanh_tich_trigger();


-- Đầu tiên, tạo chuỗi tự tăng bắt đầu từ giá trị 100
CREATE SEQUENCE phat_thuong_sequence START 100;

-- Sửa đổi cấu trúc bảng Phat_thuong để thêm giá trị mặc định từ chuỗi tự tăng
ALTER TABLE Phat_thuong
ALTER COLUMN Ma_phat_thuong SET DEFAULT nextval('phat_thuong_sequence');
-- Đặt giá trị khởi đầu mới cho cột tự tăng làm khóa chính
ALTER TABLE phat_thuong ALTER COLUMN ma_phat_thuong RESTART WITH 100;


CREATE OR REPLACE FUNCTION phat_banh_keo_trigger()
RETURNS TRIGGER AS $$
DECLARE
    em RECORD;
BEGIN
    -- Lấy thông tin từ bảng nhân khẩu để đếm số trẻ em trong gia đình
    FOR em IN
        SELECT Ma_nhan_khau
        FROM nhan_khau
        WHERE Ma_ho_khau = NEW.Ma_ho_khau
            AND EXTRACT(YEAR FROM AGE(CURRENT_DATE, ngay_sinh)) BETWEEN 6 AND 17
    LOOP
        -- Phát bánh kẹo cho từng em
        INSERT INTO phat_thuong (ma_nhan_khau, ma_phan_thuong, so_luong, ngay_phat, dip_le)
        VALUES (em.Ma_nhan_khau, 1, 1, CURRENT_DATE, NEW.ten_dip_le);
    END LOOP;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER phat_banh_keo
AFTER INSERT ON dip_le
FOR EACH ROW
WHEN (NEW.ten_dip_le != 'Cuối Năm Học')
EXECUTE FUNCTION phat_banh_keo_trigger();

UPDATE thanh_tich_hoc_tap SET thanh_tich = 'Giỏi' WHERE ten_hoc_sinh = 'Le Thi N';