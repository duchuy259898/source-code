-- ===================================================================
-- $Id: 2020.03.23.data_account_info.txt
-- ===================================================================
--
-- ¡ŒûÀî•ñ
--
-- ===================================================================

CREATE TABLE data_account_info
(
  customer_id integer NOT NULL,-- ©“®Ì”Ô
  account_customer_no character varying(14) NOT NULL,-- 0000000000001‚©‚ç©“®Ì”Ô
  bank_code character varying(4) NOT NULL,
  bank_name character varying(15) NOT NULL,
  branch_code character varying(3) NOT NULL,
  branch_name character varying(15) NOT NULL,
  account_type character varying(1) NOT NULL,--1‚©2‚ª“ü‚é
  account_number character varying(7) NOT NULL,
  account_name character varying(30) NOT NULL,
  insert_date timestamp with time zone NOT NULL,
  update_date timestamp with time zone NOT NULL,
  delete_date timestamp with time zone,
  CONSTRAINT data_account_info_pkey PRIMARY KEY (customer_id)
)
WITH (OIDS=FALSE);
ALTER TABLE data_account_info OWNER TO postgres;
COMMENT ON COLUMN data_account_info.customer_id IS '©“®Ì”Ô';
COMMENT ON COLUMN data_account_info.account_customer_no IS '0000000000001‚©‚ç©“®Ì”Ô';
COMMENT ON COLUMN data_account_info.account_type IS '1‚©2‚ª“ü‚é';