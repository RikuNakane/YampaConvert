import java.io.StringReader;
import java.util.regex.Pattern;

public class UppaalXMLMake extends UppaalXML{
    class D{
		final static int XDIS = 200,YDIS = 100;
    }
    class CODE{
        final static String FCODE = "int power(int x,int n){\n  int num=1,i;\n  for(i=0;i<n;i++) num = num * x;\n  return num;\n}\n\nint positive(int top,int low){\nif(top < 0 || (top == 0 && low < 0)) return 0;\nelse return 1;\n}\n\nint IncNum(int top,int low){\n  if(top < 0 && low > 0) return (low/10000) + 1;\n  else if(top > 0 && low < 0) return (low/10000) - 1;\n  else return low/10000;\n}\n\nint add(int top1,int low1,int top2,int low2,int class){\n  int top,low,inc;\n  top = top1 + top2;\n  low = low1 + low2;\n  inc = IncNum(top,low);\n  if(class == TOP) return top + inc;\n  else return low - inc*10000;\n} \n\nint sub(int top1,int low1,int top2,int low2,int class){\n  int top,low,inc;\n  top = top1 - top2;\n  low = low1 - low2;\n  inc = IncNum(top,low);\n  if(class == TOP) return top + inc;\n  else return low - inc*10000;\n} \n\nint addsub3(int top1,int low1,int lowlow1,int top2,int low2,int lowlow2,int class){\n  int top,low,lowlow,inc;\n  lowlow = add(low1,lowlow1,low2,lowlow2,LOW);\n  low = add(low1,lowlow1,low2,lowlow2,TOP);\n  top = top1+top2;\n  inc = IncNum(top,low);\n  if(class == TOP) return top + inc;\n  else if(class == LOW) return low - inc*10000;\n  else return lowlow;\n}\n\nint mult(int top1,int low1,int top2,int low2,int class){\n  int t1=top1,t2=top2,l1=low1,l2=low2;\n  int num1[10],num2[10],i,j,num[20],answer=0,flag=1,t,n1,n2;\n  for(i=0;i<20;i++) num[i]=0;\n  if(positive(top1,low1) && !positive(top2,low2)){\n    t2 = -t2;\n    l2 = -l2;\n    flag = -1;\n  }\n  else if(!positive(top1,low1) && positive(top2,low2)){\n    t1 = -t1;\n    l1 = -l1;\n    flag = -1;\n  }\n  else if(!positive(top1,low1) && !positive(top2,low2)){\n    t1 = -t1;\n    t2 = -t2;\n    l1 = -l1;\n    l2 = -l2;\n  }\n  \n  for(i=0;i<4;i++){\n    num1[i] = (l1/power(10,i))%10;\n    num2[i] = (l2/power(10,i))%10;\n    num1[i+4] = (t1/power(10,i))%10;\n    num2[i+4] = (t2/power(10,i))%10;\n  }\n\n  for(i=0;i<8;i++){\n    for(j=0;j<8;j++){\n      t = num1[i] * num2[j];\n      num[i+j] += t%10;\n      num[i+j+1] += t/10;\n    }\n  }\n  for(i=0;i<15;i++){\n    num[i+1] += num[i]/10;\n    num[i] = num[i]%10;\n  }\n  if(class == LOW){\n    for(i=0;i<4;i++){\n      answer += num[i+4] * power(10,i);\n    }\n  }\n  else{\n    for(i=0;i<4;i++){\n      answer += num[i+8] * power(10,i);\n    }\n  }\n  return answer * flag;\n} \n\nint div(int top1,int low1,int top2,int low2,int class){\n  int t1=top1,t2=top2,l1=low1,l2=low2,ll1=0,ll2=0;\n  int top=0,low=0,flag=1,i,j,t,l,ll;\n\n  if(positive(top1,low1) && !positive(top2,low2)){\n    t2 = -t2;\n    l2 = -l2;\n    flag = -1;\n  }\n  else if(!positive(top1,low1) && positive(top2,low2)){\n    t1 = -t1;\n    l1 = -l1;\n    flag = -1;\n  }\n  else if(!positive(top1,low1) && !positive(top2,low2)){\n    t1 = -t1;\n    t2 = -t2;\n    l1 = -l1;\n    l2 = -l2;\n  }\n\n  for(i=0;t1 > t2 || (t1==t2 && l1>=l2);i++){ \n    top++;\n    t=addsub3(t1,l1,ll1,-t2,-l2,-ll2,TOP);\n    l=addsub3(t1,l1,ll1,-t2,-l2,-ll2,LOW);\n    ll=addsub3(t1,l1,ll1,-t2,-l2,-ll2,LOWLOW);\n    t1 = t;\n    l1 = l;\n    ll1 = ll;\n  }\n\n  for(i=0;i<4;i++){\n    ll2 = ll2/10 + (l2%10)*1000;\n    l2 = l2/10 + (t2%10)*1000;\n    t2 = t2/10;\n    low=low*10;\n    if(t2 != 0 || l2 != 0){\n      for(j=0;t1 > t2 || (t1==t2 && l1>=l2);j++){\n	low++;\n	t=addsub3(t1,l1,ll1,-t2,-l2,-ll2,TOP);\n	l=addsub3(t1,l1,ll1,-t2,-l2,-ll2,LOW);\n	ll=addsub3(t1,l1,ll1,-t2,-l2,-ll2,LOWLOW);\n	t1 = t;\n	l1 = l;\n	ll1 = ll;\n      } \n    }\n  }\n  if(class == TOP) return top * flag;\n  else return low * flag;\n}\n\nbool compareL(int top1,int low1,int top2,int low2){\n  if(top1 < top2) return true;\n  else if(top1 > top2) return false;\n  else if(low1 < low2) return true;\n  else return false;\n}\n\nbool comparR(int top1,int low1,int top2,int low2){\n  if(top1 > top2) return true;\n  else if(top1 < top2) return false;\n  else if(low1 > low2) return true;\n  else return false;\n}\n\nbool compareEL(int top1,int low1,int top2,int low2){\n  if(top1 < top2) return true;\n  else if(top1 > top2) return false;\n  else if(low1 <= low2) return true;\n  else return false;\n}\n\nbool compareER(int top1,int low1,int top2,int low2){\n  if(top1 > top2) return true;\n  else if(top1 < top2) return false;\n  else if(low1 >= low2) return true;\n  else return false;\n}\n\nbool compareE(int top1,int low1,int top2,int low2){ return ((top1 == top2) && (low1 == low2));}\nint AND(int left,int right){return left && right;}int OR(int left,int right){return left || right;}int NOT(int bo){return !bo;}\n ";
        final static String DEC = "chan trig,chk;\nint Top_dt = 1;\nint Low_dt = 0;\nconst int Event = 1;\nconst int NoEvent = 0;\nconst int TOP = 0;\nconst int LOW = 1;\nconst int LOWLOW = 2;\nint Top_Reg1;\nint Low_Reg1;\nint Top_Reg2;\nint Low_Reg2;\nint Top_Reg3;\nint Low_Reg3;\nbool BoolReg1;\nbool BoolReg2;\nbool BoolReg3;\n\n\n";
    }
    //Make Uppaal xml file
    public String UppaalGet(String str,String[] args){
    	Function.Func[] funclist;
        String xmlstr = "";
        String dec = "";
        XML xml;
        Template[] temp = new UppaalXML.Template[2];
        funclist = funcread(str);
        temp[0] = TemplateMake(funclist);
        temp[1] = TemplateRT(temp[0].location.length);
        dec = DeclarationMake(funclist,args);
        xml = xmlSet(dec,temp);
        xmlstr = xmlStr(xml);
        return xmlstr;
    }
    //Get Functiom Information
     Function.Func[] funcread(String str){
        try{
            Function parser = new Function(new StringReader(str));
            parser.start();
            return parser.funcl;
        }
        catch (ParseException e){
            System.out.println("###" + e.getMessage());
            System.exit(0);
            return null;
        }
    }
    String calcread(String str,String address,String input){
        try{
            Calc parser = new Calc(new StringReader(str));
            parser.start(address,input);
            return parser.update;
        }
        catch (ParseException e){
            System.out.println("###" + e.getMessage());
            System.exit(0);
            return null;
        }
    }
    String eventguardread(String str,String address){
        try{
            Calc parser = new Calc(new StringReader(str));
            parser.guard(address);
            return parser.update;
        }
        catch (ParseException e){
            System.out.println("###" + e.getMessage());
            System.exit(0);
            return null;
        }
    }
    String numguardread(String str,String address,String input){
        try{
            Calc parser = new Calc(new StringReader(str));
            parser.numguard(address,input);
            return parser.update;
        }
        catch (ParseException e){
            System.out.println("###" + e.getMessage());
            System.exit(0);
            return null;
        }
    }
    String numguardread2(String str,String address){
        try{
            String[] line;
            Calc parser;
            String update = "";
            String not,and;
            if(!str.equals("")){
                line = str.split("\n");
                for(int i=0;i<line.length;i++){
                    not = "";
                    and = "";
                    if(line[i].contains("not")){
                        line[i]=line[i].replace("not","");
                        not = "not";
                    }
                    if(line[i].contains("and")){
                        line[i]=line[i].replace("and","");
                        and = "and";
                    }
                    parser = new Calc(new StringReader(line[i]));
                    update += not + " " + parser.numguard2(address) + " " + and + "\n";
                }
            }
            return update;
        }
        catch (ParseException e){
            System.out.println("###" + e.getMessage());
            System.exit(0);
            return null;
        }
    }
    //Make Discrete Time Model
    Template TemplateMake(Function.Func[] funclist){
        Template temp;
        Transition[] trans;
        Location[] loc;
        loc = LocationMake(funclist);
        trans = TransitionMake(funclist,loc);
        temp = tempSet("SwitchSignalFunction","",loc,trans);
        return temp;
    }
    //Make Location in Model
    Location[] LocationMake(Function.Func[] funclist){
        Location[] loclist = null;
        Location loc;
        int ssfnum = 0;
        Label[] label = new Label[1];
        for(int i=0;i<funclist.length;i++){
            if(funclist[i].type == Function.FuncType.SSF){
                loc = locSet(ssfnum*7,0,ssfnum*D.YDIS*6,LocState.NORMAL,null,false);
                loclist = addLocation(loclist,loc);
                if(ssfnum == 0){
                    loc = locSet(ssfnum*7+1,0,ssfnum*D.YDIS*6 + D.YDIS,LocState.COMMITTED,null,true);
                }
                else{
                    loc = locSet(ssfnum*7+1,0,ssfnum*D.YDIS*6 + D.YDIS,LocState.COMMITTED,null,false);
                }
                loclist = addLocation(loclist,loc);
                loc = locSet(ssfnum*7+2,D.XDIS,ssfnum*D.YDIS*6,LocState.COMMITTED,null,false);
                loclist = addLocation(loclist,loc);
                loc = locSet(ssfnum*7+3,D.XDIS*2,ssfnum*D.YDIS*6,LocState.COMMITTED,null,false);
                loclist = addLocation(loclist,loc);
                loc = locSet(ssfnum*7+4,D.XDIS*2,ssfnum*D.YDIS*6 + D.YDIS,LocState.COMMITTED,null,false);
                loclist = addLocation(loclist,loc);
                loc = locSet(ssfnum*7+5,D.XDIS*3,ssfnum*D.YDIS*6,LocState.COMMITTED,null,false);
                loclist = addLocation(loclist,loc);
                label[0] = labelSet(LabelKind.NAME,D.XDIS*3 + 10,ssfnum*D.YDIS*6 + D.YDIS,funclist[i].name + "_switch");
                loc = locSet(ssfnum*7+6,D.XDIS*3,ssfnum*D.YDIS*6 + D.YDIS,LocState.COMMITTED,label,false);
                loclist = addLocation(loclist,loc);
                ssfnum++;
            }
        }
    return loclist;
    }
    //Make Transition in Model
    Transition[] TransitionMake(Function.Func[] funclist,Location[] loc){
        Transition[] translist=null;
        Transition trans;
        int ssfnum = 0;
        Label[] label;
        Nail[] nail;
        Function.Func ssf;
        Function.Func sf;
        int sfid;
        String ssfname;
        String outputupdate;

        for(int i=0;i<funclist.length;i++){
            if(funclist[i].type == Function.FuncType.SSF){
                ssf = funclist[i];
                ssfname = SupportFunc.firstUpper(ssf.name);
                sfid = FuncGrep(funclist,ssf.ssf.arg1_f);
                sf = funclist[sfid];
                outputupdate = "";
                for(int j=0;j<sf.sf.output.length;j++){
                    outputupdate = outputupdate + "Top_" + "Output" + (j+1) + " := " + "Top_" + ssfname + "Output" + (j+1) + ",\n";
                    outputupdate = outputupdate + "Low_" + "Output" + (j+1) + " := " + "Low_" + ssfname + "Output" + (j+1) + ",\n";
                }
                outputupdate = outputupdate.substring(0,outputupdate.lastIndexOf(","));
                label = new Label[1];
                label[0] = UppaalXML.labelSet(LabelKind.SYNCHRONISATION, D.XDIS/2,ssfnum*D.YDIS*6, "trig?" );
                trans = transSet(ssfnum*7,ssfnum*7+2,label,null);
                translist = addTransition(translist, trans);
                nail = new Nail[2];
                label = new Label[2];
                label[0] = labelSet(LabelKind.SYNCHRONISATION, D.XDIS/2,ssfnum*D.YDIS*6-D.YDIS*2, "chk!" );
                label[1] = labelSet(LabelKind.ASSIGNMENT,D.XDIS/2,ssfnum*D.YDIS*6 - D.YDIS * 3,outputupdate);
                nail[0] = nailSet(D.XDIS*3, ssfnum*D.YDIS*6-D.YDIS*2);
                nail[1] = nailSet(0,ssfnum*D.YDIS*6-D.YDIS*2);
                trans = transSet(ssfnum*7+5,ssfnum*7,label,nail);
                translist = addTransition(translist, trans);    
                nail[0] = nailSet(D.XDIS*3, ssfnum*D.YDIS*6+D.YDIS*3);
                nail[1] = nailSet(0,ssfnum*D.YDIS*6+D.YDIS*3);
                trans = transSet(ssfnum*7+6,ssfnum*7+1,null,nail);
                translist = addTransition(translist, trans);
                trans = InitUpdateMake(funclist,funclist[i],ssfnum);
                translist = addTransition(translist, trans);
                trans = UpdateMake(funclist,funclist[i],ssfnum);
                translist = addTransition(translist, trans);
                translist = BranchMake1(funclist,funclist[i],translist,ssfnum);
                translist = BranchMake2(funclist,funclist[i],translist,ssfnum,loc);
                ssfnum++;
            }
        }
        return translist;
    }
    //Make Initial Update Sentence
    Transition InitUpdateMake(Function.Func[] funclist,Function.Func ssf, int ssfnum){
        Transition trans;
        Label[] label = new Label[1];
        String update = "";
        int sfid;
        Function.Func sf;
        String ssfname;
        String sfname;     
        sfid = FuncGrep(funclist,ssf.ssf.arg1_f);
        if(sfid < 0){
            System.out.println("###" + ssf.ssf.arg1_f + "can't be found\n");
            System.exit(0);
        }
        sf = funclist[sfid];
        ssfname = SupportFunc.firstUpper(ssf.name);
        sfname = SupportFunc.firstUpper(sf.name);
        if(sf.arg != null){
            for(int i=0;i<sf.arg.length;i++){
                update = update + "Top_" + ssfname + sfname + sf.arg[i] + " := " + "Top_" + ssfname + ssf.ssf.arg1_arg[i] + ",\n";   
                update = update + "Low_" + ssfname + sfname + sf.arg[i] + " := " + "Low_" + ssfname + ssf.ssf.arg1_arg[i] + ",\n";     
            }
        }
        if(sf.sf.input != null){
            for(int i=0;i<sf.sf.input.length;i++){
                update = update + "Top_" + sfname + sf.sf.input[i] + " := 0,\n";
                update = update + "Low_" + sfname + sf.sf.input[i] + " := 0,\n";
            }
        }

        update = update + InitUpdateSF(funclist,sf,ssfname);

        update = update.substring(0,update.lastIndexOf(","));
        label[0] = labelSet(LabelKind.ASSIGNMENT,0,ssfnum * 6 * D.YDIS,update);
		trans = transSet(ssfnum * 7 + 1,ssfnum * 7 + 2,label,null);
        return trans;
    }

