public class SupportFunc {
	//n文字の空白文字列を作る
 public static String spaceCreate(int n) {
	 String str = "";
	 for(int i=0;i < n;i++) {
		 str = str + " ";
	 }
	 return str;
 }
 //文字列先頭の空白を数える
 public static int spaceCount(String str) {
	 int n=0;
	 for(int i=0;i < str.length();i++) {
		 if(str.charAt(i) == '\n') {
			 n = 999;
			 break;
		 }
		 else if(str.charAt(i) != ' ') {
			 break;
		 }
		 n++;
	 }
	 return n;
 }
 //変数に使用できる文字かどうかを判定
 public static boolean varChar(char c) {
	 if('a' <= c && c <= 'z') {
		 return true;
	 }
	 else if('A' <= c && c <= 'Z') {
		 return true;
	 }
	 else if('0' <= c && c <= '9') {
		 return true;
	 }
	 else if(c == '\'' || c == '_') {
		 return true;
	 }
	 else return false;
 }
 //対象となるかっこの右側を探す
 public static int parenthesisEnd(String str,int place) {
	 int p=0;
	 for(int i=0;i + place < str.length();i++) {
		 if(str.charAt(i+place) == '(') {
			 p++;
		 }
		 if(str.charAt(i+place) == ')') {
			 p--;
		 }
		 if(p < 0) {
			 return i+place;
		 }
	 }
	 return -1;
 }
 
 
 //文字列から変数を抽出する
 public static String[] varBookCreate(String str) {
	 char[] cstr = (str + " ").toCharArray();
	 String[] wordbook;
	 char[] buf = new char[80] ;
	 char[] word;
	 boolean vcp=false;
	 int wordnum=0;
	 int wordlen = 0;
	 for(int i=0;i<cstr.length;i++) {
		 if(varChar(cstr[i])) {
			 if(vcp == false) {
				 wordnum++;
			 }
		 }
		 vcp = varChar(cstr[i]);
	 }
	 wordbook = new String[wordnum];
	 vcp = false;
	 wordnum=0;
	 for(int i=0;i<cstr.length;i++) {
		 if(varChar(cstr[i])) {
			 if(vcp == false) {
				 wordlen=0;
			 }
			 buf[wordlen] = cstr[i];
			 wordlen++;
		 }
		 else {
			 if(vcp == true) {
				 word = new char[wordlen];
				 for(int j=0;j<wordlen;j++) {
					 word[j] = buf[j];
				 }
				 wordbook[wordnum] = String.valueOf(word);
				 wordnum++;
			 }
		 }
		 vcp = varChar(cstr[i]);
	 }
	 return wordbook;
 }
 //配列に数字nが入っているかを判定する
 public static boolean intcontains(int[] list,int size,int n) {
	 for(int i=0;i<size;i++) {
		 if(list[i] == n) {
			 return true;
		 }
	 }
	 return false;
 }
 //文字列を関数と引数に分割
 public static String[] argDivide(String str,int argnum) {
	 String[] sentence = new String[argnum+1];
	 int sennum=0;
	 int place;
	 int j;
	 for(int i=0;i<str.length();i++) {
		 if(str.charAt(i) == '(') {
			 place = parenthesisEnd(str,i+1);
			 sentence[sennum] = str.substring(i+1, place);
			 i = place;
			 sennum++;
		 }
		 else if(varChar(str.charAt(i))) {
			 for(j=i;j<str.length();j++) {
				 if(varChar(str.charAt(j)) == false) {
					 break;
				 }
			 }
			 sentence[sennum] = str.substring(i,j);
			 i = j;
			 sennum++;
		 }
	 }
	 return sentence;
 }
 //文字列の初めを大文字にする
 public static String firstUpper(String str){
	String capitalizedstr = new String();
	if (str.length() > 1){
	    capitalizedstr = Character.toTitleCase(str.charAt(0)) + str.substring(1);
	}
	else {
	    capitalizedstr = str.toUpperCase();
	}
	return capitalizedstr;
 }
 //文字列を置き換える
 public static String replace(String str,String bword,String aword) {
	 String afterstr = str;
	 int place = 0;
	 int place2 = 0;
	 boolean flag;
	 while(place < str.length()) {
		 place = str.indexOf(bword,place);
		 if(place != -1) {
			 place2 = afterstr.indexOf(bword,place2);
			 flag = false;
			 if(place == 0) {
				 if(varChar(str.charAt(bword.length())) == false) {
					 afterstr = aword + str.substring(bword.length());
					 flag = true;
				 }
			 }
			 else if(place + bword.length() >= str.length()) {
				 if(varChar(str.charAt(place-1)) == false) {
					 afterstr = afterstr.substring(0,place2) + aword;
					 flag = true;
				 }
			 }
			 else {
				 if(varChar(str.charAt(place-1)) == false && varChar(str.charAt(place + bword.length())) == false) {
					afterstr = afterstr.substring(0,place2) + aword + afterstr.substring(place2 + bword.length()); 
					flag = true;
				 }
			 }
			 if(flag) {
				 place = place + bword.length();
				 place2 = place2 + aword.length();
			 }
			 else {
				 place++;
				 place2++;
			 }
		 }
		 else {
			 break;
		 }
	 }
	 return afterstr;
 }
 //重複した文字列を削除する
 public static String[] deplicationRemove(String[] list) {
	 String[] afterlist;
	 boolean[] flag = new boolean[list.length];
	 int wordnum=0;
	 for(int i=list.length-1;i>=0;i--) {
		 flag[i] = true;
		 if(('0' <= list[i].charAt(0) && list[i].charAt(0) <= '9') || list[i].charAt(0) == '-') {
			 flag[i] = false;
		 }
		 else {
			 for(int j=i-1;j>=0;j--) {
				 if(list[i].equals(list[j])) {
					 flag[i] = false;
					 break;
				 }
			 }
		 }
	 }
	 for(int i=0;i<list.length;i++) {
		 if(flag[i]) {
			 wordnum++;
		 }
	 }
	 afterlist = new String[wordnum];
	 wordnum=0;
	 for(int i=0;i<list.length;i++) {
		 if(flag[i]) {
			 afterlist[wordnum] = list[i];
			 wordnum++;
		 }
	 }
	 return afterlist;
 }
 //タプルの要素ごとに文字列を分解する
 static String[] TupleDivide(String s){
	String[] tuple;
	int pts = 0;
	int pte = 0;
	int[] comma = new int[10];
	int commanum=0;
	int p;
	for(int i=0;i < s.length();i++){
		if(s.charAt(i) == '(') {
			pts = i;
			p=0;
			for(int j=i+1;j<s.length();j++){
				if(s.charAt(j) == '(') {
					p++;
				}
				if(s.charAt(j) == ')') {
					p--;
				}
				if(p==0 && s.charAt(j) == ','){
					comma[commanum] = j;
					commanum++;
				}
				if(p < 0){
					pte = j;
					break;
				}
			}
			break;
		}
	}
	tuple = new String[commanum+1];
	if(commanum == 0){
		tuple[0] = s;
	}
	else{
		for(int i=0;i<commanum;i++){
			if(i==0){
				tuple[i] = s.substring(pts+1, comma[0]);
			}
			else{
				tuple[i] = s.substring(comma[i-1]+1, comma[i]);
			}
		}
		tuple[commanum] = s.substring(comma[commanum-1]+1,pte);
	}
	return tuple;
 }
//文字列から対象となる文字列を探す
 static int Grep(String word,String str,int p){
	 int place;
	 for(int i=p;i<str.length();i++){
		place = str.indexOf(word,i);
		if(place < 0){
			break;
		}
		else if(place == 0){
			if(varChar(str.charAt(word.length())) == false){
				return place;
			}
		 }
		else{
		 	if(varChar(str.charAt(place-1)) == false && varChar(str.charAt(place + word.length())) == false){
				return place;
 		 	}
			else{
				i = place + 1;
			}
		} 
	}
	return -1;
 }
}
