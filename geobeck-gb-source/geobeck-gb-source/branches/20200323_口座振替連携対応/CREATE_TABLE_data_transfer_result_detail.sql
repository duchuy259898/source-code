-- ===================================================================
-- $Id: 2020.03.23.data_transfer_result_detail.txt
-- ===================================================================
--
-- ■請求・結果情報明細
--
-- ===================================================================


CREATE TABLE data_transfer_result_detail
(
  transfer_id integer NOT NULL, -- 自動採番
  transfer_detail_id integer NOT NULL, -- 請求IDに対して自動採番
  customer_id integer NOT NULL,
  account_customer_no character varying(14) NOT NULL,
  bank_code character varying(4) NOT NULL,
  branch_code character varying(3) NOT NULL,
  account_type character varying(1) NOT NULL,
  account_number character varying(7) NOT NULL,
  account_name character varying(30) NOT NULL,
  billing_amount integer NOT NULL,
  result_code integer, -- 0：成功...
  shop_id integer NOT NULL, -- 対象の伝票の店舗IDをセット
  slip_no integer NOT NULL, -- 対象の伝票の伝票NOをセット
  insert_date timestamp with time zone NOT NULL,
  update_date timestamp with time zone NOT NULL,
  delete_date timestamp with time zone,
  CONSTRAINT data_transfer_result_detail_pkey PRIMARY KEY (transfer_id, transfer_detail_id, customer_id)
)
WITH (OIDS=FALSE);
ALTER TABLE data_transfer_result_detail OWNER TO postgres;
COMMENT ON COLUMN data_transfer_result_detail.transfer_id IS '自動採番';
COMMENT ON COLUMN data_transfer_result_detail.transfer_detail_id IS '請求IDに対して自動採番';
COMMENT ON COLUMN data_transfer_result_detail.result_code IS '0：成功
1：失敗:資金不足
2：失敗:預金取引なし
3：失敗:預金者都合による振替停止 
4：失敗:預金口座振替依頼書なし
8：失敗:委託者都合による振替停止
9：失敗:その他';
COMMENT ON COLUMN data_transfer_result_detail.shop_id IS '対象の伝票の店舗IDをセット';
COMMENT ON COLUMN data_transfer_result_detail.slip_no IS '対象の伝票の伝票NOをセット';