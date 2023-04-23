/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.OriginalReportBean;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * オリジナル帳票３ ビジネスロジック
 * 
 * @author user
 */
public class OriginalReport03Logic extends OriginalReportCommonLogic {
    
    /**
     * コンストラクタ
     * 
     * @param bean 画面入力内容 
     */
    public OriginalReport03Logic(OriginalReportBean bean) {
        super(bean);
    }

    /**
     * 帳票名の取得
     * 
     * @return 帳票名
     */
    @Override
    protected String getReportName() {
        return "デザインランキング";
    }

    /**
     * 帳票出力メイン処理
     * 
     * @param connection DBコネクション
     * @return 処理結果
     * @throws Exception 汎用例外 
     */
    @Override
    protected Result process(ConnectionWrapper connection) throws Exception {

        // ヘッダー書き込み
        this.writeHeader();
        // 明細部書き込み
        this.writeMain(connection);

        return Result.PRINT;
    }
    
    /**
     * 帳票ヘッダー部書き込み処理
     */
    protected void writeHeader() {
        super.writeVerticalItems(2, 3
                , WriteItem.SHOP_NAME
                , WriteItem.STAFF_NAME
                , WriteItem.TARGET_RANGE
                , WriteItem.TAX);
    }
    
    /**
     * 帳票明細部書き込み処理
     * 
     * @param conn DBコネクション
     * @throws Exception 汎用例外
     */
    protected void writeMain(ConnectionWrapper conn) throws Exception {

        final int INITIAL_LINE = 9;
        List<OriginalListBean> results = this.getDesignRankingList(conn);

        if (!results.isEmpty()) {
            
            // 取得結果を帳票に書き込む
            int row = INITIAL_LINE;
        
            this.getReport().insertRow(row, results.size() - 1);

            for (OriginalListBean result : results) {
                if (row != INITIAL_LINE) {
                    this.getReport().mergeCells(1, row, 4, row);
                }
                super.writeValue(1, row, result.getName());
                super.writeValue(5, row, result.getNum());
                row++;
            }
        }
    }
    
    /**
     * 明細部に表示する情報を取得します。
     * 
     * @param conn DBコネクション
     * @return デザインランキング情報
     * @throws Exception 汎用例外
     */
    private List<OriginalListBean> getDesignRankingList(ConnectionWrapper conn) throws Exception {
        
        StringBuilder query = new StringBuilder();
        query.append(" SELECT");
        query.append("   SUM(t2.product_num) AS technic_num");
        query.append("   , m1.technic_name");
        query.append(" FROM");
        query.append("   data_sales AS t1");
        query.append(" INNER JOIN");
        query.append("   data_sales_detail AS t2");
        query.append("   ON (");
        query.append("     t1.shop_id = t2.shop_id");
        query.append("     AND t1.slip_no = t2.slip_no");
        query.append("     AND t2.product_division = 1");
        query.append("   )");
        query.append(" INNER JOIN");
        query.append("   mst_technic as m1");
        query.append("   ON (t2.product_id = m1.technic_id)");
        query.append(" WHERE");
        query.append("   t2.delete_date IS NULL");
        //22:オプション,23:オフ,27:割引,28:モデル,30:無料お直し　を対象外とする
        query.append("   AND m1.technic_class_id not in (22,23,27,28,30)");
        
        if (super.getBean().getShopId() != null) {
            query.append("   AND t1.shop_id = ").append(SQLUtil.convertForSQL(super.getBean().getShopId()));
        }
        if (super.getBean().getStaffId() != null) {
            query.append("   AND t2.staff_id = ").append(SQLUtil.convertForSQL(super.getBean().getStaffId()));
        }
        if (super.getBean().getTargetDateFrom() != null) {
            query.append("   AND t1.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        }
        if (super.getBean().getTargetDateTo() != null) {
            query.append("   AND t1.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        }
        
        query.append(" GROUP BY");
        query.append("   m1.technic_name ");
        query.append(" ORDER BY");
        query.append("   technic_num DESC, technic_name");

        //SQL実行
        ResultSetWrapper rs = conn.executeQuery(query.toString());
        rs.beforeFirst();

        List<OriginalListBean> results = new ArrayList<>();
        while (rs.next()) {
            OriginalListBean row = new OriginalListBean();
            row.setName(rs.getString("technic_name"));
            row.setNum(rs.getLong("technic_num"));
            results.add(row);
        }
        return results;
    }

}
