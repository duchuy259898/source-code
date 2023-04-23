-- ===================================================================
-- $Id: 2020.03.23.data_transfer_result_detail.txt
-- ===================================================================
--
-- ¡¿‹EŒ‹‰Êî•ñ–¾×
--
-- ===================================================================


CREATE TABLE data_transfer_result_detail
(
  transfer_id integer NOT NULL, -- ©“®Ì”Ô
  transfer_detail_id integer NOT NULL, -- ¿‹ID‚É‘Î‚µ‚Ä©“®Ì”Ô
  customer_id integer NOT NULL,
  account_customer_no character varying(14) NOT NULL,
  bank_code character varying(4) NOT NULL,
  branch_code character varying(3) NOT NULL,
  account_type character varying(1) NOT NULL,
  account_number character varying(7) NOT NULL,
  account_name character varying(30) NOT NULL,
  billing_amount integer NOT NULL,
  result_code integer, -- 0F¬Œ÷...
  shop_id integer NOT NULL, -- ‘ÎÛ‚Ì“`•[‚Ì“X•ÜID‚ğƒZƒbƒg
  slip_no integer NOT NULL, -- ‘ÎÛ‚Ì“`•[‚Ì“`•[NO‚ğƒZƒbƒg
  insert_date timestamp with time zone NOT NULL,
  update_date timestamp with time zone NOT NULL,
  delete_date timestamp with time zone,
  CONSTRAINT data_transfer_result_detail_pkey PRIMARY KEY (transfer_id, transfer_detail_id, customer_id)
)
WITH (OIDS=FALSE);
ALTER TABLE data_transfer_result_detail OWNER TO postgres;
COMMENT ON COLUMN data_transfer_result_detail.transfer_id IS '©“®Ì”Ô';
COMMENT ON COLUMN data_transfer_result_detail.transfer_detail_id IS '¿‹ID‚É‘Î‚µ‚Ä©“®Ì”Ô';
COMMENT ON COLUMN data_transfer_result_detail.result_code IS '0F¬Œ÷
1F¸”s:‘‹à•s‘«
2F¸”s:—a‹àæˆø‚È‚µ
3F¸”s:—a‹àÒ“s‡‚É‚æ‚éU‘Ö’â~ 
4F¸”s:—a‹àŒûÀU‘ÖˆË—Š‘‚È‚µ
8F¸”s:ˆÏ‘õÒ“s‡‚É‚æ‚éU‘Ö’â~
9F¸”s:‚»‚Ì‘¼';
COMMENT ON COLUMN data_transfer_result_detail.shop_id IS '‘ÎÛ‚Ì“`•[‚Ì“X•ÜID‚ğƒZƒbƒg';
COMMENT ON COLUMN data_transfer_result_detail.slip_no IS '‘ÎÛ‚Ì“`•[‚Ì“`•[NO‚ğƒZƒbƒg';