    String InitUpdateSF(Function.Func[] funclist,Function.Func sf,String rootfuncname){
        String update = "";
        String sfname = SupportFunc.firstUpper(sf.name);
        Function.Func sf2;
        String sf2name;
        int sfid;
        String address;
        for(int i=0;i < sf.sf.process.length;i++){
            address = rootfuncname + sfname + (i+1);
            if(sf.sf.process[i].func.equals("integral")){
                update = update + "Top_" + address + "integral := 0,\n"; 
                update = update + "Top_" + address + "integral_input := 0,\n"; 
                update = update + "Top_" + address + "integral_inputPRE := 0,\n"; 
                update = update + "Low_" + address + "integral := 0,\n"; 
                update = update + "Low_" + address + "integral_input := 0,\n"; 
                update = update + "Low_" + address + "integral_inputPRE := 0,\n"; 
            }
            else if(sf.sf.process[i].func.equals("edge")){
                update = update + address + "edge_input := 1,\n"; 
                update = update + address + "edge_inputPRE := 1,\n"; 
            }
            else if((sfid = FuncGrep(funclist,sf.sf.process[i].func)) >= 0){
                sf2 = funclist[sfid];
                sf2name = SupportFunc.firstUpper(sf2.name);
                if(sf2.arg != null){
                    for(int j=0;j<sf2.arg.length;j++){
                        update = update + "Top_" + address + sf2name + sf2.arg[j] + " := " + "Top_" + rootfuncname + sfname + sf.sf.process[i].funcarg[j] + ",\n";   
                        update = update + "Low_" + address + sf2name + sf2.arg[j] + " := " + "Low_" + rootfuncname + sfname + sf.sf.process[i].funcarg[j] + ",\n";  
                    }
                }
                update = update + InitUpdateSF(funclist,sf2,address);
            }
        }
        return update;
    }
    //Make Update of Continuous Value and Detect Event Sentence  
    Transition UpdateMake(Function.Func[] funclist,Function.Func ssf,int ssfnum){
        Transition trans;
        Label[] label = new Label[1];
        String update;
        int sfid;
        Function.Func sf;
        String ssfname;
        sfid = FuncGrep(funclist,ssf.ssf.arg1_f);
        if(sfid < 0){
            System.out.println("###" + ssf.ssf.arg1_f + "can't be found\n");
            System.exit(0);
        }
        sf = funclist[sfid];
        ssfname = SupportFunc.firstUpper(ssf.name);
        
        update = UpdateSF(funclist,sf,ssfname);

        update = update.substring(0,update.lastIndexOf(","));
        label[0] = labelSet(LabelKind.ASSIGNMENT,D.XDIS,ssfnum * 6 * D.YDIS,update);
		trans = transSet(ssfnum * 7 + 2,ssfnum * 7 + 3,label,null);
        return trans;
        
    }

