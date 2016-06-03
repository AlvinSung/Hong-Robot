package com.founder.chatRobot.task.treader;

import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class NeededCertificatesTreader extends TaskTreader {

	public NeededCertificatesTreader() {
		super(null, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		result.setFinished();
		Object entity;
		// System.out.println(factList.size());
		/*if (factList != null && factList.size() > 0) {
			if ((entity = factList.get(0).getInfo().getInfo().get(0)) instanceof Entity) {
				System.out.println(((Entity) entity).getName());
				System.out.println(((Entity) entity).getMainExpress());
			}
		}*/
		if (factList == null || factList.size() == 0) {
			result.setAnswer("您好，若您是境内居民新开户，您必须携带您的有效期内的二代身份证。若您已在其他地方开立过证券账户，"
					+ "则除身份证外还需要携带您的股东账户卡（沪市、深市），若遗失股东卡我司可以补办，收费10元一张。");
		} else if ((entity = factList.get(0).getInfo().getInfo().get(0)) instanceof Entity
				&& (((Entity) entity).getName().equals("香港特别行政区") || ((Entity) entity)
						.getName().equals("澳门特别行政区"))) {
			result.setAnswer("您好，只有在境内工作生活的香港、澳门居民才能开立A股账户，"
					+ "需要提供：港澳居民往来内地通行证、港澳居民身份证、公安机关出具的临时住宿登记证明表。");
		} else if ((entity = factList.get(0).getInfo().getInfo().get(0)) instanceof Entity
				&& (((Entity) entity).getName().equals("台湾省"))) {
			result.setAnswer("您好，只有在境内工作生活的台湾居民才能开立A股账户，需要提供："
					+ "台湾居民往来内地通行证、台湾居民身份证、公安机关出具的临时住宿登记证明表。");
		} else {
			result.setAnswer("您好，若您是境内居民新开户，您必须携带您的有效期内的二代身份证。若您已在其他地方开立过证券账户，"
					+ "则除身份证外还需要携带您的股东账户卡（沪市、深市），若遗失股东卡我司可以补办，收费10元一张。");
		}

		result.addResult(
				"AnswerContent",
				"1. 境内自然人投资者，如果申请办理证券账户开立，需要携带以下材料：\n"
						+ "（1）《证券账户开立申请表》；\n"
						+ "（2）第二代居民身份证及复印件；\n"
						+ "（3）委托他人代办的，还需提供经公证的委托代办书、代办人有效身份证明文件及复印件。\n"
						+ "\n"
						+ "2. 在境内工作生活的港澳台居民，如果申请办理证券账户开立，需要携带以下材料：\n"
						+ "（1）《证券账户开立申请表》；\n"
						+ "（2）港澳居民来往内地通行证或台湾居民来往大陆通行证及复印件；\n"
						+ "（3）委托他人代办的，还需提供经公证的委托代办书、代办人有效身份证明文件及复印件。\n"
						+ "（4）港澳台当地居民身份证及复印件，公安机关出具的临时住宿登记证明表（或公安机关认可的同类证明文件）"
						+ "及复印件等能够证明港、澳、台居民在大陆工作生活的书面证明材料（需加盖公安机关公章）。\n"
						+ "\n"
						+ "3. 获得中国永久居留权的外国人，如果申请办理证券账户开立，需要携带以下材料：\n"
						+ "（1）《证券账户开立申请表》；\n"
						+ "（2）外国人永久居留证及复印件；\n"
						+ "（3）委托他人代办的，还须提供经公证的委托代办书、代办人有效身份证明文件及复印件。\n"
						+ "\n"
						+ "4. 境外自然人投资者，如果申请开立B股账户，需要携带以下材料：\n"
						+ "（1）《证券账户开立申请表》；\n"
						+ "（2）投资者有效身份证明文件（境外护照，有境外其他国家或地区永久居留签证的中国护照，"
						+ "香港、澳门居民身份证，台湾居民来往大陆通行证）及复印件；\n"
						+ "（3）委托他人代办的，还须提供经公证的委托代办书、代办人有效身份证明文件及复印件。\n"
						+ "\n"
						+ "5.境内法人投资者，如果申请办理证券账户开立，需要携带以下材料：\n"
						+ "（1）《证券账户开立申请表》；\n"
						+ "（2）公司制法人为工商营业执照及组织机构代码证，社团法人为社团法人登记证书及组织机构代码证，事业法人为事业法人登记证书及组织机构代码证，机关法人为机关法人成立批文及组织机构代码证等原件及复印件；\n"
						+ "（3）法定代表人证明书、法定代表人授权委托书和法定代表人的有效身份证明文件复印件（需加盖公章，授权委托书还需法定代表人签章）；\n"
						+ "（4）经办人有效身份证明文件及复印件。\n"
						+ "\n"
						+ "6. 作为境内合伙企业、非法人创业投资企业，如果申请开立证券账户，需要携带以下材料：\n"
						+ "（1）《证券账户开立申请表》；\n"
						+ "（2）《合伙企业等非法人组织合伙人信息采集表》；\n"
						+ "（3）工商管理部门颁发的营业执照或国家有权机关颁发的合伙组织成立证书，以及组织机构代码证；"
						+ "非法人创业投资企业还须提交国家商务部颁发的《外商投资企业批准证书》"
						+ "或省级以上创业投资管理部门出具的创投企业备案文件证明及复印件；\n"
						+ "（4）合伙协议或投资各方签署的创业投资企业合同及章程（需加盖企业公章）；\n"
						+ "（5）全体合伙人或投资者名单、有效身份证明文件原件及复印件；\n"
						+ "（6）经办人有效身份证明文件及复印件，执行事务合伙人或负责人证明书（需加盖企业公章），"
						+ "执行事务合伙人或负责人有效身份证明文件及复印件，"
						+ "加盖企业公章的执行事务合伙人或负责人对经办人的授权委托书"
						+ "（合伙企业执行事务合伙人，以合伙企业营业执照上的记载为准，"
						+ "有多名合伙企业执行事务合伙人的，由其中一名在授权书上签字；执行事务合伙人是法人或者其他组织的，"
						+ "由其委派代表在授权书上签字）。\n"
						+ "\n"
						+ "7.境外机构，在申请开立B股账户时，需要携带以下材料：\n"
						+ "（1）《证券账户开立申请表》；\n"
						+ "（2）经认证或公证的有效商业登记证明文件；对于无商业注册登记证明文件的境外机构，"
						+ "其有效身份证明文件是与商业注册登记证明文件具有相同法律效力的可证明其机构设立的文件，"
						+ "如基金注册文件、基金设立公告、基金与其托管机构签订的托管协议、托管机构对开户申请人身份的确认文件、"
						+ "或境外开户代理机构对开户申请人身份的确认文件等；\n"
						+ "（3）经公证或认证的董事会或董事、主要股东等出具的授权委托书，以及授权人的有效身份证明文件复印件，"
						+ "或符合规定的其他相关授权书；\n"
						+ "（4）经公证或认证的公司章程、董事会决议、股东会决议等能够证明授权主体具有合法授权资格的相关证明文件；\n"
						+ "（5）经办人有效身份证明文件及复印件。");

		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		// TODO Auto-generated method stub
		return null;
	}

}
