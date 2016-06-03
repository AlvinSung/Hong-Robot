package com.founder.chatRobot.domain;

import java.util.concurrent.atomic.AtomicBoolean;


final public class Constants {

	public static AtomicBoolean guideEnable = new AtomicBoolean(true);//确保dba只初始化一次
	public static AtomicBoolean indexEnable = new AtomicBoolean(true);//确保建索引只启动一次
	public static AtomicBoolean installEnable = new AtomicBoolean(true);//允许初始化开关
	
	// 点属性
	public static final String classProperty = "classproperty";//节点类型，如primitiveClass义原类型
	public static final String nameProperty = "name";//主要显示字段，如“取                  	V    	seek|谋取”中词语的“取”， 义原的“谋取”
	public static final String senseTypeProperty = "sensetypeproperty";//词语的类型，如 V、N、ADJ等
	public static final String primitiveIdProperty = "primitiveidproperty";//义原的ID，WHOLE.DAT用于上义集判定
	public static final String dependenceWeight = "weight";//匹配节点权值，表达式总共100,各节点各占部分
	public static final String hyponymNumberProperty = "hyponymnumberproperty";//下义词数量
	public static final String limitHypernymProperty = "limithypernymproperty";//最顶的x层义原标识
	public static final String deleteEdgePropery = "deleteedgepropery";//标记不使用的边
	public static final String originalIdProperty = "orighnalidproperty";//临时数据库用于标记原数据库节点Id
	public static final String taskNumberProperty = "tasknumberproperty";//分类标记语句类型Id
	public static final String possibleEntityProperty = "possibleentityproperty";//通过依存结构发现可能实体

	//public static final String unique = KnowledgeMapConstants.unique;//知识图谱中节点唯一索引
	public static final String knowledgeMapId = "knowledgeMapId".toLowerCase();//知识图谱中节点的titan id
	public static final String productSortExpandSortAndValuesProperty = "expandSortAndValues";
	public static final String productSortPropMeasuresProperty = "propMeasures";
	public static final String productSortQingDanProperty = "qingDans";
	
	
	// 边属性
	public static final String relationNotProperty = "relationnotproperty";//是否抽取否定事实
	public static final String relationNumberProperty = "relationnumberproperty";//关系的添加数量
	public static final String relationTaskPropertyPrefix = "relationtaskpropertyprefix";//分类标记的属性前缀，加到分类名字前
	public static final String pairFatherIdProperty = "pairfatheridproperty";//一对节点同父节点的id
	
	// 类型
	public static final String primitiveClass = "primitiveclass";//义原节点标志
	public static final String expressionClass = "expressionclass";//表达式节点标志
	public static final String taskClass = "taskclass";//表达式关系到的任务节点
	public static final String sentenceClass = "sentenceclass";//语句节点
	public static final String senseClass = "senseclass";//词语节点
	public static final String orVertexClass = "orvertexclass";//表达式中的or节点
	public static final String andVertexClass = "andvertexclass";//表达式中的and节点
	//public static final String productSortClass = KnowledgeMapConstants.productSortClass;//知识图谱中productsort节点

	// 点间关系
	public static final String dependenceRelation = "dependencerelation";//依存关系
	public static final String derivationRelation = "derivationrelation";//来源关系
	public static final String pairDependenceRelation = "pairdependencerelation";//一对节点的依存关系
	public static final String pairDerivationRelation = "pairderivationrelation";//一对节点的来源关系
	public static final String orVertexToAndVertexRelation = "orvertextoandvertexrelation";//表达式中or节点到and节点关系
	public static final String andVertexToPrimitiveRelation = "andvertextoprimitiverelation";//表达式中and节点到义原节点关系

	public static final String sentenceToSenseRelation = "sentencetosenserelation";//语句节点到词语节点关系
	public static final String senseToPrimitiveRelation = "sensetoprimitiverelation";//词语节点到义原节点关系
	public static final String senseToOrVertexRelation = "sensetoorvertexrelation";//词语节点到义原节点关系
	public static final String dealPrimitiveRelation = "dealprimitiverelation";//词语节点义原节点,表示是可以使用的关系
	public static final String relationPrimitiveRelation = "relationprimitiverelation";//词语通过关系关联到义原的关系
	public static final String symbolPrimitiveRelation = "symbolprimitiverelation";//词语通过符号关联到义原的关系

