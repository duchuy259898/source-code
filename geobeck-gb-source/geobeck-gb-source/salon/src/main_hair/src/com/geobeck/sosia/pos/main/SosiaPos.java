/*
 * SosiaPos.java
 *
 * Created on 2006/05/01, 21:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.main;

import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;

import com.geobeck.swing.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.login.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.Reconnection;
import com.geobeck.sql.ReconnectionHandler;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import java.util.zip.*;

/**
 * メイン処理
 *
 * @author katagiri
 */
public class SosiaPos {

	public static final String VERSION = "0.3.8";
	/**
	 * アイコンのパス
	 */
	public static final String ICON_PATH = "/images/common/icon/icon16.gif";

	/**
	 * 二重起動チェックに使うポート
	 */
	private static int PORT = 3776;
	/**
	 * 二重起動チェックに使うソケット
	 */
	private static ServerSocket sock = null;

	/**
	 * メインフレーム
	 */
	private static MainFrame mf = null;

	/**
	 * 起動メッセージダイアログ
	 */
	public static WaitDialog waitDialog = null;

	/**
	 * コンストラクタ
	 */
	public SosiaPos() {
	}

	/**
	 * メインフレームをセットする。
	 *
	 * @param mf メインフレーム
	 */
	public static void setMainFrameTitle(String s) {
		if (SosiaPos.mf != null) {
			SosiaPos.mf.setMessageTitle(s);
		}
	}

	/**
	 * メイン処理
	 *
	 * @param args 引数
	 */
	public static void main(String args[]) throws Exception {
		initPreBoot();

		// 動作環境をチェックする。
		// ウィンドウを１つも出さないと UAC がウィンドウを閉じた状態で出てきてしまうので、ここで行う。
		if (MigrationUtils.supportCheck()) {
			MigrationUtils.checkEnvironment();
		}

		LoginDialog login = newLoginDialog();
		login.setVisible(true);

		//tempの削除
		TempDeleteThread td = new TempDeleteThread();
		td.start();

		//Windowsシャットダウン時の処理
		Runtime.getRuntime().addShutdownHook(new ShutdownThread());
	}