    String UpdateSF(Function.Func[] funclist,Function.Func sf,String rootfuncname){
        String update = "";
        String sfname = SupportFunc.firstUpper(sf.name);
        Function.Func sf2;
        String address;
        int sfid;
        String op;
        String token;
        String sf2name;
        String[] num;
        for(int i=0;i < sf.sf.process.length;i++){
            address = rootfuncname + sfname + (i+1);
            if(sf.sf.process[i].func.equals("integral")){ 
                update = update + calcread(sf.sf.process[i].input[0],sfname,address + "integral_input");
                update = update + "Top_Reg1 := " + "Top_" + address + "integral,\n";
                update = update + "Low_Reg1 := " + "Low_" + address + "integral,\n";                
                update = update + "Top_Reg2 := " + "mult(Top_" + address + "integral_inputPRE" + "," + "Low_" + address + "integral_inputPRE" + ",Top_dt,Low_dt,TOP)" + ",\n";
                update = update + "Low_Reg2 := " + "mult(Top_" + address + "integral_inputPRE" + "," + "Low_" + address + "integral_inputPRE" + ",Top_dt,Low_dt,LOW)" + ",\n";
                update = update + "Top_" + address + "integral := add(Top_Reg1,Low_Reg1,Top_Reg2,Low_Reg2,TOP)" + ",\n";
                update = update + "Low_" + address + "integral := add(Top_Reg1,Low_Reg1,Top_Reg2,Low_Reg2,LOW)" + ",\n";
                update = update + "Top_" + sfname + sf.sf.process[i].output[0] + " := " + "Top_" + address + "integral,\n";
                update = update + "Low_" + sfname + sf.sf.process[i].output[0] + " := " + "Low_" + address + "integral,\n";
                update = update + "Top_" + address + "integral_inputPRE := " + "Top_" + address + "integral_input,\n"; 
                update = update + "Low_" + address + "integral_inputPRE := " + "Low_" + address + "integral_input,\n"; 
            }
            else if(sf.sf.process[i].func.equals("edge")){
                update = update + numguardread(sf.sf.process[i].input[0],sfname,address + "edge_input"); 
                update = update + sfname + sf.sf.process[i].output[0] + " := " + address + "edge_input and (not " + address + "edge_inputPRE" + "),\n";  
                update = update + address + "edge_inputPRE := " + address + "edge_input,\n"; 
            }
            else if(sf.sf.process[i].func.equals("arr")){
                op = sf.sf.process[i].funcarg[0];
                token = sf.sf.process[i].funcarg[1];

                if(op.equals("+")){
                    op = "add";
                }
                else if(op.equals("-")){
                    op = "sub";
                }
                if(op.equals("*")){
                    op = "mult";
                }
                else if(op.equals("/")){
                    op = "div";
                }

                if(token.contains(".")){
                    num = Calc.numVal(token);
                    update = update + "Top_" + sfname + sf.sf.process[i].output[0] + " := " + op + "(" + "Top_" + sfname + sf.sf.process[i].input[0] + "," + "Low_" + sfname + sf.sf.process[i].input[0] + "," + num[0] + "," + num[1] + ",TOP)" + ",\n";
                    update = update + "Low_" + sfname + sf.sf.process[i].output[0] + " := " + op + "(" + "Top_" + sfname + sf.sf.process[i].input[0] + "," + "Low_" + sfname + sf.sf.process[i].input[0] + "," + num[0] + "," + num[1] + ",LOW)" + ",\n";
                }
                else if(Pattern.compile("^-?[0-9]+$").matcher(token).find()){
                        update = update + "Top_" + sfname + sf.sf.process[i].output[0] + " := " + op + "(" + "Top_" + sfname + sf.sf.process[i].input[0] + "," + "Low_" + sfname + sf.sf.process[i].input[0] + "," + token + ",0,TOP)" + ",\n";
                        update = update + "Low_" + sfname + sf.sf.process[i].output[0] + " := " + op + "(" + "Top_" + sfname + sf.sf.process[i].input[0] + "," + "Low_" + sfname + sf.sf.process[i].input[0] + "," + token + ",0,LOW)" + ",\n";
                }

                else{
                        update = update + "Top_" + sfname + sf.sf.process[i].output[0] + " := " + op + "(" + "Top_" + sfname + sf.sf.process[i].input[0] + "," + "Low_" + sfname + sf.sf.process[i].input[0] + "," + "Top_" + rootfuncname + sfname + token + "," + "Low_" + rootfuncname + sfname + token + ",TOP)" + ",\n";
                        update = update + "Low_" + sfname + sf.sf.process[i].output[0] + " := " + op + "(" + "Top_" + sfname + sf.sf.process[i].input[0] + "," + "Low_" + sfname + sf.sf.process[i].input[0] + "," + "Top_" + rootfuncname + sfname + token + "," + "Low_" + rootfuncname + sfname + token + ",LOW)" + ",\n";
                }
            }
            else if((sfid = FuncGrep(funclist,sf.sf.process[i].func)) >= 0){
                sf2 = funclist[sfid];
                sf2name = SupportFunc.firstUpper(sf2.name);
                if(sf2.sf.input != null){
                    for(int j=0;j<sf2.sf.input.length;j++){
                        update = update + "Top_" + sf2name + sf2.sf.input[j] + " := " + "Top_" + sfname + sf.sf.process[i].input[j] + ",\n";
                        update = update + "Low_" + sf2name + sf2.sf.input[j] + " := " + "Low_" + sfname + sf.sf.process[i].input[j] + ",\n";
                    }
                }
                update = update + UpdateSF(funclist,sf2,address);
                for(int j=0;j<sf2.sf.output.length;j++){
                        update = update + "Top_" + sfname + sf.sf.process[i].output[j] + " := " + "Top_" + sf2name + sf2.sf.output[j] + ",\n";  
                        update = update + "Low_" + sfname + sf.sf.process[i].output[j] + " := " + "Low_" + sf2name + sf2.sf.output[j] + ",\n";   
                }
            }
        }
        return update;
    }
    //Make Event Branch Transition 
    Transition[] BranchMake1(Function.Func[] funclist,Function.Func ssf,Transition[] translist,int ssfnum){
        String ssfname = SupportFunc.firstUpper(ssf.name);
        String sfname;
        String fname;
        int sfid;
        int fid;
        Function.Func sf;
        Function.Func f;
        String outputupdate = "";
        String eventupdate = "";
        Label[] label = new Label[2];
        Nail[] nail = new Nail[1];
        int eventnum = 0;
        int noeventnum = 0;
        Transition trans;
        Transition[] aftertranslist = translist;
        sfid = FuncGrep(funclist,ssf.ssf.arg1_f);
        sf = funclist[sfid];
        fid = FuncGrep(funclist,ssf.ssf.arg2_f);
        f = funclist[fid];
        sfname = SupportFunc.firstUpper(sf.name);
        fname = SupportFunc.firstUpper(f.name);
        for(int i=0;i<sf.sf.output.length;i++){
            outputupdate = outputupdate + "Top_" + ssfname + "Output" + (i+1) + " := " + "Top_" + sfname + sf.sf.output[i] + ",\n";
            outputupdate = outputupdate + "Low_" + ssfname + "Output" + (i+1) + " := " + "Low_" + sfname + sf.sf.output[i] + ",\n";
        }
        outputupdate = outputupdate.substring(0,outputupdate.lastIndexOf(","));
        for(int i=0;i < sf.sf.event.length;i++){
            eventupdate = "";
            if(f.arg != null) {
            	for(int j=0;j<f.arg.length;j++){
                    eventupdate = eventupdate + calcread(sf.sf.event[i].expr[j],sfname,fname + f.arg[j]);
            	}
            }
            eventupdate = eventupdate.substring(0,eventupdate.lastIndexOf(","));
            if(sf.sf.event[i].eventval.equals("NoEvent")){
                label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + noeventnum * D.XDIS,ssfnum * D.YDIS * 6,eventguardread(sf.sf.event[i].guard,sfname));
                label[1] = labelSet(LabelKind.ASSIGNMENT,D.XDIS * 2 + noeventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS/2,outputupdate);
                nail[0] = nailSet(D.XDIS * 2 + D.XDIS/2,ssfnum*D.YDIS*6 + noeventnum * 10);
                trans = transSet(ssfnum*7 + 3,ssfnum*7 + 5,label,nail);
                aftertranslist = addTransition(aftertranslist,trans);
                noeventnum++;
            }

            else if(sf.sf.event[i].eventval.equals("Event()")){
                label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + eventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS,eventguardread(sf.sf.event[i].guard,sfname));
                label[1] = labelSet(LabelKind.ASSIGNMENT,D.XDIS * 2 + eventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS*3/2,eventupdate);
                nail[0] = nailSet(D.XDIS * 2 + eventnum * 10,ssfnum*D.YDIS*6 + D.YDIS/2);
                trans = transSet(ssfnum*7 + 3,ssfnum*7 + 4,label,nail);
                aftertranslist = addTransition(aftertranslist,trans);
                eventnum++;
            }
            else{
                if(sf.sf.event[i].guard.equals("")){
                    label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + noeventnum * D.XDIS,ssfnum * D.YDIS * 6, "not(" + sfname + sf.sf.event[i].eventval + " /= NoEvent)");
                }
                else{
                    label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + noeventnum * D.XDIS,ssfnum * D.YDIS * 6, eventguardread(sf.sf.event[i].guard,sfname) + " and\nnot(" + sf.sf.event[i].eventval + " /= NoEvent)");
                }
                label[1] = labelSet(LabelKind.ASSIGNMENT,D.XDIS * 2 + noeventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS/2,outputupdate);
                nail[0] = nailSet(D.XDIS * 2 + D.XDIS/2,ssfnum*D.YDIS*6 + noeventnum * 10);
                trans = transSet(ssfnum*7 + 3,ssfnum*7 + 5,label,nail);
                aftertranslist = addTransition(aftertranslist,trans);
                noeventnum++;
                if(sf.sf.event[i].guard.equals("")){
                    label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + eventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS,"(" + sfname + sf.sf.event[i].eventval + " /= NoEvent)");
                }
                else{
                    label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + eventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS,sf.sf.event[i].guard + " and\n(" + sf.sf.event[i].eventval + " /= NoEvent)");
                }
                label[1] = labelSet(LabelKind.ASSIGNMENT,D.XDIS * 2 + eventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS*3/2,eventupdate);
                nail[0] = nailSet(D.XDIS * 2 + eventnum * 10,ssfnum*D.YDIS*6 + D.YDIS/2);
                trans = transSet(ssfnum*7 + 3,ssfnum*7 + 4,label,nail);
                aftertranslist = addTransition(aftertranslist,trans);
                eventnum++;
            }
        }
        return aftertranslist;

    }
    //Make Switch SF Transition
    Transition[] BranchMake2(Function.Func[] funclist,Function.Func ssf,Transition[] translist,int ssfnum,Location[] loc){
        int ssfid;
        int fid;
        int locid;
        Function.Func f;
        Function.Func ssf2;
        String ssf2name;
        String fname;
        String update = "";
        Label[] label = new Label[2];
        Nail[] nail = new Nail[1];
        int num = 0;
        Transition trans;
        Transition[] aftertranslist = translist;
        fid = FuncGrep(funclist,ssf.ssf.arg2_f);
        f = funclist[fid];
        fname = SupportFunc.firstUpper(f.name);
        for(int i=0;i < f.f.change.length;i++){
            update = "";
            ssfid = FuncGrep(funclist,f.f.change[i].ssfname);
            ssf2 = funclist[ssfid];
            ssf2name = SupportFunc.firstUpper(ssf2.name);
            if(ssf2.arg != null) {
            	for(int j=0;j<ssf2.arg.length;j++){
            		update = update + calcread(f.f.change[i].expr[j],fname,ssf2name + ssf2.arg[j]);
            	}
            }
            if(update.equals("") == false) {
            	update = update.substring(0,update.lastIndexOf(","));
            }
            label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + num * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS*2,numguardread2(f.f.change[i].guard,fname));
            label[1] = labelSet(LabelKind.ASSIGNMENT,D.XDIS * 2 + num * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS*5/2,update);
            nail[0] = nailSet(D.XDIS * 2 + D.XDIS/2,ssfnum*D.YDIS*6 + D.YDIS + num * 10);
            locid = LocGrep(loc,ssf2.name + "_switch");
            trans = transSet(ssfnum*7 + 4,locid,label,nail);
            aftertranslist = addTransition(aftertranslist,trans);
            num++;
        }
        return aftertranslist;
    }

    //Make Send Trigger Model
    Template TemplateRT(int locnum) {
		Template temp;
		Location[] loc = new Location[2];
		Transition[] trans = new Transition[2];
		Label[] label = new Label[1];
		Nail[] nail = new Nail[1];
		loc[0] = locSet(locnum+1,169,-2,LocState.COMMITTED,null,true);
		loc[1] = locSet(locnum+2, 0, 0, LocState.NORMAL,null,false);
	 
		label[0] = labelSet(LabelKind.SYNCHRONISATION, 72, 34, "chk?");
		nail[0]  = nailSet(85, 33);
		trans[0] = transSet(locnum+1, locnum+2, label, nail);
		label[0] = labelSet(LabelKind.SYNCHRONISATION, 70, -58, "trig!");
		nail[0]  = nailSet(85, -36);
		trans[1] = transSet(locnum+2, locnum+1, label, nail);
	 
		temp = tempSet("RT", "", loc, trans);
		return temp;
    }
    //Make Variable Declaration
    String DeclarationMake(Function.Func[] funclist,String[] args){
        String dec = CODE.DEC;
        String[] varlist;
        String ssfname = SupportFunc.firstUpper(args[1]);
        Function.Func ssf;
        Function.Func sf;
        int ssfid;
        int sfid;
        boolean write;
        int outputnum = 0;
        String[] num;
        ssfid = FuncGrep(funclist,args[1]);
        ssf = funclist[ssfid];
        varlist = VarListMake(funclist);
        for(int i=0;i<varlist.length;i++){
            write=false;
            for(int j=2;j<args.length;j++){ 
                num = Calc.numVal(args[j]);
                if(varlist[i].equals("Top_" + ssfname + ssf.arg[j-2])){
                    dec = dec + "int " + varlist[i] + " = " + num[0] + ";\n";
                    write=true;
                }
                else if(varlist[i].equals("Low_" + ssfname + ssf.arg[j-2])){
                    dec = dec + "int " + varlist[i] + " = " + num[1] + ";\n";
                    write=true;
                }
            }
            if(write == false){
                dec = dec + "int " + varlist[i] + ";\n";
            }
        }
        for(int i=0;i<funclist.length;i++){
            if(funclist[i].type == Function.FuncType.SSF){
                ssf = funclist[i];
                ssfname = SupportFunc.firstUpper(ssf.name);
                sfid = FuncGrep(funclist,ssf.ssf.arg1_f);
                sf = funclist[sfid];
                outputnum = sf.sf.output.length;
                for(int j=0;j<outputnum;j++){
                    dec = dec + "int " + "Top_" + ssfname + "Output" + (j+1) + ";\n";
                    dec = dec + "int " + "Low_" + ssfname + "Output" + (j+1) + ";\n";
                }
            }
        }

        dec = dec + "\n";
        for(int i=0;i<outputnum;i++){
            dec = dec + "int " + "Top_Output" + (i+1) + ";\n";
            dec = dec + "int " + "Low_Output" + (i+1) + ";\n";
        }

        return dec +"\n\n\n" + CODE.FCODE;
    }
    //Record Variable
    String[] VarListMake(Function.Func[] funclist){
        String[] varlist = null;
        String ssfname;
        Function.Func ssf;
        Function.Func sf;
        Function.Func f;
        int sfid;
        int fid;
        for(int i=0;i<funclist.length;i++){
            if(funclist[i].type == Function.FuncType.SSF){
                ssf = funclist[i];
                ssfname = SupportFunc.firstUpper(ssf.name);
                sfid = FuncGrep(funclist,ssf.ssf.arg1_f);
                sf = funclist[sfid];
                fid = FuncGrep(funclist,ssf.ssf.arg2_f);
                f = funclist[fid];
                if(ssf.arg != null){
                    for(int j=0;j<ssf.arg.length;j++){
                        varlist = Function.addString(varlist,"Top_" + ssfname + ssf.arg[j]);
                        varlist = Function.addString(varlist,"Low_" + ssfname + ssf.arg[j]);
                    }
                }
                if(f.arg != null){
                    for(int j=0;j<f.arg.length;j++){
                        varlist = Function.addString(varlist,"Top_" + SupportFunc.firstUpper(f.name) + f.arg[j]);
                        varlist = Function.addString(varlist,"Low_" + SupportFunc.firstUpper(f.name) + f.arg[j]);
                    }       
                }
                varlist = VarListSF(funclist,sf,ssfname,varlist);
            }
        }
        return SupportFunc.deplicationRemove(varlist);
    }

    String[] VarListSF(Function.Func[] funclist,Function.Func sf,String rootfuncname,String[] varlist){
        String sfname = SupportFunc.firstUpper(sf.name);
        String address;
        int sfid;
        String[] aftervarlist = varlist;
        if(sf.arg != null){
            for(int i=0;i< sf.arg.length;i++){
                aftervarlist = Function.addString(aftervarlist,"Top_" + rootfuncname + sfname + sf.arg[i]);
                aftervarlist = Function.addString(aftervarlist,"Low_" + rootfuncname + sfname + sf.arg[i]);
            }
        }
        if(sf.sf.input != null){
            for(int i=0;i<sf.sf.input.length;i++){
                aftervarlist = Function.addString(aftervarlist,"Top_" + sfname + sf.sf.input[i]);
                aftervarlist = Function.addString(aftervarlist,"Low_" + sfname + sf.sf.input[i]);
            }
        }
        for(int i=0;i < sf.sf.process.length;i++){
            address = rootfuncname + sfname + (i+1);
            if(sf.sf.process[i].func.equals("integral")){ 
                aftervarlist = Function.addString(aftervarlist,"Top_" + address + "integral");
                aftervarlist = Function.addString(aftervarlist,"Top_" + address + "integral_input");
                aftervarlist = Function.addString(aftervarlist,"Top_" + address + "integral_inputPRE");                
                aftervarlist = Function.addString(aftervarlist,"Top_" + sfname  + sf.sf.process[i].output[0]);
                aftervarlist = Function.addString(aftervarlist,"Low_" + address + "integral");
                aftervarlist = Function.addString(aftervarlist,"Low_" + address + "integral_input");
                aftervarlist = Function.addString(aftervarlist,"Low_" + address + "integral_inputPRE");                  
                aftervarlist = Function.addString(aftervarlist,"Low_" + sfname  + sf.sf.process[i].output[0]);
            }
            else if(sf.sf.process[i].func.equals("edge")){
                aftervarlist = Function.addString(aftervarlist,address + "edge_input");
                aftervarlist = Function.addString(aftervarlist,address + "edge_inputPRE");
                aftervarlist = Function.addString(aftervarlist,sfname  + sf.sf.process[i].output[0]);          
            }
            else if(sf.sf.process[i].func.equals("arr")){                
                aftervarlist = Function.addString(aftervarlist,"Top_" + sfname + sf.sf.process[i].output[0]);                       
                aftervarlist = Function.addString(aftervarlist,"Low_" + sfname + sf.sf.process[i].output[0]);                         
            }
            else if((sfid = FuncGrep(funclist,sf.sf.process[i].func)) >= 0){
                if(sf.sf.process[i].output != null){
                    for(int j=0;j<sf.sf.process[i].output.length;j++){
                        aftervarlist = Function.addString(aftervarlist,"Top_" + sfname + sf.sf.process[i].output[j]);    
                        aftervarlist = Function.addString(aftervarlist,"Low_" + sfname + sf.sf.process[i].output[j]); 
                    }
                }
                aftervarlist = VarListSF(funclist,funclist[sfid],address,aftervarlist);
            }
        }
        return aftervarlist;
    }
    //Looking for Argment Name Function
    int FuncGrep(Function.Func[] funclist,String name){
        int id = -1;
        for(int i=0;i<funclist.length;i++){
            if(name.equals(funclist[i].name)){
               id = i;
               break;
           }
       }
       return id;
    }
}