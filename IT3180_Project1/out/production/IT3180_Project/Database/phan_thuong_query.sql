--  thống kê tổng các phần quà và giá trị tương ứng đã phát trong mỗi lần
select sum(Phat_thuong.So_luong) as Tong_so_luong, sum(Phat_thuong.So_luong * Phan_thuong.Gia_tri) as Tong_gia_tri
from Phat_thuong
join Phan_thuong on Phat_thuong.ma_phan_thuong = Phan_thuong.ma_phan_thuong
  where extract(YEAR FROM Phat_thuong.Ngay_phat) = 2023
  AND Phat_thuong.Dip_le = 'Cuối Năm Học'

-- xem chi tiết mỗi hộ đã nhận những phần quà nào.
SELECT Ho_khau.Ma_ho_khau, Phan_thuong.loai_phan_thuong, Phat_thuong.*
FROM Phat_thuong
JOIN Nhan_khau ON Phat_thuong.Ma_nhan_khau = Nhan_khau.Ma_nhan_khau
JOIN Ho_khau ON Nhan_khau.Ma_ho_khau = Ho_khau.Ma_ho_khau
JOIN Phan_thuong ON Phat_thuong.ma_phan_thuong = Phan_thuong.Ma_phan_thuong
WHERE Phat_thuong.Dip_le = 'Cuối Năm Học'
  AND EXTRACT(YEAR FROM Phat_thuong.Ngay_phat) = 2023
