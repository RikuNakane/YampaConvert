import java.io.StringReader;
import java.util.regex.Pattern;

public class UppaalXMLMake extends UppaalXML{
    class D{
		final static int XDIS = 200,YDIS = 100;
    }
    //Uppaalのxmlファイルを作成
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
    //関数情報を取得
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
    //離散時間モデルの作成
    Template TemplateMake(Function.Func[] funclist){
        Template temp;
        Transition[] trans;
        Location[] loc;
        loc = LocationMake(funclist);
        trans = TransitionMake(funclist,loc);
        temp = tempSet("SwitchSignalFunction","",loc,trans);
        return temp;
    }
    //離散時間モデルの状態(Location)を作成
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
    //離散時間モデルの遷移(Transition)を作成
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
                    outputupdate = outputupdate + "Output" + (j+1) + " := " + ssfname + "Output" + (j+1) + ",\n";
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
    //シグナル関数の初期化に関する更新式を作成
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
            System.out.println("###" + ssf.ssf.arg1_f + "が見つかりません\n");
            System.exit(0);
        }
        sf = funclist[sfid];
        ssfname = SupportFunc.firstUpper(ssf.name);
        sfname = SupportFunc.firstUpper(sf.name);
        if(sf.arg != null){
            for(int i=0;i<sf.arg.length;i++){
                update = update + ssfname + sfname + sf.arg[i] + " := " + ssfname + ssf.ssf.arg1_arg[i] + ",\n";         
            }
        }
        if(sf.sf.input != null){
            for(int i=0;i<sf.sf.input.length;i++){
                update = update + sf.sf.input[i] + " := 0,\n";
            }
        }

        update = update + InitUpdateSF(funclist,sf,ssfname);

        update = update.substring(0,update.lastIndexOf(","));
        label[0] = labelSet(LabelKind.ASSIGNMENT,0,ssfnum * 6 * D.YDIS,update);
		trans = transSet(ssfnum * 7 + 1,ssfnum * 7 + 2,label,null);
        return trans;
    }
    //シグナル関数の初期化に関する更新式を作成
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
                update = update + address + "integral := 0,\n"; 
                update = update + address + "integral_input := 0,\n"; 
                update = update + address + "integral_inputPRE := 0,\n"; 
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
                        update = update + address + sf2name + sf2.arg[j] + " := " + rootfuncname + sfname + sf.sf.process[i].funcarg[j] + ",\n";         
                    }
                }
                update = update + InitUpdateSF(funclist,sf2,address);
            }
        }
        return update;
    }
    //連続値の更新，イベント判定に関する更新式を作成
    Transition UpdateMake(Function.Func[] funclist,Function.Func ssf,int ssfnum){
        Transition trans;
        Label[] label = new Label[1];
        String update;
        int sfid;
        Function.Func sf;
        String ssfname;
        sfid = FuncGrep(funclist,ssf.ssf.arg1_f);
        if(sfid < 0){
            System.out.println("###" + ssf.ssf.arg1_f + "が見つかりません\n");
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
        for(int i=0;i < sf.sf.process.length;i++){
            address = rootfuncname + sfname + (i+1);
            if(sf.sf.process[i].func.equals("integral")){ 
                update = update + address + "integral_input := " + sf.sf.process[i].input[0] + ",\n";
                update = update + address + "integral := " +  address + "integral + " + address + "integral_inputPRE * dt,\n";
                update = update + sf.sf.process[i].output[0] + " := " + address + "integral,\n";
                update = update + address + "integral_inputPRE := " + address + "integral_input,\n"; 
            }
            else if(sf.sf.process[i].func.equals("edge")){
                update = update + address + "edge_input := " + sf.sf.process[i].input[0] + ",\n"; 
                update = update + sf.sf.process[i].output[0] + " := " + address + "edge_input and (not " + address + "edge_inputPRE" + "),\n";  
                update = update + address + "edge_inputPRE := " + address + "edge_input,\n"; 
            }
            else if(sf.sf.process[i].func.equals("arr")){
                op = sf.sf.process[i].funcarg[0];
                token = sf.sf.process[i].funcarg[1];
                if(Pattern.compile("^-?[0-9]+$").matcher(token).find()){
                    update = update + sf.sf.process[i].output[0] + " := " + token + " " + op + " " + sf.sf.process[i].input[0] + ",\n";
                }
                else{
                    update = update + sf.sf.process[i].output[0] + " := " + rootfuncname + sfname + token + " " + op + " " + sf.sf.process[i].input[0] + ",\n";
                }
            }
            else if((sfid = FuncGrep(funclist,sf.sf.process[i].func)) >= 0){
                sf2 = funclist[sfid];
                if(sf2.sf.input != null){
                    for(int j=0;j<sf2.sf.input.length;j++){
                        update = update + sf2.sf.input[j] + " := " + sf.sf.process[i].input[j] + ",\n";
                    }
                }
                update = update + UpdateSF(funclist,sf2,address);
                for(int j=0;j<sf2.sf.output.length;j++){
                        update = update + sf.sf.process[i].output[j] + " := " + sf2.sf.output[j] + ",\n";         
                }
            }
        }
        return update;
    }
    //イベント発生の有無に関する遷移の作成
    Transition[] BranchMake1(Function.Func[] funclist,Function.Func ssf,Transition[] translist,int ssfnum){
        String ssfname = SupportFunc.firstUpper(ssf.name);
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
        for(int i=0;i<sf.sf.output.length;i++){
            outputupdate = outputupdate + ssfname + "Output" + (i+1) + " := " + sf.sf.output[i] + ",\n";
        }
        outputupdate = outputupdate.substring(0,outputupdate.lastIndexOf(","));
        for(int i=0;i < sf.sf.event.length;i++){
            eventupdate = "";
            if(f.arg != null) {
            	for(int j=0;j<f.arg.length;j++){
            		eventupdate = eventupdate + f.arg[j] + " := " + sf.sf.event[i].expr[j] + ",\n";
            	}
            }
            eventupdate = eventupdate.substring(0,eventupdate.lastIndexOf(","));
            if(sf.sf.event[i].eventval.equals("NoEvent")){
                label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + noeventnum * D.XDIS,ssfnum * D.YDIS * 6,sf.sf.event[i].guard);
                label[1] = labelSet(LabelKind.ASSIGNMENT,D.XDIS * 2 + noeventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS/2,outputupdate);
                nail[0] = nailSet(D.XDIS * 2 + D.XDIS/2,ssfnum*D.YDIS*6 + noeventnum * 10);
                trans = transSet(ssfnum*7 + 3,ssfnum*7 + 5,label,nail);
                aftertranslist = addTransition(aftertranslist,trans);
                noeventnum++;
            }

            else if(sf.sf.event[i].eventval.equals("Event()")){
                label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + eventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS,sf.sf.event[i].guard);
                label[1] = labelSet(LabelKind.ASSIGNMENT,D.XDIS * 2 + eventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS*3/2,eventupdate);
                nail[0] = nailSet(D.XDIS * 2 + eventnum * 10,ssfnum*D.YDIS*6 + D.YDIS/2);
                trans = transSet(ssfnum*7 + 3,ssfnum*7 + 4,label,nail);
                aftertranslist = addTransition(aftertranslist,trans);
                eventnum++;
            }
            else{
                if(sf.sf.event[i].guard.equals("")){
                    label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + noeventnum * D.XDIS,ssfnum * D.YDIS * 6, "not(" + sf.sf.event[i].eventval + " /= NoEvent)");
                }
                else{
                    label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + noeventnum * D.XDIS,ssfnum * D.YDIS * 6,sf.sf.event[i].guard + " and\nnot(" + sf.sf.event[i].eventval + " /= NoEvent)");
                }
                label[1] = labelSet(LabelKind.ASSIGNMENT,D.XDIS * 2 + noeventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS/2,outputupdate);
                nail[0] = nailSet(D.XDIS * 2 + D.XDIS/2,ssfnum*D.YDIS*6 + noeventnum * 10);
                trans = transSet(ssfnum*7 + 3,ssfnum*7 + 5,label,nail);
                aftertranslist = addTransition(aftertranslist,trans);
                noeventnum++;
                if(sf.sf.event[i].guard.equals("")){
                    label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + eventnum * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS,"(" + sf.sf.event[i].eventval + " /= NoEvent)");
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
    //シグナル関数の切り替えに関する遷移の作成
    Transition[] BranchMake2(Function.Func[] funclist,Function.Func ssf,Transition[] translist,int ssfnum,Location[] loc){
        int ssfid;
        int fid;
        int locid;
        Function.Func f;
        Function.Func ssf2;
        String ssf2name;
        String update = "";
        Label[] label = new Label[2];
        Nail[] nail = new Nail[1];
        int num = 0;
        Transition trans;
        Transition[] aftertranslist = translist;
        fid = FuncGrep(funclist,ssf.ssf.arg2_f);
        f = funclist[fid];
        for(int i=0;i < f.f.change.length;i++){
            update = "";
            ssfid = FuncGrep(funclist,f.f.change[i].ssfname);
            ssf2 = funclist[ssfid];
            ssf2name = SupportFunc.firstUpper(ssf2.name);
            if(f.arg != null) {
            	for(int j=0;j<f.arg.length;j++){
            		update = update + ssf2name + ssf2.arg[j] + " := " + f.f.change[i].expr[j] + ",\n";
            	}
            }
            if(update.equals("") == false) {
            	update = update.substring(0,update.lastIndexOf(","));
            }
            label[0] = labelSet(LabelKind.GUARD,D.XDIS * 2 + num * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS*2,f.f.change[i].guard);
            label[1] = labelSet(LabelKind.ASSIGNMENT,D.XDIS * 2 + num * D.XDIS,ssfnum * D.YDIS * 6 + D.YDIS*5/2,update);
            nail[0] = nailSet(D.XDIS * 2 + D.XDIS/2,ssfnum*D.YDIS*6 + D.YDIS + num * 10);
            locid = LocGrep(loc,ssf2.name + "_switch");
            trans = transSet(ssfnum*7 + 4,locid,label,nail);
            aftertranslist = addTransition(aftertranslist,trans);
            num++;
        }
        return aftertranslist;
    }

    //トリガーを送るモデルの作成
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
    //変数の宣言文の作成
    String DeclarationMake(Function.Func[] funclist,String[] args){
        String dec = "chan trig,chk;\nint dt = 1;\nconst int Event = 1;\nconst int NoEvent = 0;\n\n";
        String[] varlist;
        String ssfname = SupportFunc.firstUpper(args[1]);
        Function.Func ssf;
        Function.Func sf;
        int ssfid;
        int sfid;
        boolean write;
        int outputnum = 0;
        ssfid = FuncGrep(funclist,args[1]);
        ssf = funclist[ssfid];
        varlist = VarListMake(funclist);
        for(int i=0;i<varlist.length;i++){
            write=false;
            for(int j=2;j<args.length;j++){
                if(varlist[i].equals(ssfname + ssf.arg[j-2])){
                    dec = dec + "int " + varlist[i] + " = " + args[j] + ";\n";
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
                    dec = dec + "int " + ssfname + "Output" + (j+1) + ";\n";
                }
            }
        }

        dec = dec + "\n";
        for(int i=0;i<outputnum;i++){
            dec = dec + "int " + "Output" + (i+1) + ";\n";
        }

        return dec;
    }
    //使用される変数を記録
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
                        varlist = Function.addString(varlist,ssfname + ssf.arg[j]);
                    }
                }
                if(f.arg != null){
                    for(int j=0;j<f.arg.length;j++){
                        varlist = Function.addString(varlist,f.arg[j]);
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
                aftervarlist = Function.addString(aftervarlist,rootfuncname + sfname + sf.arg[i]);
            }
        }
        if(sf.sf.input != null){
            for(int i=0;i<sf.sf.input.length;i++){
                aftervarlist = Function.addString(aftervarlist,sf.sf.input[i]);
            }
        }
        for(int i=0;i < sf.sf.process.length;i++){
            address = rootfuncname + sfname + (i+1);
            if(sf.sf.process[i].func.equals("integral")){ 
                aftervarlist = Function.addString(aftervarlist,address + "integral");
                aftervarlist = Function.addString(aftervarlist,address + "integral_input");
                aftervarlist = Function.addString(aftervarlist,address + "integral_inputPRE");
                aftervarlist = Function.addString(aftervarlist,sf.sf.process[i].input[0]);                    
                aftervarlist = Function.addString(aftervarlist,sf.sf.process[i].output[0]);
            }
            else if(sf.sf.process[i].func.equals("edge")){
                aftervarlist = Function.addString(aftervarlist,address + "edge_input");
                aftervarlist = Function.addString(aftervarlist,address + "edge_inputPRE");
                aftervarlist = Function.addString(aftervarlist,sf.sf.process[i].output[0]);          
            }
            else if(sf.sf.process[i].func.equals("arr")){
                aftervarlist = Function.addString(aftervarlist,sf.sf.process[i].input[0]);                    
                aftervarlist = Function.addString(aftervarlist,sf.sf.process[i].output[0]);                  
            }
            else if((sfid = FuncGrep(funclist,sf.sf.process[i].func)) >= 0){
                if(sf.sf.process[i].input != null){
                    for(int j=0;j<sf.sf.process[i].input.length;j++){
                        aftervarlist = Function.addString(aftervarlist,sf.sf.process[i].input[j]);    
                    }
                }
                if(sf.sf.process[i].output != null){
                    for(int j=0;j<sf.sf.process[i].output.length;j++){
                        aftervarlist = Function.addString(aftervarlist,sf.sf.process[i].output[j]);    
                    }
                }
                aftervarlist = VarListSF(funclist,funclist[sfid],address,aftervarlist);
            }
        }
        return aftervarlist;
    }
    //変数nameと一致する関数を探す
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