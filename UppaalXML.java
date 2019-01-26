
public class UppaalXML {
	public class LabelKind{
		final static int INVALIANT=0,COMMENTS=1,SELECT=2,GUARD=3,SYNCHRONISATION=4,ASSIGNMENT=5,NAME=6;
	}
	public class LocState{
		final static int NORMAL=0,URGENT=1,COMMITTED=2;
	}
 public static class Label{
	 int kind;
	 int x;
	 int y;
	 String context;
 }
 
 public static class Nail{
	 int x;
	 int y;
 }
 
 public static class Transition{
	 int sourceid;
	 int targetid;
	 Label[] label;
	 Nail[] nail;
 }
 
 public static class Location{
	 int id;
	 int x;
	 int y;
	 int state;
	 Label[] label;
	 boolean init;
 }
 
 public static class Template{
	 String name;
	 String declaration;
	 Location[] location;
	 Transition[] transition;
 }
 
 public static class XML{
	 String declaration;
	 Template[] template;
 }
 
 public static Label labelSet(int k,int x,int y,String s) {
	 Label label = new Label();
	 label.kind = k;
	 label.x = x;
	 label.y = y;
	 label.context = s;
	 return label;
 }
 
 public static Nail nailSet(int x,int y) {
	 Nail nail = new Nail();
	 nail.x = x;
	 nail.y = y;
	 return nail;
 }
 
 public static Transition transSet(int sid,int tid,Label[] label,Nail[] nail) {
	 Transition trans = new Transition();
	 trans.sourceid = sid;
	 trans.targetid = tid;
	 if(label != null) {
		 trans.label = new Label[label.length];
		 for(int i=0;i<trans.label.length;i++) {
			 trans.label[i] = label[i];
		 }
	 }
	 else {
		 trans.label = null;
	 }
	 if(nail != null) {
		 trans.nail = new Nail[nail.length];
		 for(int i=0;i<trans.nail.length;i++) {
			 trans.nail[i] = nail[i];
		 }
	 }
	 else {
		 trans.nail = null;
	 }
	 return trans;
 }
 
 public static Location locSet(int id,int x,int y,int state,Label[] label,boolean init) {
	 Location loc = new Location();
	 loc.id = id;
	 loc.x = x;
	 loc.y = y;
	 loc.state = state;
	 if(label != null) {
		 loc.label = new Label[label.length];
		 for(int i=0;i<loc.label.length;i++) {
			 loc.label[i] = label[i];
		 }
	 }
	 else {
		 loc.label = null;
	 }
	 loc.init = init;
	 return loc;
 }
 
 public static Template tempSet(String name,String dec,Location[] loc,Transition[] trans) {
	 Template temp = new Template();
	 temp.name = name;
	 temp.declaration =dec;
	 if(loc != null) {
		 temp.location = new Location[loc.length];
		 for(int i=0;i<temp.location.length;i++) {
			 temp.location[i] = loc[i];
		 }
	 }
	 else {
		 temp.location = null;
	 }
	 if(trans != null) {
		 temp.transition = new Transition[trans.length];
		 for(int i=0;i<temp.transition.length;i++) {
			 temp.transition[i] = trans[i];
		 }
	 }
	 else {
		 temp.transition = null;
	 }
	 return temp;
 }
 
