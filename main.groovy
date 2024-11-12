import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine;

import java.lang.reflect.Type;

Type TT_mapStringString = new TypeToken<HashMap<String, ArrayList< TransformNR >>>() {}.getType();
Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();


//Alternate method to load from a string
//File	cachejson = ScriptingEngine.fileFromGit(myURL, filename)
//String jsonString = null;
//InputStream inPut = null;
//inPut = FileUtils.openInputStream(cachejson);
//jsonString = IOUtils.toString(inPut);											
//HashMap<String, HashMap<String, Object>> database = gson.fromJson(jsonString, TT_mapStringString);
// Reading Data

println("starting");
MobileBase base=DeviceManager.getSpecificDevice( "Standard6dof",{
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

DHParameterKinematics aegisArm = base.getAllDHChains().get(0);
double zLift = 20;

TransformNR offset = new TransformNR(0, 0, zLift);


//Start from where the arm already is and move it from there with absolute location
TransformNR current = aegisArm.calcHome().copy();
//current.translateZ(zLift);



for(double i = 0; i <= 1; i += 0.01){
	TransformNR incre = offset.scale(i);
	TransformNR temp = incre.times(current);//multiply offset by current
	aegisArm.setDesiredTaskSpaceTransform(temp,  0);//change this value to smooth out motion
	Thread.sleep(30); //wait 30 msec
}
// size of increment, and wait determine velocity of motion



return null;