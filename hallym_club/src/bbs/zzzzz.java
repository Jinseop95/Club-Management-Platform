package bbs;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class zzzzz {

	public String str() {

		JSONArray personArray = new JSONArray();
		JSONObject personInfo = new JSONObject();

		personInfo.put("title", "테스트");
		personInfo.put("start", "2019-10-12");
		personInfo.put("end", "2019-10-15");

		personArray.add(personInfo);

		personInfo = new JSONObject();
		personInfo.put("title", "test");
		personInfo.put("start", "2019-10-16");
		personInfo.put("end", "2019-10-19");
		personArray.add(personInfo);

		String jsonInfo = personArray.toJSONString();
		System.out.println(jsonInfo);
		return jsonInfo;
	}

	public static void main(String[] args) {
		zzzzz a = new zzzzz();
		a.str();

	}

}
