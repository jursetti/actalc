package com.antm.fdsm.caas.actalc;

import java.util.concurrent.CompletableFuture;

import com.antm.fdsm.orcl.utils.Singleton;

public class ServiceFacade {

	public static void archive(Singleton oacService, Singleton dbService) {
		//EssbaseCubeService svc = new EssbaseCubeService(oacService).associate(dbService);
	}

	public static void base(Singleton oacService, Singleton dbService) throws Exception {
		oacService.slackInfo(Def.SLACK_WEBHOOK_APP, ":rocket: starting " + Def.CUBE_NAME + " update[base].");

		final EssbaseMetadataService metaService = new EssbaseMetadataService(oacService);
		final Actadm2CubeService actadm2 = new Actadm2CubeService(oacService);
		CompletableFuture<Void> extract = actadm2.extractUnallocated();
		CompletableFuture<Void> createRptg =  metaService.createReportingCube();
		CompletableFuture<Void> createCalc =  metaService.createCalculatingCube();
		
		
		
		createCalc.get();
		createRptg.get();
		extract.get();
		
		EssbaseCalculationService calcService = new EssbaseCalculationService(oacService);
		calcService.clearAllData();
		CompletableFuture<Void> unallocatedLoad = calcService.loadUnallocated();
		CompletableFuture<Void> detailRates = calcService.loadCostCenterRatesDetail();
		//CompletableFuture<Void> summaryRates = calcService.loadCostCenterRatesSummary();
		unallocatedLoad.get();
		detailRates.get();
		//summaryRates.get();
		//calcService.allocate();
		//calcService.exportCube();
		//createRptg.get();
		/*EssbaseReportingService rptgService = new EssbaseReportingService(oacService);
		rptgService.clearAllData()
			.loadData();
		
		createCalc.get();

		Logger.info("calc cube creation completed.");
		EssbaseCalculationService calcService = new EssbaseCalculationService(oacService);
		calcService.clearAllData();
		CompletableFuture<Void> unallocatedLoad = calcService.loadUnallocated();
		CompletableFuture<Void> driverLoad = calcService.loadDrivers();
		unallocatedLoad.get();
		driverLoad.get();

		calcService.allocate()
			.exportCube()
			.moveNewExport2Previous();

		createRptg.get();

		rptgService.move2Production()
			.balance()
			.associate(dbService);*/

		oacService.slackInfo(Def.SLACK_WEBHOOK_APP, ":checkered_flag: finished " + Def.CUBE_NAME + " update[base].");
	}

	public static void incremental(Singleton oacService, Singleton dbService) throws Exception {
		oacService.slackInfo(Def.SLACK_WEBHOOK_APP, ":rocket: starting " + Def.CUBE_NAME + " update[incremental].");
		/*RelationalDatabaseService relationalService = new RelationalDatabaseService(dbService);
		relationalService.extractPSGLCurrentMonth();
		EssbaseCalculationService calcService = new EssbaseCalculationService(oacService);
		calcService.clearAllData()
			.loadDrivers().get()
			.loadUnallocated().get()
			.exportCube()
			.loadPreviousExport()
			.exportIncremental()
			.moveNewExport2Previous();

		EssbaseCubeService cubeService = new EssbaseCubeService(oacService);
		cubeService.loadIncrementalSlice().balance();

		//update time here.*/
		oacService.slackInfo(Def.SLACK_WEBHOOK_APP, ":checkered_flag: finished " + Def.CUBE_NAME + " update[incremental].");
	}

	public void transitionPlan() {

	}

	public void transitionApprovedForecast() {

	}

	public void transitionBoardApprovedPlan() {

	}

	public void transitionActualMonth() {

	}

	public void transitionActualYear() {

	}
}