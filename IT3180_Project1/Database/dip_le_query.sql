-- Active: 1685701040408@@localhost@5432@nncnpm@public
insert into dip_le VALUES('Cuối Năm Học', '2023-06-15');


CREATE TRIGGER phat_thuong_trigger
AFTER INSERT ON dip_le
FOR EACH ROW
EXECUTE FUNCTION insert_phat_thuong_trigger();

SELECT trigger_name
FROM information_schema.triggers
WHERE event_object_table = 'dip_le'; -- Thay 'dip_le' bằng tên bảng bạn quan tâm

DROP TRIGGER IF EXISTS phat_banh_keo ON dip_le;






CREATE TRIGGER phat_thuong_from_thanh_tich_trigger
AFTER INSERT ON thanh_tich_hoc_tap
FOR EACH ROW
EXECUTE FUNCTION insert_phat_thuong_from_thanh_tich_trigger();


ALTER TABLE "phat_thuong" 
ADD CONSTRAINT aa 
FOREIGN KEY ("dip_le", "ngay") 
REFERENCES "dip_le"("ten_dip_le", "ngay");
