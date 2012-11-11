/**
 * 
 */
package jp.ne.kazuaki.util;

import java.util.ArrayList;
import java.util.List;

/**
 * CSVを解析するクラス
 */
public class CSVParser {
	private final Character COMMA = ',';
	private final Character DOUBLE_QUOTE = '"';
	
	public CSVParser() {
		super();
	}

	/**
	 * CSVの文字列を解析し、リストで返します
	 * 解析可能な文字列は以下のとおり
	 *   １．カンマ区切り
	 *   ２．文字列に改行は含まない
	 *   ３．カンマを区切りでなく文字列として扱う場合、要素を「"」ダブルクォーテーションで囲む。（ダブルクォーテーションを除いたものを返します)
	 *   ４．ダブルクォーテーションで囲まれた要素内にダブルクォーテーションを含める場合、""でエスケープする。
	 * @param str 解析する文字列
	 * @return 解析後のリスト
	 * @throws CSVParseException 解析不能な場合Exception(ダブルクォーテーションが閉じられていない場合)
	 */
	public List<String> parse(String str) throws CSVParseException {
		if (str == null || str.isEmpty()) { return null;}
		
		return parseList(str);
	}
	
	/**
	 * 文字列を解析し、List形式にする。
	 * @param str 解析する文字列
	 * @return 解析後のList
	 */
	private List<String> parseList(String str) throws CSVParseException {
		List<String> result = new ArrayList<String>();
		Boolean isClose = false;
		Boolean isDoubleQuoteMode = false;	// ダブルクォーテーションモード
		int numOfDoubleQuote = 0;			// ダブルクォーテーションの個数
		Character c = null;
		StringBuffer tmp = new StringBuffer();
		
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			
			if (c == DOUBLE_QUOTE) {
				numOfDoubleQuote++;
				// 文字を連結
				tmp.append(String.valueOf(c));
				
				// close状態でダブルクォーテーション→ダブルクォーテーションモード
				if (isClose) {
					isDoubleQuoteMode = true;
					
					// open
					isClose = false;
				}	
			} else if (c == COMMA) {
				if (isDoubleQuoteMode) {
					// ダブルクォートの数が偶数→close
					if (numOfDoubleQuote % 2 == 0) {
						isClose = true;
					}
				} else {
					isClose = true;
				}
				
				if (isClose) {
					// 要素追加して新しい要素へ
					result.add(convert(tmp.toString()));
					
					// 初期化
					numOfDoubleQuote = 0;
					isDoubleQuoteMode = false;
					tmp = new StringBuffer();
				} else {
					// 文字を連結
					tmp.append(String.valueOf(c));
				}
			} else {
				// open
				isClose = false;
				
				// 文字を連結
				tmp.append(String.valueOf(c));
			}		
		}
		
		if (str.length() != 0) {
			// ダブルクォートモードで最後がダブルクォートで閉じられていない場合エラー
			if (isDoubleQuoteMode && numOfDoubleQuote % 2 != 0) {
				throw new CSVParseException("CSVの解析中にエラーが発生しました。ダブルクォートで閉じられていません。");
			}
			// 最後の要素追加
			result.add(convert(tmp.toString()));
		}
		
		return result;
	}
	
	/**
	 * 文字列の変換処理を行う
	 * ・前後の空白を除去
	 * ・前後のダブルクォーテーションを除去
	 * ・前後がダブルクォーテーションで囲まれている場合は、要素内の２重のダブルクォーテーションを1つにする。
	 * 例) "aaa""aaa"→aaa"aaa
	 *    ただし、前後がダブルクォーテーションで囲まれていない場合は、処理しない。
	 *    例) aaa""aaa→aaa""aaa
	 * @param str 変換する文字列
	 * @return 変換後の文字列
	 */
	private String convert(String str) {
		String r = str.trim();
		final String doubleQuote = String.valueOf(DOUBLE_QUOTE);
		if (str.startsWith(doubleQuote) && str.endsWith(doubleQuote)) {
			r = str.substring(1, str.length() -1);
			
			// ２重のダブルクォーテーションを1つに置換
			r = r.replaceAll(doubleQuote.concat(doubleQuote), doubleQuote);
		}
		
		return r;
	}
}
