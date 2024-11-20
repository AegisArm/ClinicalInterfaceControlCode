import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.neuronrobotics.bowlerstudio.creature.MobileBaseLoader
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine;
import com.neuronrobotics.sdk.addons.kinematics.DHParameterKinematics
import com.neuronrobotics.sdk.addons.kinematics.MobileBase
import com.neuronrobotics.sdk.addons.kinematics.math.RotationNR
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR
import com.neuronrobotics.sdk.common.DeviceManager

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.lang.reflect.Type;

Type TT_mapStringString = new TypeToken<HashMap<String, ArrayList< TransformNR >>>() {}.getType();
Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();


//Alternate method to load from a string
File	cachejson = ScriptingEngine.fileFromGit("https://github.com/AegisArm/ClinicalInterfaceControlCode.git", "data.json")
String jsonString = null;
InputStream inPut = null;
inPut = FileUtils.openInputStream(cachejson);
jsonString = IOUtils.toString(inPut);											
HashMap<String, ArrayList<TransformNR>> database = gson.fromJson(jsonString, TT_mapStringString);
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

//Start from where the arm already is and move it from there with absolute location



// size of increment, and wait determine velocity of motion

TransformNR translate(DHParameterKinematics arm, TransformNR home, float x, float y, float z){
	TransformNR endPose = new TransformNR(x, y, z);
	TransformNR startPose = home;

	
	for(double i = 0; i <= 1; i += 0.01){
		TransformNR incre = endPose.scale(i);
		TransformNR temp = incre.times(startPose);//multiply offset by current
		arm.setDesiredTaskSpaceTransform(temp,  0);//change this value to smooth out motion
		Thread.sleep(30); //wait 30 msec
	}

	return endPose;
}

TransformNR translate(DHParameterKinematics arm, TransformNR home, float x, float y, float z, float increment) throws IllegalArgumentException{
	TransformNR endPose = new TransformNR(x, y, z);
	TransformNR startPose = home;

	if(increment > 1){
		throw new IllegalArgumentException("increment > 1");
	}
	
	for(double i = 0; i <= 1; i += increment){
		TransformNR incre = endPose.scale(i);
		TransformNR temp = incre.times(startPose);//multiply offset by current
		arm.setDesiredTaskSpaceTransform(temp,  0);//change this value to smooth out motion
		Thread.sleep(30); //wait 30 msec
	}
	
	return endPose;
}

TransformNR translate(DHParameterKinematics arm, TransformNR home, TransformNR end, float increment) throws IllegalArgumentException{

	TransformNR endPose = end;
	TransformNR startPose = home;

	if(increment > 1){
		throw new IllegalArgumentException("increment > 1");
	}
	
	for(double i = 0; i <= 1; i += increment){
		TransformNR incre = endPose.scale(i);
		TransformNR temp = incre.times(startPose);//multiply offset by current
		arm.setDesiredTaskSpaceTransform(temp,  0);//change this value to smooth out motion
		Thread.sleep(30); //wait 30 msec
	}
	
	return endPose;
}



TransformNR home = aegisArm.calcHome().copy();

TransformNR move = new TransformNR(30.0, 50.0, -50.0, new RotationNR(0, 0, 0));

TransformNR current = translate(aegisArm, home, move, 0.04);

move = new TransformNR(0.0, 0.0, 0.0);

current = translate(aegisArm, current, home, 0.04);


return null;