package gr.uom.java.metrics;

import gr.uom.java.ast.ClassObject;
import gr.uom.java.ast.FieldInstructionObject;
import gr.uom.java.ast.MethodObject;
import gr.uom.java.ast.SystemObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LCOM {
	
	private Map<String, Integer> lcomMap;
	
	public LCOM(SystemObject system) {
		lcomMap = new HashMap<String, Integer>(); 
		
		
		Set<ClassObject> classes = system.getClassObjects();
		for(ClassObject clazz : classes){
			int cohesion = computeCohesion(clazz);
			lcomMap.put(clazz.getName(), cohesion);
		}
	}

	private Integer computeCohesion(ClassObject clazz) {
		List<MethodObject> methodList = clazz.getMethodList();
		int q =0;
		int p =0;
		
		if(methodList.size() < 2)
			return -1;			
		
		for(int i=0; i< methodList.size() -1 ; i++){
			MethodObject methodObject = methodList.get(i);
			
			for(int j=i+1; j< methodList.size(); j++){
				MethodObject methodObject2 = methodList.get(j);
				boolean shareAttributes = shareAttributes(methodObject, methodObject2);
				if(shareAttributes){
					q++;
				}else{
					p++;
				}
			}
		}
		if(p>q){
			return p -q;
		}else{
			return 0;
		}
	}

	private boolean shareAttributes(MethodObject methodObject, MethodObject methodObject2) {
		String className = methodObject.getClassName();
		List<FieldInstructionObject> fieldInstructionsObject = methodObject.getFieldInstructions();
		List<FieldInstructionObject> fieldInstructionsObject2 = methodObject2.getFieldInstructions();
		for(FieldInstructionObject field : fieldInstructionsObject){
			if(fieldInstructionsObject2.contains(field) && field.getOwnerClass().equals(className)){
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(String className : lcomMap.keySet()){
			Integer cohesionValue = lcomMap.get(className);
			sb.append(className);
			sb.append(" - ");
			sb.append(cohesionValue);
			sb.append("\n");
		}
		return sb.toString();
	}
}