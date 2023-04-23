-- ===================================================================
-- $Id: 2020.03.23.data_transfer_result.txt
-- ===================================================================
--
-- ■請求・結果情報ヘッダ
--
-- ===================================================================

CREATE TABLE data_transfer_result
(
  transfer_id integer NOT NULL, -- 自動採番
  target_month timestamp without time zone NOT NULL, -- data_monthly_batch_log.targer_month
  transfer_year character varying(4) NOT NULL, -- 西暦が入る
  transfer_month character varying(2) NOT NULL, -- 01～12が入る
  transfer_date character varying(2) NOT NULL, -- 14か27が入る
  total_num integer NOT NULL,
  total_amount integer NOT NULL,
  status integer NOT NULL, -- 1:請求データ登録　2:結果データ取込済
  insert_date timestamp with time zone NOT NULL,
  update_date timestamp with time zone NOT NULL,
  delete_date timestamp with time zone,
  CONSTRAINT data_transfer_result_pkey PRIMARY KEY (transfer_id)
)
WITH (OIDS=FALSE);
ALTER TABLE data_transfer_result OWNER TO postgres;
COMMENT ON COLUMN data_transfer_result.transfer_id IS '自動採番';
COMMENT ON COLUMN data_transfer_result.target_month IS 'data_monthly_batch_log.targer_month';
COMMENT ON COLUMN data_transfer_result.transfer_year IS '西暦が入る';
COMMENT ON COLUMN data_transfer_result.transfer_month IS '01～12が入る';
COMMENT ON COLUMN data_transfer_result.transfer_date IS '14か27が入る';
COMMENT ON COLUMN data_transfer_result.status IS '1:請求データ登録　2:結果データ取込済';