	public static final String hypernymRelation = "hypernymrelation";//上义关系
	public static final String hyponymRelation = "hyponymrelation";//下义关系

	public static final String taskRelationSentenceRelation = "taskrelationrentencerelation";//任务关系到语句的关系
	public static final String taskRelationExpressionRelation = "taskrelationexpressionrelation";//任务关系到表达式的关系
	
	// 事实类型、事实提取相关
	public static final String extractRelation = "extractrelation";//抽取事实的关系
	public static final String orderNumberFact = "ordernumberfact";//订单号事实
	public static final String orderAccountFact = "orderaccountfact";//下单账号事实
	public static final String orderTimeFact = "ordertimefact";//下单时间事实
	public static final String consigneeNameFact = "consigneenamefact";//收货人姓名事实
	public static final String consigneePhoneFact = "consigneephonefact";//收货人电话事实
	public static final String paymentMethodFact = "paymentmethodfact";//支付方式事实
	public static final String possibleEntityVertexFact = "possibleentityvertexFact";//概率实体关系
	
	//前缀
	public static final String notPrifix = "not";//否定的前缀
	public static final String wareSubTaskPrifix = "ware";//ware子任务前缀
	
	//后缀
	public static final String sameSuffix = "samesuffix";//自己关系到自己节点时创建的相同节点后缀
	public static final String taskSuffix = "task";//任务后缀
	public static final String subTaskSuffix = "subtask";//子任务后缀
	public static final String factSuffix = "fact";//事实后缀
	public static final String entitySuffix = "entity";//实体后缀
	
	//任务类型字段必须以“task”结尾
	public static final String orderQueryTask = "orderquerytask";//订单查询任务
	
	public static final String activityInformationQueryTask = "activityinformationquerytask";//活动信息查询任务
	public static final String activityEndTimeQueryTask = "activityendtimequerytask";//活动时间结束查询任务
	public static final String activityStartTimeQueryTask = "activitystarttimequerytask";//活动开始时间查询任务
	public static final String activityQueryTask = "activityquerytask";//活动查询任务
	
	public static final String productQueryComTask = "productQueryComTask".toLowerCase();//查询产品组合任务,既导购任务
	public static final String productQuerySortConfirmTask = "productQuerySortConfirmTask".toLowerCase();//三级分类确认任务
	public static final String productQueryFilterTask = "productQueryFilterTask".toLowerCase();//商品筛选任务
	
	public static final String productRecommendComTask = "productRecommendComTask".toLowerCase();//产品推荐流程
	
	public static final String productWareIdComTask = "productWareIdComTask".toLowerCase();//产品的wid任务，包括产品查询、属性查询、属性确认、活动咨询、配件查询
	
	public static final String productPropertyValueLessComTask = "productPropertyValueLessComTask".toLowerCase();//不关心当前属性任务
	public static final String productQueryResultComTask = "productQueryResultComTask".toLowerCase();//要推荐结果任务
	
	public static final String openClosingFile = "openClosing".toLowerCase();//开头结束语文件夹
	public static final String openingComTask = "openingComTask".toLowerCase();//开头语
	public static final String closingComTask = "closingComTask".toLowerCase();//结束语
	public static final String connectUserComtask = "connectUserComtask".toLowerCase();//需联系用户
	public static final String productFailureComtask = "productfailurecomtask".toLowerCase();//产品故障
	public static final String customerServiceComtask = "customerServiceComtask".toLowerCase();//用户联系人工客服
	public static final String examineGoodsComtask = "examineGoodsComtask".toLowerCase();//验货
	public static final String qualityGoodsComtask = "qualityGoodsComtask".toLowerCase();//商品是否正品
	
	
	/**
	 * 纯表情
	 */
	public static final String faceExpressionComTask = "faceExpressionComTask".toLowerCase();
	
	public static final String otherComTask = "otherComTask".toLowerCase();//其它未知任务
	public static final String otherFile = "other";//其它未知任务文件

