println("starting");
	base=DeviceManager.getSpecificDevice( "Standard6dof",{
			//If the device does not exist, prompt for the connection
			
			MobileBase m = MobileBaseLoader.fromGit(
				"https://github.com/AegisArm/GroguMechanicsCad.git",
				"hephaestus.xml"
				)
			if(m==null)
				throw new RuntimeException("Arm failed to assemble itself")
			println "Connecting new device robot arm "+m
			return m
		})
		return null;