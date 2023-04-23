/*
 * ReportGeneratorLogic.java
 *
 * Created on 29 August 2008, 11:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;


import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager; 
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import java.text.SimpleDateFormat;
import java.io.InputStream;
import java.util.logging.Level;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.report.util.ReportManager;



/**
 *
 * @author trino
 */
public class ReportGeneratorLogic
{
    
    protected String TEMPLATE_PATH = "/reports/";
    
    //Report file types
    public static final int REPORT_FILE_TYPE_XML = 1;
    public static final int REPORT_FILE_TYPE_JASPER = 2;
    
    //Export to file types 
    public static final int EXPORT_FILE_PDF = 1;
    public static final int EXPORT_FILE_XLS = 2;
    public static final int EXPORT_FILE_HTML = 3;
    public static final int EXPORT_FILE_PDF2 = 4;
    
    
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_DATA_NOTHNIG = 1;
    public static final int RESULT_ERROR   = 2;

    
    /**
     * Creates a new instance of ReportGeneratorLogic
     */
    public ReportGeneratorLogic()
    {
    }
        
    protected JasperReport loadReport( String filename, int filetype ) throws Exception
    {        
        InputStream report = null;
        JasperReport jasperReport = null;
        
        report = ReportGeneratorLogic.class.getResourceAsStream( this.TEMPLATE_PATH + filename );

        if( filetype == REPORT_FILE_TYPE_JASPER )
        {
            jasperReport = (JasperReport)JRLoader.loadObject( report );
        }
        else if( filetype == REPORT_FILE_TYPE_XML )
        {
            jasperReport = JasperCompileManager.compileReport(report);
        }
        else
        {
            throw new Exception( "Invalid file type input." );
        }
        return jasperReport;
    }

    protected boolean generatePDFFile( JasperPrint jasperPrint, String exportFile)
    {
            try {
                
                JasperExportManager.exportReportToPdfFile(jasperPrint, exportFile);
                
            } catch( Exception e ) {

                SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
                return false;
            }

            return true;
    }

    protected boolean generatePDFFile( String outputFileName, JasperPrint jasperPrint, String[] strExportFile)
    {
            String exportFile;

            try
            {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhms");
                    String dateString = format.format(new java.util.Date());

                    exportFile = ReportManager.getExportPath() + outputFileName + dateString + ".pdf";

                    JasperExportManager.exportReportToPdfFile(jasperPrint, exportFile);
            }
            catch( Exception e )
            {
                    SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
                    return false;
            }

            strExportFile[0] = exportFile;

            return true;
    }

    protected boolean generateAndPreviewPDFFile( String outputFileName, JasperPrint jasperPrint )
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhms");
	    String dateString = format.format(new java.util.Date());

            String exportFile = ReportManager.getExportPath() + outputFileName + dateString + ".pdf";
             
            JasperExportManager.exportReportToPdfFile(jasperPrint, exportFile);
            
            Runtime runtime = Runtime.getRuntime();
            //runtime.exec(ReportManager.getPdfViewerPath() + " " + exportFile);
            runtime.exec("rundll32 url.dll,FileProtocolHandler " + exportFile);
        }
        catch( Exception e )
        {
             SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
             return false;
        }

        return true;
    }

	protected boolean generateXLSFile( String outputFileName, JasperPrint jasperPrint, String[] strExportFile)
	{
		String exportFile;

		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhms");
			String dateString = format.format(new java.util.Date());

			exportFile = ReportManager.getExportPath() + outputFileName + dateString + ".xls";
			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, exportFile);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, java.lang.Boolean.FALSE);
            exporter.exportReport();
		}
		catch( Exception e )
		{
			SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
			return false;
		}
		
		strExportFile[0] = exportFile;

		return true;
	}

    protected boolean generateAndPreviewXLSFile( String outputFileName, JasperPrint jasperPrint )
    {
        try
        {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhms");
			String dateString = format.format(new java.util.Date());

			String exportFile = ReportManager.getExportPath() + outputFileName + dateString + ".xls";
			JRXlsExporter exporter = new JRXlsExporter();

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, exportFile);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, java.lang.Boolean.FALSE);
            exporter.exportReport();

			Runtime runtime = Runtime.getRuntime();
			//runtime.exec(ReportManager.getPdfViewerPath() + " " + exportFile);
                        //Start edit 20131022 lvut (runtime report excel)
                        runtime.exec("rundll32 url.dll,FileProtocolHandler " + exportFile);
                        //End edit 20131022 lvut (runtime report excel)
        }
        catch( Exception e )
        {
             SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
             return false;
        }

        return true;
    }    


}