	//子任务类型字段必须以“subfact”结尾
	//ware子任务必须以"ware"开头
	public static final String wareNoAnswerSubTask = "wareNoAnswerSubTask".toLowerCase();//暂不能处理
	public static final String wareQuerySubTask = "wareQuerySubTask".toLowerCase();//直接查询
	public static final String wareCompareSubTask = "wareCompareSubTask".toLowerCase();//商品比较
	public static final String wareStockQuerySubTask = "wareStockQuerySubTask".toLowerCase();//库存查询
	public static final String wareActivitySubTask = "wareActivitySubTask".toLowerCase();//活动
	public static final String wareAccessoryQuerySubTask = "wareAccessoryQuerySubTask".toLowerCase();//清单查询
	public static final String wareFittingConfirmSubTask = "wareFittingConfirmSubTask".toLowerCase();//配件确认
	public static final String warePriceQuerySubTask = "warePriceQuerySubTask".toLowerCase();//价格
	public static final String warePropertyQuerySubTask = "warePropertyQuerySubTask".toLowerCase();//细节查询
	public static final String wareConfirmQuerySubTask = "wareConfirmQuerySubTask".toLowerCase();//细节确认

	//事实类型字段必须以“fact”结尾
	public static final String productQuantifierFact = "productquantifierfact";//产品数、量词实体
	public static final String productSizeFact = "productSizeFact".toLowerCase();//产品尺寸
	public static final String productPropertyNameFact = "productPropertyNameFact".toLowerCase();//属性名
	public static final String productSortFact = "productsortfact";//产品分类事实 id
	public static final String productPropertyValueFact = "productPropertyValueFact".toLowerCase();//属性值
	public static final String productBrandFact = "productBrandFact".toLowerCase();//品牌-属性值
	public static final String productIdFact = "productIdFact".toLowerCase();//产品id事实
	public static final String activityFact = "activityFact".toLowerCase();//活动事实
	public static final String giftFact = "giftFact".toLowerCase();//赠品事实
	public static final String addressFact = "addressFact".toLowerCase();//地点事实
	public static final String decisionWordFact = "decisionWordFact".toLowerCase();//判定词事实（谓词事实）
	public static final String stockFact = "stockFact".toLowerCase();//库存事实
	public static final String accessoryFact = "accessoryFact".toLowerCase();//附件清单事实（直接用中文比较用）
	public static final String productCompareFact = "productCompareFact".toLowerCase();//商品比较事实
	public static final String phoneNumFact = "phoneNumFact".toLowerCase();//电话号码事实
	public static final String qingDanFact = "qingDanFact".toLowerCase();//附件清单事实（实体识别）
	
	//实体类别, 必须以 "entity" 结尾
	public static final String entityIdProperty = "entityidproperty";//实体的id属性
	public static final String entityTypeProperty = "entitytypeproperty";//实体的类型属性
	
	public static final String productIdEntity = "productidentity";//wareId实体
	public static final String productSortEntity = "productsortentity";//产品分类实体
	public static final String productSortEntityLayer = "productSortEntityLayer".toLowerCase();//产品分类实体
	
	public static final String productBrandEntity = "productbrandentity";//产品品牌实体
	public static final String productModelEntity = "productmodelentity";//产品型号实体
	
	public static final String productPropertyNameEntity = "productPropertyNameEntity".toLowerCase();//产品属性实体
	public static final String productPropertyValueEntity = "productpropertyvalueentity";//产品属性值实体
	public static final String activityNameEntity = "activitynameentity";//活动名实体

	public static final String productBrandModelEntity = "productbrandmodeentity";//产品品牌+型号实体
	
	public static final String productQuantifierEntity = "productquantifierentity";//产品数、量词实体
	public static final String productSizeEntity = "productsizeentity";//产品尺寸实体
	public static final String productColorEntity = "productcolorentity";//产品颜色实体
	
	/**
	 * 产品名部分实体
	 */
	public static final String productNamePartEntity = "productnamepartentity";
	
	//任务处理方式
	public static final String TASK_DEAL_DIRECT = "直接任务处理";
	public static final String TASK_DEAL_CLICK_INTERACTION = "点击交互";
	public static final String TASK_DEAL_DIRECT_SEARCH = "直接搜索商品";
	public static final String TASK_DEAL_JIMI = "jimi_faqChat";
	public static final String TASK_DEAL_MANAGER_ENGINE = "任务管理引擎";
	
	public static final String BUSINESS_CHAT_SERVER_KEY = "business_chat";

    public static final String COMMON_CHAT_SERVER_KEY = "common_chat";
    
    public static final String WORD_TYPE_SERVER_KEY = "wordtype";
    
    public static final String PRESALES_FAQ_SERVER_KEY = "presales_faq";

