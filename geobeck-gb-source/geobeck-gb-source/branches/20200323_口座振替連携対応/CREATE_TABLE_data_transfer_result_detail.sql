-- ===================================================================
-- $Id: 2020.03.23.data_transfer_result_detail.txt
-- ===================================================================
--
-- �������E���ʏ�񖾍�
--
-- ===================================================================


CREATE TABLE data_transfer_result_detail
(
  transfer_id integer NOT NULL, -- �����̔�
  transfer_detail_id integer NOT NULL, -- ����ID�ɑ΂��Ď����̔�
  customer_id integer NOT NULL,
  account_customer_no character varying(14) NOT NULL,
  bank_code character varying(4) NOT NULL,
  branch_code character varying(3) NOT NULL,
  account_type character varying(1) NOT NULL,
  account_number character varying(7) NOT NULL,
  account_name character varying(30) NOT NULL,
  billing_amount integer NOT NULL,
  result_code integer, -- 0�F����...
  shop_id integer NOT NULL, -- �Ώۂ̓`�[�̓X��ID���Z�b�g
  slip_no integer NOT NULL, -- �Ώۂ̓`�[�̓`�[NO���Z�b�g
  insert_date timestamp with time zone NOT NULL,
  update_date timestamp with time zone NOT NULL,
  delete_date timestamp with time zone,
  CONSTRAINT data_transfer_result_detail_pkey PRIMARY KEY (transfer_id, transfer_detail_id, customer_id)
)
WITH (OIDS=FALSE);
ALTER TABLE data_transfer_result_detail OWNER TO postgres;
COMMENT ON COLUMN data_transfer_result_detail.transfer_id IS '�����̔�';
COMMENT ON COLUMN data_transfer_result_detail.transfer_detail_id IS '����ID�ɑ΂��Ď����̔�';
COMMENT ON COLUMN data_transfer_result_detail.result_code IS '0�F����
1�F���s:�����s��
2�F���s:�a������Ȃ�
3�F���s:�a���ғs���ɂ��U�֒�~ 
4�F���s:�a�������U�ֈ˗����Ȃ�
8�F���s:�ϑ��ғs���ɂ��U�֒�~
9�F���s:���̑�';
COMMENT ON COLUMN data_transfer_result_detail.shop_id IS '�Ώۂ̓`�[�̓X��ID���Z�b�g';
COMMENT ON COLUMN data_transfer_result_detail.slip_no IS '�Ώۂ̓`�[�̓`�[NO���Z�b�g';