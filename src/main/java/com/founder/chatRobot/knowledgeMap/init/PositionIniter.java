package com.founder.chatRobot.knowledgeMap.init;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TypeMissException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.context.fact.InternalFactType;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.entity.InternalEntity;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.taskType.InternalTaskConception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;
import com.founder.chatRobot.recognition.comparisionMethod.entryInfo.SemanticEntryInfo;
import com.founder.chatRobot.task.treader.WeahterTreader;

public class PositionIniter {
	private BufferedReader file;
	private KnowledgeManager knowledgeManager;
	ComparisionRecognition recognition;
	ContextManager contextManager;

	// private Map<String, Entity> positionMap;

	public PositionIniter(String fileName, KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		try {
			InputStream is = Thread.currentThread().getClass()
					.getResourceAsStream("/" + fileName);
			
			if(is == null) {
				is = PositionIniter.class.getResourceAsStream("/" + fileName);
			}
			
			if (is == null) {
				is = new FileInputStream(fileName);
			}
			file = new BufferedReader(new InputStreamReader(is, "GBK"));
			/*
			 * file = new BufferedReader(new InputStreamReader( new
			 * FileInputStream(fileName), "GBK"));
			 */
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.knowledgeManager = knowledgeManager;
		this.contextManager = contextManager;
		this.recognition = recognition;
		// this.positionMap = positionMap;
	}

	private void addToRecognition(Entity entity, String typeName,
			String... marks) {
		FactInfo<Entity> factInfo = new FactInfo<Entity>(entity);
		EntryInfo<Entity> entryinfo = new EntryInfo<Entity>(typeName, factInfo);
		// System.out.println(entryinfo.hashCode());
		SemanticEntryInfo<Entity> sem = new SemanticEntryInfo<Entity>(entryinfo);
		for (String mark : marks) {
			sem.addMark(mark);
		}
		this.recognition.addOverallSemanticEntry(entryinfo, sem);
	}

	public void deal() {
		// BufferedReader br = null;
		TaskConception taskType = new InternalTaskConception("Weather",
				new WeahterTreader(), "查询天气");

		try {
			this.contextManager.addTaskType(taskType);
			this.contextManager.addTypeRelation("division", "Division",
					"Weather");

			String line;
			while ((line = this.file.readLine()) != null) {
				if (line != null && !line.equals("")) {
					String[] parts = line.split("=");
					Entity position = (Entity) this.knowledgeManager
							.uniqueIndex(parts[1]);

					if (position == null) {
						position = new InternalEntity(false);
						// System.out.println(parts[0]);
						// System.out.println(parts[1]);
						position.setValue(parts[0]);
						position.setName(parts[1]);

						position.setMainExpress(parts[1]);

						this.addToRecognition(position, "Division", parts[1]);
						// position.setParent(mainPos);
						this.knowledgeManager
								.addUniqueIndex(parts[1], position);
						// this.positionMap.put(parts[1], position);
					}

					WeahterTreader.locationCodeMap.put(
							(String) position.getValue(), parts[0]);
				}

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != file) {
				try {
					file.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// 创建常用日期表示
		try {
			Entity time = new InternalEntity(false);
			time.setName("Date");
			FactType factType = new InternalFactType("Date", "日期", true, time);
			this.contextManager.addFactType(factType);
			this.addToRecognition(time, "__ATTRIBUTE__Date", "日期");
			this.contextManager.addTypeRelation("date", "Date",
					taskType.getTypeName());

			Entity day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					// System.out.println(new Date());
					return new Date();
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("Today");
			this.addToRecognition(day, "Date", "今天");
			day.setParent(time);
			this.knowledgeManager.addUniqueIndex("今天", day);

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
					date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
					// System.out.println("weather : ");
					// //System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("Tomorrow");
			this.addToRecognition(day, "Date", "明天");
			day.setParent(time);
			this.knowledgeManager.addUniqueIndex("明天", day);

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE, 2);// 把日期往后增加一天.整数往后推,负数往前移动
					date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
					// //System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("AfterTomorrow");
			this.addToRecognition(day, "Date", "后天");
			day.setParent(time);
			this.knowledgeManager.addUniqueIndex("后天", day);

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE, 3);// 把日期往后增加一天.整数往后推,负数往前移动
					date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
					// //System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("ThreeDaysFromNow");
			this.addToRecognition(day, "Date", "大后天");
			day.setParent(time);
			this.knowledgeManager.addUniqueIndex("大后天", day);

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
					date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
					// //System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("Yesterday");
			this.addToRecognition(day, "Date", "昨天");
			day.setParent(time);
			this.knowledgeManager.addUniqueIndex("昨天", day);

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE, -2);// 把日期往后增加一天.整数往后推,负数往前移动
					date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
					// //System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("BeforeYesterday");
			this.addToRecognition(day, "Date", "前天");
			day.setParent(time);
			this.knowledgeManager.addUniqueIndex("前天", day);

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE, -3);// 把日期往后增加一天.整数往后推,负数往前移动
					date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
					// //System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("ThreeDaysAgo");
			this.addToRecognition(day, "Date", "大前天");
			day.setParent(time);
			this.knowledgeManager.addUniqueIndex("大前天", day);

			Entity week = new InternalEntity(false);
			week.setName("Week");

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);

					calendar.add(Calendar.DATE,
							2 - calendar.get(Calendar.DAY_OF_WEEK));
					date = calendar.getTime();
					// System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("Monday");
			this.addToRecognition(day, "Date", "星期一", "周一");
			day.setParent(week);
			this.knowledgeManager.addUniqueIndex("星期一", day);
			this.knowledgeManager.addUniqueIndex("周一", day);

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);

