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
 * ���C������
 *
 * @author katagiri
 */
public class SosiaPos {

	public static final String VERSION = "0.3.8";
	/**
	 * �A�C�R���̃p�X
	 */
	public static final String ICON_PATH = "/images/common/icon/icon16.gif";

	/**
	 * ��d�N���`�F�b�N�Ɏg���|�[�g
	 */
	private static int PORT = 3776;
	/**
	 * ��d�N���`�F�b�N�Ɏg���\�P�b�g
	 */
	private static ServerSocket sock = null;

	/**
	 * ���C���t���[��
	 */
	private static MainFrame mf = null;

	/**
	 * �N�����b�Z�[�W�_�C�A���O
	 */
	public static WaitDialog waitDialog = null;

	/**
	 * �R���X�g���N�^
	 */
	public SosiaPos() {
	}

	/**
	 * ���C���t���[�����Z�b�g����B
	 *
	 * @param mf ���C���t���[��
	 */
	public static void setMainFrameTitle(String s) {
		if (SosiaPos.mf != null) {
			SosiaPos.mf.setMessageTitle(s);
		}
	}

	/**
	 * ���C������
	 *
	 * @param args ����
	 */
	public static void main(String args[]) throws Exception {
		initPreBoot();

		// ��������`�F�b�N����B
		// �E�B���h�E���P���o���Ȃ��� UAC ���E�B���h�E�������Ԃŏo�Ă��Ă��܂��̂ŁA�����ōs���B
		if (MigrationUtils.supportCheck()) {
			MigrationUtils.checkEnvironment();
		}

		LoginDialog login = newLoginDialog();
		login.setVisible(true);

		//temp�̍폜
		TempDeleteThread td = new TempDeleteThread();
		td.start();

		//Windows�V���b�g�_�E�����̏���
		Runtime.getRuntime().addShutdownHook(new ShutdownThread());
	}

	/**
	 * boot �O�̏����������B
	 */
	public static void initPreBoot() {
		SystemInfo.setBootOS(System.getProperty("os.name"));

		try {
			MessageUtil.readMassageFile();

			//��d�N���`�F�b�N
			/*
			if (!SosiaPos.checkSingleProcess()) {
				System.exit(0);
			}
			*/
			
			//Look&Feel��ݒ�
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel("net.sourceforge.napkinlaf.NapkinLookAndFeel");
//			SynthLookAndFeel synth = new SynthLookAndFeel();
//			synth.load(SynthFrame.class.getResourceAsStream("demo.xml"), SynthFrame.class);
//			UIManager.setLookAndFeel(synth);
		} catch (Exception e) {
			System.out.println(UIManager.getSystemLookAndFeelClassName());
		}

		SystemInfo.getLogger().log(Level.INFO, "SOSIA POS �N����...");

		// �N�������b�Z�[�W�\��
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

			// xulrunner�_�E�����[�h
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

					File file = new File(fileName); // �ۑ���
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
	 * ��d�N���`�F�b�N���s���B
	 *
	 * @return ��d�N���ł͖����ꍇtrue
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
	 * ��d�N�����Ă��邩���擾����B
	 *
	 * @return ��d�N�����Ă���ꍇtrue
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
                                        // nami start add 20170828 #24239 [GB���Ή�][gb]SB�p�X���[�h�ύX���ɃA���[�g���o��
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
		//�I��
		SystemInfo.closeConnection();
		SystemInfo.getLogger().log(Level.INFO, "�I��");
		System.gc();

		// 2013-10-31 mahara: �����A�������g�������I���������Ă��̃R�[�h�����݂��Ă���B
		// ���̃v���O�������ƑS�Ă̋N������ javaw.exe �v���Z�X�� kill ���Ă��܂��̂ŁAEclipse �� kill ����Ă��܂��B
		// �������g�������I������悤�ɕύX�B
		// �����A�o�b�N�O���E���h�œ������Ă���X���b�h���~���Ă��Ȃ��Ȃǂ̗��R�ŁAmain() ���I�����Ă��v���Z�X���I�����Ȃ���肪�������̂ł͂Ȃ����B
		try {
			Long pid;

			// �������g�̃v���Z�XID���擾����B
			{
				RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
				String vmName = bean.getName();
				pid = Long.valueOf(vmName.split("@")[0]);
			}

			// �N�����Ă���u�S�Ă� SPOS�v�� kill �������ꍇ�͂�����̃R�[�h��L��������B
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
	 * Zip �t�@�C���̉𓀃��\�b�h
	 *
	 * @param file �Ώۂ�ZIP�t�@�C��
	 * @throws ZipException
	 * @throws IOException
	 */
	private static void unzip(File file) throws ZipException, IOException {

		// �Ώۂ�Zip�t�@�C���Ɠ����̃f�B���N�g�����쐬����B
		String fileName = file.getName();
		int exindex = fileName.lastIndexOf(".");
		String baseDirName = fileName.substring(0, exindex);
		File baseDir = new File(file.getParent(), baseDirName);
		if (!baseDir.mkdir()) {
			throw new FileNotFoundException(baseDir + "�̐����Ɏ��s���܂����B");
		}

		// Zip �t�@�C������ ZipEntry ��������o���A�t�@�C���ɕۑ����Ă����B
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry ze = entries.nextElement();

			// �o�͐�t�@�C��
			File outFile = new File(baseDir, ze.getName());
			if (ze.isDirectory()) {
				// ZipEntry ���f�B���N�g���̏ꍇ�̓f�B���N�g�����쐬�B
				outFile.mkdirs();
			} else {

				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;
				try {
					// ZipFile ���� �Ώ�ZipEntry �� InputStream �����o���B
					InputStream is = zipFile.getInputStream(ze);
					// �����悭�ǂݍ��ނ��� BufferedInputStream �Ń��b�v����B
					bis = new BufferedInputStream(is);

					if (!outFile.getParentFile().exists()) {
						// �o�͐�t�@�C���̕ۑ���f�B���N�g�������݂��Ȃ��ꍇ�́A
						// �f�B���N�g�����쐬���Ă����B
						outFile.getParentFile().mkdirs();
					}

					// �o�͐� OutputStream ���쐬�B
					bos
							= new BufferedOutputStream(new FileOutputStream(
											outFile));

					// ���̓X�g���[������ǂݍ��݁A�o�̓X�g���[���֏������ށB
					int ava;
					while ((ava = bis.available()) > 0) {
						byte[] bs = new byte[ava];
						// ����
						bis.read(bs);

						// �o��
						bos.write(bs);
					}
				} catch (FileNotFoundException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} finally {
					try {
						if (bis != null) // �X�g���[���͕K�� close ����B
						{
							bis.close();
						}
					} catch (IOException e) {
					}
					try {
						if (bos != null) // �X�g���[���͕K�� close ����B
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
	 * ���݂̃��C���t���[����Ԃ��B�e�X�g�p
	 */
	public static MainFrame getMainFrame() {
		return mf;
	}
}