	/**
	 * boot 前の初期化処理。
	 */
	public static void initPreBoot() {
		SystemInfo.setBootOS(System.getProperty("os.name"));

		try {
			MessageUtil.readMassageFile();

			//二重起動チェック
			/*
			if (!SosiaPos.checkSingleProcess()) {
				System.exit(0);
			}
			*/
			
			//Look&Feelを設定
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel("net.sourceforge.napkinlaf.NapkinLookAndFeel");
//			SynthLookAndFeel synth = new SynthLookAndFeel();
//			synth.load(SynthFrame.class.getResourceAsStream("demo.xml"), SynthFrame.class);
//			UIManager.setLookAndFeel(synth);
		} catch (Exception e) {
			System.out.println(UIManager.getSystemLookAndFeelClassName());
		}

		SystemInfo.getLogger().log(Level.INFO, "SOSIA POS 起動中...");

		// 起動中メッセージ表示
		waitDialog = new WaitDialog();
		waitDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		waitDialog.setLocationRelativeTo(null);
		waitDialog.setVisible(true);

		Reconnection.setHandler(new ReconnectionHandler() {

			@Override
			public void setTitle(String title) {
				SosiaPos.setMainFrameTitle(title);
			}

		});

		if (!SystemInfo.getBaseConnection().getUrl().startsWith("jdbc:postgresql://127.0.0.1:5432")) {

			String fileName = SystemInfo.getLogRoot() + "/xulrunner.zip";
			String dirName = SystemInfo.getLogRoot() + "/xulrunner";

			// xulrunnerダウンロード
			try {
				if (!(new File(dirName)).exists()) {

					URL url = null;
					if (SystemInfo.isWindows()) {
						url = new URL("http://sv2.sosia.jp/hair/xulrunner.zip");
					} else {
						url = new URL("http://sv2.sosia.jp/hair/mac/xulrunner.zip");
					}

					URLConnection conn = url.openConnection();
					InputStream in = conn.getInputStream();

					File file = new File(fileName); // 保存先
					FileOutputStream out = new FileOutputStream(file, false);
					byte[] bytes = new byte[512];
					while (true) {
						int ret = in.read(bytes);
						if (ret == -1) {
							break;
						}
						out.write(bytes, 0, ret);
					}
					out.close();
					in.close();

					File f = new File(fileName);
					unzip(f);
					f.delete();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			System.setProperty("user.dir", dirName);
			System.setProperty("mozswing.xulrunner.home", dirName);
		}
	}

	/**
	 * 二重起動チェックを行う。
	 *
	 * @return 二重起動では無い場合true
	 */
	private static boolean checkSingleProcess() {

		if (isSingleProcess()) {
			return true;
		} else {
			MessageDialog.showMessageDialog(null,
					MessageUtil.getMessage(1),
					"Sosia POS", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	/**
	 * 二重起動しているかを取得する。
	 *
	 * @return 二重起動している場合true
	 */
	public static boolean isSingleProcess() {
		try {
			sock = new ServerSocket(SosiaPos.PORT);
		} catch (BindException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static LoginDialog newLoginDialog() {
		final LoginDialog login = new LoginDialog(SosiaPos.VERSION, null);

		login.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				SosiaPos.closeApplication();
			}
		});

		login.addComponentListener(new java.awt.event.ComponentAdapter() {

			@Override
			public void componentHidden(java.awt.event.ComponentEvent evt) {
				if (login.isLogin()) {
					SosiaPos.mf = new MainFrame();
					SosiaPos.mf.setLocationRelativeTo(null);
					SosiaPos.mf.setVisible(true);
                                        // nami start add 20170828 #24239 [GB内対応][gb]SBパスワード変更時にアラートを出す
                                        SosiaPos.mf.checkUserMediaIsSuspended();
                                        // nami end add 20170828 #24239
				} else {
					SosiaPos.closeApplication();
				}
			}
		});

		return login;
	}

	public static void closeApplication() {
		SosiaPos.mf = null;

		Login.logout();
		//終了
		SystemInfo.closeConnection();
		SystemInfo.getLogger().log(Level.INFO, "終了");
		System.gc();

		// 2013-10-31 mahara: 多分、自分自身を強制終了したくてこのコードが存在している。
		// 元のプログラムだと全ての起動中の javaw.exe プロセスを kill してしまうので、Eclipse も kill されてしまう。
		// 自分自身だけを終了するように変更。
		// 多分、バックグラウンドで動かしているスレッドを停止していないなどの理由で、main() を終了してもプロセスが終了しない問題があったのではないか。
		try {
			Long pid;

			// 自分自身のプロセスIDを取得する。
			{
				RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
				String vmName = bean.getName();
				pid = Long.valueOf(vmName.split("@")[0]);
			}

			// 起動している「全ての SPOS」を kill したい場合はこちらのコードを有効化する。
//			{
//				String cmd = "wmic process get commandLine, ProcessId";
//
//				Process process = Runtime.getRuntime().exec(cmd);
//				BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//				for (;;) {
//					String s = in.readLine();
//
//					if (s == null) {
//						break;
//					}
//
//					if (s.indexOf("java") == -1) {
//						continue;
//					}
//					if (s.indexOf("SosiaPos") == -1) {
//						continue;
//					}
//					Pattern p = Pattern.compile("(\\d+)\\s*$");
//					Matcher matcher = p.matcher(s);
//
//					if (!matcher.find()) {
//						continue;
//					}
//
//					pid = Long.valueOf(matcher.group(1));
//				}
//			}
//
			if (pid != null) {
				Process process = Runtime.getRuntime().exec("taskkill /F /PID " + pid);
				process.waitFor();
			}
		} catch (Exception e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		System.exit(0);
	}

	/**
	 * Zip ファイルの解凍メソッド
	 *
	 * @param file 対象のZIPファイル
	 * @throws ZipException
	 * @throws IOException
	 */
	private static void unzip(File file) throws ZipException, IOException {

		// 対象のZipファイルと同名のディレクトリを作成する。
		String fileName = file.getName();
		int exindex = fileName.lastIndexOf(".");
		String baseDirName = fileName.substring(0, exindex);
		File baseDir = new File(file.getParent(), baseDirName);
		if (!baseDir.mkdir()) {
			throw new FileNotFoundException(baseDir + "の生成に失敗しました。");
		}

		// Zip ファイルから ZipEntry を一つずつ取り出し、ファイルに保存していく。
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry ze = entries.nextElement();

			// 出力先ファイル
			File outFile = new File(baseDir, ze.getName());
			if (ze.isDirectory()) {
				// ZipEntry がディレクトリの場合はディレクトリを作成。
				outFile.mkdirs();
			} else {

				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;
				try {
					// ZipFile から 対象ZipEntry の InputStream を取り出す。
					InputStream is = zipFile.getInputStream(ze);
					// 効率よく読み込むため BufferedInputStream でラップする。
					bis = new BufferedInputStream(is);

					if (!outFile.getParentFile().exists()) {
						// 出力先ファイルの保存先ディレクトリが存在しない場合は、
						// ディレクトリを作成しておく。
						outFile.getParentFile().mkdirs();
					}

					// 出力先 OutputStream を作成。
					bos
							= new BufferedOutputStream(new FileOutputStream(
											outFile));

					// 入力ストリームから読み込み、出力ストリームへ書き込む。
					int ava;
					while ((ava = bis.available()) > 0) {
						byte[] bs = new byte[ava];
						// 入力
						bis.read(bs);

						// 出力
						bos.write(bs);
					}
				} catch (FileNotFoundException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} finally {
					try {
						if (bis != null) // ストリームは必ず close する。
						{
							bis.close();
						}
					} catch (IOException e) {
					}
					try {
						if (bos != null) // ストリームは必ず close する。
						{
							bos.close();
						}
					} catch (IOException e) {
					}
				}
			}
		}
		zipFile.close();
	}

	/**
	 * 現在のメインフレームを返す。テスト用
	 */
	public static MainFrame getMainFrame() {
		return mf;
	}
}
