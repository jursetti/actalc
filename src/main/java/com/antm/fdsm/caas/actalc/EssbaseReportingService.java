package com.antm.fdsm.caas.actalc;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import com.antm.fdsm.orcl.oac.ExportFile;
import com.antm.fdsm.orcl.oac.EssbaseApplication;
import com.antm.fdsm.orcl.oac.EssbaseCube;
import com.antm.fdsm.orcl.oac.EssbaseServer;
import com.antm.fdsm.orcl.oac.services.EssbaseService;
import com.antm.fdsm.orcl.utils.GlobalOptions;
import com.antm.fdsm.orcl.utils.Helpers;

public class EssbaseReportingService {

	private EssbaseService essbase;
	private EssbaseServer server;
	private EssbaseApplication rptgApp;
	private EssbaseCube rptgCube;

	public EssbaseReportingService(EssbaseService essbaseService) {
		essbase = essbaseService;
		server = new EssbaseServer(essbase);
		rptgApp = server.getApplication(essbase, Def.RPTG_NAME);
		rptgCube = rptgApp.getCube(Def.RPTG_NAME);
	}

	public EssbaseReportingService agg() throws InterruptedException, ExecutionException {
		rptgCube.aggregate().get();
		return this;
	}

	public EssbaseReportingService clearAllData() throws InterruptedException, ExecutionException {
		rptgCube.clear().get();
		return this ;
	};
	
	public EssbaseReportingService loadAlloc() throws InterruptedException, ExecutionException {
		List<String> alternateStructures = Arrays.asList("Alloc_0", "Alloc_1", "Alloc_2", "Alloc_3", "Alloc_4", "Alloc_5");
		alternateStructures.stream().forEach( structure -> loadDivAllocFile(rptgCube, GlobalOptions.HOME,structure));
		return this;
	}
	
	

