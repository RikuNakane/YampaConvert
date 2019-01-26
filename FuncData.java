//プログラムを関数ごとに分割し，switch関数が使用する関数を抽出する
public class FuncData {
	String name;
	String type;
	String context;
	//プログラムを関数ごとに分割する
	public static FuncData[] dataGet(String str,int lnum){
		FuncData[] data = new FuncData[datanum(str)];
		String[] line;
		String[] word;
		String valp = "";
		int j=0;
		for(int i=0;i<data.length;i++) {
			data[i] = new FuncData();
		}
		
		line = str.split("\n");
		for(int i=0;i<line.length;i++) {
			if(line[i].startsWith("import ")) {
				data[j].name = line[i].substring(7);
				data[j].type = "import";
				data[j].context = line[i];
				j++;
			}
			else if(line[i].startsWith("type ")) {
				word = line[i].split("=");
				data[j].name = word[0].substring(5);
				data[j].type = "type";
				data[j].context = line[i];
				j++;
			}
			else if(line[i].startsWith("data ")) {
				word = line[i].split("=");
				data[j].name = word[0].substring(5);
				data[j].type = "data";
				data[j].context = line[i];
				if(line[i].contains("{")) {
						while(line[i].contains("}")) {
							i++;
							data[j].context = data[j].context + "\n" + line[i];
						}
				}
				j++;
			}
			else {
				word = line[i].split(" ");
				if(!(word[0].equals("") || word[0].equals(valp))) {
					data[j].name = word[0];
					data[j].type = "val";
					data[j].context = line[i];
					j++;
				}
				else {
					data[j-1].context = data[j-1].context + "\n"  + line[i];
				}
				valp = word[0];
			}
		}
		return data;
	}
	//プログラム内の関数の数を数える
	public static int datanum(String str) {
		int n=0;
		String valp = "";
		String[] line;
		String[] word;
		line = str.split("\n");
		for(int i=0;i<line.length;i++) {
			if(line[i].startsWith("import ")) {
				n++;
			}
			else if(line[i].startsWith("type ")) {
				n++;
			}
			else if(line[i].startsWith("data ")) {
				if(line[i].contains("{")) {
					while(line[i].contains("}")) {
						i++;
					}
				}
				n++;
			}
			else {
				word = line[i].split(" ");
				if(!(word[0].equals("") || word[0].equals(valp))) {
					n++;
				}
				valp = word[0];
			}
		}
		return n;
	}

	//引数nameと一致する名前の関数を探す
	public static int dataGrep(FuncData[] data,String name) {
		for(int i = 0;i<data.length;i++) {
			if(name.equals(data[i].name)) {
				return i;
			}
		}
		return -1;
	}
	//使用する関数を抽出する
	public static FuncData[] dataSelect(FuncData[] data,String sf) {
		int[] datanumber = new int[data.length];
		int datanum;
		String[] wordbook;
		FuncData[] afterdata;
		if(dataGrep(data,sf) == -1) {
			System.out.println(sf + " can not be found\n");
			System.exit(1);
		}
		datanum = 1;
		datanumber[0] = dataGrep(data,sf);
		for(int i = 0;i < datanum;i++) {
			wordbook = SupportFunc.varBookCreate(data[datanumber[i]].context);
			for(int j=0;j < wordbook.length;j++) {
				if(dataGrep(data,wordbook[j]) != -1) {
					if(SupportFunc.intcontains(datanumber, datanum, dataGrep(data,wordbook[j])) == false) {
						datanumber[datanum] = dataGrep(data,wordbook[j]);
						datanum++;
					}
				}
			}
		}
		afterdata = new FuncData[datanum];
		for(int i=0;i<datanum;i++) {
			afterdata[i] = data[datanumber[i]];
		}
		return afterdata;
	}
	
	//Functionの文脈自由文法にあうプログラム文字列を作成
	public static String dataSFprint(FuncData[] data) {
		String str = "";
		String[] line;
		for(int i=0;i<data.length;i++){
			line = data[i].context.split("\n");
			for(int j=0;j<line.length;j++){
				if(!(line[j].contains("::"))){
					str = str + line[j] + "\n";
				}
			}
			str = str + "\n";
		}
		return str;
	}
}
