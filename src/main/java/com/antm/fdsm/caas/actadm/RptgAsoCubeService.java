package com.antm.fdsm.caas.actadm;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.antm.fdsm.orcl.oac.AnalyticCloudPlatform;
import com.antm.fdsm.orcl.oac.EssbaseCube;
import com.antm.fdsm.orcl.oac.EssbaseServer;

public class RptgAsoCubeService {
	
	private EssbaseServer server = AnalyticCloudPlatform.getEssbaseServer("fdsm-dev-oac01.anthem.com");
	private EssbaseCube rptgAso01 = server.getApplication(ServiceDefs.RPTG_NAME_ASO_01).getCube(ServiceDefs.RPTG_NAME_ASO_01);
	
	
	public void clearAllData() {
		rptgAso01.clear();
	}
	
	public void loadHistory() {	
		rptgAso01.loadFilesInDirectory(ServiceDefs.DIRECTORY_HOME + ServiceDefs.DIRECTORY_PROJECT + "/" + ServiceDefs.DIRECTORY_DATA + "/" + ServiceDefs.DIRECTORY_HISTORY);
	}
	
	public void loadCurrentPeriodIncremental() {
		try {
			Files.newDirectoryStream(Paths.get(ServiceDefs.DIRECTORY_HOME + ServiceDefs.DIRECTORY_PROJECT + "/" + ServiceDefs.DIRECTORY_HISTORY), path -> path.toFile().isFile())
				.forEach(f -> {
					if (f.toString().endsWith(".txt")) {
						rptgAso01.load( (loadFile, ruleFile) -> {
							loadFile.localPath(f.toString());
							ruleFile.aiSourceFile(f.toString());
						});
					}
				}
			);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadCurrentPeriodBase() {
		rptgAso01.loadFilesInDirectory(ServiceDefs.DIRECTORY_HOME + ServiceDefs.DIRECTORY_PROJECT + "/" + ServiceDefs.DIRECTORY_DATA + "/" + ServiceDefs.DIRECTORY_BASE);
	}
	
	public void loadCurrentPeriodWithPartialClear() {
		
	}
	
	public void updateTime() {
		
	}
	
	
}
