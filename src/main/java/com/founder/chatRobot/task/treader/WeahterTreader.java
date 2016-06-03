package com.founder.chatRobot.task.treader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class WeahterTreader extends TaskTreader {
	public WeahterTreader() {
		super(null,null);
		// TODO Auto-generated constructor stub
	}

	public static Map<String, String> locationCodeMap = new HashMap<String, String>();

	private String getWeather(String posCode, Date time) {
		String result = "";
		GregorianCalendar date = new GregorianCalendar(), current = new GregorianCalendar();
		
		//System.out.println("gfdsasdf");

		date.setTime(time);
		current.setTime(new Date());

		int year = current.get(Calendar.YEAR);
		int month = current.get(Calendar.MONTH);
		int day = current.get(Calendar.DATE);

		if (date.get(Calendar.YEAR) != year
				|| (date.get(Calendar.MONTH) != month)
				|| (date.get(Calendar.DATE) != day)) {
			result = "对不起，目前暂时暂时只能查询当天的天气。";
			return result;
		}

		String url = "http://www.weather.com.cn/data/sk/";
		String url2 = "http://www.weather.com.cn/data/cityinfo/";
		// String urlAll = "http://www.weather.com.cn/data/";
		HttpClient http = new HttpClient();
		GetMethod getMethod = new GetMethod(url + posCode + ".html");
		GetMethod getMethod2 = new GetMethod(url2 + posCode + ".html");
		// GetMethod getMethodAll = new GetMethod(urlAll + posCode + ".html");
		try {
			http.executeMethod(getMethod);
			byte[] bytes = getMethod.getResponseBody();
			http.executeMethod(getMethod2);
			byte[] bytes2 = getMethod2.getResponseBody();
			// http.executeMethod(getMethodAll);
			// byte[] bytesAll = getMethodAll.getResponseBody();
			@SuppressWarnings("unchecked")
			Map<String, Object> weather = (Map<String, Object>) ((Map<String, Object>) JSONUtil
					.deserialize(new String(bytes, "UTF-8")))
					.get("weatherinfo");
			@SuppressWarnings("unchecked")
			Map<String, Object> weather2 = (Map<String, Object>) ((Map<String, Object>) JSONUtil
					.deserialize(new String(bytes2, "UTF-8")))
					.get("weatherinfo");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result += weather.get("city") + "\t" + weather2.get("weather")
					+ "\t" + df.format(new Date()).substring(0, 10) + " "
					+ weather.get("time") + "\n";
			result += "温度 ：" + weather.get("temp") + "°C\n";
			result += "风向 ：" + weather.get("WD") + "\n";
			result += "风速 ：" + weather.get("WS") + "\n";
			result += "风力 ：" + weather.get("WSE") + "\n";
			result += "湿度 ：" + weather.get("SD") + "\n";
			return result;
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = "对不起，由于网络原因，目前暂时暂时不能查询天气情况。";
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		List<String> posCodes = new ArrayList<String>(5);
		List<String> postions = new ArrayList<String>(5);
		List<Date> dates = new ArrayList<Date>(5);

		for (@SuppressWarnings("rawtypes")
		Fact fact : factList) {
			if (fact.getType().getTypeName().equals("Division")) {
				for (Object entity : (List<Object>) fact.getInfo().getInfo()) {
					if (entity instanceof Entity) {
						// System.out.println(((Entity) entity).getValue());
						// System.out.println(((Entity) entity).getName());

						posCodes.add((String) ((Entity) entity).getValue());
						postions.add((String) ((Entity) entity).getName());
					}
				}
			}
			if (fact.getType().getTypeName().equals("Date")) {
				for (Object entity : (List<Object>) fact.getInfo().getInfo()) {
					if ((entity instanceof Entity)) {
						dates.add((Date) ((Entity) entity).getValue());
						/*
						 * System.out.println("ttttt : " + date.compareTo(new
						 * Date()));
						 */
					}
				}
			}
		}

		if (dates.size() == 0) {
			dates.add(new Date());
		}

		String answer = "", tmp;

		for (int i = 0; i < posCodes.size(); i++) {
			String posCode = posCodes.get(i);
			posCode = locationCodeMap.get(posCode);
			//System.out.println(posCode);
			for (Date date : dates) {
				tmp = this.getWeather(posCode, date);
				if (tmp.equals("对不起，目前暂时暂时只能查询当天的天气。")) {
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					answer += calendar.get(Calendar.YEAR) + "-"
							+ (calendar.get(Calendar.MONTH) + 1) + "-"
							+ calendar.get(Calendar.DATE) + postions.get(i)
							+ "的天气情况与预报暂时不能提供 \n";
					//System.out.println(posCode);
				} else
					answer += tmp;
			}
		}
		result.setAnswer(answer);
		result.setFinished();

		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> checkFact(Task task) {
		List<String> result = new LinkedList<String>();
		boolean hasPosition = true;

		for (FactType factType : task.checkFacts(false)) {
			//System.out.println(factType.getTypeName());
			if (factType.getTypeName().equals("Division"))
				hasPosition = false;
		}

		if (!hasPosition) {
			task.addFact(task.getSession().getPosition());
		}

		result.add(TaskTreader.DOTASK);
		return result;
	}
}
