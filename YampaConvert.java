public class YampaConvert {
	//Main Function
    public static void main(String[] args) {
		String str;
		String filename;
		FuncData[] data;
		UppaalXMLMake uppaal = new UppaalXMLMake();
		str = YampaConvertClass.fileRead(args[0]);
		str = YampaConvertClass.commentRemove(str);
		str = YampaConvertClass.moduleLine0(str);
		str = YampaConvertClass.reservedWordShifted(str);
		str = YampaConvertClass.layoutChange(str);
		data = FuncData.dataGet(str, 0);
		data = FuncData.dataSelect(data, args[1]);
		str = FuncData.dataSFprint(data);
		str = uppaal.UppaalGet(str,args);
		filename = args[0].substring(0,args[0].lastIndexOf(".")) + ".xml";
		YampaConvertClass.fileWrite(filename,str);
		
	}
}