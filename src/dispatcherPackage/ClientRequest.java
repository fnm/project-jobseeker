package dispatcherPackage;

import entitiesPackage.Entity;

public class ClientRequest {
	
	private RequestCodeEnum OpCode;
	private Entity entity;
	
	public RequestCodeEnum getOpCode() {
		return OpCode;
	}
	public void setOpCode(RequestCodeEnum opCode) {
		OpCode = opCode;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	
	
	

}