					calendar.add(Calendar.DATE,
							3 - calendar.get(Calendar.DAY_OF_WEEK));
					date = calendar.getTime();
					// System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("TUESDAY");
			this.addToRecognition(day, "Date", "星期二", "周二");
			day.setParent(week);
			this.knowledgeManager.addUniqueIndex("星期二", day);
			this.knowledgeManager.addUniqueIndex("周二", day);

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE,
							4 - calendar.get(Calendar.DAY_OF_WEEK));
					date = calendar.getTime();
					// System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("WEDNESDAY");
			this.addToRecognition(day, "Date", "星期三", "周三");
			day.setParent(week);
			this.knowledgeManager.addUniqueIndex("星期三", day);
			this.knowledgeManager.addUniqueIndex("周三", day);

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE,
							5 - calendar.get(Calendar.DAY_OF_WEEK));
					date = calendar.getTime();
					// System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("THURSDAY");
			this.addToRecognition(day, "Date", "星期四", "周四");
			day.setParent(week);
			this.knowledgeManager.addUniqueIndex("星期四", day);
			this.knowledgeManager.addUniqueIndex("周四", day);

			day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE,
							6 - calendar.get(Calendar.DAY_OF_WEEK));
					date = calendar.getTime();
					// System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("FRIDAY");
			this.addToRecognition(day, "Date", "星期五", "周五");
			day.setParent(week);
			this.knowledgeManager.addUniqueIndex("星期五", day);
			this.knowledgeManager.addUniqueIndex("周五", day);

			Entity saturday = day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE,
							7 - calendar.get(Calendar.DAY_OF_WEEK));
					date = calendar.getTime();
					// System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("SATURDAY");
			this.addToRecognition(day, "Date", "星期六", "周六");
			day.setParent(week);
			this.knowledgeManager.addUniqueIndex("星期六", day);
			this.knowledgeManager.addUniqueIndex("周六", day);

			Entity sunday = day = new InternalEntity(false) {
				@SuppressWarnings("rawtypes")
				public Comparable getValue() {
					Date date = new Date();// 取时间
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(date);
					calendar.add(Calendar.DATE,
							8 - calendar.get(Calendar.DAY_OF_WEEK));
					date = calendar.getTime();
					// System.out.println(date);
					return date;
				}

				@SuppressWarnings("rawtypes")
				public Class getValueType() {
					return Date.class;
				}
			};
			day.setName("SUNDAY");
			this.addToRecognition(day, "Date", "星期日", "星期天", "周日");
			day.setParent(week);
			this.knowledgeManager.addUniqueIndex("星期日", day);
			this.knowledgeManager.addUniqueIndex("周日", day);

			FactInfo<Entity> factInfo = new FactInfo<Entity>(saturday, sunday);
			EntryInfo<Entity> entryinfo = new EntryInfo<Entity>("Date",
					factInfo);
			SemanticEntryInfo<Entity> sem = new SemanticEntryInfo<Entity>(
					entryinfo);
			sem.addMark("周末");
			this.recognition.addOverallSemanticEntry(entryinfo, sem);
			/*new Express<ExpressConception>("周末", saturday,
					this.expressRecognition);
			// this.expressRecognition.addExpress(express);
			new Express<ExpressConception>("周末", sunday,
					this.expressRecognition);
			// this.expressRecognition.addExpress(express);
*/
		} catch (DoubleInsertException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TypeUnmatchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 创建天气信息
		try {
			Entity mainWeather = new InternalEntity(false);
			mainWeather.setName("Weather");
			FactType factType = new InternalFactType("Weather", "天气", true,
					mainWeather);
			this.contextManager.addFactType(factType);
			this.addToRecognition(mainWeather, "__ATTRIBUTE__Weather", "天气");

			Entity weather = new InternalEntity(false);
			weather.setValue("Sunny");
			weather.setName("Sunny");
			this.addToRecognition(weather, "Weather", "晴");
			weather.setParent(mainWeather);
			this.knowledgeManager.addUniqueIndex("晴", weather);

			weather = new InternalEntity(false);
			weather.setValue("Overcast");
			weather.setName("Overcast");
			this.addToRecognition(weather, "Weather", "阴");
			weather.setParent(mainWeather);
			this.knowledgeManager.addUniqueIndex("阴", weather);

			weather = new InternalEntity(false);
			weather.setValue("Fog");
			this.addToRecognition(weather, "Weather", "雾");
			weather.setParent(mainWeather);
			this.knowledgeManager.addUniqueIndex("雾", weather);

			weather = new InternalEntity(false);
			weather.setValue("Haze");
			weather.setName("Haze");
			this.addToRecognition(weather, "Weather", "霾");
			weather.setParent(mainWeather);
			this.knowledgeManager.addUniqueIndex("霾", weather);

			weather = new InternalEntity(false);
			weather.setValue("Cream");
			weather.setName("Cream");
			this.addToRecognition(weather, "Weather", "霜");
			weather.setParent(mainWeather);
			this.knowledgeManager.addUniqueIndex("霜", weather);

			weather = new InternalEntity(false);
			weather.setValue("Cloudy");
			weather.setName("Cloudy");
			this.addToRecognition(weather, "Weather", "多云");
			weather.setParent(mainWeather);
			this.knowledgeManager.addUniqueIndex("多云", weather);

			weather = new InternalEntity(false);
			weather.setValue("Hail");
			weather.setName("Hail");
			this.addToRecognition(weather, "Weather", "冰雹");
			weather.setParent(mainWeather);
			this.knowledgeManager.addUniqueIndex("冰雹", weather);

			weather = new InternalEntity(false);
			weather.setValue("Rain");
			weather.setName("Rain");
			this.addToRecognition(weather, "Weather", "雨");
			weather.setParent(mainWeather);
			this.knowledgeManager.addUniqueIndex("雨", weather);

			weather = new InternalEntity(false);
			weather.setValue("Snow");
			weather.setName("Snow");
			this.addToRecognition(weather, "Weather", "雪");
			weather.setParent(mainWeather);
			this.knowledgeManager.addUniqueIndex("雪", weather);
		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
