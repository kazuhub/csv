/**
 * 
 */
package jp.ne.kazuaki.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jp.ne.kazuaki.util.CSVParseException;
import jp.ne.kazuaki.util.CSVParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kazu
 *
 */
public class CSVParserTest {

	private String method_name = "";
	
	private CSVParser parser = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		print("start testcase ==============");
		
		if (parser == null) {
			parser = new CSVParser();
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		print("=============== end testcase : " + method_name);
	}

	/**
	 * {@link jp.ne.kazuaki.util.CSVParser#parse(java.lang.String)} のためのテスト・メソッド。
	 */
	@Test
	public void testParse1() {
		method_name = "testParse1";
		
		String arg = "aaa,bbb,ccc";
		List<String> expect = new ArrayList<String>();
		expect.add("aaa");
		expect.add("bbb");
		expect.add("ccc");
		
		try {
			List<String> l = parser.parse(arg);
			
			assertEquals(expect, l);
		} catch (CSVParseException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

	@Test
	public void testParse2() {
		method_name = "testParse2";
		
		String arg = "aaa,bbb,\"ccc\"";
		List<String> expect = new ArrayList<String>();
		expect.add("aaa");
		expect.add("bbb");
		expect.add("ccc");
		
		try {
			List<String> l = parser.parse(arg);
			
			assertEquals(expect, l);
		} catch (CSVParseException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

	@Test
	public void testParse3() {
		method_name = "testParse3";
		
		String arg = "aaa,bbb,\"ccc\"\"\"";
		List<String> expect = new ArrayList<String>();
		expect.add("aaa");
		expect.add("bbb");
		expect.add("ccc\"");
		
		try {
			List<String> l = parser.parse(arg);
			
			assertEquals(expect, l);
		} catch (CSVParseException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

	@Test
	public void testParse4() {
		method_name = "testParse4";
		
		String arg = "aaa,bbb,\"ccc\"\"";
		List<String> expect = new ArrayList<String>();
		expect.add("aaa");
		expect.add("bbb");
		expect.add("ccc");
		
		try {
			List<String> l = parser.parse(arg);
			
			assertFalse(true);
		} catch (CSVParseException e) {
			e.printStackTrace();
			assertTrue(true);
		}
	}
	
	@Test
	public void testParse5() {
		method_name = "testParse5";
		
		String arg = "aaa,bbb,ccc\",";
		List<String> expect = new ArrayList<String>();
		expect.add("aaa");
		expect.add("bbb");
		expect.add("ccc\"");
		expect.add("");
		
		try {
			List<String> l = parser.parse(arg);
			
			assertEquals(expect, l);
		} catch (CSVParseException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

	@Test
	public void testParse6() {
		method_name = "testParse6";
		
		String arg = " aaa ,bbb,\"12, 345\",ccc\"\",あいうえお999,,\"\"\"\"\"\",abc\"\"def,ghk,\"aaa,123\",\"999,999,999\"";
		List<String> expect = new ArrayList<String>();
		expect.add("aaa");
		expect.add("bbb");
		expect.add("12, 345");
		expect.add("ccc\"\"");
		expect.add("あいうえお999");
		expect.add("");
		expect.add("\"\"");
		expect.add("abc\"\"def");
		expect.add("ghk");
		expect.add("aaa,123");
		expect.add("999,999,999");
		
		try {
			List<String> l = parser.parse(arg);
			
			assertEquals(expect, l);
		} catch (CSVParseException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

	
	public void print(String str) {
		System.out.println(str);
	}
}