    public static final String SORT_SEPERATOR = "_";

    public static final String GETDOC_PREFIX = "getdoc#";

    public static final String SEARCH_PREFIX = "search#";

    public static final String ANSWER_PREFIX = "answer#";

    public static final String INDEX_CHAR_SMAE = "_char_same";
    public static final String INDEX_REVERSE = "_reverse";

	public static final String GENERAL_BORDER = "@#$";
	public static final String GENERAL_THIRD_SPLIT_SIGN_STRING = "-&:";//属性与属性之间的分隔符
	public static final char[] GENERAL_THIRD_SPLIT_SIGN = GENERAL_THIRD_SPLIT_SIGN_STRING.toCharArray();//属性与属性之间的分隔符
	public static final String GENERAL_SECOND_SPLIT_SIGN_STRING="&;:";
	public static final char[] GENERAL_SECOND_SPLIT_SIGN = GENERAL_SECOND_SPLIT_SIGN_STRING.toCharArray();//不同属性之间的分隔符
	public static final String GENERAL_FIRST_SPLIT_SIGN_STRING = ":-;";//同一属性之间的字段的分隔符
    public static final char[] GENERAL_FIRST_SPLIT_SIGN = GENERAL_FIRST_SPLIT_SIGN_STRING.toCharArray();//同一属性之间的字段的分隔符

    public static final String KNOWLEDGE_MAP_NAME = "knowledgeMap";
    public static final String OPTIOMIZE_MAP_NAME = "optiomizeMap";
    public static final String TASK_MAP_NAME = "taskMap";
    public static final String SENTENCE_MAP_NAME = "sentenceMap";
    public static final String SIMI_WARE_NAME = "simiWare";
    public static final String UPDATE_STORE_NAME = "updateStore";
    
    //返回消息提示key
    public static final String MSG_TIP = "tip"; //普通提示信息
    public static final String MSG_TXT = "txt"; //普通文本信息
    public static final String MSG_URL = "url"; //带url促销信息
    public static final String MSG_GIFT = "gift"; //赠品信息
    public static final String INFO_TYPE_PRODUCTSEARCH = "productsearch"; //商品查询类信息
    public static final String INFO_TYPE_PRODUCTGUIDE = "productguide"; //售前导购类信息
    public static final String INFO_TYPE_PROPSEARCH = "propsearch"; //商品相关属性类信息
    public static final String INFO_TYPE_COMMONMSG = "commonmsg"; //普通响应类信息
    public static final String INFO_TYPE_PROMOTION = "promotion"; //促销活动类信息，包括赠品
    public static final String INFO_TYPE_GIFTMSG = "giftmsg"; //赠品类信息
    
    //前一个任务的各种值:包括环境主任务、环境子任务、环境事实类型(productidentity)、环境事实值(867054)
    public static final String MAINTASKTYPE = "mainTaskType";//主任务
    public static final String SUBTASKTYPE = "subTaskType";//子任务
    public static final String TASKFACTTYPE = "taskFactType";//环境事实类型
    public static final String TASKFACTVALUE = "taskFactValue";//环境事实值
    
    /**
     * 每一个商品连接的相似商品个数
     */
    public static final int SIMILARGOODCOUNT = 10;
    
    /**
     * 透传线上:商品属性名和属性值的分隔符
     */
    public static final String PRODUCTPROPNAMEANDVALUESPLIT = ":=;";
    
    /**
     * 透传线上：商品属性之间的分隔符
     */
    public static final String PRODUCTPROPSPLIT = "@##@";
    
    //商品属性接口的常量
    /**
     * 商品上下柜标识字段
     */
    public static final String PRODUCTONWALLFLAG = "state";
    /**
     * 商品上柜状态
     */
    public static final String PRODUCTONWALL = "1";
    
    public static final String canClearFlag = "<clear>";
    //public static final String productFlag = "<" + productSortClass + ">";
    
    public static final String JSS_BUCKET_PRESALES = "com.jd.jimi.presales";
    
    public static final String faceExpressionReg = "^\\s*\\#E-[\u4e00-\u9fa5]{2}\\s*" + "|" + "^\\s*<a class=\"image-file\" href=\"http://misc.360buyimg.com/chat/im/pic/[^\"]*\">.*?</a>\\s*";
    
    public static final int TIME_LOG_LIMIT = 50;
}
