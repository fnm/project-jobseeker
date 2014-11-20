package dispatcherPackage;
import java.util.ArrayList;
import entitiesPackage.Entity;

public class HandleRequestStatus {
	
	private RequestCodeEnum OpCode;
	private String status;
	private String errorMsg;
	private Entity entity;
	private ArrayList entityArr;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
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
	public ArrayList getEntityArr() {
		return entityArr;
	}
	public void setEntityArr(ArrayList entityArr) {
		this.entityArr = entityArr;
	}
	
}
