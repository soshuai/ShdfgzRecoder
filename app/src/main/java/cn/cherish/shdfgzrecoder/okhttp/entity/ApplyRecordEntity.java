package cn.cherish.shdfgzrecoder.okhttp.entity;

public class ApplyRecordEntity extends BaseApiEntity {

	private RecordInfoEntity data;

	public ApplyRecordEntity() {
		super();
	}

	public ApplyRecordEntity(Integer result, String msg) {
		super(result, msg);
	}

	public RecordInfoEntity getData() {
		return data;
	}

	public void setData(RecordInfoEntity data) {
		this.data = data;
	}

}
