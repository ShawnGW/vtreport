package Tools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FTPRealseModel {
	public final String reportBath="/server212/TestReport/";
	public HashMap<String, String> InitMap(String LOT, String ID, String CPPROC, String TIME, String DEVICE, String WAFER,
			String VERSION) {
				HashMap<String, String> NameMap=new HashMap<>();
				NameMap.put("LOT", LOT);
				NameMap.put("FINID", ID);
				NameMap.put("CPPROC", CPPROC);
				NameMap.put("TIME", TIME);
				NameMap.put("DEVICE", DEVICE);
				NameMap.put("WAFER", WAFER);
				NameMap.put("VERSION", VERSION);
				return NameMap;
		// TODO Auto-generated method stub	
	}
	public HashMap<String, String> InitMap(String LOT, String ID, String CPPROC, String TIME, String DEVICE, String WAFER,String VERSION,String SLOT) {
				HashMap<String, String> NameMap=new HashMap<>();
				NameMap.put("LOT", LOT);
				NameMap.put("FINID", ID);
				NameMap.put("CPPROC", CPPROC);
				NameMap.put("TIME", TIME);
				NameMap.put("DEVICE", DEVICE);
				NameMap.put("WAFER", WAFER);
				NameMap.put("VERSION", VERSION);
				NameMap.put("SL", SLOT);
				return NameMap;
		// TODO Auto-generated method stub	
	}
	public void FTP_Release(String CustomerCode, String Device, String Lot, String CP, File file) {
		// TODO Auto-generated method stub
		File ReleaseDirectory=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Inkless_Mapping");
		if (!ReleaseDirectory.exists()) {
			ReleaseDirectory.mkdirs();
		}
		File ReleaseFile=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Inkless_Mapping/"+file.getName());
		if (ReleaseFile.exists()) {
			ReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, ReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File MailReleaseDirectory=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Inkless_Mapping");
		if (!MailReleaseDirectory.exists()) {
			MailReleaseDirectory.mkdirs();
		}
		File MailReleaseFile=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Inkless_Mapping/"+file.getName());
		if (MailReleaseFile.exists()) {
			MailReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, MailReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void FTP_Release_delete(String CustomerCode, String Device, String Lot, String CP) {
		// TODO Auto-generated method stub
		File ReleaseDirectory=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata");
		if (!ReleaseDirectory.exists()) {
			ReleaseDirectory.mkdirs();
		}
		File[] filelist=new File(("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata/")).listFiles();
		for (int i = 0; i < filelist.length; i++) {
				filelist[i].delete();
		}
		/***/
		File MailReleaseDirectory=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata");
		if (!MailReleaseDirectory.exists()) {
			MailReleaseDirectory.mkdirs();
		}
		File[] filelist2=new File(("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata/")).listFiles();
		for (int i = 0; i < filelist2.length; i++) {
				filelist2[i].delete();
		}
	}
	public void FTP_Release_FAB_Ndelete(String CustomerCode, String Device, String Lot, String CP, File file) {
		// TODO Auto-generated method stub
		File ReleaseDirectory=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata");
		if (!ReleaseDirectory.exists()) {
			ReleaseDirectory.mkdirs();
		}
		File ReleaseFile=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata/"+file.getName());
		if (ReleaseFile.exists()) {
			ReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, ReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/***/
		File MailReleaseDirectory=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata");
		if (!MailReleaseDirectory.exists()) {
			MailReleaseDirectory.mkdirs();
		}
		File MailReleaseFile=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata/"+file.getName());
		if (MailReleaseFile.exists()) {
			MailReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, MailReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void FTP_Release_FAB(String CustomerCode, String Device, String Lot, String CP, File file) {
		// TODO Auto-generated method stub
		File ReleaseDirectory=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata");
		if (!ReleaseDirectory.exists()) {
			ReleaseDirectory.mkdirs();
		}
		File[] filelist=new File(("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata/")).listFiles();
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].getName().contains(file.getName().split("_")[0])) {
				filelist[i].delete();
			}
		}
		File ReleaseFile=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata/"+file.getName());
		if (ReleaseFile.exists()) {
			ReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, ReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/***/
		File MailReleaseDirectory=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata");
		if (!MailReleaseDirectory.exists()) {
			MailReleaseDirectory.mkdirs();
		}
		File MailReleaseFile=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Rawdata/"+file.getName());
		if (MailReleaseFile.exists()) {
			MailReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, MailReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void FTP_Release_FAB_Mapping(String CustomerCode, String Device, String Lot, String CP, File file) {
		// TODO Auto-generated method stub
		File ReleaseDirectory=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping");
		if (!ReleaseDirectory.exists()) {
			ReleaseDirectory.mkdirs();
		}
		File[] filelist=new File(("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping/")).listFiles();
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].getName().contains(file.getName().split("_")[0])) {
				filelist[i].delete();
			}
		}
		File ReleaseFile=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping/"+file.getName());
		if (ReleaseFile.exists()) {
			ReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, ReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/***/
		File MailReleaseDirectory=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping");
		if (!MailReleaseDirectory.exists()) {
			MailReleaseDirectory.mkdirs();
		}
		File MailReleaseFile=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Fab_Mapping/"+file.getName());
		if (MailReleaseFile.exists()) {
			MailReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, MailReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//CP_Summary Merged_Mapping
	public void FTP_Release_CP(String CustomerCode, String Device, String Lot, String CP, File file) {
		// TODO Auto-generated method stub
		File ReleaseDirectory=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/CP_Summary");
		if (!ReleaseDirectory.exists()) {
			ReleaseDirectory.mkdirs();
		}
		File[] filelist=new File(("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/CP_Summary/")).listFiles();
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].getName().contains(file.getName().split("_")[0])) {
				filelist[i].delete();
			}
		}
		File ReleaseFile=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/CP_Summary/"+file.getName());
		if (ReleaseFile.exists()) {
			ReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, ReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/***/
		File MailReleaseDirectory=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/CP_Summary");
		if (!MailReleaseDirectory.exists()) {
			MailReleaseDirectory.mkdirs();
		}
		File MailReleaseFile=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/CP_Summary/"+file.getName());
		if (MailReleaseFile.exists()) {
			MailReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, MailReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void FTP_Release_Merge(String CustomerCode, String Device, String Lot, String CP, File file) {
		// TODO Auto-generated method stub
		File ReleaseDirectory=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Merged_Mapping");
		if (!ReleaseDirectory.exists()) {
			ReleaseDirectory.mkdirs();
		}
		File[] filelist=new File(("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Merged_Mapping/")).listFiles();
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].getName().contains(file.getName().split("_")[0])) {
				filelist[i].delete();
			}
		}
		File ReleaseFile=new File("/server212/UploadData/TestReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Merged_Mapping/"+file.getName());
		if (ReleaseFile.exists()) {
			ReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, ReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/***/
		File MailReleaseDirectory=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Merged_Mapping");
		if (!MailReleaseDirectory.exists()) {
			MailReleaseDirectory.mkdirs();
		}
		File MailReleaseFile=new File("/server212/UploadData/MailReportRelease/"+CustomerCode+"/"+Device+"/"+Lot+"/"+CP+"/Merged_Mapping/"+file.getName());
		if (MailReleaseFile.exists()) {
			MailReleaseFile.delete();
		}
		try {
			FilesCopy.copyfile(file, MailReleaseDirectory.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