	public EssbaseReportingService loadHC() {
		try {
			
			rptgCube.load((loadFile, ruleFile) -> {
				loadFile.localPath(Def.IN + "/tstalc_headcount_alloc_h1.txt");
				ruleFile.aiSourceFile(Def.IN + "/tstalc_headcount_alloc_h1.txt")
				.addVirtualColumn("Scenarios", "Actual")
				.ignoreFileColumn("BegBalance")
				;
			}).get();
	
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	public EssbaseReportingService loadCaremoreQiAlloc() throws InterruptedException, ExecutionException {
		List<String> alternateStructuresqi = Arrays.asList("Alloc_0_qi_reclass", "Alloc_1_qi_reclass", "Alloc_2_qi_reclass", "Alloc_3_qi_reclass", "Alloc_4_qi_reclass", "Alloc_5_qi_reclass");
		alternateStructuresqi.stream().forEach( structureqi -> loadQiDivAllocFile(rptgCube, GlobalOptions.HOME,structureqi));
		return this;
	}

	public EssbaseReportingService loadDBGAlloc() {
		try {
			
			rptgCube.load((loadFile, ruleFile) -> {
				loadFile.localPath(Def.IN + "/h_actalc_99DBG_QI_ar_19.txt");
				ruleFile.aiSourceFile(Def.IN + "/h_actalc_99DBG_QI_ar_19.txt")
				.addVirtualColumn("Scenarios", "Actual")
				;
			}).get();
	
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	
	
	
//	public EssbaseReportingService loadDBGAlloc() throws InterruptedException, ExecutionException {
//		List<String> alternateStructuresdbg = Arrays.asList("Alloc_DBG");
//		alternateStructuresdbg.stream().forEach( structuredbg -> loadDBGDivAllocFile(rptgCube, GlobalOptions.HOME,structuredbg));
//		return this;
//	}
	
//new version file comes from cloud actqi
	public EssbaseReportingService loadPSTQIAlloc() {
		try {
			
			rptgCube.load((loadFile, ruleFile) -> {
				loadFile.localPath(Def.IN + "/actqi_4actalc.txt");
				ruleFile.aiSourceFile(Def.IN + "/actqi_4actalc.txt")
				.ignoreFileColumn("Quality Improvement")
				.addVirtualColumn("Fixed Pool", "F00")
				.addVirtualColumn("Accounts", "QI Alloc Exp")
				.ignoreFileColumn("BegBalance")
				;
			}).get();
	
	
	
//old version comes from on prem file	
/*	public EssbaseReportingService loadPSTQIAlloc() {
		try {
			
			rptgCube.load((loadFile, ruleFile) -> {
				loadFile.localPath(Def.IN + "/par_pstqi2_4actalc.txt");
				ruleFile.aiSourceFile(Def.IN + "/par_pstqi2_4actalc.txt")
				.ignoreFileColumn("Quality Improvement")
				.addVirtualColumn("Fixed Pool", "F00")
				.addVirtualColumn("Accounts", "QI Alloc Exp")
				.ignoreFileColumn("BegBalance")
				;
			}).get();
*/	
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}	
	
	
	public EssbaseReportingService loadHistory()  {
		try {
			
			rptgCube.load((loadFile, ruleFile) -> {
				loadFile.localPath(Def.IN + "/history/h_actalc_ar_" + (Def.YR2D-2) + ".txt");
				ruleFile.aiSourceFile(Def.IN + "/history/h_actalc_ar_" + (Def.YR2D-2) + ".txt")
//				.ignoreFileColumn("Accounts")
//				.addVirtualColumn("Segments", "SumProduct Default")
//				.ignoreFileColumn("Fixed Pool")
				.ignoreFileColumn("BegBalance")
				;
			}).get();
	
			rptgCube.load((loadFile, ruleFile) -> {
				loadFile.localPath(Def.IN + "/history/h_actalc_ar_" + (Def.YR2D-1) + ".txt");
				ruleFile.aiSourceFile(Def.IN + "/history/h_actalc_ar_" + (Def.YR2D-1) + ".txt")
//				.ignoreFileColumn("Accounts")
//				.addVirtualColumn("Segments", "SumProduct Default")
//				.ignoreFileColumn("Fixed Pool")
				.ignoreFileColumn("BegBalance")
				;
			}).get();			

			rptgCube.load((loadFile, ruleFile) -> {
				loadFile.localPath(Def.IN + "/history/h_actalc_wp_" + (Def.YR2D) + ".txt");
				ruleFile.aiSourceFile(Def.IN + "/history/h_actalc_wp_" + (Def.YR2D) + ".txt")
//				.ignoreFileColumn("Accounts")
//				.addVirtualColumn("Segments", "SumProduct Default")
//				.ignoreFileColumn("Fixed Pool")
				.ignoreFileColumn("BegBalance")
				;
			}).get();
			
		
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}	
	
	
	public CompletableFuture<EssbaseReportingService> loadCurrentPeriodHistory()  {
		CompletableFuture<EssbaseReportingService> cf = CompletableFuture.supplyAsync(() -> {
			try {
				rptgCube.load((loadFile, ruleFile) -> {
					loadFile.localPath(Def.CPHISTORY + "/h_" + Def.PROJECT_NAME + "_ar_" + Def.YR2D + ".txt");
					ruleFile.aiSourceFile(Def.CPHISTORY + "/h_" + Def.PROJECT_NAME + "_ar_" + Def.YR2D + ".txt")
					.ignoreFileColumn("BegBalance");
				}).get();

	
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this;
		});
		return cf;
	}

	private static void loadDivAllocFile(EssbaseCube cube, String strHome, String strDiv ) {
		try {
			cube.load((loadFile, ruleFile) -> {
				loadFile.localPath(Def.EXPORT + "/required/" + Def.PROJECT_NAME + "_" + strDiv.toLowerCase() + ".txt");
				ruleFile.aiSourceFile(Def.EXPORT + "/required/" + Def.PROJECT_NAME + "_" + strDiv.toLowerCase() + ".txt")
				.addVirtualColumn("Scenarios", "Actual")
				.ignoreFileColumn("BegBalance");
			}).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void loadQiDivAllocFile(EssbaseCube cube, String strHome, String strDiv ) {
		try {
			cube.load((loadFile, ruleFile) -> {
				loadFile.localPath(Def.EXPORT + "/required/" + Def.PROJECT_NAME + "_" + strDiv.toLowerCase() + ".txt");
				ruleFile.aiSourceFile(Def.EXPORT + "/required/" + Def.PROJECT_NAME + "_" + strDiv.toLowerCase() + ".txt")
				.addVirtualColumn("Scenarios", "Actual")
				.ignoreFileColumn("BegBalance");
//				.addDataFields("CareMore QI Exp");
			}).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	private static void loadDBGDivAllocFile(EssbaseCube cube, String strHome, String strDiv ) {
//		try {
//			cube.load((loadFile, ruleFile) -> {
//				loadFile.localPath(Def.EXPORT + "/required/" + Def.PROJECT_NAME + "_" + strDiv.toLowerCase() + ".txt");
//				ruleFile.aiSourceFile(Def.EXPORT + "/required/" + Def.PROJECT_NAME + "_" + strDiv.toLowerCase() + ".txt")
//				.addVirtualColumn("Scenarios", "Actual");			
////				.addDataFields("DBG QI Exp");
//				//.ignoreFileColumn("BegBalance");
//			}).get();
//		} catch (InterruptedException | ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public EssbaseCubeService move2Production() throws InterruptedException, ExecutionException {
		rptgApp.ifAppExistsThenDelete(Def.CUBE_NAME).get();

		rptgApp.rename(Def.CUBE_NAME).get();

		EssbaseCube cube = server.getApplication(essbase, Def.CUBE_NAME).getCube(Def.RPTG_NAME);

		cube.rename(Def.CUBE_NAME).get();

		EssbaseCubeService cubeService = new EssbaseCubeService(essbase);
		return cubeService;
	}

	public void updateTime() {

	}
	
	public EssbaseReportingService exportActallc4Regalc() throws Exception {
		String fix = "FIX ( Jan:Dec, @RELATIVE(\"Company\",0),@REMOVE(@RELATIVE(\"Accounts\", 0), \"Admin Exp Alloc\" ))";
		ExportFile export = rptgCube.export(f -> f
				.fileName(Def.PROJECT_NAME + ".txt")
				.addFixStatement(fix))
				.get();
		export.bringLocally(
				Def.EXPORT + "/required/" + "par_actallc_4regalc.txt",
				Def.EXPORT+ "/new/" + "par_actallc_4regalc.txt"
		).pipeify().copy2Backup(Def.BKP);
		return this;	
	}
	
	
}
