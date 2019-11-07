package file;

public class FileDTO {
	String fileName;
	String fileSaveName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSaveName() {
		return fileSaveName;
	}

	public void setFileSaveName(String fileSaveName) {
		this.fileSaveName = fileSaveName;
	}

	public FileDTO(String fileName, String fileSaveName) {
		super();
		this.fileName = fileName;
		this.fileSaveName = fileSaveName;
	}

}
