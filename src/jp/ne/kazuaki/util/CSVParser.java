/**
 * 
 */
package jp.ne.kazuaki.util;

import java.util.ArrayList;
import java.util.List;

/**
 * CSV����͂���N���X
 */
public class CSVParser {
	private final Character COMMA = ',';
	private final Character DOUBLE_QUOTE = '"';
	
	public CSVParser() {
		super();
	}

	/**
	 * CSV�̕��������͂��A���X�g�ŕԂ��܂�
	 * ��͉\�ȕ�����͈ȉ��̂Ƃ���
	 *   �P�D�J���}��؂�
	 *   �Q�D������ɉ��s�͊܂܂Ȃ�
	 *   �R�D�J���}����؂�łȂ�������Ƃ��Ĉ����ꍇ�A�v�f���u"�v�_�u���N�H�[�e�[�V�����ň͂ށB�i�_�u���N�H�[�e�[�V���������������̂�Ԃ��܂�)
	 *   �S�D�_�u���N�H�[�e�[�V�����ň͂܂ꂽ�v�f���Ƀ_�u���N�H�[�e�[�V�������܂߂�ꍇ�A""�ŃG�X�P�[�v����B
	 * @param str ��͂��镶����
	 * @return ��͌�̃��X�g
	 * @throws CSVParseException ��͕s�\�ȏꍇException(�_�u���N�H�[�e�[�V�����������Ă��Ȃ��ꍇ)
	 */
	public List<String> parse(String str) throws CSVParseException {
		if (str == null || str.isEmpty()) { return null;}
		
		return parseList(str);
	}
	
	/**
	 * ���������͂��AList�`���ɂ���B
	 * @param str ��͂��镶����
	 * @return ��͌��List
	 */
	private List<String> parseList(String str) throws CSVParseException {
		List<String> result = new ArrayList<String>();
		Boolean isClose = false;
		Boolean isDoubleQuoteMode = false;	// �_�u���N�H�[�e�[�V�������[�h
		int numOfDoubleQuote = 0;			// �_�u���N�H�[�e�[�V�����̌�
		Character c = null;
		StringBuffer tmp = new StringBuffer();
		
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			
			if (c == DOUBLE_QUOTE) {
				numOfDoubleQuote++;
				// ������A��
				tmp.append(String.valueOf(c));
				
				// close��ԂŃ_�u���N�H�[�e�[�V�������_�u���N�H�[�e�[�V�������[�h
				if (isClose) {
					isDoubleQuoteMode = true;
					
					// open
					isClose = false;
				}	
			} else if (c == COMMA) {
				if (isDoubleQuoteMode) {
					// �_�u���N�H�[�g�̐���������close
					if (numOfDoubleQuote % 2 == 0) {
						isClose = true;
					}
				} else {
					isClose = true;
				}
				
				if (isClose) {
					// �v�f�ǉ����ĐV�����v�f��
					result.add(convert(tmp.toString()));
					
					// ������
					numOfDoubleQuote = 0;
					isDoubleQuoteMode = false;
					tmp = new StringBuffer();
				} else {
					// ������A��
					tmp.append(String.valueOf(c));
				}
			} else {
				// open
				isClose = false;
				
				// ������A��
				tmp.append(String.valueOf(c));
			}		
		}
		
		if (str.length() != 0) {
			// �_�u���N�H�[�g���[�h�ōŌオ�_�u���N�H�[�g�ŕ����Ă��Ȃ��ꍇ�G���[
			if (isDoubleQuoteMode && numOfDoubleQuote % 2 != 0) {
				throw new CSVParseException("CSV�̉�͒��ɃG���[���������܂����B�_�u���N�H�[�g�ŕ����Ă��܂���B");
			}
			// �Ō�̗v�f�ǉ�
			result.add(convert(tmp.toString()));
		}
		
		return result;
	}
	
	/**
	 * ������̕ϊ��������s��
	 * �E�O��̋󔒂�����
	 * �E�O��̃_�u���N�H�[�e�[�V����������
	 * �E�O�オ�_�u���N�H�[�e�[�V�����ň͂܂�Ă���ꍇ�́A�v�f���̂Q�d�̃_�u���N�H�[�e�[�V������1�ɂ���B
	 * ��) "aaa""aaa"��aaa"aaa
	 *    �������A�O�オ�_�u���N�H�[�e�[�V�����ň͂܂�Ă��Ȃ��ꍇ�́A�������Ȃ��B
	 *    ��) aaa""aaa��aaa""aaa
	 * @param str �ϊ����镶����
	 * @return �ϊ���̕�����
	 */
	private String convert(String str) {
		String r = str.trim();
		final String doubleQuote = String.valueOf(DOUBLE_QUOTE);
		if (str.startsWith(doubleQuote) && str.endsWith(doubleQuote)) {
			r = str.substring(1, str.length() -1);
			
			// �Q�d�̃_�u���N�H�[�e�[�V������1�ɒu��
			r = r.replaceAll(doubleQuote.concat(doubleQuote), doubleQuote);
		}
		
		return r;
	}
}
