import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
//プログラムのコメントと不要な改行文字の削除
public class YampaConvertClass {
	//ファイル情報取得
	static String fileRead(String input) {
		try{
			BufferedReader br = new BufferedReader(new FileReader(input));
			String str = "";
			String rstr;
			while(true) {
				rstr = br.readLine();
				if(rstr == null) break;
				str = str + rstr + "\n";
			}
			br.close();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	//ファイルに出力
	static void fileWrite(String output,String str) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			bw.write(str);
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	//コメントを削除
	static String commentRemove(String str) {
		String afterstr = "";
		int flag=0;
		for(int i=0;i<str.length();i++) {
			switch(flag){
			case 0:
				if(i+2 < str.length()) {
					if(str.charAt(i) == '-' && str.charAt(i+1) == '-' && str.charAt(i+2) == ' ') {
						flag = 1;
					}
				}
				if(i+1< str.length()) {
					if(str.charAt(i) == '{' && str.charAt(i+1) == '-') {
						flag = 2;
					}
				}
				break;
			case 1:
				if(str.charAt(i) == '\n') {
					flag = 0;
				}
				break;
			case 2:
				if(i > 1) {
					if(str.charAt(i-2) == '-' && str.charAt(i-1) == '}') {
						flag = 0;
					}
				}
				break;
			}
			if(flag == 0) {
				afterstr = afterstr + str.charAt(i);
			}
			else if(str.charAt(i) == '\n') {
				afterstr = afterstr + "\n";
			}
		}
		return afterstr;
	}
	//プログラムの左端に文字が来るようにする
	static String moduleLine0(String str) {
		String afterstr = "";
		String[] line;
		int moduleline=0;
		int flag=0;
		line = str.split("\n",0);
		for(int i=0;flag == 0;i++) {
			for(int j=0;j<line.length;j++) {
				if(i < line[j].length() ) {
					if(line[j].charAt(i) != ' ') {
						flag = 1;
						moduleline = i;
						break;
					}
				}
			}
		}
		for(int i=0;i < line.length;i++) {
			if(moduleline < line[i].length() ) {
				afterstr = afterstr + line[i].substring(moduleline) + "\n";
			}
		}
		return afterstr;
	}
	//予約語の後ろに改行文字を入れる
	static String reservedWordShifted(String str) {
		String afterstr;
		afterstr = wordShifted(str,"do");
		afterstr = wordShifted(afterstr,"of");
		afterstr = wordShifted(afterstr,"where");
		afterstr = wordShifted(afterstr,"let");
		return afterstr;
	}
	static String wordShifted(String str,String target){
		String[] line;
		String[] afterline;
		String[] s;
		String afterstr = "";
		line = str.split("\n");
		afterline = new String[line.length];
		for(int i=0; i < line.length;i++) {
			s = line[i].split(" "+target+" ");
			afterline[i] = s[0];
			for(int j = 1;j < s.length;j++) {
				afterline[i] = afterline[i] + " " + target + "\n";
				s[j] = SupportFunc.spaceCreate(s[j-1].length() + target.length() + 2) + s[j];
				afterline[i] = afterline[i] + s[j];
			}		
		}
		for(int i = 0;i < line.length;i++) {
			afterstr = afterstr + afterline[i] + "\n";
		}
		return afterstr;
	}
	//不要な改行文字を削除する
	static String layoutChange(String str) {
		Stack<Integer> offsideline = new Stack<Integer>();
		String afterstr="";
		String[] line;
		int n;
		boolean flag = false;
		line = str.split("\n");
		for(int i=0;i<line.length;i++) {
			line[i] = line[i] + "\n";
		}
		offsideline.push(0);
		for(int i=0;i<line.length;i++) {
			n = SupportFunc.spaceCount(line[i]);
			if(n != 999) {
				if(flag == true) {
					offsideline.push(n);
				}
				if(line[i].contains(" do\n") || line[i].contains(" where\n") || line[i].contains(" of\n") || line[i].contains(" let\n")) {
					flag = true;
				}
				else {
					flag = false;
				}
				if(n > offsideline.peek()) {
					line[i-1] = line[i-1].substring(0, line[i-1].length() - 1);
					line[i] = line[i].substring(n-1);
				}
				while(n < offsideline.peek()) {
					offsideline.pop();
				}
			}
		}
		for(int i = 0;i < line.length;i++) {
			if(SupportFunc.spaceCount(line[i]) != 999){
				afterstr = afterstr + line[i];
			}
		}
		return afterstr;
	}
}
