-- ===================================================================
-- $Id: 2020.03.23.mst_account_transfer_setting.txt
-- ===================================================================
--
-- 口座振替基本情報
--
-- ===================================================================
CREATE TABLE mst_account_transfer_setting
(
  consignor_code character varying(6) NOT NULL,
  division character varying(2) NOT NULL,
  consignor_name character varying(30) NOT NULL,
  transfer_date character varying(2) NOT NULL, -- 14か27が入る
  payment_method integer NOT NULL,
  insert_date timestamp with time zone NOT NULL,
  update_date timestamp with time zone NOT NULL,
  delete_date timestamp with time zone,
  CONSTRAINT mst_account_transfer_setting_pkey PRIMARY KEY (consignor_code, division)
)
WITH (OIDS=FALSE);
ALTER TABLE mst_account_transfer_setting OWNER TO postgres;
COMMENT ON COLUMN mst_account_transfer_setting.transfer_date IS '14か27が入る';