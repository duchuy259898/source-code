-- ===================================================================
-- $Id: 2020.03.23.data_transfer_result.txt
-- ===================================================================
--
-- �������E���ʏ��w�b�_
--
-- ===================================================================

CREATE TABLE data_transfer_result
(
  transfer_id integer NOT NULL, -- �����̔�
  target_month timestamp without time zone NOT NULL, -- data_monthly_batch_log.targer_month
  transfer_year character varying(4) NOT NULL, -- �������
  transfer_month character varying(2) NOT NULL, -- 01�`12������
  transfer_date character varying(2) NOT NULL, -- 14��27������
  total_num integer NOT NULL,
  total_amount integer NOT NULL,
  status integer NOT NULL, -- 1:�����f�[�^�o�^�@2:���ʃf�[�^�捞��
  insert_date timestamp with time zone NOT NULL,
  update_date timestamp with time zone NOT NULL,
  delete_date timestamp with time zone,
  CONSTRAINT data_transfer_result_pkey PRIMARY KEY (transfer_id)
)
WITH (OIDS=FALSE);
ALTER TABLE data_transfer_result OWNER TO postgres;
COMMENT ON COLUMN data_transfer_result.transfer_id IS '�����̔�';
COMMENT ON COLUMN data_transfer_result.target_month IS 'data_monthly_batch_log.targer_month';
COMMENT ON COLUMN data_transfer_result.transfer_year IS '�������';
COMMENT ON COLUMN data_transfer_result.transfer_month IS '01�`12������';
COMMENT ON COLUMN data_transfer_result.transfer_date IS '14��27������';
COMMENT ON COLUMN data_transfer_result.status IS '1:�����f�[�^�o�^�@2:���ʃf�[�^�捞��';
