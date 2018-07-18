package com.antm.fdsm.caas.actadm;
import com.antm.fdsm.orcl.utils.Singleton;

import io.vertx.core.AbstractVerticle;

public class Start extends AbstractVerticle {

	private final static Singleton oacActService = Singleton.OACDEV.setDirs(Def.DIRS).setSlackApp(Def.SLACK_WEBHOOK_APP).setApp(Def.CUBE_NAME);
	private final static Singleton dbHypusrService = Singleton.HYPUSR.setDirs(Def.DIRS).setSlackApp(Def.SLACK_WEBHOOK_APP).setApp(Def.CUBE_NAME);

	public static void main(String[] args) {
		try {
			//automatically download wh files.
			//add security.
			//test on server.
			//localhost on mac + SSL.
			//error handling.
			//maybe change from runnable to observable
			//date and move error files to more permanent location.  DO FIRST.
			ServiceFacade.base(oacActService,dbHypusrService);
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
