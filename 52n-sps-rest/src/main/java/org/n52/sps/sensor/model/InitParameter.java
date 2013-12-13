package org.n52.sps.sensor.model;

/** 
 * Clase utilizada para representar parametros de configuración iniciales para los plugins desarrollados
 * Se podria refactorizar para heredar de la clase Parameter
 * @author cgiraldo
 */
public class InitParameter {
	String name;
	String description;
	String value;

	public InitParameter(){};
	
	public InitParameter(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	public InitParameter(String name, String description, String value){
		this.name = name;
		this.description = description;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "InitParameter [name=" + name + ", value=" + value + "]";
	}
	
}