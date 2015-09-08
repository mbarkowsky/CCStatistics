package util;

import java.util.Comparator;

public class AlphaNumericalComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		int sign = 1;
		if(o2.length() < o1.length()){
			sign = -1;
			String temp = o1;
			o1 = o2;
			o2 = temp;
		}
		for(int i = 0; i < o1.length(); i++){
			int result = compareBlockFrom(o1, o2, i);
			if(result != 0){
				return sign * result;
			}
		}
		return 0;
	}

	private int compareBlockFrom(String s1, String s2, int i) {
		if(Character.isDigit(s1.charAt(i)) && Character.isDigit(s2.charAt(i))) {
			return compareNumberFrom(s1, s2, i);
		}
		return Character.compare(s1.charAt(i), s2.charAt(i));
	}

	private int compareNumberFrom(String s1, String s2, int i) {
		int i1 = i;
		int i2 = i;

		String n1 = "";
		char c1;
		while(i1 < s1.length() && Character.isDigit((c1 = s1.charAt(i1)))){
			n1 = n1 + c1;
			i1++;
		}
		long number1 = Long.parseLong(n1);
		
		String n2 = "";
		char c2;
		while(i2 < s2.length() && Character.isDigit((c2 = s2.charAt(i2)))){
			n2 = n2 + c2;
			i2++;
		}
		long number2 = Long.parseLong(n2);
		if(!s1.equals("game" + number1)){
			System.out.println("error");
		}
		if(!s2.equals("game" + number2)){
			System.out.println("error");
		}
		return Long.compare(number1, number2);
	}

}