 public static XML xmlSet(String dec,Template[] temp) {
	 XML xml = new XML();
	 xml.declaration = dec;
	 if(temp != null) {
		 xml.template = new Template[temp.length];
		 for(int i=0;i<xml.template.length;i++) {
			 xml.template[i] = temp[i];
		 }
	 }
	 else {
		 xml.template = null;
	 }
	 return xml;
 }
 //xmlで使用できない文字を変換
 static String xmlRewrite(String str) {
	 String afterstr = str;
	 afterstr = afterstr.replaceAll("&", "&amp;");
	 afterstr = afterstr.replaceAll("<", "&lt;");
	 afterstr = afterstr.replaceAll(">", "&gt;");
	 afterstr = afterstr.replaceAll("\"", "&quot;");
	 afterstr = afterstr.replaceAll("\'", "sqm");
	 return afterstr;
 }
 //xmlファイルを作成
 public static String xmlStr(XML xml) {
	 String str;
	 String tempname = "";
	 str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	 str = str + "<!DOCTYPE nta PUBLIC \"-//Uppaal Team//DTD Flat System 1.1//EN\" \"http://www.it.uu.se/research/group/darts/uppaal/flat-1_1.dtd\">\n";
	 str = str + "<nta>\n";
	 str = str + "<declaration>" + xmlRewrite(xml.declaration) + "</declaration>\n";
	 if(xml.template != null) {
		 for(int i=0;i<xml.template.length;i++) {
			 str = str + tempStr(xml.template[i]);
			 if(i == xml.template.length - 1) {
				 tempname = tempname + xml.template[i].name + ";";
			 }
			 else {
				 tempname = tempname + xml.template[i].name + ",";
			 }
		 }
	 }
	 str = str + "<system>\n";
	 str = str + "system " + tempname;
	 str = str + "</system>\n";
	 str = str + "</nta>\n";
	 return str;

 }
 //Templateノードを作成
 static String tempStr(Template temp) {
	 String str;
	 int initloc=0;
	 str = "<template>\n";
	 str = str + "<name>" + xmlRewrite(temp.name) + "</name>\n";
	 str = str + "<declaration>" + xmlRewrite(temp.declaration) + "</declaration>\n";
	 if(temp.location != null) {
		 for(int i=0;i<temp.location.length;i++) {
			 str = str + locStr(temp.location[i]);
			 if(temp.location[i].init == true) {
				 initloc = temp.location[i].id;
			 }
		 }
	 }
	 str = str + "<init ref=\"id" + initloc + "\" />\n";
	 if(temp.transition != null) {
		 for(int i=0;i<temp.transition.length;i++) {
			 str = str + transStr(temp.transition[i]);
		 }
	 }
	 str = str + "</template>\n";
	 return str;
 }
 //Locationノードを作成
 static String locStr(Location loc) {
	 String str;
	 str = "<location id=\"id" + loc.id + "\" x=\"" + loc.x + "\" y=\"" + loc.y + "\">\n";
	 if(loc.label != null) {
		 for(int i=0;i<loc.label.length;i++) {
			 str = str + labelStr(loc.label[i]);
		 }
	 }
	 switch(loc.state) {
	 case LocState.URGENT:
		 str = str + "<urgent />\n";
		 break;
	 case LocState.COMMITTED:
		 str = str + "<committed />\n";
		 break;
	 default:
		 break;
	 }
	 str = str + "</location>\n";
	 return str;
 }
 //Transitionノードを作成
 static String transStr(Transition trans) {
	 String str = "";
	 if(trans != null) {
		 str = "<transition>\n";
		 str = str + "<source ref=\"id" + trans.sourceid + "\" />\n";
		 str = str + "<target ref=\"id" + trans.targetid + "\" />\n";
		 if(trans.label != null) {
			 for(int i=0;i<trans.label.length;i++) {
				 str = str + labelStr(trans.label[i]);
			 }
		 }
		 if(trans.nail != null) {
			 for(int i=0;i<trans.nail.length;i++) {
				 str = str + nailStr(trans.nail[i]);
			 }
		 }
		 str = str + "</transition>\n";
	 }
	 return str;
 }
 //Nailノードを作成
 static String nailStr(Nail nail) {
	 String str;
	 str = "<nail x=\"" + nail.x + "\" y=\"" + nail.y + "\" />\n"; 
	 return str;
 }
 //Labelノードを作成
 static String labelStr(Label label) {
	 String str = "";
	 switch(label.kind) {
	 case LabelKind.INVALIANT:
		 str = "<label kind=\"invariant\" x=\"" + label.x + "\" y=\"" + label.y + "\">" + xmlRewrite(label.context) + "</label>\n"; 
		 break;
	 case LabelKind.COMMENTS:
		 str = "<label kind=\"comments\">" + xmlRewrite(label.context) + "</label>\n"; 
		 break;
	 case LabelKind.SELECT:
		 str = "<label kind=\"select\" x=\"" + label.x + "\" y=\"" + label.y + "\">" + xmlRewrite(label.context) + "</label>\n"; 
		 break;
	 case LabelKind.GUARD:
		 str = "<label kind=\"guard\" x=\"" + label.x + "\" y=\"" + label.y + "\">" + GuardConvert(xmlRewrite(label.context)) + "</label>\n"; 
		 break;
	 case LabelKind.SYNCHRONISATION:
		 str = "<label kind=\"synchronisation\" x=\"" + label.x + "\" y=\"" + label.y + "\">" + xmlRewrite(label.context) + "</label>\n"; 
		 break;
	 case LabelKind.ASSIGNMENT:
		 str = "<label kind=\"assignment\" x=\"" + label.x + "\" y=\"" + label.y + "\">" + xmlRewrite(label.context) + "</label>\n"; 
		 break;
	 case LabelKind.NAME:
		 str = "<name x=\"" + label.x + "\" y=\"" + label.y + "\">" + xmlRewrite(label.context) + "</name>\n"; 
		 break;
	 }
	 return str;
 }
//ガード条件をUppaal用に変換
 static String GuardConvert(String str){
	String guard = str;
	guard = SupportFunc.replace(guard,"||"," or ");
	guard = SupportFunc.replace(guard,"&&"," and ");
	guard = SupportFunc.replace(guard,"/="," != ");
	guard = SupportFunc.replace(guard,"Event()"," Event ");
	return guard;
}
 
 int LocGrep(Location[] loc,String name){
	 for(int i=0;i<loc.length;i++){
		 if(loc[i].label != null){
			 for(int j=0;j<loc[i].label.length;j++){
				 if(loc[i].label[j].kind == LabelKind.NAME && loc[i].label[j].context.equals(name)){
					 return i;
				 }
			 }
		 }
	 }
	 return -1;
 }

 Location[] addLocation(Location[] list,Location data){
	Location[] after;
	if(list == null){
			after = new Location[1];
			after[0] = data;
	}
	else{
			after = new Location[list.length + 1];
			for(int i=0;i<list.length;i++){
					after[i] = list[i];
			}
	after[list.length] = data;
	}
	return after;
 }

 Transition[] addTransition(Transition[] list,Transition data){
	Transition[] after;
	if(list == null){
			after = new Transition[1];
			after[0] = data;
	}
	else{
			after = new Transition[list.length + 1];
			for(int i=0;i<list.length;i++){
					after[i] = list[i];
			}
	after[list.length] = data;
	}
	return after;
 }
}
